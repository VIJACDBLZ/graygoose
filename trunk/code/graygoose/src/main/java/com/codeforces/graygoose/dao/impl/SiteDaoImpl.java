package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.model.AbstractEntity;
import com.codeforces.graygoose.model.Site;
import com.google.inject.Inject;

import java.util.LinkedList;
import java.util.List;

public class SiteDaoImpl extends BasicDaoImpl<Site> implements SiteDao {

    @Inject
    private RuleDao ruleDao;

    @Override
    public void delete(Site site) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void markDeleted(Site site) {
        super.markDeleted(site);

        for (AbstractEntity dependentEntity : getDependentEntities(site)) {
            dependentEntity.setDeleted(true);
        }
    }

    @Override
    public void unmarkDeleted(Site site) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Site find(long id) {
        return super.find(Site.class, id);
    }

    @Override
    public List<Site> findAll() {
        return super.findAll(Site.class, null, "creationTime DESC", true);
    }

    private List<AbstractEntity> getDependentEntities(Site site) {
        List<AbstractEntity> dependentEntities = new LinkedList<AbstractEntity>();

        dependentEntities.addAll(ruleDao.findBySite(site));

        return dependentEntities;
    }
}
