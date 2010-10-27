package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.Site;
import com.codeforces.graygoose.model.AbstractEntity;
import com.codeforces.graygoose.model.Alert;
import com.google.inject.Inject;

import java.util.LinkedList;
import java.util.List;

public class RuleDaoImpl extends BasicDaoImpl<Rule> implements RuleDao {

    @Inject
    private RuleCheckEventDao ruleCheckEventDao;

    @Inject
    private RuleAlertRelationDao ruleAlertRelationDao;

    @Override
    public void delete(Rule rule) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void markDeleted(Rule rule) {
        super.markDeleted(rule);

        for (AbstractEntity dependentEntities : getDependentEntities(rule)) {
            dependentEntities.setDeleted(true);
        }
    }

    @Override
    public void unmarkDeleted(Rule rule) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Rule find(long id) {
        return super.find(Rule.class, id);
    }

    @Override
    public List<Rule> findAll() {
        return super.findAll(Rule.class, null, "creationTime DESC", true);
    }

    @Override
    public List<Rule> findBySite(Site site) {
        return findBySite(site.getId());
    }

    @Override
    public List<Rule> findBySite(long siteId) {
        //TODO: check that rule properties are retrieved
        /*List<Rule> allRules = findAll();
        List<Rule> rulesBySite = new LinkedList<Rule>();

        for (Rule rule : allRules) {
            if (rule.getSiteId() == siteId)
                rulesBySite.add(rule);
        }

        return rulesBySite;*/
        return super.findAll(Rule.class, String.format("siteId == %d", siteId), "creationTime DESC", true);
    }

    private List<AbstractEntity> getDependentEntities(Rule rule) {
        List<AbstractEntity> dependentEntities = new LinkedList<AbstractEntity>();

        dependentEntities.addAll(ruleCheckEventDao.findByRule(rule));
        dependentEntities.addAll(ruleAlertRelationDao.findByRule(rule));

        return dependentEntities;
    }
}
