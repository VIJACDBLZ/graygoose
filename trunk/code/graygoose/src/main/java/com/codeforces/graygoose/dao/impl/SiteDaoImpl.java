package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.dao.cache.Cacheable;
import com.codeforces.graygoose.dao.cache.InvalidateCache;
import com.codeforces.graygoose.model.AbstractEntity;
import com.codeforces.graygoose.model.Site;
import com.google.inject.Inject;

import java.util.LinkedList;
import java.util.List;

public class SiteDaoImpl extends BasicDaoImpl<Site> implements SiteDao {
    @Inject
    private RuleDao ruleDao;

    @InvalidateCache
    @Override
    public void insert(Site site) {
        super.insert(site);
    }

    @InvalidateCache
    @Override
    public void markDeleted(Site site) {
        super.markDeleted(site);

        for (AbstractEntity dependentEntity : getDependentEntities(site)) {
            dependentEntity.setDeleted(true);
        }
    }

    @InvalidateCache
    @Override
    public void update(Site site) {
        super.update(site);
    }

    private List<AbstractEntity> getDependentEntities(Site site) {
        List<AbstractEntity> dependentEntities = new LinkedList<AbstractEntity>();

        dependentEntities.addAll(ruleDao.findAllBySite(site.getId()));

        return dependentEntities;
    }

    @Cacheable
    @Override
    public Site find(long id) {
        return super.find(Site.class, id);
    }

    @Cacheable
    @Override
    public List<Site> findAll() {
        return super.findAll(Site.class, null, "this.creationTime DESC", null, true);
    }
}
