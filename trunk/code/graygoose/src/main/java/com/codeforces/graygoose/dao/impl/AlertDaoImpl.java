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
    public void update(Alert alert) {
        super.update(alert);
    }

    @InvalidateCache
    @Override
    public void markDeleted(Alert alert) {
        super.markDeleted(alert);

        for (AbstractEntity dependentEntity : getDependentEntities(alert)) {
            dependentEntity.setDeleted(true);
        }
    }

    //NOT @Cacheable
    private List<AbstractEntity> getDependentEntities(Alert alert) {
        List<AbstractEntity> dependentEntities = new LinkedList<AbstractEntity>();

        dependentEntities.addAll(alertTriggerEventDao.findByAlert(alert));
        dependentEntities.addAll(ruleAlertRelationDao.findByAlert(alert));

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
        return super.findAll(Alert.class, null, "name", true);
    }
}
