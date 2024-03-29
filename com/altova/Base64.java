/**
 * Base64.java
 *
 * This file was generated by XMLSpy 2009 Enterprise Edition.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the XMLSpy Documentation for further details.
 * http://www.altova.com/xmlspy
 */
package com.altova;

import java.io.IOException;

public class Base64 {

	private static String    Base64Map =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	private static byte[]    Base64RevMap = new byte[128];
		static {
			for (int i=0; i<Base64RevMap.length; ++i) Base64RevMap[i] = -1;
			for (int i=0; i<64; ++i) Base64RevMap[Base64Map.charAt(i)] = (byte)i;
		}
	
	public static String encode( byte[] bInput) {
		int inLength = bInput.length;
		int outLength = (inLength * 4 + 2) / 3;
		char[] cOutput = new char[((inLength + 2)/3) * 4];
		int inP = 0;
		int outP = 0;
		while( inP < inLength) {
			int in0 = bInput[inP++] & 0xff;
			int in1 = inP < inLength ? bInput[inP++] & 0xff : 0;
			int in2 = inP < inLength ? bInput[inP++] & 0xff : 0;
			int out0 = in0 >>> 2;
			int out1 = ((in0 & 3) << 4) | (in1 >>> 4);
			int out2 = ((in1 & 0xf) << 2) | (in2 >>> 6);
			int out3 = in2 & 0x3f;
			cOutput[ outP++] = Base64Map.charAt(out0);
			cOutput[ outP++] = Base64Map.charAt(out1);
			cOutput[ outP] = outP < outLength ? Base64Map.charAt(out2) : '=';
			outP++;
			cOutput[ outP] = outP < outLength ? Base64Map.charAt(out3) : '=';
			outP++;
		}
		return new String(cOutput);
	}
	
	public static byte[] decode( String sInput) throws IOException {
		if( sInput.length() % 4 != 0)
			throw new IOException( "Length of Base64 String illegal.");
		char[] cInput = sInput.toCharArray();
		int inLength = cInput.length;
		while( inLength > 0 && cInput[inLength - 1] == '=') inLength--;
		int outLength = inLength * 3 / 4;
		byte[] bOutput = new byte[outLength];
		
		int inP = 0;
		int outP = 0;
		while ( inP < inLength) {
			int[] in = new int[4];
			in[0] = cInput[inP++];
			in[1] = cInput[inP++];
			in[2] = inP < inLength ? cInput[inP++] : 'A';
			in[3] = inP < inLength ? cInput[inP++] : 'A';
			isValidChar( in, true);
			
			int[] by = new int[4];
			for( int i = 0; i < by.length; ++i) by[i] = Base64RevMap[in[i]];
			isValidChar( by, false);
			
			int[] out = new int[3];
			out[0] = ( by[0] << 2) | (by[1] >>> 4);
			out[1] = ((by[1] & 0xf) << 4) | (by[2] >>> 2);
			out[2] = ((by[2] & 3) << 6) | by[3];
			bOutput[outP++] = (byte)out[0];
			if( outP < outLength) bOutput[outP++] = (byte)out[1];
			if( outP < outLength) bOutput[outP++] = (byte)out[2];
		}
		
		return bOutput;
	}
	
	public static void isValidChar( int[] chars, boolean checkContent) throws IOException
	{
		for( int i = 0; i < chars.length; ++i) {
			if( chars[i] > 127 || (checkContent && Base64Map.indexOf( chars[i]) == -1))
				throw new IOException( "Illegal character: '" + (char)chars[i] + "'");
		}
	}
}
