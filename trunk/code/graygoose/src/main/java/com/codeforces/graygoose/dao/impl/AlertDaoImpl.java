package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.RuleAlertRelation;
import com.google.inject.Inject;

import java.util.List;

public class AlertDaoImpl extends ApplicationDaoImpl<Alert> implements AlertDao {
    @Inject
    private RuleAlertRelationDao ruleAlertRelationDao;

    @Override
    public Alert find(long id) {
        return super.find(id);
    }

    @Override
    public Alert find(long id, boolean ignoreDeleted) {
        return super.find(id, ignoreDeleted);
    }

    @Override
    public void markDeleted(Alert alert) {
        super.markDeleted(alert);

        for (RuleAlertRelation ruleAlertRelation : ruleAlertRelationDao.findAllByAlert(alert.getId())) {
            ruleAlertRelationDao.delete(ruleAlertRelation);
        }
    }

    @Override
    public List<Alert> findAll() {
        return findBy("NOT deleted ORDER BY name");
    }

    @Override
    public void insert(Alert alert) {
        super.insert(alert);
    }

    @Override
    public void update(Alert alert) {
        super.update(alert);
    }
}
