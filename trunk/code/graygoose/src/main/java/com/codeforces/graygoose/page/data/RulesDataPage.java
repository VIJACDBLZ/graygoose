package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.util.RuleTypeUtil;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.link.Link;
import org.nocturne.validation.OptionValidator;
import org.nocturne.validation.RequiredValidator;

import java.util.Date;
import java.util.Map;

@Link("data/rules")
public class RulesDataPage extends DataPage {
    @Parameter
    private Long ruleId;

    @Parameter
    private long siteId;

    @Parameter
    private String ruleType;

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
                ruleDao.markDeleted(rule);
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

        printTemplateMapAsStringsUsingJson();
    }

    private boolean validateEditOrAdd() {
        addValidator("siteId", new RequiredValidator());

        addValidator("ruleType", new RequiredValidator());

        Rule.RuleType[] ruleTypes = Rule.RuleType.values();
        String[] ruleTypeNames = new String[ruleTypes.length];

        for (int ruleTypeIndex = 0; ruleTypeIndex < ruleTypes.length; ++ruleTypeIndex) {
            ruleTypeNames[ruleTypeIndex] = ruleTypes[ruleTypeIndex].toString();
        }

        addValidator("ruleType", new OptionValidator((Object[]) ruleTypeNames));

        RuleTypeUtil.addRulePropertyValidators(this, ruleType);

        return runValidationAndPrintErrors();
    }

    @Validate("add")
    public boolean validateAdd() {
        return validateEditOrAdd();
    }

    @Validate("edit")
    public boolean validateEdit() {
        addValidator("ruleId", new RequiredValidator());
        return validateEditOrAdd();
    }

    @Action("edit")
    public void onEdit() {
        if (rule != null) {
            rule.setRuleType(Rule.RuleType.valueOf(ruleType));
            setupRuleProperties(rule);

            put("success", true);
        } else {
            put("error", $("Can't find rule to update."));
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Action("add")
    public void onAdd() {
        try {
            Rule rule = new Rule(siteId, Rule.RuleType.valueOf(ruleType));
            setupRuleProperties(rule);
            ruleDao.insert(rule);

            put("success", true);
            setMessage($("Rule has been added."));
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    private void setupRuleProperties(Rule rule) {
        RuleTypeUtil.setupRuleProperties(rule, this);
    }

    @Override
    public void action() {
        // No operations.
    }
}
