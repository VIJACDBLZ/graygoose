package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleAlertRelation;

import java.util.List;

public interface RuleAlertRelationDao {
    void insert(RuleAlertRelation ruleAlertRelation);

    void delete(RuleAlertRelation ruleAlertRelation);

    List<RuleAlertRelation> findAll();

    List<RuleAlertRelation> findByRule(Rule rule);

    List<RuleAlertRelation> findByRule(long ruleId);

    List<RuleAlertRelation> findByAlert(Alert alert);

    List<RuleAlertRelation> findByAlert(long alertId);
}
