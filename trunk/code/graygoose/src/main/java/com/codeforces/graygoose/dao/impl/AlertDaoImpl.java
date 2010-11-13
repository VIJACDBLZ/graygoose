package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.dao.cache.Cacheable;
import com.codeforces.graygoose.dao.cache.InvalidateCache;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.RuleAlertRelation;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class AlertDaoImpl extends BasicDaoImpl<Alert> implements AlertDao {
    @Inject
    private RuleAlertRelationDao ruleAlertRelationDao;

    @InvalidateCache
    @Override
    public void insert(Alert alert) {
        super.insert(alert);
    }

    @InvalidateCache
    @Override
    public void markDeleted(Alert alert) {
        super.markDeleted(alert);

        for (RuleAlertRelation ruleAlertRelation : ruleAlertRelationDao.findAllByAlert(alert.getId())) {
            ruleAlertRelationDao.delete(ruleAlertRelation);
        }
    }

    @InvalidateCache
    @Override
    public void update(Alert alert) {
        super.update(alert);
    }

    @Cacheable
    @Override
    public Alert find(long id) {
        return super.find(Alert.class, id);
    }

    @Cacheable
    @Override
    public Alert find(long id, boolean ignoreDeleted) {
        return super.find(Alert.class, id, ignoreDeleted);
    }

    @Cacheable
    @Override
    public List<Alert> findAll() {
        return super.findAll(Alert.class, null, "this.name", null, true);
    }
}
