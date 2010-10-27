package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.model.Site;

import java.util.List;

@SuppressWarnings({"unchecked"})
public class SiteDaoImpl extends BasicDaoImpl implements SiteDao {
    @Override
    public void insert(Site site) {
        makePersistent(site);
    }

    @Override
    public void delete(Site site) {
        site.setDeleted(true);
        //TODO: delete linked entities
    }

    @Override
    public List<Site> findAll() {
        return super.findAll(Site.class, "ORDER BY creationTime DESC");
    }

    @Override
    public Site find(long id) {
        return getObjectById(Site.class, id);
    }
}
