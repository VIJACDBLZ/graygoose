package com.codeforces.graygoose.page.web;

import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.util.DateFormatter;
import com.google.inject.Inject;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModelException;
import org.nocturne.link.Link;

@Link("sites")
public class SitesPage extends WebPage {
    @Inject
    private SiteDao siteDao;

    private final DateFormatter dateFormatter = new DateFormatter();

    @Override
    public String getTitle() {
        return $("Sites");
    }

    @Override
    public void action() {
        put("sites", siteDao.findAll());
        put("dateFormatter", dateFormatter);
    }
}