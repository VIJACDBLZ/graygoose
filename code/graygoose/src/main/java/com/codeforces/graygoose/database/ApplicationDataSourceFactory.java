package com.codeforces.graygoose.database;

import com.codeforces.graygoose.exception.ConfigurationException;
import com.codeforces.graygoose.misc.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.nocturne.main.ApplicationContext;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class ApplicationDataSourceFactory {
    /**
     * Database connection configuration properties.
     */
    private static final Properties properties = new Properties();

    /**
     * The only instance.
     */
    private static DataSource instance;

    /**
     * Deny construct outside class.
     */
    private ApplicationDataSourceFactory() {
        // No operations.
    }

    public static synchronized DataSource getInstance() {
        if (instance == null) {
            boolean debug = ApplicationContext.getInstance().isDebug();
            ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();

            comboPooledDataSource.setJdbcUrl(properties.getProperty("database.url"));
            comboPooledDataSource.setUser(properties.getProperty("database.user"));
            comboPooledDataSource.setPassword(properties.getProperty("database.password"));
            comboPooledDataSource.setCheckoutTimeout(60000);
            comboPooledDataSource.setIdleConnectionTestPeriod(60);
            comboPooledDataSource.setMaxStatementsPerConnection(128);

            if (debug) {
                comboPooledDataSource.setInitialPoolSize(1);
            } else {
                comboPooledDataSource.setInitialPoolSize(4);
            }

            comboPooledDataSource.setMaxPoolSize(4);
            comboPooledDataSource.setPreferredTestQuery("SET time_zone='" + Configuration.getTimeZone() + "'");

            instance = comboPooledDataSource;
        }

        return instance;
    }

    static {
        try {
            properties.load(ApplicationDataSourceFactory.class.getResourceAsStream("/database.properties"));
            Class.forName(properties.getProperty("database.driver"));
        } catch (IOException e) {
            throw new ConfigurationException("Can't load /database.properties.", e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Can't load database driver " + properties.getProperty("database.driver") + ".", e);
        }
    }
}
