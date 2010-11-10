package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.RuleAlertRelation;

import java.util.List;

public interface RuleAlertRelationDao extends BasicDao<RuleAlertRelation> {
    RuleAlertRelation find(long id);

    List<RuleAlertRelation> findAll();

    List<RuleAlertRelation> findAllByRuleAndAlert(long ruleId, long alertId);

    List<RuleAlertRelation> findAllByRule(long ruleId);

    List<RuleAlertRelation> findAllByAlert(long alertId);

    void insert(RuleAlertRelation ruleAlertRelation);

    void delete(RuleAlertRelation ruleAlertRelation);

    void update(RuleAlertRelation ruleAlertRelation);
}
