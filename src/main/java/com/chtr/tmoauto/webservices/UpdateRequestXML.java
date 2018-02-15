package com.chtr.tmoauto.webservices;

import java.io.BufferedWriter;
import java.io.File;
/*import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;*/

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
/*import javax.xml.parsers.ParserConfigurationException;*/
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This class is used to update value in xml.
 * @since: 10/26/2016
 * @author: Sangram Pisal
 */
public class UpdateRequestXML
{

	/**
	 * This function is used to update particular tag value in xml.
	 * 
	 * @param: filePath
	 * @param: tagName
	 * @param: updatedValue
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */

	// ******************************************************************************
		// *
		// * Name: fw_modify_xml
		// * Author: Gaurav Kumar
		// * Date: 11/16/2016
		// * Method to find and replace the old node value with a new value in the
		// xml file
		// *
		// ******************************************************************************

		public static File fw_modify_xml(String xmlFilePath,
				LinkedHashMap oldValueNewValuePair, File updatedXMLFile)
				throws Exception {

			File fetched = new File(xmlFilePath);
			
			String xmlInput = RequestToWSDL.fw_ConvertFile2Str(fetched);

			Set<String> keys = oldValueNewValuePair.keySet();
			for (String k : keys) {
				
				xmlInput = xmlInput.replaceAll(k, oldValueNewValuePair.get(k)
						.toString());
			}

			try (Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(
							updatedXMLFile.getAbsolutePath()), "utf-8"))) {
				writer.write(xmlInput);
			}
			return updatedXMLFile;

		}
}