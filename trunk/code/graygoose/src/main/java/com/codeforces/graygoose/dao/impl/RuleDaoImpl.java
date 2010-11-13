package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.cache.Cacheable;
import com.codeforces.graygoose.dao.cache.InvalidateCache;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleAlertRelation;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class RuleDaoImpl extends BasicDaoImpl<Rule> implements RuleDao {
    @Inject
    private RuleAlertRelationDao ruleAlertRelationDao;

    @InvalidateCache
    @Override
    public void insert(Rule rule) {
        super.insert(rule);
    }

    @InvalidateCache
    @Override
    public void markDeleted(Rule rule) {
        super.markDeleted(rule);

        for (RuleAlertRelation ruleAlertRelation : ruleAlertRelationDao.findAllByRule(rule.getId())) {
            ruleAlertRelationDao.delete(ruleAlertRelation);
        }
    }

    @InvalidateCache
    @Override
    public void update(Rule rule) {
        super.update(rule);
    }

    @Cacheable
    @Override
    public Rule find(long id) {
        return super.find(Rule.class, id);
    }

    @Cacheable
    @Override
    public Rule find(long id, boolean ignoreDeleted) {
        return super.find(Rule.class, id);
    }

    @Cacheable
    @Override
    public List<Rule> findAll() {
        return super.findAll(Rule.class, null, "this.creationTime DESC", null, true);
    }

    @Cacheable
    @Override
    public List<Rule> findAllBySite(long siteId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("siteId", siteId);

        return super.findAll(Rule.class,
                "this.siteId == siteId PARAMETERS long siteId",
                "this.creationTime DESC", parameters, true);
    }
}
