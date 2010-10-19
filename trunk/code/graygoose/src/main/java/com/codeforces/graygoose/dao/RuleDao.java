package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.Site;

import java.util.List;

public interface RuleDao {
    void insert(Rule rule);

    void delete(Rule rule);

    List<Rule> findAll();

    List<Rule> findBySite(Site site);

    List<Rule> findBySite(long siteId);

    Rule find(long id);
}
