import java.io.IOException;
import java.net.*;

//Class to represent the TCP thread for processing
//control messages
public class TCPThread extends Thread
{
	private String hostAddr = null;		//server address
	private int hostPort = 0;			//server port num
	private static Socket sock = null;	//socket for TCP
	
	
	//Constructor
	public TCPThread(String host, int port)
	{
		super();						//call base constructor
		
		hostAddr = host;				//save host address
		hostPort = port;				//set host port
		
		try 
		{
			//create socket and connect
			sock = new Socket(hostAddr, hostPort);
		} 
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}

	//Entry point for the execution of the thread
	public void run()
	{
		byte[] buffer = new byte[1024];
		
		try
		{
			//loop forever to read from socket
			while(true)
			{
				int readLen = sock.getInputStream().read(buffer, 0, 1024);	//read socket
				String incomingXML = new String(buffer);					//get string
				CmdLib.ParseIncomingMessage(incomingXML);					//parse xml
				sleep(1000);												//sleep
			}
			
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public static void write(String xmlMsg)
	{
		//write data out the socket to the server
		try 
		{
			//write out the socket
			sock.getOutputStream().write(xmlMsg.getBytes(), 0, xmlMsg.getBytes().length);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
