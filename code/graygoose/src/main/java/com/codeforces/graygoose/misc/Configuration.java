package com.codeforces.graygoose.misc;

import com.codeforces.graygoose.exception.ConfigurationException;

import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;

public class Configuration {
    private static final Properties properties = new Properties();

    public static TimeZone getTimeZone() {
        return TimeZone.getTimeZone(properties.getProperty("timezone"));
    }

    static {
        System.setProperty("file.encoding", "UTF-8");

        try {
            properties.load(Configuration.class.getResourceAsStream("/graygoose.properties"));
        } catch (IOException e) {
            throw new ConfigurationException("Can't load /graygoose.properties.", e);
        }
    }
}
