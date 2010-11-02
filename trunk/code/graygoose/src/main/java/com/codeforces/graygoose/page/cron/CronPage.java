package com.codeforces.graygoose.page.cron;

import com.codeforces.graygoose.page.ApplicationPage;

import javax.servlet.http.HttpServletResponse;

public abstract class CronPage extends ApplicationPage {
    @Override
    public void initializeAction() {
        super.initializeAction();
        skipTemplate();

        if (getRequest().getHeader("X-AppEngine-Cron") == null) {
            abortWithError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
