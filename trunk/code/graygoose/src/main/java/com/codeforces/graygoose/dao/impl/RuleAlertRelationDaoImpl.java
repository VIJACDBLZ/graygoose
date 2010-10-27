package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleAlertRelation;

import java.util.List;

@SuppressWarnings({"unchecked"})
public class RuleAlertRelationDaoImpl extends BasicDaoImpl implements RuleAlertRelationDao {
    @Override
    public void insert(RuleAlertRelation ruleAlertRelation) {
        makePersistent(ruleAlertRelation);
    }

    @Override
    public void delete(RuleAlertRelation ruleAlertRelation) {
        ruleAlertRelation.setDeleted(true);
        //TODO: delete linked entities
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
        return super.findAll(RuleAlertRelation.class, "WHERE ruleId == %d && alertId == %d", ruleId, alertId);
    }

    @Override
    public List<RuleAlertRelation> findByRule(Rule rule) {
        return findByRule(rule.getId());
    }

    @Override
    public List<RuleAlertRelation> findByRule(long ruleId) {
        return super.findAll(RuleAlertRelation.class, "WHERE ruleId == %d", ruleId);
    }

    @Override
    public List<RuleAlertRelation> findByAlert(Alert alert) {
        return findByAlert(alert.getId());
    }

    @Override
    public List<RuleAlertRelation> findByAlert(long alertId) {
        return super.findAll(RuleAlertRelation.class, "WHERE alertId == %d", alertId);
    }
}
