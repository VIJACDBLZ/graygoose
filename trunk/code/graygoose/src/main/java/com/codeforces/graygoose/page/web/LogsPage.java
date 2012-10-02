package com.codeforces.graygoose.page.web;

import com.codeforces.graygoose.dao.*;
import com.codeforces.graygoose.misc.TimeInterval;
import com.codeforces.graygoose.model.*;
import com.google.inject.Inject;
import org.nocturne.annotation.Parameter;
import org.nocturne.link.Link;

import java.util.*;

@Link("logs")
public class LogsPage extends WebPage {
    private static final int DEFAULT_EVENT_LIMIT = 50;
    private static final int MAX_EVENT_LIMIT = 500;

    @Inject
    private RuleCheckEventDao ruleCheckEventDao;
    @Inject
    private AlertTriggerEventDao alertTriggerEventDao;
    @Inject
    private SiteDao siteDao;
    @Inject
    private AlertDao alertDao;
    @Inject
    private RuleDao ruleDao;

    @Parameter
    private Long siteId;
    @Parameter
    private String status;
    @Parameter
    private String timeInterval;
    @Parameter
    private Boolean withAlertsOnly;
    @Parameter
    private Integer limit;

    @Override
    public String getTitle() {
        return $("Logs");
    }

    @Override
    public void action() {
        boolean withAlertsOnlyValue = getWithAlertsOnlyValue();
        RuleCheckEvent.Status statusValue = getStatusValue(withAlertsOnlyValue);
        TimeInterval currentTimeIntervalValue = getCurrentTimeIntervalValue();
        int limitValue = getLimitValue();
        Long siteIdValue = getSiteIdValue();

        long intervalEnd = System.currentTimeMillis();
        long intervalBegin = intervalEnd - currentTimeIntervalValue.getValueMillis();

        List<RuleCheckEvent> eventsForPeriod =
                getEventsForPeriod(siteIdValue, statusValue, intervalEnd, intervalBegin);

        List<EventDto> events = new ArrayList<EventDto>(limitValue);

        Iterator<RuleCheckEvent> eventsIterator = eventsForPeriod.iterator();
        int eventsAdded = 0;

        while (eventsIterator.hasNext() && eventsAdded < limitValue) {
            RuleCheckEvent ruleCheckEvent = eventsIterator.next();

            List<AlertTriggerEvent> alertTriggerEvents =
                    ruleCheckEvent.getStatus() == RuleCheckEvent.Status.FAILED ?
                            alertTriggerEventDao.findAllByRuleCheck(ruleCheckEvent.getId()) :
                            new ArrayList<AlertTriggerEvent>();

            if (!withAlertsOnlyValue || !alertTriggerEvents.isEmpty()) {
                List<Alert> alerts = new ArrayList<Alert>(alertTriggerEvents.size());

                for (AlertTriggerEvent alertTriggerEvent : alertTriggerEvents) {
                    alerts.add(alertDao.find(alertTriggerEvent.getAlertId(), false));
                }

                events.add(new EventDto(
                        siteDao.find(ruleCheckEvent.getSiteId(), false),
                        ruleDao.find(ruleCheckEvent.getRuleId(), false),
                        ruleCheckEvent,
                        alerts
                ));

                ++eventsAdded;
            }
        }

        if (siteIdValue != null) {
            put("siteId", siteIdValue);
        }

        if (statusValue != null) {
            put("status", statusValue);
        }

        if (withAlertsOnlyValue) {
            put("withAlertsOnly", withAlertsOnlyValue);
        }

        put("limit", limitValue);
        put("currentTimeInterval", currentTimeIntervalValue);
        put("timeIntervals", TimeInterval.getTimeIntervalsOrderedByValueDesc());
        put("events", events);
    }

    private Long getSiteIdValue() {
        return siteId == null || siteId <= 0 ? null : siteId;
    }

    private RuleCheckEvent.Status getStatusValue(boolean withAlertsOnlyValue) {
        if (withAlertsOnlyValue) {
            return RuleCheckEvent.Status.FAILED;
        }

        try {
            return RuleCheckEvent.Status.valueOf(this.status);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private TimeInterval getCurrentTimeIntervalValue() {
        try {
            return TimeInterval.valueOf(timeInterval);
        } catch (RuntimeException e) {
            return TimeInterval.getDefaultValue();
        }
    }

    private int getLimitValue() {
        if (limit == null || limit <= 0) {
            return DEFAULT_EVENT_LIMIT;
        } else if (limit > MAX_EVENT_LIMIT) {
            return MAX_EVENT_LIMIT;
        } else {
            return limit;
        }
    }

    private boolean getWithAlertsOnlyValue() {
        return this.withAlertsOnly == null ? false : this.withAlertsOnly;
    }

    private List<RuleCheckEvent> getEventsForPeriod(
            Long siteIdValue, RuleCheckEvent.Status statusValue, long intervalEnd, long intervalBegin) {
        if (siteIdValue != null && statusValue != null) {
            return ruleCheckEventDao.findAllBySiteAndStatusForPeriod(siteIdValue, statusValue, intervalBegin, intervalEnd);
        } else if (siteIdValue != null) {
            return ruleCheckEventDao.findAllBySiteForPeriod(siteIdValue, intervalBegin, intervalEnd);
        } else if (statusValue != null) {
            return ruleCheckEventDao.findAllByStatusForPeriod(statusValue, intervalBegin, intervalEnd);
        } else {
            return ruleCheckEventDao.findAllForPeriod(intervalBegin, intervalEnd);
        }
    }

    public static class EventDto {
        private final Site site;
        private final Rule rule;
        private final RuleCheckEvent ruleCheckEvent;
        private final List<Alert> alerts;

        public EventDto(Site site, Rule rule, RuleCheckEvent ruleCheckEvent, List<Alert> alerts) {
            this.site = site;
            this.rule = rule;
            this.ruleCheckEvent = ruleCheckEvent;
            this.alerts = new ArrayList<Alert>(alerts);
        }

        public Site getSite() {
            return site;
        }

        public Rule getRule() {
            return rule;
        }

        public Long getResponseId() {
            return ruleCheckEvent.getResponseId();
        }

        public RuleCheckEvent.Status getStatus() {
            return ruleCheckEvent.getStatus();
        }

        public String getDescription() {
            return ruleCheckEvent.getDescription();
        }

        public Date getCheckTime() {
            return ruleCheckEvent.getCheckTime();
        }

        public List<Alert> getAlerts() {
            return Collections.unmodifiableList(alerts);
        }
    }
}
