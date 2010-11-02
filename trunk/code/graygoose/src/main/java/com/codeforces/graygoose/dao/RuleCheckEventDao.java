package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;
import com.codeforces.graygoose.model.Alert;

import java.util.List;

public interface RuleCheckEventDao extends BasicDao<RuleCheckEvent> {
    RuleCheckEvent find(long id);

    List<RuleCheckEvent> findAll();

    List<RuleCheckEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMilli);

    List<RuleCheckEvent> findByRule(Rule rule);

    List<RuleCheckEvent> findByRule(long ruleId);

    List<RuleCheckEvent> findByRuleForPeriod(Rule rule, long lowerBoundMillis, long upperBoundMilli);

    List<RuleCheckEvent> findByRuleForPeriod(long ruleId, long lowerBoundMillis, long upperBoundMilli);

    void delete(RuleCheckEvent ruleCheckEvent);

    void insert(RuleCheckEvent ruleCheckEvent);
}