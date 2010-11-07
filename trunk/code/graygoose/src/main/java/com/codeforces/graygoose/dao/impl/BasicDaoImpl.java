package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.BasicDao;
import com.codeforces.graygoose.model.AbstractEntity;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.nocturne.main.ApplicationContext;
import org.nocturne.util.StringUtil;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class BasicDaoImpl<T extends AbstractEntity> implements BasicDao<T> {
    private static final ThreadLocal<PersistenceManager> persistenceManagerByThread =
            new ThreadLocal<PersistenceManager>();

    public static PersistenceManagerFactory getPersistenceManagerFactory() {
        return PersistenceManagerFactoryHolder.getPersistenceManagerFactory();
    }

    private static class PersistenceManagerFactoryHolder {
        private static final PersistenceManagerFactory persistenceManagerFactory =
                ApplicationContext.getInstance().getInjector().getInstance(PersistenceManagerFactory.class);

        public static PersistenceManagerFactory getPersistenceManagerFactory() {
            return persistenceManagerFactory;
        }
    }

    private static void setPersistenceManager(PersistenceManager manager) {
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

    private static void reopenPersistenceManager() {
        closePersistenceManager();
        openPersistenceManager();
    }

    public static void makeTransient(Object result) {
        PersistenceManager persistenceManager = getPersistenceManager();

        if (result instanceof Iterable) {
            makeIterableTransient(persistenceManager, (Iterable) result);
        } else if (result instanceof Map) {
            makeMapTransient(persistenceManager, (Map) result);
        } else if (result instanceof Array) {
            makeArrayTransient(persistenceManager, (Array) result);
        } else {
            makeEntityTransient(persistenceManager, result);
        }
    }

    private static void makeIterableTransient(PersistenceManager persistenceManager, Iterable iterable) {
        for (Object o : iterable) {
            makeEntityTransient(persistenceManager, o);
        }
    }

    private static void makeMapTransient(PersistenceManager persistenceManager, Map map) {
        for (Object o : map.values()) {
            makeEntityTransient(persistenceManager, o);
        }
    }

    private static void makeArrayTransient(PersistenceManager persistenceManager, Array array) {
        int arrayLength = Array.getLength(array);
        for (int arrayIndex = 0; arrayIndex < arrayLength; ++arrayIndex) {
            makeEntityTransient(persistenceManager, Array.get(array, arrayIndex));
        }
    }

    private static void makeEntityTransient(PersistenceManager persistenceManager, Object persistentEntity) {
        if (persistentEntity instanceof AbstractEntity) {
            persistenceManager.makeTransient(persistentEntity);
        }
    }

    private Object executeQueryWithMap(String queryString, Map<String, Object> parameters) {
        PersistenceManager persistenceManager = getPersistenceManager();
        Object result = persistenceManager.newQuery(queryString).executeWithMap(parameters);
        persistenceManager.retrieveAll((Collection) result);
        return result;
    }

    protected T find(Class<T> clazz, long id) {
        return find(clazz, id, true);
    }

    protected T find(Class<T> clazz, long id, boolean ignoreDeleted) {
        try {
            PersistenceManager persistenceManager = getPersistenceManager();
            T entity = persistenceManager.getObjectById(clazz, id);
            persistenceManager.retrieve(entity);
            return entity == null || (entity.isDeleted() && ignoreDeleted) ? null : entity;
        } catch (JDOObjectNotFoundException e) {
            return null;
        }
    }

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

    @SuppressWarnings({"unchecked"})
    protected void delete(T entity) {
        assertEntityIdNotNull(entity);
        T persistentEntity = find((Class<T>) entity.getClass(), entity.getId(), false);
        assertPersistentEntityNotNull(persistentEntity);

        getPersistenceManager().deletePersistent(persistentEntity);
    }

    protected void markDeleted(T entity) {
        entity.setDeleted(true);
        update(entity);
    }

    protected void unmarkDeleted(T entity) {
        entity.setDeleted(false);
        update(entity);
    }

    @SuppressWarnings({"unchecked"})
    protected void update(T entity) {
        assertEntityIdNotNull(entity);
        T persistentEntity = find((Class<T>) entity.getClass(), entity.getId(), false);
        assertPersistentEntityNotNull(persistentEntity);

        try {
            new PropertyUtilsBean().copyProperties(persistentEntity, entity);
            reopenPersistenceManager();
        } catch (Exception e) {
            throw new RuntimeException("Can't update entity [" + entity + "].", e);
        }
    }

    private void assertEntityIdNotNull(T entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Can't operate with non-persistent entity [" + entity + "].");
        }
    }

    private void assertPersistentEntityNotNull(T persistentEntity) {
        if (persistentEntity == null) {
            throw new JDOObjectNotFoundException("Persistent entity [" + persistentEntity
                    + "] is not found in the DataStorage.");
        }
    }
}
