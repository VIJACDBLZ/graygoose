package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.Site;
import com.codeforces.graygoose.page.web.SiteEditPage;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.link.Link;
import org.nocturne.validation.*;

import java.util.Map;
import java.util.Date;
import java.net.URL;
import java.net.MalformedURLException;

@Link("data/rules")
public class RulesDataPage extends DataPage {
    @Parameter
    private Long ruleId;

    @Parameter
    private String ruleType;

    @Parameter
    private String expectedCodes;

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

    @Action("findById")
    public void onFindById() {
        try {
            if (rule != null) {
                put("ruleType", rule.getRuleType());

                Map<String, String> settings = rule.getData();

                for (String settingName : settings.keySet()) {
                    put(settingName, settings.get(settingName));
                }

                put("success", true);
            } else {
                put("error", $("No such rule."));
            }
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("ruleType", "success", "error");
    }

    private boolean validateEditOrAdd() {
        addValidator("ruleType", new RequiredValidator());
        addValidator("ruleType", new OptionValidator(Rule.RuleType.RESPONSE_CODE_RULE_TYPE.toString(),
                Rule.RuleType.SUBSTRING_RULE_TYPE.toString(),
                Rule.RuleType.REGEX_RULE_TYPE.toString()));

        if (Rule.RuleType.RESPONSE_CODE_RULE_TYPE.toString().equals(ruleType)) {
            addValidator("expectedCodes", new RequiredValidator());
        }

        return runValidation();
    }

    @Validate("add")
    public boolean validateAdd() {
        return validateEditOrAdd();
    }

    @Validate("edit")
    public boolean validateEdit() {
        return validateEditOrAdd();
    }

    @Action("edit")
    public void onEdit() {
        if (rule != null) {
            rule.setRuleType(Rule.RuleType.valueOf(ruleType));

            put("success", true);
        } else {
            put("error", $("Can't find rule to update."));
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    /*@Action("add")
    public void onAdd() {
        Site site = new Site(name, url, rescanPeriod, new Date());
        siteDao.insert(site);
        setMessage($("Site has been added."));
        abortWithRedirect(redirectPageClass);
    }*/

    @Override
    public void action() {
        // No operations.
    }
}
