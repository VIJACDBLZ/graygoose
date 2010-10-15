package com.codeforces.graygoose.page.web;

import com.codeforces.graygoose.frame.AlertEditOrAddFrame;
import com.google.inject.Inject;
import org.nocturne.link.Link;

@Link("addAlert")
public class AlertAddPage extends WebPage {
    @Inject
    private AlertEditOrAddFrame alertEditOrAddFrame;

    @Override
    public String getTitle() {
        return $("Add alert");
    }

    @Override
    public void action() {
        alertEditOrAddFrame.setup(AlertsPage.class);
        parse("alertAddFrame", alertEditOrAddFrame);
    }
}