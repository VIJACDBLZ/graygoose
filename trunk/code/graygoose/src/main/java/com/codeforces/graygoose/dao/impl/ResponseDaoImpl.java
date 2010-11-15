package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.ResponseDao;
import com.codeforces.graygoose.dao.cache.Cacheable;
import com.codeforces.graygoose.dao.cache.InvalidateCache;
import com.codeforces.graygoose.model.Response;
import com.google.inject.Singleton;

@Singleton
public class ResponseDaoImpl extends BasicDaoImpl<Response> implements ResponseDao {
    @Cacheable
    @Override
    public Response find(long id) {
        return super.find(Response.class, id);
    }

    @InvalidateCache
    @Override
    public void insert(Response response) {
        super.insert(response);
    }
}
