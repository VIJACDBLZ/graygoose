package com.codeforces.graygoose.model;

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RuleAlertRelation extends AbstractEntity {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private long ruleId;

    @Persistent
    private long alertId;

    @Persistent
    private long maxConsecutiveFailCount;

    @Persistent
    private boolean deleted;

    @Persistent
    private Date creationTime;

    public RuleAlertRelation(long ruleId, long alertId, long maxConsecutiveFailCount) {
        this.ruleId = ruleId;
        this.alertId = alertId;
        this.maxConsecutiveFailCount = maxConsecutiveFailCount;
        deleted = false;
        creationTime = new Date();
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

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Date getCreationTime() {
        return creationTime;
    }
}
