package com.codeforces.graygoose.web.page;

import org.nocturne.link.Link;

@Link("logout")
public class LogoutPage extends WebPage {
    @Override
    protected String getTitle() {
        return $("Logout");
    }

    @Override
    public void action() {
        logout();
        abortWithRedirect(EnterPage.class);
    }

    @Override
    public boolean hasAnonymousAccess() {
        return true;
    }
}
