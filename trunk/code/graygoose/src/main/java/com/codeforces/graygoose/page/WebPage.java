package com.codeforces.graygoose.page;

import com.google.inject.Inject;
import com.codeforces.graygoose.frame.TopMenuFrame;
import com.codeforces.graygoose.frame.MainMenuFrame;

public abstract class WebPage extends ApplicationPage {
    @Inject
    private TopMenuFrame topMenuFrame;

    @Inject
    private MainMenuFrame mainMenuFrame;

    public abstract String getTitle();

    public void initializeAction() {
        super.initializeAction();

        putGlobal("user", getUser());
        putGlobal("pageTitle", getTitle());

        parse("topMenuFrame", topMenuFrame);
        parse("mainMenuFrame", mainMenuFrame);
    }
}
