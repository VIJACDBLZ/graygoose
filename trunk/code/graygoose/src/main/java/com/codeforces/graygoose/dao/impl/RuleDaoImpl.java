package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.Site;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"unchecked"})
public class RuleDaoImpl extends BasicDaoImpl implements RuleDao {

    @Override
    public void insert(Rule rule) {
        makePersistent(rule);
    }

    @Override
    public void delete(Rule rule) {
        rule.setDeleted(true);
        //TODO: delete linked entities
    }

    @Override
    public List<Rule> findAll() {
        return super.findAll(Rule.class, "ORDER BY creationTime DESC");
    }

    @Override
    public List<Rule> findBySite(Site site) {
        return findBySite(site.getId());
    }

    @Override
    public List<Rule> findBySite(long siteId) {
        List<Rule> allRules = findAll();
        List<Rule> rulesBySite = new LinkedList<Rule>();

        for (Rule rule : allRules) {
            if (rule.getSiteId() == siteId)
                rulesBySite.add(rule);
        }

        return rulesBySite;
    }

    @Override
    public Rule find(long id) {
        return getObjectById(Rule.class, id);
    }
}
