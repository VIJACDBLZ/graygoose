package com.codeforces.graygoose.page.web;

import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.model.AlertTriggerEvent;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;
import com.codeforces.graygoose.model.Site;
import com.google.inject.Inject;
import org.nocturne.annotation.Parameter;
import org.nocturne.link.Link;

import java.util.*;

@Link("")
public class DashboardPage extends WebPage {
    private static final long MILLIS_PER_HOUR = 60L /*m*/ * 60L /*s*/ * 1000L /*ms*/;
    private static final long MILLIS_PER_DAY = 24L /*h*/ * MILLIS_PER_HOUR;

    @Inject
    private SiteDao siteDao;

    @Inject
    private RuleDao ruleDao;

    @Inject
    private RuleCheckEventDao ruleCheckEventDao;

    @Inject
    private AlertTriggerEventDao alertTriggerEventDao;

    @Parameter
    private String timeInterval;

    @Override
    public String getTitle() {
        return $("Dashboard");
    }

    @Override
    public void action() {
        TimeInterval currentTimeInterval;

        try {
            currentTimeInterval = TimeInterval.valueOf(timeInterval);
        } catch (RuntimeException e) {
            currentTimeInterval = TimeInterval.getDefaultValue();
        }

        put("currentTimeInterval", currentTimeInterval);

        final long intervalEnd = System.currentTimeMillis();
        final long intervalBegin = intervalEnd - currentTimeInterval.getValueMillis();

        put("timeIntervals", getTimeIntervalsOrderedByValueDesc());

        final List<Site> sites = siteDao.findAll();

        put("sites", sites);
        put("siteInfoBySiteId", getSiteInfoBySiteIdMap(sites, intervalBegin, intervalEnd));
    }

    private static TimeInterval[] getTimeIntervalsOrderedByValueDesc() {
        final TimeInterval[] timeIntervals = TimeInterval.values();

        Arrays.sort(timeIntervals, new Comparator<TimeInterval>() {
            @Override
            public int compare(TimeInterval o1, TimeInterval o2) {
                if (o1.getValueMillis() < o2.getValueMillis()) {
                    return 1;
                }

                if (o1.getValueMillis() > o2.getValueMillis()) {
                    return -1;
                }

                return 0;
            }
        });

        return timeIntervals;
    }

    private Map<String, SiteInfo> getSiteInfoBySiteIdMap(List<Site> sites, long intervalBegin, long intervalEnd) {
        final Map<String, SiteInfo> siteInfoBySiteId = new HashMap<String, SiteInfo>();

        final Map<Long, Long> alertTriggerCountByRuleCheckId =
                getAlertTriggerCountByRuleCheckIdMap(intervalBegin, intervalEnd);

        for (Site site : sites) {
            final long siteId = site.getId();
            final List<Rule> rules = ruleDao.findBySite(siteId);

            long siteCheckCount = 0;
            long totalRuleCheckCount = 0;
            long succeededRuleCheckCount = 0;
            long pendingRuleCheckCount = 0;
            long failedRuleCheckCount = 0;
            long alertTriggerCount = 0;

            for (Rule rule : rules) {
                final Long ruleId = rule.getId();

                final List<RuleCheckEvent> succeededChecks = ruleCheckEventDao.findByRuleAndStatusForPeriod(
                        ruleId, RuleCheckEvent.Status.SUCCEEDED, intervalBegin, intervalEnd);

                final List<RuleCheckEvent> pendingChecks = ruleCheckEventDao.findByRuleAndStatusForPeriod(
                        ruleId, RuleCheckEvent.Status.PENDING, intervalBegin, intervalEnd);

                for (RuleCheckEvent pendingCheck : pendingChecks) {
                    final Long alertCount = alertTriggerCountByRuleCheckId.get(pendingCheck.getId());
                    if (alertCount != null) {
                        alertTriggerCount += alertCount;
                    }
                }

                final List<RuleCheckEvent> failedChecks = ruleCheckEventDao.findByRuleAndStatusForPeriod(
                        ruleId, RuleCheckEvent.Status.FAILED, intervalBegin, intervalEnd);

                for (RuleCheckEvent failedCheck : failedChecks) {
                    final Long alertCount = alertTriggerCountByRuleCheckId.get(failedCheck.getId());
                    if (alertCount != null) {
                        alertTriggerCount += alertCount;
                    }
                }

                succeededRuleCheckCount += succeededChecks.size();
                pendingRuleCheckCount += pendingChecks.size();
                failedRuleCheckCount += failedChecks.size();

                final long currentRuleCheckCount = succeededChecks.size() + pendingChecks.size() + failedChecks.size();

                if (currentRuleCheckCount > siteCheckCount) {
                    siteCheckCount = currentRuleCheckCount;
                }

                totalRuleCheckCount += currentRuleCheckCount;
            }

            siteInfoBySiteId.put("" + siteId,
                    new SiteInfo(siteId, rules.size(), siteCheckCount, totalRuleCheckCount,
                            succeededRuleCheckCount, pendingRuleCheckCount, failedRuleCheckCount, alertTriggerCount));
        }

        return siteInfoBySiteId;
    }

    private Map<Long, Long> getAlertTriggerCountByRuleCheckIdMap(long intervalBegin, long intervalEnd) {
        final Map<Long, Long> alertTriggerCountByRuleCheckId = new HashMap<Long, Long>();
        final List<AlertTriggerEvent> alertTriggers = alertTriggerEventDao.findAllForPeriod(intervalBegin, intervalEnd);

        for (AlertTriggerEvent alertTrigger : alertTriggers) {
            final Long alertTriggerCount = alertTriggerCountByRuleCheckId.get(alertTrigger.getRuleCheckEventId());
            alertTriggerCountByRuleCheckId.put(
                    alertTrigger.getRuleCheckEventId(), alertTriggerCount == null ? 1L : alertTriggerCount + 1L);
        }

        return alertTriggerCountByRuleCheckId;
    }

    public static class SiteInfo {
        private final long siteId;
        private final long ruleCount;
        private final long siteCheckCount;
        private final long totalRuleCheckCount;
        private final long succeededRuleCheckCount;
        private final long pendingRuleCheckCount;
        private final long failedRuleCheckCount;
        private final long alertTriggerCount;

        public SiteInfo(long siteId, long ruleCount, long siteCheckCount, long totalRuleCheckCount,
                        long succeededRuleCheckCount, long pendingRuleCheckCount, long failedRuleCheckCount,
                        long alertTriggerCount) {
            this.siteId = siteId;
            this.ruleCount = ruleCount;
            this.siteCheckCount = siteCheckCount;
            this.totalRuleCheckCount = totalRuleCheckCount;
            this.succeededRuleCheckCount = succeededRuleCheckCount;
            this.pendingRuleCheckCount = pendingRuleCheckCount;
            this.failedRuleCheckCount = failedRuleCheckCount;
            this.alertTriggerCount = alertTriggerCount;
        }

        public long getSiteId() {
            return siteId;
        }

        public long getRuleCount() {
            return ruleCount;
        }

        public long getSiteCheckCount() {
            return siteCheckCount;
        }

        public long getTotalRuleCheckCount() {
            return totalRuleCheckCount;
        }

        public long getSucceededRuleCheckCount() {
            return succeededRuleCheckCount;
        }

        public long getPendingRuleCheckCount() {
            return pendingRuleCheckCount;
        }

        public long getFailedRuleCheckCount() {
            return failedRuleCheckCount;
        }

        public long getAlertTriggerCount() {
            return alertTriggerCount;
        }
    }

    public enum TimeInterval {
        SIX_HOURS("6 hours", 6L * DashboardPage.MILLIS_PER_HOUR),
        TWELVE_HOURS("12 hours", 12L * DashboardPage.MILLIS_PER_HOUR),
        TWENTY_FOUR_HOURS("24 hours", 24L * DashboardPage.MILLIS_PER_HOUR),
        TWO_DAYS("2 days", 2L * DashboardPage.MILLIS_PER_DAY),
        FOUR_DAYS("4 days", 4L * DashboardPage.MILLIS_PER_DAY),
        SEVEN_DAYS("7 days", 7L * DashboardPage.MILLIS_PER_DAY),
        FOURTEEN_DAYS("14 days", 14L * DashboardPage.MILLIS_PER_DAY),
        THIRTY_DAYS("30 days", 30L * DashboardPage.MILLIS_PER_DAY);

        private final String synonym;
        private final long valueMillis;

        private TimeInterval(String synonym, Long valueMillis) {
            this.synonym = synonym;
            this.valueMillis = valueMillis;
        }

        public String getSynonym() {
            return synonym;
        }

        public long getValueMillis() {
            return valueMillis;
        }

        public static TimeInterval getDefaultValue() {
            return TWENTY_FOUR_HOURS;
        }
    }
}
