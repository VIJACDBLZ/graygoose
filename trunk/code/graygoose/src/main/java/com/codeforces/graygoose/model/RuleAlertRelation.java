package com.codeforces.graygoose.model;

public class RuleAlertRelation extends ApplicationEntity {
    private long ruleId;
    private long alertId;
    private long maxConsecutiveFailCount;

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    public long getAlertId() {
        return alertId;
    }

    public void setAlertId(long alertId) {
        this.alertId = alertId;
    }

    public long getMaxConsecutiveFailCount() {
        return maxConsecutiveFailCount;
    }

    public void setMaxConsecutiveFailCount(long maxConsecutiveFailCount) {
        this.maxConsecutiveFailCount = maxConsecutiveFailCount;
    }

    public static RuleAlertRelation newRuleAlertRelation(Long ruleId, Long alertId, long maxConsecutiveFailCount) {
        RuleAlertRelation ruleAlertRelation = new RuleAlertRelation();

        ruleAlertRelation.setRuleId(ruleId);
        ruleAlertRelation.setAlertId(alertId);
        ruleAlertRelation.setMaxConsecutiveFailCount(maxConsecutiveFailCount);

        return ruleAlertRelation;
    }
}
