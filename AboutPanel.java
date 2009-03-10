/*
 *  File:  AboutPanel.java
 *  Modified:  March 11, 2009
 *  Description:  This will be the frame which shows the About information.
 *  Copyright (C) 2009 - File created for CS544 by Bill Shaya, Jordan Osecki, and Robert Cochran.
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutPanel extends JFrame
{
	// Private Variables
	private static final long serialVersionUID = 0;
	JPanel mainPanel = new JPanel();
	JButton closeButton = new JButton("Close");
	JLabel aboutText = new JLabel("<html><br><br><p align='center'>Voice IRC:  The Voice Messaging Application.<p align='center'>" + 
			"<p align='center'>Version 1.0<p align='center'>(C) Copyright 2009, Osecki, Cochran, Shaya. All rights reserved.<p align='center'>" +
			"<p align='center'>This software was developed for CS544 at Drexel University in the Winter of 2009.<p align='center'>" + 
			"For more information about the software, please contact Cochran, Shaya, or Osecki.<br><br></html>");
	JLabel image = new JLabel(new ImageIcon ("AboutLogo.jpg"));
	
	// Constructor for About Panel
	public AboutPanel ()
	{
		// Instantiates the application
		super("About:  Voice IRC");
		
		image.setAlignmentX(CENTER_ALIGNMENT);
		closeButton.setAlignmentX(CENTER_ALIGNMENT);
		aboutText.setAlignmentX(CENTER_ALIGNMENT);
		
		mainPanel.setMaximumSize(new Dimension(500, 400));
		mainPanel.setMinimumSize(new Dimension(500, 400));
		mainPanel.setPreferredSize(new Dimension(500, 400));

		mainPanel.setBorder(BorderFactory.createTitledBorder("About:  Voice IRC"));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
        mainPanel.add(image);
        mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
        mainPanel.add(aboutText);
        mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
        mainPanel.add(closeButton);
        mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
        add(mainPanel);
        
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