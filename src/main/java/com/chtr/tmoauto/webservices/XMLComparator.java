package com.chtr.tmoauto.webservices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class is used to compare XML.
 * @since: 10/28/2016
 * @author: Sangram Pisal
 */
public class XMLComparator
{

	public static String messages = "";
	private static XMLComparator xmlComparator = null;
	boolean isValid = true; // This is to report the final status

	/**
	 * This function is used to getInstance of XMLComparator.
	 * 
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */

	public static XMLComparator fw_GetInstance()
	{
		if (null == xmlComparator)
		{
			xmlComparator = new XMLComparator();
		}
		return xmlComparator;
	}

	/**
	 * This function is used to compare two XML files File and returns true or
	 * false.
	 * 
	 * @param: baseNode
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */

	public boolean fw_CompareFiles(final File xmlFile1, final File xmlFile2)
	{

		Document documentActual;
		Document documentTempl;

		int iNodesCounter, iNodesActualSize, iNodesTemplSize;
		try
		{
			isValid = true;
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			/*
			 * Command line arguments as the name of the XML files which need to
			 * be compared Takes the command line arguments as the name of the
			 * files
			 */
			documentTempl = builder.parse(xmlFile2);
			documentActual = builder.parse(xmlFile1);
			documentActual.getDocumentElement().normalize();

			// Here the first node of both XML is taken
			final NodeList nodesActual = documentActual.getChildNodes();
			final NodeList nodesTempl = documentTempl.getChildNodes();
			iNodesActualSize = nodesActual.getLength();
			iNodesTemplSize = nodesTempl.getLength();

			/*
			 * checks whether the size of the two nodes is same.If it is not
			 * same then it doesn't parses, as parsing becomes invalid.
			 */
			if (iNodesTemplSize == iNodesActualSize)
			{
				for (iNodesCounter = 0; iNodesCounter < iNodesActualSize; iNodesCounter++)
				{
					/*
					 * This is the method where all comparing of nodes and
					 * attributes takes place
					 */
					fw_CompareNodes(nodesActual.item(iNodesCounter), nodesTempl.item(iNodesCounter));
				}
			} else
			{
				isValid = false;
			}
			// Checks for valid being true or false
			if (isValid)
			{

				System.out.println(xmlFile1 + " and " + xmlFile2 + " are identical\n");
			} else
			{

				messages = messages + xmlFile1 + " and " + xmlFile2 + " are NOT identical\n";

				System.out.println(xmlFile1 + " and " + xmlFile2 + " are NOT identical\n");
			}
		} catch (Exception e)
		{
			System.out.println("Exception occurred : " + e.getMessage());
			System.out.println(" One of the XML documents (Template/Actual) is not valid");
			e.printStackTrace();
			isValid = false;
			messages = messages + "Exception occurred : " + "\n"
					+ " One of the XML documents (Template/Actual) is not valid";

			System.out.println(
					"Exception occurred : " + "\n" + " One of the XML documents (Template/Actual) is not valid");
		}
		return isValid;
	}

	/**
	 * This function is used to compare two XMLNodes
	 * (xmlNodeActual/xmlNodeTempl)
	 * 
	 * @param: xmlNodeActual
	 * @param: xmlNodeTempl
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */
	private void fw_CompareNodes(Node xmlNodeActual, Node xmlNodeTempl)
	{
		int iActAttrLen, iTemplAttrLen;
		int iActNodesListSize, iTempNodeListSize;
		String strActNodeName;
		String strActNodeValue = null;
		String strTemplNodeName;
		String strTemplNodeValue = null;
		ArrayList<Node> actNodesList;
		ArrayList<Node> templNodesList;
		NamedNodeMap actAttrMap;
		NamedNodeMap templAttrMap;
		// Child Nodes Logic.
		final NodeList nodesActual = xmlNodeActual.getChildNodes();
		final NodeList nodesTempl = xmlNodeTempl.getChildNodes();
		/*
		 * This method will remove any empty nodes with value as "#text". This
		 * is required because the xmls may have extra blank spaces, tabs or new
		 * lines.
		 */
		actNodesList = fw_RemoveDirtyNodes(nodesActual);
		templNodesList = fw_RemoveDirtyNodes(nodesTempl);
		iActNodesListSize = actNodesList.size();
		iTempNodeListSize = templNodesList.size();
		strActNodeName = xmlNodeActual.getNodeName().trim();
		if (iActNodesListSize == 0)
		{
			strActNodeValue = XercesExtra.fw_GetTextContent(xmlNodeActual).trim();
		}
		strTemplNodeName = xmlNodeTempl.getNodeName().trim();
		if (iTempNodeListSize == 0)
		{
			strTemplNodeValue = XercesExtra.fw_GetTextContent(xmlNodeTempl).trim();
		}
		// Actual comparison of node takes place.
		if (!fw_IsMaskableValue(strActNodeName))
		{
			// System.out.println("isn ot maskable");
			if (!strTemplNodeName.equalsIgnoreCase(strActNodeName) || (strTemplNodeValue != null
					&& strActNodeValue != null && !strTemplNodeValue.equalsIgnoreCase(strActNodeValue)))
			{
				// System.out.println("hjdgajshg");
				isValid = false;
				// Displays the appropriate message if nodes value does not
				// match
				fw_Message(strActNodeName, strActNodeValue, strTemplNodeName, strTemplNodeValue);
			} else
			{
				actAttrMap = xmlNodeActual.getAttributes();
				templAttrMap = xmlNodeTempl.getAttributes();
				if ((actAttrMap != null) && (templAttrMap != null))
				{
					iActAttrLen = actAttrMap.getLength();
					iTemplAttrLen = templAttrMap.getLength();
					// Compares the length for the attributes.
					if (iActAttrLen == iTemplAttrLen)
					{
						fw_CompareAttr(xmlNodeActual, xmlNodeTempl);
					} else
					{
						isValid = false;
						/*
						 * logmanager.info(log, "Error in Node : " +
						 * strActNodeName + " number of Actual Attributes are "
						 * + iActAttrLen + " Required number of Attributes are "
						 * + iTemplAttrLen);
						 */
						messages = messages + "Error in Node : " + strActNodeName + " number of Actual Attributes are "
								+ iActAttrLen + " Required number of Attributes are " + iTemplAttrLen;
					}
				}
			}
			if (iActNodesListSize == iTempNodeListSize)
			{
				// Child nodes are taken one by one.
				fw_ListSizeisSame(actNodesList, templNodesList, iActNodesListSize);
			} else
			{
				if (!"ns6:Response".equalsIgnoreCase(strActNodeName))
				{
					isValid = false;
					messages = messages.concat("Error in node: " + xmlNodeActual.getNodeName()
							+ " number of child elements differ ::::::::.");
					System.out.println("Error in node: " + xmlNodeActual.getNodeName()
							+ " number of child elements differ ::::::::.");

				}
			}
		}
	}

	/**
	 * This function is used to check the size of List is same.
	 * 
	 * @param: actNodesList
	 * @param: templNodesList
	 * @param: iActNodesListSize
	 * @param: xmlChildNodeAct
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */

	public void fw_ListSizeisSame(List<Node> actNodesList, List<Node> templNodesList, int iActNodesListSize)
	{
		Node xmlChildNodeAct;
		Node xmlChildNodeTempl;
		for (int iNodesCounter = 0; iNodesCounter < iActNodesListSize; iNodesCounter++)
		{
			xmlChildNodeAct = actNodesList.get(iNodesCounter);
			xmlChildNodeTempl = templNodesList.get(iNodesCounter);
			// Method is called with child nodes as the parameters
			fw_CompareNodes(xmlChildNodeAct, xmlChildNodeTempl);
		}
	}

	/**
	 * This function is used to check the size of List.
	 * 
	 * @param: strActNodeName
	 * @param: strActNodeValue
	 * @param: strTemplNodeName
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */
	public void fw_Message(String strActNodeName, String strActNodeValue, String strTemplNodeName,
			String strTemplNodeValue)
	{
		messages = messages.concat("Error in node " + strActNodeName + "." + " Actual Node Name is " + strActNodeName
				+ " Expected Node Name is " + strTemplNodeName + "." + " Actual Node value is " + strActNodeValue
				+ ". The Expected Node Value is: " + strTemplNodeValue);
		/*
		 * logmanager.info(log, "Error in node " + strActNodeName);
		 * logmanager.info(log, " Actual Node Name is " + strActNodeName +
		 * " Expected Node Name is " + strTemplNodeName); logmanager.info(log,
		 * " Actual Node value is " + strActNodeValue); logmanager.info(log,
		 * " The Expected Node Value is: " + strTemplNodeValue);
		 */

	}

	/**
	 * This function iis used to check the size of List.
	 * 
	 * @param: xmlNodeActual
	 * @param: xmlNodeTempl
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */

	private void fw_CompareAttr(final Node xmlNodeActual, final Node xmlNodeTempl)
	{
		int iActAttrLen, iCount;
		NamedNodeMap actAttrMap;
		NamedNodeMap templAttrMap;
		Node actAttr;
		Node templAttr;
		// Here the attributes of the node are found out
		actAttrMap = xmlNodeActual.getAttributes();
		templAttrMap = xmlNodeTempl.getAttributes();
		iActAttrLen = actAttrMap.getLength();
		for (iCount = 0; iCount < iActAttrLen; iCount++)
		{
			actAttr = actAttrMap.item(iCount);
			templAttr = templAttrMap.getNamedItem(actAttr.getNodeName().trim());
			if (!fw_IsMaskableAttributeValue(actAttr.getNodeName().trim()))
			{
				if (templAttr == null)
				{
					isValid = false;
					/*
					 * logmanager.info(log, "Error in Node " +
					 * xmlNodeActual.getNodeName().trim() + " Attribute " +
					 * actAttr.getNodeName().trim() +
					 * " not found in Template XML");
					 */

				}
			} else
			{
				if (!(actAttr.getNodeValue().trim().equalsIgnoreCase(templAttr.getNodeValue().trim())))
				{
					isValid = false;

					System.out.println("Error in Node " + xmlNodeActual.getNodeName()
							+ " Attribute Values do not match for attribute " + actAttr.getNodeName()
							+ " Actual Value is " + actAttr.getNodeValue() + " Expected Value is "
							+ templAttr.getNodeValue());

				}
			}
		}
	}

	/**
	 * This function is used to remove dirty nodes.
	 * 
	 * @param: nodes
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */

	private ArrayList<Node> fw_RemoveDirtyNodes(final NodeList nodes)
	{
		final ArrayList<Node> retNodes = new ArrayList<Node>();
		int iCount;
		int iSize;
		iSize = nodes.getLength();
		for (iCount = 0; iCount < iSize; iCount++)
		{
			// if the node is empty then don't add to the list of nodes to be
			// compared
			if (!"#text".equalsIgnoreCase(nodes.item(iCount).getNodeName()))
			{
				retNodes.add(nodes.item(iCount));
			}
		}
		return retNodes;
	}

	/**
	 * This function is used to check value is maskable or not and returns
	 * true/false.
	 * 
	 * @param: strNodeVal
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */
	public boolean fw_IsMaskableValue(final String strNodeVal)
	{
		int iCount;
		String[] strArrToBeMasked;
		boolean isReturn = false;

		strArrToBeMasked = Constants.MASKABLENODES;
		// strArrToBeMasked = new
		// String[]{"ns2:EchoBack","ns3:MessageID","ns3:CorrelationID"};
		for (iCount = 0; iCount < strArrToBeMasked.length; iCount++)
		{
			if (strNodeVal.equalsIgnoreCase(strArrToBeMasked[iCount].trim()))
			{
				isReturn = true;
			}
		}
		return isReturn;
	}

	/**
	 * This function is used to check attribute value is maskable or not and
	 * returns true/false.
	 * 
	 * @param: strAttrVals
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */

	public boolean fw_IsMaskableAttributeValue(final String strAttrVal)
	{
		int iCount;
		final String[] toBeMasked = Constants.maskableAttribute;
		// final String[] toBeMasked = new String[]{"attribute","masked"};
		boolean isReturn = false;
		for (iCount = 0; iCount < toBeMasked.length; iCount++)
		{
			if (strAttrVal.equalsIgnoreCase(toBeMasked[iCount].trim()))
			{
				isReturn = true;
			}
		}
		return isReturn;
	}
}
