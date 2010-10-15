package com.codeforces.graygoose.page.web;

import org.nocturne.link.Link;
import com.google.inject.Inject;
import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.dto.AlertDto;

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
        put("alerts", AlertDto.getWrappedAlertsList(alertDao.findAll()));
    }
}
