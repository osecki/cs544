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
		try
		{
			//loop forever to read from socket
			while(true)
			{
				System.out.println("READ");
				sleep(1000);
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
			sock.getOutputStream().write(xmlMsg.getBytes(), 0, xmlMsg.getBytes().length);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
