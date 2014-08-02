package com.codeforces.graygoose.web.page;

public abstract class WebPage extends ApplicationPage {
    protected abstract String getTitle();

    @Override
    public void initializeAction() {
        super.initializeAction();

        handleAccess();

        String root = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().length() - getRequest().getServletPath().length());
        putGlobal("root", root);
        putGlobal("user", getUser());

        put("pageTitle", getTitle());
    }

    private void handleAccess() {
        if (getUser() == null && !hasAnonymousAccess()) {
            abortWithRedirect(EnterPage.class);
        }
    }
}
