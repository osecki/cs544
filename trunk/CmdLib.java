import com.altova.*;
import com.altova.types.*;
import com.IRC.*;

public class CmdLib 
{
	public static int transID;
	
	public enum CommandType
	{
		Conn,
		Disconn,
		Join,
		Part,
		Kick,
		Ban,
		Mute,
		NewDesc,
		GetChans,
		GetUsers
	}
	
	public static int GetTransID()
	{
		return transID++;
	}
	
	public static void ParseIncomingMessage(String xmlMsg)
	{
		//We'll parse out the XML message and store the settings in objects
		//and then let the GUI use them to update itself.
		//We'll need to define the classes but for now, just create some local
		//variables
		
		
		try 
		{
			//load from string
			com.IRC.IRC2 doc = com.IRC.IRC2.loadFromString(xmlMsg);
			
			//check for VIRC root node
			if (doc.VIRC.exists())
			{
				//Grab the node
				VIRCType root = doc.VIRC.first();
				
				//check for transaction ID
				if (root.ID.exists())
				{
					Long ID = root.ID.getValue();
				}
				
				//check for command node
				if (root.Cmd.exists())
				{
					String command = root.Cmd.first().getValue();
				}
				
				//check for Status node
				if (root.Stat.exists())
				{
					//Grab the node
					StatType stat = root.Stat.first();
					
					//check for status code
					if (stat.Code.exists())
					{
						Long statusCode = stat.Code.first().getValue();
					}
					
					//check for status message
					if (stat.Msg.exists())
					{
						String statusMsg = stat.Msg.first().getValue();
					}
				}
			}
		} 
		catch (Exception e) 
		{
		}	
		
		
		//if valid message, send to the GUI to do whatever it needs
		//need to populate objects and trigger the GUI to do something for updating
		MainForm.UpdateGUI();
	}
	
	//Method to create a conn XML command
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
		}	
	}
	
	//Method to create a disconn XML command
	public static void CreateDisconnCommand(String nick)
	{
	}
	
	public static void CreateJoinCommand(String channelName, String nick)
	{
	}
	
	public static void CreatePartCommand(String channelName, String nick)
	{
	}
	
	public static void CreateKickCommand(String channelName, String nick)
	{
	}
	
	public static void CreateBanCommand(String channelName, String nick)
	{
	}
	
	public static void CreateMuteCommand(String channelName, String nick)
	{
	}
	
	public static void CreateNewDescCommand(String channelName, String desc)
	{
	}
	
	public static void CreateGetChansCommand()
	{
	}
	
	public static void CreateGetUsersCommand(String channelName)
	{
	}
}
