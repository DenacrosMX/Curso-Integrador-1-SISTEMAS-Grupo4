package com.habitech.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConexionDB {

    private static final Logger logger = LoggerFactory.getLogger(ConexionDB.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/habitech_db"; // Ajustar puerto si es 5432
    private static final String USER = "postgres";
    private static final String PASS = "123456";

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