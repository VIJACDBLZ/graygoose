package com.codeforces.graygoose.page;

import com.codeforces.graygoose.frame.MessageBoxFrame;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;
import org.nocturne.main.ApplicationContext;
import org.nocturne.main.Page;

public abstract class ApplicationPage extends Page {
    @Inject
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @Override
    public void initializeAction() {
        getTemplateEngineConfiguration().setNumberFormat("0.######");
        getTemplateEngineConfiguration().setLocale(ApplicationContext.getInstance().getLocale());
        getTemplateEngineConfiguration().setEncoding(ApplicationContext.getInstance().getLocale(), "UTF-8");

        if (getCurrentUser() == null) {
            abortWithRedirect(getUserService().createLoginURL(getRequest().getRequestURI()));
        }
    }

    public void setMessage(String message) {
        putSession(MessageBoxFrame.MESSAGE_BOX_TEXT, message);
    }
}
