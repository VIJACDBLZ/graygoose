package com.codeforces.graygoose;

import com.codeforces.graygoose.web.page.*;
import com.codeforces.graygoose.web.page.data.*;
import org.nocturne.link.Links;
import org.nocturne.main.LinkedRequestRouter;

public class ApplicationRequestRouter extends LinkedRequestRouter {
    static {
        Links.add(EnterPage.class);
        Links.add(LogoutPage.class);
        Links.add(DashboardPage.class);
        Links.add(RegisterPage.class);
        Links.add(SitesPage.class);
        Links.add(SiteAddPage.class);
        Links.add(SiteEditPage.class);
        Links.add(SitesDataPage.class);
        Links.add(RulesDataPage.class);
        Links.add(RuleAlertRelationsDataPage.class);
        Links.add(AlertsPage.class);
        Links.add(AlertAddPage.class);
        Links.add(AlertEditPage.class);
        Links.add(AlertsDataPage.class);
        Links.add(LogsPage.class);
        Links.add(ResponsesDataPage.class);
    }
}
