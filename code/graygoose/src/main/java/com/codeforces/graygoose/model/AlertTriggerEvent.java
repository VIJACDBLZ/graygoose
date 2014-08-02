package com.codeforces.graygoose.model;

public class    AlertTriggerEvent extends ApplicationEntity {
    private long alertId;
    private long ruleCheckEventId;

    public long getAlertId() {
        return alertId;
    }

    public void setAlertId(long alertId) {
        this.alertId = alertId;
    }

    public long getRuleCheckEventId() {
        return ruleCheckEventId;
    }

    public void setRuleCheckEventId(long ruleCheckEventId) {
        this.ruleCheckEventId = ruleCheckEventId;
    }

    public static AlertTriggerEvent newAlertTriggerEvent(long alertId, long ruleCheckEventId) {
        AlertTriggerEvent alertTriggerEvent = new AlertTriggerEvent();

        alertTriggerEvent.setAlertId(alertId);
        alertTriggerEvent.setRuleCheckEventId(ruleCheckEventId);

        return alertTriggerEvent;
    }
}
