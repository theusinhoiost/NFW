package com.nfw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public static void createDBEventsIfNotExists(){
        String url = null;
        String user = null;
        String password = null;
        String databaseName = null;
        try {
            url = null;
            user = null;
            password = null;
            databaseName = null;
            Properties props = loadProperties();
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            databaseName = "events";
        } catch (Exception e) {
            Logging.logError("Can't get properties");
        }

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

    public static void createTableIfNotExists(){
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
        } catch (SQLException | IOException ex) {
            Logging.logError("Error creating table 'eventstable'");
        }
    }

    public static void insertData(String date, String event, int priority){
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
        } catch (IOException e) {
            Logging.logError("Can't get properties");
        }
    }
}



