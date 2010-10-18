package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.model.Alert;

import java.util.List;

@SuppressWarnings({"unchecked"})
public class AlertDaoImpl extends BasicDaoImpl implements AlertDao {
    @Override
    public void insert(Alert alert) {
        makePersistent(alert);
    }

    @Override
    public void delete(Alert alert) {
        deletePersistent(alert);
    }

    @Override
    public List<Alert> findAll() {
        return (List<Alert>) execute("SELECT FROM " + Alert.class.getName() + " ORDER BY name");
    }

    @Override
    public Alert find(long id) {
        return getObjectById(Alert.class, id);
    }
}
