package com.codeforces.graygoose;

import com.codeforces.graygoose.page.web.DashboardPage;
import com.codeforces.graygoose.page.web.SitesPage;
import com.codeforces.graygoose.page.web.SiteAddPage;
import com.codeforces.graygoose.page.web.SiteEditPage;
import com.codeforces.graygoose.page.data.SitesDataPage;
import org.nocturne.main.LinkedRequestRouter;
import org.nocturne.link.Links;

public class ApplicationRequestRouter extends LinkedRequestRouter {
    static {
        Links.add(DashboardPage.class);
        Links.add(SitesPage.class);
        Links.add(SiteAddPage.class);
        Links.add(SiteEditPage.class);

        Links.add(SitesDataPage.class);
    }
}
