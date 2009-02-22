import java.io.IOException;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

// Class to represent the UDP thread for processing control messages
public class UDPThread extends Thread implements ActionListener
{
	private String hostAddr = null;				//server address
	private int hostPort = 0;					//server port num
	private static DatagramSocket sock = null;	//socket for UDP
	private Timer timer;			//timer to read UDP socket
	
	//create a timer every x milliseconds
	//need a buffer.  
	
	//Constructor
	public UDPThread(String host, int port)
	{
		super();						//call base constructor
		
		hostAddr = host;				//save host address
		hostPort = port;				//set host port
		timer = new Timer(500, this);
		
		try 
		{	
			//create socket and connect
			sock = new DatagramSocket(1244);					//hardcode for now
			InetAddress address = InetAddress.getByName(hostAddr);
			sock.connect(address, 1501);	
		} 
		catch (Exception e)
		{
			System.out.println("Error: UDPThread::UDPThread - " + e.getMessage());
		}
	}

	//Entry point for the execution of the thread
	public void run()
	{
		timer.start();											//start the timer
	}
	
	public static void write()
	{
	}

	//Event handle for timer elapse action
	public void actionPerformed(ActionEvent event) 
	{
		if (event.getSource() == timer)		//if timer elapsed
		{
			//Debug that the timer works, and it does!
			//Below is some sample code to read datagrams from the UDP socket
			
			System.out.println("TIMER TICK");
/*			
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
*/					
		}
	}
}
