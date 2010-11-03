package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleCheckEventDaoImpl extends BasicDaoImpl<RuleCheckEvent> implements RuleCheckEventDao {
    @Override
    public void insert(RuleCheckEvent ruleCheckEvent) {
        super.insert(ruleCheckEvent);
    }

    @Override
    public void delete(RuleCheckEvent ruleCheckEvent) {
        super.delete(ruleCheckEvent);
    }

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
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ruleId", ruleId);

        return super.findAll(RuleCheckEvent.class,
                "this.ruleId == ruleId PARAMETERS long ruleId",
                null, parameters, true);
    }

    @Override
    public List<RuleCheckEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMillis) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(RuleCheckEvent.class,
                "this.checkTime >= lowerBound"
                        + " && this.checkTime <= upperBound"
                        + " PARAMETERS java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                null, parameters, true);
    }

    @Override
    public List<RuleCheckEvent> findByRuleForPeriod(Rule rule, long lowerBoundMillis, long upperBoundMillis) {
        return findByRuleForPeriod(rule.getId(), lowerBoundMillis, upperBoundMillis);
    }

    @Override
    public List<RuleCheckEvent> findByRuleForPeriod(long ruleId, long lowerBoundMillis, long upperBoundMillis) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(RuleCheckEvent.class,
                String.format("this.ruleId == %d"
                        + " && this.checkTime >= lowerBound"
                        + " && this.checkTime <= upperBound"
                        + " PARAMETERS java.util.Date lowerBound,"
                        + " java.util.Date upperBound", ruleId),
                null, parameters, true);
    }
}