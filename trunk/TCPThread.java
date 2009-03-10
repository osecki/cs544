/*
 *  File:  TCPThread.java
 *  Modified:  March 11, 2009
 *  Description:  This file controls the TCP stream.
 *  Copyright (C) 2009 - File created for CS544 by Bill Shaya, Jordan Osecki, and Robert Cochran.
 */

// Sources:  Some of this code was based off of the socket code examples in class. if you have any
//   questions, don't hesitate to ask.

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
		super();						// call base constructor

		hostAddr = host;				// save host address
		hostPort = port;				// set host port

		try 
		{
			// Create socket and connect
			java.util.Random random = new java.util.Random();
			int localPort = random.nextInt(8500) + 1000;

			sock = new Socket(hostAddr, hostPort, InetAddress.getLocalHost(), localPort);
		} 
		catch (Exception e)
		{
			System.out.println("Error: TCPThread::TCPThread - " + e.getMessage());
		}
	}

	// Entry point for the execution of the thread
	public void run()
	{
		byte[] buffer = new byte[1024];

		try
		{
			// Loop forever to read from socket
			while (true)
			{
				if (sock.isConnected())
				{
					int readLen = sock.getInputStream().read(buffer, 0, 1024);	//read socket

					//System.out.println("readLen = " + readLen);

					if ( readLen != -1 )
					{
						String incomingXML = new String(buffer);					//get string
						incomingXML = incomingXML.substring(0, readLen);
						System.out.println("RECEIVED: " + incomingXML);

						CmdLib.ParseIncomingMessage(incomingXML);					//parse xml
					}
				}
				else
				{
					System.out.println("Inside run the socket is no longer connected. Crashed? Kicked? Banned?");
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
		// Write data out the socket to the server
		try 
		{	
			if (sock.isConnected())
			{
				//write out the socket
				sock.getOutputStream().write(xmlMsg.getBytes(), 0, xmlMsg.getBytes().length);
				sock.getOutputStream().flush();
				System.out.println("-");
				System.out.println("SENDING: " + xmlMsg);
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Error: TCPThread::write - " + e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	public void Close()
	{
		try 
		{
			if (TCPThread.sock != null)
				TCPThread.sock.close();

			this.stop();
		} 
		catch (IOException e) 
		{
			System.out.println("Error: TCPThread::close - " + e.getMessage());
		}		
	}
}