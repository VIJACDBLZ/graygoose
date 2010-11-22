package com.codeforces.graygoose.page.cron;

import com.codeforces.graygoose.util.SiteCheckingService;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.link.Link;

@Link("cron/sites")
public class SitesCronPage extends CronPage {
    @Inject
    private SiteCheckingService siteCheckingService;

    @Action("checkSites")
    public void onCheckSites() {
        siteCheckingService.checkSites();
    }

    @Override
    public void action() {
        // No operations.
    }
}