package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;

import java.util.List;

@SuppressWarnings({"unchecked"})
public class RuleCheckEventDaoImpl extends BasicDaoImpl implements RuleCheckEventDao {
    @Override
    public void insert(RuleCheckEvent ruleCheckEvent) {
        makePersistent(ruleCheckEvent);
    }

    @Override
    public void delete(RuleCheckEvent ruleCheckEvent) {
        ruleCheckEvent.setDeleted(true);
        //TODO: delete linked entities
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
        return super.findAll(RuleCheckEvent.class, "WHERE ruleId == %d", ruleId);
    }

    @Override
    public RuleCheckEvent find(long id) {
        return getObjectById(RuleCheckEvent.class, id);
    }
}