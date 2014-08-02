package com.codeforces.graygoose.model;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.jacuzzi.mapping.Transient;

import java.lang.reflect.Type;
import java.util.*;

public class Rule extends ApplicationEntity {
    private long siteId;

    private RuleType ruleType;

    private String dataJson;

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    @Transient
    public Map<String, String> getData() {
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        Gson gson = new Gson();
        Map<String, String> data = gson.fromJson(getDataJson(), mapType);
        return Collections.unmodifiableMap(data);
    }

    @Transient
    public void setData(Map<String, String> data) {
        setDataJson(new Gson().toJson(data));
    }

    @Transient
    public String getProperty(String key) {
        return getData().get(key);
    }

    @Transient
    public int getPropertyAsInteger(String key) {
        return Integer.parseInt(getProperty(key));
    }

    @Transient
    public void setProperty(String key, String value) {
        Map<String, String> data = new LinkedHashMap<>(getData());
        data.put(key, value);
        setData(data);
    }

    public String toShortString() {
        return "Rule {" + "TYPE=" + ruleType.getShortName() + ", " + getData() + '}';
    }

    public static Rule newRule(long siteId, RuleType ruleType) {
        Rule rule = new Rule();
        rule.setSiteId(siteId);
        rule.setRuleType(ruleType);
        rule.setDataJson("");

        return rule;
    }

    public enum RuleType {
        RESPONSE_CODE_RULE_TYPE("CODE", "codes"),
        SUBSTRING_RULE_TYPE("SUBSTR", "substring", "substringMinCount", "substringMaxCount"),
        REGEX_MATCH_RULE_TYPE("REGEX_MATCH", "matchPattern"),
        REGEX_NOT_MATCH_RULE_TYPE("REGEX_NOT_MATCH", "notMatchPattern"),
        REGEX_FIND_RULE_TYPE("REGEX_FIND", "findPattern", "patternMinCount", "patternMaxCount");

        private final Set<String> propertyNames = new LinkedHashSet<>();

        private final String shortName;

        RuleType(String shortName, String... propertyNames) {
            this.shortName = shortName;
            this.propertyNames.addAll(Arrays.asList(propertyNames));
        }

        public Set<String> getPropertyNames() {
            return Collections.unmodifiableSet(propertyNames);
        }

        public String getShortName() {
            return shortName;
        }
    }
}
