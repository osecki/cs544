// XmlTreeOperations.java
// This file contains generated code and will be overwritten when you rerun code generation.

package com.altova.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.math.*;
import java.util.*;

import com.altova.typeinfo.MemberInfo;
import com.altova.AltovaException;

public class XmlTreeOperations
{
	private static int staticNSCount = 0;
		
	private static XmlFormatter getFormatter(MemberInfo member)
	{
		if (member.getDataType().getFormatter() != null)
			return (XmlFormatter) member.getDataType().getFormatter();
		else
			return (XmlFormatter) Xs.AnySimpleTypeFormatter;
	}
	
	public static void copyAll(Node src, Node tgt)
	{
		// elements
		for(Node node = src.getFirstChild(); node != null; node = node.getNextSibling())
			tgt.appendChild(tgt.getOwnerDocument().importNode(node, true));
		// attrs
		NamedNodeMap attrz = src.getAttributes();
		for (int i=0; i<attrz.getLength(); i++)
			((Element)tgt).setAttributeNodeNS((Attr)tgt.getOwnerDocument().importNode(attrz.item(i), true));
	}

	
	public static class AllIterator
	{
		private Node currentNode;

		public AllIterator(Node node)
		{
			currentNode = node;
		}

		public boolean exists()
		{
			return currentNode != null;
		}

		public Node getCurrent()
		{
			if (!exists())
				throw new AltovaException("AllIterator::current(): is null");
			return currentNode;
		}

		public boolean next()
		{
			currentNode = getCurrent().getNextSibling();
			return currentNode != null;
		}

		public boolean previous()
		{
			currentNode = getCurrent().getPreviousSibling();
			return currentNode != null;
		}
	}
	
	public static class MemberIterator
	{
		private AllIterator iterator;
		private MemberInfo memberInfo;

		public MemberIterator(Node node, MemberInfo info)
		{
			iterator = new AllIterator(node);
			memberInfo = info;

			while (iterator.exists() && !isMember(iterator.getCurrent(), memberInfo))
				iterator.next();
		}

		public boolean exists()
		{
			return iterator.exists();
		}

		public Node getCurrent()
		{
			if (!exists())
				throw new AltovaException("MemberIterator::current(): is null");
			return iterator.getCurrent();
		}

		public boolean next()
		{
			while (iterator.next())
			{
				if (isMember(iterator.getCurrent(), memberInfo))
					return true;
			}
			return false;
		}

		public boolean previous()
		{
			if (!exists())
				throw new AltovaException("MemberIterator::current(): is null");
			while (iterator.previous())
			{
				if (isMember(iterator.getCurrent(), memberInfo))
					return true;
			}
			return false;
		}
	}

	public static boolean isMember(Node node, MemberInfo member)
	{
		if (member.getLocalName() == null)
			return (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE);
		
		if (node.getNodeType() != Node.ELEMENT_NODE)
			return false;
		
		return memberEqualsNode(member, node);
	}
	
	public static boolean exists(Node node)
	{
		return node != null; //?
	}

	public static AllIterator getElements(Node node)
	{
		return new AllIterator(node.getFirstChild());
	}

	public static MemberIterator getElements(Node node, MemberInfo member)
	{
		return new MemberIterator(node.getFirstChild(), member);
	}

	public static String getTextContent(Node node)
	{
		short nodeType = node.getNodeType();
		if( nodeType == Node.ATTRIBUTE_NODE ||
			nodeType == Node.PROCESSING_INSTRUCTION_NODE ||
			nodeType == Node.COMMENT_NODE ||
			nodeType == Node.TEXT_NODE ||
			nodeType == Node.CDATA_SECTION_NODE )
		{
			return com.altova.CoreTypes.castToString(node.getNodeValue());
		}
		else
		if( nodeType == Node.DOCUMENT_NODE ||
			nodeType == Node.DOCUMENT_FRAGMENT_NODE ||
			nodeType == Node.ELEMENT_NODE ||
			nodeType == Node.ENTITY_REFERENCE_NODE )
		{
			String result = "";
			Node child = node.getFirstChild();
			for( ; child != null ; child = child.getNextSibling() )
			{
				short childNodeType = child.getNodeType();
				if( childNodeType == Node.TEXT_NODE ||
					childNodeType == Node.CDATA_SECTION_NODE ||
					childNodeType == Node.ELEMENT_NODE ||
					childNodeType == Node.ENTITY_REFERENCE_NODE )
				{
					result += getTextContent( child );
				}
			}
			return result;
		}
		else
			return "";
	}
	
	public static void setTextValue(Node node, String value)
	{
		//node.setNodeValue(value);
		while( node.getFirstChild() != null )
			node.removeChild( node.getFirstChild() );

		value = value.replaceAll("\r\n", "\n");
		node.appendChild( node.getOwnerDocument().createTextNode(value) );
	}

	public static String getTextValue(Node node)
	{
		return getTextContent( node );
	}
	
	public static void findDeclarationsForNamespace(Node node, String uri, ArrayList declarations)
	{
		if( node.getParentNode() != null )
			findDeclarationsForNamespace(node.getParentNode(), uri, declarations);

		NamedNodeMap attrs = node.getAttributes();
		if( attrs == null )
			return;

		int length = attrs.getLength();
		for( int index = 0 ; index < length ; ++index )
		{
			Node att = attrs.item( index );
			if( att.getPrefix() != null )
				if( att.getPrefix().equals("xmlns") )
				{
					if( att.getNodeValue().equals(uri) )
					{
						declarations.add(att);
					}
					else
					{
						for( int i = 0 ; i < declarations.size() ; ++i )
						{
							Node cur = (Node) declarations.get(i);
							
							if( cur.getLocalName().equals(att.getLocalName()) )
								declarations.remove(i--);
						}
					}
				}
		}
	}

	public static void setValue(Node node, MemberInfo member, String value)
	{
		if( value == null )
			return;
		Element element = (Element) node;
		if (member.getLocalName() != null)
		{
			String prefix = "";
			if( member.getNamespaceURI() != null )
			{
				ArrayList declarations = new ArrayList();
				findDeclarationsForNamespace(node, member.getNamespaceURI(), declarations);
				if( !declarations.isEmpty() )
					prefix = ((Node) declarations.get(0)).getLocalName() + ":";
			}

			Attr attr = element.getOwnerDocument().createAttributeNS(member.getNamespaceURI(), prefix + member.getLocalName());
			attr.setNodeValue(value);
			node.getAttributes().setNamedItemNS( attr );
		}
		else
			setTextValue(node, value);
	}
	
	public static void setValue(Node node, MemberInfo member, boolean b)
	{
		setValue(node, member, getFormatter(member).format(b));
	}
	
	public static void setValue(Node node, MemberInfo member, int b)
	{
		setValue(node, member, getFormatter(member).format(b));
	}

	public static void setValue(Node node, MemberInfo member, long b)
	{
		setValue(node, member, getFormatter(member).format(b));
	}

	public static void setValue(Node node, MemberInfo member, java.math.BigDecimal b) 
	{
		setValue(node, member, getFormatter(member).format(b));
	}

	public static void setValue(Node node, MemberInfo member, java.math.BigInteger b)
	{
		setValue(node, member, getFormatter(member).format(b));
	}

	public static void setValue(Node node, MemberInfo member, double b)
	{
		setValue(node, member, getFormatter(member).format(b));
	}

	public static void setValue(Node node, MemberInfo member, com.altova.types.DateTime b)
	{
		setValue(node, member, getFormatter(member).format(b));
	}

	public static void setValue(Node node, MemberInfo member, com.altova.types.Duration b)
	{
		setValue(node, member, getFormatter(member).format(b));
	}
	
	public static void setValue(Node node, MemberInfo member, byte[] bytearr)
	{
		setValue(node, member, getFormatter(member).format(bytearr));
	}
	
	public static void setValue(Node node, MemberInfo member, javax.xml.namespace.QName qname)
	{
		if (qname.getNamespaceURI().equals(javax.xml.XMLConstants.NULL_NS_URI))
		{
			setValue(node, member, qname.getLocalPart());
			return;
		}
		
		String prefix = node.lookupPrefix(qname.getNamespaceURI());
		if (prefix == null)
		{
			prefix = findUnusedPrefix(node, qname.getPrefix());
			((Element)node).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, qname.getNamespaceURI());
		}
		setValue(node, member, prefix + ":" + qname.getLocalPart());	
	}
	
	public static Node addElement(Node node, MemberInfo member)
	{
		String prefix = "";
		if( member.getNamespaceURI() != null )
		{
			ArrayList declarations = new ArrayList();
			findDeclarationsForNamespace(node, member.getNamespaceURI(), declarations);
			if( !declarations.isEmpty() )
				prefix = ((Node) declarations.get(0)).getLocalName() + ":";
		}

		Document doc = node.getOwnerDocument();
		if (doc == null)
			doc = (Document) node;
		Node newNode = doc.createElementNS(member.getNamespaceURI(), prefix + member.getLocalName());
		node.appendChild(newNode);
		return newNode;
	}
	
	public static double castToDouble(Node node, MemberInfo member)
	{
		return com.altova.CoreTypes.castToDouble( getTextContent(node) );
	}

	public static String castToString(Node node, MemberInfo member)
	{
		return com.altova.CoreTypes.castToString( getTextContent(node) );
	}

	public static long castToLong(Node node, MemberInfo member)
	{
		return com.altova.CoreTypes.castToLong( getTextContent(node) );
	}

	public static int castToInt(Node node, MemberInfo member)
	{
		return com.altova.CoreTypes.castToInt( getTextContent(node) );
	}

	public static BigInteger castToBigInteger(Node node, MemberInfo member)
	{
		return com.altova.CoreTypes.castToBigInteger( getTextContent(node) );
	}

	public static BigDecimal castToBigDecimal(Node node, MemberInfo member)
	{
		return com.altova.CoreTypes.castToBigDecimal( getTextContent(node) );
	}

	public static boolean castToBool(Node node, MemberInfo member)
	{
		return com.altova.CoreTypes.castToBool( getTextContent(node) );
	}

	public static com.altova.types.DateTime castToDateTime(Node node, MemberInfo member)
	{
		return com.altova.CoreTypes.castToDateTime( getTextContent(node) );
	}

	public static com.altova.types.Duration castToDuration(Node node, MemberInfo member)
	{
		return com.altova.CoreTypes.castToDuration( getTextContent(node) );
	}
	
	public static byte[] castToBinary(Node node, MemberInfo member)
	{
		return getFormatter(member).parseBinary( getTextContent(node) );
	}

	public static javax.xml.namespace.QName castToQName(Node node, MemberInfo member)
	{
		String value = getTextValue(node);
		int i = value.indexOf(':');
		if (i == -1)
			return com.altova.CoreTypes.castToQName(value);
		
		String prefix = value.substring(0, i);
		String local = value.substring(i+1);
		
		String uri = node.lookupNamespaceURI(prefix);
		
		return new javax.xml.namespace.QName(uri, local, prefix);
	}
	
	public static Node findAttribute( Node node,  MemberInfo member )
	{
		Element el = (Element) node;
		return el.getAttributeNodeNS( member.getNamespaceURI(), member.getLocalName() );
	}

	public static Document loadDocument(com.altova.io.Input input) throws Exception
	{
		if (input instanceof com.altova.io.FileInput)
		{
			return loadDocument(((com.altova.io.FileInput)input).getFilename());
		}
		switch (input.getType())
		{
			case com.altova.io.Input.IO_STREAM:
				return loadDocument(new org.xml.sax.InputSource(input.getStream()));
			
			case com.altova.io.Input.IO_READER:
				return loadDocument(new org.xml.sax.InputSource(input.getReader()));
			
			case com.altova.io.Input.IO_DOM:
				return input.getDocument();
			
			default:
				throw new Exception ("Unknown input type");
		}
	}
	
	public static Document loadDocument(org.xml.sax.InputSource source ) throws Exception
	{
		// turn true to validate onload
		boolean validation = false;
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setNamespaceAware(true);
		if (validation) 
		{
			factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
			factory.setValidating(true);
		}
		
		javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
		
		try
		{
			Document doc = builder.parse(source);
			return doc;
		}
		catch (Exception e)
		{
			throw new com.altova.SourceInstanceUnavailableException("cannot parse input stream.", e);
		}
	}
	
	public static Document loadDocument( java.io.InputStream stream ) throws Exception
	{
		return loadDocument(new org.xml.sax.InputSource(stream));
	}
	
	public static Document loadDocument( String filename ) throws Exception
	{
		// turn true to validate onload
		boolean validation = false;
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setNamespaceAware(true);
		if (validation) 
		{
			factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
			factory.setValidating(true);
		}
		javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
		
		try
		{
			Document doc = builder.parse( new File(filename));
			return doc;
		}
		catch (Exception e)
		{
			throw new com.altova.SourceInstanceUnavailableException("cannot parse input file.", e);
		}
	}


	public static Document loadXmlBinary( byte[] xmlTree ) throws Exception
	{
		// turn true to validate onload
		boolean validation = false;
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setNamespaceAware(true);
		if (validation) 
		{
			factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
			factory.setValidating(true);
		}

		javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
		
		try
		{
			Document doc = builder.parse(new java.io.ByteArrayInputStream(xmlTree));
			return doc;
		}
		catch (Exception e)
		{
			throw new com.altova.SourceInstanceUnavailableException("Cannot parse binary.", e);
		}
	}

	public static Document loadXml( String xmlString ) throws Exception
	{
		return loadDocument(new org.xml.sax.InputSource(new java.io.StringReader(xmlString)));
	}
	
	public static void saveDocument( Document doc, String filename, boolean prettyPrint ) throws Exception
	{
		saveDocument( doc, filename, "UTF-8", false, false, prettyPrint );
	}
	
	public static void saveDocument( Document doc, com.altova.io.Output output, String encoding, boolean bBigEndian, boolean bBOM, boolean prettyPrint ) throws Exception
	{
		switch (output.getType())
		{
		case com.altova.io.Output.IO_STREAM:
			saveDocument(doc, output.getStream(), encoding, bBigEndian, bBOM, prettyPrint);
			break;
			
		case com.altova.io.Output.IO_WRITER:
			saveXml(doc, prettyPrint, output.getWriter());
			break;
			
		case com.altova.io.Output.IO_DOM:
			break;
			
		default:
			throw new Exception ("Unknown output type");
		}
	}
	
	public static void saveDocument( Document doc, java.io.OutputStream targetStream, String encoding, boolean bBigEndian, boolean bBOM, boolean prettyPrint ) throws Exception
	{
		if(prettyPrint)
			indentNode(doc.getDocumentElement(), 0);
	
		try {
			java.io.ByteArrayOutputStream result = new java.io.ByteArrayOutputStream();
			internalSave(new javax.xml.transform.stream.StreamResult(result), doc, encoding, prettyPrint);

			correctByteOrderAndBOM( targetStream, result.toByteArray(), encoding, bBigEndian, bBOM );
		}
		catch (Exception e)
		{
			throw new com.altova.TargetInstanceUnavailableException("Cannot write to output stream", e);
		}
	}
	
	public static void saveDocument( Document doc, String filename, String encoding, boolean bBigEndian, boolean bBOM, boolean prettyPrint ) throws Exception
	{
		java.io.FileOutputStream outStream = new java.io.FileOutputStream( filename, false );
		saveDocument(doc, outStream, encoding, bBigEndian, bBOM, prettyPrint);
	}
	
	public static byte[] saveXmlBinary( Document doc, String encoding, boolean bBigEndian, boolean bBOM, boolean prettyPrint ) throws Exception
	{
		if(prettyPrint)
			indentNode(doc.getDocumentElement(), 0);

		try
		{
			java.io.ByteArrayOutputStream result = new java.io.ByteArrayOutputStream();
			internalSave(new javax.xml.transform.stream.StreamResult(result), doc, encoding, prettyPrint);

			byte[] transformedData = result.toByteArray();
			result.reset();

			correctByteOrderAndBOM( result, transformedData, encoding, bBigEndian, bBOM );
			return result.toByteArray();
		}
		catch (Exception e)
		{
			throw new com.altova.TargetInstanceUnavailableException("Cannot serialize.", e);
		}
	}
	
	private static void saveXml( Document doc, boolean prettyPrint, java.io.Writer writer ) throws Exception
	{
		if(prettyPrint)
			indentNode(doc.getDocumentElement(), 0);

		try 
		{
			internalSave(new javax.xml.transform.stream.StreamResult(writer), doc, null, prettyPrint);
			writer.flush();
		}
		catch (Exception e)
		{
			throw new com.altova.TargetInstanceUnavailableException("Cannot serialize.", e);
		}
	}
	
	public static String saveXml( Document doc, boolean prettyPrint ) throws Exception
	{
		java.io.StringWriter result = new java.io.StringWriter();
		saveXml(doc, prettyPrint, result);
		return result.toString();
	}

	protected static void internalSave(javax.xml.transform.Result result, Node node, String encoding, boolean bIndent) throws Exception
	{
		try 
		{
			javax.xml.transform.Source source
					= new javax.xml.transform.dom.DOMSource(node);
			javax.xml.transform.Transformer transformer
					= javax.xml.transform.TransformerFactory.newInstance().newTransformer();
			if (encoding != null)
				transformer.setOutputProperty("encoding", encoding);
			Document doc = node.getNodeType() == Node.DOCUMENT_NODE ? (Document) node : node.getOwnerDocument();
			if (doc.getDoctype() != null) 
			{
				if (doc.getDoctype().getPublicId() != null)
					transformer.setOutputProperty(javax.xml.transform.OutputKeys.DOCTYPE_PUBLIC, doc.getDoctype().getPublicId());
				if (doc.getDoctype().getSystemId() != null)
					transformer.setOutputProperty(javax.xml.transform.OutputKeys.DOCTYPE_SYSTEM, doc.getDoctype().getSystemId());
			}
			transformer.setOutputProperty("indent", bIndent ? "yes" : "no");
			transformer.transform(source, result);
		} 
		catch (javax.xml.transform.TransformerConfigurationException e) 
		{
			throw new Exception(e);
		} 
		catch (javax.xml.transform.TransformerException e) 
		{
			throw new Exception(e);
		}
	}

	private static void correctByteOrderAndBOM( java.io.OutputStream outStream, byte[] inData, String encoding, boolean bBigEndian, boolean bBOM ) throws Exception
	{
		if( inData == null || outStream == null )
			return;

		if( encoding == null )
		{
			outStream.write( inData );
			return;
		}

		int unisize = getUnicodeSizeFromEncodingName( encoding );
		if( unisize == 0 )
		{
			outStream.write( inData );
			return;
		}

		if( unisize < 2 )
			bBigEndian = false;

		int encbo = getEncodingAndByteOrderFromXml( inData );

		boolean bHasBOM = (encbo & 0x10) != 0;
		boolean bIsBigEndian = (encbo & 0x20) != 0;

		if( bBOM == bHasBOM && bBigEndian == bIsBigEndian )
		{
			outStream.write( inData );
			return;
		}

		if( unisize > 0 && bBOM && !bHasBOM )
		{
			if( unisize == 1 )
			{
				outStream.write( 0xef );
				outStream.write( 0xbb );
				outStream.write( 0xbf );
			}
			else
			if( unisize == 2 )
			{
				if( bBigEndian ) {
					outStream.write( 0xfe );
					outStream.write( 0xff );
				} else {
					outStream.write( 0xff );
					outStream.write( 0xfe );
				}
			}
		}

		int off = 0;
		int len = inData.length;

		if( !bBOM && bHasBOM )
		{
			int bomsize = (encbo & 0xf00) >> 8;
			off += bomsize;
			len -= bomsize;
		}

		if( bBigEndian != bIsBigEndian && unisize >= 2 )
		{
			int count = (len / unisize) * unisize;
			for( int i = 0 ; i < count ; i += unisize )
			{
				outStream.write( inData[off + i + 1] );
				outStream.write( inData[off + i    ] );
			}
		}
		else
			outStream.write( inData, off, len );
	}

	// Returns
	//	bits 0-3:		encoding char size in bytes (1 = UTF-8, 2 = UTF-16 or UCS-2)
	//	bit  4:			if BOM is included
	//	bit  5:			if BigEndian (set) or LittleEndian (cleared)
	//  bits 8-11:		BOM size in bytes
	private static int getEncodingAndByteOrderFromXml( byte[] source )
	{
		if( source == null ) return 0;
		if( source.length < 2 ) return 0;

		if( ((source[0] & 0xff) == 0xfe) && ((source[1] & 0xff) == 0xff) )
			return 0x232;	// UTF-16BE with BOM

		if( ((source[0] & 0xff) == 0xff) && ((source[1] & 0xff) == 0xfe) )
			return 0x212;	// UTF-16LE with BOM

		if( ((source[0] & 0xff) == 0x00) && ((source[1] & 0xff) == 0x3c) )	// checks for "<" character
			return 0x022;	// UTF-16BE without BOM

		if( ((source[0] & 0xff) == 0x3c) && ((source[1] & 0xff) == 0x00) )	// checks for "<" character
			return 0x002;	// UTF-16LE without BOM

		if( source.length < 3 ) return 0;

		if( ((source[0] & 0xff) == 0xef) // & is because chars are signed!
				&& ((source[1] & 0xff) == 0xbb) && ((source[2] & 0xff) == 0xbf) )
			return 0x311;	// UTF-8 with BOM

		return 0;
	}

	private static int getUnicodeSizeFromEncodingName( String encoding )
	{
		if( encoding == null ) return 0;
		encoding = encoding.toUpperCase();

		if( encoding.indexOf("UTF-8") >= 0 )
			return 1;

		if( encoding.indexOf("UTF-16") >= 0 || encoding.indexOf("UCS-2") >= 0 )
			return 2;

		return 0;
	}

	private static void indentNode(org.w3c.dom.Node node, int nIndent) {
		if (node == null)
			return;
		
		if (nIndent > 0 && (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE || node.getNodeType() == org.w3c.dom.Node.COMMENT_NODE))
		{
			if (node.getPreviousSibling() == null || 
				(node.getPreviousSibling().getNodeType() != org.w3c.dom.Node.CDATA_SECTION_NODE && 
				 node.getPreviousSibling().getNodeType() != org.w3c.dom.Node.TEXT_NODE))
			{
				String sIndent = "\n";
				for (int i=0; i<nIndent; i++) sIndent += "\t";
				node.getParentNode().insertBefore(node.getOwnerDocument().createTextNode(sIndent), node);
				if (node.getNextSibling() == null)
				{
					sIndent = sIndent.substring(0, nIndent);
					node.getParentNode().appendChild(node.getOwnerDocument().createTextNode(sIndent));
				}
			}
		}
	
		for (org.w3c.dom.Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
			indentNode(child, nIndent+1);
	}
	
	protected static javax.xml.parsers.DocumentBuilder getDomBuilder() throws javax.xml.parsers.ParserConfigurationException
	{
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setNamespaceAware(true);
		
		return factory.newDocumentBuilder();
	}
	
	public static Document createDocument() throws Exception
	{
		return XmlTreeOperations.getDomBuilder().newDocument();
	}

	public static DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) throws Exception
	{
		return XmlTreeOperations.getDomBuilder().getDOMImplementation().createDocumentType(qualifiedName, publicId, systemId);		
	}

	private static boolean memberEqualsNode(MemberInfo member, Node node)
	{
		String nodeURI = node.getNamespaceURI() == null ? "" : node.getNamespaceURI();
		String nodeLocalName = node.getLocalName() == null ? "" : node.getLocalName();
		String memberURI = member.getNamespaceURI() == null ? "" : member.getNamespaceURI();
		String memberLocalName = member.getLocalName() == null ? "" : member.getLocalName();
		
		// saop12 todo? or not? 
		// soap-array specialty: no-namespace elements are array members.
		if ((member.getFlags() & MemberInfo.SPECIAL_NAME) != 0)
				return nodeURI.equals("");
		
		if (nodeURI.equals(memberURI) && nodeLocalName.equals(memberLocalName))
			return true;
		return false;
	}
	
	public static void setAttribute(Node node, String nameNsUri, String name, String valueNsUri, String value)
	{
		setAttribute((Element) node, nameNsUri, name, valueNsUri, value);
	}
	
	public static void setAttribute(Element element, String nameNsUri, String name, String valueNsUri, String value)
	{
		String namePrefix = null;
		String valuePrefix = null;
		String inlinedNamePrefix = "";
		
		int i = name.indexOf(':');
		if (i != -1)
			inlinedNamePrefix = name.substring(0, i);
		
		if (inlinedNamePrefix.length() > 0)
			name = name.substring(name.indexOf(':')+1);
		
		if (nameNsUri != null && nameNsUri.length() > 0)
		{
			namePrefix = element.lookupPrefix(nameNsUri);
			if (namePrefix == null)
			{
				if (inlinedNamePrefix.length() > 0)
					namePrefix = inlinedNamePrefix;
				else
					namePrefix = "ns" + staticNSCount++;
				element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + namePrefix, nameNsUri);
			}
			namePrefix += ":";
		}
		else
			namePrefix = "";
		
		if (valueNsUri != null && valueNsUri.length() > 0)
		{	
			valuePrefix = element.lookupPrefix(valueNsUri);
			if (valuePrefix == null)
			{
				valuePrefix = "ns" + staticNSCount++;
				element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + valuePrefix, valueNsUri);
			}
			valuePrefix += ":";
		}
		else 
			valuePrefix = "";
		
		element.setAttributeNS(nameNsUri, namePrefix + name, valuePrefix + value);
	}
		
	private static String findUnusedPrefix(Node node)
	{
		return findUnusedPrefix(node, null);
	}
	
	public static String findUnusedPrefix(Node node, String prefixHint)
	{
		String pp = prefixHint;
		if (pp == null || pp.equals(javax.xml.XMLConstants.DEFAULT_NS_PREFIX))
			pp = "n";
		else
		{
			if (node.lookupNamespaceURI(pp) == null)
				return pp;
		}
		
		int n = 1;
		while (true)
		{
			String s = pp + String.valueOf(n);
			if (node.lookupNamespaceURI(s) == null)
				return s;
		}
	}
		
	public static Node dereference(Node node) throws Exception
	{
		if (node.getNodeType() == Node.ELEMENT_NODE && ((Element) node).hasAttribute("href"))
		{
			String href = ((Element) node).getAttribute("href");
			if (href.length() == 0 || href.charAt(0) != '#')
				throw new Exception("Cannot dereference external references.");
				
			href = href.substring(1);
			
			for (Node ref = node.getOwnerDocument().getDocumentElement().getFirstChild().getNextSibling(); ref != null; ref = ref.getNextSibling())
				if (ref.getNodeType() == Node.ELEMENT_NODE && ((Element)ref).getAttribute("id").equals(href))
					return ref;
		}
		return node;
	}
	
	public static Node dereference12(Node node) throws Exception
	{
		if (node.getNodeType() == Node.ELEMENT_NODE && ((Element) node).hasAttributeNS("http://www.w3.org/2003/05/soap-encoding", "ref"))
		{
			String ref = ((Element) node).getAttributeNS("http://www.w3.org/2003/05/soap-encoding", "ref");
			if (ref.length() == 0)
				throw new Exception("Cannot dereference null references.");
			
			Node refNode = derefNode12(node.getOwnerDocument().getDocumentElement(), ref);
			if (refNode != null)
				return refNode;
		}
		return node;
	}
	
	private static Node derefNode12(Node node, String ref) throws Exception
	{
		if (node.getNodeType() == Node.ELEMENT_NODE && ((Element)node).getAttributeNS("http://www.w3.org/2003/05/soap-encoding", "id").equals(ref))
			return node;
		
		for (Node n = node.getFirstChild(); n!= null; n=n.getNextSibling())
		{
			Node refNode = derefNode12(n, ref);
			if (refNode != null)
				return refNode;
		}
		
		return null;
	}
	
	public static Element getNextElementSibling(Node node)
	{
		Node n = node.getNextSibling();
					
		if (n == null || n.getNodeType() == Node.ELEMENT_NODE)
			return (Element) n;
		
		return getNextElementSibling(n);
	}
	
	public static Element getFirstElementChild(Node node)
	{
		for (Node n=node.getFirstChild(); n!=null; n=n.getNextSibling())
			if (n.getNodeType() == Node.ELEMENT_NODE)
				return (Element) n;
		return null;
	}
	
	public static Element findChildNode(Node node, String localName)
	{
		for (Element e = getFirstElementChild(node); e != null; e = getNextElementSibling(e))
		{
			if (e.getLocalName().equals(localName))
				return e;
		}
			
		return null;
	}
	
	public static Element appendElement(Node parent, String localName)
	{
		return appendElement(parent, null, localName);
	}
	
	public static Element appendElement(Node parent, String nsURI, String localName)
	{
		if (parent instanceof Document)
			return (Element) parent.appendChild(((Document) parent).createElementNS(nsURI, localName));
		return (Element) parent.appendChild(parent.getOwnerDocument().createElementNS(nsURI, localName));
	}
}
