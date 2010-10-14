package com.codeforces.graygoose.misc;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.JDOHelper;

public class PersistenceManagerFactoryInstance {
    private static final PersistenceManagerFactory factory
            = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    public static PersistenceManagerFactory getFactory() {
        return factory;
    }
}
