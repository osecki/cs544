import java.util.Vector;

/*
 *  File:  ResponseMessage.java
 *  Modified:  March 11, 2009
 *  Description:  This file stores all information parsed out of response messages from the server
 *  Copyright (C) 2009 - File created for CS544 by Bill Shaya, Jordan Osecki, and Robert Cochran.
 */

public class ResponseMessage 
{
	Long id;
	String command;
	Vector <String> channelNames;
	Vector <String> channelDescs;
	Vector <String> channelModes;
	Vector <String> userNicks;
	Long statusCode;
	String statusMsg;
	
	/**
	 * Constructor
	 */
	public ResponseMessage (Long i, String c, Vector <String> cN, Vector <String> cD, Vector <String> cM, Vector <String> uN, Long sC, String sM)
	{
		id = i;
		command = c;
		channelNames = cN;
		channelDescs = cD;
		channelModes = cM;
		userNicks = uN;
		statusCode = sC;
		statusMsg = sM;
	}
	
	/**
	 * Empty Constructor
	 */
	public ResponseMessage ()
	{
		
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	/**
	 * @return the channelNames
	 */
	public Vector<String> getChannelNames() {
		return channelNames;
	}
	/**
	 * @param channelNames the channelNames to set
	 */
	public void setChannelNames(Vector<String> channelNames) {
		this.channelNames = channelNames;
	}
	/**
	 * @return the channelDescs
	 */
	public Vector<String> getChannelDescs() {
		return channelDescs;
	}
	/**
	 * @param channelDescs the channelDescs to set
	 */
	public void setChannelDescs(Vector<String> channelDescs) {
		this.channelDescs = channelDescs;
	}
	/**
	 * @return the channelModes
	 */
	public Vector<String> getChannelModes() {
		return channelModes;
	}
	/**
	 * @param channelModes the channelModes to set
	 */
	public void setChannelModes(Vector<String> channelModes) {
		this.channelModes = channelModes;
	}
	/**
	 * @return the userNicks
	 */
	public Vector<String> getUserNicks() {
		return userNicks;
	}
	/**
	 * @param userNicks the userNicks to set
	 */
	public void setUserNicks(Vector<String> userNicks) {
		this.userNicks = userNicks;
	}
	/**
	 * @return the statusCode
	 */
	public Long getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the statusMsg
	 */
	public String getStatusMsg() {
		return statusMsg;
	}
	/**
	 * @param statusMsg the statusMsg to set
	 */
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
}
