
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
		
		String xml;
		xml = CmdLib.CreateConnCommand("bill", "192.168.1.1", "bill shaya", "abcd");
		
        TCPThread tcpThread = new TCPThread("64.233.161.99", 80);
        tcpThread.start();		
		
		new MainForm().show();			//show GUI
	}

}
