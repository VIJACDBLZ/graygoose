package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.model.AlertTriggerEvent;

import java.util.Date;
import java.util.List;

public class AlertTriggerEventDaoImpl extends ApplicationDaoImpl<AlertTriggerEvent> implements AlertTriggerEventDao {
    @Override
    public List<AlertTriggerEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMillis) {
        return findBy("NOT deleted AND creationTime>=? AND creationTime<=? ORDER BY creationTime DESC", new Date(lowerBoundMillis), new Date(upperBoundMillis));
    }

    @Override
    public List<AlertTriggerEvent> findAllByAlert(long alertId) {
        return findBy("NOT deleted AND alertId=? ORDER BY creationTime DESC", alertId);
    }

    @Override
    public List<AlertTriggerEvent> findAllByAlertForPeriod(long alertId, long lowerBoundMillis, long upperBoundMillis) {
        return findBy("NOT deleted AND alertId=? AND creationTime>=? AND creationTime<=? ORDER BY creationTime DESC",
                alertId, new Date(lowerBoundMillis), new Date(upperBoundMillis));
    }

    @Override
    public List<AlertTriggerEvent> findAllByRuleCheck(long ruleCheckEventId) {
        return findBy("NOT deleted AND ruleCheckEventId=? ORDER BY creationTime DESC", ruleCheckEventId);
    }

    @Override
    public void insert(AlertTriggerEvent object) {
        super.insert(object);
    }

    @Override
    public AlertTriggerEvent find(long id) {
        return super.find(id);
    }

    @Override
    public List<AlertTriggerEvent> findAll() {
        return super.findAll();
    }


}
