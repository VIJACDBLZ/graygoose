package com.codeforces.graygoose.web.page;

import com.codeforces.graygoose.web.frame.SiteEditOrAddFrame;
import com.google.inject.Inject;
import org.nocturne.link.Link;

@Link("site/add")
public class SiteAddPage extends WebPage {
    @Inject
    private SiteEditOrAddFrame siteEditOrAddFrame;

    @Override
    protected String getTitle() {
        return $("Add Site");
    }

    @Override
    public void action() {
        siteEditOrAddFrame.setupAdd(SitesPage.class);
        parse("siteAddFrame", siteEditOrAddFrame);
    }
}
