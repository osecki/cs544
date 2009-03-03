/*
 *  File:  TutorialPanel.java
 *  Modified:  March 11, 2009
 *  Description:  This will be the frame which shows the Tutorial information.
 *  Copyright (C) 2009 - File created for CS544 by Bill Shaya, Jordan Osecki, and Robert Cochran.
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument;

public class TutorialPanel extends JFrame
{
	// Private Variables
	private static final long serialVersionUID = 0;
	JPanel mainPanel = new JPanel();
	JButton closeButton = new JButton("Close");
	JEditorPane htmlPane;
	JScrollPane scrollPane;
	
	// Constructor for TutorialPanel
	public TutorialPanel ()
	{
		// Instantiates the application
		super("Voice IRC Tutorial");
	
		try 
		{
			InputStream in = new FileInputStream("src/Tutorial.htm");
			htmlPane = new JEditorPane();
			htmlPane.setContentType("text/html");
			htmlPane.read(in, new HTMLDocument ());
			htmlPane.setEditable(false);
			
			scrollPane = new JScrollPane(htmlPane);
			scrollPane.setAlignmentX(CENTER_ALIGNMENT);
			closeButton.setAlignmentX(CENTER_ALIGNMENT);

			mainPanel.setMaximumSize(new Dimension(600, 600));
			mainPanel.setMinimumSize(new Dimension(600, 600));
			mainPanel.setPreferredSize(new Dimension(600, 600));
					
			mainPanel.setBorder(BorderFactory.createTitledBorder("Voice IRC Tutorial"));
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
			mainPanel.add(scrollPane);
			mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
			mainPanel.add(closeButton);
			mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
			add(mainPanel);
			
		}
		catch(IOException ioe)
		{
			  System.err.println("Error displaying Tutorial.");
		}
		
	    // ActionListener for closeButton
	    closeButton.addActionListener(new ActionListener () 
	    {
	    	public void actionPerformed(ActionEvent arg0) 
			{
	    		dispose();
			}
	    });
	}
}