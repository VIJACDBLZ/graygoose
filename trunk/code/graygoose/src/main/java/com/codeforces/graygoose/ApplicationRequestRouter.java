package com.codeforces.graygoose;

import com.codeforces.graygoose.page.data.AlertsDataPage;
import com.codeforces.graygoose.page.data.RulesDataPage;
import com.codeforces.graygoose.page.data.SitesDataPage;
import com.codeforces.graygoose.page.data.SiteCheckingDataPage;
import com.codeforces.graygoose.page.web.*;
import org.nocturne.link.Links;
import org.nocturne.main.LinkedRequestRouter;

public class ApplicationRequestRouter extends LinkedRequestRouter {
    static {
        //Web pages
        Links.add(DashboardPage.class);
        Links.add(SitesPage.class);
        Links.add(SiteAddPage.class);
        Links.add(SiteEditPage.class);
        Links.add(AlertsPage.class);
        Links.add(AlertAddPage.class);
        Links.add(AlertEditPage.class);

        //Data pages
        Links.add(SitesDataPage.class);
        Links.add(AlertsDataPage.class);
        Links.add(RulesDataPage.class);
        Links.add(SiteCheckingDataPage.class);
    }
}