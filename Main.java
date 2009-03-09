/*
 *  File:  Main.java
 *  Modified:  March 11, 2009
 *  Description:  This is the driver file.
 *  Copyright (C) 2009 - File created for CS544 by Bill Shaya, Jordan Osecki, and Robert Cochran.
 */

public class Main 
{
	static MainForm temp = new MainForm();
	
	/**
	 * @return the temp
	 */
	public static MainForm getTemp() {
		return temp;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) 
	{		
		temp.show();			//show GUI
	}
}
