package com.nfw;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetDate {
	public String GetAllDate(){
		LocalDateTime now = LocalDateTime.now(); // Get the current date and time
	     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Define the date format
	     String formattedDate = now.format(formatter); // Format the date
		return formattedDate;
	}
	
	public String JustDMY() {
		return "1232343543545656576";
		
	}

}

