package com.codeforces.graygoose.page.web;

import org.nocturne.link.Link;
import org.nocturne.annotation.Validate;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.validation.*;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.model.Site;
import com.codeforces.graygoose.frame.SiteEditOrAddFrame;
import com.google.inject.Inject;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.Date;

@Link("editSite/{id}")
public class SiteEditPage extends WebPage {
    @Parameter
    private long id;

    @Inject
    private SiteEditOrAddFrame siteEditOrAddFrame;

    @Override
    public String getTitle() {
        return $("Edit site");
    }

    @Override
    public void action() {
        siteEditOrAddFrame.setup(SitesPage.class);
        parse("siteAddFrame", siteEditOrAddFrame);
    }
}