package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.RuleCheckEvent;

import java.util.List;

public interface RuleCheckEventDao extends BasicDao<RuleCheckEvent> {
    RuleCheckEvent find(long id);

    List<RuleCheckEvent> findAll();

    List<RuleCheckEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMillis);

    List<RuleCheckEvent> findByRule(long ruleId);

    List<RuleCheckEvent> findByStatusForPeriod(RuleCheckEvent.Status status, long lowerBoundMillis, long upperBoundMillis);

    List<RuleCheckEvent> findByRuleForPeriod(long ruleId, long lowerBoundMillis, long upperBoundMillis);

    List<RuleCheckEvent> findByRuleAndStatusForPeriod(long ruleId, RuleCheckEvent.Status status, long lowerBoundMillis, long upperBoundMillis);

    void delete(RuleCheckEvent ruleCheckEvent);

    void insert(RuleCheckEvent ruleCheckEvent);
}