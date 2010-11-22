package com.codeforces.graygoose.page;

import com.codeforces.graygoose.frame.MessageBoxFrame;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;
import org.apache.log4j.Logger;

import java.util.Date;

public abstract class UserPage extends ApplicationPage {
    private static final Logger logger = Logger.getLogger(UserPage.class);

    @Inject
    private UserService userService;

    protected UserService getUserService() {
        return userService;
    }

    protected User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @Override
    public void initializeAction() {
        super.initializeAction();

        if (getCurrentUser() == null) {
            logger.info("User is not logged in, redirecting ...");
            abortWithRedirect(userService.createLoginURL(getRequest().getRequestURI()));
        }

        put("serverTime", new Date());
    }

    protected void setMessage(String message) {
        putSession(MessageBoxFrame.MESSAGE_BOX_TEXT, message);
    }
}
