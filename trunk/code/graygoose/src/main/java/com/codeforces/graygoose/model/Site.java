package com.codeforces.graygoose.model;

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Site extends AbstractEntity {
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
    private boolean deleted;

    @Persistent
    private Date creationTime;

    public Site(String name, String url, int rescanPeriodSeconds) {
        this.name = name;
        this.url = url;
        this.rescanPeriodSeconds = rescanPeriodSeconds;
        deleted = false;
        creationTime = new Date();
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
