package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.AlertTriggerEvent;

import java.util.List;

public interface AlertTriggerEventDao {
    AlertTriggerEvent find(long id);

    List<AlertTriggerEvent> findAll();

    List<AlertTriggerEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMillis);

    List<AlertTriggerEvent> findAllByAlert(long alertId);

    List<AlertTriggerEvent> findAllByAlertForPeriod(long alertId, long lowerBoundMillis, long upperBoundMillis);

    List<AlertTriggerEvent> findAllByRuleCheck(long ruleCheckEventId);

    void insert(AlertTriggerEvent alertTriggerEvent);
}
