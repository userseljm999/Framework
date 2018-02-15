package com.chtr.tmoauto.webservices;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class used to send request to Url and get the response.
 * @since: 11/02/2016
 * @author: Sangram Pisal
 */

public class RequestToWSDL
{

	/**
	 * This function is used to send request using WSDl and store response at
	 * particular location path. connection object. *
	 * 
	 * @param: EndpointURLstring
	 * @param: inputFilePath
	 * @param: outputFilePath
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 * @throws Exception 
	 */
	public static String fw_send_request_xml(File updateRequestXMLFile,
			String wsdlURL) throws Exception {

		
	  
		
		String updatedXMLFilePath =updateRequestXMLFile.getCanonicalPath().replaceAll("RequestFolder","ResponseFolder")+"-response";

		try {
			// TODO Auto-generated method stub

			// Code to make a webservice HTTP request
			String responseString = "";
			String outputString = "";

			URL url = new URL(wsdlURL);
			URLConnection connection = url.openConnection();
		
			String userpass = "chtr\\svc_tst_automation" + ":" + "H2i1fL9!";
			String basicAuth = "Basic "
					+ new String(new Base64().encode(userpass.getBytes()));

			// uc.setRequestProperty ("Authorization", basicAuth);
			HttpURLConnection httpConn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			// converting xml file to an xml string
			String xmlInput = fw_ConvertFile2Str(updateRequestXMLFile);

			byte[] buffer = new byte[xmlInput.length()];
			buffer = xmlInput.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			httpConn.setRequestProperty("Content-Length",
					String.valueOf(b.length));
			httpConn.setRequestProperty("Authorization", basicAuth);
			httpConn.setRequestProperty("Content-Type",
					"text/xml; charset=utf-8");

			// httpConn.setRequestProperty("SOAPAction", SOAPAction);
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			OutputStream out = httpConn.getOutputStream();

			// Write the content of the request to the outputstream of the HTTP
			// Connection.
			out.write(b);
			out.close();

			// Ready with sending the request.
			if (httpConn.getResponseCode() == 500) {
				InputStream error = ((HttpURLConnection) connection)
						.getErrorStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(error), 1);
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					System.out.println("  error" + line);
				}
				error.close();
				bufferedReader.close();
			}
		
			// Read the response.
			InputStreamReader isr = new InputStreamReader(
					httpConn.getInputStream());
			BufferedReader in = new BufferedReader(isr);

			// Write the SOAP message response to a String.
			while ((responseString = in.readLine()) != null) {
				outputString = outputString + responseString;
			}
			

			// Write the SOAP message formatted to the console.
			String formattedSOAPResponse = fw_format_xml(outputString);
			fw_string_to_dom(formattedSOAPResponse, updatedXMLFilePath);
			

		} catch (Exception e) {
			System.err
					.println("Error occurred while sending SOAP Request to Server");
			e.printStackTrace();
			throw e;
		}

		return updatedXMLFilePath;

	}

	// ******************************************************************************
		// *
		// * Name: fw_string_to_dom
		// * Author: Gaurav Kumar
		// * Date: 11/16/2016
		// * Method to convert a xml string to a document
		// *
		// ******************************************************************************
		public static void fw_string_to_dom(String xmlSource, String fileName)
				throws IOException {
			java.io.FileWriter fw = new java.io.FileWriter(fileName);
			fw.write(xmlSource);
			fw.close();
		}
	
	// ******************************************************************************
		// *
		// * Name: fw_parse_xml_File
		// * Author: Gaurav Kumar
		// * Date: 11/16/2016
		// * Method to parse the String output to a org.w3c.dom.Document
		// *
		// ******************************************************************************

		public static Document fw_parse_xml_File(String in) {
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(in));
				return db.parse(is);
			} catch (ParserConfigurationException e) {
				throw new RuntimeException(e);
			} catch (SAXException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		// ******************************************************************************
		// *
		// * Name: fw_format_xml
		// * Author: Gaurav Kumar
		// * Date: 11/16/2016
		// * Method to write the SOAP message formatted to the console.
		// *
		// ******************************************************************************
		public static String fw_format_xml(String unformattedXml) {
			try {
				Document document = fw_parse_xml_File(unformattedXml);
				OutputFormat format = new OutputFormat(document);
				format.setIndenting(true);
				format.setIndent(3);
				format.setOmitXMLDeclaration(true);
				Writer out = new StringWriter();
				XMLSerializer serializer = new XMLSerializer(out, format);
				serializer.serialize(document);
				return out.toString();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}



	

	/**
	 * The fw_Connect is used for Connection Object to open connection to.
	 * 
	 * @param: url
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */
	public static URLConnection fw_Connect(URL url) throws IOException
	{
		// URL url = null; // URL Object to connect to ESB in case of ACP
		URLConnection conn = null; // Connection Object to open connection to
		conn = fw_OpenConn2ESB(url);
		return conn;
	}

	/**
	 * The fw_ConvertFile2Str reads file from location and return as string.
	 * 
	 * @param: fileInput
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */
	public static String fw_ConvertFile2Str(File fileInput) throws IOException
	{

		FileReader fileReader = null;
		BufferedReader bfrReader = null;
		String strLine = null;
		String strText = "";
		// Logger log = Logger.getLogger(StageProcessorEngine.class);
		// Logging logmanager = Logging.getInstance();

		try
		{
			fileReader = new FileReader(fileInput);
			bfrReader = new BufferedReader(fileReader);
			while ((strLine = bfrReader.readLine()) != null)
			{
				strText = strText + strLine;
			}
		} catch (IOException ioException)
		{
			ioException.printStackTrace();
			// logmanager.exceptionErrorMsg(log,"Exception occured in FileUtil
			// in reading the file!!!",
			// ioException);
			throw ioException;

		} finally
		{
			if (null != bfrReader)
			{
				bfrReader.close();
			}
			if (fileReader != null)
			{
				fileReader.close();
			}

		}
		return strText;
	}

	/**
	 * The fw_OpenConn2ESB is used to open connection to ESB.
	 * 
	 * @param: url
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */
	private static URLConnection fw_OpenConn2ESB(URL url) throws IOException
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		byte[] byteArrayRequest = bout.toByteArray();

		final URLConnection conn = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) conn;
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setAllowUserInteraction(false);
		httpConn.setRequestProperty("Content-Length", String.valueOf(byteArrayRequest.length));

		httpConn.setRequestProperty("Accept", "text/xml");

		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("SOAPAction", "customAction");
		httpConn.setRequestMethod("POST");

		return httpConn;
	}

}
