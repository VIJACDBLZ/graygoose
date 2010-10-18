package com.codeforces.graygoose.misc;

import com.codeforces.graygoose.dao.impl.BasicDaoImpl;
import org.nocturne.listener.PageRequestListener;
import org.nocturne.main.ApplicationContext;
import org.nocturne.main.Page;

import javax.jdo.PersistenceManagerFactory;

public class ApplicationPageRequestListener implements PageRequestListener {
    private volatile PersistenceManagerFactory persistenceManagerFactory;

    private PersistenceManagerFactory getPersistenceManagerFactory() {
        if (persistenceManagerFactory == null) {
            persistenceManagerFactory =
                    ApplicationContext.getInstance().getInjector().getInstance(PersistenceManagerFactory.class);
        }

        return persistenceManagerFactory;
    }

    @Override
    public void beforeProcessPage(Page page) {
        BasicDaoImpl.setPersistenceManager(getPersistenceManagerFactory().getPersistenceManager());
    }

    @Override
    public void afterProcessPage(Page page, Throwable t) {
        BasicDaoImpl.closePersistenceManager();
    }
}
