package com.habitech.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConexionDB {

    private static final Logger logger = LoggerFactory.getLogger(ConexionDB.class);
    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        // Carga las credenciales desde db.properties (classpath)
        Properties props = new Properties();
        try (InputStream input = ConexionDB.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró db.properties en el classpath");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo db.properties", e);
        }

        URL  = props.getProperty("db.url");
        USER = props.getProperty("db.user");
        PASS = props.getProperty("db.password");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Driver de PostgreSQL no encontrado en el classpath", e);
        }

        logger.info("ConexionDB inicializada para: {}", URL);
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