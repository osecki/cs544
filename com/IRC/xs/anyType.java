////////////////////////////////////////////////////////////////////////
//
// anyType.java
//
// This file was generated by XMLSpy 2009 Enterprise Edition.
//
// YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
// OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
//
// Refer to the XMLSpy Documentation for further details.
// http://www.altova.com/xmlspy
//
////////////////////////////////////////////////////////////////////////

package com.IRC.xs;


public class anyType extends com.altova.xml.TypeBase
{
		public static com.altova.xml.meta.ComplexType getStaticInfo() { return new com.altova.xml.meta.ComplexType(com.IRC.IRC_TypeInfo.binder.getTypes()[com.IRC.IRC_TypeInfo._altova_ti_xs_altova_anyType]); }
	
	public anyType(org.w3c.dom.Node init)
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
		com.altova.typeinfo.MemberInfo member = com.IRC.IRC_TypeInfo.binder.getMembers()[com.IRC.IRC_TypeInfo._altova_mi_xs_altova_anyType._unnamed];
		return (String)com.altova.xml.XmlTreeOperations.castToString(getNode(), member);
	}
	
	public void setValue(String value)
	{
		com.altova.typeinfo.MemberInfo member = com.IRC.IRC_TypeInfo.binder.getMembers()[com.IRC.IRC_TypeInfo._altova_mi_xs_altova_anyType._unnamed];
		com.altova.xml.XmlTreeOperations.setValue(getNode(), member, value);
	}
	


	// Elements

		public void setXsiType() {com.altova.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.w3.org/2001/XMLSchema", "anyType");}
}
