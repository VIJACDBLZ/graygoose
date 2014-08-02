package com.codeforces.graygoose.web.page;

import com.codeforces.graygoose.web.frame.AlertEditOrAddFrame;
import com.google.inject.Inject;
import org.nocturne.link.Link;

@Link("alert/add")
public class AlertAddPage extends WebPage {
    @Inject
    private AlertEditOrAddFrame alertEditOrAddFrame;

    @Override
    public String getTitle() {
        return $("Add Alert");
    }

    @Override
    public void action() {
        alertEditOrAddFrame.setupAdd(AlertsPage.class);
        parse("alertAddFrame", alertEditOrAddFrame);
    }
}
