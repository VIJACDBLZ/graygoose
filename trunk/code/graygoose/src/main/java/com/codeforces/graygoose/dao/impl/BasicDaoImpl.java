package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.BasicDao;
import com.codeforces.graygoose.model.AbstractEntity;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.nocturne.main.ApplicationContext;
import org.nocturne.util.StringUtil;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;

public abstract class BasicDaoImpl<T extends AbstractEntity> implements BasicDao<T> {
    private static final Map<String, Query> compiledQueryByQueryString = new Hashtable<String, Query>();

    public static volatile ThreadLocal<PersistenceManager> persistenceManagerByThread =
            new ThreadLocal<PersistenceManager>();

    private static volatile PersistenceManagerFactory persistenceManagerFactory;

    public static PersistenceManagerFactory getPersistenceManagerFactory() {
        if (persistenceManagerFactory == null) {
            persistenceManagerFactory =
                    ApplicationContext.getInstance().getInjector().getInstance(PersistenceManagerFactory.class);
        }

        return persistenceManagerFactory;
    }

    public static void setPersistenceManager(PersistenceManager manager) {
        persistenceManagerByThread.set(manager);
    }

    private static PersistenceManager getPersistenceManager() {
        return persistenceManagerByThread.get();
    }

    public static void closePersistenceManager() {
        getPersistenceManager().close();
    }

    public static void openPersistenceManager() {
        setPersistenceManager(getPersistenceManagerFactory().getPersistenceManager());
    }

    private void reopenPersistenceManager() {
        closePersistenceManager();
        openPersistenceManager();
    }

    private Object executeQueryWithMap(String queryString, Map<String, Object> parameters) {
        Query query = compiledQueryByQueryString.get(queryString);

        if (query == null) {
            //Compile new query from the query string and cache it
            query = getPersistenceManager().newQuery(queryString);
            compiledQueryByQueryString.put(queryString, query);
        } else {
            //Create new query using cached compiled query
            query = getPersistenceManager().newQuery(query);
        }

        return query.executeWithMap(parameters);
    }

    protected T find(Class<T> clazz, long id) {
        return find(clazz, id, true);
    }

    protected T find(Class<T> clazz, long id, boolean ignoreDeleted) {
        try {
            T entity = getPersistenceManager().getObjectById(clazz, id);
            return entity == null || (entity.isDeleted() && ignoreDeleted) ? null : entity;
        } catch (JDOObjectNotFoundException e) {
            return null;
        }
    }

    @SuppressWarnings({"unchecked"})
    protected List<T> findAll(Class<T> clazz) {
        return findAll(clazz, null, null, null, true);
    }

    @SuppressWarnings({"unchecked"})
    protected List<T> findAll(Class<T> clazz, String whereClause, String orderByClause,
                              Map<String, Object> parameters, boolean ignoreDeleted) {
        StringBuilder queryText = new StringBuilder();
        queryText.append("SELECT FROM ").append(clazz.getName());

        if (ignoreDeleted) {
            queryText.append(" WHERE this.deleted == false");
        }

        if (!StringUtil.isEmptyOrNull(whereClause)) {
            if (ignoreDeleted) {
                queryText.append(" && ");
            } else {
                queryText.append(" WHERE ");
            }

            queryText.append(whereClause);
        }

        if (!StringUtil.isEmptyOrNull(orderByClause)) {
            queryText.append(" ORDER BY ").append(orderByClause);
        }

        return (List<T>) executeQueryWithMap(queryText.toString(), parameters);
    }


    protected void insert(T entity) {
        getPersistenceManager().makePersistent(entity);
    }

    protected void delete(T entity) {
        getPersistenceManager().deletePersistent(entity);
    }

    protected void markDeleted(T entity) {
        entity.setDeleted(true);
    }

    protected void unmarkDeleted(T entity) {
        entity.setDeleted(false);
    }

    @SuppressWarnings({"unchecked"})
    protected void update(T entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Can't update non-persistent entity.");
        }

        T instance = find((Class<T>) entity.getClass(), entity.getId());

        try {
            new PropertyUtilsBean().copyProperties(instance, entity);
            reopenPersistenceManager();
        } catch (Exception e) {
            throw new RuntimeException("Can't update entity [" + entity + "].", e);
        }
    }
}
