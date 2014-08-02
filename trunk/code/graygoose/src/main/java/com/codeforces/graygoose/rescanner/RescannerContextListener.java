package com.codeforces.graygoose.rescanner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RescannerContextListener implements ServletContextListener {
    private volatile Thread thread = null;
    private volatile boolean alive;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        alive = true;

        if (thread == null) {
            thread = new Thread(new RescannerRunnable(this));
            thread.start();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        alive = false;

        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    public boolean isAlive() {
        return alive;
    }
}
