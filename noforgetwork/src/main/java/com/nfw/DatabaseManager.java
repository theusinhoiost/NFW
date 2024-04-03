package com.nfw;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream("db.properties");
        props.load(file);
        file.close();
        return props;
    }

    public static void createDBEvents()throws IOException{
        try {
            Properties props = loadProperties();
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            String databaseName = "Events";

            String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;

            try {
                
            } catch (Exception e) {
               
            }
                               
        } catch (Exception e) {
            
        }
    }

    public static void createTableIfNotExists() throws IOException {
        try {
            Properties props = loadProperties();
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            String sql = "CREATE TABLE IF NOT EXISTS events (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "date VARCHAR(255) NOT NULL," +
                    "event VARCHAR(255) NOT NULL," +
                    "priority INT NOT NULL)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.executeUpdate();
                System.out.println("Table 'events' created successfully");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    public static void insertData(String date, String event, int priority) throws IOException {
        try {
            Properties props = loadProperties();
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            String sql = "INSERT INTO events (date, event, priority) VALUES (?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setDate(2, Date.valueOf(date));
                statement.setString(3, event);
                statement.setInt(4, priority);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("New data was registered successfully");
                }
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
}
