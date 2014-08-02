package com.codeforces.graygoose.web.page;

import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.util.DateFormatter;
import com.google.inject.Inject;
import org.nocturne.link.Link;

@Link("sites")
public class SitesPage extends WebPage {
    @Inject
    private SiteDao siteDao;

    @Override
    protected String getTitle() {
        return $("Sites");
    }

    @Override
    public void action() {
        put("sites", siteDao.findAll());
        put("dateFormatter", new DateFormatter());
    }
}
