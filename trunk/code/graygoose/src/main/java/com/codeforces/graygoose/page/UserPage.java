package com.codeforces.graygoose.page;

import com.codeforces.graygoose.frame.MessageBoxFrame;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;

public abstract class UserPage extends ApplicationPage {
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
        super.initializeAction();

        if (getCurrentUser() == null) {
            abortWithRedirect(getUserService().createLoginURL(getRequest().getRequestURI()));
        }
    }

    public void setMessage(String message) {
        putSession(MessageBoxFrame.MESSAGE_BOX_TEXT, message);
    }
}
