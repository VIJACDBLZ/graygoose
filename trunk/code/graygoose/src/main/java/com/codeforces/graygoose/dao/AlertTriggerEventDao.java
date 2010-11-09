package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.AlertTriggerEvent;
import com.codeforces.graygoose.model.RuleCheckEvent;

import java.util.List;

public interface AlertTriggerEventDao extends BasicDao<AlertTriggerEvent> {
    AlertTriggerEvent find(long id);

    List<AlertTriggerEvent> findAll();

    List<AlertTriggerEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMillis);

    List<AlertTriggerEvent> findByAlert(long alertId);

    List<AlertTriggerEvent> findByAlertForPeriod(long alertId, long lowerBoundMillis, long upperBoundMillis);

    List<AlertTriggerEvent> findByRuleCheckForPeriod(long ruleCheckEventId, long lowerBoundMillis, long upperBoundMillis);

    void insert(AlertTriggerEvent alertTriggerEvent);
}