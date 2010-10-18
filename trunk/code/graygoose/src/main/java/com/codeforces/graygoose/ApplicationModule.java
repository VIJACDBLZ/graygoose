package com.codeforces.graygoose;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.dao.impl.AlertDaoImpl;
import com.codeforces.graygoose.dao.impl.RuleDaoImpl;
import com.codeforces.graygoose.dao.impl.SiteDaoImpl;
import com.codeforces.graygoose.misc.PersistenceManagerFactoryInstance;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Binder;
import com.google.inject.Module;

import javax.jdo.PersistenceManagerFactory;

public class ApplicationModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(UserService.class).toInstance(UserServiceFactory.getUserService());
        binder.bind(PersistenceManagerFactory.class).toInstance(PersistenceManagerFactoryInstance.getFactory());
        binder.bind(SiteDao.class).to(SiteDaoImpl.class);
        binder.bind(AlertDao.class).to(AlertDaoImpl.class);
        binder.bind(RuleDao.class).to(RuleDaoImpl.class);
    }
}
