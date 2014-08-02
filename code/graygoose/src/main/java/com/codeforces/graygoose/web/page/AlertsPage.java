package com.codeforces.graygoose.web.page;

import com.codeforces.graygoose.dao.AlertDao;
import com.google.inject.Inject;
import org.nocturne.link.Link;

@Link("alerts")
public class AlertsPage extends WebPage {
    @Inject
    private AlertDao alertDao;

    @Override
    protected String getTitle() {
        return $("Alerts");
    }

    @Override
    public void action() {
        put("alerts", alertDao.findAll());
    }
}
