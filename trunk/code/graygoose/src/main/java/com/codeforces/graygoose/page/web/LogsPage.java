package com.codeforces.graygoose.page.web;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.misc.TimeInterval;
import com.codeforces.graygoose.model.RuleCheckEvent;
import com.google.inject.Inject;
import org.nocturne.annotation.Parameter;
import org.nocturne.link.Link;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Link("logs")
public class LogsPage extends WebPage {
    private static final int DEFAULT_LIMIT = 50;
    private static final int MAX_LIMIT = 200;

    @Inject
    private RuleCheckEventDao ruleCheckEventDao;

    @Parameter
    private Long siteId;

    @Parameter
    private String status;

    @Parameter
    private Boolean withAlertsOnly;

    @Parameter
    private String timeInterval;

    @Parameter
    private Integer limit;

    @Override
    public String getTitle() {
        return $("Logs");
    }

    @Override
    public void action() {
        RuleCheckEvent.Status status = getStatus();
        TimeInterval currentTimeInterval = getCurrentTimeInterval();
        int limit = getLimit();
        boolean withAlertsOnly = this.withAlertsOnly == null ? false : this.withAlertsOnly;

        final long intervalEnd = System.currentTimeMillis();
        final long intervalBegin = intervalEnd - currentTimeInterval.getValueMillis();

        List<RuleCheckEvent> eventsForPeriod = getEventsForPeriod(status, intervalEnd, intervalBegin);

        List<RuleCheckEvent> events = new ArrayList<RuleCheckEvent>(limit);

        final Iterator<RuleCheckEvent> eventsIterator = eventsForPeriod.iterator();
        int eventsAdded = 0;

        while (eventsIterator.hasNext()) {
            //TODO:
            //if (!withAlertsOnly)
        }
    }

    private RuleCheckEvent.Status getStatus() {
        RuleCheckEvent.Status status;

        try {
            status = RuleCheckEvent.Status.valueOf(this.status);
        } catch (RuntimeException e) {
            status = null;
        }

        return status;
    }

    private TimeInterval getCurrentTimeInterval() {
        TimeInterval currentTimeInterval;

        try {
            currentTimeInterval = TimeInterval.valueOf(timeInterval);
        } catch (RuntimeException e) {
            currentTimeInterval = TimeInterval.getDefaultValue();
        }

        return currentTimeInterval;
    }

    private int getLimit() {
        return limit == null || limit <= 0 || limit > MAX_LIMIT ?
                DEFAULT_LIMIT :
                limit;
    }

    private List<RuleCheckEvent> getEventsForPeriod(RuleCheckEvent.Status status, long intervalEnd, long intervalBegin) {
        List<RuleCheckEvent> eventsForPeriod;

        if (siteId != null && status != null) {
            eventsForPeriod = ruleCheckEventDao.findAllBySiteAndStatusForPeriod(siteId, status, intervalBegin, intervalEnd);
        } else if (siteId != null) {
            eventsForPeriod = ruleCheckEventDao.findAllBySiteForPeriod(siteId, intervalBegin, intervalEnd);
        } else if (status != null) {
            eventsForPeriod = ruleCheckEventDao.findAllByStatusForPeriod(status, intervalBegin, intervalEnd);
        } else {
            eventsForPeriod = ruleCheckEventDao.findAllForPeriod(intervalBegin, intervalEnd);
        }

        return eventsForPeriod;
    }
}
