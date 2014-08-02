package com.codeforces.graygoose.rescanner;

import com.codeforces.graygoose.ApplicationModule;
import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.misc.TimeConstants;
import com.codeforces.graygoose.model.RuleCheckEvent;
import com.codeforces.graygoose.util.SiteCheckingService;
import com.google.inject.Guice;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

public class RescannerRunnable implements Runnable {
    private static final Logger logger = Logger.getLogger(RescannerRunnable.class);

    private static final int MAX_EVENTS_TO_REMOVE = 100;

    private RuleCheckEventDao ruleCheckEventDao;

    private RescannerContextListener rescannerContextListener;

    public RescannerRunnable(RescannerContextListener rescannerContextListener) {
        this.rescannerContextListener = rescannerContextListener;
        this.ruleCheckEventDao = Guice.createInjector(new ApplicationModule()).getInstance(RuleCheckEventDao.class);
    }

    private void removeOld() {
        logger.info("Retrieve old rule check events with \'SUCCESS\' status from the data storage.");

        List<RuleCheckEvent> oldEvents = ruleCheckEventDao.findAllByStatusForPeriod(
                RuleCheckEvent.Status.SUCCEEDED, 0, System.currentTimeMillis() - TimeConstants.MILLIS_PER_WEEK);

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

        try {
            int iteration = 0;

            while (rescannerContextListener.isAlive()) {
                logger.info(String.format("Start sites checking."));
                siteCheckingService.checkSites();
                logger.info(String.format("Finish sites checking."));

                Thread.sleep(60 * 1000);
                iteration++;

                if (iteration == 15) {
                    iteration = 0;
                    removeOld();
                }
            }
        } catch (Exception e) {
            logger.error("Unexpected exception " + e, e);
        }
    }

}
