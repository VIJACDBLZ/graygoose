package com.codeforces.graygoose.page.web;

import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.dao.cache.Cacheable;
import com.codeforces.graygoose.misc.TimeConstants;
import com.codeforces.graygoose.misc.TimeInterval;
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
    private static final long STATISTICS_REFRESH_INTERVAL_MILLIS = 10L * TimeConstants.MILLIS_PER_MINUTE;

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
        put("timeIntervals", getTimeIntervalsOrderedByValueDesc());

        final List<Site> sites = siteDao.findAll();

        put("sites", sites);
        put("siteInfoBySiteId", getSiteInfoBySiteIdMap(sites, currentTimeInterval));
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

    //TODO: cache for STATISTICS_REFRESH_INTERVAL_MILLIS
    private Map<String, SiteInfo> getSiteInfoBySiteIdMap(List<Site> sites, TimeInterval currentTimeInterval) {
        final long intervalEnd = System.currentTimeMillis();
        final long intervalBegin = intervalEnd - currentTimeInterval.getValueMillis();

        final Hashtable<String, SiteInfo> siteInfoBySiteId = new Hashtable<String, SiteInfo>();

        final Map<Long, Long> alertTriggerCountByRuleCheckId =
                getAlertTriggerCountByRuleCheckIdMap(intervalBegin, intervalEnd);

        for (Site site : sites) {
            final long siteId = site.getId();
            final List<Rule> rules = ruleDao.findBySite(siteId);

            long maxTotalRuleCheckCount = 0;
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

                final long currentRuleCheckCount =
                        succeededChecks.size() + pendingChecks.size() + failedChecks.size();

                if (currentRuleCheckCount > maxTotalRuleCheckCount) {
                    maxTotalRuleCheckCount = currentRuleCheckCount;
                }

                totalRuleCheckCount += currentRuleCheckCount;
            }

            siteInfoBySiteId.put("" + siteId,
                    new SiteInfo(siteId, rules.size(), maxTotalRuleCheckCount, totalRuleCheckCount,
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
        private final long maxTotalRuleCheckCount;
        private final long totalRuleCheckCount;
        private final long succeededRuleCheckCount;
        private final long pendingRuleCheckCount;
        private final long failedRuleCheckCount;
        private final long alertTriggerCount;

        public SiteInfo(long siteId, long ruleCount, long maxTotalRuleCheckCount, long totalRuleCheckCount,
                        long succeededRuleCheckCount, long pendingRuleCheckCount, long failedRuleCheckCount,
                        long alertTriggerCount) {
            this.siteId = siteId;
            this.ruleCount = ruleCount;
            this.maxTotalRuleCheckCount = maxTotalRuleCheckCount;
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

        public long getMaxTotalRuleCheckCount() {
            return maxTotalRuleCheckCount;
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
}
