package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.*;
import com.codeforces.graygoose.model.*;
import com.codeforces.graygoose.util.*;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.link.Link;

import java.io.IOException;
import java.util.*;


@Link("data/checkSites")
public class SiteCheckingDataPage extends DataPage {
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

    @Override
    public void initializeAction() {
        super.initializeAction();
    }

    @Action("checkSites")
    public synchronized void onCheckSites() {
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
                List<RuleCheckEvent> ruleCheckEventsForRescanInterval =
                        ruleCheckEventDao.findByRuleForPeriod(
                                rule, currentTimeMillis - site.getRescanPeriodSeconds() * 1000, currentTimeMillis);
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

        if (sitesToRescan.size() == 0) {
            return;
        }

        //Enqueue pending rules for sites
        for (Site site : sitesToRescan) {
            for (Rule rule : rulesBySiteId.get(site.getId())) {
                long ruleId = rule.getId();

                RuleCheckEvent ruleCheckEvent = new RuleCheckEvent(ruleId);
                ruleCheckEventDao.insert(ruleCheckEvent);
                ruleCheckEventByRuleId.put(ruleId, ruleCheckEvent);
            }
        }

        //Scanning sites and committing rule check events by setting check time,
        //status (SUCCEEDED or FAILED) and description
        for (Site site : sitesToRescan) {
            try {
                ResponseChecker.Response response = FetchUtil.fetchUrl(site.getUrl());

                for (Rule rule : rulesBySiteId.get(site.getId())) {
                    long ruleId = rule.getId();

                    RuleCheckEvent ruleCheckEvent = ruleCheckEventByRuleId.get(ruleId);
                    String errorMessage = ResponseChecker.getErrorMessage(response, rule);

                    ruleCheckEvent.setCheckTime(new Date());
                    if (errorMessage == null) {
                        ruleCheckEvent.setStatus(RuleCheckEvent.Status.SUCCEEDED);
                        RuleFailStatistics.resetFailCount(ruleId);
                    } else {
                        ruleCheckEvent.setStatus(RuleCheckEvent.Status.FAILED);
                        ruleCheckEvent.setDesription(errorMessage);
                        RuleFailStatistics.increaseRuleFailCount(ruleId);
                    }
                }
            } catch (IOException e) {
                //Setting status FAILED to all check events for site if fetching has been failed
                for (Rule rule : rulesBySiteId.get(site.getId())) {
                    long ruleId = rule.getId();

                    RuleCheckEvent ruleCheckEvent = ruleCheckEventByRuleId.get(rule.getId());
                    ruleCheckEvent.setCheckTime(new Date());
                    ruleCheckEvent.setStatus(RuleCheckEvent.Status.FAILED);
                    ruleCheckEvent.setDesription("Site " + site.getUrl() + " fetch exception: " + e.getMessage());
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
                    if ("E-mail".equals(alert.getType())) {
                        if (!MailUtil.sendMail(alert.getEmail(), "GrayGoose alert: " + alert.getName(), "")) {
                            throw new RuntimeException($("E-mail was not sent for an unknown reason."));
                        }
                    } else if ("Google calendar event".equals(alert.getType())) {
                        SmsUtil.send("GrayGoose alert: " + ruleCheckEvent.getDesription(), "",
                                alert.getEmail(), alert.getPassword());
                    } else {
                        throw new UnsupportedOperationException($("Unsupported alert type."));
                    }

                    alertTriggerEventDao.insert(new AlertTriggerEvent(alert.getId(), ruleCheckEvent.getId()));
                } catch (Exception e) {
                    //No actions needed. In case of exception alert trigger event has not been created
                    //and alert shall be executed next time.
                }
            }
        }
    }

    @Action("fetch")
    public void onFetch() {
        try {
            ResponseChecker.Response response = FetchUtil.fetchUrl(getString("url"));

            put("responseCode", response.getCode());
            put("responseText", response.getText());

            put("success", true);
        } catch (IOException e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error", "responseCode", "responseText");
    }

    @Action("checkRule")
    public void onCheckRule() {
        try {
            ResponseChecker.Response response = new ResponseChecker.Response(
                    getString("url"), getInteger("responseCode"), getString("responseText"));

            String errorMessage = ResponseChecker.getErrorMessage(response, ruleDao.find(getLong("ruleId")));

            if (errorMessage == null) {
                put("success", true);
            } else {
                put("error", errorMessage);
            }
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Override
    public void action() {
        // No operations.
    }
}