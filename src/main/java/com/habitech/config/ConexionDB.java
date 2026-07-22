package com.habitech.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConexionDB {

    private static final Logger logger = LoggerFactory.getLogger(ConexionDB.class);
    private static final String URL = String.format("jdbc:postgresql://%s:%s/%s",
            System.getProperty("db.host", "localhost"),
            System.getProperty("db.port", "5432"),
            System.getProperty("db.name", "habitech_db"));
    private static final String USER = System.getProperty("db.user", "postgres");
    private static final String PASS = System.getProperty("db.password", "123456");

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Driver de PostgreSQL no encontrado en el classpath", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            logger.error("Error crítico al conectar a habitech_db: {}", e.getMessage());
            throw e;
        }
    }
}