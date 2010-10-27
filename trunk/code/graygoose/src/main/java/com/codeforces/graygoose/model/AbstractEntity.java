package com.codeforces.graygoose.model;

import javax.jdo.annotations.Persistent;
import java.util.Date;

public abstract class AbstractEntity {

    /*@Persistent
    protected boolean deleted;

    @Persistent
    protected Date creationTime;

    protected AbstractEntity() {
        deleted = false;
        creationTime = new Date();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreationTime() {
        return creationTime;
    }*/

    public abstract boolean isDeleted();

    public abstract void setDeleted(boolean deleted);

    public abstract Date getCreationTime();
}
