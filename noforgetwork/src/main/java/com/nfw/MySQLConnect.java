package com.nfw;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnect {

    // Open connection with DataBase
    public static Connection openConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Exit connection with DataBase
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection: " + e.getMessage());
        }
    }

    // Query in DB
    public static void executeQuery(Connection connection, String query) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

            // Process results 
            while (resultSet.next()) {
                String column1Value = resultSet.getString("column1");
                // Other columns 
                System.out.println("Column 1 Value: " + column1Value);
            }
        } catch (SQLException e) {
            System.err.println("Error executing the query: " + e.getMessage());
        }
    }
}
