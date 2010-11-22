package com.codeforces.graygoose.model;

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RuleCheckEvent extends AbstractEntity {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Long ruleId;

    @Persistent
    private Long siteId;

    @Persistent
    private Long responseId;

    @Persistent
    private Status status;

    @Persistent
    private String description;

    @Persistent
    private Date checkTime;

    @Persistent
    private boolean deleted;

    @SuppressWarnings({"FieldMayBeFinal"})
    @Persistent
    private Date creationTime;

    public RuleCheckEvent(long ruleId, long siteId) {
        this.ruleId = ruleId;
        this.siteId = siteId;
        status = Status.PENDING;
        deleted = false;
        creationTime = new Date();
    }

    @Override
    public Long getId() {
        return id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SuppressWarnings({"ReturnOfDateField"})
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = new Date(checkTime.getTime());
    }

    public enum Status {
        PENDING,
        SUCCEEDED,
        FAILED
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @SuppressWarnings({"ReturnOfDateField"})
    @Override
    public Date getCreationTime() {
        return creationTime;
    }
}