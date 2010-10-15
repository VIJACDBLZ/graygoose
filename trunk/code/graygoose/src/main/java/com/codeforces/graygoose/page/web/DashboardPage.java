package com.codeforces.graygoose.page.web;

import org.nocturne.link.Link;

@Link("")
public class DashboardPage extends WebPage {
    @Override
    public String getTitle() {
        return $("Dashboard");
    }

    @Override
    public void action() {
        //
    }
}
