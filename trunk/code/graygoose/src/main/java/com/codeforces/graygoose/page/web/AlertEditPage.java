package com.codeforces.graygoose.page.web;

import com.codeforces.graygoose.frame.AlertEditOrAddFrame;
import com.google.inject.Inject;
import org.nocturne.annotation.Parameter;
import org.nocturne.link.Link;

@Link("editAlert/{id}")
public class AlertEditPage extends WebPage {
    @Parameter
    private long id;

    @Inject
    private AlertEditOrAddFrame alertEditOrAddFrame;

    @Override
    public String getTitle() {
        return $("Edit alert");
    }

    @Override
    public void action() {
        alertEditOrAddFrame.setup(AlertsPage.class);
        parse("alertAddFrame", alertEditOrAddFrame);
    }
}