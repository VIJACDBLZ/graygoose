package com.codeforces.graygoose.misc;

import com.codeforces.graygoose.dao.impl.BasicDaoImpl;
import org.nocturne.listener.PageRequestListener;
import org.nocturne.main.Page;

public class ApplicationPageRequestListener implements PageRequestListener {
    @Override
    public void beforeProcessPage(Page page) {
        BasicDaoImpl.openPersistenceManager();
    }

    @Override
    public void afterProcessPage(Page page, Throwable t) {
        BasicDaoImpl.closePersistenceManager();
    }
}