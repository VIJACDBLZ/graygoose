package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;

import java.util.List;

public interface RuleCheckEventDao extends BasicDao<RuleCheckEvent> {
    List<RuleCheckEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMilli);

    List<RuleCheckEvent> findByRule(Rule rule);

    List<RuleCheckEvent> findByRule(long ruleId);

    List<RuleCheckEvent> findByRuleForPeriod(Rule rule, long lowerBoundMillis, long upperBoundMilli);

    List<RuleCheckEvent> findByRuleForPeriod(long ruleId, long lowerBoundMillis, long upperBoundMilli);
}