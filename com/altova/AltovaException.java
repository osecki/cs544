/**
 * AltovaException.java
 *
 * This file was generated by XMLSpy 2008r2sp2 Enterprise Edition.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the XMLSpy Documentation for further details.
 * http://www.altova.com/xmlspy
 */


package com.altova;


public class AltovaException extends RuntimeException {
	protected Exception	innerException;
	protected String	message;

	public AltovaException(String text) {
		innerException = null;
		message = text;
	}

	public AltovaException(Exception other) {
		innerException = other;
		message = other.getMessage();
	}

	public String getMessage() {
		return message;
	}

	public Exception getInnerException() {
		return innerException;
	}
}
