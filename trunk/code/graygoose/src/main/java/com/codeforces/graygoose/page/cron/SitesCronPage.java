package com.codeforces.graygoose.page.cron;

import com.codeforces.graygoose.dao.*;
import com.codeforces.graygoose.util.SiteCheckingUtil;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.link.Link;

@Link("cron/sites")
public class SitesCronPage extends CronPage {
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
    public void onCheckSites() {
        SiteCheckingUtil.checkSites(siteDao, ruleDao, alertDao,
                ruleAlertRelationDao, ruleCheckEventDao, alertTriggerEventDao);
    }

    @Override
    public void action() {
        // No operations.
    }
}