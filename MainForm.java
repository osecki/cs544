/*
 *  File:  MainForm.java
 *  Modified:  March 11, 2009
 *  Description:  This file creates the main GUI for the client.
 *  Copyright (C) 2009 - File created for CS544 by Bill Shaya, Jordan Osecki, and Robert Cochran.
 */

import javax.media.Format;
import javax.media.MediaLocator;
import javax.swing.border.TitledBorder;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Rectangle;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.event.*;
import java.io.File;
import java.awt.Dimension;

public class MainForm extends JFrame implements ActionListener
{
	private boolean udpSetup = false;
	
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
	private JLabel lblChan2 = null;
	private JTextField txtChannel = null;
	private JButton btnJoin = null;
	private JButton btnPart = null;
	private JButton btnDisconnect = null;
	private JPanel pnlChannelInfo = null;
	private JList jList = null;  //  @jve:decl-index=0:visual-constraint="736,120"
	private JScrollPane pnlUsers = null;
	private static JList listUsers = null;
	private JTextArea txtDescription = null;
	private JButton btnKick = null;
	private JButton btnBan = null;
	private JButton btnMute = null;

	private JTextField txtConnPortUDP = null;
	private JLabel lblPass = null;
	private JTextField txtPass = null;
	private JButton btnSocketConn = null;
	private JButton btnSockDisconnect = null;
	
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
		this.txtChannel.setEnabled(false);		//disable channel textbox
		this.btnJoin.setEnabled(false);			//disable join button
		this.btnPart.setEnabled(false);			//disable part button
		
		this.btnBan.setEnabled(false);		//disable ban button
		this.btnKick.setEnabled(false);		//disable kick button
		this.btnMute.setEnabled(false);		//disable mute button
	}
	
	// Method where events trigger
	public void actionPerformed(ActionEvent event) 
	{			
		if (event.getSource() == btnSocketConn)
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
		else if (event.getSource() == this.btnSockDisconnect)
		{
			// Close the TCP socket
			// Close connections
			this.tcpThread.Close();
			this.udpReceiver.close();
			this.udpTransmitter.stopTx();
			
			this.udpSetup = false;
			
		}
		else if (event.getSource() == btnConnect) // Button is clicked
		{
			if (!this.udpSetup)
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
			CmdLib.CreateConnCommand(this.txtNick.getText(), "", this.txtName.getText(), this.txtPass.getText());
			
			//Change button status
			btnConnect.setEnabled(false);
			btnDisconnect.setEnabled(true);
			btnJoin.setEnabled(true);
			cboChannel.setEnabled(true);
			txtChannel.setEnabled(true);
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
			//if there is text in the text box, grab it
			String channelName = this.txtChannel.getText();
			String nick = this.txtNick.getText();
			
			//Create join command
			CmdLib.CreateJoinCommand(channelName, nick);
			
			//Create Get users command
			CmdLib.CreateGetUsersCommand(channelName);
			
			this.btnJoin.setEnabled(false);
			this.btnPart.setEnabled(true);
		}
		else if (event.getSource() == btnPart)
		{
			//if there is text in the text box, grab it
			String channelName = this.txtChannel.getText();
			String nick = this.txtNick.getText();			
			
			//Create part command
			CmdLib.CreatePartCommand(channelName, nick);
			
			this.btnJoin.setEnabled(true);
			this.btnPart.setEnabled(false);
		}
		else if (event.getSource() == btnKick)
		{
			
		}
		else if (event.getSource() == btnBan)
		{
			
		}
		else if (event.getSource() == btnMute)
		{
			
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
			cboChannel.removeAll();
			
			for (int i = 0; i < respMsg.getChannelNames().size(); i++)
			{
				cboChannel.addItem(respMsg.getChannelNames().elementAt(i));
			}
		}
		else if (respMsg.getCommand().equals("GetUsers"))
		{
			DefaultListModel dlm = new DefaultListModel();
			//dlm.addElement("Test1");
			//dlm.addElement("Test2");
			
			for (int i = 0; i < respMsg.getUserNicks().size(); i++)
			{
				cboChannel.addItem(respMsg.getUserNicks().elementAt(i));
			}
			
			listUsers = new JList(dlm);
		}
		else // Not sure if more than these need to be singled out at the moment
		{
			// TODO
		}
	}

	private void initialize() {
		this.setSize(619, 561);
		this.setContentPane(getJContentPane());
		this.setTitle("Voice IRC");
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
		JMenu fileMenu = new JMenu("File	");  	 		 	//create file item
		JMenuItem fileMenuItem1 = new JMenuItem("Open");	    //create open sub item
		JMenuItem fileMenuItem2 = new JMenuItem("Quit");  		//create quit sub item
		fileMenu.add(fileMenuItem1);							//add menu items
		fileMenu.add(fileMenuItem2);							//add menu items
		menuBar.add(fileMenu);									//add file menu
		
		//Add Connection menu item
		JMenu connMenu = new JMenu("Connection");  	 		 	//create connection item
		JMenuItem connMenuItem1 = new JMenuItem("Connect");	    //create connect sub item
		JMenuItem connMenuItem2 = new JMenuItem("Disconnect");  //create disconnect sub item
		connMenu.add(connMenuItem1);							//add menu items
		connMenu.add(connMenuItem2);							//add menu items
		menuBar.add(connMenu);									//add file menu
		
		//Add Channels menu item
		JMenu channelsMenu = new JMenu("Channels");
		JMenuItem channelsMenuItem1 = new JMenuItem("Join");    //list sub item
		JMenuItem channelsMenuItem2 = new JMenuItem("Part");    //list sub item
		channelsMenu.add(channelsMenuItem1);
		menuBar.add(channelsMenu);

		//Add Help menu item
		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpMenuItem1 = new JMenuItem("Tutorial");  	//list sub item
		JMenuItem helpMenuItem2 = new JMenuItem("About");    	//list sub item
		helpMenu.add(helpMenuItem1);
		helpMenu.add(helpMenuItem2);
		menuBar.add(helpMenu);	
		
		//Add Unit Test menu item
		JMenu testMenu = new JMenu("Unit Test");
		JMenuItem testMenuItem1 = new JMenuItem("Connect");  	//list sub item		
		JMenuItem testMenuItem2 = new JMenuItem("Disconnect"); 	//list sub item
		JMenuItem testMenuItem3 = new JMenuItem("GetChans"); 	//list sub item
		JMenuItem testMenuItem4 = new JMenuItem("GetUsers"); 	//list sub item
		JMenuItem testMenuItem5 = new JMenuItem("Join");	 	//list sub item
		JMenuItem testMenuItem6 = new JMenuItem("Part");	 	//list sub item
		testMenu.add(testMenuItem1);
		testMenu.add(testMenuItem2);
		testMenu.add(testMenuItem3);
		testMenu.add(testMenuItem4);
		testMenu.add(testMenuItem5);
		testMenu.add(testMenuItem6);
		menuBar.add(testMenu);
		
		//Add action events to test menu items
		testMenuItem1.addActionListener(this);
		testMenuItem2.addActionListener(this);
		testMenuItem3.addActionListener(this);
		testMenuItem4.addActionListener(this);
		testMenuItem5.addActionListener(this);
		testMenuItem6.addActionListener(this);
		
		
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
			lblPass.setBounds(new Rectangle(10, 140, 69, 20));
			lblPass.setText("Password:");
			lblNick = new JLabel();
			lblNick.setBounds(new Rectangle(10, 119, 68, 16));
			lblNick.setText("Nickname:");
			lblName = new JLabel();
			lblName.setBounds(new Rectangle(11, 89, 81, 16));
			lblName.setText("Name:");
			lblConnPort = new JLabel();
			lblConnPort.setBounds(new Rectangle(10, 55, 82, 16));
			lblConnPort.setText("Server Ports:");
			lblConnIP = new JLabel();
			lblConnIP.setBounds(new Rectangle(10, 28, 82, 19));
			lblConnIP.setText("Server IP:");
			pnlConnection = new JPanel();
			pnlConnection.setBorder(new TitledBorder("Connection"));
			pnlConnection.setLayout(null);
			pnlConnection.setBounds(new Rectangle(15, 15, 285, 240));
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
		}
		return pnlConnection;
	}

	private JTextField getTxtConnIP() {
		if (txtConnIP == null) {
			txtConnIP = new JTextField();
			txtConnIP.setBounds(new Rectangle(95, 25, 175, 20));
		}
		return txtConnIP;
	}

	private JTextField getTxtConnPortTCP() {
		if (txtConnPortTCP == null) {
			txtConnPortTCP = new JTextField();
			txtConnPortTCP.setBounds(new Rectangle(95, 55, 77, 20));
			txtConnPortTCP.setText("");
		}
		return txtConnPortTCP;
	}


	private JButton getBtnConnect() {
		if (btnConnect == null) {
			btnConnect = new JButton();
			btnConnect.setBounds(new Rectangle(68, 203, 100, 20));
			btnConnect.setText("Connect");
			btnConnect.addActionListener(this);
		}
		return btnConnect;
	}

	private JTextField getTxtName() {
		if (txtName == null) {
			txtName = new JTextField();
			txtName.setBounds(new Rectangle(95, 85, 175, 20));
			txtName.setText("");
		}
		return txtName;
	}

	private JTextField getTxtNick() {
		if (txtNick == null) {
			txtNick = new JTextField();
			txtNick.setBounds(new Rectangle(95, 115, 175, 20));
			txtNick.setText("");
		}
		return txtNick;
	}

	private JPanel getPnlChannels() {
		if (pnlChannels == null) {
			lblChan2 = new JLabel();
			lblChan2.setBounds(new Rectangle(16, 55, 74, 21));
			lblChan2.setText("New:");
			lblChannel = new JLabel();
			lblChannel.setBounds(new Rectangle(15, 26, 73, 19));
			lblChannel.setText("Channel:");
			pnlChannels = new JPanel();
			pnlChannels.setBorder(new TitledBorder("Channels"));
			pnlChannels.setLayout(null);
			pnlChannels.setBounds(new Rectangle(315, 15, 285, 240));
			pnlChannels.add(lblChannel, null);
			pnlChannels.add(getCboChannel(), null);
			pnlChannels.add(lblChan2, null);
			pnlChannels.add(getTxtChannel(), null);
			pnlChannels.add(getBtnJoin(), null);
			pnlChannels.add(getBtnPart(), null);
		}
		return pnlChannels;
	}

	private JComboBox getCboChannel() {
		if (cboChannel == null) {
			cboChannel = new JComboBox();
			cboChannel.setBounds(new Rectangle(93, 25, 176, 23));
		}
		return cboChannel;
	}

	private JTextField getTxtChannel() {
		if (txtChannel == null) {
			txtChannel = new JTextField();
			txtChannel.setBounds(new Rectangle(92, 55, 176, 23));
		}
		return txtChannel;
	}

	private JButton getBtnJoin() {
		if (btnJoin == null) {
			btnJoin = new JButton();
			btnJoin.addActionListener(this);
			btnJoin.setBounds(new Rectangle(70, 165, 100, 20));
			btnJoin.setText("Join");
		}
		return btnJoin;
	}

	private JButton getBtnPart() {
		if (btnPart == null) {
			btnPart = new JButton();
			btnPart.addActionListener(this);
			btnPart.setBounds(new Rectangle(175, 165, 100, 20));
			btnPart.setText("Leave");
		}
		return btnPart;
	}

	private JButton getBtnDisconnect() {
		if (btnDisconnect == null) {
			btnDisconnect = new JButton();
			btnDisconnect.addActionListener(this);
			btnDisconnect.setBounds(new Rectangle(173, 203, 100, 20));
			btnDisconnect.setText("Disconnect");
		}
		return btnDisconnect;
	}

	private JPanel getPnlChannelInfo() {
		if (pnlChannelInfo == null) {
			pnlChannelInfo = new JPanel();
			pnlChannelInfo.setBorder(new TitledBorder("Channel Information"));
			pnlChannelInfo.setLayout(null);
			pnlChannelInfo.setBounds(new Rectangle(20, 262, 583, 232));
			pnlChannelInfo.add(getPnlUsers(), null);
			pnlChannelInfo.add(getTxtDescription(), null);
			pnlChannelInfo.add(getBtnKick(), null);
			pnlChannelInfo.add(getBtnBan(), null);
			pnlChannelInfo.add(getBtnMute(), null);
		}
		return pnlChannelInfo;
	}

	private JList getJList() {
		if (jList == null) {
			jList = new JList();
		}
		return jList;
	}

	private JScrollPane getPnlUsers() {
		if (pnlUsers == null) {
			pnlUsers = new JScrollPane();
			pnlUsers.setBorder(new TitledBorder("Users"));
			pnlUsers.setBounds(new Rectangle(9, 23, 190, 193));
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
			txtDescription.setBounds(new Rectangle(207, 29, 365, 61));
			txtDescription.setLineWrap(true);
			txtDescription.setText("Channel Description Here");
		}
		return txtDescription;
	}

	private JButton getBtnKick() {
		if (btnKick == null) {
			btnKick = new JButton();
			btnKick.setBounds(new Rectangle(240, 200, 100, 20));
			btnKick.setText("Kick");
		}
		return btnKick;
	}

	private JButton getBtnBan() {
		if (btnBan == null) {
			btnBan = new JButton();
			btnBan.setBounds(new Rectangle(348, 200, 100, 20));
			btnBan.setText("Ban");
		}
		return btnBan;
	}

	private JButton getBtnMute() {
		if (btnMute == null) {
			btnMute = new JButton();
			btnMute.setBounds(new Rectangle(455, 200, 100, 20));
			btnMute.setText("Mute");
		}
		return btnMute;
	}

	private JTextField getTxtConnPortUDP() {
		if (txtConnPortUDP == null) {
			txtConnPortUDP = new JTextField();
			txtConnPortUDP.setBounds(new Rectangle(190, 55, 77, 20));
			txtConnPortUDP.setText("");
		}
		return txtConnPortUDP;
	}

	private JTextField getTxtPass() {
		if (txtPass == null) {
			txtPass = new JTextField();
			txtPass.setBounds(new Rectangle(95, 142, 175, 20));
			txtPass.setText("");
		}
		return txtPass;
	}

	private JButton getBtnSocketConn() {
		if (btnSocketConn == null) {
			btnSocketConn = new JButton();
			btnSocketConn.addActionListener(this);
			btnSocketConn.setBounds(new Rectangle(69, 174, 100, 22));
			btnSocketConn.setText("TCP Open");
		}
		return btnSocketConn;
	}

	private JButton getBtnSockDisconnect() {
		if (btnSockDisconnect == null) {
			btnSockDisconnect = new JButton();
			btnSockDisconnect.addActionListener(this);
			btnSockDisconnect.setBounds(new Rectangle(174, 174, 100, 21));
			btnSockDisconnect.setText("TCP Close");
		}
		return btnSockDisconnect;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"