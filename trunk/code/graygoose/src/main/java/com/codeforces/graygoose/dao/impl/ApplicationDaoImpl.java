package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.database.ApplicationDataSourceFactory;
import com.codeforces.graygoose.model.ApplicationEntity;
import org.jacuzzi.core.GenericDaoImpl;

import java.util.List;

public class ApplicationDaoImpl<T extends ApplicationEntity> extends GenericDaoImpl<T, Long> {
    protected ApplicationDaoImpl() {
        super(ApplicationDataSourceFactory.getInstance());
    }

    public T find(long id) {
        return findOnlyBy(true, "id=? AND NOT deleted", id);
    }

    public T find (long id, boolean ignoreDeleted) {
        if (ignoreDeleted) {
            return findOnlyBy(true, "id=? AND NOT deleted", id);
        } else {
            return findOnlyBy(true, "id=?", id);
        }
    }

    @Override
    public List<T> findAll() {
        return findBy("NOT deleted ORDER BY creationTime DESC");
    }

    protected void markDeleted(T object) {
        object.setDeleted(true);
        update(object);
    }
}
