package com.codeforces.graygoose.model;

import org.jacuzzi.mapping.Id;

import java.io.Serializable;
import java.util.Date;

public class ApplicationEntity implements Serializable {
    @Id
    private long id;

    private boolean deleted;

    private Date creationTime = new Date();

    @SuppressWarnings("UnusedDeclaration")
    public long getId() {
        return id;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setId(long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    @Deprecated
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime != null ? new Date(creationTime.getTime()) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationEntity that = (ApplicationEntity) o;

        //noinspection RedundantIfStatement
        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '#' + id;
    }
}
