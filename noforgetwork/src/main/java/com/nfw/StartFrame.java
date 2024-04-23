package com.nfw;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.URL;
import com.toedter.calendar.JCalendar;



public class StartFrame extends JFrame {
	// Import ScreenSizeHelper
	ScreenSizeHelper fromSSH = new ScreenSizeHelper();
	//
	// Create Panels
	JPanel principalCard = new JPanel(new GridBagLayout());
	Panel eventCard = new Panel(new GridBagLayout());
	//
	// Create MenuBar
	JMenuBar menubar = new JMenuBar();
	JMenu settings = new JMenu("Settings");
	JMenu events = new JMenu("Events");
	JMenuItem settingExitButton = new JMenuItem("Exit");
	JMenuItem newActionButton = new JMenuItem("New event");
	JMenuItem editButton = new JMenuItem("Edit event");
	//
	//
	// Objects Principal Card
	JLabel normalLabel = new JLabel("Normal");
	JLabel urgencyLabel = new JLabel("Urgency");
	JLabel emergencyLabel = new JLabel("Emergency");
	GridBagConstraints principalCardConstraints = new GridBagConstraints();
	//
	// Objects EventCard Card
	JButton ButtonCreateEventCard = new JButton("Add event");
	JTextArea giveTxtEvents = new JTextArea();
	GridBagConstraints eventCardConstraints = new GridBagConstraints();
	//
	// Date selector
	JCalendar calendar = new JCalendar();
	//
	// JComboBox
	String[] levels = { "Normal", "Urgency", "Emergency" };
	JComboBox<String> comboBox = new JComboBox<>(levels);
	//
	//Table
	private AddTablesInPrincipalCard tableAdder;




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
			this.createTable();
			this.addIcon();
			this.setVisible(true);
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		} catch (Exception e) {
			Logging.logFatal("Error"+ e.getMessage());

			
		}
	}

	private void addIcon() {
		ImageIcon icon = new ImageIcon("noforgetwork\\src\\main\\resources\\download.jpeg");
        Image iconImage = icon.getImage();
		this.setIconImage(iconImage);

	}

	private void createTable() {
		tableAdder = new AddTablesInPrincipalCard();
		//
		principalCardConstraints.gridx = 0;
		principalCardConstraints.gridy = 1;
		principalCard.add(tableAdder.AddNormalTable(), principalCardConstraints);
		//
		principalCardConstraints.gridx = 1;
		principalCardConstraints.gridy = 1;
		principalCard.add(tableAdder.AddUrgencyTable(), principalCardConstraints);
		//
		principalCardConstraints.gridx = 2;
		principalCardConstraints.gridy = 1;
		principalCard.add(tableAdder.AddEmergencyTable(),principalCardConstraints);
	}



	private void startWin() {
		int[] bounds = fromSSH.ScreenBounds();
		this.setTitle("NFW");
		this.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		this.add(principalCard);
		this.addGetTxtEvents();
	}

	private void addGetTxtEvents() {
		//Normal Label
		principalCardConstraints.gridx = 0;
		principalCardConstraints.gridy = 0;
		principalCardConstraints.ipadx = 50;
		Font font = new Font("Arial", Font.BOLD, 22);
		normalLabel.setFont(font);
		normalLabel.setForeground(Color.GREEN);
		principalCard.add(normalLabel,principalCardConstraints);
		//Urgency Label
		principalCardConstraints.gridx = 1;
		principalCardConstraints.gridy = 0;
		principalCardConstraints.ipadx = 50;
		 font = new Font("Arial", Font.BOLD, 22);
		urgencyLabel.setFont(font);
		urgencyLabel.setForeground(Color.YELLOW);
		principalCard.add(urgencyLabel,principalCardConstraints);
		//Emergency Label
		principalCardConstraints.gridx = 2;
		principalCardConstraints.gridy = 0;
		principalCardConstraints.ipadx = 50;
		 font = new Font("Arial", Font.BOLD, 22);
		emergencyLabel.setFont(font);
		emergencyLabel.setForeground(Color.RED);
		principalCard.add(emergencyLabel,principalCardConstraints);
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

	private void configEventCard() {//ADD components to event card
		configButtonCreateEventCard();
		configJComboBox();
		configScrollPane();
		configCalendar();

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
					Date selectedDate = calendar.getDate();
					Calendar selectedCalendar = calendar.getCalendar();
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					String selectedDateString = dateFormat.format(selectedDate);
					String eventText = giveTxtEvents.getText();
					int priorityNum = comboBox.getSelectedIndex();
					if (Objects.equals(eventText, "")){
						Logging.logInfo("Event is empty");
						eventText = "You don't write the event";
						DatabaseManager.insertData(selectedDateString,eventText,priorityNum);
                    }else{
						DatabaseManager.insertData(selectedDateString,eventText,priorityNum);
						Logging.logInfo("It's OK (StartFrame)");

					}

				} catch (Exception er) {
					throw new RuntimeException(er);
				}
			}

			private void createDBandTable() {
                DatabaseManager.createDBEventsIfNotExists();
                DatabaseManager.createTableIfNotExists();
            }
		});

	}


}
