package com.chtr.tmoauto.util;



import com.chtr.tmoauto.logging.Logging;
import com.chtr.tmoauto.logging.LoggingMultiThreaded;
import com.chtr.tmoauto.webservices.RequestToWSDL;
import com.chtr.tmoauto.webservices.UpdateRequestXML;
import com.chtr.tmoauto.webservices.ValidateXmlResponseXML;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class CancelCSGOrder {

	public String returnMessage = "";
	public  LoggingMultiThreaded logMultithread = null;
	public Logging logofLogging=null;
	

	public CancelCSGOrder(LoggingMultiThreaded log) {
		this.logMultithread=log;
	
	}

	public CancelCSGOrder(Logging log) {
		this.logofLogging=log;
	
	}
	

	// ******************************************************************************
	// *
	// * Name: fw_cancel_csg_orders_perAddress
	// * Author: Gaurav Kumar
	// * Date: 11/17/2016
	// * Method to invoke cancel order method
	// *
	// ******************************************************************************

	public Boolean fw_cancel_csg_orders_perAddress(String address_value,
			String zip_code_value,int tcID
			) throws Exception {
		
		
		String filePathOfgetAccountRequestXML="webservices\\templates\\ECOMMERCE-getAccounts";
		String filePathOfgetOrderRequestXML="webservices\\templates\\ECOMMERCE-getOrders";
		String filePathOfgetCancelOrderRequestXML="webservices\\templates\\ECOMMERCE-cancelOrder";
		String accountServiceWsdlURL="https://ebs-uat.corp.chartercom.com/csg_cter/2.06/AccountService.asmx?wsdl";
		String orderServiceWsdlURL="https://ebs-uat.corp.chartercom.com/csg_cter/2.06/OrderService.asmx?wsdl";
		String cancelOrderDetailwsdlURL="https://ebs-uat.corp.chartercom.com/csg_cter/2.06/OrderDetailService.asmx?wsdl";

		Boolean addressAvailableToSubmitOrder = true;

		try {

			String[] nodeValueFromGetAccount = fw_get_accounts_sysprin_locationID(
					address_value, zip_code_value,
					filePathOfgetAccountRequestXML, accountServiceWsdlURL,tcID);
			String sysPrin = nodeValueFromGetAccount[0];
			String locationID = nodeValueFromGetAccount[1];
			
			if(logofLogging==null)
				logMultithread.fw_writeLogEntry(
						"TCID - " +tcID
						+ "  Sysprin obtained is: "+ sysPrin
						+ " and locationID obtained is:" + locationID,
				"NA");
			else
				logofLogging.fw_writeLogEntry(
					"TCID - " +tcID
							+ "  Sysprin obtained is: "+ sysPrin
							+ " and locationID obtained is:" + locationID,
					"NA");
			System.out.println("SysPrin obtained is:" + sysPrin
					+ " and locationID is:" + locationID);

			if (!sysPrin.isEmpty() && !locationID.isEmpty()) {

				List<String> listEcommOrderID = fw_get_orderIds(
						nodeValueFromGetAccount[0], nodeValueFromGetAccount[1],
						filePathOfgetOrderRequestXML, orderServiceWsdlURL,tcID);

				if (listEcommOrderID != null) {
					for (String ecommOrderID : listEcommOrderID) {
						
						if(logofLogging==null)
							logMultithread.fw_writeLogEntry(
									"TCID - " +tcID
									+ "Cancelling order for  Order ID: "+ ecommOrderID,
							"NA");
						else
						logofLogging.fw_writeLogEntry(
								"TCID - " +tcID
										+ "Cancelling order for  Order ID: "+ ecommOrderID,
								"NA");
						Boolean cancelOrder = fw_cancel_order(ecommOrderID,
								sysPrin, filePathOfgetCancelOrderRequestXML,
								cancelOrderDetailwsdlURL,tcID);
						if (cancelOrder) {
							returnMessage = returnMessage
									+ "\nORDER CANCELLED FOR" + ecommOrderID;
							System.out.println("ORDER CANCELLED for"
									+ ecommOrderID);
						} else {
							returnMessage = returnMessage
									+ "\nCSG Cancel Pending Order webservice did NOT cancel Order for"
									+ ecommOrderID;
							System.out
									.println("CSG Cancel Pending Order webservice did NOT cancel Order for"
											+ ecommOrderID);
							addressAvailableToSubmitOrder = false;
						}
					}
				} else {
					returnMessage = returnMessage
							+ "\nGet Orders webservice response does NOT have OrderId value.";
					System.out
							.println("Get Orders webservice response does NOT have OrderId value.");
				}

			} else {
				returnMessage = returnMessage
						+ "\nGet Accounts webservice response does NOT have SysPrin or LocationId value.";
				System.out
						.println("Get Accounts webservice response does NOT have SysPrin or LocationId value.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			addressAvailableToSubmitOrder = false;
			returnMessage = returnMessage + e.getMessage();
			throw new Exception("Cancel CSG Order failed");

		}
		return addressAvailableToSubmitOrder;

	}

	// ******************************************************************************
	// *
	// * Name: fw_ge_accounts_sysprin_locationID
	// * Author: Gaurav Kumar
	// * Date: 11/27/2016
	// * Method to get sysprin and location id value from an address value and
	// zip code value
	// *
	// ******************************************************************************

	private String[] fw_get_accounts_sysprin_locationID(String address_value,
			String zip_code_value, String requestXMLFilePath, String wsdlURL,int tcID)
			throws Exception {

		LinkedHashMap<String, String> addressNodeValuePair = new LinkedHashMap<String, String>();
		addressNodeValuePair.put("XML_ECOMM_STREET_ADDRESS", address_value);
		addressNodeValuePair.put("XML_ECOMM_ZIP_CODE", zip_code_value);
		
		DateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
		Date dt = new Date();
		
		
		File updatedXMLFile = new File("webservices\\runtime\\RequestFolder\\"+dateFormat.format(dt)+" - TC-"+tcID+" - ECOMMERCE-getAccounts – KSTLENGN73V6659");

		File updateRequestXMLFile = UpdateRequestXML.fw_modify_xml(
				requestXMLFilePath, addressNodeValuePair, updatedXMLFile);

		File responseXML = new File(RequestToWSDL.fw_send_request_xml(
				updateRequestXMLFile, wsdlURL));

		String[] nodeValues = new String[2];

		nodeValues[0] = ValidateXmlResponseXML
				.fw_read_from_xml(responseXML, "SysPrin");

		nodeValues[1] = ValidateXmlResponseXML.fw_read_from_xml(responseXML,
				"LocationId");
		
		String errorResponse=null;
		if(nodeValues[0].isEmpty() && nodeValues[1].isEmpty())
		{
			errorResponse= ValidateXmlResponseXML.fw_read_from_xml(responseXML,
					"Message");
			String returnCode=ValidateXmlResponseXML.fw_read_from_xml(responseXML,
					"ReturnCode");
			if(returnCode.equalsIgnoreCase("Exception"))
			throw new Exception("Exception in response code with message:"+errorResponse);
		}

		return nodeValues;
	}

	// ******************************************************************************
	// *
	// * Name: fw_get_orderIds
	// * Author: Gaurav Kumar
	// * Date: 11/27/2016
	// * Method to get order ids based on sysprin and location id
	// *
	// ******************************************************************************

	private List<String> fw_get_orderIds(String sysPrin, String LocationId,
			String requestXMLFilePath, String wsdlURL,int tcID) throws Exception {

		String routingArea = sysPrin;
		LinkedHashMap<String, String> routingAreaLocationIdPair = new LinkedHashMap<String, String>();
		routingAreaLocationIdPair.put("XML_ECOMM_ROUTING_AREA", routingArea);
		routingAreaLocationIdPair.put("XML_ECOMM_LOCATION_ID", LocationId);
		DateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
		Date dt = new Date();
		
		File updatedXMLFile = new File("webservices\\runtime\\RequestFolder\\"+dateFormat.format(dt)+" - TC-"+tcID+" - ECOMMERCE-getOrders  – KSTLENGN73V6659");

		

		File updateRequestXMLFile = UpdateRequestXML.fw_modify_xml(
				requestXMLFilePath, routingAreaLocationIdPair, updatedXMLFile);

		File responseXML = new File(RequestToWSDL.fw_send_request_xml(
				updateRequestXMLFile, wsdlURL));

		List<String> ecommOrderID = null;

		if (!ValidateXmlResponseXML.fw_read_from_xml(responseXML, "ReturnCode")
				.equalsIgnoreCase("NoDataFound"))
			ecommOrderID = ValidateXmlResponseXML.read_from_xml_to_get_multiple_tag(
					responseXML, "OrderId");

		System.out.println("Ecomm id is:" + ecommOrderID);

		return ecommOrderID;

	}

	// ******************************************************************************
	// *
	// * Name: fw_cancel_order
	// * Author: Gaurav Kumar
	// * Date: 11/24/2016
	// * Method to cancel new order for an address having a pending order(s)
	// based on ecommerce id
	// *
	// ******************************************************************************

	private Boolean fw_cancel_order(String ecommOrderID, String routingArea,
			String requestXMLFilePath, String wsdlURL,int tcID) throws Exception {
		Boolean orderCancelled = false;

		LinkedHashMap<String, String> routingAreaPair = new LinkedHashMap<String, String>();
		routingAreaPair.put("XML_ECOMM_ROUTING_AREA", routingArea);
		routingAreaPair.put("XML_ECOMM_ORDER_ID", ecommOrderID);
		
		DateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
		Date dt = new Date();
		File updatedXMLFile = new File("webservices\\runtime\\RequestFolder\\"+dateFormat.format(dt)+" - TC-"+tcID+" - ECOMMERCE-cancelOrder   – KSTLENGN73V6659");

		
		
		
		File updateRequestXMLFile = UpdateRequestXML.fw_modify_xml(
				requestXMLFilePath, routingAreaPair, updatedXMLFile);

		File responseXML = new File(RequestToWSDL.fw_send_request_xml(
				updateRequestXMLFile, wsdlURL));

		if (ValidateXmlResponseXML.fw_read_from_xml(responseXML, "ReturnCode")
				.equalsIgnoreCase("Success")) {
			orderCancelled = true;
		}

		return orderCancelled;
	}

	
	
	public static String[] fw_get_custId_locId(String accountNumber,int tcID)
			throws Exception {

		DateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
		Date dt = new Date();
		File updateRequestXMLFile = new File("webservices\\runtime\\RequestFolder\\"+dateFormat.format(dt)+" - TC-"+tcID+" - ECOMMERCE-getCustomerID   – KSTLENGN73V6659");

		

		String xmlInput = "<OSSRequest><getCSGInfo><accountNumber>"
				+ accountNumber + "</accountNumber></getCSGInfo></OSSRequest>";
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(updateRequestXMLFile.getAbsolutePath()),
				"utf-8"))) {
			writer.write(xmlInput);
		}

		File responseXML = new File(
				RequestToWSDL.fw_send_request_xml(updateRequestXMLFile,
						"http://ossapiu.corp.chartercom.com:8099"));

		String[] nodeValues = new String[2];

		nodeValues[0] = ValidateXmlResponseXML.fw_read_from_xml(responseXML,
				"billingCustomerID");
		System.out.println("Customer ID is:" + nodeValues[0]);
		nodeValues[1] = ValidateXmlResponseXML.fw_read_from_xml(responseXML,
				"billingLocationID");
		System.out.println("Location id is:" + nodeValues[1]);

		return nodeValues;
	}


}
