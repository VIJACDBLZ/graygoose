package com.codeforces.graygoose.rescanner;

import com.codeforces.commons.time.TimeUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RescannerContextListener implements ServletContextListener {
    private volatile Thread thread;
    private volatile RescannerRunnable rescannerRunnable;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (thread == null && rescannerRunnable == null) {
            rescannerRunnable = new RescannerRunnable();
            thread = new Thread(rescannerRunnable);
            thread.start();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (rescannerRunnable != null) {
            rescannerRunnable.stop();
            rescannerRunnable = null;
        }

        if (thread != null) {
            try {
                thread.join(30L * TimeUtil.MILLIS_PER_SECOND);
            } catch (InterruptedException ignored) {
                // No operations.
            }
            if (thread.isAlive()) {
                forceStopThread();
            }
            thread = null;
        }
    }

    @SuppressWarnings({"CallToThreadStopSuspendOrResumeManager", "deprecation"})
    private void forceStopThread() {
        thread.stop();
    }
}
