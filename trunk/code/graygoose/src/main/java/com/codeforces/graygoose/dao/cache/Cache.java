package com.codeforces.graygoose.dao.cache;

public interface Cache {
    Object get(String key);

    void put(String key, Object value);

    void clear();
}
