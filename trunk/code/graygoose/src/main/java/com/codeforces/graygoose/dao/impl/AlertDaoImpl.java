package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.dao.cache.Cacheable;
import com.codeforces.graygoose.dao.cache.InvalidateCache;
import com.codeforces.graygoose.model.AbstractEntity;
import com.codeforces.graygoose.model.Alert;
import com.google.inject.Inject;

import java.util.LinkedList;
import java.util.List;

public class AlertDaoImpl extends BasicDaoImpl<Alert> implements AlertDao {
    @Inject
    private AlertTriggerEventDao alertTriggerEventDao;

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

        for (AbstractEntity dependentEntity : getDependentEntities(alert)) {
            dependentEntity.setDeleted(true);
        }
    }

    @InvalidateCache
    @Override
    public void update(Alert alert) {
        super.update(alert);
    }

    private List<AbstractEntity> getDependentEntities(Alert alert) {
        List<AbstractEntity> dependentEntities = new LinkedList<AbstractEntity>();

        dependentEntities.addAll(alertTriggerEventDao.findAllByAlert(alert.getId()));
        dependentEntities.addAll(ruleAlertRelationDao.findAllByAlert(alert.getId()));

        return dependentEntities;
    }

    @Cacheable
    @Override
    public Alert find(long id) {
        return super.find(Alert.class, id);
    }

    @Cacheable
    @Override
    public List<Alert> findAll() {
        return super.findAll(Alert.class, null, "this.name", null, true);
    }
}
