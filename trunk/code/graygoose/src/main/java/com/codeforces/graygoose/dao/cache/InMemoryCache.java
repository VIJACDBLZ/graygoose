package com.codeforces.graygoose.dao.cache;

import com.google.inject.Singleton;
import org.apache.log4j.Logger;

import java.util.Hashtable;
import java.util.Map;

@Singleton
public class InMemoryCache implements Cache {
    private static final Logger logger = Logger.getLogger(InMemoryCache.class);

    private final Map<String, Object> storage = new Hashtable<String, Object>();

    @Override
    public Object get(String key) {
        logger.info("Retrieve value by the key [" + key + "] ...");
        Object value = storage.get(key);

        if (value == null) {
            logger.info("... value is not found");
        }

        return value;
    }

    @Override
    public void put(String key, Object value) {
        logger.info("Insert value with the key [" + key + "].");
        storage.put(key, value);
    }

    @Override
    public void clear() {
        logger.info("Invalidate.");
        storage.clear();
    }
}
