package com.codeforces.graygoose;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.dao.impl.SiteDaoImpl;
import com.codeforces.graygoose.misc.PersistenceManagerFactoryInstance;

import javax.jdo.PersistenceManagerFactory;

public class ApplicationModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(UserService.class).toInstance(UserServiceFactory.getUserService());
        binder.bind(PersistenceManagerFactory.class).toInstance(PersistenceManagerFactoryInstance.getFactory());
        binder.bind(SiteDao.class).to(SiteDaoImpl.class);
    }
}
