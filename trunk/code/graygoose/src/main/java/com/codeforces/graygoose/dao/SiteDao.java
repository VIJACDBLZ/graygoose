package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Site;

import java.util.List;

public interface SiteDao {
    void insert(Site site);
    void delete(Site site);
    List<Site> findAll();
    Site find(long id);
}
