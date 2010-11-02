package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.dao.cache.Cacheable;
import com.codeforces.graygoose.dao.cache.InvalidateCache;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleAlertRelation;

import java.util.List;

public class RuleAlertRelationDaoImpl extends BasicDaoImpl<RuleAlertRelation> implements RuleAlertRelationDao {
    @Cacheable
    @Override
    public RuleAlertRelation find(long id) {
        return super.find(RuleAlertRelation.class, id);
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findAll() {
        return super.findAll(RuleAlertRelation.class);
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findByRuleAndAlert(Rule rule, Alert alert) {
        return findByRuleAndAlert(rule.getId(), alert.getId());
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findByRuleAndAlert(long ruleId, long alertId) {
        return super.findAll(RuleAlertRelation.class, String.format("ruleId == %d && alertId == %d", ruleId, alertId), null, true);
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findByRule(Rule rule) {
        return findByRule(rule.getId());
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findByRule(long ruleId) {
        return super.findAll(RuleAlertRelation.class, String.format("ruleId == %d", ruleId), null, true);
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findByAlert(Alert alert) {
        return findByAlert(alert.getId());
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findByAlert(long alertId) {
        return super.findAll(RuleAlertRelation.class, String.format("alertId == %d", alertId), null, true);
    }

    @InvalidateCache
    @Override
    public void insert(RuleAlertRelation ruleAlertRelation) {
        super.insert(ruleAlertRelation);
    }

    @InvalidateCache
    @Override
    public void delete(RuleAlertRelation ruleAlertRelation) {
        super.delete(ruleAlertRelation);
    }
}
