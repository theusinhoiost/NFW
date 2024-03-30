package com.nfw;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import com.toedter.calendar.JCalendar;

public class StartFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	// Import ScreenSizeHelper
	ScreenSizeHelper fromSSH = new ScreenSizeHelper();
	// Import GetDate
	GetDate fromGD = new GetDate();
	public String dateComplete = fromGD.GetAllDate();
	// Create Panels
	Panel pc = new Panel(new GridBagLayout());
	Panel eventCard = new Panel(new GridBagLayout());
	// Create MenuBar
	JMenuBar menubar = new JMenuBar();
	JMenu settings = new JMenu("Settings");
	JMenu events = new JMenu("Events");
	JMenuItem settingExitButton = new JMenuItem("Exit");
	JMenuItem newActionButton = new JMenuItem("New event");
	JMenuItem editButton = new JMenuItem("Edit event");
	JCheckBoxMenuItem darkTheme = new JCheckBoxMenuItem("Dark Theme");
	// Objects Principal Card
	JTextArea Clock = new JTextArea(dateComplete);
	GridBagConstraints clockConstraints = new GridBagConstraints();
	// Objects EventCard Card
	JButton ButtonCreateEventCard = new JButton("Add event");
	JTextArea txtArea1 = new JTextArea();
	JRadioButton blueSelector = new JRadioButton("Normal");
	JRadioButton yellowSelector = new JRadioButton("Urgency");
	JRadioButton redSelector = new JRadioButton("Emergency");
	GridBagConstraints eventCardConstraints = new GridBagConstraints();
	// Date selector
	JCalendar calendar = new JCalendar();
	// JCOmboBox
	String[] levels = { "Normal", "Urgency", "Emergency" };
	JComboBox<String> comboBox = new JComboBox<>(levels);
	// Database
	Properties prop = new Properties();
	InputStream input = null;

	public static void main(String[] args) {
		// Create the JFrame Object
		try {
			new StartFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public StartFrame() {
		try {
			this.connectWithDataBase();
			this.startWin();
			this.XCloseButton();
			this.configMenuBar();
			this.setVisible(true);
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connectWithDataBase() {
		try {
			input = new FileInputStream("db.properties");
            prop.load(input);
			String url = prop.getProperty("db.url");
			String user = prop.getProperty("db.user");
			String password = prop.getProperty("db.password");
			Connection connection = DriverManager.getConnection(url, user, password); // Connect with DB
			System.out.println("Connected to the database!");// Connected with DB

		} catch (IOException | SQLException ex) {
			System.err.println("Error connecting to the database: " + ex.getMessage());
		} 
		finally
		{
			if (input != null) {
				try {
					input.close();		
				} catch (IOException e) {
					e.printStackTrace();
				}
              
		}
	}
}

	private void startWin() {
		int[] bounds = fromSSH.ScreenBounds();
		this.setTitle("NFW");
		this.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		this.add(pc);

	}

	private void XCloseButton() { // Close with "X" - WINDOWS INTERFACE
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				XClose();
			}
		});
	}

	private void configMenuBar() {
		setJMenuBar(menubar);
		menubar.add(settings);
		menubar.add(events);
		events.add(editButton);
		events.add(newActionButton);
		settings.add(darkTheme);
		settings.add(settingExitButton);

		settingExitButton.addActionListener(new ActionListener() { // Close Win with "exit" in settings
			public void actionPerformed(ActionEvent e) {
				XClose();
			}
		});

		newActionButton.addActionListener(new ActionListener() {// Open the "New Event" screen
			public void actionPerformed(ActionEvent e) {
				configEventCard();
				getContentPane().removeAll();
				getContentPane().add(eventCard);
				revalidate();
				repaint();
			}

		});
		darkTheme.addActionListener(new ActionListener() {
			// Dark mode
			boolean darkThemeEnabled = false;

			public void actionPerformed(ActionEvent e) {
				darkThemeEnabled = !darkThemeEnabled; // Toggle dark theme

				if (darkThemeEnabled) {
					pc.setBackground(Color.GRAY);
				} else {
					pc.setBackground(Color.WHITE);
				}

				revalidate();
				repaint();
			}
		});
	}

	private void XClose() {// used to close with "exit" in settings and WINDOWS INTERFACE
		int confirmed = JOptionPane.showConfirmDialog(StartFrame.this, "Are you sure you want to exit?",
				"Exit Confirmation", JOptionPane.YES_NO_OPTION);
		if (confirmed == JOptionPane.YES_OPTION) {
			dispose();
		} else {
			System.out.println("No, was selected");

		}

	}

	private void configEventCard() {
		configButtonCreateEventCard();
		configScrollPane();
		configJComboBox();
		configCalendar();

	}

	private void configJComboBox() {
		eventCardConstraints.gridx = 1;
		eventCardConstraints.gridy = 2;
		eventCard.add(comboBox, eventCardConstraints);

	}

	private void configCalendar() {
		eventCardConstraints.gridx = 0;
		eventCardConstraints.gridy = 0;
		eventCardConstraints.fill = GridBagConstraints.BOTH;
		eventCardConstraints.weightx = 1.0;
		eventCardConstraints.weighty = 1.0;
		eventCardConstraints.gridwidth = 2;
		eventCard.add(calendar, eventCardConstraints);

	}

	private void configButtonCreateEventCard() {
		eventCardConstraints.gridx = 0;
		eventCardConstraints.gridy = 2;
		eventCard.add(ButtonCreateEventCard, eventCardConstraints);

	}

	private void configScrollPane() {
		txtArea1.setLineWrap(true);
		txtArea1.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(txtArea1);
		eventCardConstraints.gridx = 0;
		eventCardConstraints.gridy = 1;
		eventCardConstraints.weightx = 1;
		eventCardConstraints.weighty = 1;
		eventCardConstraints.gridwidth = 2;
		eventCardConstraints.fill = GridBagConstraints.BOTH;
		eventCardConstraints.anchor = GridBagConstraints.NORTHWEST;
		eventCard.add(scrollPane, eventCardConstraints);
	}

}
