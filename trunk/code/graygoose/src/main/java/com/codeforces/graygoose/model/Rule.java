package com.codeforces.graygoose.model;

import javax.jdo.annotations.*;
import java.util.*;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Rule extends AbstractEntity {
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
    private boolean deleted;

    @Persistent
    private Date creationTime;

    public Rule(long siteId, RuleType ruleType) {
        this.siteId = siteId;
        this.ruleType = ruleType;
        deleted = false;
        creationTime = new Date();
    }

    @Override
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
                .append("ruleType=").append(ruleType.getShortName())
                .append(", parameters=").append(data)
                .append('}');

        return result.toString();
    }

    public static enum RuleType {
        RESPONSE_CODE_RULE_TYPE("RESP_CODE", "expectedCodes"),
        SUBSTRING_RULE_TYPE("SUBSTR", "expectedSubstring", "expectedSubstringMinimalCount", "expectedSubstringMaximalCount"),
        REGEX_RULE_TYPE("REGEX", "expectedRegex");

        private final SortedSet<String> propertyNames = new TreeSet<String>();

        private final String shortName;

        private RuleType(String shortName, String... propertyNames) {
            this.shortName = shortName;
            this.propertyNames.addAll(Arrays.asList(propertyNames));
        }

        public Set<String> getPropertyNames() {
            return Collections.unmodifiableSortedSet(propertyNames);
        }

        public String getShortName() {
            return shortName;
        }
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Date getCreationTime() {
        return creationTime;
    }
}
