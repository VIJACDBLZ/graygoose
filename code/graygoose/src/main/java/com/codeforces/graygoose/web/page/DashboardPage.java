package com.codeforces.graygoose.web.page;

import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.misc.TimeInterval;
import com.codeforces.graygoose.model.AlertTriggerEvent;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;
import com.codeforces.graygoose.model.Site;
import com.google.inject.Inject;
import org.nocturne.annotation.Parameter;
import org.nocturne.link.Link;

import java.util.*;

@Link(";dashboard")
public class DashboardPage extends WebPage {
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
        put("timeIntervals", TimeInterval.getTimeIntervalsOrderedByValueDesc());
        put("sites", getSiteDtos(siteDao.findAll(), currentTimeInterval));
    }

    private List<SiteDto> getSiteDtos(List<Site> sites, TimeInterval currentTimeInterval) {
        long intervalEnd = System.currentTimeMillis();
        long intervalBegin = intervalEnd - currentTimeInterval.getValueMillis();

        Map<Long, Long> alertTriggerCountByRuleCheckId =
                getAlertTriggerCountByRuleCheckIdMap(intervalBegin, intervalEnd);

        List<SiteDto> siteDtos = new ArrayList<>();

        for (Site site : sites) {
            siteDtos.add(getSiteDto(site, intervalBegin, intervalEnd, alertTriggerCountByRuleCheckId));
        }

        return siteDtos;
    }

    private SiteDto getSiteDto(Site site, long intervalBegin, long intervalEnd,
                               Map<Long, Long> alertTriggerCountByRuleCheckId) {
        List<Rule> rules = ruleDao.findAllBySite(site.getId());

        long maxTotalRuleCheckCount = 0;
        long totalRuleCheckCount = 0;
        long succeededRuleCheckCount = 0;
        long pendingRuleCheckCount = 0;
        long failedRuleCheckCount = 0;
        long alertTriggerCount = 0;

        for (Rule rule : rules) {
            Long ruleId = rule.getId();

            List<Long> succeededCheckKeys = ruleCheckEventDao.findKeysByRuleAndStatusForPeriod(
                    ruleId, RuleCheckEvent.Status.SUCCEEDED, intervalBegin, intervalEnd);

            List<Long> pendingCheckKeys = ruleCheckEventDao.findKeysByRuleAndStatusForPeriod(
                    ruleId, RuleCheckEvent.Status.PENDING, intervalBegin, intervalEnd);

            List<Long> failedCheckKeys = ruleCheckEventDao.findKeysByRuleAndStatusForPeriod(
                    ruleId, RuleCheckEvent.Status.FAILED, intervalBegin, intervalEnd);

            for (long failedCheckKey : failedCheckKeys) {
                Long alertCount = alertTriggerCountByRuleCheckId.get(failedCheckKey);
                if (alertCount != null) {
                    alertTriggerCount += alertCount;
                }
            }

            succeededRuleCheckCount += succeededCheckKeys.size();
            pendingRuleCheckCount += pendingCheckKeys.size();
            failedRuleCheckCount += failedCheckKeys.size();

            long currentRuleCheckCount =
                    succeededCheckKeys.size() + pendingCheckKeys.size() + failedCheckKeys.size();

            if (currentRuleCheckCount > maxTotalRuleCheckCount) {
                maxTotalRuleCheckCount = currentRuleCheckCount;
            }

            totalRuleCheckCount += currentRuleCheckCount;
        }

        return new SiteDto(site, rules.size(), maxTotalRuleCheckCount, totalRuleCheckCount,
                succeededRuleCheckCount, pendingRuleCheckCount, failedRuleCheckCount, alertTriggerCount);
    }

    private Map<Long, Long> getAlertTriggerCountByRuleCheckIdMap(long intervalBegin, long intervalEnd) {
        Map<Long, Long> alertTriggerCountByRuleCheckId = new HashMap<>();
        List<AlertTriggerEvent> alertTriggers = alertTriggerEventDao.findAllForPeriod(intervalBegin, intervalEnd);

        for (AlertTriggerEvent alertTrigger : alertTriggers) {
            Long alertTriggerCount = alertTriggerCountByRuleCheckId.get(alertTrigger.getRuleCheckEventId());
            alertTriggerCountByRuleCheckId.put(
                    alertTrigger.getRuleCheckEventId(), alertTriggerCount == null ? 1L : alertTriggerCount + 1L);
        }

        return alertTriggerCountByRuleCheckId;
    }

    public static class SiteDto {
        private final Site site;
        private final long ruleCount;
        private final long maxTotalRuleCheckCount;
        private final long totalRuleCheckCount;
        private final long succeededRuleCheckCount;
        private final long pendingRuleCheckCount;
        private final long failedRuleCheckCount;
        private final long alertTriggerCount;

        public SiteDto(Site site, long ruleCount, long maxTotalRuleCheckCount, long totalRuleCheckCount,
                       long succeededRuleCheckCount, long pendingRuleCheckCount, long failedRuleCheckCount,
                       long alertTriggerCount) {
            this.site = site;
            this.ruleCount = ruleCount;
            this.maxTotalRuleCheckCount = maxTotalRuleCheckCount;
            this.totalRuleCheckCount = totalRuleCheckCount;
            this.succeededRuleCheckCount = succeededRuleCheckCount;
            this.pendingRuleCheckCount = pendingRuleCheckCount;
            this.failedRuleCheckCount = failedRuleCheckCount;
            this.alertTriggerCount = alertTriggerCount;
        }

        public long getId() {
            return site.getId();
        }

        public String getName() {
            return site.getName();
        }

        public String getUrl() {
            return site.getUrl();
        }

        public int getRescanPeriodSeconds() {
            return site.getRescanPeriodSeconds();
        }

        public Date getCreationTime() {
            return site.getCreationTime();
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
