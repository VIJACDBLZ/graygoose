package com.codeforces.graygoose.util;

import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.page.ApplicationPage;
import com.codeforces.graygoose.validation.PatternValidator;
import com.codeforces.graygoose.validation.ResponseCodesValidator;
import org.nocturne.main.Component;
import org.nocturne.validation.IntegerValidator;
import org.nocturne.validation.LengthValidator;
import org.nocturne.validation.RequiredValidator;

import java.util.LinkedHashMap;
import java.util.Map;

public class RuleTypeUtil {
    public static void setupRuleProperties(Rule rule, Component component) {
        Map<String, String> properties = new LinkedHashMap<String, String>();

        for (String property : rule.getRuleType().getPropertyNames()) {
            properties.put(property, component.getString(property));
        }

        rule.setData(properties);
    }

    public static void addRulePropertyValidators(ApplicationPage page, String ruleTypeName) {
        if (Rule.RuleType.RESPONSE_CODE_RULE_TYPE.toString().equals(ruleTypeName)) {
            page.addValidator("codes", new RequiredValidator());
            page.addValidator("codes", new ResponseCodesValidator());
        } else if (Rule.RuleType.SUBSTRING_RULE_TYPE.toString().equals(ruleTypeName)) {
            page.addValidator("substring", new RequiredValidator());
            page.addValidator("substring", new LengthValidator(1, 256));

            page.addValidator("substringMinCount", new RequiredValidator());
            page.addValidator("substringMinCount", new IntegerValidator(0, 1024));

            page.addValidator("substringMaxCount", new RequiredValidator());
            page.addValidator("substringMaxCount", new IntegerValidator(0, 1024));
        } else if (Rule.RuleType.REGEX_MATCH_RULE_TYPE.toString().equals(ruleTypeName)) {
            page.addValidator("matchPattern", new RequiredValidator());
            page.addValidator("matchPattern", new LengthValidator(1, 512));
            page.addValidator("matchPattern", PatternValidator.getInstance());
        } else if (Rule.RuleType.REGEX_NOT_MATCH_RULE_TYPE.toString().equals(ruleTypeName)) {
            page.addValidator("notMatchPattern", new RequiredValidator());
            page.addValidator("notMatchPattern", new LengthValidator(1, 512));
            page.addValidator("notMatchPattern", PatternValidator.getInstance());
        } else if (Rule.RuleType.REGEX_FIND_RULE_TYPE.toString().equals(ruleTypeName)) {
            page.addValidator("findPattern", new RequiredValidator());
            page.addValidator("findPattern", new LengthValidator(1, 512));
            page.addValidator("findPattern", PatternValidator.getInstance());

            page.addValidator("patternMinCount", new RequiredValidator());
            page.addValidator("patternMinCount", new IntegerValidator(0, 1024));

            page.addValidator("patternMaxCount", new RequiredValidator());
            page.addValidator("patternMaxCount", new IntegerValidator(0, 1024));
        }
    }

    private RuleTypeUtil() {
        throw new UnsupportedOperationException();
    }
}
