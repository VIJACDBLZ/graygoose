package com.codeforces.graygoose.frame;

import org.nocturne.main.Frame;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;

public abstract class ApplicationFrame extends Frame {
    @Inject
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public User getUser() {
        return userService.getCurrentUser();
    }
}
