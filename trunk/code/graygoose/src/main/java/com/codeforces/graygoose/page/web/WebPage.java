package com.codeforces.graygoose.page.web;

import com.google.inject.Inject;
import com.codeforces.graygoose.frame.TopMenuFrame;
import com.codeforces.graygoose.frame.MainMenuFrame;
import com.codeforces.graygoose.frame.MessageBoxFrame;
import com.codeforces.graygoose.page.ApplicationPage;

public abstract class WebPage extends ApplicationPage {
    @Inject
    private TopMenuFrame topMenuFrame;

    @Inject
    private MainMenuFrame mainMenuFrame;

    @Inject
    private MessageBoxFrame messageBoxFrame;

    public abstract String getTitle();

    public void initializeAction() {
        super.initializeAction();

        putGlobal("user", getUser());
        putGlobal("pageTitle", getTitle());

        parse("topMenuFrame", topMenuFrame);
        parse("mainMenuFrame", mainMenuFrame);
        parse("messageBoxFrame", messageBoxFrame);
    }
}
