package com.codeforces.graygoose.web.page;

import com.codeforces.graygoose.web.frame.SiteEditOrAddFrame;
import com.google.inject.Inject;
import org.nocturne.link.Link;

@Link("site/edit")
public class SiteEditPage extends WebPage {
    @Inject
    private SiteEditOrAddFrame siteEditOrAddFrame;

    @Override
    protected String getTitle() {
        return $("Edit Site");
    }

    @Override
    public void action() {
        siteEditOrAddFrame.setupAdd(SitesPage.class);
        parse("siteEditFrame", siteEditOrAddFrame);
    }
}
