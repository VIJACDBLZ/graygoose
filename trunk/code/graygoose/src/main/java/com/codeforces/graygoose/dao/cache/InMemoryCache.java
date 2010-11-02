package com.codeforces.graygoose.dao.cache;

import com.google.inject.Singleton;

import java.util.Hashtable;
import java.util.Map;

@Singleton
public class InMemoryCache implements Cache {
    private Map<String, Object> storage = new Hashtable<String, Object>();

    @Override
    public Object get(String key) {
        return storage.get(key);
    }

    @Override
    public void put(String key, Object value) {
        storage.put(key, value);
    }

    @Override
    public void clear() {
        storage.clear();
    }
}
