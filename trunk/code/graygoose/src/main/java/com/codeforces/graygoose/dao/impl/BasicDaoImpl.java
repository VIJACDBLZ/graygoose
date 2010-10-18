package com.codeforces.graygoose.dao.impl;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

public class BasicDaoImpl {
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

    protected void makePersistent(Object object) {
        getPersistenceManager().makePersistent(object);
    }

    protected void deletePersistent(Object object) {
        getPersistenceManager().deletePersistent(object);
    }

    protected <T> T getObjectById(Class<T> clazz, Object id) {
        try {
            return getPersistenceManager().getObjectById(clazz, id);
        } catch (JDOObjectNotFoundException e) {
            return null;
        }
    }

    protected Object execute(String query) {
        return getPersistenceManager().newQuery(query).execute();
    }
}
