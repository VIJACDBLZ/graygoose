package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.ResponseDao;
import com.codeforces.graygoose.model.Response;

public class ResponseDaoImpl extends ApplicationDaoImpl<Response> implements ResponseDao {
    @Override
    public Response find(long id) {
        return super.find(id);
    }

    @Override
    public void insert(Response object) {
        super.insert(object);
    }
}
