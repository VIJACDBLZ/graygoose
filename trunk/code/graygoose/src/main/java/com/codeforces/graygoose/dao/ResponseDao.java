package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Response;

public interface ResponseDao {
    Response find(long id);
    void insert(Response response);
}
