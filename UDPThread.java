import java.io.IOException;
import java.net.*;

//Class to represent the UDP thread for processing
//control messages
public class UDPThread extends Thread
{
	private String hostAddr = null;				//server address
	private int hostPort = 0;					//server port num
	private static DatagramSocket sock = null;	//socket for UDP
	
	
	//Constructor
	public UDPThread(String host, int port)
	{
		super();						//call base constructor
		
		hostAddr = host;				//save host address
		hostPort = port;				//set host port
		
		try 
		{
			//create socket and connect
			sock = new DatagramSocket(1244);					//hardcode for now
			InetAddress address = InetAddress.getByName(hostAddr);
			sock.connect(address, hostPort);
		} 
		catch (Exception e)
		{
			System.out.println("Error: UDPThread::UDPThread - " + e.getMessage());
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
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				sock.receive(packet);
				String incomingXML = new String(packet.getData());				
				System.out.println("RECEIVED: " + incomingXML);
				sleep(1000);												//sleep
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Error: UDPThread::run - " + e.getMessage());
		}
	}
	
	public static void write()
	{
	}
}
