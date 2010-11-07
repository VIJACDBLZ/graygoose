package com.codeforces.graygoose.util;

import com.codeforces.graygoose.dao.*;
import com.codeforces.graygoose.model.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import org.nocturne.util.StringUtil;

import javax.mail.MessagingException;
import java.util.*;

@Singleton
public class SiteCheckingService {
    private static final Logger logger = Logger.getLogger(SiteCheckingService.class);

    @Inject
    private SiteDao siteDao;

    @Inject
    private RuleDao ruleDao;

    @Inject
    private AlertDao alertDao;

    @Inject
    private RuleAlertRelationDao ruleAlertRelationDao;

    @Inject
    private RuleCheckEventDao ruleCheckEventDao;

    @Inject
    private AlertTriggerEventDao alertTriggerEventDao;

    public synchronized void checkSites() {
        logger.info("Start site checking procedures ...");
        long currentTimeMillis = System.currentTimeMillis();

        logger.info("Retrieve list of the sites from data storage.");
        List<Site> allSites = siteDao.findAll();

        ArrayList<Site> sitesToRescan = new ArrayList<Site>();
        Map<Long, List<Rule>> rulesBySiteId = new HashMap<Long, List<Rule>>();
        Map<Long, RuleCheckEvent> ruleCheckEventByRuleId = new HashMap<Long, RuleCheckEvent>();

        logger.info("Fill list of the sites to rescan ...");
        fillSitesToRescan(currentTimeMillis, allSites, sitesToRescan, rulesBySiteId);

        if (sitesToRescan.size() == 0) {
            logger.info("There is no sites to rescan at this moment.");
            return;
        }

        logger.info("Start to rescan sites ...");
        processSitesToRescan(sitesToRescan, rulesBySiteId, ruleCheckEventByRuleId);
    }

    private void fillSitesToRescan(long currentTimeMillis, List<Site> allSites,
                                   List<Site> sitesToRescan, Map<Long, List<Rule>> rulesBySiteId) {
        //Find sites to rescan and initialize data structures
        for (Site site : allSites) {
            long siteId = site.getId();
            long siteRescanPeriodLowerBoundMillis = currentTimeMillis - site.getRescanPeriodSeconds() * 1000L;

            //Find rules for current site
            List<Rule> rules = ruleDao.findBySite(siteId);

            boolean rescanNeeded = rules.size() > 0;

            //Check if no rescan needed for site
            for (Rule rule : rules) {
                //Find check events for current rule
                List<RuleCheckEvent> ruleCheckEventsForRescanInterval =
                        ruleCheckEventDao.findByRuleForPeriod(
                                rule.getId(), siteRescanPeriodLowerBoundMillis, currentTimeMillis);
                if (ruleCheckEventsForRescanInterval.size() > 0) {
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

    private void processSitesToRescan(ArrayList<Site> sitesToRescan, Map<Long, List<Rule>> rulesBySiteId,
                                      Map<Long, RuleCheckEvent> ruleCheckEventByRuleId) {
        enqueuePendingRules(sitesToRescan, rulesBySiteId, ruleCheckEventByRuleId, ruleCheckEventDao);

        final int siteCount = sitesToRescan.size();
        ArrayList<String> urlStrings = new ArrayList<String>(siteCount);

        //Start to fetch URLs
        for (int siteIndex = 0; siteIndex < siteCount; ++siteIndex) {
            urlStrings.add(sitesToRescan.get(siteIndex).getUrl());
        }

        List<ResponseCheckingService.Response> responses = UrlUtil.fetchUrls(urlStrings);

        //Scan sites and commit rule check events by setting check time,
        //status (SUCCEEDED or FAILED) and description
        for (int siteIndex = 0; siteIndex < siteCount; ++siteIndex) {
            Site site = sitesToRescan.get(siteIndex);
            ResponseCheckingService.Response response = responses.get(siteIndex);

            for (Rule rule : rulesBySiteId.get(site.getId())) {
                long ruleId = rule.getId();

                RuleCheckEvent ruleCheckEvent = ruleCheckEventByRuleId.get(ruleId);
                String errorMessage = response.getCode() == -1 && !StringUtil.isEmptyOrNull(response.getText()) ?
                        response.getText() :
                        ResponseCheckingService.getErrorMessage(response, rule);

                ruleCheckEvent.setCheckTime(new Date());
                if (errorMessage == null) {
                    ruleCheckEvent.setStatus(RuleCheckEvent.Status.SUCCEEDED);
                    RuleFailStatistics.resetConsecutiveFailCountByRuleId(ruleId);
                } else {
                    logger.warn("Rule check has been failed: " + errorMessage + ".");

                    ruleCheckEvent.setStatus(RuleCheckEvent.Status.FAILED);
                    ruleCheckEvent.setDesription(errorMessage);
                    RuleFailStatistics.increaseConsecutiveFailCountByRuleId(ruleId);
                }
            }
        }

        //Iterate through rule check events and trigger alert for failed ones
        for (RuleCheckEvent ruleCheckEvent : ruleCheckEventByRuleId.values()) {
            if (ruleCheckEvent.getStatus() == RuleCheckEvent.Status.SUCCEEDED) {
                continue;
            }

            List<Alert> triggeredAlerts = RuleFailStatistics.getTriggeredAlerts(
                    ruleCheckEvent.getRuleId(), ruleAlertRelationDao, alertDao, alertTriggerEventDao);

            for (Alert triggeredAlert : triggeredAlerts) {
                try {
                    logger.warn("Trigger alert [" + triggeredAlert.getName() + "].");
                    triggerAlert(ruleCheckEvent, triggeredAlert);
                    alertTriggerEventDao.insert(new AlertTriggerEvent(triggeredAlert.getId(), ruleCheckEvent.getId()));
                } catch (RuntimeException e) {
                    logger.error("Failed to trigger alert [" + triggeredAlert.getName() + "] or to save trigger event.", e);
                }
            }
        }
    }

    private void triggerAlert(RuleCheckEvent ruleCheckEvent, Alert alert) {
        if ("E-mail".equals(alert.getType())) {
            try {
                MailUtil.sendMail(alert.getEmail(), "GrayGoose alert: " + alert.getName(),
                        ruleCheckEvent.getDesription());
            } catch (MessagingException e) {
                throw new RuntimeException("E-mail was not sent: " + e.getMessage());
            }
        } else if ("Google calendar event".equals(alert.getType())) {
            try {
                SmsUtil.send("GrayGoose alert: " + ruleCheckEvent.getDesription(), "",
                        alert.getEmail(), alert.getPassword());
            } catch (SmsSendException e) {
                throw new RuntimeException("Can't add Google calendar event: " + e.getMessage());
            }
        } else {
            throw new UnsupportedOperationException("Unsupported alert type.");
        }
    }

    private void enqueuePendingRules(
            List<Site> sitesToRescan, Map<Long, List<Rule>> rulesBySiteId,
            Map<Long, RuleCheckEvent> ruleCheckEventByRuleId, RuleCheckEventDao ruleCheckEventDao) {
        for (Site site : sitesToRescan) {
            for (Rule rule : rulesBySiteId.get(site.getId())) {
                long ruleId = rule.getId();

                RuleCheckEvent ruleCheckEvent = new RuleCheckEvent(ruleId);
                ruleCheckEventDao.insert(ruleCheckEvent);
                ruleCheckEventByRuleId.put(ruleId, ruleCheckEvent);
            }
        }
    }
}