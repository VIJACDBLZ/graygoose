package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.Site;

import java.util.List;

public interface RuleDao extends BasicDao<Rule> {
    List<Rule> findBySite(Site site);

    List<Rule> findBySite(long siteId);
}
