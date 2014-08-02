package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleAlertRelation;
import com.google.inject.Inject;

import java.util.List;

public class RuleDaoImpl extends ApplicationDaoImpl<Rule> implements RuleDao {
    @Inject
    private RuleAlertRelationDao ruleAlertRelationDao;

    @Override
    public Rule find(long id) {
        return super.find(id);
    }

    @Override
    public Rule find(long id, boolean ignoreDeleted) {
        return super.find(id, ignoreDeleted);
    }

    @Override
    public List<Rule> findAll() {
        return super.findAll();
    }

    @Override
    public List<Rule> findAllBySite(long siteId) {
        return findBy("NOT deleted AND siteId=? ORDER BY creationTime DESC", siteId);
    }

    @Override
    public void markDeleted(Rule rule) {
        super.markDeleted(rule);

        for (RuleAlertRelation ruleAlertRelation : ruleAlertRelationDao.findAllByRule(rule.getId())) {
            ruleAlertRelationDao.delete(ruleAlertRelation);
        }
    }

    @Override
    public void insert(Rule rule) {
        super.insert(rule);
    }

    @Override
    public void update(Rule rule) {
        super.update(rule);
    }
}
