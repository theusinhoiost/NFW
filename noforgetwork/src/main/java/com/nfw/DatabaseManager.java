package com.nfw;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
//Get information from db.properties to connect with DB - Url,User and Password
//Just for security
public class DatabaseManager {
    private static Properties loadProperties() throws IOException{
        Properties props = new Properties();
        FileInputStream file = new FileInputStream("db.properties");
        props.load(file);
        file.close();
        return props;
    }
    public static void insertData(int pk, String date, String event, int priority) throws IOException {
        try{
            String sql = "INSERT INTO TABLE (pk,date,event,priority) VALUES (?, ?, ?, ?)";

            Properties props = loadProperties();
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, pk);
                statement.setString(2, date);
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
