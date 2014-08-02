package com.codeforces.graygoose.web.page.data;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.misc.Noty;
import com.codeforces.graygoose.model.Response;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.util.ResponseCheckingService;
import com.codeforces.graygoose.util.RuleTypeUtil;
import com.codeforces.graygoose.util.UrlUtil;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.link.Link;
import org.nocturne.validation.OptionValidator;
import org.nocturne.validation.RequiredValidator;

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
                put("error", $("No such rule"));
            }
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @SuppressWarnings({"KeySetIterationMayUseEntrySet"})
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
                put("error", $("No such rule"));
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

            ruleDao.update(rule);
            put("success", true);
        } else {
            put("error", $("Can't find rule to update"));
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Action("add")
    public void onAdd() {
        try {
            Rule newRule = Rule.newRule(siteId, Rule.RuleType.valueOf(ruleType));
            setupRuleProperties(newRule);

            ruleDao.insert(newRule);

            put("success", true);
            addMessage($("Rule has been added"), Noty.Type.SUCCESS);
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Action("checkRule")
    public void onCheckRule() {
        try {
            Response response = Response.newResponse(
                    getString("url"), getInteger("responseCode"), getString("responseText"));

            String errorMessage = ResponseCheckingService.getErrorMessageOrNull(
                    getString("siteName"), response, ruleDao.find(getLong("ruleId")));

            if (errorMessage == null) {
                put("success", true);
            } else {
                put("error", errorMessage);
            }
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Action("fetch")
    public void onFetch() {
        Response response = UrlUtil.fetchUrl(getString("url"), 1);

        put("responseCode", response.getCode());
        put("responseText", response.getText());
        put("success", true);

        printTemplateMapAsStringsUsingJson("success", "error", "responseCode", "responseText");
    }

    private void setupRuleProperties(Rule rule) {
        RuleTypeUtil.setupRuleProperties(rule, this);
    }
}
