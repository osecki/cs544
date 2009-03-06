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
import java.util.regex.*;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
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
import javax.swing.JTextArea;
import java.awt.event.*;
import java.io.File;

public class MainForm extends JFrame implements ActionListener
{
	private boolean udpSetup = false;
	private boolean operator = false;
	private String nickname = "";
	private static String channelName = "";
	private UDPReceiver udpReceiver;
	private UDPTransmitter udpTransmitter;
	private TCPThread tcpThread;

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel pnlConnection = null;
	private JLabel lblConnIP = null;
	private JLabel lblConnPort = null;
	private JTextField txtConnIP = null;
	private JTextField txtConnPortTCP = null;
	private JButton btnConnect = null;
	private JLabel lblName = null;
	private JLabel lblNick = null;
	private JTextField txtName = null;
	private JTextField txtNick = null;
	private JPanel pnlChannels = null;
	private JLabel lblChannel = null;
	private static JComboBox cboChannel = null;
	//private JButton btnRefreshChan = null;
	private JLabel lblChanName = null;
	private JLabel lblChanDesc = null;
	private JTextField txtChannel = null;
	private JTextField txtChannel2 = null;
	private JButton btnJoin = null;
	private JButton btnPart = null;
	private JButton btnDisconnect = null;
	private JPanel pnlChannelInfo = null;
	private JScrollPane pnlUsers = null;
	private static JList listUsers = null;
	private JTextArea txtDescription = null;
	private JButton btnKick = null;
	private JButton btnBan = null;
	private JButton btnMute = null;
	private JTextField txtConnPortUDP = null;
	private JLabel lblPass = null;
	private JPasswordField txtPass = null;
	private JButton btnSocketConn = null;
	private JButton btnSockDisconnect = null;
	private JTextArea txtChannelDisplayName = null;
	private JTextArea txtNewDesc = null;
	private JButton btnNewDesc = null;
	private JButton btnRefreshUsers = null;

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
		this.btnSockDisconnect.setEnabled(false); //disable socket disconnect button
		this.btnConnect.setEnabled(false);		//disable connect button
		this.btnDisconnect.setEnabled(false);	//disable disconnect button
		
		this.cboChannel.setEnabled(false);		//disable channel drop down
		//this.btnRefreshChan.setEnabled(false);  //disable refresh channel button
		this.txtChannel.setEnabled(false);		//disable channel textbox
		this.txtChannel2.setEnabled(false); 	//disable channel textbox 2
		this.btnJoin.setEnabled(false);			//disable join button
		this.btnPart.setEnabled(false);			//disable part button
		
		this.btnBan.setEnabled(false);		//disable ban button
		this.btnKick.setEnabled(false);		//disable kick button
		this.btnMute.setEnabled(false);		//disable mute button
		
		this.txtChannelDisplayName.setEditable(false);	//disable
		this.txtDescription.setEditable(false);			//disable
		
		this.txtNewDesc.setEnabled(false);				//disable
		this.btnNewDesc.setEnabled(false);				//disable
		
		this.btnRefreshUsers.setEnabled(false);			//disable
	}
	
	// Method where events trigger
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
				String hostIP = this.txtConnIP.getText();
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
		else if (event.getSource() == this.btnSockDisconnect)
		{
			// Close the TCP socket
			// Close connections
			this.tcpThread.Close();
			this.udpReceiver.close();
			this.udpTransmitter.stopTx();
			
			this.udpSetup = false;
			
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
					Pattern.matches("^\\d+$", this.txtConnPortTCP.getText()) && Pattern.matches("^\\d+$", this.txtConnPortUDP.getText()) &&
					! txtNick.getText().equals("") && ! txtName.getText().equals("") && ! txtPass.getText().equals("") )
			{
				if ( ! this.udpSetup )
				{
					this.udpSetup = true;
					
			        // Setup UDP receiver thread
			        String[] argv = new String[1];
			        argv[0] = this.txtConnIP.getText() + "/" + this.txtConnPortUDP.getText();
			        udpReceiver = new UDPReceiver(argv);
			        udpReceiver.start();
		       
			        // Setup UDP transmitter thread
			    	udpTransmitter = new UDPTransmitter(new MediaLocator("javasound://44100"),
			    					     this.txtConnIP.getText(), this.txtConnPortUDP.getText(), null);    
			        udpTransmitter.start();
				}
	       
		        // Create and send connection command
				nickname = this.txtNick.getText();
				CmdLib.CreateConnCommand(nickname, "", this.txtName.getText(), this.txtPass.getText());
				
				//Change button status
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(true);
				btnJoin.setEnabled(true);
				cboChannel.setEnabled(true);
				//btnRefresh
				txtChannel.setEnabled(true);
				txtChannel2.setEnabled(true);
				btnSockDisconnect.setEnabled(false);
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
				CmdLib.CreateJoinCommand(channelName, nickname);
				
				// Create Get users command
				CmdLib.CreateGetUsersCommand(channelName);
				
				// Change button statuses
				this.btnJoin.setEnabled(false);
				this.btnPart.setEnabled(true);
				this.btnMute.setEnabled(true);
				this.btnRefreshUsers.setEnabled(true);
				
				// Update bottom boxes
				this.txtChannelDisplayName.setText("Connected to ... " + channelName);
				//this.txtDescription.setText(this.txtChannel2.getText());
				// TODO Set description here
			}
			else if ( ((String)cboChannel.getSelectedItem()).equals("(New Channel)") && ! this.txtChannel.getText().equals("") )
			{
				// Means a new channel is being created
				channelName = this.txtChannel.getText();
				
				// Create join command
				CmdLib.CreateJoinCommand(channelName, nickname);
				
				// Create Get users command
				//CmdLib.CreateGetUsersCommand(channelName);
				
				// Create New desc command
				//CmdLib.CreateNewDescCommand(channelName, this.txtChannel2.getText());
				
				// Change button statuses
				this.btnJoin.setEnabled(false);
				this.btnPart.setEnabled(true);
				this.btnMute.setEnabled(true);
				this.btnRefreshUsers.setEnabled(true);
				
				// Update bottom boxes
				this.txtChannelDisplayName.setText("Connected to ... " + channelName);
				this.txtDescription.setText(this.txtChannel2.getText());
				
				// Created new one, so operator
				operator = true;
				this.btnKick.setEnabled(true);
				this.btnBan.setEnabled(true);
				this.btnNewDesc.setEnabled(true);
				this.txtNewDesc.setEnabled(true);
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
			
			this.btnJoin.setEnabled(true);
			this.btnPart.setEnabled(false);
			this.btnRefreshUsers.setEnabled(false);
			
			// Reset ops
			operator = false;
			this.btnKick.setEnabled(false);
			this.btnBan.setEnabled(false);
			this.btnMute.setEnabled(false);
			this.btnNewDesc.setEnabled(false);
			this.txtNewDesc.setEnabled(false);
			
			// Update bottom boxes
			this.txtChannelDisplayName.setText("Not connected to any channel...");
			this.txtDescription.setText("(Description)");
		}
		else if (event.getSource() == btnKick)
		{
			//System.out.println("TEST:" + listUsers.getSelectedIndex());
			
			if ( operator && listUsers.getSelectedIndex() != -1 )
			{
				CmdLib.CreateKickCommand(channelName, (String)listUsers.getSelectedValue());
			}
			else
			{
				JOptionPane.showMessageDialog(getJFrame(), "Please select a nickname before performing this action.",
        			    "No nickname selected", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (event.getSource() == btnBan)
		{
			//System.out.println("TEST:" + listUsers.getSelectedIndex());
			
			if ( operator && listUsers.getSelectedIndex() != -1 )
			{
				CmdLib.CreateBanCommand(channelName, (String)listUsers.getSelectedValue());
			}
			else
			{
				JOptionPane.showMessageDialog(getJFrame(), "Please select a nickname before performing this action.",
        			    "No nickname selected", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (event.getSource() == btnMute)
		{
			//System.out.println("TEST:" + listUsers.getSelectedIndex());
			
			if ( listUsers.getSelectedIndex() != -1 )
			{
				CmdLib.CreateMuteCommand(channelName, (String)listUsers.getSelectedValue());
			}
			else
			{
				JOptionPane.showMessageDialog(getJFrame(), "Please select a nickname before performing this action.",
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
		else
		{
			//this is a menu item
			JMenuItem menuItem = (JMenuItem)event.getSource();

			if (menuItem.getText() == "Connect")
				CmdLib.CreateConnCommand("billNick", "127.0.0.1", "bill", "abcd");
			else if (menuItem.getText() == "Disconnect")
				CmdLib.CreateDisconnCommand("billNick");
			else if (menuItem.getText() == "GetChans")
				CmdLib.CreateGetChansCommand();
			else if (menuItem.getText() == "GetUsers")
				CmdLib.CreateGetUsersCommand("java");
			else if (menuItem.getText() == "Join")
				CmdLib.CreateJoinCommand("java", "billNick");
			else if (menuItem.getText() == "Part")
				CmdLib.CreatePartCommand("java", "billNick");
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
			//dlm.addElement("TestName");
			
			for (int i = 0; i < respMsg.getUserNicks().size(); i++)
				dlm.addElement(respMsg.getUserNicks().elementAt(i));
			
			listUsers.setModel(dlm);
		}
		else if ( respMsg.getStatusMsg().contains("joined") ) // TODO
		{
			CmdLib.CreateGetUsersCommand(channelName);
		}
		else if ( respMsg.getStatusMsg().contains("connected") ) // TODO
		{
			CmdLib.CreateGetChansCommand();
		}
		else if ( respMsg.getStatusMsg().contains("left") ) // TODO
		{
			CmdLib.CreateGetChansCommand();
		}
		else // Not sure if more than these need to be singled out at the moment
		{
			// TODO
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
		JMenuItem fileMenuItem1 = new JMenuItem("Open");	    //create open sub item
		JMenuItem fileMenuItem2 = new JMenuItem("Quit");  		//create quit sub item
		fileMenu.add(fileMenuItem1);							//add menu items
		fileMenu.add(fileMenuItem2);							//add menu items
		menuBar.add(fileMenu);									//add file menu
		
		//Add Connection menu item
//		JMenu connMenu = new JMenu("Connection");  	 		 	//create connection item
//		JMenuItem connMenuItem1 = new JMenuItem("Connect");	    //create connect sub item
//		JMenuItem connMenuItem2 = new JMenuItem("Disconnect");  //create disconnect sub item
//		connMenu.add(connMenuItem1);							//add menu items
//		connMenu.add(connMenuItem2);							//add menu items
//		menuBar.add(connMenu);									//add file menu
		
		//Add Channels menu item
//		JMenu channelsMenu = new JMenu("Channels");
//		JMenuItem channelsMenuItem1 = new JMenuItem("Join");    //list sub item
//		JMenuItem channelsMenuItem2 = new JMenuItem("Part");    //list sub item
//		channelsMenu.add(channelsMenuItem1);
//		menuBar.add(channelsMenu);

		//Add Help menu item
		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpMenuItem1 = new JMenuItem("Tutorial");  	//list sub item
		JMenuItem helpMenuItem2 = new JMenuItem("About");    	//list sub item
		helpMenu.add(helpMenuItem1);
		helpMenu.add(helpMenuItem2);
		menuBar.add(helpMenu);	
		
		//Add Unit Test menu item
		JMenu testMenu = new JMenu("Unit Test");
		//JMenuItem testMenuItem1 = new JMenuItem("Connect");  	//list sub item		
		//JMenuItem testMenuItem2 = new JMenuItem("Disconnect"); 	//list sub item
		JMenuItem testMenuItem3 = new JMenuItem("GetChans"); 	//list sub item
		JMenuItem testMenuItem4 = new JMenuItem("GetUsers"); 	//list sub item
		//JMenuItem testMenuItem5 = new JMenuItem("Join");	 	//list sub item
		//JMenuItem testMenuItem6 = new JMenuItem("Part");	 	//list sub item
		//testMenu.add(testMenuItem1);
		//testMenu.add(testMenuItem2);
		testMenu.add(testMenuItem3);
		testMenu.add(testMenuItem4);
		//testMenu.add(testMenuItem5);
		//testMenu.add(testMenuItem6);
		menuBar.add(testMenu);
		
		//Add action events to test menu items
		//testMenuItem1.addActionListener(this);
		//testMenuItem2.addActionListener(this);
		testMenuItem3.addActionListener(this);
		testMenuItem4.addActionListener(this);
		//testMenuItem5.addActionListener(this);
		//testMenuItem6.addActionListener(this);
		
		this.setJMenuBar(menuBar);								//add the menu bar to the frame
		
		// ActionListener for loadItem
		fileMenuItem1.addActionListener(new ActionListener () 
        {
        	public void actionPerformed(ActionEvent arg0) 
			{
        		final JFileChooser fc = new JFileChooser();
        		int returnVal = fc.showOpenDialog(getJFrame());

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File file = fc.getSelectedFile();
                    
                    // IMPLEMENT WHAT TO DO WITH FILE HERE
                    System.out.println(file.getAbsolutePath());
                } 
			}
        });
		
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

	private JTextArea getTxtDescription() {
		if (txtDescription == null) {
			txtDescription = new JTextArea();
			txtDescription.setBounds(new Rectangle(209, 88, 338, 48));
			txtDescription.setLineWrap(true);
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
	private JTextArea getTxtChannelDisplayName() {
		if (txtChannelDisplayName == null) {
			txtChannelDisplayName = new JTextArea();
			txtChannelDisplayName.setBounds(new Rectangle(209, 29, 338, 48));
			txtChannelDisplayName.setLineWrap(true);
			txtChannelDisplayName.setText("Not connected to any channel...");
		}
		return txtChannelDisplayName;
	}

	/**
	 * This method initializes txtNewChan	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTxtNewDesc() {
		if (txtNewDesc == null) {
			txtNewDesc = new JTextArea();
			txtNewDesc.setBounds(new Rectangle(209, 163, 160, 20));
			txtNewDesc.setLineWrap(true);
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
}  //  @jve:decl-index=0:visual-constraint="10,10"