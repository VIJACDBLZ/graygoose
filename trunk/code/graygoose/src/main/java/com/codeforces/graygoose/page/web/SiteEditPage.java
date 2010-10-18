package com.codeforces.graygoose.page.web;

import com.codeforces.graygoose.frame.SiteEditOrAddFrame;
import com.google.inject.Inject;
import org.nocturne.annotation.Parameter;
import org.nocturne.link.Link;

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