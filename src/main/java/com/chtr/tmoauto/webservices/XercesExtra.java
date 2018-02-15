package com.chtr.tmoauto.webservices;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.TextImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/**
 *  This class is used  for parsing and manipulating XML.(Xerces is Apache's collection of software libraries and we are using libraries in this classs with additional libraries .
 * @since: 11/03/2016
 * @author: Sangram Pisal
 */

public class XercesExtra
{
	/**
	 * This function creates a new instance of XercesExtra.
	 * 
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */

	public XercesExtra()
	{
	}

	/**
	 * This function is Get Node text content.
	 * 
	 * @param: baseNode
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */
	public static String fw_GetTextContent(final Node baseNode) throws DOMException
	{
		final Node child = baseNode.getFirstChild();
		String strReturn = "";
		if (child != null)
		{
			final Node next = child.getNextSibling();
			if (next == null)
			{
				if (fw_HasTextContent(child))
				{
					return ((NodeImpl) child).getTextContent();
				} else
				{
					return "";
				}
			}
			final StringBuffer buf = new StringBuffer();
			fw_GetTextContent(baseNode, buf);
			strReturn = buf.toString();
		}

		return strReturn;

	}

	
	/**
	 * This function is used to forward text content.
	 * 
	 * @param: baseNode
	 * @param: StringBuffer
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */
	private static void fw_GetTextContent(final Node baseNode, final StringBuffer buf) throws DOMException
	{
		Node child = baseNode.getFirstChild();
		while (child != null)
		{
			if (fw_HasTextContent(child))
			{
				fw_GetTextContentExtra((child), buf);
			}
			child = child.getNextSibling();
		}
	}

	

	/**
	 * This function checks forwarded has text content or not.
	 * 
	 * @param: child
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */
	private static final boolean fw_HasTextContent(final Node child)
	{
		return child.getNodeType() != Node.COMMENT_NODE && child.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE
				&& (child.getNodeType() != Node.TEXT_NODE || !(((TextImpl) child).isIgnorableWhitespace()));
	}

	

	/**
	 * This function forwards text content.
	 * 
	 * @param: child
	 * @param: StringBuffer
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */
	private static void fw_GetTextContentExtra(final Node child, final StringBuffer buf) throws DOMException
	{
		String strContent;
		strContent = child.getNodeValue();
		if (strContent != null)
		{
			buf.append(strContent);
		}
	}

}
