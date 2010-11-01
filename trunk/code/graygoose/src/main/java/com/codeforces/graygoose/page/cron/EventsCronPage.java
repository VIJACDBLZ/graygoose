package com.codeforces.graygoose.page.cron;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.model.RuleCheckEvent;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.link.Link;

import java.util.List;

@Link("cron/events")
public class EventsCronPage extends CronPage {

    private static final long MILLIS_PER_DAY = 24L /*h*/ * 60L /*m*/ * 60L /*s*/ * 1000L /*ms*/;
    //private static final long MILLIS_PER_MONTH = 30L /*d*/ * MILLIS_PER_DAY;

    @Inject
    private RuleCheckEventDao ruleCheckEventDao;

    @Action("removeOld")
    public void onRemoveold() {
        List<RuleCheckEvent> oldEvents =
                ruleCheckEventDao.findAllForPeriod(0, System.currentTimeMillis() - MILLIS_PER_DAY);

        for (RuleCheckEvent oldEvent : oldEvents) {
            if (oldEvent.getStatus() == RuleCheckEvent.Status.SUCCEEDED) {
                ruleCheckEventDao.delete(oldEvent);
            }
        }
    }

    @Override
    public void action() {
        // No operations.
    }
}
