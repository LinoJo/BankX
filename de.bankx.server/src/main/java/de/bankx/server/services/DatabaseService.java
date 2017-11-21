package de.bankx.server.services;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseService {

    private ComboPooledDataSource connectionPool;

    private static DatabaseService instance;
    public static DatabaseService getInstance()
    {
        if (instance == null)
            instance = new DatabaseService();

        return instance;
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    private DatabaseService() {
        String dbPath = "jdbc:derby:testDB;create=true";
        //log.info("init db-service");
        connectionPool = new ComboPooledDataSource();

        try {
            connectionPool.setDriverClass("org.apache.derby.jdbc.EmbeddedDriver"); //loads the jdbc driver
            connectionPool.setMaxPoolSize(10);
            connectionPool.setMinPoolSize(5);
        } catch (PropertyVetoException e) {
            //log.error("Unable to setup Connection Pool for Derby DB.");
            //log.error(e.getMessage());
            //log.error(e.getStackTrace());
        }

        connectionPool.setJdbcUrl(dbPath);



    }
}
