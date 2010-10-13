package com.codeforces.graygoose.dao.impl;

import com.google.inject.Inject;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.PersistenceManager;

public class BasicDaoImpl {
    @Inject
    private PersistenceManagerFactory persistenceManagerFactory;

    protected void makePersistent(Object object) {
        PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
        try {
            pm.makePersistent(object);
        } finally {
            pm.close();
        }
    }

    protected Object execute(String query) {
        PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
        try {
            return pm.newQuery(query).execute();
        } finally {
            pm.close();
        }
    }
}
