package com.codeforces.graygoose.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.jdo.annotations.*;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Rule {
    private static final Type DATA_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();
    private final Gson gson = new Gson();

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Long siteId;

    @Persistent
    private RuleType ruleType;

    /**
     * Contains key-value pairs, specific for each rule type.
     */
    @Persistent(serialized = "true")
    private Map<String, String> data = new HashMap<String, String>();

    @Persistent
    private Date creationTime;

    public Rule(Long siteId, RuleType ruleType, Date creationTime, String jsonData) {
        this.siteId = siteId;
        this.ruleType = ruleType;
        this.creationTime = creationTime;

        setJsonData(jsonData);
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

    public String getJsonData() {
        return gson.toJson(data, DATA_TYPE);
    }

    public void setJsonData(String jsonData) {
        data = gson.fromJson(jsonData, DATA_TYPE);
    }

    public Map<String, String> getData() {
        return data;
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

    public static enum RuleType {
        RESPONSE_CODE_RULE_TYPE,
        SUBSTRING_RULE_TYPE,
        REG_EX_RULE_TYPE
    }
}
