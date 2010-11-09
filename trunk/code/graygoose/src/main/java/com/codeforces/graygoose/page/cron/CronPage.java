package com.codeforces.graygoose.page.cron;

import com.codeforces.graygoose.page.ApplicationPage;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;

public abstract class CronPage extends ApplicationPage {
    private static final Logger logger = Logger.getLogger(CronPage.class);

    @Override
    public void initializeAction() {
        super.initializeAction();
        skipTemplate();

        if (getRequest().getHeader("X-AppEngine-Cron") == null) {
            logger.info("This page restricted to CRON-jobs only, aborting request ...");
            abortWithError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
