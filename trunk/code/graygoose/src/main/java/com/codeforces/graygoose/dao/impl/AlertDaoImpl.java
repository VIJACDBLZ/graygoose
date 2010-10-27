package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.model.Alert;
import com.google.inject.Inject;

import java.util.List;

@SuppressWarnings({"unchecked"})
public class AlertDaoImpl extends BasicDaoImpl implements AlertDao {
    @Override
    public void insert(Alert alert) {
        makePersistent(alert);
    }

    @Override
    public void delete(Alert alert) {
        alert.setDeleted(true);
        //TODO: delete linked entities
    }

    @Override
    public List<Alert> findAll() {
        return super.findAll(Alert.class, "ORDER BY name");
    }

    @Override
    public Alert find(long id) {
        return getObjectById(Alert.class, id);
    }
}
