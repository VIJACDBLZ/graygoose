package com.codeforces.graygoose;

import com.codeforces.graygoose.page.DashboardPage;
import com.codeforces.graygoose.page.SitesPage;
import org.nocturne.main.LinkedRequestRouter;
import org.nocturne.link.Links;

public class ApplicationRequestRouter extends LinkedRequestRouter {
    static {
        Links.add(DashboardPage.class);
        Links.add(SitesPage.class);
    }
}
