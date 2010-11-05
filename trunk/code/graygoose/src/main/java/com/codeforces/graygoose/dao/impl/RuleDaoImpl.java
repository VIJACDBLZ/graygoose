package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.cache.Cacheable;
import com.codeforces.graygoose.dao.cache.InvalidateCache;
import com.codeforces.graygoose.model.AbstractEntity;
import com.codeforces.graygoose.model.Rule;
import com.google.inject.Inject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RuleDaoImpl extends BasicDaoImpl<Rule> implements RuleDao {
    @Inject
    private RuleCheckEventDao ruleCheckEventDao;

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

        for (AbstractEntity dependentEntity : getDependentEntities(rule)) {
            dependentEntity.setDeleted(true);
        }
    }

    @InvalidateCache
    @Override
    public void update(Rule rule) {
        super.update(rule);
    }

    private List<AbstractEntity> getDependentEntities(Rule rule) {
        List<AbstractEntity> dependentEntities = new LinkedList<AbstractEntity>();

        dependentEntities.addAll(ruleCheckEventDao.findByRule(rule.getId()));
        dependentEntities.addAll(ruleAlertRelationDao.findByRule(rule.getId()));

        return dependentEntities;
    }

    @Cacheable
    @Override
    public Rule find(long id) {
        return super.find(Rule.class, id);
    }

    @Cacheable
    @Override
    public List<Rule> findAll() {
        return super.findAll(Rule.class, null, "this.creationTime DESC", null, true);
    }

    @Cacheable
    @Override
    public List<Rule> findBySite(long siteId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("siteId", siteId);

        return super.findAll(Rule.class,
                "this.siteId == siteId PARAMETERS long siteId",
                "this.creationTime DESC", parameters, true);
    }
}
