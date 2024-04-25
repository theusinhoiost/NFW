import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class AddTablesInPrincipalCard {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    private List<EventData> fetchEvents(int priorityValues) {
        List<EventData> events = new ArrayList<>();
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("noforgetwork/src/main/resources/db.properties"));
            String url = properties.getProperty("db.url") + "/events";
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
                if (priority == priorityValues) {
                    EventData eventData = new EventData(event, date, false);
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

    public JScrollPane addNormalTable() {
        List<EventData> normalEvents = fetchEvents(0);
        return createTable(normalEvents);
    }

    public JScrollPane addUrgencyTable() {
        List<EventData> urgencyEvents = fetchEvents(1);
        return createTable(urgencyEvents);
    }

    public JScrollPane addEmergencyTable() {
        List<EventData> emergencyEvents = fetchEvents(2);
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
            public Class<?> getColumnClass(int column) {
                if (column == 2) return Boolean.class;
                return String.class;
            }
        };

        // Definindo a largura mínima para a coluna de Checkbox
        TableColumn checkoutColumn = table.getColumnModel().getColumn(2);
        checkoutColumn.setMinWidth(50); // Define largura mínima para a coluna de Checkbox
        checkoutColumn.setMaxWidth(75); // Define uma largura máxima para manter a proporção

        // Se desejar, defina largura mínima para outras colunas
        TableColumn eventColumn = table.getColumnModel().getColumn(0);
        eventColumn.setMinWidth(150); // Defina uma largura mínima para a coluna de Evento

        JScrollPane scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(450, 200));
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        return scrollPane;
    }

    public static class EventData {
        private String event;
        private String date;
        private boolean checked;

        public EventData(String event, String date, boolean checked) {
            this.event = event;
            this.checked = checked;
            this.date = date;
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
