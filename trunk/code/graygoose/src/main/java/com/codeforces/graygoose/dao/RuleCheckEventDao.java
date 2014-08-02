package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.RuleCheckEvent;

import java.util.List;

public interface RuleCheckEventDao {
    RuleCheckEvent find(long id);

    List<RuleCheckEvent> findAll();

    List<RuleCheckEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMillis);

    List<RuleCheckEvent> findAllByRule(long ruleId);

    List<RuleCheckEvent> findAllByStatusForPeriod(RuleCheckEvent.Status status, long lowerBoundMillis, long upperBoundMillis);

    List<RuleCheckEvent> findAllBySiteForPeriod(long siteId, long lowerBoundMillis, long upperBoundMillis);

    List<RuleCheckEvent> findAllByRuleForPeriod(long ruleId, long lowerBoundMillis, long upperBoundMillis);

    List<RuleCheckEvent> findAllBySiteAndStatusForPeriod(long siteId, RuleCheckEvent.Status status, long lowerBoundMillis, long upperBoundMillis);

    List<RuleCheckEvent> findAllByRuleAndStatusForPeriod(long ruleId, RuleCheckEvent.Status status, long lowerBoundMillis, long upperBoundMillis);

    List<Long> findKeysByRuleAndStatusForPeriod(long ruleId, RuleCheckEvent.Status status, long lowerBoundMillis, long upperBoundMillis);

    void delete(RuleCheckEvent ruleCheckEvent);

    void insert(RuleCheckEvent ruleCheckEvent);

    void update(RuleCheckEvent ruleCheckEvent);
}
