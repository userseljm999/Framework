package com.chtr.tmoauto.webservices;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class is used for validating two xml tags.
 * @since: 11/03/2016
 * @author: Sangram Pisal
 */

public class ValidateXmlResponseXML
{
	static boolean result;

	

	/**
	 * This function is used to check value in xml tags and returns true/false.
	 * 
	 * @param: actualXmlFilePath
	 * @param: actualTagname
	 * @param: expectedXmlFilePath
	 * @param: expectedTagname
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */
	public boolean fw_ValidateSOAPResponseXML(String actualXmlFilePath, String actualTagname, String referenceFilePath,
			String expectedTagname) throws Exception
	{

		// reading actual xml for the particular node
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(actualXmlFilePath);

		Node nodeActual = document.getElementsByTagName(actualTagname).item(0);

		String nodeActualValue = nodeActual.getTextContent();
		System.out.println("The value of actual node is : " + nodeActualValue);

		// reading reference xml for the particular node
		DocumentBuilderFactory builderFactory1 = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder1 = builderFactory1.newDocumentBuilder();
		Document document1 = builder1.parse(referenceFilePath);

		Node nodeRef = document1.getElementsByTagName(expectedTagname).item(0);
		String nodeRefValue = nodeRef.getTextContent();
		System.out.println("The value of reference node is : " + nodeRefValue);

		if (nodeActualValue.equalsIgnoreCase(nodeRefValue))
		{
			result = true;
			System.out.println("both the nodes are same");
		} else
		{
			result = false;
		}

		return result;

	}
	
	
	
	// ******************************************************************************
		// *
		// * Name: fw_read_from_xml
		// * Author: Gaurav Kumar
		// * Date: 11/16/2016
		// * Method to get value of a node in the xml file
		// *
		// ******************************************************************************

		public static String fw_read_from_xml(File responseXML, String tagName)
				throws ParserConfigurationException, SAXException, IOException {
			String value = null;

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(responseXML);

			Node node = document.getElementsByTagName(tagName).item(0);
			try {
				value = node.getTextContent();
			} catch (NullPointerException ne) {
				value = "";
			}

			return value;
		}

		
		
		
		// ******************************************************************************
		// *
		// * Name: fw_read_from_xml
		// * Author: Gaurav Kumar
		// * Date: 11/24/2016
		// * Method to get multiple tag values in the xml file
		// *
		// ******************************************************************************

		public static List<String> read_from_xml_to_get_multiple_tag(
				File responseXML, String tagName)
				throws ParserConfigurationException, SAXException, IOException {
			String value = null;

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(responseXML);
			List<String> returnValueList = new ArrayList<String>();
			NodeList nodelist = document.getElementsByTagName(tagName);
			for (int i = 0; i < nodelist.getLength(); i++) {
				Node node = nodelist.item(i);
				try {
					value = node.getTextContent();
				} catch (NullPointerException ne) {
					value = "";
				}
				returnValueList.add(value);
			}

			return returnValueList;
		}

}
