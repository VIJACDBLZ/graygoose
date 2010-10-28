package com.codeforces.graygoose.model;

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class AlertTriggerEvent extends AbstractEntity {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Long alertId;

    @Persistent
    private Long ruleCheckEventId;

    @Persistent
    private boolean deleted;

    @Persistent
    private Date creationTime;

    public AlertTriggerEvent(long alertId, long ruleCheckEventId) {
        this.alertId = alertId;
        this.ruleCheckEventId = ruleCheckEventId;
        deleted = false;
        creationTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public Long getRuleCheckEventId() {
        return ruleCheckEventId;
    }

    public void setRuleCheckEventId(Long ruleCheckEventId) {
        this.ruleCheckEventId = ruleCheckEventId;
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