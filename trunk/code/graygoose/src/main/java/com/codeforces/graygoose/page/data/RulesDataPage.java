package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.validation.ResponseCodesValidator;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.link.Link;
import org.nocturne.validation.OptionValidator;
import org.nocturne.validation.RequiredValidator;
import org.nocturne.validation.LengthValidator;
import org.nocturne.validation.IntegerValidator;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Link("data/rules")
public class RulesDataPage extends DataPage {
    @Parameter
    private Long ruleId;

    @Parameter
    private long siteId;

    @Parameter
    private String ruleType;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String expectedCodes;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String expectedSubstring;

    @Parameter
    private String expectedSubstringMinimalCount;

    @Parameter
    private String expectedSubstringMaximalCount;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String expectedRegex;

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

        printTemplateMapAsStringsUsingJson();
    }

    private boolean validateEditOrAdd() {
        addValidator("siteId", new RequiredValidator());

        addValidator("ruleType", new RequiredValidator());
        addValidator("ruleType", new OptionValidator(Rule.RuleType.RESPONSE_CODE_RULE_TYPE.toString(),
                Rule.RuleType.SUBSTRING_RULE_TYPE.toString(),
                Rule.RuleType.REGEX_RULE_TYPE.toString()));

        if (Rule.RuleType.RESPONSE_CODE_RULE_TYPE.toString().equals(ruleType)) {
            addValidator("expectedCodes", new RequiredValidator());
            addValidator("expectedCodes", new ResponseCodesValidator());
        } else if (Rule.RuleType.SUBSTRING_RULE_TYPE.toString().equals(ruleType)) {
            addValidator("expectedSubstring", new RequiredValidator());
            addValidator("expectedSubstring", new LengthValidator(1, 512));

            addValidator("expectedSubstringMinimalCount", new RequiredValidator());
            addValidator("expectedSubstringMinimalCount", new IntegerValidator(0, 1024));

            addValidator("expectedSubstringMaximalCount", new RequiredValidator());
            addValidator("expectedSubstringMaximalCount", new IntegerValidator(0, 1024));
        } else if (Rule.RuleType.REGEX_RULE_TYPE.toString().equals(ruleType)) {
            addValidator("expectedRegex", new RequiredValidator());
            addValidator("expectedRegex", new LengthValidator(1, 512));
        }

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
            Rule rule = new Rule(siteId, Rule.RuleType.valueOf(ruleType), new Date());
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
        Map<String, String> properties = new TreeMap<String, String>();

        switch (rule.getRuleType()) {
            case RESPONSE_CODE_RULE_TYPE:
                properties.put("expectedCodes", expectedCodes);
                break;
            case SUBSTRING_RULE_TYPE:
                properties.put("expectedSubstring", expectedSubstring);
                properties.put("expectedSubstringMinimalCount", expectedSubstringMinimalCount);
                properties.put("expectedSubstringMaximalCount", expectedSubstringMaximalCount);
                break;
            case REGEX_RULE_TYPE:
                properties.put("expectedRegex", expectedRegex);
                break;
            default:
        }

        rule.setData(properties);
    }

    @Override
    public void action() {
        // No operations.
    }
}
