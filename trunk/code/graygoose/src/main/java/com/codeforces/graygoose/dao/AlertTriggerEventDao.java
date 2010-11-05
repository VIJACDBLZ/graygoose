package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.AlertTriggerEvent;

import java.util.List;

public interface AlertTriggerEventDao extends BasicDao<AlertTriggerEvent> {
    AlertTriggerEvent find(long id);

    List<AlertTriggerEvent> findAll();

    List<AlertTriggerEvent> findByAlert(long alertId);

    List<AlertTriggerEvent> findByAlertForPeriod(long alertId, long lowerBoundMillis, long upperBoundMillis);

    void insert(AlertTriggerEvent alertTriggerEvent);
}