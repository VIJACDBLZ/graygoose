package com.codeforces.graygoose;

import com.codeforces.graygoose.dao.*;
import com.codeforces.graygoose.dao.impl.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class ApplicationModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(AlertDao.class).to(AlertDaoImpl.class).in(Singleton.class);
        binder.bind(AlertTriggerEventDao.class).to(AlertTriggerEventDaoImpl.class).in(Singleton.class);
        binder.bind(ResponseDao.class).to(ResponseDaoImpl.class).in(Singleton.class);
        binder.bind(RuleAlertRelationDao.class).to(RuleAlertRelationDaoImpl.class).in(Singleton.class);
        binder.bind(RuleCheckEventDao.class).to(RuleCheckEventDaoImpl.class).in(Singleton.class);
        binder.bind(RuleDao.class).to(RuleDaoImpl.class).in(Singleton.class);
        binder.bind(SiteDao.class).to(SiteDaoImpl.class).in(Singleton.class);
        binder.bind(UserDao.class).to(UserDaoImpl.class).in(Singleton.class);
    }
}
