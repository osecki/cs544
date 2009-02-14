import com.altova.*;
import com.altova.types.*;
import com.IRC.*;

public class CmdLib 
{
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
	
	//Method to create a conn XML command
	public static String CreateConnCommand(String nick, String IP, String name)
	{
		String xmlCmd = "";
		
		try
		{
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();	
			
			//add root node
			com.IRC.VoiceIRCType root = doc.VoiceIRC.append();
			
			//add command node
			root.CommandName.append().setValue(CommandType.Conn.toString());
			
			//Add users node
		}
		catch (Exception e) 
		{
		}	
		
		return xmlCmd;
	}
	
	//Method to create a disconn XML command
	public static String CreateDisconnCommand(String nick)
	{
		return "";
	}
	
	public static String CreateJoinCommand(String channelName, String nick)
	{
		return "";
	}
	
	public static String CreatePartCommand(String channelName, String nick)
	{
		return "";
	}
	
	public static String CreateKickCommand(String channelName, String nick)
	{
		return "";
	}
	
	public static String CreateBanCommand(String channelName, String nick)
	{
		return "";
	}
	
	public static String CreateMuteCommand(String channelName, String nick)
	{
		return "";
	}
	
	public static String CreateNewDescCommand(String channelName, String desc)
	{
		return "";
	}
	
	public static String CreateGetChansCommand()
	{
		return "";
	}
	
	public static String CreateGetUsersCommand(String channelName)
	{
		return "";
	}
	
	
	/*	EXAMPLE CODE I'LL LEAVE FOR NOW
	public static String CreateCommand()
	{	
		try
		{
			
			//create document
			com.IRC.IRC2 doc = com.IRC.IRC2.createDocument();
			
			//add root node
			com.IRC.VoiceIRCType root = doc.VoiceIRC.append();
			
			//add command node
			root.CommandName.append().setValue("JoinChannel");
			
			//add channel node
			ChannelClass channelClass = root.Channel.append();
			
			//add channel name to channel node
			channelClass.ChannelName.append().setValue("Software Development");
			
			//add user node
			UserClass userClass = root.User.append();
			
			//add user name to user node
			userClass.UserName.append().setValue("Bill");
			
			//save XML to String
			String xmlString = doc.saveToString(false);
			
	//**************************************************************
			
			//load from string
			com.IRC.IRC2 _doc = com.IRC.IRC2.loadFromString(xmlString);
			
			//check for VoiceIRC root node
			if (doc.VoiceIRC.exists())
			{
				//Grab the node
				VoiceIRCType _root = doc.VoiceIRC.first();
				
				//Check for Channel node
				if (_root.Channel.exists())
				{
					//Grab it
					ChannelClass _channelClass =_root.Channel.first();
					
					//Check for ChannelName node
					if (_channelClass.ChannelName.exists())
					{
						//Grab the value of ChannelName node
						String channelName = _channelClass.ChannelName.first().getValue();
					}
				}
				
				//Check for User node
				if (_root.User.exists())
				{
					//Grab the node
					UserClass _userClass = _root.User.first();
					
					//Check for user name node
					if (_userClass.UserName.exists())
					{
						//Grab the value
						String userName = _userClass.UserName.first().getValue();
					}
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}			
		//   
		//   ...
		//   doc.saveToFile("IRC1.xml", true);
		//
		// Example code to load and save a structure:
		//   com.IRC.IRC2 doc = com.IRC.IRC2.loadFromFile("IRC1.xml");
		//   com.IRC.VoiceIRCType root = doc.VoiceIRC.first();
		//   ...
		//   doc.saveToFile("IRC1.xml", true);		
		
		return "";
	}
	*/
}
