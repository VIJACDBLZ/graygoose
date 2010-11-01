package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleCheckEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

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
    public List<RuleCheckEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMillis) {
        List<RuleCheckEvent> ruleCheckEvents = super.findAll(RuleCheckEvent.class);
        List<RuleCheckEvent> result = new ArrayList<RuleCheckEvent>();

        for (RuleCheckEvent ruleCheckEvent : ruleCheckEvents) {
            Date checkTime = ruleCheckEvent.getCheckTime();
            if (checkTime != null
                    && checkTime.getTime() >= lowerBoundMillis
                    && checkTime.getTime() <= upperBoundMillis) {
                result.add(ruleCheckEvent);
            }
        }

        return result;
    }

    @Override
    public List<RuleCheckEvent> findByRule(Rule rule) {
        return findByRule(rule.getId());
    }

    @Override
    public List<RuleCheckEvent> findByRule(long ruleId) {
        return super.findAll(RuleCheckEvent.class, String.format("ruleId == %d", ruleId), null, true);
    }

    @Override
    public List<RuleCheckEvent> findByRuleForPeriod(Rule rule, long lowerBoundMillis, long upperBoundMillis) {
        return findByRuleForPeriod(rule.getId(), lowerBoundMillis, upperBoundMillis);
    }

    @Override
    public List<RuleCheckEvent> findByRuleForPeriod(long ruleId, long lowerBoundMillis, long upperBoundMillis) {
        //TODO: DATETIME is not supported for now
        /*Date upperBound = new Date(upperBoundMillis);
        Date lowerBound = new Date(lowerBoundMillis);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder whereClause = new StringBuilder();
        whereClause
                .append("checkTime >= DATETIME('").append(dateFormat.format(lowerBound))
                .append("') && checkTime <= DATETIME('").append(dateFormat.format(upperBound))
                .append("') && ruleId == ").append(ruleId);

        return super.findAll(RuleCheckEvent.class, whereClause.toString(), null, true);*/

        List<RuleCheckEvent> ruleCheckEvents = super.findAll(RuleCheckEvent.class);
        List<RuleCheckEvent> result = new ArrayList<RuleCheckEvent>();

        for (RuleCheckEvent ruleCheckEvent : ruleCheckEvents) {
            Date checkTime = ruleCheckEvent.getCheckTime();
            if (ruleCheckEvent.getRuleId() == ruleId
                    && checkTime != null
                    && checkTime.getTime() >= lowerBoundMillis
                    && checkTime.getTime() <= upperBoundMillis) {
                result.add(ruleCheckEvent);
            }
        }

        return result;
    }
}