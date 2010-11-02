package com.codeforces.graygoose.util;

import com.codeforces.graygoose.dao.*;
import com.codeforces.graygoose.model.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;

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
        long currentTimeMillis = System.currentTimeMillis();

        List<Site> allSites = siteDao.findAll();
        List<Site> sitesToRescan = new ArrayList<Site>();
        Map<Long, List<Rule>> rulesBySiteId = new HashMap<Long, List<Rule>>();
        Map<Long, RuleCheckEvent> ruleCheckEventByRuleId = new HashMap<Long, RuleCheckEvent>();

        //Finding sites to rescan and initializing data structures
        for (Site site : allSites) {
            long siteId = site.getId();

            //Finding rules for current site
            List<Rule> rules = ruleDao.findBySite(siteId);

            //Checking if no rescan needed for site
            boolean rescanNeeded = true;
            for (Rule rule : rules) {
                //Finding check events for current rule
                // TODO: Make it faster!
                List<RuleCheckEvent> ruleCheckEventsForRescanInterval =
                        ruleCheckEventDao.findByRuleForPeriod(
                                rule,
                                currentTimeMillis - site.getRescanPeriodSeconds() * 1000,
                                currentTimeMillis);
                if (ruleCheckEventsForRescanInterval.size() > 0) {
                    rescanNeeded = false;
                    break;
                }
            }

            if (rescanNeeded) {
                //Adding site to rescan list
                sitesToRescan.add(site);

                //Storing rules for current site in the map
                rulesBySiteId.put(siteId, rules);
            }
        }

        if (sitesToRescan.size() != 0) {
            processSitesToRescan(sitesToRescan, rulesBySiteId, ruleCheckEventByRuleId);
        }
    }

    private void processSitesToRescan(List<Site> sitesToRescan, Map<Long, List<Rule>> rulesBySiteId, Map<Long, RuleCheckEvent> ruleCheckEventByRuleId) {
        enqueuePendingRules(sitesToRescan, rulesBySiteId, ruleCheckEventByRuleId, ruleCheckEventDao);

        //Scanning sites and committing rule check events by setting check time,
        //status (SUCCEEDED or FAILED) and description
        for (Site site : sitesToRescan) {
            ResponseChecker.Response response = UrlUtil.fetchUrl(site.getUrl());

            for (Rule rule : rulesBySiteId.get(site.getId())) {
                long ruleId = rule.getId();

                RuleCheckEvent ruleCheckEvent = ruleCheckEventByRuleId.get(ruleId);
                String errorMessage = ResponseChecker.getErrorMessage(response, rule);

                ruleCheckEvent.setCheckTime(new Date());
                if (errorMessage == null) {
                    ruleCheckEvent.setStatus(RuleCheckEvent.Status.SUCCEEDED);
                    RuleFailStatistics.resetConsecutiveFailCountByRuleId(ruleId);
                } else {
                    ruleCheckEvent.setStatus(RuleCheckEvent.Status.FAILED);
                    ruleCheckEvent.setDesription(errorMessage);
                    RuleFailStatistics.increaseConsecutiveFailCountByRuleId(ruleId);
                }
            }
        }

        //Iterating through rule check events and triggering alert for failed ones
        for (RuleCheckEvent ruleCheckEvent : ruleCheckEventByRuleId.values()) {
            if (ruleCheckEvent.getStatus() == RuleCheckEvent.Status.SUCCEEDED) {
                continue;
            }

            List<Alert> alerts = RuleFailStatistics.getNeededAlerts(ruleCheckEvent.getRuleId(),
                    ruleAlertRelationDao, alertDao, alertTriggerEventDao);

            for (Alert alert : alerts) {
                try {
                    triggerAlert(ruleCheckEvent, alert);
                    alertTriggerEventDao.insert(new AlertTriggerEvent(alert.getId(), ruleCheckEvent.getId()));
                } catch (RuntimeException e) {
                    logger.error("Fail to trigger alert or to save it [alert=" + alert.getName() + "].", e);
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
