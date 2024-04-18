package com.nfw;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream("noforgetwork/src/main/resources/db.properties");
        props.load(file);
        file.close();
        return props;
    }

    public static void createDBEventsIfNotExists() throws IOException {
        Properties props = loadProperties();
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        String databaseName = "events";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;

            try (Statement statement = conn.createStatement()) {

                statement.executeUpdate(sql);
                Logging.logInfo("Database " + databaseName + " created successfully");
            }
        } catch (SQLException e) {
            Logging.logError("Error creating database " + databaseName);
        }
    }

    public static void createTableIfNotExists() throws IOException {
        try {
            Properties props = loadProperties();
            String url = props.getProperty("db.url") + "/events";
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            String sql = "CREATE TABLE IF NOT EXISTS eventstable (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "date VARCHAR(255) NOT NULL," +
                    "event VARCHAR(500) NOT NULL," +
                    "priority INT NOT NULL)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.executeUpdate();
                Logging.logInfo("Table 'eventstable' created successfully");
            }
        } catch (SQLException ex) {
            Logging.logError("Error creating table 'eventstable'");
        }
    }

    public static void insertData(String date, String event, int priority) throws IOException {
        try {
            Properties props = loadProperties();
            String url = props.getProperty("db.url") + "/events";
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            String sql = "INSERT INTO eventstable (date, event, priority) VALUES (?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, date);
                statement.setString(2, event);
                statement.setInt(3, priority);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    Logging.logInfo("The event was added successfully");
                }
            }
        } catch (SQLException ex) {
            Logging.logError("Error inserting data");
        }
    }
}

