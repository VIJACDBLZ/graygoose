package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.AlertTriggerEvent;

import java.util.List;

public interface AlertTriggerEventDao extends BasicDao<AlertTriggerEvent> {
    List<AlertTriggerEvent> findByAlert(Alert alert);

    List<AlertTriggerEvent> findByAlert(long alertId);

    List<AlertTriggerEvent> findByAlertForPeriod(Alert alert, long lowerBoundMillis, long upperBoundMillis);

    List<AlertTriggerEvent> findByAlertForPeriod(long alertId, long lowerBoundMillis, long upperBoundMillis);
}