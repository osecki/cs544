import java.awt.BorderLayout;
import java.awt.Menu;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JTabbedPane;
import java.awt.Rectangle;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;

	/**
	 * This is the default constructor
	 */
	public MainForm() {
		super();
		initialize();
		
		CreateMenu();						//create application menu
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(757, 466);
		this.setContentPane(getJContentPane());
		this.setTitle("Voice IRC");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
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

}  //  @jve:decl-index=0:visual-constraint="10,10"
