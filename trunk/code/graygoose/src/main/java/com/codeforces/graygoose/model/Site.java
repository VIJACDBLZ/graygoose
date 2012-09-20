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

    @SuppressWarnings({"FieldMayBeFinal"})
    @Persistent
    private Date creationTime;

    @Persistent
    private Long pauseFromMinute;

    @Persistent
    private Long pauseToMinute;

    public Site(String name, String url, int rescanPeriodSeconds, Long pauseFromMinute, Long pauseToMinute) {
        this.name = name;
        this.url = url;
        this.rescanPeriodSeconds = rescanPeriodSeconds;
        deleted = false;
        creationTime = new Date();

        this.pauseFromMinute = pauseFromMinute;
        this.pauseToMinute = pauseToMinute;
    }

    @Override
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

    @SuppressWarnings({"ReturnOfDateField"})
    @Override
    public Date getCreationTime() {
        return creationTime;
    }

    public Long getPauseFromMinute() {
        return pauseFromMinute;
    }

    public Long getPauseToMinute() {
        return pauseToMinute;
    }

    public void setPauseFromMinute(Long pauseFromMinute) {
        this.pauseFromMinute = pauseFromMinute;
    }

    public void setPauseToMinute(Long pauseToMinute) {
        this.pauseToMinute = pauseToMinute;
    }
}
