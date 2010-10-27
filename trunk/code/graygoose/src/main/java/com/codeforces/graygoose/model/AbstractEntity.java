package com.codeforces.graygoose.model;

import javax.jdo.annotations.Persistent;

public abstract class AbstractEntity {

    @Persistent
    private boolean deleted;

    protected AbstractEntity() {
        deleted = false;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
