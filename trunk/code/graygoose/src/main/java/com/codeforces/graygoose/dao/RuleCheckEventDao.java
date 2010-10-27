package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.Site;
import com.codeforces.graygoose.model.RuleCheckEvent;

import java.util.List;

public interface RuleCheckEventDao {
    void insert(RuleCheckEvent ruleCheckEvent);

    void delete(RuleCheckEvent ruleCheckEvent);

    List<RuleCheckEvent> findAll();

    List<RuleCheckEvent> findByRule(Rule rule);

    List<RuleCheckEvent> findByRule(long ruleId);

    RuleCheckEvent find(long id);
}