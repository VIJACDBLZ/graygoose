package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.Site;
import com.google.inject.Inject;

import java.util.List;

public class SiteDaoImpl extends ApplicationDaoImpl<Site> implements SiteDao {
    @Inject
    private RuleDao ruleDao;

    @Override
    public Site find(long id) {
        return super.find(id);
    }

    @Override
    public Site find(long id, boolean ignoreDeleted) {
        return super.find(id, ignoreDeleted);
    }

    @Override
    public void markDeleted(Site site) {
        super.markDeleted(site);

        for (Rule rule : ruleDao.findAllBySite(site.getId())) {
            ruleDao.markDeleted(rule);
        }
    }

    @Override
    public List<Site> findAll() {
        return super.findAll();
    }

    @Override
    public void insert(Site site) {
        super.insert(site);
    }

    @Override
    public void update(Site site) {
        super.update(site);
    }
}
