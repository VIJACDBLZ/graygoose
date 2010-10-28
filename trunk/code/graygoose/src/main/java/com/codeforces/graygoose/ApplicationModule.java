package com.codeforces.graygoose;

import com.codeforces.graygoose.dao.*;
import com.codeforces.graygoose.dao.impl.*;
import com.codeforces.graygoose.misc.PersistenceManagerFactoryInstance;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

import javax.jdo.PersistenceManagerFactory;

public class ApplicationModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(UserService.class).toInstance(UserServiceFactory.getUserService());
        binder.bind(PersistenceManagerFactory.class).toInstance(PersistenceManagerFactoryInstance.getFactory());

        binder.bind(SiteDao.class).to(SiteDaoImpl.class).in(Singleton.class);
        binder.bind(AlertDao.class).to(AlertDaoImpl.class).in(Singleton.class);
        binder.bind(RuleDao.class).to(RuleDaoImpl.class).in(Singleton.class);
        binder.bind(RuleAlertRelationDao.class).to(RuleAlertRelationDaoImpl.class).in(Singleton.class);
        binder.bind(RuleCheckEventDao.class).to(RuleCheckEventDaoImpl.class).in(Singleton.class);
        binder.bind(AlertTriggerEventDao.class).to(AlertTriggerEventDaoImpl.class).in(Singleton.class);
    }
}
