package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;
import com.codeforces.graygoose.model.AlertTriggerEvent;
import com.codeforces.graygoose.model.Alert;

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
}