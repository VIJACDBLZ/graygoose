package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.AlertTriggerEvent;

import java.util.ArrayList;
import java.util.List;

public class AlertTriggerEventDaoImpl extends BasicDaoImpl<AlertTriggerEvent> implements AlertTriggerEventDao {

    @Override
    public AlertTriggerEvent find(long id) {
        return super.find(AlertTriggerEvent.class, id);
    }

    @Override
    public List<AlertTriggerEvent> findAll() {
        return super.findAll(AlertTriggerEvent.class);
    }

    @Override
    public List<AlertTriggerEvent> findByAlert(Alert alert) {
        return findByAlert(alert.getId());
    }

    @Override
    public List<AlertTriggerEvent> findByAlert(long alertId) {
        return super.findAll(AlertTriggerEvent.class, String.format("alertId == %d", alertId), null, true);
    }

    @Override
    public List<AlertTriggerEvent> findByAlertForPeriod(
            Alert alert, long lowerBoundMillis, long upperBoundMillis) {
        return findByAlertForPeriod(alert.getId(), lowerBoundMillis, upperBoundMillis);
    }

    @Override
    public List<AlertTriggerEvent> findByAlertForPeriod(
            long alertId, long lowerBoundMillis, long upperBoundMillis) {
        /*Date upperBound = new Date(upperBoundMillis);
        Date lowerBound = new Date(lowerBoundMillis);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder whereClause = new StringBuilder();
        whereClause.append("creationTime >= DATETIME('").append(dateFormat.format(lowerBound))
                .append("') && creationTime <= DATETIME('").append(dateFormat.format(upperBound)).append("')");

        return super.findAll(AlertTriggerEvent.class, whereClause.toString(), null, true);*/

        List<AlertTriggerEvent> alertTriggerEvents = super.findAll(AlertTriggerEvent.class);
        List<AlertTriggerEvent> result = new ArrayList<AlertTriggerEvent>();

        for (AlertTriggerEvent alertTriggerEvent : alertTriggerEvents) {
            if (alertTriggerEvent.getCreationTime().getTime() >= lowerBoundMillis
                    && alertTriggerEvent.getCreationTime().getTime() <= upperBoundMillis) {
                result.add(alertTriggerEvent);
            }
        }

        return result;
    }
}