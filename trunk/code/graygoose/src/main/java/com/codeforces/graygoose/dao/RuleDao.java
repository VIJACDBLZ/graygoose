package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Rule;

import java.util.List;

public interface RuleDao {
    void insert(Rule rule);

    void delete(Rule rule);

    List<Rule> findAll();

    Rule find(long id);
}
