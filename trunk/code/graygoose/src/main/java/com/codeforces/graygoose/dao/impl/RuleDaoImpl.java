package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.cache.Cacheable;
import com.codeforces.graygoose.dao.cache.InvalidateCache;
import com.codeforces.graygoose.model.AbstractEntity;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.Site;
import com.google.inject.Inject;

import java.util.LinkedList;
import java.util.List;

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
    public void update(Rule rule) {
        super.update(rule);
    }

    @InvalidateCache
    @Override
    public void markDeleted(Rule rule) {
        super.markDeleted(rule);

        for (AbstractEntity dependentEntity : getDependentEntities(rule)) {
            dependentEntity.setDeleted(true);
        }
    }

    //NOT @Cacheable
    private List<AbstractEntity> getDependentEntities(Rule rule) {
        List<AbstractEntity> dependentEntities = new LinkedList<AbstractEntity>();

        dependentEntities.addAll(ruleCheckEventDao.findByRule(rule));
        dependentEntities.addAll(ruleAlertRelationDao.findByRule(rule));

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
        return super.findAll(Rule.class, null, "creationTime DESC", true);
    }

    @Cacheable
    @Override
    public List<Rule> findBySite(Site site) {
        return findBySite(site.getId());
    }

    @Cacheable
    @Override
    public List<Rule> findBySite(long siteId) {
        return super.findAll(Rule.class, String.format("siteId == %d", siteId), "creationTime DESC", true);
    }
}
