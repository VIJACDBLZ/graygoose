package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleAlertRelation;

import java.util.List;

public class RuleAlertRelationDaoImpl extends BasicDaoImpl<RuleAlertRelation> implements RuleAlertRelationDao {
    @Override
    public RuleAlertRelation find(long id) {
        return super.find(RuleAlertRelation.class, id);
    }

    @Override
    public List<RuleAlertRelation> findAll() {
        return super.findAll(RuleAlertRelation.class);
    }

    @Override
    public List<RuleAlertRelation> findByRuleAndAlert(Rule rule, Alert alert) {
        return findByRuleAndAlert(rule.getId(), alert.getId());
    }

    @Override
    public List<RuleAlertRelation> findByRuleAndAlert(long ruleId, long alertId) {
        return super.findAll(RuleAlertRelation.class, String.format("ruleId == %d && alertId == %d", ruleId, alertId), null, true);
    }

    @Override
    public List<RuleAlertRelation> findByRule(Rule rule) {
        return findByRule(rule.getId());
    }

    @Override
    public List<RuleAlertRelation> findByRule(long ruleId) {
        return super.findAll(RuleAlertRelation.class, String.format("ruleId == %d", ruleId), null, true);
    }

    @Override
    public List<RuleAlertRelation> findByAlert(Alert alert) {
        return findByAlert(alert.getId());
    }

    @Override
    public List<RuleAlertRelation> findByAlert(long alertId) {
        return super.findAll(RuleAlertRelation.class, String.format("alertId == %d", alertId), null, true);
    }
}
