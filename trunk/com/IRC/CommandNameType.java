////////////////////////////////////////////////////////////////////////
//
// CommandNameType.java
//
// This file was generated by XMLSpy 2008r2sp2 Enterprise Edition.
//
// YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
// OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
//
// Refer to the XMLSpy Documentation for further details.
// http://www.altova.com/xmlspy
//
////////////////////////////////////////////////////////////////////////

package com.IRC;


public class CommandNameType extends com.altova.xml.TypeBase
{
		public static com.altova.xml.meta.ComplexType getStaticInfo() { return new com.altova.xml.meta.ComplexType(com.IRC.IRC_TypeInfo.binder.getTypes()[com.IRC.IRC_TypeInfo._altova_ti_altova_CommandNameType]); }
	
	public CommandNameType(org.w3c.dom.Node init)
	{
		super(init);
		instantiateMembers();
	}
	
	private void instantiateMembers()
	{

	}
	// Attributes
	public String getValue() 
	{ 
		com.altova.typeinfo.MemberInfo member = com.IRC.IRC_TypeInfo.binder.getMembers()[com.IRC.IRC_TypeInfo._altova_mi_altova_CommandNameType._unnamed];
		return (String)com.altova.xml.XmlTreeOperations.castToString(getNode(), member);
	}
	
	public void setValue(String value)
	{
		com.altova.typeinfo.MemberInfo member = com.IRC.IRC_TypeInfo.binder.getMembers()[com.IRC.IRC_TypeInfo._altova_mi_altova_CommandNameType._unnamed];
		com.altova.xml.XmlTreeOperations.setValue(getNode(), member, value);
	}
	


	// Elements
}
