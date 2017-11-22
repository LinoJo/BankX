package de.bankx.server.services;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.sql.*;

public class DatabaseService {

    static Logger log = Logger.getLogger(DatabaseService.class);
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
        String dbPath = "jdbc:derby:;databaseName=DerbyDB;create=true";
        log.info("Initialisiere Datenbank-Service");
        connectionPool = new ComboPooledDataSource();

        try {
            connectionPool.setDriverClass("org.apache.derby.jdbc.EmbeddedDriver"); //loads the jdbc driver
            connectionPool.setMaxPoolSize(10);
            connectionPool.setMinPoolSize(5);
        } catch (PropertyVetoException e) {
            log.error("Apache Derby: Fehler im Connectionpool");
            log.error(e.getMessage());
            log.error(e.getStackTrace());
        }

        connectionPool.setJdbcUrl(dbPath);
    }
}
