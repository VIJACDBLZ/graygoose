package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.model.RuleAlertRelation;

import java.util.List;

public class RuleAlertRelationDaoImpl extends ApplicationDaoImpl<RuleAlertRelation> implements RuleAlertRelationDao {
    @Override
    public List<RuleAlertRelation> findAllByRuleAndAlert(long ruleId, long alertId) {
        return findBy("NOT deleted AND ruleId=? AND alertId=? ORDER BY creationTime DESC", ruleId, alertId);
    }

    @Override
    public List<RuleAlertRelation> findAllByRule(long ruleId) {
        return findBy("NOT deleted AND ruleId=? ORDER BY creationTime DESC", ruleId);
    }

    @Override
    public List<RuleAlertRelation> findAllByAlert(long alertId) {
        return findBy("NOT deleted AND alertId=? ORDER BY creationTime DESC", alertId);
    }

    @Override
    public RuleAlertRelation find(long id) {
        return super.find(id);
    }

    @Override
    public List<RuleAlertRelation> findAll() {
        return super.findAll();
    }

    @Override
    public void insert(RuleAlertRelation object) {
        super.insert(object);
    }

    @Override
    public void update(RuleAlertRelation object) {
        super.update(object);
    }

    @Override
    public void delete(RuleAlertRelation object) {
        super.delete(object);
    }
}
