
public class Main 
{

	public static void main(String[] args) 
	{

		//debug to test thread creation
		//will need to do this on a gui action such
		//as the connect command.  Connect should
		//spawn the threads and start processing
		//so for now, lets try to connect to Google
		//TCPThread tcpThread = new TCPThread("64.233.161.99", 80);										//create TCP thread
		//tcpThread.run();
		
        TCPThread tcpThread = new TCPThread("64.233.161.99", 80);
        tcpThread.start();		
		
		new MainForm().show();			//show GUI
	}

}
