package com.codeforces.graygoose.model;

import javax.jdo.annotations.*;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RuleAlertRelation {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private long ruleId;

    @Persistent
    private long alertId;

    @Persistent
    private long maxConsecutiveFailCount;

    public RuleAlertRelation(long ruleId, long alertId, long maxConsecutiveFailCount) {
        this.ruleId = ruleId;
        this.alertId = alertId;
        this.maxConsecutiveFailCount = maxConsecutiveFailCount;
    }

    public RuleAlertRelation(Rule rule, Alert alert, long maxConsecutiveFailCount) {
        this.ruleId = rule.getId();
        this.alertId = alert.getId();
        this.maxConsecutiveFailCount = maxConsecutiveFailCount;
    }

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
}
