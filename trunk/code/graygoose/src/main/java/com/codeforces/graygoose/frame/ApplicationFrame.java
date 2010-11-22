package com.codeforces.graygoose.frame;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;
import org.nocturne.main.Frame;

public abstract class ApplicationFrame extends Frame {
    @Inject
    private UserService userService;

    protected UserService getUserService() {
        return userService;
    }

    public User getUser() {
        return userService.getCurrentUser();
    }

    void setMessage(String message) {
        putSession(MessageBoxFrame.MESSAGE_BOX_TEXT, message);
    }
}
