package com.codeforces.graygoose.page;

import org.nocturne.main.Page;
import com.google.inject.Inject;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.User;
import com.codeforces.graygoose.frame.MessageBoxFrame;

public abstract class ApplicationPage extends Page {
    @Inject
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public User getUser() {
        return userService.getCurrentUser();
    }

    @Override
    public void initializeAction() {
        if (getUser() == null) {
            abortWithRedirect(getUserService().createLoginURL(getRequest().getRequestURI()));
        }
    }

    public void setMessage(String message) {
        putSession(MessageBoxFrame.MESSAGE_BOX_TEXT, message);
    }
}
