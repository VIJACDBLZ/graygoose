package com.codeforces.graygoose.model;

import javax.jdo.annotations.*;
import java.io.Serializable;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Site extends AbstractEntity implements Serializable {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String name;

    @Persistent
    private String url;

    @Persistent
    private int rescanPeriodSeconds;

    @Persistent
    private Date creationTime;

    public Site(String name, String url, int rescanPeriodSeconds, Date creationTime) {
        this.name = name;
        this.url = url;
        this.rescanPeriodSeconds = rescanPeriodSeconds;
        this.creationTime = creationTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRescanPeriodSeconds() {
        return rescanPeriodSeconds;
    }

    public void setRescanPeriodSeconds(int rescanPeriodSeconds) {
        this.rescanPeriodSeconds = rescanPeriodSeconds;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
