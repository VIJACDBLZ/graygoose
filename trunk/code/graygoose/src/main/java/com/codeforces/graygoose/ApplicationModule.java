package com.codeforces.graygoose;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.dao.impl.SiteDaoImpl;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.JDOHelper;

public class ApplicationModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(UserService.class).toInstance(UserServiceFactory.getUserService());
        binder.bind(PersistenceManagerFactory.class).toInstance(JDOHelper.getPersistenceManagerFactory("transactions-optional"));
        binder.bind(SiteDao.class).to(SiteDaoImpl.class);
    }
}
