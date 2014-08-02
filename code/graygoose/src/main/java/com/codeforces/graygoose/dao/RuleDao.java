package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Rule;

import java.util.List;

public interface RuleDao {
    Rule find(long id);

    Rule find(long id, boolean ignoreDeleted);

    List<Rule> findAll();

    List<Rule> findAllBySite(long siteId);

    void markDeleted(Rule rule);

    void insert(Rule rule);

    void update(Rule rule);
}
