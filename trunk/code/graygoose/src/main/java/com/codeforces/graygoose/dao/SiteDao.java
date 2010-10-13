package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Site;

import java.util.List;

public interface SiteDao {
    void insert(Site site);
    List<Site> findAll();
}
