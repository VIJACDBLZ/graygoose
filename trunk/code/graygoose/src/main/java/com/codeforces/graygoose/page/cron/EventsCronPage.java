package com.codeforces.graygoose.page.cron;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.misc.TimeConstants;
import com.codeforces.graygoose.model.RuleCheckEvent;
import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.nocturne.annotation.Action;
import org.nocturne.link.Link;

import java.util.Iterator;
import java.util.List;

@Link("cron/events")
public class EventsCronPage extends CronPage {
    private static final Logger logger = Logger.getLogger(EventsCronPage.class);

    private static final int MAX_EVENTS_TO_REMOVE = 100;

    @Inject
    private RuleCheckEventDao ruleCheckEventDao;

    @Action("removeOld")
    public void onRemoveold() {
        logger.info("Retrieve old rule check events with \'SUCCESS\' status from the data storage.");

        List<RuleCheckEvent> oldEvents = ruleCheckEventDao.findAllByStatusForPeriod(
                RuleCheckEvent.Status.SUCCEEDED, 0, System.currentTimeMillis() - TimeConstants.MILLIS_PER_DAY);

        final int oldEventCount = oldEvents.size();
        logger.info(String.format("%d events was found.", oldEventCount));

        if (oldEventCount > MAX_EVENTS_TO_REMOVE) {
            logger.info(String.format("Can't remove more than %d events per request.", MAX_EVENTS_TO_REMOVE));
        }

        final Iterator<RuleCheckEvent> oldEventsIterator = oldEvents.iterator();
        int removedEventCount = 0;

        while (oldEventsIterator.hasNext() && removedEventCount < MAX_EVENTS_TO_REMOVE) {
            ruleCheckEventDao.delete(oldEventsIterator.next());
            ++removedEventCount;
        }

        logger.info(String.format("%d events was successfully removed.", removedEventCount));
    }

    @Override
    public void action() {
        // No operations.
    }
}
