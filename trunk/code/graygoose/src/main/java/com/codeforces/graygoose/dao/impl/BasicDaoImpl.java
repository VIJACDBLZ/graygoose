package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.BasicDao;
import com.codeforces.graygoose.model.AbstractEntity;
import org.nocturne.util.StringUtil;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import java.util.List;

public abstract class BasicDaoImpl<T extends AbstractEntity> implements BasicDao<T> {
    public static volatile ThreadLocal<PersistenceManager> persistenceManagerByThread =
            new ThreadLocal<PersistenceManager>();

    public static void setPersistenceManager(PersistenceManager manager) {
        persistenceManagerByThread.set(manager);
    }

    private static PersistenceManager getPersistenceManager() {
        return persistenceManagerByThread.get();
    }

    public static void closePersistenceManager() {
        getPersistenceManager().close();
    }


    private Object executeQuery(String query) {
        return getPersistenceManager().newQuery(query).execute();
    }

    protected T find(Class<T> clazz, long id) {
        return find(clazz, id, true);
    }

    protected T find(Class<T> clazz, long id, boolean ignoreDeleted) {
        try {
            T entity = getPersistenceManager().getObjectById(clazz, id);
            return entity == null || entity.isDeleted() ? null : entity;
        } catch (JDOObjectNotFoundException e) {
            return null;
        }
    }

    @SuppressWarnings({"unchecked"})
    protected List<T> findAll(Class<T> clazz) {
        return findAll(clazz, null, null, true);
    }

    @SuppressWarnings({"unchecked"})
    protected List<T> findAll(Class<T> clazz, String whereClause, String orderByClause, boolean ignoreDeleted) {

        StringBuilder queryText = new StringBuilder();
        queryText.append("SELECT FROM ").append(clazz.getName());

        if (ignoreDeleted) {
            queryText.append(" WHERE (deleted == false)");
        }

        if (!StringUtil.isEmptyOrNull(whereClause)) {
            if (ignoreDeleted) {
                queryText.append(" && ");
            } else {
                queryText.append(" WHERE ");
            }

            queryText.append("(").append(whereClause).append(")");
        }

        if (!StringUtil.isEmptyOrNull(orderByClause)) {
            queryText.append(" ORDER BY ").append(orderByClause);
        }

        return (List<T>) executeQuery(queryText.toString());
    }


    @Override
    public void insert(T entity) {
        getPersistenceManager().makePersistent(entity);
    }

    @Override
    public void delete(T entity) {
        getPersistenceManager().deletePersistent(entity);
    }

    @Override
    public void markDeleted(T entity) {
        entity.setDeleted(true);
    }

    @Override
    public void unmarkDeleted(T entity) {
        entity.setDeleted(false);
    }
}
