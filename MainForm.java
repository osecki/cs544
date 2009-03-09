/*
 *  File:  MainForm.java
 *  Modified:  March 11, 2009
 *  Description:  This file creates the main GUI for the client.
 *  Copyright (C) 2009 - File created for CS544 by Bill Shaya, Jordan Osecki, and Robert Cochran.
 */

import javax.media.MediaLocator;
import javax.swing.border.TitledBorder;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Rectangle;
import java.util.Vector;
import java.util.regex.*;
import javax.swing.DefaultListModel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.event.*;

public class MainForm extends JFrame implements ActionListener
{
	private static boolean udpSetup = false;
	private static boolean operator = false;
	
	private String nickname = "";
	private static String channelName = "";
	private static Vector <String> muteList;
	private static boolean newChannel = false;
	
	private static UDPReceiver udpReceiver;
	private static UDPTransmitter udpTransmitter;
	private TCPThread tcpThread;

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel pnlConnection = null;
	private JLabel lblConnIP = null;
	private JLabel lblConnPort = null;
	private static JTextField txtConnIP = null;
	private JTextField txtConnPortTCP = null;
	private static JButton btnConnect = null;
	private JLabel lblName = null;
	private JLabel lblNick = null;
	private JTextField txtName = null;
	private JTextField txtNick = null;
	private JPanel pnlChannels = null;
	private JLabel lblChannel = null;
	private static JComboBox cboChannel = null;
	private JLabel lblChanName = null;
	private JLabel lblChanDesc = null;
	private static JTextField txtChannel = null;
	private static JTextField txtChannel2 = null;
	private static JButton btnJoin = null;
	private static JButton btnPart = null;
	private static JButton btnDisconnect = null;
	private JPanel pnlChannelInfo = null;
	private JScrollPane pnlUsers = null;
	private static JList listUsers = null;
	private static JTextField txtDescription = null;
	private static JButton btnKick = null;
	private static JButton btnBan = null;
	private static JButton btnMute = null;
	private static JTextField txtConnPortUDP = null;
	private JLabel lblPass = null;
	private JPasswordField txtPass = null;
	private JButton btnSocketConn = null;
	private static JButton btnSockDisconnect = null;
	private static JTextField txtChannelDisplayName = null;
	private static JTextField txtNewDesc = null;
	private static JButton btnNewDesc = null;
	private static JButton btnRefreshUsers = null;
	private static JButton btnRefreshChannels = null;

	public MainForm() 
	{
		super();
		initialize();

		// Add a window listener for close button
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
			}
		});

		CreateMenu();						//create application menu
		InitGUI();							//initialize GUI
	}

	private void InitGUI()
	{
		btnSockDisconnect.setEnabled(false); 	//disable socket disconnect button
		btnConnect.setEnabled(false);			//disable connect button
		btnDisconnect.setEnabled(false);		//disable disconnect button

		cboChannel.setEnabled(false);			//disable channel drop down
		txtChannel.setEnabled(false);			//disable channel textbox
		txtChannel2.setEnabled(false); 			//disable channel textbox 2
		btnJoin.setEnabled(false);				//disable join button
		btnPart.setEnabled(false);				//disable part button

		btnBan.setEnabled(false);			//disable ban button
		btnKick.setEnabled(false);			//disable kick button
		btnMute.setEnabled(false);			//disable mute button

		txtChannelDisplayName.setEditable(false);	//disable
		txtDescription.setEditable(false);			//disable

		txtNewDesc.setEnabled(false);				//disable
		btnNewDesc.setEnabled(false);				//disable

		btnRefreshUsers.setEnabled(false);				//disable
		btnRefreshChannels.setEnabled(false);			//disable
	}

	// Method where events trigger
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event) 
	{			
		if (event.getSource() == btnSocketConn)
		{
			// Validate the IP and TCP Port boxes are filled correctly, give an error if not
			String[] tempIP = txtConnIP.getText().split("\\.");
			if ( tempIP.length == 4 && Pattern.matches("^\\d+$", tempIP[0]) && Pattern.matches("^\\d+$", tempIP[1]) &&
					Pattern.matches("^\\d+$", tempIP[2]) && Pattern.matches("^\\d+$", tempIP[3]) && 
					Pattern.matches("^\\d+$", this.txtConnPortTCP.getText()) )
			{
				// Open the TCP socket
				// Setup TCP thread and sockets due to the Connect command
				String hostIP = txtConnIP.getText();
				int hostTCPPort = Integer.parseInt(this.txtConnPortTCP.getText());
				tcpThread = new TCPThread(hostIP, hostTCPPort);
				tcpThread.start();

				// Change button statuses
				btnSocketConn.setEnabled(false);
				btnConnect.setEnabled(true);
			}
			else
			{
				JOptionPane.showMessageDialog(getJFrame(), "Please enter a valid IP address and TCP port number.",
						"Invalid Input Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (event.getSource() == btnSockDisconnect)
		{
			// Close the TCP socket
			// Close connections
			this.tcpThread.Close();
			udpReceiver.close();
			udpTransmitter.stopTx();

			udpSetup = false;

			// Change button statuses
			btnSockDisconnect.setEnabled(false);
			btnConnect.setEnabled(false);
			btnSocketConn.setEnabled(true);
		}
		else if (event.getSource() == btnConnect) // Button is clicked
		{
			// Validate the textboxes are filled correctly, give an error if not
			String[] tempIP = txtConnIP.getText().split("\\.");
			if ( tempIP.length == 4 && Pattern.matches("^\\d+$", tempIP[0]) && Pattern.matches("^\\d+$", tempIP[1]) &&
					Pattern.matches("^\\d+$", tempIP[2]) && Pattern.matches("^\\d+$", tempIP[3]) && 
					Pattern.matches("^\\d+$", this.txtConnPortTCP.getText()) && Pattern.matches("^\\d+$", txtConnPortUDP.getText()) &&
					! txtNick.getText().equals("") && ! txtName.getText().equals("") && ! txtPass.getText().equals("") )
			{
				// Create and send connection command
				nickname = this.txtNick.getText();
				CmdLib.CreateConnCommand(nickname, "", this.txtName.getText(), this.txtPass.getText());
			}
			else
			{
				JOptionPane.showMessageDialog(getJFrame(), "Please enter a valid IP, port numbers, and user information.",
						"Invalid Input Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (event.getSource() == btnDisconnect)
		{
			//Change button status
			btnConnect.setEnabled(true);
			btnDisconnect.setEnabled(false);	
			btnJoin.setEnabled(false);
			cboChannel.setEnabled(false);
			txtChannel.setEnabled(false);
			btnRefreshChannels.setEnabled(false);

			// Create and send disconnect command
			CmdLib.CreateDisconnCommand(this.txtNick.getText());

			// Change button statuses
			btnDisconnect.setEnabled(false);
			btnSockDisconnect.setEnabled(true);
		}
		else if (event.getSource() == btnJoin)
		{
			// If something is selected in drop-down, ALWAYS go with that
			if ( ! ((String)cboChannel.getSelectedItem()).equals("(New Channel)") )
			{
				// Otherwise grab from the drop-down menu
				channelName = ((String)cboChannel.getSelectedItem());

				// Create join command
				CmdLib.CreateJoinCommand(channelName, nickname, "");
				
				newChannel = false;
			}
			else if ( ((String)cboChannel.getSelectedItem()).equals("(New Channel)") && ! txtChannel.getText().equals("") )
			{
				// First check to see if there is one already named this
				boolean doesntExist = true;
				for (int i = 0; i < cboChannel.getItemCount(); i++)
				{
					if ( txtChannel.getText().equals(((String)cboChannel.getItemAt(i))))
						doesntExist = false;
				}

				if ( doesntExist ) // Mean it is a unique channel, so create it
				{
					// Means a new channel is being created
					channelName = txtChannel.getText();

					// Create join command
					CmdLib.CreateJoinCommand(channelName, nickname, txtChannel2.getText());
					
					newChannel = true;
				}
				else
				{
					JOptionPane.showMessageDialog(getJFrame(), "The channel name you have entered already exists, please try again.",
							"Invalid Input Error", JOptionPane.WARNING_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(getJFrame(), "Please enter a channel name or select one from the menu.",
						"Invalid Input Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (event.getSource() == btnPart)
		{
			// Create part command
			CmdLib.CreatePartCommand(channelName, nickname);

			btnJoin.setEnabled(true);
			btnPart.setEnabled(false);
			btnRefreshUsers.setEnabled(false);

			// Reset ops
			operator = false;
			btnKick.setEnabled(false);
			btnBan.setEnabled(false);
			btnMute.setEnabled(false);
			btnNewDesc.setEnabled(false);
			MainForm.txtNewDesc.setEnabled(false);
			
			muteList.removeAllElements();

			// Update bottom boxes
			txtChannelDisplayName.setText("Not connected to any channel...");
			MainForm.txtDescription.setText("(Description)");
		}
		else if (event.getSource() == btnKick)
		{
			if ( operator && listUsers.getSelectedIndex() != -1 && ! ((String)listUsers.getSelectedValue()).equals(nickname) )
			{
				CmdLib.CreateKickCommand(channelName, (String)listUsers.getSelectedValue());
			}
			else
			{
				JOptionPane.showMessageDialog(getJFrame(), "Please select a nickname (not your own) before performing this action.",
						"No nickname selected", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (event.getSource() == btnBan)
		{
			if ( operator && listUsers.getSelectedIndex() != -1 && ! ((String)listUsers.getSelectedValue()).equals(nickname) )
			{
				CmdLib.CreateBanCommand(channelName, (String)listUsers.getSelectedValue());
			}
			else
			{
				JOptionPane.showMessageDialog(getJFrame(), "Please select a nickname (not your own) before performing this action.",
						"No nickname selected", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (event.getSource() == btnMute)
		{
			if ( listUsers.getSelectedIndex() != -1 && ! ((String)listUsers.getSelectedValue()).equals(nickname) )
			{
				//get the user name
				String userName = (String)listUsers.getSelectedValue();
				
				//if they are already muted, strip out (Muted)
				int mutedPos = userName.indexOf(" (Muted)");

				if (mutedPos >= 0)
					userName = userName.substring(0, mutedPos);
				
				CmdLib.CreateMuteCommand(channelName, userName);
				
				if ( ! muteList.contains(userName ) )
						muteList.addElement(userName);
				else
					muteList.remove(userName);
			}
			else
			{
				JOptionPane.showMessageDialog(getJFrame(), "Please select a nickname (not your own) before performing this action.",
						"No nickname selected", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (event.getSource() == btnNewDesc)
		{
			if ( ! txtNewDesc.getText().equals("") )
			{
				CmdLib.CreateNewDescCommand(channelName, txtNewDesc.getText());
			}
			else
			{
				JOptionPane.showMessageDialog(getJFrame(), "Please enter a description before performing this action.",
						"No description entered", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (event.getSource() == btnRefreshUsers)
		{
			CmdLib.CreateGetUsersCommand(channelName);
		}
		else if (event.getSource() == btnRefreshChannels)
		{
			CmdLib.CreateGetChansCommand();
		}
		else
		{
			//			//this is a menu item
			//			JMenuItem menuItem = (JMenuItem)event.getSource();
			//
			//			if (menuItem.getText() == "Connect")
			//				CmdLib.CreateConnCommand("billNick", "127.0.0.1", "bill", "abcd");
			//			else if (menuItem.getText() == "Disconnect")
			//				CmdLib.CreateDisconnCommand("billNick");
			//			else if (menuItem.getText() == "GetChans")
			//				CmdLib.CreateGetChansCommand();
			//			else if (menuItem.getText() == "GetUsers")
			//				CmdLib.CreateGetUsersCommand("java");
			//			else if (menuItem.getText() == "Join")
			//				CmdLib.CreateJoinCommand("java", "billNick", "");
			//			else if (menuItem.getText() == "Part")
			//				CmdLib.CreatePartCommand("java", "billNick");
		}
	}	

	public static void UpdateGUI(ResponseMessage respMsg)
	{
		// Takes in a response message and updates the GUI accordingly

		if (respMsg.getCommand().equals("GetChans"))
		{
			// Re-creates the items in the channels drop-down menu
			cboChannel.removeAllItems();

			for (int i = 0; i < respMsg.getChannelNames().size(); i++)
				cboChannel.insertItemAt(respMsg.getChannelNames().elementAt(i), 0);

			cboChannel.insertItemAt("(New Channel)", 0);
			cboChannel.setSelectedIndex(0);
		}
		else if (respMsg.getCommand().equals("GetUsers"))
		{
			DefaultListModel dlm = new DefaultListModel();

			for (int i = 0; i < respMsg.getUserNicks().size(); i++)
			{
				if ( muteList.contains(respMsg.getUserNicks().elementAt(i)))
					dlm.addElement(respMsg.getUserNicks().elementAt(i) + " (Muted)");
				else
					dlm.addElement(respMsg.getUserNicks().elementAt(i));
			}

			listUsers.setModel(dlm);
		}
		else if ( respMsg.getCommand().equals("Conn") )
		{
			if ( respMsg.getStatusCode() == 0 )
			{
				// Setting up UDP Port
				if ( ! udpSetup )
				{
					udpSetup = true;

					// Setup UDP receiver thread
					String[] argv = new String[1];
					argv[0] = txtConnIP.getText() + "/" + txtConnPortUDP.getText();
					udpReceiver = new UDPReceiver(argv);
					udpReceiver.start();

					// Setup UDP transmitter thread
					udpTransmitter = new UDPTransmitter(new MediaLocator("javasound://44100"),
							txtConnIP.getText(), txtConnPortUDP.getText(), null);    
					udpTransmitter.start();
				}
				
				//Change button status
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(true);
				btnJoin.setEnabled(true);
				cboChannel.setEnabled(true);
				btnRefreshChannels.setEnabled(true);
				txtChannel.setEnabled(true);
				txtChannel2.setEnabled(true);
				btnSockDisconnect.setEnabled(false);
				
				CmdLib.CreateGetChansCommand();
			}
			else
			{
				JOptionPane.showMessageDialog(new MainForm(), "Error connecting or you used a nickname that already exists.",
						"Invalid Connect Attempt", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if ( respMsg.getCommand().equals("Part") )
		{
			CmdLib.CreateGetChansCommand();
		}
		else if ( respMsg.getCommand().equals("Mute") )
		{
			CmdLib.CreateGetUsersCommand(channelName);
		}
		else if ( respMsg.getCommand().equals("Kick") )
		{
			// First check to see if this is an ACTION command to inform a user they were kicked
			if ( respMsg.getStatusMsg().contains("Kicked from"))
			{
				// Reset basic buttons
				btnJoin.setEnabled(true);
				btnPart.setEnabled(false);
				btnRefreshUsers.setEnabled(false);

				// Reset ops and mute
				operator = false;
				btnKick.setEnabled(false);
				btnBan.setEnabled(false);
				btnMute.setEnabled(false);
				btnNewDesc.setEnabled(false);
				txtNewDesc.setEnabled(false);
				
				muteList.removeAllElements();

				// Update bottom boxes
				txtChannelDisplayName.setText("Not connected to any channel...");
				MainForm.txtDescription.setText("(Description)");
			}
			
			// If operator gets a failed response
			if ( respMsg.getStatusCode() == 1 )
			{
				JOptionPane.showMessageDialog(Main.getTemp(), "Error, User could not be kicked from the channel.",
						"Failed Kick Attempt", JOptionPane.WARNING_MESSAGE);
			}
			
			CmdLib.CreateGetUsersCommand(channelName);
		}
		else if ( respMsg.getCommand().equals("Ban") )
		{
			// First check to see if this is an ACTION command to inform a user they were banned
			if ( respMsg.getStatusMsg().contains("Banned from"))
			{
				// Reset basic buttons
				btnJoin.setEnabled(true);
				btnPart.setEnabled(false);
				btnRefreshUsers.setEnabled(false);

				// Reset ops and mute
				operator = false;
				btnKick.setEnabled(false);
				btnBan.setEnabled(false);
				btnMute.setEnabled(false);
				btnNewDesc.setEnabled(false);
				txtNewDesc.setEnabled(false);
				
				muteList.removeAllElements();

				// Update bottom boxes
				txtChannelDisplayName.setText("Not connected to any channel...");
				MainForm.txtDescription.setText("(Description)");
			}
			
			// If operator gets a failed response
			if ( respMsg.getStatusCode() == 1 )
			{
				JOptionPane.showMessageDialog(Main.getTemp(), "Error, User could not be banned from the channel.",
						"Failed Ban Attempt", JOptionPane.WARNING_MESSAGE);
			}
			
			CmdLib.CreateGetUsersCommand(channelName);
		}
		else if (respMsg.getCommand().equals("Join"))
		{
			// If Successfully joined
			if ( respMsg.getStatusCode() == 0)
			{
				// Change button statuses
				btnJoin.setEnabled(false);
				btnPart.setEnabled(true);
				btnMute.setEnabled(true);
				btnRefreshUsers.setEnabled(true);
				
				// Create mute
				muteList = new Vector <String> ();

				// Update bottom boxes
				txtChannelDisplayName.setText("Connected to ... " + channelName);
				txtDescription.setText(respMsg.getStatusMsg());
				
				// Issue getusers
				CmdLib.CreateGetUsersCommand(channelName);
				
				if ( newChannel )
				{
					// Created new one, so operator
					operator = true;
					btnKick.setEnabled(true);
					btnBan.setEnabled(true);
					btnNewDesc.setEnabled(true);
					MainForm.txtNewDesc.setEnabled(true);
				}
			}
			// Unsuccessfully joined because banned from it
			else
			{
				JOptionPane.showMessageDialog(Main.getTemp(), "Cannot join this channel because you have been banned from it.",
						"Invalid Join Attempt", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if ((respMsg.getCommand().equals("NewDesc")) && (respMsg.getStatusCode() == 0))
		{
			txtDescription.setText(txtNewDesc.getText());
		}
	}

	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("Voice IRC");
		this.setBounds(new Rectangle(0, 0, 595, 565));
	}

	private JFrame getJFrame ()
	{
		return this;
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getPnlConnection(), null);
			jContentPane.add(getPnlChannels(), null);
			jContentPane.add(getPnlChannelInfo(), null);
		}
		return jContentPane;
	}

	private void CreateMenu()
	{
		JMenuBar menuBar = new JMenuBar();						//create menu bar

		//Add File menu item
		JMenu fileMenu = new JMenu("File");  	 		 	    //create file item
		//JMenuItem fileMenuItem1 = new JMenuItem("Open");	    //create open sub item
		JMenuItem fileMenuItem2 = new JMenuItem("Quit");  		//create quit sub item
		//fileMenu.add(fileMenuItem1);							//add menu items
		fileMenu.add(fileMenuItem2);							//add menu items
		menuBar.add(fileMenu);									//add file menu

		//Add Help menu item
		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpMenuItem1 = new JMenuItem("Tutorial");  	//list sub item
		JMenuItem helpMenuItem2 = new JMenuItem("About");    	//list sub item
		helpMenu.add(helpMenuItem1);
		helpMenu.add(helpMenuItem2);
		menuBar.add(helpMenu);	

		//Add Unit Test menu item
		//JMenu testMenu = new JMenu("Unit Test");
		//JMenuItem testMenuItem1 = new JMenuItem("Connect");  	//list sub item		
		//JMenuItem testMenuItem2 = new JMenuItem("Disconnect"); 	//list sub item
		//JMenuItem testMenuItem3 = new JMenuItem("GetChans"); 	//list sub item
		//JMenuItem testMenuItem4 = new JMenuItem("GetUsers"); 	//list sub item
		//JMenuItem testMenuItem5 = new JMenuItem("Join");	 	//list sub item
		//JMenuItem testMenuItem6 = new JMenuItem("Part");	 	//list sub item
		//testMenu.add(testMenuItem1);
		//testMenu.add(testMenuItem2);
		//testMenu.add(testMenuItem3);
		//testMenu.add(testMenuItem4);
		//testMenu.add(testMenuItem5);
		//testMenu.add(testMenuItem6);
		//menuBar.add(testMenu);

		//Add action events to test menu items
		//testMenuItem1.addActionListener(this);
		//testMenuItem2.addActionListener(this);
		//testMenuItem3.addActionListener(this);
		//testMenuItem4.addActionListener(this);
		//testMenuItem5.addActionListener(this);
		//testMenuItem6.addActionListener(this);

		this.setJMenuBar(menuBar);								//add the menu bar to the frame

		// ActionListener for loadItem
		//		fileMenuItem1.addActionListener(new ActionListener () 
		//        {
		//        	public void actionPerformed(ActionEvent arg0) 
		//			{
		//        		final JFileChooser fc = new JFileChooser();
		//        		int returnVal = fc.showOpenDialog(getJFrame());
		//
		//                if (returnVal == JFileChooser.APPROVE_OPTION)
		//                {
		//                    File file = fc.getSelectedFile();
		//                    
		//                    // IMPLEMENT WHAT TO DO WITH FILE HERE
		//                    System.out.println(file.getAbsolutePath());
		//                } 
		//			}
		//        });

		// ActionListener for exitItem
		fileMenuItem2.addActionListener(new ActionListener () 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);
			}
		});

		// Create the About and help panel
		final JFrame aboutPanel = new AboutPanel ();
		final JFrame tutorialPanel = new TutorialPanel ();

		// ActionListener for tutorialItem
		helpMenuItem1.addActionListener(new ActionListener () 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				tutorialPanel.pack ();
				tutorialPanel.setVisible (true);
			}
		});

		// ActionListener for aboutItem
		helpMenuItem2.addActionListener(new ActionListener () 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				aboutPanel.pack ();
				aboutPanel.setVisible (true);
			}
		});
	}

	private JPanel getPnlConnection() {
		if (pnlConnection == null) {
			lblPass = new JLabel();
			lblPass.setBounds(new Rectangle(9, 144, 82, 19));
			lblPass.setText("Password:");
			lblNick = new JLabel();
			lblNick.setBounds(new Rectangle(9, 114, 82, 19));
			lblNick.setText("Nickname:");
			lblName = new JLabel();
			lblName.setBounds(new Rectangle(9, 84, 82, 19));
			lblName.setText("Real Name:");
			lblConnPort = new JLabel();
			lblConnPort.setBounds(new Rectangle(9, 54, 82, 19));
			lblConnPort.setText("Server Ports:");
			lblConnIP = new JLabel();
			lblConnIP.setBounds(new Rectangle(9, 24, 82, 19));
			lblConnIP.setText("Server IP Add:");
			pnlConnection = new JPanel();
			pnlConnection.setBorder(new TitledBorder("Connection"));
			pnlConnection.setLayout(null);
			pnlConnection.setBounds(new Rectangle(15, 15, 279, 240));
			pnlConnection.add(lblConnIP, null);
			pnlConnection.add(lblConnPort, null);
			pnlConnection.add(getTxtConnIP(), null);
			pnlConnection.add(getTxtConnPortTCP(), null);
			pnlConnection.add(getBtnConnect(), null);
			pnlConnection.add(lblName, null);
			pnlConnection.add(lblNick, null);
			pnlConnection.add(getTxtName(), null);
			pnlConnection.add(getTxtNick(), null);
			pnlConnection.add(getBtnDisconnect(), null);
			pnlConnection.add(getTxtConnPortUDP(), null);
			pnlConnection.add(lblPass, null);
			pnlConnection.add(getTxtPass(), null);
			pnlConnection.add(getBtnSocketConn(), null);
			pnlConnection.add(getBtnSockDisconnect(), null);

			txtConnPortTCP.addFocusListener(new FocusListener()
			{
				public void focusGained (FocusEvent e) { addFocus(txtConnPortTCP); }
				public void focusLost (FocusEvent e) {}
			});

			txtConnPortUDP.addFocusListener(new FocusListener()
			{
				public void focusGained (FocusEvent e) { addFocus(txtConnPortUDP); }
				public void focusLost (FocusEvent e) {}
			});
		}
		return pnlConnection;
	}

	// Method used to create focus in each text field
	public void addFocus(JTextField tf)
	{
		tf.selectAll();
	}

	private JTextField getTxtConnIP() {
		if (txtConnIP == null) {
			txtConnIP = new JTextField();
			txtConnIP.setBounds(new Rectangle(95, 24, 175, 20));
			txtConnIP.setText("129.25.9.");
		}
		return txtConnIP;
	}

	private JTextField getTxtConnPortTCP() {
		if (txtConnPortTCP == null) {
			txtConnPortTCP = new JTextField();
			txtConnPortTCP.setBounds(new Rectangle(95, 54, 77, 20));
			txtConnPortTCP.setText("TCP");
		}
		return txtConnPortTCP;
	}

	private JButton getBtnConnect() {
		if (btnConnect == null) {
			btnConnect = new JButton();
			btnConnect.setBounds(new Rectangle(9, 204, 100, 20));
			btnConnect.setText("Connect");
			btnConnect.addActionListener(this);
		}
		return btnConnect;
	}

	private JTextField getTxtName() {
		if (txtName == null) {
			txtName = new JTextField();
			txtName.setBounds(new Rectangle(95, 84, 175, 20));
			txtName.setText("");
		}
		return txtName;
	}

	private JTextField getTxtNick() {
		if (txtNick == null) {
			txtNick = new JTextField();
			txtNick.setBounds(new Rectangle(95, 114, 175, 20));
			txtNick.setText("");
		}
		return txtNick;
	}

	private JPanel getPnlChannels() {
		if (pnlChannels == null) {
			lblChanName = new JLabel();
			lblChanName.setBounds(new Rectangle(9, 84, 70, 19));
			lblChanName.setText("New Name:");
			lblChanDesc = new JLabel();
			lblChanDesc.setBounds(new Rectangle(9, 114, 70, 19));
			lblChanDesc.setText("New Desc:");
			lblChannel = new JLabel();
			lblChannel.setBounds(new Rectangle(9, 24, 70, 19));
			lblChannel.setText("Channels:");
			pnlChannels = new JPanel();
			pnlChannels.setBorder(new TitledBorder("Channels"));
			pnlChannels.setLayout(null);
			pnlChannels.setBounds(new Rectangle(305, 15, 268, 240));
			pnlChannels.add(lblChannel, null);
			pnlChannels.add(getCboChannel(), null);
			pnlChannels.add(lblChanName, null);
			pnlChannels.add(getTxtChannel(), null);
			pnlChannels.add(lblChanDesc, null);
			pnlChannels.add(getTxtChannel2(), null);
			pnlChannels.add(getBtnJoin(), null);
			pnlChannels.add(getBtnPart(), null);
			pnlChannels.add(getBtnRefreshChannels(), null);
		}
		return pnlChannels;
	}

	private JComboBox getCboChannel() {
		if (cboChannel == null) {
			cboChannel = new JComboBox();
			cboChannel.setBounds(new Rectangle(83, 24, 176, 20));
			cboChannel.addItem("(New Channel)");
		}
		return cboChannel;
	}

	private JTextField getTxtChannel() {
		if (txtChannel == null) {
			txtChannel = new JTextField();
			txtChannel.setBounds(new Rectangle(83, 84, 176, 20));
		}
		return txtChannel;
	}

	private JTextField getTxtChannel2() {
		if (txtChannel2 == null) {
			txtChannel2 = new JTextField();
			txtChannel2.setBounds(new Rectangle(83, 114, 176, 20));
		}
		return txtChannel2;
	}

	private JButton getBtnJoin() {
		if (btnJoin == null) {
			btnJoin = new JButton();
			btnJoin.addActionListener(this);
			btnJoin.setBounds(new Rectangle(9, 174, 100, 20));
			btnJoin.setText("Join");
		}
		return btnJoin;
	}

	private JButton getBtnPart() {
		if (btnPart == null) {
			btnPart = new JButton();
			btnPart.addActionListener(this);
			btnPart.setBounds(new Rectangle(159, 174, 100, 20));
			btnPart.setText("Leave");
		}
		return btnPart;
	}

	private JButton getBtnDisconnect() {
		if (btnDisconnect == null) {
			btnDisconnect = new JButton();
			btnDisconnect.addActionListener(this);
			btnDisconnect.setBounds(new Rectangle(170, 203, 100, 20));
			btnDisconnect.setText("Disconnect");
		}
		return btnDisconnect;
	}

	private JPanel getPnlChannelInfo() {
		if (pnlChannelInfo == null) {
			pnlChannelInfo = new JPanel();
			pnlChannelInfo.setBorder(new TitledBorder("Channel Information"));
			pnlChannelInfo.setLayout(null);
			pnlChannelInfo.setBounds(new Rectangle(15, 262, 558, 232));
			pnlChannelInfo.add(getPnlUsers(), null);
			pnlChannelInfo.add(getTxtDescription(), null);
			pnlChannelInfo.add(getBtnKick(), null);
			pnlChannelInfo.add(getBtnBan(), null);
			pnlChannelInfo.add(getBtnMute(), null);
			pnlChannelInfo.add(getTxtChannelDisplayName(), null);
			pnlChannelInfo.add(getTxtNewDesc(), null);
			pnlChannelInfo.add(getBtnNewDesc(), null);
			pnlChannelInfo.add(getBtnRefreshUsers(), null);
		}
		return pnlChannelInfo;
	}

	private JScrollPane getPnlUsers() {
		if (pnlUsers == null) {
			pnlUsers = new JScrollPane();
			pnlUsers.setBorder(new TitledBorder("Users"));
			pnlUsers.setBounds(new Rectangle(9, 24, 190, 163));
			pnlUsers.setViewportView(getListUsers());
		}
		return pnlUsers;
	}

	private JList getListUsers() {
		if (listUsers == null) {
			listUsers = new JList();
		}
		return listUsers;
	}

	private JTextField getTxtDescription() {
		if (txtDescription == null) {
			txtDescription = new JTextField();
			txtDescription.setBounds(new Rectangle(209, 88, 338, 48));
			txtDescription.setText("(Description)");
		}
		return txtDescription;
	}

	private JButton getBtnKick() {
		if (btnKick == null) {
			btnKick = new JButton();
			btnKick.setBounds(new Rectangle(209, 194, 100, 20));
			btnKick.setText("Kick");
			btnKick.addActionListener(this);
		}
		return btnKick;
	}

	private JButton getBtnBan() {
		if (btnBan == null) {
			btnBan = new JButton();
			btnBan.setBounds(new Rectangle(328, 194, 100, 20));
			btnBan.setText("Ban");
			btnBan.addActionListener(this);
		}
		return btnBan;
	}

	private JButton getBtnMute() {
		if (btnMute == null) {
			btnMute = new JButton();
			btnMute.setBounds(new Rectangle(448, 194, 100, 20));
			btnMute.setText("Mute");
			btnMute.addActionListener(this);
		}
		return btnMute;
	}

	private JTextField getTxtConnPortUDP() {
		if (txtConnPortUDP == null) {
			txtConnPortUDP = new JTextField();
			txtConnPortUDP.setBounds(new Rectangle(193, 54, 77, 20));
			txtConnPortUDP.setText("UDP");
		}
		return txtConnPortUDP;
	}

	private JTextField getTxtPass() {
		if (txtPass == null) {
			txtPass = new JPasswordField();
			txtPass.setBounds(new Rectangle(95, 144, 175, 20));
			txtPass.setText("");
			txtPass.setEchoChar('*');
		}
		return txtPass;
	}

	private JButton getBtnSocketConn() {
		if (btnSocketConn == null) {
			btnSocketConn = new JButton();
			btnSocketConn.addActionListener(this);
			btnSocketConn.setBounds(new Rectangle(9, 174, 100, 20));
			btnSocketConn.setText("TCP Open");
		}
		return btnSocketConn;
	}

	private JButton getBtnSockDisconnect() {
		if (btnSockDisconnect == null) {
			btnSockDisconnect = new JButton();
			btnSockDisconnect.addActionListener(this);
			btnSockDisconnect.setBounds(new Rectangle(170, 174, 100, 20));
			btnSockDisconnect.setText("TCP Close");
		}
		return btnSockDisconnect;
	}

	/**
	 * This method initializes txtChannelDisplayName	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextField getTxtChannelDisplayName() {
		if (txtChannelDisplayName == null) {
			txtChannelDisplayName = new JTextField();
			txtChannelDisplayName.setBounds(new Rectangle(209, 29, 338, 48));
			txtChannelDisplayName.setText("Not connected to any channel...");
		}
		return txtChannelDisplayName;
	}

	/**
	 * This method initializes txtNewChan	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextField getTxtNewDesc() {
		if (txtNewDesc == null) {
			txtNewDesc = new JTextField();
			txtNewDesc.setBounds(new Rectangle(209, 163, 160, 20));
			txtNewDesc.setText("");
		}
		return txtNewDesc;
	}

	/**
	 * This method initializes btnNewDesc	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnNewDesc() {
		if (btnNewDesc == null) {
			btnNewDesc = new JButton();
			btnNewDesc.setBounds(new Rectangle(388, 163, 160, 20));
			btnNewDesc.setText("Change Description");
			btnNewDesc.addActionListener(this);
		}
		return btnNewDesc;
	}

	/**
	 * This method initializes btnRefreshUsers	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnRefreshUsers() {
		if (btnRefreshUsers == null) {
			btnRefreshUsers = new JButton();
			btnRefreshUsers.setBounds(new Rectangle(33, 194, 140, 20));
			btnRefreshUsers.setText("Refresh Users");
			btnRefreshUsers.addActionListener(this);
		}
		return btnRefreshUsers;
	}

	/**
	 * This method initializes btnRefreshChannels	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnRefreshChannels() {
		if (btnRefreshChannels == null) {
			btnRefreshChannels = new JButton();
			btnRefreshChannels.setBounds(new Rectangle(101, 53, 140, 20));
			btnRefreshChannels.setText("Refresh Channels");
			btnRefreshChannels.addActionListener(this);
		}
		return btnRefreshChannels;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"