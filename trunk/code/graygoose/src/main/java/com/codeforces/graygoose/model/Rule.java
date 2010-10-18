package com.codeforces.graygoose.model;

//import com.google.gson.reflect.TypeToken;

import javax.jdo.annotations.*;
//import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Rule {
    /*private static final String RESPONSE_CODE_RULE_TYPE = "Response code is in list";
    private static final String REG_EX_RULE_TYPE = "RegEx match count is between";*/

    /*private static final Type DATA_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();*/
    //private final Gson gson = new Gson();

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Long siteId;

    private RuleType ruleType;

    @Persistent
    private Date creationTime;

    @Persistent
    private String jsonData;

    /**
     * This map mirrors jsonData jdo-field.
     * Contains <String, String> key-value pairs, specific for each rule type.
     *
    */
    @Persistent(serialized = "true")
    private Map<String, String> data = new HashMap<String, String>();

    public Rule(Long siteId, RuleType ruleType, Date creationTime, String jsonData) {
        this.siteId = siteId;
        this.ruleType = ruleType;
        this.creationTime = creationTime;

        setJsonData(jsonData);

        data.put("key", "value");
        data.put("key1", "value1");
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
        return jsonData;
        //return gson.toJson(data, DATA_TYPE);
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
        //data = gson.fromJson(jsonData, DATA_TYPE);
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
