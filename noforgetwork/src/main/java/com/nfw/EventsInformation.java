package com.nfw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public record EventsInformation(String event, String date, String priority) {
}
public class Create{
    List<EventsInformation> events = getFromBD();
}
public static List<EventsInformation> getFromBD(){
    List<EventsInformation> events = new ArrayList<>();


}