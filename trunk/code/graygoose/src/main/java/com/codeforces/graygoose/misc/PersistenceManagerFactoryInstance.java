package com.codeforces.graygoose.misc;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PersistenceManagerFactoryInstance {
    private static final PersistenceManagerFactory factory
            = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    public static PersistenceManagerFactory getFactory() {
        return factory;
    }
}
