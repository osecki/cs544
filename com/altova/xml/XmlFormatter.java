// XmlFormatter.java
// This file contains generated code and will be overwritten when you rerun code generation.

package com.altova.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Document;

import com.altova.CoreTypes;
import com.altova.types.*;
import com.altova.typeinfo.ValueFormatter;

import java.math.BigInteger;
import java.math.BigDecimal;

public class XmlFormatter implements ValueFormatter
{
	public String format(DateTime v)
	{
		return CoreTypes.castToString(v);
	}
	
	public String format(Duration v)
	{
		return CoreTypes.castToString(v);
	}
	
	public String format(long v)
	{
		return CoreTypes.castToString(v);
	}
	
	public String format(double v)
	{
		return CoreTypes.castToString(v);
	}
	
	public String format(BigInteger v)
	{
		return CoreTypes.castToString(v);
	}
	
	public String format(BigDecimal v)
	{
		return CoreTypes.castToString(v);
	}
	
	public String format(String v)
	{
		return CoreTypes.castToString(v);
	}
	
	public String format(byte[] v)
	{
		if( v == null ) return "";
		String sResult = new sun.misc.BASE64Encoder().encode(v);
		return sResult.replaceAll( "\r", "" );
	}
	
	public String format(boolean v)
	{
		return CoreTypes.castToString(v);
	}
	
	public byte[] parseBinary(String v)
	{
		if( v == null ) return null;
		String newvalue = v.replaceAll("\\s{2,}", "").trim();	// collapse whitespace
		if( newvalue.length() == 0 ) return new byte[0];
		try {
			return new sun.misc.BASE64Decoder().decodeBuffer(newvalue);
		}
		catch ( java.io.IOException e ) {
			return null;
		}
	}
}
