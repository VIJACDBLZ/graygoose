package com.codeforces.graygoose.page.web;

import com.codeforces.graygoose.dao.AlertDao;
import com.google.inject.Inject;
import org.nocturne.link.Link;

@Link("alerts")
public class AlertsPage extends WebPage {
    @Inject
    private AlertDao alertDao;

    @Override
    public String getTitle() {
        return $("Alerts");
    }

    @Override
    public void action() {
        put("alerts", alertDao.findAll());
    }
}
