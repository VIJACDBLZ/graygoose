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
    private Date triggerTime;

    @Persistent
    private boolean deleted;

    @Persistent
    private Date creationTime;

    public AlertTriggerEvent() {
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

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
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