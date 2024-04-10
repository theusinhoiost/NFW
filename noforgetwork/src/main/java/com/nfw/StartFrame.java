package com.nfw;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import com.toedter.calendar.JCalendar;

import static com.nfw.DatabaseManager.getDataAndDisplay;


public class StartFrame extends JFrame {
	// Import ScreenSizeHelper
	ScreenSizeHelper fromSSH = new ScreenSizeHelper();
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
	//Dark Theme
	JCheckBoxMenuItem darkTheme = new JCheckBoxMenuItem("Dark Theme");
	// Objects Principal Card
	JTextArea getTxtEvents = new JTextArea();
	// Objects EventCard Card
	JButton ButtonCreateEventCard = new JButton("Add event");
	JTextArea giveTxtEvents = new JTextArea();
	GridBagConstraints eventCardConstraints = new GridBagConstraints();
	// Date selector
	JCalendar calendar = new JCalendar();
	// JComboBox
	String[] levels = { "Normal", "Urgency", "Emergency" };
	JComboBox<String> comboBox = new JComboBox<>(levels);


	public static void main(String[] args) {
		// Create the JFrame Object
		try {
			new StartFrame();
			Logging.logInfo("It's open(just information)");
		} catch (Exception e) {
			Logging.logFatal("Error"+ e.getMessage());
		}
	}

	public StartFrame() {
		try {
			this.startWin();
			this.XCloseButton();
			this.configMenuBar();
			this.setVisible(true);
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		} catch (Exception e) {
			Logging.logFatal("Error"+ e.getMessage());

			
		}
	}

	private void startWin() {
		int[] bounds = fromSSH.ScreenBounds();
		this.setTitle("NFW");
		this.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		this.add(pc);
		this.addGetTxtEvents();
	}

	private void addGetTxtEvents() {
		getDataAndDisplay(getTxtEvents);
		pc.add(getTxtEvents);
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
		eventCardConstraints.weighty = 0.15;
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
		eventCardConstraints.fill = GridBagConstraints.BOTH;
		eventCard.add(ButtonCreateEventCard, eventCardConstraints);
		ButtonCreateEventCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createDBandTable();
				getAndPutInformation();
			}



			private void getAndPutInformation() {
				try {
					Calendar cal = Calendar.getInstance();
       				int year = cal.get(Calendar.YEAR);
        			int month = cal.get(Calendar.MONTH) + 1; 
        			int day = cal.get(Calendar.DAY_OF_MONTH);
					String dateString = String.format("%04d-%02d-%02d", year, month, day);
					String eventText = giveTxtEvents.getText();
					int priorityNum = comboBox.getSelectedIndex();
					if (Objects.equals(eventText, "")){
						Logging.logInfo("Event is empty");
						eventText = "You don't write the event";
						DatabaseManager.insertData(dateString,eventText,priorityNum);
                    }else{
						DatabaseManager.insertData(dateString,eventText,priorityNum);
						Logging.logInfo("It's OK (StartFrame)");
					
					}

				} catch (Exception er) {
					throw new RuntimeException(er);
				}
			}

			private void createDBandTable() {
				try {
					DatabaseManager.createDBEventsIfNotExists();
					DatabaseManager.createTableIfNotExists();
				} catch (IOException er) {
					Logging.logError("Error in StartFrame"+ er.getMessage());
				}
			}
		});

	}

	private void configScrollPane() {
		giveTxtEvents.setLineWrap(true);
		giveTxtEvents.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(giveTxtEvents);
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
