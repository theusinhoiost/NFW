package com.nfw;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddTablesInPrincipalCard {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    private List<EventData> ArrayTables(int priorityValues) {
        List<EventData> events = new ArrayList<>();
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("noforgetwork/src/main/resources/db.properties"));
            String url = properties.getProperty("db.url")+ "/events";
            String username = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT event, date, priority FROM eventstable";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String event = resultSet.getString("event");
                String date = resultSet.getString("date");
                int priority = resultSet.getInt("priority");
                String eventFormatted = event;
                if (priority == priorityValues) {
                    EventData eventData = new EventData(eventFormatted, date, false);
                    events.add(eventData);
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return events;
    }

    public JScrollPane AddNormalTable() {
        List<EventData> normalEvents = ArrayTables(0);
        return createTable(normalEvents);
    }

    public JScrollPane AddUrgencyTable() {
        List<EventData> urgencyEvents = ArrayTables(1);
        return createTable(urgencyEvents);
    }

    public JScrollPane AddEmergencyTable() {
        List<EventData> emergencyEvents = ArrayTables(2);
        return createTable(emergencyEvents);
    }

    private JScrollPane createTable(List<EventData> events) {
        String[] columnNames = {"Event", "Date", "Checkout"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (EventData eventData : events) {
            model.addRow(new Object[]{eventData.getEvent(), eventData.getDate(), eventData.isChecked()});
        }
        JTable table = new JTable(model) {
            @Override
            public Class getColumnClass(int column) {
                if (column == 2) return Boolean.class;
                return String.class;
            }
        };
        table.setPreferredScrollableViewportSize(new Dimension(450, 200));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        return scrollPane;
    }

    public static class EventData {
        private String event;
        private String date;
        private boolean checked;

        public EventData(String event, String date, boolean checked) {
            this.event = event;
            this.date = date;
            this.checked = checked;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
