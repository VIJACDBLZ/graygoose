package com.codeforces.graygoose.model;

import javax.jdo.annotations.*;
import java.util.*;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Rule {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Long siteId;

    @Persistent
    private RuleType ruleType;

    /**
     * Contains key-value pairs, specific for each rule type.
     * Type settings can be accessed via <code>(String[]) ruleType.getPropertyNames()</code> function call.
     */
    @Persistent(serialized = "true")
    private SortedMap<String, String> data = new TreeMap<String, String>();

    @Persistent
    private Date creationTime;

    public Rule(long siteId, RuleType ruleType, Date creationTime) {
        this.siteId = siteId;
        this.ruleType = ruleType;
        this.creationTime = creationTime;
    }

    public Long getId() {
        return id;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public SortedMap<String, String> getData() {
        return data;
    }

    public void setData(SortedMap<String, String> data) {
        this.data = data;
    }

    public String getProperty(String key) {
        return data.get(key);
    }

    public int getPropertyAsInteger(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public void setProperty(String key, String value) {
        data.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Rule {")
                .append("ruleType=").append(ruleType)
                .append(", parameters=").append(data)
                .append('}');

        return result.toString();
    }

    public static enum RuleType {
        RESPONSE_CODE_RULE_TYPE("expectedCodes"),
        SUBSTRING_RULE_TYPE("expectedSubstring", "expectedSubstringMinimalCount", "expectedSubstringMaximalCount"),
        REGEX_RULE_TYPE("expectedRegex");

        private final SortedSet<String> propertyNames = new TreeSet<String>();

        private RuleType(String... propertyNames) {
            this.propertyNames.addAll(Arrays.asList(propertyNames));
        }

        public Set<String> getPropertyNames() {
            return Collections.unmodifiableSortedSet(propertyNames);
        }
    }
}
