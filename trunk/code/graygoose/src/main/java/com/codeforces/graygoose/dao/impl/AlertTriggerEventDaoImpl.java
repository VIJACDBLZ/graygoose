package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;
import com.codeforces.graygoose.model.AlertTriggerEvent;
import com.codeforces.graygoose.model.Alert;

import java.util.List;

@SuppressWarnings({"unchecked"})
public class AlertTriggerEventDaoImpl extends BasicDaoImpl implements AlertTriggerEventDao {
    @Override
    public void insert(AlertTriggerEvent alertTriggerEvent) {
        makePersistent(alertTriggerEvent);
    }

    @Override
    public void delete(AlertTriggerEvent alertTriggerEvent) {
        alertTriggerEvent.setDeleted(true);
        //TODO: delete linked entities
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
        return super.findAll(AlertTriggerEvent.class, "WHERE alertId == %d", alertId);
    }

    @Override
    public AlertTriggerEvent find(long id) {
        return getObjectById(AlertTriggerEvent.class, id);
    }
}