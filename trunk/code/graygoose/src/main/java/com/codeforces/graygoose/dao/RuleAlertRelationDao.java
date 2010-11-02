package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleAlertRelation;

import java.util.List;

public interface RuleAlertRelationDao extends BasicDao<RuleAlertRelation> {
    RuleAlertRelation find(long id);

    List<RuleAlertRelation> findAll();

    List<RuleAlertRelation> findByRuleAndAlert(Rule rule, Alert alert);

    List<RuleAlertRelation> findByRuleAndAlert(long ruleId, long alertId);

    List<RuleAlertRelation> findByRule(Rule rule);

    List<RuleAlertRelation> findByRule(long ruleId);

    List<RuleAlertRelation> findByAlert(Alert alert);

    List<RuleAlertRelation> findByAlert(long alertId);

    void insert(RuleAlertRelation ruleAlertRelation);

    void delete(RuleAlertRelation ruleAlertRelation);
}
