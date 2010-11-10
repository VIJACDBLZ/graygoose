package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.model.AlertTriggerEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertTriggerEventDaoImpl extends BasicDaoImpl<AlertTriggerEvent> implements AlertTriggerEventDao {
    @Override
    public void insert(AlertTriggerEvent alertTriggerEvent) {
        super.insert(alertTriggerEvent);
    }

    @Override
    public AlertTriggerEvent find(long id) {
        return super.find(AlertTriggerEvent.class, id);
    }

    @Override
    public List<AlertTriggerEvent> findAll() {
        return super.findAll(AlertTriggerEvent.class);
    }

    @Override
    public List<AlertTriggerEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMillis) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(AlertTriggerEvent.class,
                "this.creationTime >= lowerBound"
                        + " && this.creationTime <= upperBound"
                        + " PARAMETERS java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                null, parameters, true);
    }

    @Override
    public List<AlertTriggerEvent> findAllByAlert(long alertId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("alertId", alertId);

        return super.findAll(AlertTriggerEvent.class,
                "this.alertId == alertId PARAMETERS long alertId",
                null, parameters, true);
    }

    @Override
    public List<AlertTriggerEvent> findAllByAlertForPeriod(long alertId, long lowerBoundMillis, long upperBoundMillis) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("alertId", alertId);
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(AlertTriggerEvent.class,
                "this.alertId == alertId"
                        + " && this.creationTime >= lowerBound"
                        + " && this.creationTime <= upperBound"
                        + " PARAMETERS long alertId,"
                        + " java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                null, parameters, true);
    }

    @Override
    public List<AlertTriggerEvent> findAllByRuleCheckForPeriod(
            long ruleCheckEventId, long lowerBoundMillis, long upperBoundMillis) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ruleCheckEventId", ruleCheckEventId);
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(AlertTriggerEvent.class,
                "this.ruleCheckEventId == ruleCheckEventId"
                        + " && this.creationTime >= lowerBound"
                        + " && this.creationTime <= upperBound"
                        + " PARAMETERS long ruleCheckEventId,"
                        + " java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                null, parameters, true);
    }
}