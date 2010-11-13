package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Site;

import java.util.List;

public interface SiteDao extends BasicDao<Site> {
    Site find(long id);

    Site find(long id, boolean ignoreDeleted);

    List<Site> findAll();

    void markDeleted(Site site);

    void insert(Site site);

    void update(Site site);
}
