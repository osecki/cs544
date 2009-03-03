import java.io.IOException;
import java.net.*;

// Class to represent the TCP thread for processing control messages
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
			java.util.Random random = new java.util.Random();
			int localPort = random.nextInt(8500)+1000;

			sock = new Socket(hostAddr, hostPort, InetAddress.getLocalHost(), localPort);
		} 
		catch (Exception e)
		{
			System.out.println("Error: TCPThread::TCPThread - " + e.getMessage());
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
				if (sock.isConnected())
				{
				
					int readLen = sock.getInputStream().read(buffer, 0, 1024);	//read socket
					String incomingXML = new String(buffer);					//get string
					incomingXML = incomingXML.substring(0, readLen);
					System.out.println("RECEIVED: " + incomingXML);
					
					CmdLib.ParseIncomingMessage(incomingXML);					//parse xml
				}
				sleep(1000);												//sleep
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Error: TCPThread::run - " + e.getMessage());
		}
	}
	
	public static void write(String xmlMsg)
	{
		//write data out the socket to the server
		try 
		{	
			if (sock.isConnected())
			{
				//write out the socket
				sock.getOutputStream().write(xmlMsg.getBytes(), 0, xmlMsg.getBytes().length);
				sock.getOutputStream().flush();
				System.out.println("SENDING: " + xmlMsg);
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Error: TCPThread::write - " + e.getMessage());
		}
	}
	
	public void Close()
	{
		try 
		{
			this.sock.close();
			this.stop();
		} 
		catch (IOException e) 
		{
		}
		
	}
}
