package com.codeforces.graygoose.model;

import java.util.Date;

public class RuleCheckEvent extends ApplicationEntity {
    private long ruleId;
    private long siteId;
    private Long responseId;
    private Status status;
    private String description;
    private Date checkTime;

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
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

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime != null ? new Date(checkTime.getTime()) : null;
    }

    public enum Status {
        PENDING,
        SUCCEEDED,
        FAILED
    }

    public static RuleCheckEvent newRuleCheckEvent (long ruleId, long siteId) {
        RuleCheckEvent ruleCheckEvent = new RuleCheckEvent();

        ruleCheckEvent.setRuleId(ruleId);
        ruleCheckEvent.setSiteId(siteId);
        ruleCheckEvent.setStatus(Status.PENDING);

        return ruleCheckEvent;
    }
}
