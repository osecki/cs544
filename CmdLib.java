/*
 *  File:  CmdLib.java
 *  Modified:  March 11, 2009
 *  Description:  This file contains the information to parse and build up the XML control messages.
 *  Copyright (C) 2009 - File created for CS544 by Bill Shaya, Jordan Osecki, and Robert Cochran.
 */

// Sources:  Part of this file, along with all of the files within the com.* packages were generated using
//   the XMLSpy software and are used only to generate and parse XML messages, as was specified also in our
//   design document. If you have any questions, do not hesitate to ask.

import java.util.Vector;
import com.IRC.*;

public class CmdLib 
{
	public static int transID;

	public enum CommandType
	{
		Conn, Disconn, Join, Part, Kick, Ban, Mute, NewDesc, GetChans, GetUsers
	}

	public static int GetTransID()
	{
		return transID++;
	}

	public static void ParseIncomingMessage(String xmlMsg)
	{
		// Parse out the XML message and store the settings in ResponseMessage objects
		// and then let the GUI use them to update itself.

		try 
		{
			// Load from string
			com.IRC.IRC2 doc = com.IRC.IRC2.loadFromString(xmlMsg);

			// Generate Variables
			Long id = 0L;
			String command = "";
			Vector <String> channelNames = new Vector <String> ();
			Vector <String> channelDescs = new Vector <String> ();
			Vector <String> channelModes = new Vector <String> ();
			Vector <String> userNicks = new Vector <String> ();
			Long statusCode = 0L;
			String statusMsg = "";

			//check for VIRC root node
			if (doc.VIRC.exists())
			{
				//Grab the node
				VIRCType root = doc.VIRC.first();

				//check for transaction ID
				if (root.ID.exists())
				{
					id = root.ID.getValue();
				}

				//check for command node
				if (root.Cmd.exists())
				{
					command = root.Cmd.first().getValue();
				}

				//check for Chans node
				if (root.Chans.exists())
				{
					ChansType chans = root.Chans.first();

					for (int i = 0; i < chans.Chan.count(); i++)
					{
						String channelName;
						String channelDesc;
						String channelMode;

						// Store these in some object

						if (chans.Chan.at(i).Name.exists())
						{
							channelName = chans.Chan.at(i).Name.first().getValue();
							channelNames.add(channelName);
						}

						if (chans.Chan.at(i).Desc.exists())
						{
							channelDesc = chans.Chan.at(i).Desc.first().getValue();
							channelDescs.add(channelDesc);
						}

						if (chans.Chan.at(i).Mode.exists())
						{
							channelMode = chans.Chan.at(i).Mode.first().getValue();
							channelModes.add(channelMode);
						}
					}
				}

				//check for Users node
				if (root.Users.exists())
				{
					UsersType users = root.Users.first();

					for (int i = 0; i < users.User.count(); i++)
					{
						String userNick;

						//Store these in some object

						if (users.User.at(i).Nick.exists())
						{
							userNick = users.User.at(i).Nick.first().getValue();
							userNicks.add(userNick);
						}
					}
				}

				//check for Status node
				if (root.Stat.exists())
				{
					//Grab the node
					StatType stat = root.Stat.first();

					//check for status code
					if (stat.Code.exists())
					{
						statusCode = stat.Code.first().getValue();
					}

					//check for status message
					if (stat.Msg.exists())
					{
						statusMsg = stat.Msg.first().getValue();
					}
				}
			}

			// If valid message, send to the GUI to do whatever it needs
			// Need to populate objects and trigger the GUI to do things for updating
			ResponseMessage respMsg = new ResponseMessage (id, command, channelNames, channelDescs, channelModes, userNicks, statusCode, statusMsg);
			MainForm.UpdateGUI(respMsg);
		} 
		catch (Exception e) 
		{
			System.out.println("Error trying to parse out the server response message.");
		}	
	}

	// Method to create a conn XML command
	public static void CreateConnCommand(String nick, String IP, String name, String pass)
	{
		String xmlCmd = "";

		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	

			//add root node
			com.IRC.VIRCType root = doc.VIRC.append();

			//add transaction ID
			root.ID.setValue(GetTransID());

			root.Version.setValue("1.1");

			//add command node
			root.Cmd.append().setValue(CommandType.Conn.toString());

			//Add users node
			UsersType users = root.Users.append();

			//Add user node
			UserClass user = users.User.append();

			//Add nick
			user.Nick.append().setValue(nick);

			//Add IP
			user.IP.append().setValue(IP);

			//Add name
			user.Name.append().setValue(name);

			//Add password
			user.Pass.append().setValue(pass);

			//save XML to String
			xmlCmd = doc.saveToString(false);

			//Write message to server
			TCPThread.write(xmlCmd);
		}
		catch (Exception e) 
		{
			System.out.println("Error attempting to create a conn command.");
		}	
	}

	// Method to create a disconn XML command
	public static void CreateDisconnCommand(String nick)
	{
		String xmlCmd = "";

		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	

			//add root node
			com.IRC.VIRCType root = doc.VIRC.append();

			//add transaction ID
			root.ID.setValue(GetTransID());

			root.Version.setValue("1.1");

			//add command node
			root.Cmd.append().setValue(CommandType.Disconn.toString());

			//Add users node
			UsersType users = root.Users.append();

			//Add user node
			UserClass user = users.User.append();

			//Add nick
			user.Nick.append().setValue(nick);

			//save XML to String
			xmlCmd = doc.saveToString(false);

			//Write message to server
			TCPThread.write(xmlCmd);
		}
		catch (Exception e) 
		{
			System.out.println("Error attempting to create a disconn command.");
		}
	}

	// Method to create a join XML command
	public static void CreateJoinCommand(String channelName, String nick, String desc)
	{
		String xmlCmd = "";

		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	

			//add root node
			com.IRC.VIRCType root = doc.VIRC.append();

			//add transaction ID
			root.ID.setValue(GetTransID());

			root.Version.setValue("1.1");

			//add command node
			root.Cmd.append().setValue(CommandType.Join.toString());

			//Add users node
			UsersType users = root.Users.append();

			//Add user node
			UserClass user = users.User.append();

			//Add nick
			user.Nick.append().setValue(nick);

			// Add Channels node
			ChansType channels = root.Chans.append();

			// Add Channel node
			ChannelClass channel = channels.Chan.append();

			// Add Name
			channel.Name.append().setValue(channelName);

			// Add description if this is a new channel..... if it is not
			//a new channel, i pass in an emtpy string

			if (!desc.equals(""))
				channel.Desc.append().setValue(desc);

			//save XML to String
			xmlCmd = doc.saveToString(false);

			//Write message to server
			TCPThread.write(xmlCmd);
		}
		catch (Exception e) 
		{
			System.out.println("Error attempting to create a join command.");
		}
	}

	// Method to create a part XML command
	public static void CreatePartCommand(String channelName, String nick)
	{
		String xmlCmd = "";

		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	

			//add root node
			com.IRC.VIRCType root = doc.VIRC.append();

			//add transaction ID
			root.ID.setValue(GetTransID());

			root.Version.setValue("1.1");

			//add command node
			root.Cmd.append().setValue(CommandType.Part.toString());

			//Add users node
			UsersType users = root.Users.append();

			//Add user node
			UserClass user = users.User.append();

			//Add nick
			user.Nick.append().setValue(nick);

			// Add Channels node
			ChansType channels = root.Chans.append();

			// Add Channel node
			ChannelClass channel = channels.Chan.append();

			// Add Name
			channel.Name.append().setValue(channelName);

			//save XML to String
			xmlCmd = doc.saveToString(false);

			//Write message to server
			TCPThread.write(xmlCmd);
		}
		catch (Exception e) 
		{
			System.out.println("Error attempting to create a part command.");
		}
	}

	// Method to create a kick XML command
	public static void CreateKickCommand(String channelName, String nick)
	{
		String xmlCmd = "";

		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	

			//add root node
			com.IRC.VIRCType root = doc.VIRC.append();

			//add transaction ID
			root.ID.setValue(GetTransID());

			root.Version.setValue("1.1");

			//add command node
			root.Cmd.append().setValue(CommandType.Kick.toString());

			//Add users node
			UsersType users = root.Users.append();

			//Add user node
			UserClass user = users.User.append();

			//Add nick
			user.Nick.append().setValue(nick);

			// Add Channels node
			ChansType channels = root.Chans.append();

			// Add Channel node
			ChannelClass channel = channels.Chan.append();

			// Add Name
			channel.Name.append().setValue(channelName);

			//save XML to String
			xmlCmd = doc.saveToString(false);

			//Write message to server
			TCPThread.write(xmlCmd);
		}
		catch (Exception e) 
		{
			System.out.println("Error attempting to create a kick command.");
		}
	}

	// Method to create a ban XML command
	public static void CreateBanCommand(String channelName, String nick)
	{
		String xmlCmd = "";

		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	

			//add root node
			com.IRC.VIRCType root = doc.VIRC.append();

			//add transaction ID
			root.ID.setValue(GetTransID());

			root.Version.setValue("1.1");

			//add command node
			root.Cmd.append().setValue(CommandType.Ban.toString());

			//Add users node
			UsersType users = root.Users.append();

			//Add user node
			UserClass user = users.User.append();

			//Add nick
			user.Nick.append().setValue(nick);

			// Add Channels node
			ChansType channels = root.Chans.append();

			// Add Channel node
			ChannelClass channel = channels.Chan.append();

			// Add Name
			channel.Name.append().setValue(channelName);

			//save XML to String
			xmlCmd = doc.saveToString(false);

			//Write message to server
			TCPThread.write(xmlCmd);
		}
		catch (Exception e) 
		{
			System.out.println("Error attempting to create a ban command.");
		}
	}

	// Method to create a mute XML command
	public static void CreateMuteCommand(String channelName, String nick)
	{
		String xmlCmd = "";

		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	

			//add root node
			com.IRC.VIRCType root = doc.VIRC.append();

			//add transaction ID
			root.ID.setValue(GetTransID());

			root.Version.setValue("1.1");

			//add command node
			root.Cmd.append().setValue(CommandType.Mute.toString());

			//Add users node
			UsersType users = root.Users.append();

			//Add user node
			UserClass user = users.User.append();

			//Add nick
			user.Nick.append().setValue(nick);

			// Add Channels node
			ChansType channels = root.Chans.append();

			// Add Channel node
			ChannelClass channel = channels.Chan.append();

			// Add Name
			channel.Name.append().setValue(channelName);

			//save XML to String
			xmlCmd = doc.saveToString(false);

			//Write message to server
			TCPThread.write(xmlCmd);
		}
		catch (Exception e) 
		{
			System.out.println("Error attempting to create a mute command.");
		}
	}

	// Method to create a newdesc XML command
	public static void CreateNewDescCommand(String channelName, String desc)
	{
		String xmlCmd = "";

		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	

			//add root node
			com.IRC.VIRCType root = doc.VIRC.append();

			//add transaction ID
			root.ID.setValue(GetTransID());

			root.Version.setValue("1.1");

			//add command node
			root.Cmd.append().setValue(CommandType.NewDesc.toString());

			// Add Channels node
			ChansType channels = root.Chans.append();

			// Add Channel node
			ChannelClass channel = channels.Chan.append();

			// Add Name
			channel.Name.append().setValue(channelName);

			// Add Desc
			channel.Desc.append().setValue(desc);

			//save XML to String
			xmlCmd = doc.saveToString(false);

			//Write message to server
			TCPThread.write(xmlCmd);
		}
		catch (Exception e) 
		{
			System.out.println("Error attempting to create a newdesc command.");
		}
	}

	// Method to create a getchans XML command
	public static void CreateGetChansCommand()
	{
		String xmlCmd = "";

		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	

			//add root node
			com.IRC.VIRCType root = doc.VIRC.append();

			//add transaction ID
			root.ID.setValue(GetTransID());

			root.Version.setValue("1.1");

			//add command node
			root.Cmd.append().setValue(CommandType.GetChans.toString());

			//save XML to String
			xmlCmd = doc.saveToString(false);

			//Write message to server
			TCPThread.write(xmlCmd);
		}
		catch (Exception e) 
		{
			System.out.println("Error attempting to create a getchans command.");
		}
	}

	// Method to create a getusers XML command
	public static void CreateGetUsersCommand(String channelName)
	{
		String xmlCmd = "";

		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	

			//add root node
			com.IRC.VIRCType root = doc.VIRC.append();

			//add transaction ID
			root.ID.setValue(GetTransID());

			root.Version.setValue("1.1");

			//add command node
			root.Cmd.append().setValue(CommandType.GetUsers.toString());

			// Add Channels node
			ChansType channels = root.Chans.append();

			// Add Channel node
			ChannelClass channel = channels.Chan.append();

			// Add Name
			channel.Name.append().setValue(channelName);

			//save XML to String
			xmlCmd = doc.saveToString(false);

			//Write message to server
			TCPThread.write(xmlCmd);
		}
		catch (Exception e) 
		{
			System.out.println("Error attempting to create a getusers command.");
		}
	}
}