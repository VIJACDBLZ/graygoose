package com.codeforces.graygoose.page;

import org.nocturne.link.Link;

@Link("")
public class DashboardPage extends WebPage {
    @Override
    public String getTitle() {
        return $("Dashboard");
    }

    @Override
    public void action() {
        System.out.println("123");
    }
}
