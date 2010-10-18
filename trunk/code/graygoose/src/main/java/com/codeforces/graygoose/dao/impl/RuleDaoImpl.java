package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.model.Rule;

import java.util.List;

@SuppressWarnings({"unchecked"})
public class RuleDaoImpl extends BasicDaoImpl implements RuleDao {

    @Override
    public void insert(Rule rule) {
        makePersistent(rule);
    }

    @Override
    public void delete(Rule rule) {
        deletePersistent(rule);
    }

    @Override
    public List<Rule> findAll() {
        return (List<Rule>) execute("SELECT FROM " + Rule.class.getName() + " ORDER BY creationTime DESC");
    }

    @Override
    public Rule find(long id) {
        return getObjectById(Rule.class, id);
    }
}
