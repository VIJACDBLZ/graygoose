package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.Site;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.link.Link;

@Link("data/rules")
public class RulesDataPage extends DataPage {
    @Parameter
    private Long ruleId;

    private Rule rule;

    @Inject
    private RuleDao ruleDao;

    @Override
    public void initializeAction() {
        super.initializeAction();

        if (ruleId != null) {
            rule = ruleDao.find(ruleId);
        }
    }

    @Action("deleteRule")
    public void onDeleteRule() {
        try {
            if (rule != null) {
                ruleDao.delete(rule);
                put("success", true);
            } else {
                put("error", $("No such rule."));
            }
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Override
    public void action() {
        // No operations.
    }
}
