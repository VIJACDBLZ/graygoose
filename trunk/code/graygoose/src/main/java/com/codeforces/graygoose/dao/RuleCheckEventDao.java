package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;

import java.util.List;

public interface RuleCheckEventDao extends BasicDao<RuleCheckEvent> {
    List<RuleCheckEvent> findByRule(Rule rule);

    List<RuleCheckEvent> findByRule(long ruleId);
}