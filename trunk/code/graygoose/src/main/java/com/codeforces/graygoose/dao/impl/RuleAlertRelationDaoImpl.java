package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.dao.cache.Cacheable;
import com.codeforces.graygoose.dao.cache.InvalidateCache;
import com.codeforces.graygoose.model.RuleAlertRelation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleAlertRelationDaoImpl extends BasicDaoImpl<RuleAlertRelation> implements RuleAlertRelationDao {
    @Cacheable
    @Override
    public RuleAlertRelation find(long id) {
        return super.find(RuleAlertRelation.class, id);
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findAll() {
        return super.findAll(RuleAlertRelation.class);
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findByRuleAndAlert(long ruleId, long alertId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ruleId", ruleId);
        parameters.put("alertId", alertId);

        return super.findAll(RuleAlertRelation.class,
                "this.ruleId == ruleId && this.alertId == alertId"
                        + " PARAMETERS long ruleId, long alertId",
                null, parameters, true);
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findByRule(long ruleId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ruleId", ruleId);

        return super.findAll(RuleAlertRelation.class,
                "this.ruleId == ruleId PARAMETERS long ruleId",
                null, parameters, true);
    }

    @Cacheable
    @Override
    public List<RuleAlertRelation> findByAlert(long alertId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("alertId", alertId);

        return super.findAll(RuleAlertRelation.class,
                "this.alertId == alertId PARAMETERS long alertId",
                null, parameters, true);
    }

    @InvalidateCache
    @Override
    public void insert(RuleAlertRelation ruleAlertRelation) {
        super.insert(ruleAlertRelation);
    }

    @InvalidateCache
    @Override
    public void delete(RuleAlertRelation ruleAlertRelation) {
        super.delete(ruleAlertRelation);
    }

    @InvalidateCache
    @Override
    public void update(RuleAlertRelation ruleAlertRelation) {
        super.update(ruleAlertRelation);
    }
}
