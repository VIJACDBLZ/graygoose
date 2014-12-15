package com.codeforces.graygoose.rescanner;

import com.codeforces.commons.process.ThreadUtil;
import com.codeforces.commons.time.TimeUtil;
import com.codeforces.graygoose.ApplicationModule;
import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.misc.TimeConstants;
import com.codeforces.graygoose.model.RuleCheckEvent;
import com.codeforces.graygoose.util.SiteCheckingService;
import com.google.inject.Guice;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RescannerRunnable implements Runnable {
    private static final Logger logger = Logger.getLogger(RescannerRunnable.class);

    private final AtomicBoolean running = new AtomicBoolean(true);
    private static final int MAX_EVENTS_TO_REMOVE = 100;

    private final RuleCheckEventDao ruleCheckEventDao;

    public RescannerRunnable() {
        this.ruleCheckEventDao = Guice.createInjector(new ApplicationModule()).getInstance(RuleCheckEventDao.class);
    }

    private void removeOld() {
        logger.info("Retrieve old rule check events with \'SUCCESS\' status from the data storage.");

        List<RuleCheckEvent> oldEvents = ruleCheckEventDao.findAllByStatusForPeriod(
                RuleCheckEvent.Status.SUCCEEDED, 0, System.currentTimeMillis() - TimeConstants.MILLIS_PER_WEEK
        );

        removeEvents(oldEvents);
    }

    private void removeAll() {
        logger.info("Retrieve all rule check events from the data storage.");

        List<RuleCheckEvent> allEvents = ruleCheckEventDao.findAll();

        removeEvents(allEvents);
    }

    private void removeEvents(List<RuleCheckEvent> ruleCheckEvents) {
        int oldEventCount = ruleCheckEvents.size();
        logger.info(String.format("%d events to remove was found.", oldEventCount));

        if (oldEventCount == 0) {
            logger.info("Nothing to remove.");
            return;
        }

        if (oldEventCount > MAX_EVENTS_TO_REMOVE) {
            logger.info(String.format("Can't remove more than %d events per request.", MAX_EVENTS_TO_REMOVE));
        }

        Iterator<RuleCheckEvent> oldEventsIterator = ruleCheckEvents.iterator();
        int removedEventCount = 0;

        while (oldEventsIterator.hasNext() && removedEventCount < MAX_EVENTS_TO_REMOVE) {
            ruleCheckEventDao.delete(oldEventsIterator.next());
            ++removedEventCount;
        }

        logger.info(String.format("%d events were successfully removed.", removedEventCount));
    }

    @Override
    public void run() {
        SiteCheckingService siteCheckingService = SiteCheckingService.newSiteCheckingService();
        int iteration = 0;

        while (running.get()) {
            try {
                logger.info("Start checking of sites.");
                siteCheckingService.checkSites();
                logger.info("Finished checking of sites.");

                checkedSleep(TimeUtil.MILLIS_PER_MINUTE, 10);

                ++iteration;

                if (iteration >= 15) {
                    iteration = 0;
                    removeOld();
                }
            } catch (Exception e) {
                logger.error("Got unexpected exception while checking sites.", e);
                checkedSleep(10L * TimeUtil.MILLIS_PER_MINUTE, 100);
            }
        }
    }

    public final void stop() {
        running.set(false);
    }

    private void checkedSleep(long intervalMillis, int iterationCount) {
        if (intervalMillis <= 0 || iterationCount <= 0 || iterationCount > intervalMillis) {
            throw new IllegalArgumentException(String.format(
                    "Illegal arguments: intervalMillis=%d, iterationCount=%d.", intervalMillis, iterationCount
            ));
        }

        long iterationIntervalMillis = intervalMillis / iterationCount;
        int lastIteration = iterationCount - 1;

        for (int iteration = 0; iteration < lastIteration; ++iteration) {
            if (!running.get()) {
                return;
            }
            ThreadUtil.sleep(iterationIntervalMillis);
        }

        if (running.get()) {
            ThreadUtil.sleep(iterationIntervalMillis + intervalMillis % iterationCount);
        }
    }
}
