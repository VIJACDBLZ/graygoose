package com.codeforces.graygoose;

import com.codeforces.graygoose.page.cron.EventsCronPage;
import com.codeforces.graygoose.page.cron.SitesCronPage;
import com.codeforces.graygoose.page.data.*;
import com.codeforces.graygoose.page.web.*;
import org.nocturne.link.Links;
import org.nocturne.main.LinkedRequestRouter;

public class ApplicationRequestRouter extends LinkedRequestRouter {
    static {
        //Web pages:
        Links.add(AlertAddPage.class);
        Links.add(AlertEditPage.class);
        Links.add(AlertsPage.class);
        Links.add(DashboardPage.class);
        Links.add(LogsPage.class);
        Links.add(SiteAddPage.class);
        Links.add(SiteEditPage.class);
        Links.add(SitesPage.class);

        //Data pages:
        Links.add(SitesDataPage.class);
        Links.add(AlertsDataPage.class);
        Links.add(RulesDataPage.class);
        Links.add(RuleAlertRelationsDataPage.class);
        Links.add(ResponsesDataPage.class);

        //Cron pages:
        Links.add(SitesCronPage.class);
        Links.add(EventsCronPage.class);
    }
}