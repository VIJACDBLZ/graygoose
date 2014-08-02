package com.codeforces.graygoose.util;

import com.codeforces.graygoose.ApplicationModule;
import com.codeforces.graygoose.dao.*;
import com.codeforces.graygoose.model.*;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import org.nocturne.util.StringUtil;

import javax.mail.MessagingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
public class SiteCheckingService {
    private static final Logger logger = Logger.getLogger(SiteCheckingService.class);
    private static final Object LOCK = new Object();

    @Inject
    private SiteDao siteDao;

    @Inject
    private RuleDao ruleDao;

    @Inject
    private AlertDao alertDao;

    @Inject
    private ResponseDao responseDao;

    @Inject
    private RuleAlertRelationDao ruleAlertRelationDao;

    @Inject
    private RuleCheckEventDao ruleCheckEventDao;

    @Inject
    private AlertTriggerEventDao alertTriggerEventDao;

    public void checkSites() {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        long currentDayMinutes = calendar.get(Calendar.HOUR_OF_DAY) * TimeUnit.HOURS.toMinutes(1)
                + calendar.get(Calendar.MINUTE);

        synchronized (LOCK) {
            logger.info("Start site checking procedures ...");
            logger.info("Retrieve list of the sites from data storage.");
            List<Site> allSites = siteDao.findAll();

            ArrayList<Site> sitesToRescan = new ArrayList<>();
            Map<Long, List<Rule>> rulesBySiteId = new HashMap<>();
            Map<Long, RuleCheckEvent> ruleCheckEventByRuleId = new HashMap<>();

            logger.info("Fill list of the sites to rescan ...");
            fillSitesToRescan(currentTimeMillis, currentDayMinutes, allSites, sitesToRescan, rulesBySiteId);

            if (sitesToRescan.isEmpty()) {
                logger.info("There is no sites to rescan at this moment.");
                return;
            }

            logger.info("Start to rescan sites ...");
            processSitesToRescan(sitesToRescan, rulesBySiteId, ruleCheckEventByRuleId);
        }
    }

    private void fillSitesToRescan(long currentTimeMillis, long currentDayMinutes, List<Site> allSites,
                                   List<Site> sitesToRescan, Map<Long, List<Rule>> rulesBySiteId) {
        //Find sites to rescan and initialize data structures
        for (Site site : allSites) {
            //check if we must not check site now
            if (site.getPauseFromMinute() != null
                    && ((site.getPauseFromMinute() <= currentDayMinutes && currentDayMinutes <= site.getPauseToMinute())
                    || (site.getPauseFromMinute() > site.getPauseToMinute()
                    && (currentDayMinutes <= site.getPauseToMinute()
                    || site.getPauseFromMinute() <= currentDayMinutes)))) {
                continue;
            }

            long siteId = site.getId();
            long siteRescanPeriodLowerBoundMillis = currentTimeMillis - site.getRescanPeriodSeconds() * 1000L;

            //Find rules for current site
            List<Rule> rules = ruleDao.findAllBySite(siteId);

            boolean rescanNeeded = !rules.isEmpty();

            //Check if no rescan needed for site
            for (Rule rule : rules) {
                //Find check events for current rule
                List<RuleCheckEvent> ruleCheckEventsForRescanInterval =
                        ruleCheckEventDao.findAllByRuleForPeriod(
                                rule.getId(), siteRescanPeriodLowerBoundMillis, currentTimeMillis);
                if (!ruleCheckEventsForRescanInterval.isEmpty()) {
                    rescanNeeded = false;
                    break;
                }
            }

            if (rescanNeeded) {
                //Add site to rescan list
                sitesToRescan.add(site);

                //Store rules for current site in the map
                rulesBySiteId.put(siteId, rules);
            }
        }
    }

    @SuppressWarnings({"OverlyLongMethod", "ForLoopReplaceableByForEach"})
    private void processSitesToRescan(List<Site> sitesToRescan, Map<Long, List<Rule>> rulesBySiteId,
                                      Map<Long, RuleCheckEvent> ruleCheckEventByRuleId) {
        enqueuePendingRuleCheckEvents(sitesToRescan, rulesBySiteId, ruleCheckEventByRuleId);
        int siteCount = sitesToRescan.size();
        List<String> urlStrings = new ArrayList<>(siteCount);

        //Start to fetch URLs
        for (int siteIndex = 0; siteIndex < siteCount; ++siteIndex) {
            urlStrings.add(sitesToRescan.get(siteIndex).getUrl());
        }

        List<Response> responses = UrlUtil.fetchUrls(urlStrings, 3);

        //Scan sites and commit rule check events by setting check time,
        //status (SUCCEEDED or FAILED) and description
        for (int siteIndex = 0; siteIndex < siteCount; ++siteIndex) {
            Site site = sitesToRescan.get(siteIndex);
            Response response = responses.get(siteIndex);

            for (Rule rule : rulesBySiteId.get(site.getId())) {
                long ruleId = rule.getId();

                RuleCheckEvent ruleCheckEvent = ruleCheckEventByRuleId.get(ruleId);
                String errorMessage =
                        response.getCode() == -1 && !StringUtil.isEmptyOrNull(response.getText()) ?
                                site.getName() + ": " + response.getText() :
                                ResponseCheckingService.getErrorMessageOrNull(site.getName(), response, rule);

                ruleCheckEvent.setCheckTime(new Date());
                if (errorMessage == null) {
                    ruleCheckEvent.setStatus(RuleCheckEvent.Status.SUCCEEDED);
                    RuleFailStatistics.resetConsecutiveFailCountByRuleId(ruleId);
                } else {
                    logger.warn("Rule check has been failed: [" + errorMessage + "].");

                    ruleCheckEvent.setStatus(RuleCheckEvent.Status.FAILED);
                    ruleCheckEvent.setDescription(errorMessage);

                    if (response.getId() == 0) {
                        responseDao.insert(response);
                    }

                    ruleCheckEvent.setResponseId(response.getId());
                    RuleFailStatistics.increaseConsecutiveFailCountByRuleId(ruleId);
                }
            }
        }

        //Iterate through rule check events and trigger alert for failed ones
        for (RuleCheckEvent ruleCheckEvent : ruleCheckEventByRuleId.values()) {
            ruleCheckEventDao.update(ruleCheckEvent);

            if (ruleCheckEvent.getStatus() == RuleCheckEvent.Status.FAILED) {
                List<Alert> triggeredAlerts = RuleFailStatistics.getTriggeredAlerts(
                        ruleCheckEvent.getRuleId(), ruleAlertRelationDao, alertDao, alertTriggerEventDao);

                for (Alert triggeredAlert : triggeredAlerts) {
                    try {
                        logger.warn("Trigger alert [" + triggeredAlert.getName() + "].");
                        triggerAlert(ruleCheckEvent, triggeredAlert);
                        alertTriggerEventDao.insert(AlertTriggerEvent.newAlertTriggerEvent(triggeredAlert.getId(), ruleCheckEvent.getId()));
                    } catch (RuntimeException e) {
                        logger.error("Failed to trigger alert [" + triggeredAlert.getName() + "] or to save trigger event.", e);
                    }
                }
            }
        }
    }

    private static void triggerAlert(RuleCheckEvent ruleCheckEvent, Alert alert) {
        if ("E-mail".equals(alert.getType())) {
            try {
                MailUtil.sendMail(alert.getEmail(), "GrayGoose alert: " + alert.getName(),
                        ruleCheckEvent.getDescription());
            } catch (MessagingException e) {
                throw new RuntimeException("E-mail was not sent: " + e.getMessage());
            }
        } else if ("Google calendar event".equals(alert.getType())) {
            try {
                SmsUtil.send("GG " + ruleCheckEvent.getDescription(),
                        alert.getEmail(), alert.getPassword());
            } catch (SmsSendException e) {
                throw new RuntimeException("Can't add Google calendar event: " + e.getMessage());
            }
        } else {
            throw new UnsupportedOperationException("Unsupported alert type.");
        }
    }

    private void enqueuePendingRuleCheckEvents(List<Site> sitesToRescan, Map<Long, List<Rule>> rulesBySiteId,
                                               Map<Long, RuleCheckEvent> ruleCheckEventByRuleId) {
        for (Site site : sitesToRescan) {
            long siteId = site.getId();

            for (Rule rule : rulesBySiteId.get(siteId)) {
                long ruleId = rule.getId();

                RuleCheckEvent ruleCheckEvent = RuleCheckEvent.newRuleCheckEvent(ruleId, siteId);
                ruleCheckEventDao.insert(ruleCheckEvent);
                ruleCheckEventByRuleId.put(ruleId, ruleCheckEvent);
            }
        }
    }

    public static SiteCheckingService newSiteCheckingService() {
        return Guice.createInjector(new ApplicationModule()).getInstance(SiteCheckingService.class);
    }
}
