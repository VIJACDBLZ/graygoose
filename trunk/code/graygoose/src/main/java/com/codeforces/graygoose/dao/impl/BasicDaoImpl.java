package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.model.AbstractEntity;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import java.util.List;

import org.nocturne.util.StringUtil;

@SuppressWarnings({"unchecked"})
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

    protected void makePersistent(AbstractEntity abstractEntity) {
        getPersistenceManager().makePersistent(abstractEntity);
    }

    private void deletePersistent(AbstractEntity abstractEntity) {
        getPersistenceManager().deletePersistent(abstractEntity);
    }

    protected <T extends AbstractEntity> T getObjectById(Class<T> clazz, Object id) {
        try {
            return getPersistenceManager().getObjectById(clazz, id);
        } catch (JDOObjectNotFoundException e) {
            return null;
        }
    }

    private Object execute(String query) {
        return getPersistenceManager().newQuery(query).execute();
    }

    protected <T extends AbstractEntity> List<T> findAll(Class<T> clazz) {
        return findAll(clazz, "");
    }

    protected <T extends AbstractEntity> List<T> findAll(
            Class<T> clazz, String additionalClause, Object... clauseParameters) {

        String queryText = "SELECT FROM " + clazz.getName();
        if (!StringUtil.isEmptyOrNull(additionalClause)) {
            queryText += " " + String.format(additionalClause, clauseParameters);
        }

        return (List<T>) execute(queryText);
    }
}
