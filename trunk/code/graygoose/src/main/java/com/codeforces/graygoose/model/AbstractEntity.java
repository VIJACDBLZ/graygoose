package com.codeforces.graygoose.model;

import java.util.Date;

public abstract class AbstractEntity {
    public abstract boolean isDeleted();

    public abstract void setDeleted(boolean deleted);

    public abstract Date getCreationTime();

    public abstract Long getId();

    @Override
    public String toString() {
        return getClass().getName() + "," + getId();
    }
}
