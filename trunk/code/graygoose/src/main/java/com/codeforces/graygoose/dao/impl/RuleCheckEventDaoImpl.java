package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;

import java.util.List;

public class RuleCheckEventDaoImpl extends BasicDaoImpl<RuleCheckEvent> implements RuleCheckEventDao {
    @Override
    public RuleCheckEvent find(long id) {
        return super.find(RuleCheckEvent.class, id);
    }

    @Override
    public List<RuleCheckEvent> findAll() {
        return super.findAll(RuleCheckEvent.class);
    }

    @Override
    public List<RuleCheckEvent> findByRule(Rule rule) {
        return findByRule(rule.getId());
    }

    @Override
    public List<RuleCheckEvent> findByRule(long ruleId) {
        return super.findAll(RuleCheckEvent.class, String.format("ruleId == %d", ruleId), null, true);
    }
}