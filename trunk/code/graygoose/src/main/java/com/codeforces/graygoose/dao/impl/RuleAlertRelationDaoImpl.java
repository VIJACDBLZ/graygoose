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
        deletePersistent(ruleAlertRelation);
    }

    @Override
    public List<RuleAlertRelation> findAll() {
        return (List<RuleAlertRelation>) execute("SELECT FROM " + RuleAlertRelation.class.getName());
    }

    @Override
    public List<RuleAlertRelation> findByRule(Rule rule) {
        return findByRule(rule.getId());
    }

    @Override
    public List<RuleAlertRelation> findByRule(long ruleId) {
        return (List<RuleAlertRelation>) execute(String.format(
                "SELECT FROM " + RuleAlertRelation.class.getName() + " WHERE ruleId = %d", ruleId));
    }

    @Override
    public List<RuleAlertRelation> findByAlert(Alert alert) {
        return findByAlert(alert.getId());
    }

    @Override
    public List<RuleAlertRelation> findByAlert(long alertId) {
        return (List<RuleAlertRelation>) execute(String.format(
                "SELECT FROM " + RuleAlertRelation.class.getName() + " WHERE alertId = %d", alertId));
    }
}
