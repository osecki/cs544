import java.awt.BorderLayout;
import java.awt.Menu;
import javax.swing.border.TitledBorder;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JTabbedPane;
import java.awt.Rectangle;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel pnlConnection = null;
	private JLabel lblConnIP = null;
	private JLabel lblConnPort = null;
	private JTextField txtConnIP = null;
	private JTextField txtConnPort = null;
	private JButton btnConnect = null;
	private JLabel lblName = null;
	private JLabel lblNick = null;
	private JTextField txtName = null;
	private JTextField txtNick = null;
	private JPanel pnlChannels = null;
	private JLabel lblChannel = null;
	private JComboBox cboChannel = null;
	private JLabel lblChan2 = null;
	private JTextField txtChannel = null;
	private JButton btnChannel = null;
	private JButton btnPart = null;
	private JButton btnDisconnect = null;
	private JPanel pnlChannelInfo = null;
	private JList jList = null;  //  @jve:decl-index=0:visual-constraint="736,120"
	private JScrollPane pnlUsers = null;
	private JList listUsers = null;
	private JTextArea txtDescription = null;
	private JButton btnKick = null;
	private JButton btnBan = null;
	private JButton btnMute = null;
	public MainForm() {
		super();
		initialize();
		
		CreateMenu();						//create application menu
		
		//Test
		this.cboChannel.addItem("Channel1");
		this.cboChannel.addItem("Channel2");
		
	}


	private void initialize() {
		this.setSize(619, 486);
		this.setContentPane(getJContentPane());
		this.setTitle("Voice IRC");
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

		//Add Connection menu item
		JMenu fileMenu = new JMenu("Connection");  	 		 	//create connection item
		JMenuItem fileMenuItem1 = new JMenuItem("Connect");	    //create connect sub item
		JMenuItem fileMenuItem2 = new JMenuItem("Disconnect");  //create disconnect sub item
		fileMenu.add(fileMenuItem1);							//add menu items
		fileMenu.add(fileMenuItem2);							//add menu items
		menuBar.add(fileMenu);									//add file menu
		
		//Add Channels menu item
		JMenu channelsMenu = new JMenu("Channels");
		JMenuItem channelsMenuItem1 = new JMenuItem("List");    //list sub item
		channelsMenu.add(channelsMenuItem1);
		menuBar.add(channelsMenu);
		
		
		this.setJMenuBar(menuBar);								//add the menu bar to the frame
	}


	private JPanel getPnlConnection() {
		if (pnlConnection == null) {
			lblNick = new JLabel();
			lblNick.setBounds(new Rectangle(10, 119, 68, 16));
			lblNick.setText("Nickname:");
			lblName = new JLabel();
			lblName.setBounds(new Rectangle(11, 89, 81, 16));
			lblName.setText("Name:");
			lblConnPort = new JLabel();
			lblConnPort.setBounds(new Rectangle(10, 55, 82, 16));
			lblConnPort.setText("Server Port:");
			lblConnIP = new JLabel();
			lblConnIP.setBounds(new Rectangle(10, 28, 82, 19));
			lblConnIP.setText("Server IP:");
			pnlConnection = new JPanel();
			pnlConnection.setBorder(new TitledBorder("Connection"));
			pnlConnection.setLayout(null);
			pnlConnection.setBounds(new Rectangle(15, 15, 285, 175));
			pnlConnection.add(lblConnIP, null);
			pnlConnection.add(lblConnPort, null);
			pnlConnection.add(getTxtConnIP(), null);
			pnlConnection.add(getTxtConnPort(), null);
			pnlConnection.add(getBtnConnect(), null);
			pnlConnection.add(lblName, null);
			pnlConnection.add(lblNick, null);
			pnlConnection.add(getTxtName(), null);
			pnlConnection.add(getTxtNick(), null);
			pnlConnection.add(getBtnDisconnect(), null);
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

	private JTextField getTxtConnPort() {
		if (txtConnPort == null) {
			txtConnPort = new JTextField();
			txtConnPort.setBounds(new Rectangle(95, 55, 175, 20));
			txtConnPort.setText("");
		}
		return txtConnPort;
	}


	private JButton getBtnConnect() {
		if (btnConnect == null) {
			btnConnect = new JButton();
			btnConnect.setBounds(new Rectangle(70, 145, 100, 20));
			btnConnect.setText("Connect");
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
			pnlChannels.setBounds(new Rectangle(315, 15, 285, 175));
			pnlChannels.add(lblChannel, null);
			pnlChannels.add(getCboChannel(), null);
			pnlChannels.add(lblChan2, null);
			pnlChannels.add(getTxtChannel(), null);
			pnlChannels.add(getBtnChannel(), null);
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


	/**
	 * This method initializes txtChannel	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtChannel() {
		if (txtChannel == null) {
			txtChannel = new JTextField();
			txtChannel.setBounds(new Rectangle(92, 55, 176, 23));
		}
		return txtChannel;
	}

	private JButton getBtnChannel() {
		if (btnChannel == null) {
			btnChannel = new JButton();
			btnChannel.setBounds(new Rectangle(70, 145, 100, 20));
			btnChannel.setText("Join");
		}
		return btnChannel;
	}

	private JButton getBtnPart() {
		if (btnPart == null) {
			btnPart = new JButton();
			btnPart.setBounds(new Rectangle(175, 145, 100, 20));
			btnPart.setText("Part");
		}
		return btnPart;
	}

	private JButton getBtnDisconnect() {
		if (btnDisconnect == null) {
			btnDisconnect = new JButton();
			btnDisconnect.setBounds(new Rectangle(175, 145, 100, 20));
			btnDisconnect.setText("Disconnect");
		}
		return btnDisconnect;
	}

	private JPanel getPnlChannelInfo() {
		if (pnlChannelInfo == null) {
			pnlChannelInfo = new JPanel();
			pnlChannelInfo.setBorder(new TitledBorder("Channel Information"));
			pnlChannelInfo.setLayout(null);
			pnlChannelInfo.setBounds(new Rectangle(15, 197, 583, 228));
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
			pnlUsers.setBounds(new Rectangle(10, 24, 190, 196));
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
			txtDescription.setBounds(new Rectangle(211, 32, 365, 61));
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


	/**
	 * This method initializes btnBan	
	 * 	
	 * @return javax.swing.JButton	
	 */
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

}  //  @jve:decl-index=0:visual-constraint="10,10"