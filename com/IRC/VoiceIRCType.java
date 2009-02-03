////////////////////////////////////////////////////////////////////////
//
// VoiceIRCType.java
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


public class VoiceIRCType extends com.altova.xml.TypeBase
{
		public static com.altova.xml.meta.ComplexType getStaticInfo() { return new com.altova.xml.meta.ComplexType(com.IRC.IRC_TypeInfo.binder.getTypes()[com.IRC.IRC_TypeInfo._altova_ti_altova_VoiceIRCType]); }
	
	public VoiceIRCType(org.w3c.dom.Node init)
	{
		super(init);
		instantiateMembers();
	}
	
	private void instantiateMembers()
	{

		CommandName= new MemberElement_CommandName (this, com.IRC.IRC_TypeInfo.binder.getMembers()[com.IRC.IRC_TypeInfo._altova_mi_altova_VoiceIRCType._CommandName]);
		Channel= new MemberElement_Channel (this, com.IRC.IRC_TypeInfo.binder.getMembers()[com.IRC.IRC_TypeInfo._altova_mi_altova_VoiceIRCType._Channel]);
		User= new MemberElement_User (this, com.IRC.IRC_TypeInfo.binder.getMembers()[com.IRC.IRC_TypeInfo._altova_mi_altova_VoiceIRCType._User]);
	}
	// Attributes


	// Elements
	
	public MemberElement_CommandName CommandName;

		public static class MemberElement_CommandName
		{
			public static class MemberElement_CommandName_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_CommandName member;
				public MemberElement_CommandName_Iterator(MemberElement_CommandName member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					CommandNameType nx = new CommandNameType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_CommandName (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public CommandNameType at(int index) {return new CommandNameType(owner.getElementAt(info, index));}
			public CommandNameType first() {return new CommandNameType(owner.getElementFirst(info));}
			public CommandNameType last(){return new CommandNameType(owner.getElementLast(info));}
			public CommandNameType append(){return new CommandNameType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_CommandName_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_Channel Channel;

		public static class MemberElement_Channel
		{
			public static class MemberElement_Channel_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_Channel member;
				public MemberElement_Channel_Iterator(MemberElement_Channel member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					ChannelClass nx = new ChannelClass(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_Channel (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public ChannelClass at(int index) {return new ChannelClass(owner.getElementAt(info, index));}
			public ChannelClass first() {return new ChannelClass(owner.getElementFirst(info));}
			public ChannelClass last(){return new ChannelClass(owner.getElementLast(info));}
			public ChannelClass append(){return new ChannelClass(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_Channel_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_User User;

		public static class MemberElement_User
		{
			public static class MemberElement_User_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_User member;
				public MemberElement_User_Iterator(MemberElement_User member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					UserClass nx = new UserClass(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_User (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public UserClass at(int index) {return new UserClass(owner.getElementAt(info, index));}
			public UserClass first() {return new UserClass(owner.getElementFirst(info));}
			public UserClass last(){return new UserClass(owner.getElementLast(info));}
			public UserClass append(){return new UserClass(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_User_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
}
