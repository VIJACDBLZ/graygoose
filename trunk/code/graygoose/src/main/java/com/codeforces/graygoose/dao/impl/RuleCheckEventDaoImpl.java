package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
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
    public List<RuleCheckEvent> findAllByRule(long ruleId) {
        //TODO: ensure we have index
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ruleId", ruleId);

        return super.findAll(RuleCheckEvent.class,
                "this.ruleId == ruleId PARAMETERS long ruleId",
                "this.checkTime DESC", parameters, true);
    }

    @Override
    public List<RuleCheckEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMillis) {
        //TODO: ensure we have index
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(RuleCheckEvent.class,
                "this.checkTime >= lowerBound"
                        + " && this.checkTime <= upperBound"
                        + " PARAMETERS java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                "this.checkTime DESC", parameters, true);
    }

    @Override
    public List<RuleCheckEvent> findAllByStatusForPeriod(
            RuleCheckEvent.Status status, long lowerBoundMillis, long upperBoundMillis) {
        //TODO: ensure we have index
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("status", status.toString());
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(RuleCheckEvent.class,
                "this.status == status"
                        + " && this.checkTime >= lowerBound"
                        + " && this.checkTime <= upperBound"
                        + " PARAMETERS String status,"
                        + " java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                "this.checkTime DESC", parameters, true);
    }

    @Override
    public List<RuleCheckEvent> findAllBySiteForPeriod(long siteId, long lowerBoundMillis, long upperBoundMillis) {
        //TODO: ensure we have index
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("siteId", siteId);
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(RuleCheckEvent.class,
                "this.siteId == siteId"
                        + " && this.checkTime >= lowerBound"
                        + " && this.checkTime <= upperBound"
                        + " PARAMETERS long siteId,"
                        + " java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                "this.checkTime DESC", parameters, true);
    }

    @Override
    public List<RuleCheckEvent> findAllByRuleForPeriod(long ruleId, long lowerBoundMillis, long upperBoundMillis) {
        //TODO: ensure we have index
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ruleId", ruleId);
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(RuleCheckEvent.class,
                "this.ruleId == ruleId"
                        + " && this.checkTime >= lowerBound"
                        + " && this.checkTime <= upperBound"
                        + " PARAMETERS long ruleId,"
                        + " java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                "this.checkTime DESC", parameters, true);
    }

    @Override
    public List<RuleCheckEvent> findAllBySiteAndStatusForPeriod(
            long siteId, RuleCheckEvent.Status status, long lowerBoundMillis, long upperBoundMillis) {
        //TODO: ensure we have index
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("siteId", siteId);
        parameters.put("status", status.toString());
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(RuleCheckEvent.class,
                "this.siteId == siteId"
                        + " && this.status == status"
                        + " && this.checkTime >= lowerBound"
                        + " && this.checkTime <= upperBound"
                        + " PARAMETERS long siteId,"
                        + " String status,"
                        + " java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                "this.checkTime DESC", parameters, true);
    }

    @Override
    public List<RuleCheckEvent> findAllByRuleAndStatusForPeriod(
            long ruleId, RuleCheckEvent.Status status, long lowerBoundMillis, long upperBoundMillis) {
        //TODO: ensure we have index
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ruleId", ruleId);
        parameters.put("status", status.toString());
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findAll(RuleCheckEvent.class,
                "this.ruleId == ruleId"
                        + " && this.status == status"
                        + " && this.checkTime >= lowerBound"
                        + " && this.checkTime <= upperBound"
                        + " PARAMETERS long ruleId,"
                        + " String status,"
                        + " java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                "this.checkTime DESC", parameters, true);
    }

    @Override
    public List<Long> findKeysByRuleAndStatusForPeriod(
            long ruleId, RuleCheckEvent.Status status, long lowerBoundMillis, long upperBoundMillis) {
        //TODO: ensure we have index
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ruleId", ruleId);
        parameters.put("status", status.toString());
        parameters.put("lowerBound", new Date(lowerBoundMillis));
        parameters.put("upperBound", new Date(upperBoundMillis));

        return super.findKeys(RuleCheckEvent.class,
                "this.ruleId == ruleId"
                        + " && this.status == status"
                        + " && this.checkTime >= lowerBound"
                        + " && this.checkTime <= upperBound"
                        + " PARAMETERS long ruleId,"
                        + " String status,"
                        + " java.util.Date lowerBound,"
                        + " java.util.Date upperBound",
                "this.checkTime DESC", parameters, true);
    }
}