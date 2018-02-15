package com.chtr.tmoauto.webui;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.hssf.usermodel.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

import com.chtr.tmoauto.logging.*;
import com.chtr.tmoauto.util.DateTime;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.chtr.tmoauto.util.ALM;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.support.ui.Select;
import org.webbitserver.helpers.Base64;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.contrib.ssl.AuthSSLProtocolSocketFactory;
//import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class GUI 
{
	 	
	public static WebDriver driver;	
	public static WebDriver driverie;
	public static WebDriver driverchrome;
	
	public static Logging log = new Logging();
	public static DateTime dt = new DateTime();
	public static ALM fwalm = new ALM();
	
	public static String window_handle = "";
	public static String return_get_text = "";
	public static Boolean return_existence;
	public static String out_object_testdata;
	public static String out_object_extrainfo = "";

	public static Session session;
	
	public static HashMap<Integer, String> hmap_object_name;
	public static HashMap<Integer, String> hmap_object_def;
	public static HashMap<Integer, String> hmap_object_extrainfo;

	/**
	 * This function is used to get a phone number.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 4/19/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_get_phone_number(String phone_type, String phone_switch_type, String account_type, String index_value) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String teamvalue = "";
		
		if (index_value.contains("---"))
		{
			String[] index_value_arr = index_value.split("---");
			index_value = index_value_arr[0];
			teamvalue = index_value_arr[1];	
		}
		else
		{
			teamvalue = "AUTOMATEDREGRESSION";
		}
		
		String npa_value = fw_get_variable("ENVgetTNlookupNPA");
		String nxx_value = fw_get_variable("ENVgetTNlookupNXX");
			
		String env_value = fw_get_variable("ENVSELECTED");
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		account_type = account_type.toUpperCase();
		phone_switch_type = phone_switch_type.toUpperCase();
		String phone_rate_center = fw_get_variable("ENVRateCenter" + phone_switch_type + account_type);
		
		if (env_value.equals("PROD"))
		{
			if (npa_value.equals(""))
			{
				sql_query = "select phone_auto_id, phone_value from tbl_phone where phone_available_flag = 'Y' and phone_type = '" + phone_type + "' and phone_switch_type = '" + phone_switch_type + "' and phone_rate_center = '" + phone_rate_center + "' and phone_environment = 'PROD' and rownum = 1 and phone_team = '" + teamvalue + "'";
			}
			else
			{
				sql_query = "select phone_auto_id, phone_value from tbl_phone where phone_available_flag = 'Y' and phone_type = '" + phone_type + "' and phone_switch_type = '" + phone_switch_type + "' and phone_rate_center = '" + phone_rate_center + "' and phone_environment = 'PROD' and substr(phone_value,1,3) = '" + npa_value + "' and substr(phone_value,4,3) = '" + nxx_value + "' and rownum = 1 and phone_team = '" + teamvalue + "'";
			}
		}
		else
		{
			if (npa_value.equals(""))
			{
				sql_query = "select phone_auto_id, phone_value from tbl_phone where phone_available_flag = 'Y' and phone_type = '" + phone_type + "' and phone_switch_type = '" + phone_switch_type + "' and phone_rate_center = '" + phone_rate_center + "' and phone_environment = 'TEST' and rownum = 1 and phone_team = '" + teamvalue + "'";
			}
			else
			{
				sql_query = "select phone_auto_id, phone_value from tbl_phone where phone_available_flag = 'Y' and phone_type = '" + phone_type + "' and phone_switch_type = '" + phone_switch_type + "' and phone_rate_center = '" + phone_rate_center + "' and phone_environment = 'TEST' and substr(phone_value,1,3) = '" + npa_value + "' and substr(phone_value,4,3) = '" + nxx_value + "' and rownum = 1 and phone_team = '" + teamvalue + "'";
			}
		}
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No TN Found in TBL_PHONE table";

		String out_tn = "";
		String out_tn_first_three_digits = "";
		
		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			int autoid = rs.getInt("phone_auto_id");
		
			out_tn = rs.getString("phone_value");
			out_tn_first_three_digits = out_tn.substring(0, 3);
					
			fw_set_variable("tnvalue" + index_value, out_tn);
			fw_set_variable("tnvaluenpa" + index_value, out_tn_first_three_digits);
			
			String sql_update = "update tbl_phone set phone_available_flag = 'N', phone_comments = 'updated phone_available flag to N', phone_last_modified_date = sysdate where phone_auto_id = " + autoid;
			
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
			
		}
	
		connection.close();

		log.fw_writeLogEntry("Get TN (Type: " + phone_type + ", Switch Type: " + phone_switch_type + ", Rate Center: " + phone_rate_center + ", Phone Number: " + out_tn + ", Index: " + index_value + ", Team: " + teamvalue + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function gets text from listbox.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 2/14/2018
	 * 
	 */
	
	public String fw_get_text_from_listbox(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
				
		return_get_text = "";
		String text_msg = "";
		String ret_cd = "0";
		String ready_flag = "";
		boolean field_isEmpty;
		boolean field_isEnabled;
		
		try
		{
					
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				for (int m=1;m<10;m++)
				{
					field_isEmpty = driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty();
					field_isEnabled = driver.findElement(fw_get_element_object(locator, locatorvalue)).isEnabled();
					
					if(!field_isEmpty && field_isEnabled)
					{
						ready_flag = "YES";
						
						WebElement element = driver.findElement(fw_get_element_object(locator, locatorvalue));
						Select listbox = new Select(element);
						return_get_text = listbox.getFirstSelectedOption().getText();

						break;
					}
					
					Thread.sleep(1000);
					
				}
			}

			if (ready_flag.equals("YES"))
			{
				ret_cd = "0";
				fw_set_variable("gettextfromlistbox", return_get_text);
			}
			else
			{
				text_msg = "*************** Field NOT Found or Field Disabled ****************";
				ret_cd = "1";
			}
					
			log.fw_writeLogEntry("Get Text from Listbox (Name: " + fieldname + ", Value: " + fieldvalue + ", Text: " + return_get_text + ", Variable File: gettextfromlistbox)" + text_msg, ret_cd);
				
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Get Text from Listbox (Name: " + fieldname + ", Value: " + fieldvalue + ", Text: " + return_get_text + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
		return return_get_text;
		
	}
		
	/**
	 * This function switches to the new window handle which is not in the window handle exclusion list.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 2/13/2018
	 * 
	 */
	
	public void fw_switch_to_new_window_exclusionlist(String window_handles_to_exclude) throws InterruptedException, IOException 
	{
		
		String ret_cd = "1";
		String text_msg = " - did not find a new window";
		
		String[] window_handles_exclusion_list_arr = window_handles_to_exclude.split("---");
        int window_handles_exclusion_list_max = window_handles_exclusion_list_arr.length;
        
        String window_handle_found_in_exclusion_list_flag = "";
        String current_window_handle = "";
        String new_window_handle = "";
        
		for(String winHandle : driver.getWindowHandles())
		{
			window_handle_found_in_exclusion_list_flag = "no";
			
			for (int s=0; s<window_handles_exclusion_list_max; s++)
			{
				current_window_handle = window_handles_exclusion_list_arr[s];
				
				if (current_window_handle.contains("FILE_"))
				{
					String[] current_window_handle_arr = current_window_handle.split("_");
					String current_window_handle_file = current_window_handle_arr[1];
					current_window_handle = fw_get_variable(current_window_handle_file);
				}
				
				if (current_window_handle.equals(winHandle))
			    {
					window_handle_found_in_exclusion_list_flag = "yes";
					break;
			    }
			}
			
			if (window_handle_found_in_exclusion_list_flag.equals("no"))
			{
			
				driver.switchTo().window(winHandle);
				
				ret_cd = "0";
				text_msg = " - Switched to New Window Handle " + winHandle;
				new_window_handle = winHandle;
				
				fw_set_variable("switchtonewwindowexclusionlist", new_window_handle);
				break;
			}
		    
		}			
		
		log.fw_writeLogEntry("Switch to New Window using ExclusionList (Handles to Exclude: " + window_handles_to_exclude + ", New Window Handle: " + new_window_handle + ")" + text_msg, ret_cd);
		
	}
	
	/**
	 *
	 * This function replaces a string with a new string.
	 *   
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @since 2/13/2018
	 */
	
	public void fw_testdata_replace_string(String tctestdata) throws MalformedURLException, IOException, InterruptedException 
	{
		String[] tctestdata_arr = tctestdata.split("---");
		String input_string = tctestdata_arr[0];
		String string_to_replace = tctestdata_arr[1];
		String new_string = tctestdata_arr[2];
		String index_value = tctestdata_arr[3];
		
		if (input_string.contains("FILE_"))
		{
			String[] input_string_arr = input_string.split("_");
			String input_string_file = input_string_arr[1];
			input_string = fw_get_variable(input_string_file);
		}
		
		if (string_to_replace.contains("FILE_"))
		{
			String[] string_to_replace_arr = string_to_replace.split("_");
			String string_to_replace_file = string_to_replace_arr[1];
			string_to_replace = fw_get_variable(string_to_replace_file);
		}
		
		if (new_string.contains("FILE_"))
		{
			String[] new_string_arr = new_string.split("_");
			String new_string_file = new_string_arr[1];
			new_string = fw_get_variable(new_string_file);
		}
		
		String output_string = input_string.replace(string_to_replace, new_string);

		fw_set_variable("replacestringoutput" + index_value, output_string);
		
		log.fw_writeLogEntry("Replace String (Input String: " + input_string + ", String to Replace: " + string_to_replace + ", New String: " + new_string + ", Output String: " + output_string + ")", "0");
		
	}
	
	/**
	 * This function is used to return an account.
	 * 
	 * @param: accountautoid
	 * @since: 2/13/2018
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_return_location(String locationautoid) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
				
		if (locationautoid.contains("FILE_"))
		{
			String[] locationautoid_arr = locationautoid.split("_");
			String locationautoid_file = locationautoid_arr[1];
			locationautoid = fw_get_variable(locationautoid_file);
		}
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		sql_query = "select location_auto_id from tbl_location where location_auto_id = '" + locationautoid + "'";
				
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Location Found in TBL_LOCATION table";
		
		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";

			// Update Available Flag to Y
			
			String fname = fw_get_variable("fname");
			String sql_update = "update tbl_location set location_last_modified_by = '" + fname + "', location_last_modified_date = sysdate, location_available_flag = 'Y' where location_auto_id = " + locationautoid;
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
						
		}
	
		connection.close();

		log.fw_writeLogEntry("Return Location (Auto ID: " + locationautoid + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function is used to get a location.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 1/29/2018
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_get_location(String teamval, String sysval, String prinval, String agentval, String indexval) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		if (teamval.contains("FILE_"))
		{
			String[] teamval_arr = teamval.split("_");
			String teamval_file = teamval_arr[1];
			teamval = fw_get_variable(teamval_file);
		}
		if (sysval.contains("FILE_"))
		{
			String[] sysval_arr = sysval.split("_");
			String sysval_file = sysval_arr[1];
			sysval = fw_get_variable(sysval_file);
		}
		if (prinval.contains("FILE_"))
		{
			String[] prinval_arr = prinval.split("_");
			String prinval_file = prinval_arr[1];
			prinval = fw_get_variable(prinval_file);
		}
		if (agentval.contains("FILE_"))
		{
			String[] agentval_arr = agentval.split("_");
			String agentval_file = agentval_arr[1];
			agentval = fw_get_variable(agentval_file);
		}
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		String s1 = "select location_auto_id, location_id, location_address1, location_address2, location_city, location_state, location_zip from tbl_location ";
		String s2 = "where location_team = '" + teamval + "' ";
		String s6 = "and location_available_flag = 'Y' and rownum = 1";
		
		// Sys
		String sqlsysval = "";
		if (sysval.equals("NULL"))
		{
			sqlsysval = "";
		}
		else
		{
			sqlsysval = "and location_system = '" + sysval + "' ";
		}
		String s3 = sqlsysval;
		
		// Prin
		String sqlprinval = "";
		if (prinval.equals("NULL"))
		{
			sqlprinval = "";
		}
		else
		{
			sqlprinval = "and location_prin = '" + prinval + "' ";
		}
		String s4 = sqlprinval;
		
		// Agent
		String sqlagentval = "";
		if (agentval.equals("NULL"))
		{
			sqlagentval = "";
		}
		else
		{
			sqlagentval = "and location_agent = '" + agentval + "' ";
		}
		String s5 = sqlagentval;
		
		sql_query = s1 + s2 + s3 + s4 + s5 + s6;
				
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Location Found in TBL_LOCATION table"; 
		
		String location_auto_id = "";
		String location_id = "";
		String location_address1 = "";
		String location_address2 = "";
		String location_city = "";
		String location_state = "";
		String location_zip = "";

		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";

			location_auto_id = rs.getString("location_auto_id");
			
			// Update Available Flag to N
			
			String fname = fw_get_variable("fname");
			String sql_update = "update tbl_location set location_last_modified_by = '" + fname + "', location_last_modified_date = sysdate, location_available_flag = 'N' where location_auto_id = " + location_auto_id;
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
			
			// End of Update Available Flag to N
			
			location_id = rs.getString("location_id");
			location_address1 = rs.getString("location_address1");
			location_address2 = rs.getString("location_address2");
			location_city = rs.getString("location_city");
			location_state = rs.getString("location_state");
			location_zip = rs.getString("location_zip");
			
			fw_set_variable("locationautoid" + indexval, location_auto_id);
			fw_set_variable("locationid" + indexval, location_id);
			fw_set_variable("locationaddress1" + indexval, location_address1);
			fw_set_variable("locationaddress2" + indexval, location_address2);
			fw_set_variable("locationcity" + indexval, location_city);
			fw_set_variable("locationstate" + indexval, location_state);
			fw_set_variable("locationzip" + indexval, location_zip);
			
		}
	
		connection.close();

		log.fw_writeLogEntry("Get Location - INPUT Values (Team: " + teamval + ", Sys: " + sysval + ", Prin: " + prinval + ", Agent: " + agentval + ", Index: " + indexval + ")" + text_msg, rc_val);
		log.fw_writeLogEntry("Get Location - OUTPUT Values (Auto ID: " + location_auto_id + ", Location ID: " + location_id + ", Address1: " + location_address1 + ", Address2: " + location_address2 + ", City: " + location_city + ", State: " + location_state + ", Zip: " + location_zip + ")" + text_msg, rc_val);
		log.fw_writeLogEntry("Get Location - OUTPUT Filenames (locationautoid" + indexval + ", locationid" + indexval + ", locationaddress1" + indexval + ", locationaddress2" + indexval + ", locationcity" + indexval + ", locationstate" + indexval + ", locationzip" + indexval + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function validates text is not equal.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 2/1/2018
	 * 
	 */
	
	public void fw_validate_text_not_equal(String fieldname, String string1, String string2) throws InterruptedException, IOException
	{
		
		String ret_cd = "";
		String text_msg = "";
		
		if (string1.contains("FILE_"))
		{
			String[] string1_arr = string1.split("_");
			String string1_file = string1_arr[1];
			string1 = fw_get_variable(string1_file);
		}
		if (string2.contains("FILE_"))
		{
			String[] string2_arr = string2.split("_");
			String string2_file = string2_arr[1];
			string2 = fw_get_variable(string2_file);
		}
		
		if (!string1.equals(string2))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** String1 and String2 are Unexpectedly EQUAL ****************";
			ret_cd = "1";
		}
				
		log.fw_writeLogEntry("Validate Text Not Equal (Name: " + fieldname + ", String1: " + string1 + ", String2: " + string2 + ")" + text_msg, ret_cd);
		
	}
	
	/**
	 * This function is used to return an account.
	 * 
	 * @param: accountautoid
	 * @since: 1/29/2018
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_return_account(String accountautoid) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
				
		if (accountautoid.contains("FILE_"))
		{
			String[] accountautoid_arr = accountautoid.split("_");
			String accountautoid_file = accountautoid_arr[1];
			accountautoid = fw_get_variable(accountautoid_file);
		}
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		sql_query = "select account_auto_id from tbl_account where account_auto_id = '" + accountautoid + "'";
				
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Account Found in TBL_ACCOUNT table";
		
		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";

			// Update Available Flag to Y
			
			String fname = fw_get_variable("fname");
			String sql_update = "update tbl_account set account_last_modified_by = '" + fname + "', account_last_modified_date = sysdate, account_available_flag = 'Y' where account_auto_id = " + accountautoid;
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
						
		}
	
		connection.close();

		log.fw_writeLogEntry("Return Account (Auto ID: " + accountautoid + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function is used to get an account.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 1/29/2018
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_get_account(String teamval, String envval, String sysval, String prinval, String agentval, String ratecenterval, String packageidnameval, String indexval) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
				
		if (teamval.contains("FILE_"))
		{
			String[] teamval_arr = teamval.split("_");
			String teamval_file = teamval_arr[1];
			teamval = fw_get_variable(teamval_file);
		}
		if (envval.contains("FILE_"))
		{
			String[] envval_arr = envval.split("_");
			String envval_file = envval_arr[1];
			envval = fw_get_variable(envval_file);
		}
		if (sysval.contains("FILE_"))
		{
			String[] sysval_arr = sysval.split("_");
			String sysval_file = sysval_arr[1];
			sysval = fw_get_variable(sysval_file);
		}
		if (prinval.contains("FILE_"))
		{
			String[] prinval_arr = prinval.split("_");
			String prinval_file = prinval_arr[1];
			prinval = fw_get_variable(prinval_file);
		}
		if (agentval.contains("FILE_"))
		{
			String[] agentval_arr = agentval.split("_");
			String agentval_file = agentval_arr[1];
			agentval = fw_get_variable(agentval_file);
		}
		if (ratecenterval.contains("FILE_"))
		{
			String[] ratecenterval_arr = ratecenterval.split("_");
			String ratecenterval_file = ratecenterval_arr[1];
			ratecenterval = fw_get_variable(ratecenterval_file);
		}
		if (packageidnameval.contains("FILE_"))
		{
			String[] packageidnameval_arr = packageidnameval.split("_");
			String packageidnameval_file = packageidnameval_arr[1];
			packageidnameval = fw_get_variable(packageidnameval_file);
		}
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		String s1 = "select account_auto_id, account_lob, account_accounttype, account_id, account_locationid, account_orderid, account_customerid, account_phonenumber, account_emailid, account_company, account_productorderid, account_workorderid from tbl_account ";
		String s2 = "where account_team = '" + teamval + "' ";
		String s3 = "and account_env_biller = '" + envval + "' ";
		String s8 = "and account_package = '" + packageidnameval + "' ";
		String s9 = "and account_available_flag = 'Y' and rownum = 1";
		
		// Sys
		String sqlsysval = "";
		if (sysval.equals("NULL"))
		{
			sqlsysval = " is null ";
		}
		else
		{
			sqlsysval = " = '" + sysval + "' ";
		}
		String s4 = "and account_sys" + sqlsysval;
		
		// Prin
		String sqlprinval = "";
		if (prinval.equals("NULL"))
		{
			sqlprinval = " is null ";
		}
		else
		{
			sqlprinval = " = '" + prinval + "' ";
		}
		String s5 = "and account_prin" + sqlprinval;
		
		// Agent
		String sqlagentval = "";
		if (agentval.equals("NULL"))
		{
			sqlagentval = " is null ";
		}
		else
		{
			sqlagentval = " = '" + agentval + "' ";
		}
		String s6 = "and account_agent" + sqlagentval;
		
		// Rate Center
		String sqlratecenterval = "";
		if (ratecenterval.equals("NULL"))
		{
			sqlratecenterval = " is null ";
		}
		else
		{
			sqlratecenterval = " = '" + ratecenterval + "' ";
		}		
		String s7 = "and account_ratecenter" + sqlratecenterval;
		
		sql_query = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8 + s9;
				
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Account Found in TBL_ACCOUNT table";

		String account_auto_id = "";
		String account_lob = "";
		String account_accounttype = "";
		String account_id = "";
		String account_locationid = "";
		String account_orderid = "";
		String account_customerid = "";
		String account_phonenumber = "";
		String account_emailid = "";
		String account_company = "";
		String account_productorderid = "";
		String account_workorderid = "";
		
		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";

			account_auto_id = rs.getString("account_auto_id");
			
			// Update Available Flag to N
			
			String fname = fw_get_variable("fname");
			String sql_update = "update tbl_account set account_last_modified_by = '" + fname + "', account_last_modified_date = sysdate, account_available_flag = 'N' where account_auto_id = " + account_auto_id;
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
			
			// End of Update Available Flag to N
			
			account_lob = rs.getString("account_lob");
			account_accounttype = rs.getString("account_accounttype");
			account_id = rs.getString("account_id");
			account_locationid = rs.getString("account_locationid");
			account_orderid = rs.getString("account_orderid");
			account_customerid = rs.getString("account_customerid");
			account_phonenumber = rs.getString("account_phonenumber");
			account_emailid = rs.getString("account_emailid");
			account_company = rs.getString("account_company");
			account_productorderid = rs.getString("account_productorderid");
			account_workorderid = rs.getString("account_workorderid");

			if (account_lob == null)
			{
				account_lob = "";
			}
			if (account_accounttype == null)
			{
				account_accounttype = "";
			}
			if (account_id == null)
			{
				account_id = "";
			}
			if (account_locationid == null)
			{
				account_locationid = "";
			}
			if (account_orderid == null)
			{
				account_orderid = "";
			}
			if (account_customerid == null)
			{
				account_customerid = "";
			}
			if (account_phonenumber == null)
			{
				account_phonenumber = "";
			}
			if (account_emailid == null)
			{
				account_emailid = "";
			}
			if (account_company == null)
			{
				account_company = "";
			}
			if (account_productorderid == null)
			{
				account_productorderid = "";
			}
			if (account_workorderid == null)
			{
				account_workorderid = "";
			}
			
			fw_set_variable("accountautoid" + indexval, account_auto_id);
			fw_set_variable("accountlob" + indexval, account_lob);
			fw_set_variable("accountaccounttype" + indexval, account_accounttype);
			fw_set_variable("accountid" + indexval, account_id);
			fw_set_variable("accountlocationid" + indexval, account_locationid);
			fw_set_variable("accountorderid" + indexval, account_orderid);
			fw_set_variable("accountcustomerid" + indexval, account_customerid);
			fw_set_variable("accountphonenumber" + indexval, account_phonenumber);
			fw_set_variable("accountemailid" + indexval, account_emailid);
			fw_set_variable("accountcompany" + indexval, account_company);
			fw_set_variable("accountproductorderid" + indexval, account_productorderid);
			fw_set_variable("accountworkorderid" + indexval, account_workorderid);
			
		}
	
		connection.close();

		log.fw_writeLogEntry("Get Account - INPUT Values (Team: " + teamval + ", Env: " + envval + ", Sys: " + sysval + ", Prin: " + prinval + ", Agent: " + agentval + ", Rate Center: " + ratecenterval + ", Package: " + packageidnameval + ", Index: " + indexval + ")" + text_msg, rc_val);
		log.fw_writeLogEntry("Get Account - OUTPUT Values (Auto ID: " + account_auto_id + ", LOB: " + account_lob + ", Account Type: " + account_accounttype + ", Account ID: " + account_id + ", Location ID: " + account_locationid + ", Order ID: " + account_orderid + ", Customer ID: " + account_customerid + ", Phone Number: " + account_phonenumber + ", Email ID: " + account_emailid + ", Company: " + account_company + ", Product Order ID: " + account_productorderid + ", Work Order ID: " + account_workorderid + ")" + text_msg, rc_val);
		log.fw_writeLogEntry("Get Account - OUTPUT Filenames (accountautoid" + indexval + ", accountlob" + indexval + ", accountaccounttype" + indexval + ", accountid" + indexval + ", accountlocationid" + indexval + ", accountorderid" + indexval + ", accountcustomerid" + indexval + ", accountphonenumber" + indexval + ", accountemailid" + indexval + ", accountcompany" + indexval + ", accountproductorderid" + indexval + ", accountworkorderid" + indexval + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This method is used to delete a file
	 * 
	 * @author Mark Elking  
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since 1/25/2018
	 */
	
	public void fw_file_delete(String input_file) throws MalformedURLException, IOException, InterruptedException
	{
		
		String ret_cd = "";
		
		try
		{
			
			if (input_file.contains("FILE_"))
			{
				String[] input_file_arr = input_file.split("_");
				String input_file_file = input_file_arr[1];
				input_file = fw_get_variable(input_file_file);
			}
			
			File f = new File(input_file);
			if(f.exists() && !f.isDirectory()) 
			{ 
			    f.delete();
			}
	        
			ret_cd = "0";
			
	    } 
		catch (Exception e) 
		{
			ret_cd = "1";
			e.printStackTrace();
		}
		
	    log.fw_writeLogEntry("FILE Delete File (Input File: " + input_file + ")", ret_cd);
	    
	}
	
	/**
	 * This method is used to copy a file
	 * 
	 * @author Mark Elking  
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since 1/25/2018
	 */
	
	public void fw_file_copy(String from_file, String to_file) throws MalformedURLException, IOException, InterruptedException
	{

		String ret_cd = "";
		
		try
		{
			
			if (from_file.contains("FILE_"))
			{
				String[] from_file_arr = from_file.split("_");
				String from_file_file = from_file_arr[1];
				from_file = fw_get_variable(from_file_file);
			}
			
			if (to_file.contains("FILE_"))
			{
				String[] to_file_arr = to_file.split("_");
				String to_file_file = to_file_arr[1];
				to_file = fw_get_variable(to_file_file);
			}
	        
			File source = new File(from_file);
	        File dest = new File(to_file);
	        	        
	        FileChannel inputChannel = null;
	        FileChannel outputChannel = null;
	        try 
	        {
	            inputChannel = new FileInputStream(source).getChannel();
	            outputChannel = new FileOutputStream(dest).getChannel();
	            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
	        }
	        finally
	        {
	            inputChannel.close();
	            outputChannel.close();
	        }
	        
			ret_cd = "0";
			
	    } 
		catch (Exception e) 
		{
			ret_cd = "1";
			e.printStackTrace();
		}
		
	    log.fw_writeLogEntry("FILE Copy File (From File: " + from_file + ", To File: " + to_file + ")", ret_cd);
	    
	}
	
	/**
	 * This function is used to calculate a math operation (+,-,*,/)
	 * 
	 * @param: numberlist
	 * @since: 1/26/2018
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_calculate_math(String first_number, String operation, String second_number, String result_type) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{

		String ret_cd = "0";
		String text_msg = "";
		
		if (first_number.contains("FILE_"))
		{
			String[] first_number_arr = first_number.split("_");
			String first_number_file = first_number_arr[1];
			first_number = fw_get_variable(first_number_file);
		}
		
		if (second_number.contains("FILE_"))
		{
			String[] second_number_arr = second_number.split("_");
			String second_number_file = second_number_arr[1];
			second_number = fw_get_variable(second_number_file);
		}
		
		double first_number_double = Double.valueOf(first_number);
		double second_number_double = Double.valueOf(second_number);
		
		double result_value_double = 0;
		
		if (operation.equals("+"))
		{
			result_value_double = first_number_double + second_number_double;
		}
		else if (operation.equals("-"))
		{
			result_value_double = first_number_double - second_number_double;
		}
		else if (operation.equals("/"))
		{
			result_value_double = first_number_double / second_number_double;
		}
		else if (operation.equals("*"))
		{
			result_value_double = first_number_double * second_number_double;
		}
		else
		{
			ret_cd = "1";
			text_msg = " - Operation not valid.";
		}
		
		String result_value = "";
		if (result_type.equals("integer"))
		{
			int x = (int)(result_value_double);
			result_value = String.valueOf(x);
		}
		else
		{
			result_value = String.valueOf(result_value_double);
		}
		
		
		
		fw_set_variable("SKIP--mathresultvalue", result_value);

		log.fw_writeLogEntry("Calculate Math (First Number: " + first_number + ", Operation: " + operation + ", Second Number: " + second_number + ", Result: " + result_value + ")" + text_msg, ret_cd);
		
	}
	
	/**
	 * This method is used to rename a file
	 * 
	 * @author Mark Elking  
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since 1/24/2018
	 */
	
	public void fw_file_rename(String input_file, String new_file) throws MalformedURLException, IOException, InterruptedException
	{

		String ret_cd = "";
		
		try
		{
			
			if (input_file.contains("FILE_"))
			{
				String[] input_file_arr = input_file.split("_");
				String input_file_file = input_file_arr[1];
				input_file = fw_get_variable(input_file_file);
			}
			
			if (new_file.contains("FILE_"))
			{
				String[] new_file_arr = new_file.split("_");
				String new_file_file = new_file_arr[1];
				new_file = fw_get_variable(new_file_file);
			}
			
			new File(input_file).renameTo(new File(new_file));
	        
			ret_cd = "0";
			
	    } 
		catch (Exception e) 
		{
			ret_cd = "1";
			e.printStackTrace();
		}
		
	    log.fw_writeLogEntry("FILE Rename File (Input File: " + input_file + ", New File: " + new_file + ")", ret_cd);
	    
	}
	
	/**
	 * This method is used to get the cell value from an Excel spreadsheet
	 * 
	 * @author Mark Elking  
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since 1/24/2018
	 */
	
	public void fw_get_excel_cell_value(String file_path, String sheet_name, String rownum, String colnum) throws MalformedURLException, IOException, InterruptedException
	{

		String output_value = "";
		String ret_cd = "";
		
		try
		{
			
			if (file_path.contains("FILE_"))
			{
				String[] file_path_arr = file_path.split("_");
				String file_path_file = file_path_arr[1];
				file_path = fw_get_variable(file_path_file);
			}
			
			if (sheet_name.contains("FILE_"))
			{
				String[] sheet_name_arr = sheet_name.split("_");
				String sheet_name_file = sheet_name_arr[1];
				sheet_name = fw_get_variable(sheet_name_file);
			}
			
			int row_num = Integer.valueOf(rownum);
			int col_num = Integer.valueOf(colnum);
	    	     
	    	FileInputStream existingFile = new FileInputStream(new File(file_path));
	    	//XSSFWorkbook workbook = new XSSFWorkbook(existingFile);
	    	HSSFWorkbook workbook = new HSSFWorkbook(existingFile);
	    	HSSFSheet newSheet = workbook.getSheet("Test");

	    	int temp_row_num = row_num - 1;
	    	int temp_col_num = col_num - 1;
	    	
	        Cell newCell = newSheet.getRow(temp_row_num).getCell(temp_col_num);
	        if(newCell == null)
	        {
	        	output_value = "NULL";
	        	ret_cd = "1";
	        }
	        else
	        {
	        	output_value = newCell.toString();
	        	ret_cd = "0";
	        }
	        
	        workbook.close();
	        
	    } 
		catch (Exception e) 
		{
			ret_cd = "1";
			e.printStackTrace();
		}
	    
		fw_set_variable("SKIP--excelcellvalue", output_value);
		
	    log.fw_writeLogEntry("Excel Get Cell Value (Row: " + rownum + ", Column: " + colnum + ", Cell Value: " + output_value + ")", ret_cd);
	    
	}
	
	/**
	 * This method is used to initialize variables
	 * 
	 * @author Mark Elking  
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since 1/17/2018
	 */
	
	//public void fw_initiliaze_variables() throws MalformedURLException, IOException, InterruptedException 
	//{
	//	fw_set_variable("SKIP--ENVTWOWAYSSL","");		
	//}
	
	/**
	 *
	 * This function executes an XML Request using 2 way SSL.
	 *  
	 * @param fileInput
	 * @param endpoint
	 * @param creds
	 * @param in_string
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Rakesh Manandhar
	 * @throws InterruptedException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @since 1/17/2018
	 */

	/*
	public String fw_execute_xml2WaySSL(String fileInput, String endpoint, String creds, String in_string, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException 
	{

		String text_msg = "";
		String ret_cd = "0";
		String outputString = "";
		String xml_request = "";
		String xml_response = "";
		
		if (!fileInput.isEmpty() && !endpoint.isEmpty())
		{
			String workspace_name = fw_get_workspace_name();
			String template_input_file = workspace_name + "\\webservices\\templates\\" + fileInput;
			String responseString = "";			       
	         
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			
			// Get input file and convert to string
			
			String xmlInput = "";				
			FileReader fileReader = null;
			BufferedReader bfrReader = null;
			String strLine = null;
	
			fileReader = new FileReader(template_input_file);
			bfrReader = new BufferedReader(fileReader);
			while ((strLine = bfrReader.readLine()) != null)
			{
				
				String[] in_string_subs_arr = in_string.split("--");
				
				for (int f=0;f<in_string_subs_arr.length;f++)
				{
					String in_string_sub = in_string_subs_arr[f];
					
					String[] in_string_arr = in_string_sub.split(",");
					String in_string_variable_name = in_string_arr[0];
					String in_string_value = in_string_arr[1];
					
					if (strLine.contains(in_string_variable_name))
					{
						int pos_string = strLine.indexOf(in_string_variable_name);
						String first_str = strLine.substring(0, pos_string);
						int len_strLine = strLine.length();
						int len_in_string_variable_name = in_string_variable_name.length();
						int len_first_str = first_str.length();
						int start_pos = len_first_str + len_in_string_variable_name;
						String third_str = strLine.substring(start_pos, len_strLine);
						
						String sub_value = in_string_value;
						
						// Get dynamic value out of the file
						
						if (in_string_value.toUpperCase().contains("FILE"))
						{
							String[] in_string_value_arr = in_string_value.split("_");
							String in_variable_name = in_string_value_arr[1];
							
							String variable_file = workspace_name + "\\variables\\" + in_variable_name;
							String varInput = fw_get_value_from_file(variable_file);
							sub_value = varInput;
						}
						
						// Added on 4/27/2017
						
						if (in_string_value.toUpperCase().contains("FUNCTION_"))
						{
							String[] in_string_value_arr = in_string_value.split("_");
							String in_function_name = in_string_value_arr[1];
							
							if (in_function_name.equals("WSTIMESTAMP"))
							{
								int in_expired_time_in_seconds = Integer.valueOf(in_string_value_arr[2]);
								sub_value = fw_get_webservice_security_header_timestamp(in_expired_time_in_seconds);
							}
							else if (in_function_name.equals("WSUSERTOKEN"))
							{
								String[] creds_arr = creds.split(":");
								String username = creds_arr[0];
								String password = creds_arr[1];
								sub_value = fw_get_webservice_security_header_usertoken(username, password);
							}
							else
							{
								sub_value = "WS_FUNCTION_NOT_FOUND - FUNCTION NAME SHOULD BE WSTIMESTAMP or WSUSERTOKEN";
							}
						}
									
						// End of Get dynamic value out of the file
						
						strLine = first_str + sub_value + third_str;
					}
				
				}
				
				xmlInput = xmlInput + strLine;
				
			}
			if (null != bfrReader)
			{
				bfrReader.close();
			}
			if (fileReader != null)
			{
				fileReader.close();
			}			
			
			// Write runtime request xml into output runtime request file

			String runtime_request_output_file = workspace_name + "\\webservices\\runtime\\requests\\" + fileInput;
			
			try(PrintWriter out = new PrintWriter(runtime_request_output_file))
			{
			    out.println(xmlInput);
			    out.close();
			}
			// End of Write runtime request xml into output runtime request file
	
			xml_request = xmlInput;
			
			// Execute webservice
			
			byte[] buffer = new byte[xmlInput.length()];
			buffer = xmlInput.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();
			
			String variable_file = workspace_name + "\\variables\\webservice_type";
			String webservice_type = fw_get_value_from_file(variable_file);
			
			String clientCert = workspace_name + fw_get_variable("ENVClientCertPath");
			String serverCert = workspace_name + fw_get_variable("ENVserverCertPath");
			String clientCertPasswd = fw_get_variable("ENVClientCertPasswd");
			String serverCertPasswd = fw_get_variable("ENVserverCertPasswd");
			int PortNumber = Integer.parseInt(fw_get_variable("ENVPortNumber"));    		
  		
			AuthSSLProtocolSocketFactory factory = new AuthSSLProtocolSocketFactory(new URL("file:" + clientCert), clientCertPasswd, new URL("file:" + serverCert), serverCertPasswd);
			Protocol authhttps = new Protocol("https", factory, PortNumber);         
	        Protocol.registerProtocol("https", authhttps);
	        HttpClient client = new HttpClient();
	           
	        PostMethod httpget = new PostMethod(endpoint);                        
	        ByteArrayRequestEntity request2 = new ByteArrayRequestEntity(xml_request.getBytes(), "application/xml");            
	        httpget.setRequestEntity(request2);
	        httpget.setRequestHeader("Accept", "application/xml");         
      
          int statusCode = client.executeMethod(httpget);                      
          byte[] responseBody = httpget.getResponseBody();            
          outputString = new String(responseBody);
          System.out.println("StatusCode: " + statusCode);
          System.out.println("responseBody: " + outputString);             
			
	
          // Write runtime request xml into output runtime request file
		
			String runtime_response_output_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
				
			try(PrintWriter outresponse = new PrintWriter(runtime_response_output_file))
			{
				outresponse.println(outputString);
				outresponse.close();
			}
				
			// End of Write runtime request xml into output runtime request file
				
			xml_response = outputString;
				
			ret_cd = "0";			
		}
		else
		{

			if (fileInput.isEmpty())
			{
				text_msg = "*************** fileInput is Not Defined ****************";
				ret_cd = "1";	
			}
			if (endpoint.isEmpty())
			{
				text_msg = "*************** endpoint is Not Defined ****************";
				ret_cd = "1";
			}			
		}		
				
		log.fw_writeLogEntry("Execute XML (Name: " + fileInput + ", Value: " + in_string + ")" + text_msg, ret_cd);
		
		fw_set_variable("NOSUB!!XMLREQUEST", xml_request);
		fw_set_variable("NOSUB!!XMLRESPONSE", xml_response);
		fw_set_variable("NOSUB!!XMLENDPOINT", endpoint);
		
		Thread.sleep(milliseconds_to_wait);		
		return outputString;	
	}
	*/
	
	/**
	 * This method is used to create a connection with Unix box
	 * 
	 * @param hostName host name of Unix server
	 * @param userName This is the user name of Unix machine
	 * @param password This is the login password of Unix machine
	 * @return return the connection object
	 *
	 * @author Mark Elking 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @since 1/11/2017
	 */
	
	public void fw_connect_to_unix(String hostName, String userName, String password) throws InterruptedException, IOException 
	{

		if (hostName.contains("FILE_"))
		{
			String[] hostName_arr = hostName.split("_");
			String hostName_filename = hostName_arr[1];
			hostName = fw_get_variable(hostName_filename);
		}
		
		if (userName.contains("FILE_"))
		{
			String[] userName_arr = userName.split("_");
			String userName_filename = userName_arr[1];
			userName = fw_get_variable(userName_filename);
		}
		
		if (password.contains("FILE_"))
		{
			String[] password_arr = password.split("_");
			String password_filename = password_arr[1];
			password = fw_get_variable(password_filename);
		}
		
		String ret_cd = "";
		
		JSch jsch = new JSch();
		session = null;
		try 
		{
			session = jsch.getSession(userName, hostName);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications",
					"publickey,keyboard-interactive,password");
			session.setConfig(config);
			session.connect();
			
			ret_cd = "0";
			
		} 
		catch (JSchException e) 
		{
			e.printStackTrace();
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Connect to Unix (Host: " + hostName + ", User: " + userName + ")", ret_cd);
		
	}
	
	/**
	 * This method is used to transfer the File from local to Unix Server 
	 * 
	 * sourceFolder This is the path of source folder 
	 * targetFolder This is the path of target folder on unix box
	 * @param fileName  Name of file that needs to transfer from Local to Unix server
	 * @author Mark Elking 
	 * @throws MalformedURLException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws JSchException 
	 * @since 1/11/2017
	 */
	
	public void fw_copy_file_from_windows_to_unix(String sourceFolder, String targetFolder, String fileName) throws MalformedURLException, IOException, InterruptedException 
	{
		
		String ret_cd = "";
		
		if (sourceFolder.contains("FILE_"))
		{
			String[] sourceFolder_arr = sourceFolder.split("_");
			String sourceFolder_filename = sourceFolder_arr[1];
			sourceFolder = fw_get_variable(sourceFolder_filename);
		}
		
		if (targetFolder.contains("FILE_"))
		{
			String[] targetFolder_arr = targetFolder.split("_");
			String targetFolder_fileName = targetFolder_arr[1];
			targetFolder = fw_get_variable(targetFolder_fileName);
		}
		
		if (fileName.contains("FILE_"))
		{
			String[] fileName_arr = fileName.split("_");
			String fileName_filename = fileName_arr[1];
			fileName = fw_get_variable(fileName_filename);
		}
		
		try 
		{

			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;
			String sftpDirectory = targetFolder;
			File directory = new File(sourceFolder);
			File[] fList = directory.listFiles();
			
			for (File file : fList) 
			{
				if (file.isFile() && file.getName().split("\\.")[0].equalsIgnoreCase(fileName)) 
				{
					String filename = file.getAbsolutePath();
					channelSftp.put(filename, sftpDirectory, ChannelSftp.OVERWRITE);
					ret_cd = "0";
				}
			}
			
			channel.disconnect();

		} 
		catch (JSchException e) 
		{
			e.printStackTrace();
			ret_cd = "1";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Copy File from Windows to Unix (Source: " + sourceFolder + ", Target: " + targetFolder + ", fileName: " + fileName + ")", ret_cd);
		
	}
	
	/**
	 * This method is used to execute a command on Unix and return the console output in string
	 * 
	 * @param command This is the command that need to execute on console
	 * @return
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since 01/22/2018
	 */
	 
	public void fw_execute_unix_command(String commandinput) throws MalformedURLException, IOException, InterruptedException
	{

		String ret_cd = "1";
		String readText = "";
		
		if (commandinput.contains("FILE_"))
		{
			String[] commandinput_arr = commandinput.split("_");
			String commandinput_filename = commandinput_arr[1];
			commandinput = fw_get_variable(commandinput_filename);
		}
		
		String[] commandinput_arr = commandinput.split(";");
		String commandinput_firstcommand = commandinput_arr[0];
		
		try 
		{
			Channel channelexec = session.openChannel("exec");
            ((ChannelExec) channelexec).setCommand(commandinput);
            channelexec.setInputStream(null);
            ((ChannelExec) channelexec).setErrStream(System.err);
            InputStream in = channelexec.getInputStream();
            OutputStream out=channelexec.getOutputStream();
        
            if(commandinput_firstcommand.contains("pbrun"))
			{
            	String[] commandinput_arr2 = commandinput_firstcommand.split("--");
        		String commandinput_userid = commandinput_arr2[1];
        		String commandinput_passid = commandinput_arr2[2];
        	
        		String cmd = "pbrun su - " + commandinput_userid;
				((ChannelExec) channelexec).setCommand(cmd);
				 ((ChannelExec)channelexec).setPty(true);			
				
				channelexec.connect();
	            String readPwdData = fw_unix_synchronize(in,"Password:");//first found password prompt then enter password
	            if(readPwdData.contains("Password:"))
	            {
	            	out.write((commandinput_passid + "\r").getBytes());
		            out.flush();
	            }            
	            String remoteUserData = fw_unix_synchronize(in, commandinput_userid + "@");//first found expected prompt then execute further command
	            
	            String temp_command = "";
	            String[] commandinput_final_arr = commandinput.split(";");
	            int commandinput_final_max = commandinput_final_arr.length;
	            for (int s=1; s<commandinput_final_max; s++)
	    		{
	            	temp_command = temp_command + commandinput_final_arr[s] + ";";	
	    		}
	            int temp_command_length = temp_command.length();
	            commandinput = temp_command.substring(0, temp_command_length-1);
	            System.out.println(commandinput);
	            
	            if(remoteUserData.contains(commandinput_userid + "@"))
	            {
		            out.write((commandinput + "\n").getBytes());
		            out.flush(); 
	            }
			}

            ((ChannelExec) channelexec).setCommand(commandinput);
			channelexec.connect();
			
			Thread.sleep(3000);
			
			byte[] tmp = new byte[1024];
            while (in.available() > 0) 
            {
                int i = in.read(tmp, 0, 1024);
                readText = new String(tmp, 0, i);
                ret_cd = "0";
            }
            channelexec.disconnect();
        	
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		fw_set_variable("SKIP--UnixExecuteCommandResult", readText);
        
		log.fw_writeLogEntry("Unix Execute Command (Command: " + commandinput + ", Result: " + readText + ")", ret_cd);
		
	}
	
	
	/**
	 * This method is used to read the input stream and validate the pattern on console
	 * @param in This the the variable of Input stream
	 * @param pattern This is the variable for pattern that need to validate on console
	 * @return
	 * @author Mark Elking
	 * @since 01/22/2018
	 */
	
	public String fw_unix_synchronize(InputStream in , String pattern)
	{
		StringBuffer buffer = new StringBuffer();
        try
        {
            synchronized (in) 
            {
                byte[] buf = new byte[2048];
                DataInputStream dataInputStream = new DataInputStream(in);
                dataInputStream.read(buf);
                String bufferData = new String(buf).trim();

                if(!bufferData.equalsIgnoreCase("null"))
                {
            		buffer.append(bufferData);
                    if(buffer.toString().contains(pattern))
                    {
                    	return buffer.toString();
                    }
                    else
                    {
                        return fw_unix_synchronize( in , pattern); //recursion till response not found;);
                    }
                }                   
            }
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
        
        return null;
        
    }
	
	/**
	 * This method is used to disconnect unix channel and session
	 *
	 * @param commandinput
	 * @param expectedString
	 * @return
	 * @author Mark Elking 
	 * @throws MalformedURLException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @since 1/15/2017
	 */
	 
	public void fw_unix_disconnect() throws InterruptedException, IOException
	{
		
		String ret_cd = "";
		
		try 
		{
            session.disconnect();
            ret_cd = "0";
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		log.fw_writeLogEntry("Disconnect from Unix", ret_cd);
		
	}
			
	/**
	 * This function selects a value from a list by index.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/3/2017
	 * 
	 */
	
	public void fw_select_from_a_list_by_index(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
				
		String text_msg = "";
		String ret_cd = "0";
		
		try
		{
			
			String found_flag = "";
			
			if (fieldvalue.contains("FILE_"))
			{
				String[] tc_test_data_array = fieldvalue.split("_");
				String tc_test_data_filename = tc_test_data_array[1];
				fieldvalue = fw_get_variable(tc_test_data_filename);
			}
			
			int fieldvalueint = Integer.valueOf(fieldvalue);
			
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
				{
					found_flag = "YES";
					WebElement element = driver.findElement(fw_get_element_object(locator, locatorvalue));
					Select listbox = new Select(element);
			        //listbox.selectByValue(fieldvalue);
					listbox.selectByIndex(fieldvalueint);
				}
			}
			else 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getAttribute(locator);
					
					if (label.contains(locatorvalue))
					{
						found_flag = "YES";
						WebElement element = item;
						Select listbox = new Select(element);
				        //listbox.selectByValue(fieldvalue);
				        listbox.selectByIndex(fieldvalueint);
						break;
					}
				}
			}
			
			if (found_flag.equals("YES"))
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** Listbox NOT Found ****************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Select from a List By Index (Name: " + fieldname + ", Index: " + fieldvalue + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Select from a List By Value (Name: " + fieldname + ", Value: " + fieldvalue + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
	}
	
	/**
	 * This function is used to validate number in a range.
	 * 
	 * @param: input_value
	 * @since: 1/2/2018
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_validate_number_in_range(String input_value) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String ret_cd = "";
		String msg_text = "";
		
		String[] input_value_arr = input_value.split(",");
		int input_value_args = input_value_arr.length;
		
		String value_to_validate = "";
		String lower_value = "";
		String upper_value = "";
		
		if (input_value_args == 3)
		{
			value_to_validate = input_value_arr[0];
			if (value_to_validate.contains("FILE_"))
			{
				String[] value_to_validate_arr = value_to_validate.split("_");
				value_to_validate = fw_get_variable(value_to_validate_arr[1].trim());
			}
			
			lower_value = input_value_arr[1];
			if (lower_value.contains("FILE_"))
			{
				String[] lower_value_arr = lower_value.split("_");
				lower_value = fw_get_variable(lower_value_arr[1].trim());
			}
			
			upper_value = input_value_arr[2];
			if (upper_value.contains("FILE_"))
			{
				String[] upper_value_arr = upper_value.split("_");
				upper_value = fw_get_variable(upper_value_arr[1].trim());
			}
		
			double temp_value_to_validate = Double.valueOf(value_to_validate);
			double temp_lower_value = Double.valueOf(lower_value);
			double temp_upper_value = Double.valueOf(upper_value);
			
			if (temp_value_to_validate == temp_lower_value || temp_value_to_validate == temp_upper_value)
			{
				ret_cd = "0";
				msg_text = "In range value equal to one of the boundaries";
			}
			else if (temp_value_to_validate > temp_lower_value && temp_value_to_validate < temp_upper_value)
			{
				ret_cd = "0";
				msg_text = "In range";
			}
			else
			{
				ret_cd = "1";
				msg_text = "Out of range";
			}
		}
		else
		{
			ret_cd = "1";
			msg_text = "Number of inputs is not correct.  Review Event description and try again.";
		}
		
		fw_set_variable("SKIP--validatenumberinrangeoutputstatus", msg_text);

		log.fw_writeLogEntry("Validate Number in Range (Input: " + input_value + ", Output Status: " + msg_text + ", Output Status Variable file: validatenumberinrangeoutputstatus)", ret_cd);
		
	}
	
	/**
	 * This function is used to get sum total of list of numbers.
	 * 
	 * @param: numberlist
	 * @since: 1/2/2018
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_getsum_numbers(String numberlist) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String number_current = "";
		//int final_number_current = 0;
		double final_number_current = 0;
		
		String[] numberlist_arr = numberlist.split(",");
		int numberlist_len = numberlist_arr.length;
		
		for (int s=0; s<numberlist_len; s++)
		{
			number_current = numberlist_arr[s];
			if (number_current.contains("FILE_"))
			{
				String[] number_current_arr = number_current.split("_");
				number_current = fw_get_variable(number_current_arr[1].trim());
			}
			
			//int temp_number_current = Integer.valueOf(number_current);
			double temp_number_current = Double.valueOf(number_current);
			final_number_current = final_number_current + temp_number_current;	
		}
		
		String finalnumber = String.valueOf(final_number_current);
		
		fw_set_variable("totalsumofnumbers", finalnumber);

		log.fw_writeLogEntry("Get Sum of Numbers (Number List Input: " + numberlist + ", Calculated Total Sum: " + finalnumber + ")", "0");
		
	}
	
	/**
	 * 
	 * This function clicks the browser refresh button.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 12/19/2017
	 * 
	 */
	
	public void fw_browser_navigate_refresh() throws InterruptedException, IOException
	{
		
		driver.navigate().refresh();
		
		log.fw_writeLogEntry("Click Browser Refresh Button", "0");
		
	}
	
	/**
	 * 
	 * This function closes the driver.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 12/19/2017
	 * 
	 */
	
	public void fw_close_driver() throws InterruptedException, IOException
	{
		
		driver.close();
		
		log.fw_writeLogEntry("Close Driver", "0");
		
	}
	
	/**
	 * This function is used to write an account record to database.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 12/12/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_write_account_database() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		String Requestor = fw_get_variable("LASTMODIFIEDBY");
		String RequestID = fw_get_variable("RequestID");
		String TEAM = fw_get_variable("TEAM");
		String ENV = fw_get_variable("ENV");
		String PackageIDName = fw_get_variable("PackageID") + " - " + fw_get_variable("PackageName");
		String LOB = fw_get_variable("LOB");
		String AccountType = fw_get_variable("AccountType");
	
		String ACCOUNTID = fw_get_variable("ACCOUNTID");
		String LOCATIONID = fw_get_variable("LOCATIONID");
		String ORDERID = fw_get_variable("ORDERID");
		String CUSTOMERID = fw_get_variable("CUSTOMERID");
		String PHONENUMBER = fw_get_variable("PHONENUMBER");
		String EMAILID = fw_get_variable("EMAILID");
		String COMPANYNAME = fw_get_variable("COMPANYNAME");
		String lanoutputlogfile = fw_get_variable("lanoutputlogfile");
		
		// Added by Mark on 1/29/2018
		String Sys = fw_get_variable("Sys");
		String Prin = fw_get_variable("Prin");
		String Agent = fw_get_variable("Agent");
		String RateCenter = fw_get_variable("RateCenter");
		String PRODUCTORDERID = fw_get_variable("PRODUCTORDERID");
		String WORKORDERID = fw_get_variable("WORKORDERID");
		// End of Added by Mark on 1/29/2018
		
		//String sql_query = "INSERT INTO tbl_account (ACCOUNT_AUTO_ID, ACCOUNT_AVAILABLE_FLAG, ACCOUNT_LAST_MODIFIED_DATE, ACCOUNT_LAST_MODIFIED_BY, ACCOUNT_COMMENTS, ACCOUNT_DATE_CREATED, ACCOUNT_REQUESTID, ACCOUNT_TEAM, ACCOUNT_ENV_BILLER, ACCOUNT_PACKAGE, ACCOUNT_LOB, ACCOUNT_ACCOUNTTYPE, ACCOUNT_ID, ACCOUNT_LOCATIONID, ACCOUNT_ORDERID, ACCOUNT_CUSTOMERID, ACCOUNT_PHONENUMBER, ACCOUNT_EMAILID, ACCOUNT_COMPANY, ACCOUNT_LOG_FILE) VALUES (seq_account_id.nextval, 'Y', sysdate, '" + Requestor + "', 'New Account created', sysdate, '" + RequestID + "', '" + TEAM + "', '" + ENV + "', '" + PackageIDName + "', '" + LOB + "', '" + AccountType + "', '" + ACCOUNTID + "', '" + LOCATIONID + "', '" + ORDERID + "', '" + CUSTOMERID + "', '" + PHONENUMBER + "', '" + EMAILID + "', '" + COMPANYNAME + "', '" + lanoutputlogfile + "')";
		String sql_query = "INSERT INTO tbl_account (ACCOUNT_AUTO_ID, ACCOUNT_AVAILABLE_FLAG, ACCOUNT_LAST_MODIFIED_DATE, ACCOUNT_LAST_MODIFIED_BY, ACCOUNT_COMMENTS, ACCOUNT_DATE_CREATED, ACCOUNT_REQUESTID, ACCOUNT_TEAM, ACCOUNT_ENV_BILLER, ACCOUNT_PACKAGE, ACCOUNT_LOB, ACCOUNT_ACCOUNTTYPE, ACCOUNT_ID, ACCOUNT_LOCATIONID, ACCOUNT_ORDERID, ACCOUNT_CUSTOMERID, ACCOUNT_PHONENUMBER, ACCOUNT_EMAILID, ACCOUNT_COMPANY, ACCOUNT_LOG_FILE, ACCOUNT_SYS, ACCOUNT_PRIN, ACCOUNT_AGENT, ACCOUNT_RATECENTER, ACCOUNT_PRODUCTORDERID, ACCOUNT_WORKORDERID) VALUES (seq_account_id.nextval, 'Y', sysdate, '" + Requestor + "', 'New Account created', sysdate, '" + RequestID + "', '" + TEAM + "', '" + ENV + "', '" + PackageIDName + "', '" + LOB + "', '" + AccountType + "', '" + ACCOUNTID + "', '" + LOCATIONID + "', '" + ORDERID + "', '" + CUSTOMERID + "', '" + PHONENUMBER + "', '" + EMAILID + "', '" + COMPANYNAME + "', '" + lanoutputlogfile + "', '" + Sys + "', '" + Prin + "', '" + Agent + "', '" + RateCenter + "', '" + PRODUCTORDERID + "', '" + WORKORDERID + "')";

		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		connection.close();
		
	}
	
	/**
	 * This function loops until element is not displayed.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 11/12/2017
	 * 
	 */
	
	public Boolean fw_element_is_not_displayed(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{

		String text_msg = "";
		String ret_cd = "";
		boolean element_is_not_displayed = true;
		boolean element_is_displayed;
		int num_loops = 1;
		long wait_time_per_loop = 1000;
				
		try
		{
			
			String[] fieldvalue_arr = fieldvalue.split(",");
			num_loops = Integer.valueOf(fieldvalue_arr[0]);
			wait_time_per_loop = Long.valueOf(fieldvalue_arr[1]);
			
			System.out.println("num_loops: " + fieldvalue_arr[0].trim());
			System.out.println("wait_time_per_loop: " + fieldvalue_arr[1].trim());				
			
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				for (int m=1;m<num_loops;m++)
				{
					
					try
					{
						element_is_displayed = driver.findElement(fw_get_element_object(locator, locatorvalue)).isDisplayed();
						
						if (element_is_displayed == true)
						{
							Thread.sleep(wait_time_per_loop);
						}
						else
						{
							System.out.println("BREAKING OUT OF LOOP");
							element_is_not_displayed = true;
							break;
						}
						
					}
					catch (Exception e) 
					{
						element_is_displayed = false;
						System.out.println("Element is Displayed, Inside Loop: " + m + ".  Continuing loop....");
					}
					
				}
			}
			
			if (element_is_not_displayed == true)
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** Field NOT Found or Field Displayed ****************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Check Element Is Not Displayed (Name: " + fieldname + ", Value: " + fieldvalue + ", Element Is Not Displayed? " + element_is_not_displayed + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Check Element Is Not Displayed (Name: " + fieldname + ", Value: " + fieldvalue + ", Element Is Not Displayed? " + element_is_not_displayed + ")" + text_msg + " - " + exception_string, ret_cd);
		}
		
		return element_is_not_displayed;
			
	}
	
	/**
	 * This function takes a screen shot whenever a failure is encountered during execution.
	 * 
	 * @since: 11/2/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	
	public void fw_take_screenshot() 
	{
		
		try
		{
			String workspace_name = fw_get_workspace_name();
			String screenshot_path = workspace_name + "\\screenshots\\";
			String variables_path = workspace_name + "\\variables\\";
			String alm_test_id_value = fw_get_value_from_file(variables_path + "ALMTESTID");
			String first_name = fw_get_value_from_file(variables_path + "fname");
			
			dt.fw_generate_datetime("yyyyMMddkkmmss");
			String date_and_time = dt.generated_date_time_output;
			
			String filenam = "SCREENSHOT - " + date_and_time + " - TC" + alm_test_id_value + " - " + first_name + ".png";
			String fileName = screenshot_path + filenam;
			
			if (driver != null)
	        {
	        
				WebDriver augmentedDriver = new Augmenter().augment(driver);
		        File screenshot = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
		        FileUtils.copyFile(screenshot, new File(fileName));
		        
		        // Copy screenshot to the LAN
		        
		        File file1 =new File(fileName);
			    File file2 =new File("X:\\Test Utility\\ScreenShots\\" + filenam);
			   
				try 
				{
					fileCopy(file1, file2);
										
					// Update TBL_TCLOGGING with Screen Shot info
					
					String logoutputfile = fw_get_variable("OUTPUTLOGFILE");
					
					String userName = fw_get_variable("usernameTESTDB");
					String password = fw_get_variable("passwordTESTDB");
					String connection_string = fw_get_variable("connectionstringTESTDB");
					
					Connection connection = null;
					Class.forName("oracle.jdbc.driver.OracleDriver");
					connection = DriverManager.getConnection(connection_string, userName, password);
					
					String sql_query = "select count(*) cntval from tbl_tclogging where logging_outputfile = '" + logoutputfile + "'";
					
					ResultSet rs = null;
					Statement st = connection.createStatement();
					rs = st.executeQuery(sql_query);
					
					int out_count = 0;
					
					if (rs.next())
					{
						out_count = rs.getInt("cntval");
					}
					
					if (out_count != 0)
					{
						sql_query = "update tbl_tclogging set LOGGING_SCREENSHOT = '" + filenam + "' where LOGGING_OUTPUTFILE = '" + logoutputfile + "'";
					
						ResultSet rs2 = null;
						Statement st2 = connection.createStatement();
						rs2 = st2.executeQuery(sql_query);
						
						connection.close();
					}
					
					// End of Update TBL_TCLOGGING with Screen Shot info
					
				} catch (IOException e) 
				{
					e.printStackTrace();
					
					System.out.println(" ");
					System.out.println(" ***** Screenshot NOT copied because FILE " + file1 + " NOT found ****** ");
					System.out.println(" ");
				}
				
	        }
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This function returns the css value for a given css name for a given element.
	 *
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 11/2/2017
	 * 
	 */
	
	public String fw_get_css_value(String locator, String locatorvalue, String css_name, long milliseconds_to_wait) throws InterruptedException, IOException 
	{

		String css_value = "";
		
		try
		{
			
			if (locator.equals("name")) 
			{
				css_value = driver.findElement(By.name(locatorvalue)).getCssValue(css_name);
			}
			else if (locator.equals("id")) 
			{
				css_value = driver.findElement(By.id(locatorvalue)).getCssValue(css_name);
			}
			else if (locator.equals("class")) 
			{
				css_value = driver.findElement(By.className(locatorvalue)).getCssValue(css_name);
			}
			else if (locator.equals("css")) 
			{
				css_value = driver.findElement(By.cssSelector(locatorvalue)).getCssValue(css_name);
			}
			else if (locator.equals("xpath")) 
			{
				css_value = driver.findElement(By.xpath(locatorvalue)).getCssValue(css_name);
			}
			
			fw_set_variable("cssvalue", css_value);
			
			log.fw_writeLogEntry("     Get CSS Value (Locator: " + locator + ", Locator Value: " + locatorvalue + ", CSS Name: " + css_name + ", CSS Value: " + css_value + ")", "NA");
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			String text_msg = "*************** EXCEPTION ****************";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("     Get CSS Value (Locator: " + locator + ", Locator Value: " + locatorvalue + ", CSS Name: " + css_name + ", Attribute Value: " + css_value + ") - " + text_msg + " - " + exception_string, "NA");
		}
		
		return css_value;
		
	}
	
	/**
	 * This function is used to get next available address.
	 * 
	 * @since: 10/30/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_get_address(String state_value, String environment_value) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		if (state_value.contains("FILE_"))
		{
			String[] state_value_array = state_value.split("_");
			String state_value_file = state_value_array[1];			
			state_value = fw_get_variable(state_value_file);
		}
		
		if (environment_value.contains("FILE_"))
		{
			String[] environment_value_array = environment_value.split("_");
			String environment_value_file = environment_value_array[1];			
			environment_value = fw_get_variable(environment_value_file);
		}
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		String appname = fw_get_variable("appname");
		
		//sql_query = "select addr_auto_id, addr_address, addr_state, addr_environment, addr_available_flag, addr_used_cnt, addr_last_log_file, to_char(addr_LAST_MODIFIED_DATE,'YYYYMMDD-HH24-MI-SS') lastmoddate, addr_last_modified_by from tbl_address WHERE addr_last_modified_date in (select min(addr_last_modified_date) from tbl_address where addr_available_flag = 'Y' and addr_state = '" + state_value + "' and addr_environment = '" + environment_value + "') and rownum = 1";
		//sql_query = "select addr_auto_id, addr_address, addr_state, addr_environment, addr_available_flag, addr_used_cnt, addr_last_log_file, to_char(addr_LAST_MODIFIED_DATE,'YYYYMMDD-HH24-MI-SS') lastmoddate, addr_last_modified_by from tbl_address where addr_available_flag = 'Y' and addr_state = '" + state_value + "' and addr_environment = '" + environment_value + "' and rownum = 1 and to_char(addr_LAST_MODIFIED_DATE,'YYYYMMDD-HH24-MI-SS') in (select min(to_char(addr_LAST_MODIFIED_DATE,'YYYYMMDD-HH24-MI-SS')) from tbl_address where addr_available_flag = 'Y' and addr_state = '" + state_value + "' and addr_environment = '" + environment_value + "')";
		sql_query = "select addr_city, addr_zip, addr_auto_id, addr_address, addr_state, addr_environment, addr_available_flag, addr_used_cnt, addr_last_log_file, to_char(addr_LAST_MODIFIED_DATE,'YYYYMMDD-HH24-MI-SS') lastmoddate, addr_last_modified_by from tbl_address where addr_available_flag = 'Y' and addr_application = '" + appname + "' and addr_state = '" + state_value + "' and addr_environment = '" + environment_value + "' and rownum = 1 and to_char(addr_LAST_MODIFIED_DATE,'YYYYMMDD-HH24-MI-SS') in (select min(to_char(addr_LAST_MODIFIED_DATE,'YYYYMMDD-HH24-MI-SS')) from tbl_address where addr_available_flag = 'Y' and addr_application = '" + appname + "' and addr_state = '" + state_value + "' and addr_environment = '" + environment_value + "')";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String addr_auto_id = "";
		String addr_address = "";
		String addr_used_cnt = "";
		String addr_city = "";
		String addr_zip = "";
		
		if (rs.next())
		{
			String firstname = fw_get_variable("fname");
			
			addr_auto_id = rs.getString("addr_auto_id");
			addr_address = rs.getString("addr_address");
			addr_used_cnt = rs.getString("addr_used_cnt");
			addr_city = rs.getString("addr_city");
			addr_zip = rs.getString("addr_zip");
			
			if (addr_address == null)
			{
				addr_address = "";
			}
			
			if (addr_city == null)
			{
				addr_city = "";
			}
			
			if (addr_zip == null)
			{
				addr_zip = "";
			}
			
			int temp_addr_used_cnt = Integer.valueOf(addr_used_cnt);
			temp_addr_used_cnt = temp_addr_used_cnt + 1;
			String final_addr_used_cnt = String.valueOf(temp_addr_used_cnt);
			
			fw_set_variable("addraddress", addr_address);
			fw_set_variable("addrcity", addr_city);
			fw_set_variable("addrzip", addr_zip);
			
			String sql_update = "update tbl_address set addr_last_modified_by = '" + firstname + "', addr_used_cnt = '" + final_addr_used_cnt + "', addr_last_modified_date = sysdate where addr_auto_id = " + addr_auto_id;
			
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
			
		}
	
		connection.close();

		log.fw_writeLogEntry("Get Address (Address: " + addr_address + ", City: " + addr_city + ", Zip: " + addr_zip + ", State: " + state_value + ", Environment: " + environment_value + ")","0");
		
	}
	
	/**
	 * This function is used to write a video device record to Device database.
	 * 
	 * @since: 10/20/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	
	public void fw_write_videodevice_database() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String TEAM = fw_get_variable("TEAM");
		String DeviceVersion = fw_get_variable("DeviceVersion");  //DEVICE_VERSION
		String RunMode = fw_get_variable("RunMode");
		
		boolean cond1 = TEAM.toUpperCase().equals("AUTOMATION");
		boolean cond2 = DeviceVersion.equals("1.0") || DeviceVersion.equals("LEGACY");
		boolean cond3 = DeviceVersion.equals("2.0") && RunMode.equals("CREATE");
		boolean cond4 = DeviceVersion.equals("2.0") && RunMode.equals("CLEANUP");
		String cond5 = fw_get_variable("VDOVERALLSTATUS");
		
		if (cond1 && (cond2 || cond3))
		{
			String userName = fw_get_variable("usernameTESTDB");
			String password = fw_get_variable("passwordTESTDB");
			String connection_string = fw_get_variable("connectionstringTESTDB");
			
			Connection connection = null;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(connection_string, userName, password);
			
			String CreatedBy = fw_get_variable("CreatedBy");  //DEVICE_LAST_MODIFIED_BY
			String DeviceType = fw_get_variable("DeviceType"); //DEVICE_TYPE
			String UIMEnv = fw_get_variable("UIMEnv");  //DEVICE_ENVIRONMENT
			String SerialNum = fw_get_variable("SerialNum"); //DEVICE_VIDEO_SERIAL_NUMBER
			String eSTBMAC = fw_get_variable("eSTBMAC"); //DEVICE_VIDEO_ESTBMAC
			String eCMMAC = fw_get_variable("eCMMAC"); //DEVICE_VIDEO_ECMMAC

			String sql_query = "INSERT INTO tbl_device (DEVICE_AUTO_ID, DEVICE_AVAILABLE_FLAG, DEVICE_LAST_MODIFIED_DATE, DEVICE_COMMENTS, DEVICE_REAL_FAKE_FLAG, DEVICE_VERSION, DEVICE_LAST_MODIFIED_BY, DEVICE_TYPE, DEVICE_ENVIRONMENT, DEVICE_VIDEO_SERIAL_NUMBER, DEVICE_VIDEO_ESTBMAC, DEVICE_VIDEO_ECMMAC) VALUES (seq_device_id.nextval, 'Y', sysdate, 'Device Created', 'F', '" + DeviceVersion + "', '" + CreatedBy + "', '" + DeviceType + "', '" + UIMEnv + "', '" + SerialNum + "', '" + eSTBMAC + "', '" + eCMMAC + "')";

			ResultSet rs = null;
			Statement st = connection.createStatement();
			rs = st.executeQuery(sql_query);
			
			connection.close();
		}
		else if (cond1 && cond4 && cond5.equals("GOOD"))
		{
			String userName = fw_get_variable("usernameTESTDB");
			String password = fw_get_variable("passwordTESTDB");
			String connection_string = fw_get_variable("connectionstringTESTDB");
			
			Connection connection = null;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(connection_string, userName, password);
			
			String DeviceType = fw_get_variable("DeviceType"); //DEVICE_TYPE
			String UIMEnv = fw_get_variable("UIMEnv");  //DEVICE_ENVIRONMENT
			String SerialNum = fw_get_variable("SerialNum"); //DEVICE_VIDEO_SERIAL_NUMBER
			String eSTBMAC = fw_get_variable("eSTBMAC"); //DEVICE_VIDEO_ESTBMAC
			String eCMMAC = fw_get_variable("eCMMAC"); //DEVICE_VIDEO_ECMMAC
			String CreatedBy = fw_get_variable("CreatedBy"); //DEVICE_LAST_MODIFIED_BY
			
			String sql_query = "update tbl_device set device_available_flag = 'Y', device_last_modified_date = sysdate, device_last_modified_by = '" + CreatedBy + "' where DEVICE_TYPE = '" + DeviceType + "' and DEVICE_ENVIRONMENT = '" + UIMEnv + "' and DEVICE_VIDEO_SERIAL_NUMBER = '" + SerialNum + "' and DEVICE_VIDEO_ESTBMAC = '" + eSTBMAC + "' and DEVICE_VIDEO_ECMMAC = '" + eCMMAC + "'";			
			
			ResultSet rs = null;
			Statement st = connection.createStatement();
			rs = st.executeQuery(sql_query);
			
			connection.close();
		}
		
	}
	
	/**
	 * This function is used to write a video device record to database.
	 * 
	 * @since: 10/18/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	
	public void fw_write_videohist_database() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
				
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		String csg_device_status = fw_get_variable("HLCSGSTATUS"); //VHIST_CSG_STATUS
		String uim_device_status = fw_get_variable("HLUIMSTATUS"); //VHIST_UIM_STATUS
		String VHIST_CSG_STATUS_DTL = fw_get_variable("CSGDETAILSTATUS"); //VHIST_CSG_STATUS_DTL
		String VHIST_UIM_STATUS_DTL = fw_get_variable("UIMDETAILSTATUS"); //VHIST_UIM_STATUS_DTL
		String VHIST_WORKSTATION = fw_get_variable("hostname");
		String VHIST_OVERALL_STATUS = fw_get_variable("VDOVERALLSTATUS");
		String CreatedBy = fw_get_variable("CreatedBy");  //VHIST_LAST_MODIFIED_BY
		String RequestID = fw_get_variable("RequestID");  //VHIST_REQUEST_ID
		String DeviceVersion = fw_get_variable("DeviceVersion");  //VHIST_DEVICE_VERSION
		String DeviceType = fw_get_variable("DeviceType"); //VHIST_DEVICE_TYPE
		String RunMode = fw_get_variable("RunMode"); //VHIST_RUN_MODE
		String UIMEnv = fw_get_variable("UIMEnv");  //VHIST_UIM_ENVIRONMENT
		String SerialNum = fw_get_variable("SerialNum"); //VHIST_CSG_SERIALNUMBER
		String eSTBMAC = fw_get_variable("eSTBMAC"); //VHIST_CSG_ESTBMAC
		String eCMMAC = fw_get_variable("eCMMAC"); //VHIST_CSG_ECMMAC
		String lanoutputlogfile = fw_get_variable("lanoutputlogfile");  //VHIST_OUTPUT_LOG_FILE
		String TEAM = fw_get_variable("TEAM");  //VHIST_TEAM
		
		String sql_query = "INSERT INTO tbl_videohist (VHIST_AUTO_ID, VHIST_LAST_MODIFIED_DATE, VHIST_OVERALL_STATUS, VHIST_WORKSTATION, VHIST_CSG_STATUS_DTL, VHIST_UIM_STATUS_DTL, VHIST_LAST_MODIFIED_BY, VHIST_REQUEST_ID, VHIST_DEVICE_VERSION, VHIST_DEVICE_TYPE, VHIST_RUN_MODE, VHIST_UIM_ENVIRONMENT, VHIST_CSG_SERIALNUMBER, VHIST_CSG_ESTBMAC, VHIST_CSG_ECMMAC, VHIST_CSG_STATUS, VHIST_UIM_STATUS, VHIST_OUTPUT_LOG_FILE, VHIST_TEAM) VALUES (seq_video_id.nextval, sysdate, '" + VHIST_OVERALL_STATUS + "', '" + VHIST_WORKSTATION + "', '" + VHIST_CSG_STATUS_DTL + "', '" + VHIST_UIM_STATUS_DTL + "', '" + CreatedBy + "', '" + RequestID + "', '" + DeviceVersion + "','" + DeviceType + "', '" + RunMode + "', '" + UIMEnv + "', '" + SerialNum + "', '" + eSTBMAC + "', '" + eCMMAC + "', '" + csg_device_status + "', '" + uim_device_status + "', '" + lanoutputlogfile + "', '" + TEAM + "')";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		connection.close();
		
	}
	
	/**
	 *
	 * This function replaces a string in a variable file or xml response file with a new string.
	 * Created for Gaurav for Neustar string substitution, but he doesn't need anymore.
	 *   
	 * @param file_type
	 * @param file_name
	 * @param string_to_replace
	 * @param new_string
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @since 10/11/2017
	 */
	
	public void fw_replace_string(String file_type, String file_name, String string_to_replace, String new_string) throws MalformedURLException, IOException, InterruptedException 
	{
		
		if (!file_type.toUpperCase().equals("XML") && !file_type.toUpperCase().equals("VAR"))
		{
			
		}
		else
		{
			
			String relative_dir_path = "";
			
			if (file_type.toUpperCase().equals("XML"))
			{
				relative_dir_path = "\\webservices\\runtime\\responses\\";
			}
			else if (file_type.toUpperCase().equals("VAR"))
			{
				relative_dir_path = "\\variables\\";
			}
			
			String workspace_name = fw_get_workspace_name();
			
			String full_path = workspace_name + relative_dir_path + file_name;	
			
			String new_value = "";				
			FileReader fileReader = null;
			BufferedReader bfrReader = null;
			String strLine = null;
			
			fileReader = new FileReader(full_path);
			bfrReader = new BufferedReader(fileReader);
			
			while ((strLine = bfrReader.readLine()) != null)
			{	
				new_value = new_value + strLine;
			}
			if (null != bfrReader)
			{
				bfrReader.close();
			}
			if (fileReader != null)
			{
				fileReader.close();
			}		
		
			String[] string_to_replace_arr = string_to_replace.split("---");
			String[] new_string_arr = new_string.split("---");
			
			int string_to_replace_len = string_to_replace_arr.length;
			
			String string_to_replace_current = "";
			String new_string_current = "";
			
			for (int s=0; s<string_to_replace_len; s++)
			{
				string_to_replace_current = string_to_replace_arr[s];
				new_string_current = new_string_arr[s];
				new_value = new_value.replace(string_to_replace_current, new_string_current);
			}
			
			try(PrintWriter out = new PrintWriter(full_path))
			{
			    out.println(new_value);
			    out.close();
			}
			
		}
		
	}
	
	/**
	 * This function hits alt+key.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 9/22/2017 
	 */

	public void fw_robot_keypress_windowskey(String keyvalue, String delayvalue) throws InterruptedException, IOException 
	{
		
		try
		{
			
			int delay_value = Integer.valueOf(delayvalue);
			
			Robot robot = new Robot();
			int keycode = 0;
			
			if (keyvalue.toUpperCase().equals("R"))
			{
				keycode = KeyEvent.VK_R;
			}
			
			robot.keyPress(KeyEvent.VK_WINDOWS);
	        robot.keyPress(keycode);
	        robot.keyRelease(keycode);
	        robot.keyRelease(KeyEvent.VK_WINDOWS);
			robot.delay(delay_value);
		
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Click Windows Key (Key Value: " + keyvalue + ")", "0");
		
	}
	
	/**
	 * This function enters data via keypress.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @since 9/22/2017 
	 */

	public void fw_robot_keypress_enterdata(String input_string, String delayvalue) throws InterruptedException, IOException 
	{
		
		try
		{    
		
			Robot robot = new Robot();
			
			int delay_value = Integer.valueOf(delayvalue);
			
			// Added on 1/19/2018
			if (input_string.contains("FILE_"))
			{
				String[] input_string_arr = input_string.split("_");
				String input_string_file = input_string_arr[1];
				input_string = fw_get_variable(input_string_file);
			}
			// End of Added on 1/19/2018
			
			int input_string_length = input_string.length();
			
			String letter = "";
			
			for (int x=1;x<input_string_length+1;x++)
			{
				
				letter = input_string.substring(x-1,x);
				
				if (letter.equals(":"))
				{
					robot.keyPress( KeyEvent.VK_SHIFT );
					robot.keyPress( KeyEvent.VK_SEMICOLON );
		            robot.keyRelease( KeyEvent.VK_SEMICOLON );
		            robot.keyRelease( KeyEvent.VK_SHIFT );
				}
				else if (letter.equals("\\"))
				{
					robot.keyPress( KeyEvent.VK_BACK_SLASH );
		            robot.keyRelease( KeyEvent.VK_BACK_SLASH );
				}
				else if (letter.equals("."))
				{
					robot.keyPress( KeyEvent.VK_PERIOD );
		            robot.keyRelease( KeyEvent.VK_PERIOD );
				}
				else if (letter.equals(" "))
				{
					robot.keyPress( KeyEvent.VK_SPACE );
		            robot.keyRelease( KeyEvent.VK_SPACE );
				}
				else if (letter.equals("-"))
				{
					robot.keyPress( KeyEvent.VK_MINUS );
		            robot.keyRelease( KeyEvent.VK_MINUS );
				}
				else if (letter.equals("_"))
				{
					robot.keyPress( KeyEvent.VK_SHIFT );
					robot.keyPress( KeyEvent.VK_MINUS );
		            robot.keyRelease( KeyEvent.VK_MINUS );
		            robot.keyRelease( KeyEvent.VK_SHIFT );
				}
				else
				{
				    boolean upperCase = Character.isUpperCase( letter.charAt(0) );
		            String variableName = "VK_" + letter.toUpperCase();
		
		            Class clazz = KeyEvent.class;
		            Field field = clazz.getField( variableName );
		            int keyCode = field.getInt(null);
		
		            if (upperCase) robot.keyPress( KeyEvent.VK_SHIFT );
		
		            robot.keyPress( keyCode );
		            robot.keyRelease( keyCode );
		
		            if (upperCase) robot.keyRelease( KeyEvent.VK_SHIFT );
				}
				
			}
			
			robot.delay(delay_value);
		
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Enter Data Into Field (Input: " + input_string + ")", "0");
		
	}
	
	/**
	 * This function hits alt+key.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 9/22/2017 
	 */

	public void fw_robot_keypress_altkey(String keyvalue, String delayvalue) throws InterruptedException, IOException 
	{
		
		try
		{
			
			int delay_value = Integer.valueOf(delayvalue);
			
			Robot robot = new Robot();
			int keycode = 0;
			
			if (keyvalue.toUpperCase().equals("N"))
			{
				keycode = KeyEvent.VK_N;
			}
			else if (keyvalue.toUpperCase().equals("S"))
			{
				keycode = KeyEvent.VK_S;
			}
			else if (keyvalue.toUpperCase().equals("TAB"))
			{
				keycode = KeyEvent.VK_TAB;
			}
			else if (keyvalue.toUpperCase().equals("C"))
			{
				keycode = KeyEvent.VK_C;
			}
			else if (keyvalue.toUpperCase().equals("M"))
			{
				keycode = KeyEvent.VK_M;
			}
			
			robot.keyPress(KeyEvent.VK_ALT);
	        robot.keyPress(keycode);
	        robot.keyRelease(keycode);
	        robot.keyRelease(KeyEvent.VK_ALT);
			robot.delay(delay_value);
		
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Click Alt Key (Key Value: " + keyvalue + ")", "0");
		
	}

	/**
	 * This function hits shift+key.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 10/4/2017 
	 */

	public void fw_robot_keypress_shiftkey(String keyvalue, String delayvalue) throws InterruptedException, IOException 
	{
		
		try
		{
			
			int delay_value = Integer.valueOf(delayvalue);
			
			Robot robot = new Robot();
			int keycode = 0;
			
			if (keyvalue.toUpperCase().equals("TAB"))
			{
				keycode = KeyEvent.VK_TAB;
			}
			else if (keyvalue.toUpperCase().equals("END"))
			{
				keycode = KeyEvent.VK_END;
			}
			
			robot.keyPress(KeyEvent.VK_SHIFT);
			Thread.sleep(1000);
	        robot.keyPress(keycode);
	        robot.keyRelease(keycode);
	        robot.keyRelease(KeyEvent.VK_SHIFT);
			robot.delay(delay_value);
		
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Click Shift Key (Key Value: " + keyvalue + ")", "0");
		
	}
	
	/**
	 * This function hits ctl+key.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 9/25/2017 
	 */

	public void fw_robot_keypress_ctlkey(String keyvalue, String delayvalue) throws InterruptedException, IOException 
	{
		
		try
		{
			
			int delay_value = Integer.valueOf(delayvalue);
			
			Robot robot = new Robot();
			int keycode = 0;
			
			if (keyvalue.toUpperCase().equals("C"))
			{
				keycode = KeyEvent.VK_C;
			}
			else if (keyvalue.toUpperCase().equals("V"))
			{
				keycode = KeyEvent.VK_V;
			}
			
			robot.keyPress(KeyEvent.VK_CONTROL);
	        robot.keyPress(keycode);
	        robot.keyRelease(keycode);
	        robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.delay(delay_value);
		
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Click Ctrl Key (Key Value: " + keyvalue + ")", "0");
		
	}
	
	/**
	 * This function hit Tab key.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 9/22/2017 
	 */

	public void fw_robot_keypress_tab(String num_tabs, String delayvalue) throws InterruptedException, IOException 
	{
		try
		{
			
			int numtabs = Integer.valueOf(num_tabs);
			int delay_value = Integer.valueOf(delayvalue);
			
			Robot robot = new Robot();
			
			for (int x=1;x<numtabs+1;x++)
			{
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyRelease(KeyEvent.VK_TAB);
			}
	
			robot.delay(delay_value);
		
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Tab (Num Tabs: " + num_tabs + ")", "0");
		
	}

	/**
	 * This function hit Delete key.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 11/6/2017 
	 */

	public void fw_robot_keypress_delete(String delayvalue) throws InterruptedException, IOException 
	{
		try
		{
			int delay_value = Integer.valueOf(delayvalue);
			
			Robot robot = new Robot();
			
			robot.keyPress(KeyEvent.VK_DELETE);
			robot.keyRelease(KeyEvent.VK_DELETE);
	
			robot.delay(delay_value);
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Delete Key", "0");
		
	}
	
	/**
	 * This function hit Enter key.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 9/22/2017 
	 */

	public void fw_robot_keypress_enterkey(String delayvalue) throws InterruptedException, IOException
	{
		try
		{
			
			int delay_value = Integer.valueOf(delayvalue);
			
			Robot robot = new Robot();
			
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
	
			robot.delay(delay_value);
		
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Hit Enter Key", "0");
		
	}
	
	/**
	 * This function hit down arrow key.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 12/14/2017 
	 */

	public void fw_robot_keypress_downarrow(String numtimes, String delayvalue) throws InterruptedException, IOException
	{
		try
		{
			
			int delay_value = Integer.valueOf(delayvalue);
			int num_times = Integer.valueOf(numtimes);
			
			Robot robot = new Robot();
			
			for (int j=0;j<num_times;j++)
			{
				robot.keyPress(KeyEvent.VK_DOWN);
				robot.keyRelease(KeyEvent.VK_DOWN);
			}
			
			robot.delay(delay_value);
		
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Hit Down Arrow Key (Number of Times: " + numtimes + ")", "0");
		
	}
	
	/**
	 * This function hit Right Arrow key.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 9/22/2017 
	 */

	public void fw_robot_keypress_rightarrow(String delayvalue) throws InterruptedException, IOException
	{
		try
		{
			
			int delay_value = Integer.valueOf(delayvalue);
			
			Robot robot = new Robot();
			
			robot.keyPress(KeyEvent.VK_RIGHT);
			robot.keyRelease(KeyEvent.VK_RIGHT);
	
			robot.delay(delay_value);
		
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Hit Right Arrow Key", "0");
		
	}
	
	/**
	 * This function moves the mouse to specified x and y coordinates.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 9/22/2017 
	 */

	public void fw_robot_mousemove(String xcoordinate, String ycoordinate, String delayvalue) throws InterruptedException, IOException 
	{
		try
		{
					
			int x_coordinate = Integer.valueOf(xcoordinate);
			int y_coordinate = Integer.valueOf(ycoordinate);
			int delay_value = Integer.valueOf(delayvalue);
			
			Robot robot = new Robot();
			
			robot.mouseMove(x_coordinate, y_coordinate);
	
			robot.delay(delay_value);
		
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Mouse Move (X Coordinate: " + xcoordinate + ", Y Coordinate: " + ycoordinate + ")", "0");
		
	}
	
	/**
	 * This function clicks the mouse button.
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @since 9/22/2017 
	 */

	public void fw_robot_mouseclick(String mouse_button, String numclicks, String delayvalue) throws InterruptedException, IOException 
	{
		try
		{
			int num_clicks = Integer.valueOf(numclicks);
			int delay_value = Integer.valueOf(delayvalue);
			
			Robot robot = new Robot();
			
			if (mouse_button.toUpperCase().equals("LEFT"))
			{
				for (int x=1;x<num_clicks+1;x++)
				{
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				}
			}
	
			if (mouse_button.toUpperCase().equals("RIGHT"))
			{
				for (int x=1;x<num_clicks+1;x++)
				{
					robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
				}
			}
			
			robot.delay(delay_value);
		}
		catch(Exception  e)
		{
			e.printStackTrace();
        }
		
		log.fw_writeLogEntry("ROBOT Mouse Click (Button: " + mouse_button + ", Num Clicks: " + numclicks + ")", "0");
		
	}
	
	/**
    * This function moves to element.
    * 
    * @author Mark Elking
    * @since 9/14/2017
    * 
    */
	
	public void fw_move_to_element(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
		
		String text_msg = "";
		String ret_cd = "0";
		int xval = 0;
		int yval = 0;
		
		try
		{			
			
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				org.openqa.selenium.Point coordinates = driver.findElement(fw_get_element_object(locator, locatorvalue)).getLocation();
				Robot robot = new Robot();
				xval = coordinates.x;
				yval = coordinates.y + 120;
				robot.mouseMove(xval,yval);
			}
			
			log.fw_writeLogEntry("Move to Element (Name: " + fieldname + ", X: " + xval + ", Y: " + yval + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Move to Element (Name: " + fieldname + ", X: " + xval + ", Y: " + yval + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
	}
		
	/**
	 * This function is used to get next available email.
	 * 
	 * @since: 7/19/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_get_email() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		String appname = fw_get_variable("appname");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		sql_query = "select email_auto_id, email_address, email_userid, email_password from tbl_email where email_application = '" + appname + "' and (email_last_modified_date < systimestamp-(2/24) or email_available_flag = 'Y') and rownum = 1";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Email Found in TBL_EMAIL table";
		
		String email_auto_id = "";
		String email_address = "";
		String email_userid = "";
		String email_password = "";
		
		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";
		
			email_auto_id = rs.getString("email_auto_id");
			email_address = rs.getString("email_address");
			email_userid = rs.getString("email_userid");
			email_password = rs.getString("email_password");
			
			fw_set_variable("getemailautoid", email_auto_id);
			fw_set_variable("getemailaddress", email_address);
			fw_set_variable("getemailuserid", email_userid);
			fw_set_variable("getemailpassword", email_password);
			
			String sql_update = "update tbl_email set email_available_flag = 'N', email_last_modified_date = sysdate where email_auto_id = " + email_auto_id;
			
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
			
		}
	
		connection.close();

		log.fw_writeLogEntry("Get Email (Address: " + email_address + ", UserID: " + email_userid + ", PassID: " + email_password + ", Variable Files: emailaddress, emailuserid, emailpassword)" + text_msg, rc_val);
		
	}

	/**
	 * This function is used to update email to available flag of Y.
	 * 
	 * @since: 7/19/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_update_email() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		String appname = fw_get_variable("appname");
		String email_auto_id = fw_get_variable("getemailautoid");
		String email_address = fw_get_variable("getemailaddress");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		sql_query = "select count(*) cntval from tbl_email where email_application = '" + appname + "' and email_auto_id = '" + email_auto_id + "'";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Email Found in TBL_EMAIL table";
		
		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			
			String sql_update = "update tbl_email set email_available_flag = 'Y', email_last_modified_date = sysdate where email_auto_id = " + email_auto_id;
			
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
			
		}
	
		connection.close();

		log.fw_writeLogEntry("Update Email (Address: " + email_address + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function clears data from a field.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 6/22/2017
	 * 
	 */
	
	public void fw_clear_data_from_field(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
		
		String text_msg = "";
		String ret_cd = "0";
		
		try
		{
					
			String found_flag = "";
			
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
				{
					found_flag = "YES";
					driver.findElement(fw_get_element_object(locator, locatorvalue)).clear();
				}
			}
			else if (locator.equals("NA")) 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(locatorvalue))
					{
						found_flag = "YES";
						item.clear();
						break;
					}
				}
			}
			else 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getAttribute(locator);
					
					if (label.contains(locatorvalue))
					{
						found_flag = "YES";
						item.clear();
						break;
					}
				}
			}
			
			if (found_flag.equals("YES"))
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** Field NOT Found ****************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Clear Data from Field (Name: " + fieldname + ")" + text_msg, ret_cd);
		
			Thread.sleep(milliseconds_to_wait);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);
			log.fw_writeLogEntry("Clear Data from Field (Name: " + fieldname + ") - " + text_msg + " - " + exception_string, ret_cd);
			
		}
		
	}
	
	/**
	 * This function is used to switch to default content.  Used right after
	 * you switch to a frame, then to get back to default main objects use
	 * this switch to default content function.
	 * 
	 * @since: 6/7/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	
	public void fw_switchto_defaultcontent() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		driver.switchTo().defaultContent();
		
		log.fw_writeLogEntry("Switch to Default Conent", "0");
		
	}
	
	/**
	 * This function will click on security certificate.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 6/6/2017
	 * 
	 */
	
	public void fw_click_security_certificate() throws InterruptedException, IOException 
	{
		
		String out_message = "Click Security Certificate - Certificate NOT FOUND";
		
		boolean isPresent = driver.findElements(fw_get_element_object("id", "overridelink")).size() > 0;
		if (isPresent == true)
		{
			fw_click_button("Click Continue", "NA", "id", "overridelink", "", 0);
			out_message = "Click Security Certificate (FOUND and CLICKED)";
		}
		
		log.fw_writeLogEntry(out_message, "0");
		
	}
	
	/**
	 *
	 * This function gets a variable value.
	 *  
	 * @param variable_name
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 6/5/2017
	 */
	
	public String fw_get_variable(String variable_name) throws MalformedURLException, IOException, InterruptedException 
	{
		
		String workspace_name = fw_get_workspace_name();
		String variable_file = workspace_name + "\\variables\\" + variable_name;	
		
		String variable_value = "";				
		FileReader fileReader = null;
		BufferedReader bfrReader = null;
		String strLine = null;
		
		fileReader = new FileReader(variable_file);
		bfrReader = new BufferedReader(fileReader);
		
		while ((strLine = bfrReader.readLine()) != null)
		{	
			variable_value = variable_value + strLine;
		}
		if (null != bfrReader)
		{
			bfrReader.close();
		}
		if (fileReader != null)
		{
			fileReader.close();
		}		
		
		return variable_value;
		
	}
	
	/**
	 * This function is used to convert string case to upper or lower.
	 * 
	 * @param: filename
	 * @param: case_to_convert_to
	 * @since: 6/1/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	
	public void fw_convert_string_case(String filename, String case_to_convert_to) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String string_value = fw_get_variable(filename);
		
		case_to_convert_to = case_to_convert_to.toUpperCase();
		if (case_to_convert_to.equals("UPPER"))
		{
			string_value = string_value.toUpperCase();
		}
		else if (case_to_convert_to.equals("LOWER"))
		{
			string_value = string_value.toLowerCase();
		}
		
		fw_set_variable("outconvertedstringcase", string_value);
		
		log.fw_writeLogEntry("Convert String Case (Input File: " + filename + ", Case to Convert to: " + case_to_convert_to + ", Output File: outconvertedstringcase, Output Value: " + string_value + ")", "0");
		
	}

	/**
	 * This function is used to get current URL.
	 * 
	 * @since: 6/1/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	
	public void fw_get_current_url() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String current_url = driver.getCurrentUrl();
		
		fw_set_variable("currenturl", current_url);
		
		log.fw_writeLogEntry("Get Current URL (URL: " + current_url + ")", "0");
		
	}

	/**
	 * This function is used to get the computer name.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 4/30/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	
	public void fw_get_computername() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String hostname = "Unknown";

		try
		{
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
		    System.out.println("Hostname can not be resolved");
		}
		
		fw_set_variable("SKIP--hostname", hostname);
		
	}
	
	/**
	 * This function is used to write a logging record to database.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 4/30/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_write_logging_database(String fname, String appname, String userid, String requesttype, String workstation, String testentity, String testcaseid, String logmessage, String execstatus, String teststepidfailed, String teststepdescfailed, String logoutputfile) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		String sql_query = "select count(*) cntval from tbl_tclogging where logging_outputfile = '" + logoutputfile + "'";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		int out_count = 0;
		
		if (rs.next())
		{
			out_count = rs.getInt("cntval");
		}
		
		if (out_count == 0)
		{
			String almtestsetcategory = fw_get_variable("ALMTESTSETCATEGORY");
			String environmentselected = fw_get_variable("ENVSELECTED");
			
			sql_query = "INSERT INTO tbl_tclogging (logging_auto_id, logging_date_created, logging_last_modified_by, logging_last_modified_date, logging_application, logging_userfirstname, logging_userid, logging_requesttype, logging_workstation, logging_entity, logging_testid, logging_message, LOGGING_EXECUTION_STATUS, LOGGING_TESTSTEP_ID_FAILED, LOGGING_TESTSTEP_DESC_FAILED, LOGGING_OUTPUTFILE, logging_componentname, logging_objectname, LOGGING_TESTSETCATEGORY, LOGGING_ENVIRONMENT) VALUES (seq_logging_id.nextval, sysdate, '" + fname + "', sysdate, '" + appname + "', '" + fname + "', '" + userid + "', '" + requesttype + "', '" + workstation + "', '" + testentity + "', '" + testcaseid + "', '" + logmessage + "', '" + execstatus + "', '" + teststepidfailed + "', '" + teststepdescfailed + "', '" + logoutputfile + "', 'NA', 'NA', '" + almtestsetcategory + "', '" + environmentselected + "')";
		}
		else
		{
			if (execstatus.equals("PASSED"))
			{
				teststepidfailed = "";
				teststepdescfailed = "";
			}
			else
			{
				teststepidfailed = fw_get_variable("failedstepnumber");
				teststepdescfailed = fw_get_variable("failedstepdesc");
				int teststepdescfailed_length = teststepdescfailed.length();
				if (teststepdescfailed_length > 2495)
				{
					teststepdescfailed = teststepdescfailed.substring(0, 2490);
				}

				teststepdescfailed = teststepdescfailed.replace("'", "");
				
			}
			sql_query = "update tbl_tclogging set LOGGING_EXECUTION_STATUS = '" + execstatus + "', LOGGING_TESTSTEP_ID_FAILED = '" + teststepidfailed + "', LOGGING_TESTSTEP_DESC_FAILED = '" + teststepdescfailed + "' where LOGGING_OUTPUTFILE = '" + logoutputfile + "'";
		}
				
		ResultSet rs2 = null;
		Statement st2 = connection.createStatement();
		rs2 = st2.executeQuery(sql_query);
		
		connection.close();
		
	}
	
	/**
	 * This function is used to write a location record to database.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 8/29/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_write_location_database() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		String RequestID = fw_get_variable("RequestID");
		String Address1 = fw_get_variable("Address1");
		String Address2 = fw_get_variable("Address2");
		String City = fw_get_variable("City");
		String State = fw_get_variable("State");
		String ZipCode = fw_get_variable("ZipCode");
		String Sys = fw_get_variable("Sys");
		String Prin = fw_get_variable("Prin");
		String Agent = fw_get_variable("Agent");
		String LocationId = fw_get_variable("LocationId");
		String lanoutputlogfile = fw_get_variable("lanoutputlogfile");
		String LASTMODIFIEDBY = fw_get_variable("LASTMODIFIEDBY");
	    String locationteam = fw_get_variable("TEAM");
	    
		String sql_query = "INSERT INTO tbl_location (location_auto_id, location_id, location_requestid, location_last_modified_date, location_address1, location_city, location_state, location_zip, location_system, location_prin, location_agent, location_log_file, location_last_modified_by, location_address2, location_team, location_available_flag) VALUES (seq_location_id.nextval, '" + LocationId + "', '" + RequestID + "', sysdate, '" + Address1 + "', '" + City + "', '" + State + "', '" + ZipCode + "', '" + Sys + "', '" + Prin + "', '" + Agent + "', '" + lanoutputlogfile + "', '" + LASTMODIFIEDBY + "', '" + Address2 + "', '" + locationteam + "', 'Y')";

		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		connection.close();
		
	}
	
	/**
	 * This function is used to get a spa location.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 4/26/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_get_spalocation(String envname, String switchtype, String ratecenter, String accounttype, String index_value, String devicetype) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
				
		if (envname.contains("FILE_"))
		{
			String[] envname_arr = envname.split("_");
			String envname_file = envname_arr[1];
			envname = fw_get_variable(envname_file);
		}
		if (switchtype.contains("FILE_"))
		{
			String[] switchtype_arr = switchtype.split("_");
			String switchtype_file = switchtype_arr[1];
			switchtype = fw_get_variable(switchtype_file);
		}
		if (ratecenter.contains("FILE_"))
		{
			String[] ratecenter_arr = ratecenter.split("_");
			String ratecenter_file = ratecenter_arr[1];
			ratecenter = fw_get_variable(ratecenter_file);
		}
		if (accounttype.contains("FILE_"))
		{
			String[] accounttype_arr = accounttype.split("_");
			String accounttype_file = accounttype_arr[1];
			accounttype = fw_get_variable(accounttype_file);
		}
		if (devicetype.contains("FILE_"))
		{
			String[] devicetype_arr = devicetype.split("_");
			String devicetype_file = devicetype_arr[1];
			devicetype = fw_get_variable(devicetype_file);
		}
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		if (devicetype.equals(""))
		{
			sql_query = "select spalocation_system, spalocation_prin, spalocation_agent, spalocation_slbos_address1, spalocation_slbos_locationid, spalocation_slbos_zipcode, spalocation_slbos_city, spalocation_slbos_state, spalocation_devicetype from tbl_spalocation where spalocation_environment = '" + envname + "' and spalocation_switch_type = '" + switchtype + "' and spalocation_rate_center = '" + ratecenter +"' and spalocation_accounttype = '" + accounttype + "'";
		}
		else
		{
			sql_query = "select spalocation_system, spalocation_prin, spalocation_agent, spalocation_slbos_address1, spalocation_slbos_locationid, spalocation_slbos_zipcode, spalocation_slbos_city, spalocation_slbos_state, spalocation_devicetype from tbl_spalocation where spalocation_environment = '" + envname + "' and spalocation_switch_type = '" + switchtype + "' and spalocation_rate_center = '" + ratecenter +"' and spalocation_accounttype = '" + accounttype + "' and spalocation_devicetype = '" + devicetype + "'";
		}
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No SPALocation Found in TBL_SPALOCATION table";

		String out_spalocation_system = "";
		String out_spalocation_prin = "";
		String out_spalocation_agent = "";
		String out_spalocation_slbos_address1 = "";
		String out_spalocation_slbos_locationid = "";
		String out_spalocation_slbos_zipcode = "";
		String out_spalocation_slbos_city = "";
		String out_spalocation_slbos_state = "";
		String out_spalocation_account_prefix = "";
		String out_spalocation_devicetype = "";
		
		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			
			out_spalocation_system = rs.getString("spalocation_system");
			out_spalocation_prin = rs.getString("spalocation_prin");
			out_spalocation_agent = rs.getString("spalocation_agent");
			out_spalocation_slbos_address1 = rs.getString("spalocation_slbos_address1");
			out_spalocation_slbos_locationid = rs.getString("spalocation_slbos_locationid");
			out_spalocation_slbos_zipcode = rs.getString("spalocation_slbos_zipcode");
			out_spalocation_slbos_city = rs.getString("spalocation_slbos_city");
			out_spalocation_slbos_state = rs.getString("spalocation_slbos_state");
			out_spalocation_account_prefix = out_spalocation_system + out_spalocation_prin.substring(0, 2) + out_spalocation_agent.substring(0, 3);
			out_spalocation_devicetype = rs.getString("spalocation_devicetype");
					
			fw_set_variable("outspalocationsystem" + index_value, out_spalocation_system);
			fw_set_variable("outspalocationprin" + index_value, out_spalocation_prin);
			fw_set_variable("outspalocationagent" + index_value, out_spalocation_agent);
			fw_set_variable("outspalocationslbosaddress1" + index_value, out_spalocation_slbos_address1);
			fw_set_variable("outspalocationslboslocationid" + index_value, out_spalocation_slbos_locationid);
			fw_set_variable("outspalocationslboszipcode" + index_value, out_spalocation_slbos_zipcode);
			fw_set_variable("outspalocationslboscity" + index_value, out_spalocation_slbos_city);
			fw_set_variable("outspalocationslbosstate" + index_value, out_spalocation_slbos_state);
			fw_set_variable("outspalocationaccountprefix" + index_value, out_spalocation_account_prefix);
			fw_set_variable("outspalocationdevicetype" + index_value, out_spalocation_devicetype);
			
		}
	
		connection.close();

		log.fw_writeLogEntry("Get SPA Location (Env: " + envname + ", Switch Type: " + switchtype + ", Rate Center: " + ratecenter + ", Account Type: " + accounttype + ", Device Type: " + devicetype + ", Index: " + index_value + ", Account Prefix: " + out_spalocation_account_prefix + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function is used to get sequence.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 4/19/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_get_sequence(String sequence_name, String index_value) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
				
		sql_query = "select sequence_auto_id, sequence_entity, sequence_value from tbl_sequence where sequence_entity = '" + sequence_name + "'";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Sequence Found in TBL_SEQUENCE table";

		String out_sequence_value = "";
		String final_sequence_value = "";
		String output_value = "";
		
		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			int autoid = rs.getInt("sequence_auto_id");
		
			out_sequence_value = rs.getString("sequence_value");
			int out_sequence_value_length = out_sequence_value.length();
			
			int temp_sequence_value = Integer.valueOf(out_sequence_value);
			temp_sequence_value = temp_sequence_value + 1;
			final_sequence_value = String.valueOf(temp_sequence_value);
			int final_sequence_value_length = final_sequence_value.length();
			
			int temp_difference = out_sequence_value_length - final_sequence_value_length;
			
			for (int j = 1; j<temp_difference+1;j++)
			{
				output_value = output_value + "0";
			}
			
			output_value = output_value + final_sequence_value;
			
			fw_set_variable("sequencevalue" + index_value, output_value);
			
			String sql_update = "update tbl_sequence set sequence_value = '" + output_value + "', sequence_comments = 'updated sequence_value', sequence_last_modified_date = sysdate where sequence_auto_id = " + autoid;
			
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
			
		}
	
		connection.close();

		log.fw_writeLogEntry("Get Sequence (Name: " + sequence_name + ", Value: " + output_value + ", Index: " + index_value + ", Variable File: sequencevalue" + index_value + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function is used to get a phone number.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 4/19/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_get_phone_number_backup20180215(String phone_type, String phone_switch_type, String account_type, String index_value) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String npa_value = fw_get_variable("ENVgetTNlookupNPA");
		String nxx_value = fw_get_variable("ENVgetTNlookupNXX");
			
		String env_value = fw_get_variable("ENVSELECTED");
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");

		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		account_type = account_type.toUpperCase();
		phone_switch_type = phone_switch_type.toUpperCase();
		String phone_rate_center = fw_get_variable("ENVRateCenter" + phone_switch_type + account_type);
		
		if (env_value.equals("PROD"))
		{
			if (npa_value.equals(""))
			{
				sql_query = "select phone_auto_id, phone_value from tbl_phone where phone_available_flag = 'Y' and phone_type = '" + phone_type + "' and phone_switch_type = '" + phone_switch_type + "' and phone_rate_center = '" + phone_rate_center + "' and phone_environment = 'PROD' and rownum = 1";
			}
			else
			{
				sql_query = "select phone_auto_id, phone_value from tbl_phone where phone_available_flag = 'Y' and phone_type = '" + phone_type + "' and phone_switch_type = '" + phone_switch_type + "' and phone_rate_center = '" + phone_rate_center + "' and phone_environment = 'PROD' and substr(phone_value,1,3) = '" + npa_value + "' and substr(phone_value,4,3) = '" + nxx_value + "' and rownum = 1";
			}
		}
		else
		{
			if (npa_value.equals(""))
			{
				sql_query = "select phone_auto_id, phone_value from tbl_phone where phone_available_flag = 'Y' and phone_type = '" + phone_type + "' and phone_switch_type = '" + phone_switch_type + "' and phone_rate_center = '" + phone_rate_center + "' and phone_environment = 'TEST' and rownum = 1";
			}
			else
			{
				sql_query = "select phone_auto_id, phone_value from tbl_phone where phone_available_flag = 'Y' and phone_type = '" + phone_type + "' and phone_switch_type = '" + phone_switch_type + "' and phone_rate_center = '" + phone_rate_center + "' and phone_environment = 'TEST' and substr(phone_value,1,3) = '" + npa_value + "' and substr(phone_value,4,3) = '" + nxx_value + "' and rownum = 1";
			}
		}
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No TN Found in TBL_PHONE table";

		String out_tn = "";
		String out_tn_first_three_digits = "";
		
		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			int autoid = rs.getInt("phone_auto_id");
		
			out_tn = rs.getString("phone_value");
			out_tn_first_three_digits = out_tn.substring(0, 3);
					
			fw_set_variable("tnvalue" + index_value, out_tn);
			fw_set_variable("tnvaluenpa" + index_value, out_tn_first_three_digits);
			
			String sql_update = "update tbl_phone set phone_available_flag = 'N', phone_comments = 'updated phone_available flag to N', phone_last_modified_date = sysdate where phone_auto_id = " + autoid;
			
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
			
		}
	
		connection.close();

		log.fw_writeLogEntry("Get TN (Type: " + phone_type + ", Switch Type: " + phone_switch_type + ", Rate Center: " + phone_rate_center + ", Phone Number: " + out_tn + ", Index: " + index_value + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function gets environment to execute test.
	 * 
	 * @since: 4/6/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_get_environment_to_execute_tests(String input_filename) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String workspace_name = fw_get_workspace_name();
		
		String envselectedfile = workspace_name + "\\variables\\ENVSELECTED";
		File file = new File(envselectedfile);
		if(file.delete())
		{
			//System.out.println(file.getName() + " is deleted!");
		}    				
		
		if (workspace_name.toUpperCase().contains("JENKINS"))
		{
			fw_set_variable("SKIP--ENVSELECTED", "FILE_JENKINSENVIRONMENT");
		}
		else
		{
			String appname = fw_get_variable("appname");
			
			String sql_query = "";
			
			String userName = fw_get_variable("usernameTESTDB");
			String password = fw_get_variable("passwordTESTDB");
			String connection_string = fw_get_variable("connectionstringTESTDB");
			
			Connection connection = null;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(connection_string, userName, password);
			
			sql_query = "select distinct environment_name from tbl_environment where environment_application = '" + appname + "'";
			
			ResultSet rs = null;
			Statement st = connection.createStatement();
			rs = st.executeQuery(sql_query);
			
			String environmentname = "";
			String results_found = "no";
			
			List<String> optionList = new ArrayList<String>();
			
			while (rs.next())
			{
				results_found = "yes";
				
				environmentname = rs.getString("environment_name");
				
				if (environmentname == null)
				{
					environmentname = "";
				}
				
				optionList.add(environmentname);
			}
			
			connection.close();
	
			if (results_found == "yes")
			{		
				Object[] options = optionList.toArray();
				Object environment_selected = JOptionPane.showInputDialog(null, "Choose Environment", "Environment", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			    String out_environment_selected = environment_selected.toString();
			    
				fw_set_variable("SKIP--ENVSELECTED", out_environment_selected);
				
			}
		
		}
		
		String temp_out_value = fw_get_variable("ENVSELECTED");
		String out_value = temp_out_value.trim();
		fw_set_variable("SKIP--ENVSELECTED", out_value);
		
	}
	
	/**
	 * This function gets user name for user running tests.
	 * 
	 * @since: 4/6/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public String fw_get_user_name(String application_name) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String username = "";
		String record_cnt = "";
		String test_type = "";
		String workspace_name = fw_get_workspace_name();
		String alm_user_id = "svc_automation";
		String alm_test_set_category = "";
		
		String datasource = "ora-dev01.corp.chartercom.com:1521/SINST01D.CORP.CHARTERCOM.COM";
		String userName = "TEST_AUTOMATION";
		String password = "y_Kjny_6R_t5";
		String connection_string = "jdbc:oracle:thin:@" + datasource;
		
		fw_set_variable("SKIP--datasourceTESTDB",datasource);
		fw_set_variable("SKIP--usernameTESTDB",userName);
		fw_set_variable("SKIP--passwordTESTDB",password);
		fw_set_variable("SKIP--connectionstringTESTDB",connection_string);
		
		// temp
		//workspace_name = workspace_name + "JENKINS";
		// end of temp
				
		if (workspace_name.toUpperCase().contains("JENKINS"))
		{

			// JENKINSTESTSETCATEGORY
			
			String filename3 = "JENKINSTESTSETCATEGORY";
			alm_test_set_category = fw_get_variable(filename3);
			alm_test_set_category = alm_test_set_category.toUpperCase().trim();
			
			System.out.println("JENKINS ALM Test Set Category: " + alm_test_set_category);
			
			// JENKINSEXECUTIONUSER
			
			String filename = "JENKINSEXECUTIONUSER";
			username = fw_get_variable(filename);
			username = username.toUpperCase().trim();
			System.out.println("JENKINS Execution User: " + username);
			
			if (username.contains("JS"))
			{
				test_type = "JENKINSSMOKE";
			}
			else if (username.contains("JR"))
			{
				test_type = "JENKINSREGRESSION";
			}
			else
			{
				test_type = "JENKINSUNIT";
			}
			
			fw_set_variable("SKIP--" + filename, username);
			
			
			// JENKINSNETWORKID
			
			String filename2 = "JENKINSNETWORKID";
			alm_user_id = fw_get_variable(filename2);
			alm_user_id = alm_user_id.toLowerCase().trim();
			
			if (alm_user_id.contains("echo is on"))
			{
				alm_user_id = "svc_automation";
			}
			
			System.out.println("JENKINS Network ID: " + alm_user_id);
		}
		else
		{
			
			// Test Set
			
			JFrame frame2 = new JFrame("InputDialog");
			alm_test_set_category = JOptionPane.showInputDialog(frame2, "Test Set Category?");
			alm_test_set_category = alm_test_set_category.toUpperCase().trim();
			
			// User
			
			JFrame frame = new JFrame("InputDialog");
			username = JOptionPane.showInputDialog(frame, "User OR Test ID? (Test ID prefix with TC, like tc9435)");
			username = username.toUpperCase().trim();
			
			test_type = "ECLIPSEUNIT";
			
			Connection connection = null;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(connection_string, userName, password);
			
			String sql_query = "";
	    	String username_type = username.trim().substring(0, 2);
			if (username_type.equals("TC"))
			{
				String[] username_arr_value = username.split(",");
				int username_arr_length = username_arr_value.length;
				
				String username_temp = "";
				String username_out = "";
				for (int s=0; s<username_arr_length; s++)
				{
					username_temp = "'" + username_arr_value[s].trim() + "'";
					username_out = username_out + username_temp + ",";
				}
				
				int username_out_length = username_out.length();
				username_out = "(" + username_out.substring(0, username_out_length - 1) + ")";
				
				sql_query = "select count(*) cnt_val from tbl_execution where execution_application = '" + application_name + "' and execution_testid in " + username_out + " and execution_testsetcategory = '" + alm_test_set_category + "'";
			}
			else
			{
				sql_query = "select count(*) cnt_val from tbl_execution where execution_application = '" + application_name + "' and (execution_user = '" + username + "' or execution_user like '%," + username + "%' or execution_user like '%" + username + ",%') and execution_testsetcategory = '" + alm_test_set_category + "'";
			}
			
			ResultSet rs = null;
			Statement st = connection.createStatement();
			rs = st.executeQuery(sql_query);
			rs.next();
			record_cnt = rs.getString("cnt_val").toString();
			if (record_cnt.equals("0"))
			{
				System.out.println(" ");
				System.out.println(" ***** Stopping Execution because USER " + username + " OR Test Set Category " + alm_test_set_category + " NOT found in Execution table for this application ****** ");
				System.out.println(" ");
				System.exit(0);
			}
			else
			{
				if (!username.contains("JS") && !username.contains("JR") && !username_type.equals("TC"))
				{
					String sql_query2 = "select security_userid from tbl_tcsecurity where security_userfirstname = '" + username + "'";
					ResultSet rs2 = null;
					Statement st2 = connection.createStatement();
					rs2 = st2.executeQuery(sql_query2);
					rs2.next();
					alm_user_id = rs2.getString("security_userid").toString();
				}
			}
			connection.close();
			
		}
		
		fw_set_variable("SKIP--appname", application_name.toUpperCase());
		fw_set_variable("SKIP--fname", username.toUpperCase());
		fw_set_variable("SKIP--TESTTYPE", test_type);
		fw_set_variable("SKIP--ALMUSERIDLOGIN", alm_user_id);
		fw_set_variable("SKIP--ALMTESTSETCATEGORY", alm_test_set_category);
		
		System.out.println("OVERALL appname: " + application_name.toUpperCase());
		System.out.println("OVERALL fname: " + username.toUpperCase());
		System.out.println("OVERALL TESTTYPE: " + test_type);
		System.out.println("OVERALL ALMUSERIDLOGIN: " + alm_user_id);
		System.out.println("OVERALL ALMTESTSETCATEGORY: " + alm_test_set_category);
		
		return username;
		
	}
	
	/**
	 * This function is used to get test execution parms.
	 * 
	 * @since: 4/6/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public String fw_get_test_execparms(String application_name, String username) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		fw_set_variable("SKIP--appname", application_name);
		
		String almtestsetcategory = fw_get_variable("ALMTESTSETCATEGORY");
		
		String test_cases_to_execute_list = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);

		String sql_query = "";
    	String username_type = username.trim().substring(0, 2);
		if (username_type.equals("TC"))
		{
			String[] username_arr_value = username.split(",");
			int username_arr_length = username_arr_value.length;
			
			String username_temp = "";
			String username_out = "";
			for (int s=0; s<username_arr_length; s++)
			{
				username_temp = "'" + username_arr_value[s].trim() + "'";
				username_out = username_out + username_temp + ",";
			}
			
			int username_out_length = username_out.length();
			username_out = "(" + username_out.substring(0, username_out_length - 1) + ")";
			
			sql_query = "select execution_testid, execution_executionflag from tbl_execution where execution_application = '" + application_name + "' and execution_testid in " + username_out + " and execution_testsetcategory = '" + almtestsetcategory + "' order by execution_executionorder";

		}
		else
		{
			sql_query = "select execution_testid, execution_executionflag from tbl_execution where execution_application = '" + application_name + "' and (execution_user = '" + username + "' or execution_user like '%," + username + "%' or execution_user like '%" + username + ",%') and execution_testsetcategory = '" + almtestsetcategory + "' order by execution_executionorder";
		}
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String execution_testid = "";
		String execution_executionflag = "";
		String config_ids_list = "";
		String worksheet_list = "";
		
		int total_count = 0;

		String cur_TC_testid = "";
		String cur_config_id = "";
		
		while (rs.next())
		{
						
			execution_testid = "" + rs.getString("execution_testid");
			execution_executionflag = "" + rs.getString("execution_executionflag");
			
			if (execution_testid.startsWith("TC") && execution_testid.contains("-") && execution_executionflag.contains("Y"))
			{
				total_count = total_count + 1;
				
				String[] ep_sheetname_arr = execution_testid.split("-");
				
				cur_TC_testid = ep_sheetname_arr[0];
				cur_config_id = ep_sheetname_arr[1];
				
				test_cases_to_execute_list = test_cases_to_execute_list + cur_TC_testid + ",";
				config_ids_list = config_ids_list + cur_config_id + ",";
				worksheet_list = worksheet_list + execution_testid + ",";
			}
			else if ((execution_testid.startsWith("TC") && execution_executionflag.contains("Y") && !username_type.equals("TC")) || (execution_testid.startsWith("TC") && username_type.equals("TC")))
			{
				total_count = total_count + 1;
				
				test_cases_to_execute_list = test_cases_to_execute_list + execution_testid + ",";
				config_ids_list = config_ids_list + "NA,";
				worksheet_list = worksheet_list + execution_testid + ",";
			}

		}
		
		fw_set_variable("SKIP--totaltestcasecount", String.valueOf(total_count));
		fw_set_variable("SKIP--totaltestcasecountpassed", "0");
		fw_set_variable("SKIP--totaltestcasecountfailed", "0");
		
		fw_set_variable("SKIP--totaltestcasefilespassed", "");
		fw_set_variable("SKIP--totaltestcasefilesfailed", "");
		fw_set_variable("SKIP--totaltestcasenamespassed", "");
		fw_set_variable("SKIP--totaltestcasenamesfailed", "");
		
		if (!config_ids_list.isEmpty())
		{
			int config_ids_list_len = config_ids_list.length();
			config_ids_list = config_ids_list.substring(0, config_ids_list_len-1);
			fw_set_variable("SKIP--configids", config_ids_list);
		}
		
		if (!test_cases_to_execute_list.isEmpty())
		{
			int test_cases_to_execute_list_len = test_cases_to_execute_list.length();
			test_cases_to_execute_list = test_cases_to_execute_list.substring(0, test_cases_to_execute_list_len-1);
			fw_set_variable("SKIP--testids", test_cases_to_execute_list);
		}
		
		if (!worksheet_list.isEmpty())
		{
			int worksheet_list_len = worksheet_list.length();
			worksheet_list = worksheet_list.substring(0, worksheet_list_len-1);
			fw_set_variable("SKIP--worksheetlist", worksheet_list);
		}
		
		connection.close();
		
		System.out.println("");
		System.out.println("Test Cases to Execute: " + test_cases_to_execute_list);
		
		return test_cases_to_execute_list;
		
	}
	
	/**
	 * This function is used to get a test objects.
	 * 
	 * @since: 4/6/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_get_test_object(String application_name) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		//HashMap<Integer, String> hmap_object_name = new HashMap<Integer, String>();
		//HashMap<Integer, String> hmap_object_def = new HashMap<Integer, String>();
		//HashMap<Integer, String> hmap_object_extrainfo = new HashMap<Integer, String>();
		
		hmap_object_name = new HashMap<Integer, String>();
		hmap_object_def = new HashMap<Integer, String>();
		hmap_object_extrainfo = new HashMap<Integer, String>();
		String temp_object_def = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		String sql_query = "select object_name, object_tagnameattributevalue, object_extrainfo, object_tagname_overwrite from tbl_tcobject where object_application = '" + application_name + "'";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Test Object Found in TBL_TCOBJECT table for this Application";
		
		String object_name = "";
		String object_def = "";
		String object_extrainfo = "";
		String object_overwrite = "";
		
		String out_object_name = "";
		String out_object_def = "";
		String out_object_extrainfo = "";
		
		int total_count = 0;
		
		while (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			
			total_count = total_count + 1;
			
			object_name = rs.getString("object_name");
			object_def = rs.getString("object_tagnameattributevalue");
			object_extrainfo = rs.getString("object_extrainfo");
			object_overwrite = rs.getString("object_tagname_overwrite");
			
			String current_environment = fw_get_variable("ENVEnvironment");

			if (object_overwrite != null)
			{
				String current_object_overwrite = "";
				
				String[] object_overwrite_arr = object_overwrite.split("\\-\\-\\-\\-");
				for (int x=0;x<object_overwrite_arr.length;x++)
				{
					current_object_overwrite = object_overwrite_arr[x];
					String[] current_object_overwrite_arr = current_object_overwrite.split(",");
					if (current_object_overwrite_arr[3].trim().equals(current_environment))
					{
						object_def = current_object_overwrite_arr[0] + "," + current_object_overwrite_arr[1] + "," + current_object_overwrite_arr[2];
					}
				}
			}
			
			out_object_name = out_object_name + object_name + "$$$$";
			out_object_def = out_object_def + object_def + "$$$$";
			out_object_extrainfo = out_object_extrainfo + object_extrainfo + "$$$$";
			
			if (object_def == null)
			{
				object_def = "";
			}
			if (object_extrainfo == null)
			{
				object_extrainfo = "";
			}
			hmap_object_name.put(total_count, object_name);
			temp_object_def = object_def;
			//System.out.println("object def: " + temp_object_def);
			temp_object_def = temp_object_def.replace("@@@", "'");
			hmap_object_def.put(total_count, temp_object_def);
			hmap_object_extrainfo.put(total_count, object_extrainfo);
			
			
		}
		
		out_object_def = out_object_def.replace("@@@", "'");
	
		
		fw_set_variable("NOSUB!!out_object_name_obj", out_object_name);
		fw_set_variable("NOSUB!!out_object_def_obj", out_object_def);
		fw_set_variable("NOSUB!!out_object_extrainfo_obj", out_object_extrainfo);
		
		String tot_total_count = "" + total_count;
		fw_set_variable("total_objects", tot_total_count);
		
		connection.close();

		log.fw_writeLogEntry("Get Test Object (Application: " + application_name + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function is used to get a test component.
	 * 
	 * @since: 4/3/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_get_test_component(String application_name) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		//String sql_query = "select COMPONENT_EVENTNAME, COMPONENT_OBJECTNAME, COMPONENT_TESTDATA, COMPONENT_OBJECTTOLOOKFOR, COMPONENT_WAITTIME from tbl_tccomponent where component_application = '" + application_name + "' order by component_name, component_executionorder";
		String sql_query = "select COMPONENT_EVENTNAME, COMPONENT_OBJECTNAME, COMPONENT_TESTDATA, COMPONENT_OBJECTTOLOOKFOR, COMPONENT_WAITTIME from tbl_tccomponent where component_application = '" + application_name + "' and (component_skipflag != 'Y' or component_skipflag is null) order by component_name, component_executionorder";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Test Component Found in TBL_TCCOMPONENT table for this Application";
		
		String event_name = "";
		String object_name = "";
		String testdata_val = "";
		String objecttolookfor_val = "";
		String waittime_val = "";
		
		String out_event_name = "";
		String out_object_name = "";
		String out_testdata_val = "";
		String out_objecttolookfor_val = "";
		String out_waittime_val = "";
		
		int total_test_case_steps = 0;
		
		while (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			
			total_test_case_steps = total_test_case_steps + 1;
			
			event_name = rs.getString("COMPONENT_EVENTNAME");
			object_name = rs.getString("COMPONENT_OBJECTNAME");
			testdata_val = rs.getString("COMPONENT_TESTDATA");
			objecttolookfor_val = rs.getString("COMPONENT_OBJECTTOLOOKFOR");
			waittime_val = rs.getString("COMPONENT_WAITTIME");
			
			out_event_name = out_event_name + event_name + "$$$$";
			out_object_name = out_object_name + object_name + "$$$$";
			out_testdata_val = out_testdata_val + testdata_val + "$$$$";
			out_objecttolookfor_val = out_objecttolookfor_val + objecttolookfor_val + "$$$$";
			out_waittime_val = out_waittime_val + waittime_val + "$$$$";			
		}
	
		out_object_name = out_object_name.replace("@@@", "'");
		out_testdata_val = out_testdata_val.replace("@@@", "'");
		out_objecttolookfor_val = out_objecttolookfor_val.replace("@@@", "'");
		
		fw_set_variable("NOSUB!!out_event_name_comp", out_event_name);
		fw_set_variable("NOSUB!!out_object_name_comp", out_object_name);
		fw_set_variable("NOSUB!!out_testdata_val_comp", out_testdata_val);
		fw_set_variable("NOSUB!!out_objecttolookfor_val_comp", out_objecttolookfor_val);
		fw_set_variable("NOSUB!!out_waittime_val_comp", out_waittime_val);
		
		String tot_test_case_steps = "" + total_test_case_steps;
		fw_set_variable("total_test_case_steps_comp", tot_test_case_steps);
		
		connection.close();

		log.fw_writeLogEntry("Get Test Component (Application: " + application_name + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function is used to get a test component by component name.
	 * 
	 * @since: 7/31/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_get_test_component_byname(String application_name, String component_name) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		//String sql_query = "select COMPONENT_EVENTNAME, COMPONENT_OBJECTNAME, COMPONENT_TESTDATA, COMPONENT_OBJECTTOLOOKFOR, COMPONENT_WAITTIME from tbl_tccomponent where component_application = '" + application_name + "' and component_name = '" + component_name + "' order by component_name, component_executionorder";
		String sql_query = "select COMPONENT_EVENTNAME, COMPONENT_OBJECTNAME, COMPONENT_TESTDATA, COMPONENT_OBJECTTOLOOKFOR, COMPONENT_WAITTIME from tbl_tccomponent where component_application = '" + application_name + "' and component_name = '" + component_name + "' and (component_skipflag != 'Y' or component_skipflag is null) order by component_name, component_executionorder";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Test Component Found in TBL_TCCOMPONENT table for this Application";
		
		String event_name = "";
		String object_name = "";
		String testdata_val = "";
		String objecttolookfor_val = "";
		String waittime_val = "";
		
		String out_event_name = "";
		String out_object_name = "";
		String out_testdata_val = "";
		String out_objecttolookfor_val = "";
		String out_waittime_val = "";
		
		int total_test_case_steps = 0;
		
		while (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			
			total_test_case_steps = total_test_case_steps + 1;
			
			event_name = rs.getString("COMPONENT_EVENTNAME");
			object_name = rs.getString("COMPONENT_OBJECTNAME");
			testdata_val = rs.getString("COMPONENT_TESTDATA");
			objecttolookfor_val = rs.getString("COMPONENT_OBJECTTOLOOKFOR");
			waittime_val = rs.getString("COMPONENT_WAITTIME");
			
			out_event_name = out_event_name + event_name + "$$$$";
			out_object_name = out_object_name + object_name + "$$$$";
			out_testdata_val = out_testdata_val + testdata_val + "$$$$";
			out_objecttolookfor_val = out_objecttolookfor_val + objecttolookfor_val + "$$$$";
			out_waittime_val = out_waittime_val + waittime_val + "$$$$";			
		}
	
		out_object_name = out_object_name.replace("@@@", "'");
		out_testdata_val = out_testdata_val.replace("@@@", "'");
		out_objecttolookfor_val = out_objecttolookfor_val.replace("@@@", "'");
		
		fw_set_variable("NOSUB!!out_event_name_comp", out_event_name);
		fw_set_variable("NOSUB!!out_object_name_comp", out_object_name);
		fw_set_variable("NOSUB!!out_testdata_val_comp", out_testdata_val);
		fw_set_variable("NOSUB!!out_objecttolookfor_val_comp", out_objecttolookfor_val);
		fw_set_variable("NOSUB!!out_waittime_val_comp", out_waittime_val);
		
		String tot_test_case_steps = "" + total_test_case_steps;
		fw_set_variable("total_test_case_steps_comp", tot_test_case_steps);
		
		connection.close();

		log.fw_writeLogEntry("Get Test Component (Application: " + application_name + ", Component Name: " + component_name + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function is used to get a test step.
	 * 
	 * @since: 4/3/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public String fw_get_test_step(String variable_file, int index) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String out_value = fw_get_variable(variable_file);
		String[] out_value_arr = out_value.split("\\$\\$\\$\\$");
		String out_val = out_value_arr[index-1];	
		
		return out_val;
		
	}
	
	/**
	 * This function is used to get a test case.
	 * 
	 * @since: 4/3/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_get_test_case(String application_name, String test_id) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
				
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		//String sql_query = "select testcase_EVENTNAME, testcase_OBJECTNAME, testcase_TESTDATA, testcase_OBJECTTOLOOKFOR, testcase_WAITTIME from tbl_tctestcase where testcase_application = '" + application_name + "' and testcase_testid = '" + test_id + "' order by testcase_executionorder";
		String sql_query = "select testcase_EVENTNAME, testcase_OBJECTNAME, testcase_TESTDATA, testcase_OBJECTTOLOOKFOR, testcase_WAITTIME from tbl_tctestcase where testcase_application = '" + application_name + "' and testcase_testid = '" + test_id + "' and (testcase_skipflag != 'Y' or testcase_skipflag is null) order by testcase_executionorder";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Test Case Found in TBL_TESTCASE table for this Application/TestID";
		
		String event_name = "";
		String object_name = "";
		String testdata_val = "";
		String objecttolookfor_val = "";
		String waittime_val = "";
		
		String out_event_name = "";
		String out_object_name = "";
		String out_testdata_val = "";
		String out_objecttolookfor_val = "";
		String out_waittime_val = "";
				
		int total_test_case_steps = 0;
		
		while (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			
			total_test_case_steps = total_test_case_steps + 1;
			
			event_name = rs.getString("testcase_EVENTNAME");
			object_name = rs.getString("testcase_OBJECTNAME");
			testdata_val = rs.getString("testcase_TESTDATA");
			objecttolookfor_val = rs.getString("testcase_OBJECTTOLOOKFOR");
			waittime_val = rs.getString("testcase_WAITTIME");
			
			out_event_name = out_event_name + event_name + "$$$$";
			out_object_name = out_object_name + object_name + "$$$$";
			out_testdata_val = out_testdata_val + testdata_val + "$$$$";
			out_objecttolookfor_val = out_objecttolookfor_val + objecttolookfor_val + "$$$$";
			out_waittime_val = out_waittime_val + waittime_val + "$$$$";			
		}
		
		out_object_name = out_object_name.replace("@@@", "'");
		out_testdata_val = out_testdata_val.replace("@@@", "'");
		out_objecttolookfor_val = out_objecttolookfor_val.replace("@@@", "'");
		
		fw_set_variable("NOSUB!!out_event_name", out_event_name);
		fw_set_variable("NOSUB!!out_object_name", out_object_name);
		fw_set_variable("NOSUB!!out_testdata_val", out_testdata_val);
		fw_set_variable("NOSUB!!out_objecttolookfor_val", out_objecttolookfor_val);
		fw_set_variable("NOSUB!!out_waittime_val", out_waittime_val);
				
		String tot_test_case_steps = "" + total_test_case_steps;
		fw_set_variable("total_test_case_steps", tot_test_case_steps);
		
		connection.close();

		log.fw_writeLogEntry("Get Test Case (Application: " + application_name + ", Test ID: " + test_id + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function is used to get a device.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 3/27/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_testdata_get_device(String device_real_fake_flag, String device_type, String device_port_type, String index_value) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		
		String env_value = fw_get_variable("ENVSELECTED");
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		if (device_type.equals("TPSIMTA") && env_value.equals("PROD") && device_real_fake_flag.equals("R"))
		{
			sql_query = "select * from (select * from (select * from tbl_device where device_available_flag = 'Y' and device_type = '" + device_type + "' and device_real_fake_flag = '" + device_real_fake_flag + "' and device_port_type = '" + device_port_type + "' and device_environment = 'PROD' union select * from tbl_device where device_available_flag = 'N' and device_type = '" + device_type + "' and device_real_fake_flag = '" + device_real_fake_flag + "' and device_port_type = '" + device_port_type + "' and device_environment = 'PROD' and device_last_modified_date < sysdate - interval '1' hour) order by device_available_flag desc ) where rownum = 1";
		}
		else if (device_type.equals("MTA") && env_value.equals("PROD") && device_real_fake_flag.equals("R"))
		{
			sql_query = "select * from (select * from (select * from tbl_device where device_available_flag = 'Y' and device_type = '" + device_type + "' and device_real_fake_flag = '" + device_real_fake_flag + "' and device_port_type = '" + device_port_type + "' and device_environment = 'PROD' union select * from tbl_device where device_available_flag = 'N' and device_type = '" + device_type + "' and device_real_fake_flag = '" + device_real_fake_flag + "' and device_port_type = '" + device_port_type + "' and device_environment = 'PROD' and device_last_modified_date < sysdate - interval '1' hour) order by device_available_flag desc ) where rownum = 1";
		}
		else if (device_type.equals("MTA") && !env_value.equals("PROD") && device_real_fake_flag.equals("R"))
		{
			sql_query = "select * from (select * from (select * from tbl_device where device_available_flag = 'Y' and device_type = '" + device_type + "' and device_real_fake_flag = '" + device_real_fake_flag + "' and device_port_type = '" + device_port_type + "' and device_environment <> 'PROD' union select * from tbl_device where device_available_flag = 'N' and device_type = '" + device_type + "' and device_real_fake_flag = '" + device_real_fake_flag + "' and device_port_type = '" + device_port_type + "' and device_environment <> 'PROD' and device_last_modified_date < sysdate - interval '1' hour) order by device_available_flag desc ) where rownum = 1";
		}
		//else if (device_type.equals("MTA"))
		else if (device_type.equals("MTA") || device_type.equals("M1G"))
		{
			sql_query = "select device_auto_id, device_cmmac, device_mtamac from tbl_device where device_available_flag = 'Y' and device_type = '" + device_type + "' and device_real_fake_flag = '" + device_real_fake_flag + "' and device_port_type = '" + device_port_type + "' and device_environment <> 'PROD' and rownum = 1";
		}
		else if (device_type.substring(0, 1).equals("T") || device_type.substring(0, 1).equals("M") || device_type.substring(0, 1).equals("D"))
		{
			sql_query = "select device_auto_id, device_video_serial_number, device_video_estbmac, device_video_ecmmac from tbl_device where device_available_flag = 'Y' and device_type = '" + device_type + "' and device_real_fake_flag = '" + device_real_fake_flag + "' and device_environment = '" + env_value + "'	and rownum = 1";
		}
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Device Found in TBL_DEVICE table";

		String out_device_cmmac = "";
		String out_device_mtamac = "";
		
		String out_device_video_serial_number = "";
		String out_device_video_estbmac = "";
		String out_device_video_ecmmac = "";

		String out_device_info = "";
		
		if (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			int autoid = rs.getInt("device_auto_id");
			
			//if (device_type.equals("MTA") || device_type.equals("TPSIMTA"))
			if (device_type.equals("MTA") || device_type.equals("TPSIMTA") || device_type.equals("M1G"))
			{
				out_device_cmmac = rs.getString("device_cmmac");
				out_device_mtamac = rs.getString("device_mtamac");
				fw_set_variable("devicecmmac" + index_value, out_device_cmmac);
				fw_set_variable("devicemtamac" + index_value, out_device_mtamac);
				out_device_info = ", Device CMMAC: " + out_device_cmmac + ", Device MTAMAC: " + out_device_mtamac;	
			}
			else if (device_type.substring(0, 1).equals("T") || device_type.substring(0, 1).equals("M") || device_type.substring(0, 1).equals("D"))
			{
				out_device_video_serial_number = rs.getString("device_video_serial_number");
				out_device_video_estbmac = rs.getString("device_video_estbmac");
				out_device_video_ecmmac = rs.getString("device_video_ecmmac");
				fw_set_variable("devicevideoserialnumber" + index_value, out_device_video_serial_number);
				fw_set_variable("devicevideoestbmac" + index_value, out_device_video_estbmac);
				fw_set_variable("devicevideoecmmac" + index_value, out_device_video_ecmmac);
				out_device_info = ", Device Video Serial Number: " + out_device_video_serial_number + ", Device Video ESTBMAC: " + out_device_video_estbmac + ", Device Video ECMMAC: " + out_device_video_ecmmac;
			}
			
			String sql_update = "update tbl_device set device_available_flag = 'N', device_comments = 'updated device_available flag to N', device_last_modified_date = sysdate where device_auto_id = " + autoid;
			
			ResultSet rs2 = null;
			Statement st2 = connection.createStatement();
			rs2 = st2.executeQuery(sql_update);
			
		}
	
		connection.close();

		log.fw_writeLogEntry("Get Device (Real or Fake: " + device_real_fake_flag + ", Device Type: " + device_type + ", Device Port Type: " + device_port_type + out_device_info + ", Index: " + index_value + ")" + text_msg, rc_val);
		
	}
	
	/**
	 * This function is used to execute sql query.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 3/7/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_execute_sql(String data_source_name, String sql_query, String values_to_get) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{

		String ret_cd = "";
		
		for (int x=1;x<10;x++)
		{
			int left_caret_pos = sql_query.indexOf("<");
			
			if (left_caret_pos == -1)
			{
				break;
			}
			else
			{
				int right_caret_pos = sql_query.indexOf(">");
				String file_name = sql_query.substring(left_caret_pos + 1, right_caret_pos);
				String entity_value = fw_get_variable(file_name);
				
				String first_string = sql_query.substring(0, left_caret_pos);
				String second_string = entity_value;
				String third_string = sql_query.substring(right_caret_pos + 1, sql_query.length());

				sql_query = first_string + second_string + third_string;
				
			}
		}
		
		String datasource = fw_get_variable("ENV" + data_source_name + "DATASOURCE");
		String userName = fw_get_variable("ENV" + data_source_name + "USERID");
		String password = fw_get_variable("ENV" + data_source_name + "PASSID");
		
		String connection_string = "jdbc:oracle:thin:@" + datasource;
		
		try
		{

			ret_cd = "0";
			
			Connection connection = null;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(connection_string, userName, password);
			
			ResultSet rs = null;
			Statement st = connection.createStatement();
			rs = st.executeQuery(sql_query);
			
			String out_values = "";
			String value_current = "";
			String out_value_current = "";
			
			if (!sql_query.toUpperCase().contains("DELETE "))
			{
				rs.next();
				
				String[] values_to_get_arr = values_to_get.split(",");
				for (int x=0;x<values_to_get_arr.length;x++)
				{
					value_current = values_to_get_arr[x];
					out_value_current = rs.getString(value_current);
					out_values = out_values + out_value_current + ",";
					fw_set_variable(value_current, out_value_current);
				}
			}
			connection.close();
			
			log.fw_writeLogEntry("Execute SQL (Data Source Name: " + data_source_name + ", Columns: " + values_to_get + ", Values: " + out_values + ", SQL Query: " + sql_query + ")", ret_cd);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);
			log.fw_writeLogEntry("Execute SQL (Data Source Name: " + data_source_name + ", Columns: " + values_to_get + ", SQL Query: " + sql_query + ") - " + exception_string, ret_cd);
		}
		
	}
	
	/**
	 * This function is used to execute sql query.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 3/7/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void fw_execute_sql_includesDELETE(String data_source_name, String sql_query, String values_to_get) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{

		String ret_cd = "";
		
		for (int x=1;x<10;x++)
		{
			int left_caret_pos = sql_query.indexOf("<");
			
			if (left_caret_pos == -1)
			{
				break;
			}
			else
			{
				int right_caret_pos = sql_query.indexOf(">");
				String file_name = sql_query.substring(left_caret_pos + 1, right_caret_pos);
				String entity_value = fw_get_variable(file_name);
				
				String first_string = sql_query.substring(0, left_caret_pos);
				String second_string = entity_value;
				String third_string = sql_query.substring(right_caret_pos + 1, sql_query.length());

				sql_query = first_string + second_string + third_string;
				
			}
		}
		
		System.out.println("SQL Query: " + sql_query);
		
		String datasource = fw_get_variable("ENV" + data_source_name + "DATASOURCE");
		String userName = fw_get_variable("ENV" + data_source_name + "USERID");
		String password = fw_get_variable("ENV" + data_source_name + "PASSID");
		
		String connection_string = "jdbc:oracle:thin:@" + datasource;
		
		String out_values = "";
		
		try
		{

			ret_cd = "0";
			
			Connection connection = null;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(connection_string, userName, password);
		
			// NEW CODE to support Delete operation
			
			if (sql_query.toUpperCase().contains("DELETE "))
			{
				String[] delete_sql_arr;
				String select_sql_query;
				
				delete_sql_arr = sql_query.split("DELETE ");
				select_sql_query = "select count(*) cntval " + delete_sql_arr[0];
				
				ResultSet rs = null;
				Statement st = connection.createStatement();
				rs = st.executeQuery(sql_query);
				rs.next();
				String record_cnt = rs.getString("cntval").toString();
				
				if (!record_cnt.equals("0"))
				{
					ResultSet rs2 = null;
					Statement st2 = connection.createStatement();
					rs2 = st2.executeQuery(sql_query);
				}

				// END OF NEW CODE to support Delete operation
				
			}
			else
			{
			
				ResultSet rs = null;
				Statement st = connection.createStatement();
				rs = st.executeQuery(sql_query);
				rs.next();
				
				String value_current = "";
				String out_value_current = "";
				String[] values_to_get_arr = values_to_get.split(",");
				for (int x=0;x<values_to_get_arr.length;x++)
				{
					value_current = values_to_get_arr[x];
					out_value_current = rs.getString(value_current);
					out_values = out_values + out_value_current + ",";
					fw_set_variable(value_current, out_value_current);
				}
			
			}
			
			connection.close();
			
			log.fw_writeLogEntry("Execute SQL (Data Source Name: " + data_source_name + ", Columns: " + values_to_get + ", Values: " + out_values + ", SQL Query: " + sql_query + ")", ret_cd);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);
			log.fw_writeLogEntry("Execute SQL (Data Source Name: " + data_source_name + ", Columns: " + values_to_get + ", SQL Query: " + sql_query + ") - " + exception_string, ret_cd);
		}
		
	}
	
	/**
	 * This function will terminate all processes.
	 * The input into this function is a list of processes comma delimited to be terminated.
	 * Example is "chrome.exe,excel.exe"
	 *  
	 * @param process_list
	 * @throws IOException
	 * @author Mark Elking
	 * @since 10/20/2016
	 * 
	 */
	
	public void fw_terminate_window_processes(String process_list) throws IOException 
	{
		
		String process_name = "";
		String[] process_list_arr = process_list.split(",");
		for (int x=0;x<process_list_arr.length;x++)
		{
			process_name = process_list_arr[x];
			Runtime.getRuntime().exec("taskkill /IM " + process_name + " /F");
		}
		
	}
	    
	/**
	 * This function launches a browser and defines Driver.
	 * The input required for this function is the browser type (example is IE or CHROME)
	 * 
	 * @param browsertype
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 10/26/2016
	 */
	
	public void fw_launch_browser(String browsertype) throws InterruptedException, IOException
	{
		String window_handle = "";
		
		String[] browsertype_arr = browsertype.split(",");
		browsertype = browsertype_arr[0];
		
		String proxytype = "NA";
		if (browsertype.contains(","))
		{
			proxytype = browsertype_arr[1];
		}
		
		String return_code = "";
		String failed_msg = "";
		
		if (browsertype.equalsIgnoreCase("IE"))
		{
			return_code = "0";
			System.setProperty("webdriver.ie.driver", "driver/IEDriverServer.exe");
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer(); 
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			caps.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);

			if (!proxytype.equals("NA"))
			{
				Proxy proxy=new Proxy();
				
				if (proxytype.equals("PROXY"))
				{
					proxy.setProxyType(ProxyType.DIRECT);
				}
				else if (proxytype.equals("NOPROXY"))
				{
					proxy.setNoProxy(null);
				}
				
				caps.setCapability(CapabilityType.PROXY, proxy);
				caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				
			}
			
			driverie = new InternetExplorerDriver(caps);
			fw_switch_to_driver("IE");
			window_handle = fw_get_window_handle();
			fw_set_variable("LAUNCH_BROWSER_WINDOW_HANDLE", window_handle);
			
		}
		else if (browsertype.equalsIgnoreCase("CHROME"))
		{
			return_code = "0";
			System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
			DesiredCapabilities caps = DesiredCapabilities.chrome();			
			caps.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);

			if (!proxytype.equals("NA"))
			{
				Proxy proxy=new Proxy();
				
				if (proxytype.equals("PROXY"))
				{
					proxy.setProxyType(ProxyType.DIRECT);
				}
				else if (proxytype.equals("NOPROXY"))
				{
					proxy.setNoProxy(null);
				}
				
				caps.setCapability(CapabilityType.PROXY, proxy);
				caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				
			}

			driverchrome = new ChromeDriver(caps);
			fw_switch_to_driver("CHROME");
			window_handle = fw_get_window_handle();
			fw_set_variable("LAUNCH_BROWSER_WINDOW_HANDLE", window_handle);
			
		}
		else 
		{		
			return_code = "1";
			failed_msg = " - Browser Type not defined.";	
		}
		
		log.fw_writeLogEntry("Launch Browser (Browser Type: " + browsertype + ")" + failed_msg, return_code);
		
	}

	/**
	 * 
	 * This function switches to driver.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/18/2017
	 * 
	 */
	
	public void fw_switch_to_driver(String browser_type) throws InterruptedException, IOException
	{
		
		if (browser_type.equalsIgnoreCase("IE"))
		{
			driver = driverie;
		}
		else if (browser_type.equalsIgnoreCase("CHROME"))
		{
			driver = driverchrome;
		}
		
		log.fw_writeLogEntry("Switch To Driver (Browser Type: " + browser_type + ")", "0");
		
	}
	
	/**
	 * 
	 * This function clicks the browser back button.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 3/14/2017
	 * 
	 */
	
	public void fw_browser_navigate_back() throws InterruptedException, IOException
	{
		
		driver.navigate().back();
		
		log.fw_writeLogEntry("Click Browser Back Button", "0");
		
	}
	
	/**
	 * 
	 * This function switches between frames.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/17/2017
	 * 
	 */
	
	public void fw_switch_frame(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException
	{

		String text_msg = "";
		String ret_cd = "0";
		
		try
		{
			
			WebElement webelement = driver.findElement(fw_get_element_object(locator, locatorvalue));		
			driver.switchTo().frame(webelement);
		
			log.fw_writeLogEntry("Switch To Frame (Object Name: " + fieldname, ret_cd);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);
			log.fw_writeLogEntry("Switch To Frame (Object Name: " + fieldname + ") - " + text_msg + " - " + exception_string, ret_cd);
			
		}
		
	}
	
	/**
	 * This function closes window.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/16/2017
	 * 
	 */
	
	public void fw_close_window() throws InterruptedException, IOException 
	{
		
		// Store the current window handle
		String winHandleBefore = driver.getWindowHandle();

		// Perform the click operation that opens new window

		// Switch to new window opened
		String window_closed = "";
				
		for(String winHandle : driver.getWindowHandles()){
			window_closed = winHandle;
		    driver.switchTo().window(winHandle);
		}

		// Perform the actions on new window

		// Close the new window, if that window no more required
		driver.close();

		// Switch back to original browser (first window)
		driver.switchTo().window(winHandleBefore);

		// Continue with original browser (first window)
		
		log.fw_writeLogEntry("Close Window (Handle: " + window_closed + ")", "0");
		
	}

	/**
	 * This function gets window handle.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/24/2017
	 * 
	 */
	
	public String fw_get_window_handle() throws InterruptedException, IOException 
	{
		
		window_handle = driver.getWindowHandle();
		fw_set_variable("WindowHandle", window_handle);
		
		log.fw_writeLogEntry("Get Window Handle (Handle: " + window_handle + ")", "0");
		
		return window_handle;
		
	}
	
	/**
	 * This function switch to window by title.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @since 6/16/2017
	 * 
	 */
	
	public void fw_switch_to_window_by_title(String window_title) throws InterruptedException, IOException, ClassNotFoundException, SQLException 
	{
		
		String found_flag = "no";
		String window_handle = "";
		String ret_cd = "";
		
		for(String winHandle : driver.getWindowHandles())
		{ 			
			if (driver.switchTo().window(winHandle).getTitle().equals(window_title)) 
			{
				found_flag = "yes";
				driver.switchTo().window(winHandle);
				window_handle = winHandle;
				break;
			}
		}
		
		if (found_flag.equals("yes"))
		{
			ret_cd = "0";
		}
		else
		{
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Switch to Window by Title (Title: " + window_title + ", Handle: " + window_handle + ")", ret_cd);
		
	}
	
	/**
	 * This function switches to new window.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/24/2017
	 * 
	 */
	
	public void fw_switch_to_new_window() throws InterruptedException, IOException 
	{
		
		String current_window_handle = "";
		int do_loop_cnt = 0;
		int for_loop_cnt = 0;
		String difference_flag = "";
		String get_window_handle = fw_get_variable("WindowHandle");
		String ret_cd = "";
		String text_msg = "";
		
		do
		{
			do_loop_cnt = do_loop_cnt + 1;
			
			difference_flag = "no";
			
			for_loop_cnt = 0;
			
			for(String winHandle : driver.getWindowHandles())
			{
				for_loop_cnt = for_loop_cnt + 1;
				
			    driver.switchTo().window(winHandle);
			    current_window_handle = winHandle;
			    
			    System.out.println(driver.switchTo().window(winHandle).getTitle());
			    
			    
			    if (get_window_handle.equals(current_window_handle))
				{
					Thread.sleep(5000);
					text_msg = "Window Handles are still equal.  Do Loop Count: " + do_loop_cnt + ", For Loop Count: " + for_loop_cnt;
					System.out.println(text_msg);
					ret_cd = "1";
				}
				else
				{
					difference_flag = "yes";
					text_msg = "Window Handles are different.  Breaking out of For Loop now.  Do Loop Count: " + do_loop_cnt + ", For Loop Count: " + for_loop_cnt;
					System.out.println(text_msg);
					ret_cd = "0";
					break;
				}
			    
			}
			
		}
		while(do_loop_cnt < 10 && difference_flag.equals("no"));
		
		log.fw_writeLogEntry("Switch to Window (Handle: " + current_window_handle + ") - " + text_msg, ret_cd);
		
	}
	
	/**
	 * This function switches to window based on window handle.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/24/2017
	 * 
	 */
	
	public void fw_switch_to_window(String window_handle_value) throws InterruptedException, IOException 
	{
		
		if (window_handle_value.contains("FILE_"))
		{
			String[] tc_test_data_array = window_handle_value.split("_");
			String tc_test_data_filename = tc_test_data_array[1];			
			window_handle_value = fw_get_variable(tc_test_data_filename);
		}
				
		driver.switchTo().window(window_handle_value);
		
		log.fw_writeLogEntry("Switch to Window (Handle: " + window_handle_value + ")", "0");
		
	}
	
	/**
	 * This function accepts an alert.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/19/2017
	 * 
	 */
	
	public void fw_accept_alert() throws InterruptedException, IOException 
	{
		
		driver.switchTo().alert().accept();
		
		log.fw_writeLogEntry("Accept Alert", "0");
		
	}
	
	/**
	 * This function navigates to a URL.
	 * 
	 * @param url
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 9/5/2016
	 * 
	 */
	
	public void fw_navigate_to_url(String url) throws InterruptedException, IOException 
	{
		
		driver.manage().window().maximize();
		driver.navigate().to(url);		
		
		log.fw_writeLogEntry("Navigate to URL (URL: " + url + ")", "0");
		
	}
	
	/**
	 * This function will login to SSO.
	 * 
	 * @param userid
	 * @param passid
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 2/23/2017
	 * 
	 */
	
	public void fw_login_to_SSO(String userid, String passid) throws InterruptedException, IOException 
	{
		
		String text_msg = "";
		
		String current_window_handle = "";
		for(String winHandle : driver.getWindowHandles())
		{
			driver.switchTo().window(winHandle);
		    current_window_handle = winHandle;
		}
		Thread.sleep(2000);
		
		String window_url = driver.switchTo().window(current_window_handle).getCurrentUrl();
		
		Thread.sleep(2000);
		
		if (window_url.contains("esso"))
		{
			fw_enter_data_into_text_field("LOGIN_Username", "input", "id", "username", userid, 0);
			fw_enter_data_into_text_field("LOGIN_Username", "input", "id", "password", passid, 0);
			fw_click_button("LOGIN_Username", "input", "name", "loginButton2", passid, 20000);
			
			String before_window_handle = fw_get_variable("LAUNCH_BROWSER_WINDOW_HANDLE");
			
			driver.switchTo().window(before_window_handle);
			
		}
		else
		{
			text_msg = " - SSO Login NOT Found.";
		}
		
		log.fw_writeLogEntry("Login to SSO (User ID: " + userid + ", Pass ID: " + passid + ")" + text_msg, "0");
		
	}
	
	/**
	 * This function enters data into a text field.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 10/26/2016
	 * 
	 */
	
	public void fw_enter_data_into_text_field(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
		
		String text_msg = "";
		String ret_cd = "0";
		
		try
		{
			
			if (fieldvalue.contains("FILE_"))
			{
				String[] tc_test_data_array = fieldvalue.split("_");
				String tc_test_data_filename = tc_test_data_array[1];
				fieldvalue = fw_get_variable(tc_test_data_filename);
			}
					
			String found_flag = "";
			
			if (fieldvalue.contains("KEYTAB"))
			{
				String[] fieldvalue_arr = fieldvalue.split(",");
				int num_tabs = Integer.valueOf(fieldvalue_arr[1]);
				for (int r=1;r<num_tabs+1;r++)
				{
					driver.switchTo().activeElement().sendKeys(Keys.TAB);
				}
				found_flag = "YES";
			}
			else if (fieldvalue.contains("KEYENTER"))
			{
				driver.switchTo().activeElement().sendKeys(Keys.ENTER);
				found_flag = "YES";
			}
			else if (fieldvalue.contains("KEYDATA"))
			{
				
				driver.findElement(By.xpath(locatorvalue)).click();
						
				String[] fieldvalue_arr = fieldvalue.split(",");
				String key_data = fieldvalue_arr[1];
				JavascriptExecutor executor = (JavascriptExecutor)driver;
				executor.executeScript("arguments[0].setAttribute('value','" + key_data + "');", driver.findElement(By.xpath(locatorvalue)));
				Thread.sleep(3000);
				found_flag = "YES";
				
			}
			else if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
				{
					found_flag = "YES";
					
					if (fieldvalue.contains("NOCLEAR"))
					{
						String[] fieldvalue_arr = fieldvalue.split(",");
						String fieldvalue_output = fieldvalue_arr[1];
						driver.findElement(fw_get_element_object(locator, locatorvalue)).sendKeys(fieldvalue_output);
					}
					else
					{
						driver.findElement(fw_get_element_object(locator, locatorvalue)).clear();
						driver.findElement(fw_get_element_object(locator, locatorvalue)).sendKeys(fieldvalue);
					}
				}
			}
			else if (locator.equals("NA")) 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(locatorvalue))
					{
						found_flag = "YES";
						item.clear();
						item.sendKeys(fieldvalue);
						break;
					}
				}
			}
			else 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getAttribute(locator);
					
					if (label.contains(locatorvalue))
					{
						found_flag = "YES";
						item.clear();
						item.sendKeys(fieldvalue);
						break;
					}
				}
			}
			
			if (found_flag.equals("YES"))
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** Textbox NOT Found ****************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Enter Data into Textbox (Name: " + fieldname + ", Value: " + fieldvalue + ")" + text_msg, ret_cd);
		
			Thread.sleep(milliseconds_to_wait);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);
			log.fw_writeLogEntry("Enter Data into Textbox (Name: " + fieldname + ", Value: " + fieldvalue + ") - " + text_msg + " - " + exception_string, ret_cd);
			
		}
		
	}
	
	/**
	 * This function selects checkbox.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/18/2017
	 * 
	 */
	
	public void fw_select_checkbox(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		String text_msg = "";
		String ret_cd = "0";
		String found_flag = "";
		
		try
		{
			
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
				{
					found_flag = "YES";
					driver.findElement(fw_get_element_object(locator, locatorvalue)).click();
				}
			}
			else if (locator.equals("NA")) 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(locatorvalue))
					{
						found_flag = "YES";
						item.click();
						break;
					}
				}
			}
			else 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getAttribute(locator);
					
					if (label.contains(locatorvalue))
					{
						found_flag = "YES";
						item.click();
						break;
					}
				}
			}
			
			if (found_flag.equals("YES"))
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** Checkbox NOT Found ****************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Select Checkbox (Name: " + fieldname + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Select Checkbox (Name: " + fieldname + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
	}

	/**
	 * This function clicks element using Javascript.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/17/2017
	 * 
	 */
	
	public void fw_click_element_using_javascript(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		String rc_val = "";
		String text_msg = "";
		
		try
		{
			
			if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
			{
				rc_val = "0";
				
				WebElement webelement = driver.findElement(fw_get_element_object(locator, locatorvalue));
				JavascriptExecutor js0 = (JavascriptExecutor) driver;
				js0.executeScript("arguments[0].click();", webelement);
			}
			else
			{
				rc_val = "1";
				text_msg = " - Object Not Found";
			}
			
			log.fw_writeLogEntry("Click Element Using Javascript (Name: " + fieldname + ")" + text_msg, rc_val);
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			rc_val = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Click Element Using Javascript (Name: " + fieldname + ") - " + text_msg + " - " + exception_string, rc_val);
			
		}
		
	}

	/**
	 * This function will get a webelements object.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/18/2017
	 * 
	 */
	
	public List<WebElement> fw_get_webelements_object(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		String text_msg = "";
		String ret_cd = "0";
		String found_flag = "";
		List<WebElement> webelement = null;
		
		try
		{
					
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
				{
					found_flag = "YES";
					webelement = driver.findElements(fw_get_element_object(locator, locatorvalue));
				}
			}
			else if (!locator.equals("NA"))  
			{
				found_flag = "YES";
				webelement = driver.findElements(By.tagName(tagname));
			}
			
			if (found_flag.equals("YES"))
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** WebElements Object NOT Found ****************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Get WebElement Object (Name: " + fieldname + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Get WebElement Object (Name: " + fieldname + ") - " + text_msg + " - " + exception_string, ret_cd);			
		}
		
		return webelement;
		
	}
	
	// ******************************************************************************
	// *
	// * Name:        fw_get_element_object
	// * Author:      Gaurav Kumar
	// * Date:   	  11/16/2016
	// * Description: Method to get By object according to passed 
	// *              parameter values for locator
	// *
	// ******************************************************************************

	public By fw_get_element_object(String locName, String locValue) 
	{

		if (locName.equalsIgnoreCase("XPATH")) 
		{
			return By.xpath(locValue);
		}
		if (locName.equalsIgnoreCase("ID")) 
		{
			return By.id(locValue);
		}
		else if (locName.equalsIgnoreCase("CLASS")) 
		{
			return By.className(locValue);
		}
		else if (locName.equalsIgnoreCase("NAME")) 
		{
			return By.name(locValue);
		}
		else if (locName.equalsIgnoreCase("CSS")) 
		{
			return By.cssSelector(locValue);
		}
		else if (locName.equalsIgnoreCase("LINK")) 
		{
			return By.linkText(locValue);
		}
		else if (locName.equalsIgnoreCase("PARTIALLINK")) 
		{
			return By.partialLinkText(locValue);
		} 
		else 
		{
			System.out.println("Wrong entry ");
		}
		
		return null;

	}	
		
	/**
	 * This function clicks a button.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 10/26/2016
	 * 
	 */
	
	public void fw_click_button(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		String text_msg = "";
		String ret_cd = "0";
		
		try
		{
			
			String button_ready_flag = "";
			boolean button_isEmpty;
			boolean button_isEnabled;
			
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				for (int m=1;m<10;m++)
				{
					button_isEmpty = driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty();
					button_isEnabled = driver.findElement(fw_get_element_object(locator, locatorvalue)).isEnabled();
					
					if(!button_isEmpty && button_isEnabled)
					{
						button_ready_flag = "YES";
						driver.findElement(fw_get_element_object(locator, locatorvalue)).click();
						break;
					}
					
					Thread.sleep(1000);
					
				}
			}
			else if (locator.equals("NA")) 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(locatorvalue))
					{
						button_isEnabled = item.isEnabled();
						if (button_isEnabled)
						{
							item.click();
							button_ready_flag = "YES";
						}
						else
						{
							button_ready_flag = "NO";
						}
						
						break;
					}
				}
			}
			else 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getAttribute(locator);
					
					if (label.contains(locatorvalue))
					{
						button_isEnabled = item.isEnabled();
						if (button_isEnabled)
						{
							item.click();
							button_ready_flag = "YES";
						}
						else
						{
							button_ready_flag = "NO";
						}
						
						break;
					}
				}
			}
			
			if (button_ready_flag.equals("YES"))
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** Button NOT Found or Button Disabled ****************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Click Button (Name: " + fieldname + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Click Button (Name: " + fieldname + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
	}

	/**
	 * This function clicks on a textbox field and enters data into that textbox.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 2/28/2017
	 * 
	 */
	
	public void fw_click_and_enter_data(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		String text_msg = "";
		String ret_cd = "0";
		
		try
		{
			
			if (fieldvalue.contains("FILE_"))
			{
				String[] tc_test_data_array = fieldvalue.split("_");
				String tc_test_data_filename = tc_test_data_array[1];
				fieldvalue = fw_get_variable(tc_test_data_filename);
			}
					
			String field_ready_flag = "";
			boolean field_isEmpty;
			boolean field_isEnabled;
			
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				for (int m=1;m<10;m++)
				{
					field_isEmpty = driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty();
					field_isEnabled = driver.findElement(fw_get_element_object(locator, locatorvalue)).isEnabled();
					
					if(!field_isEmpty && field_isEnabled)
					{
						field_ready_flag = "YES";
						driver.findElement(fw_get_element_object(locator, locatorvalue)).click();
						driver.switchTo().activeElement().sendKeys(fieldvalue);
						break;
					}
					
					Thread.sleep(1000);
					
				}
			}
			else if (locator.equals("NA")) 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(locatorvalue))
					{
						field_isEnabled = item.isEnabled();
						if (field_isEnabled)
						{
							item.click();
							driver.switchTo().activeElement().sendKeys(fieldvalue);
							field_ready_flag = "YES";
						}
						else
						{
							field_ready_flag = "NO";
						}
						
						break;
					}
				}
			}
			else 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getAttribute(locator);
					
					if (label.contains(locatorvalue))
					{
						field_isEnabled = item.isEnabled();
						if (field_isEnabled)
						{
							item.click();
							driver.switchTo().activeElement().sendKeys(fieldvalue);
							field_ready_flag = "YES";
						}
						else
						{
							field_ready_flag = "NO";
						}
						
						break;
					}
				}
			}
			
			if (field_ready_flag.equals("YES"))
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** Field NOT Found or Field Disabled ****************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Click and Enter Data into Textbox (Name: " + fieldname + ", Value: " + fieldvalue + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Click and Enter Data into Textbox (Name: " + fieldname + ", Value: " + fieldvalue + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
	}
	
	/**
	 * This function selects a value from a list by value.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 10/29/2016
	 * 
	 */
	
	public void fw_select_from_a_list_by_value(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
				
		String text_msg = "";
		String ret_cd = "0";
		
		try
		{
			
			String found_flag = "";
			
			if (fieldvalue.contains("FILE_"))
			{
				String[] tc_test_data_array = fieldvalue.split("_");
				String tc_test_data_filename = tc_test_data_array[1];
				fieldvalue = fw_get_variable(tc_test_data_filename);
			}
						
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
				{
					found_flag = "YES";
					WebElement element = driver.findElement(fw_get_element_object(locator, locatorvalue));
					Select listbox = new Select(element);
			        listbox.selectByValue(fieldvalue);
				}
			}
			else if (locator.equals("NA")) 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(fieldvalue))
					{
						found_flag = "YES";
						item.click();
						break;
					}
				}
			}
			else 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getAttribute(locator);
					
					if (label.contains(locatorvalue))
					{
						found_flag = "YES";
						WebElement element = item;
						Select listbox = new Select(element);
				        listbox.selectByValue(fieldvalue);
						break;
					}
				}
			}
			
			if (found_flag.equals("YES"))
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** Listbox NOT Found ****************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Select from a List By Value (Name: " + fieldname + ", Value: " + fieldvalue + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Select from a List By Value (Name: " + fieldname + ", Value: " + fieldvalue + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
	}

	/**
	 * This function selects a value from a list by visible text.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/17/2017
	 * 
	 */
	
	public void fw_select_from_a_list_by_visible_text(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
				
		String text_msg = "";
		String ret_cd = "0";
		
		try
		{
			
			String found_flag = "";
			
			if (fieldvalue.contains("FILE_"))
			{
				String[] tc_test_data_array = fieldvalue.split("_");
				String tc_test_data_filename = tc_test_data_array[1];
				fieldvalue = fw_get_variable(tc_test_data_filename);
			}
					
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
				{
					found_flag = "YES";
					WebElement element = driver.findElement(fw_get_element_object(locator, locatorvalue));
					Select listbox = new Select(element);
			        listbox.selectByVisibleText(fieldvalue);
				}
			}
			else if (locator.equals("NA")) 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(fieldvalue))
					{
						found_flag = "YES";
						item.click();
						break;
					}
				}
			}
			else 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getAttribute(locator);
					
					if (label.contains(locatorvalue))
					{
						found_flag = "YES";
						WebElement element = item;
						Select listbox = new Select(element);
				        listbox.selectByVisibleText(fieldvalue);
						break;
					}
				}
			}
			
			if (found_flag.equals("YES"))
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** Listbox NOT Found ****************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Select from a List By Visible Text (Name: " + fieldname + ", Value: " + fieldvalue + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Select from a List By Visible Text (Name: " + fieldname + ", Value: " + fieldvalue + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
	}
	
	//******************************************************************************
	//*
	//*	Name: 	fw_check_for_loading_page
	//*	Author: Mark Elking
	//*	Date: 	10/26/2016
	//*
	//******************************************************************************
	
	public String fw_check_for_loading_page(String tagname, String text_to_look_for, String number_loops, String milliseconds_to_wait_per_loop, String attribute_name) throws InterruptedException, IOException 
	{
		
		String text_msg = "";
		String ret_cd = "0";
		String found_flag = "no";
		long total_milliseconds = 0;
		
		// hey
		/*
		JavascriptExecutor js = (JavascriptExecutor)driver;
		
		for (int i=0; i<25; i++)
		{ 
		   try 
		   {
			   System.out.println("Document Ready Status:" + js.executeScript("return document.readyState").toString());
			   Thread.sleep(1000);
		   }catch (InterruptedException e) {} 
		   
		   //To check page ready state.
		   if (js.executeScript("return document.readyState").toString().equals("complete"))
		   { 
			   break; 
		   }   
		}
		*/
		// End of hey
				
		try
		{
				
			String[] waitandtagname_arr = tagname.split("--");
			long initial_milliseconds_to_wait = 0;
			
			if (waitandtagname_arr.length == 2)
			{
				initial_milliseconds_to_wait = Long.valueOf(waitandtagname_arr[0]);
				Thread.sleep(initial_milliseconds_to_wait);
				tagname = waitandtagname_arr[1];	
			}
			else
			{
				tagname = waitandtagname_arr[0];
			}
			
			long ms_to_wait_per_loop;
			ms_to_wait_per_loop = Long.valueOf(milliseconds_to_wait_per_loop);
			
			int num_loops;
			num_loops = Integer.valueOf(number_loops);
			
			int x;
			
			if (attribute_name.equals("NA"))
			{
			
				for (x=1;x<num_loops;x++)
				{	
					
					Thread.sleep(ms_to_wait_per_loop);
					
					List<WebElement> rows = driver.findElements(By.tagName(tagname));		
					Iterator<WebElement> iter = rows.iterator();
									
					while (iter.hasNext()) {
						WebElement item = iter.next();
						String label = item.getText().trim();
						
						if (label.contains(text_to_look_for))
						{
							found_flag = "yes";
							total_milliseconds = ms_to_wait_per_loop * x;
							break;
						}
					}
				
					if (found_flag.equals("yes"))
					{
						break;
					}
					else
					{
						Thread.sleep(ms_to_wait_per_loop);
					}
				}
			
			}
			else
			{
				
				for (x=1;x<num_loops;x++)
				{	
					
					List<WebElement> rows = driver.findElements(By.tagName(tagname));		
					Iterator<WebElement> iter = rows.iterator();
					
					while (iter.hasNext()) {
						WebElement item = iter.next();
						String label = item.getAttribute(attribute_name).trim();
						
						if (label.contains(text_to_look_for))
						{
							found_flag = "yes";
							total_milliseconds = ms_to_wait_per_loop * x;
							break;
						}
					}
					
					if (found_flag.equals("yes"))
					{
						break;
					}
					else
					{
						Thread.sleep(ms_to_wait_per_loop);
					}
				
				}
				
			}
			
			if (found_flag == "yes")
			{
				ret_cd = "0";
			}
			else
			{
				text_msg = "*************** Tag NOT Found ********************";
				ret_cd = "1";
			}
			
			log.fw_writeLogEntry("Check for Loading Page (FOUND, Tag: " + tagname + ", Text: " + text_to_look_for + ", # Loops: " + number_loops + ", Msecs to Wait per Loop: " + milliseconds_to_wait_per_loop + ", Msecs Elapsed: " + total_milliseconds + ", Attribute Name: " + attribute_name + ")" + text_msg, ret_cd);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Check for Loading Page (Tag: " + tagname + ", Text: " + text_to_look_for + ", # Loops: " + number_loops + ", Msecs to Wait per Loop: " + milliseconds_to_wait_per_loop + ", Msecs Elapsed: " + total_milliseconds + ", Attribute Name: " + attribute_name + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
		return found_flag;
		
	}

	/**
	 * This function gets text from page.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 12/01/2016
	 * 
	 */
	
	public String fw_get_text(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
				
		return_get_text = "";
		String text_msg = "";
		String ret_cd = "0";
		String ready_flag = "";
		boolean field_isEmpty;
		boolean field_isEnabled;
		String outputfile = "";
		
		try
		{
					
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				for (int m=1;m<10;m++)
				{
					field_isEmpty = driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty();
					field_isEnabled = driver.findElement(fw_get_element_object(locator, locatorvalue)).isEnabled();
					
					if(!field_isEmpty && field_isEnabled)
					{
						ready_flag = "YES";
						return_get_text = driver.findElement(fw_get_element_object(locator, locatorvalue)).getText();
						break;
					}
					
					Thread.sleep(1000);
					
				}
			}
			else if (locator.equals("NA")) 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(locatorvalue))
					{
						ready_flag = "YES";
						return_get_text = item.getText();
						break;
					}
				}
			}
			else 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getAttribute(locator);
					
					if (label.contains(locatorvalue))
					{
						ready_flag = "YES";
						return_get_text = item.getText();
						break;
					}
				}
			}
			
			if (ready_flag.equals("YES"))
			{
				ret_cd = "0";
				
				if (fieldvalue.contains("FILE--"))
				{
					String[] fieldvalue_arr_value = fieldvalue.split("--");
					outputfile = fieldvalue_arr_value[1];
				}
				else
				{
					outputfile = "GETTEXT" + fieldname;
				}
				
				fw_set_variable(outputfile, return_get_text);
			}
			else
			{
				text_msg = "*************** Field NOT Found or Field Disabled ****************";
				ret_cd = "1";
			}
					
			log.fw_writeLogEntry("Get Text (Name: " + fieldname + ", Value: " + fieldvalue + ", Text: " + return_get_text + ")" + text_msg, ret_cd);
				
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Get Text (Name: " + fieldname + ", Value: " + fieldvalue + ", Text: " + return_get_text + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
		return return_get_text;
		
	}
	
	/**
	 * This function quits the driver.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 12/09/2016
	 * 
	 */
	
	public void fw_quit_driver() throws InterruptedException, IOException
	{

		try
		{
			driver.close();
			driver.quit();
		}
		catch(Exception  e)
		{
			e.printStackTrace();
		}
		
		log.fw_writeLogEntry("Quit Driver", "0");
		
	}

	/**
	 * This function validates text.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 1/23/2017
	 * 
	 */
	
	public void fw_validate_text(String fieldname, String expected_text, String actual_text) throws InterruptedException, IOException
	{
		
		String ret_cd = "";
		String text_msg = "";
		
		if (actual_text.contains(expected_text))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** Expected Text NOT Contained in Actual Text ****************";
			ret_cd = "1";
			
			String execution_flag = fw_get_variable("stopexecutionuponfailure").trim();
			if (execution_flag.equals("yes"))
			{
				log.fw_writeLogEntry(" ", "NA");
				log.fw_writeLogEntry(" ***** Stopping Execution ****** ", "NA");
				log.fw_writeLogEntry(" ", "NA");
				System.exit(0);
			}

		}
				
		log.fw_writeLogEntry("Validate Text (Name: " + fieldname + ", Expected: " + expected_text + ", Actual: " + actual_text + ")" + text_msg, ret_cd);
		
	}
	
	/**
	 * This function sets the gets worksheet list.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 3/31/2017
	 * 
	 */
	
	public String fw_get_worksheet_list(String test_case_id) throws IOException, InterruptedException 
	{
		String worksheetlist = fw_get_variable("worksheetlist");
		String testidslist = fw_get_variable("testids");
		String cur_test_id = "";
		String worksheet_name = "";
		
		String[] testidslist_arr = testidslist.split(",");
		String[] worksheetlist_arr = worksheetlist.split(",");
		int testidslist_len = testidslist_arr.length;
		for (int i=0;i<testidslist_len;i++)
		{
			cur_test_id = testidslist_arr[i];
			if (cur_test_id.contains(test_case_id))
			{
				worksheet_name = worksheetlist_arr[i];
				break;
			}
		}
		
		return worksheet_name;
						
	}
	
	/**
	 * 
	 * * This function creates environment variables.
	 * 
	 * @param environment_value
	 * @author Mark Elking
	 * @since 3/6/2017
	 * @return 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	 
	public void fw_create_environment_variables(String environment_value) throws IOException, InterruptedException, ClassNotFoundException, SQLException
	{
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("ENVIRONMENT PARMS","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
		String appname = fw_get_variable("appname");
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		sql_query = "select environment_columnheader, environment_columnvalue from tbl_environment where environment_application = '" + appname + "' and environment_name = '" + environment_value + "'";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Environment Info Found in TBL_ENVIRONMENT table";
		String environment_columnheader = "";
		String environment_columnvalue = "";
		
		while (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			
			environment_columnheader = rs.getString("environment_columnheader");
			environment_columnvalue = rs.getString("environment_columnvalue");
			
			if (environment_columnvalue == null)
			{
				environment_columnvalue = "";
			}
		
			fw_set_variable("ENV" + environment_columnheader, environment_columnvalue);
			
			
		}
	
		connection.close();

		
		if (rc_val.equals("1"))
		{
			text_msg = "***** Environment value NOT found ****";
		}
		
		log.fw_writeLogEntry("Create Environment Variables (Environment: " + environment_value + ", App: " + appname + ")" + text_msg, rc_val);		
		
	}
	
	/**
	 * 
	 * * This function creates environment variables.
	 * 
	 * @param environment_value
	 * @author Mark Elking
	 * @since 3/6/2017
	 * @return 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	 
	public void fw_create_environment_variables_TN(String environment_value) throws IOException, InterruptedException, ClassNotFoundException, SQLException
	{
		String appname = fw_get_variable("appname");
		
		String sql_query = "";
		
		String userName = fw_get_variable("usernameTESTDB");
		String password = fw_get_variable("passwordTESTDB");
		String connection_string = fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		sql_query = "select environment_columnheader, environment_columnvalue from tbl_environment where environment_application = '" + appname + "' and environment_name = '" + environment_value + "'";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String rc_val = "1";
		String text_msg = " - No Environment Info Found in TBL_ENVIRONMENT table";
		String environment_columnheader = "";
		String environment_columnvalue = "";
		
		while (rs.next())
		{
			rc_val = "0";
			text_msg = "";
			
			environment_columnheader = rs.getString("environment_columnheader");
			environment_columnvalue = rs.getString("environment_columnvalue");
			
			if (environment_columnvalue == null)
			{
				environment_columnvalue = "";
			}
		
			fw_set_variable("SKIP--ENV" + environment_columnheader, environment_columnvalue);
			
			
		}
	
		connection.close();

		if (rc_val.equals("1"))
		{
			text_msg = "***** Environment value NOT found ****";
		}		
		
	}
	
	/**
	 * This function takes object name and performs respective webui event on that object.
	 * 
	 * @param tab_name
	 * @param event_name
	 * @param in_object_key_name
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @since 12/27/2016
	 */
	
	public void fw_event (String configuration_map_fullpath, String tab_name, String tc_event_name, String tc_object_name, String tc_test_data, String object_to_look_for_after_object_event, String milliseconds_to_wait_after_object_event) throws InterruptedException, IOException, ParseException, NoSuchAlgorithmException, KeyManagementException, ClassNotFoundException, SQLException
	{
		
		String object_objectname = "";
		String object_tagnameattributevalue = "";
		String object_extrainfo = "";
		String object_tagname = "";
		String object_attribute = "";
		String object_attributevalue = "";
		
		String workspace_name = fw_get_workspace_name();
		String variables_path = workspace_name + "\\variables\\";
		String responses_path = workspace_name + "\\webservices\\responses";
				
		out_object_testdata = tc_test_data;

		int RowObjects = Integer.parseInt(fw_get_variable("total_objects"));
		for (int y=1;y<RowObjects+1;y++)
		{
			
			//object_objectname = fw_get_test_step("out_object_name_obj", y);
			//object_tagnameattributevalue = fw_get_test_step("out_object_def_obj", y);
			//object_extrainfo = fw_get_test_step("out_object_extrainfo_obj", y);
			
			object_objectname = hmap_object_name.get(y);
			object_tagnameattributevalue = hmap_object_def.get(y);
			object_extrainfo = hmap_object_extrainfo.get(y);
			
			if (object_objectname.equals(tc_object_name))
			{
				String[] object_tagnameattributevalue_arr = object_tagnameattributevalue.split(",");
				
				object_tagname = object_tagnameattributevalue_arr[0];
				object_attribute = object_tagnameattributevalue_arr[1];
				object_attributevalue = object_tagnameattributevalue_arr[2];
				break;
			}
			
		}
		
		// Modified by Mark on 5/26/2017 to handle multiple dynamic objects in same property
		
		if (object_attributevalue.contains("---FILE_"))
		{
			String out_object_attributevalue = "";
			String object_temp = "";
			
			String[] object_attributevalue_array = object_attributevalue.split("---");
			
			int object_attributevalue_len = object_attributevalue_array.length;
			
			for (int x=0;x<object_attributevalue_len;x++)
			{
				object_temp = object_attributevalue_array[x];
				
				if (object_temp.contains("FILE_"))
				{
					String[] file_array = object_temp.split("_");
					String filenamevalue = file_array[1];	
					object_temp = fw_get_variable(filenamevalue);
				}
				out_object_attributevalue = out_object_attributevalue + object_temp;
			}
		
			object_attributevalue = out_object_attributevalue;
			
		}
		
		// End of Modified by Mark on 5/26/2017
				
		if (tc_event_name.equals("XMLExecute"))
		{
			String workspacename = fw_get_workspace_name();
			String[] tc_object_name_arr = tc_object_name.split("_");
			int arr_length = tc_object_name_arr.length;
		
			String webservice_type = "";
			String webservice_name = "";
			String column_name = "";
			
			if (arr_length == 3)
			{
				webservice_type = tc_object_name_arr[0];
				webservice_name = tc_object_name_arr[1];
				column_name = tc_object_name_arr[2];
			}
			else
			{
				webservice_type = tc_object_name_arr[0];
				webservice_name = tc_object_name_arr[1];
				column_name = webservice_name;
			}
			
			fw_set_variable("SKIP--webservice_type", webservice_type);
			
			String current_endpoint = "";
			String current_credentials = "";
			
			current_endpoint = fw_get_variable("ENV" + column_name + "ENDPOINT");
			 
			// Substitute Dynamic Endpoint
			
			for (int x=1;x<10;x++)
			{
				int left_caret_pos = current_endpoint.indexOf("{");
				
				if (left_caret_pos == -1)
				{
					break;
				}
				else
				{
					int right_caret_pos = current_endpoint.indexOf("}");
					String file_name = current_endpoint.substring(left_caret_pos + 1, right_caret_pos);
					String entity_value = fw_get_variable(file_name);
					
					String first_string = current_endpoint.substring(0, left_caret_pos);
					String second_string = entity_value;
					String third_string = current_endpoint.substring(right_caret_pos + 1, current_endpoint.length());

					current_endpoint = first_string + second_string + third_string;
				}
			}
			
			// End of Substitute Dynamic Endpoint
			
			String current_credentials_file = workspacename + "\\variables\\ENV" + column_name + "CREDS";
			current_credentials = fw_get_variable("ENV" + column_name + "CREDS");
				
			if (current_credentials.contains(":"))
			{
				String[] current_credentials_arr = current_credentials.split(":");
				String userid = current_credentials_arr[0];
				String passid = current_credentials_arr[1];
				fw_set_variable("SKIP--WSUSERID", userid);
				fw_set_variable("SKIP--WSPASSID", passid);
			}
			else
			{
				fw_set_variable("SKIP--WSUSERID", "");
				fw_set_variable("SKIP--WSPASSID", "");
			}	
			
			
			int num_loops = 1;
			long wait_per_loop = 0;
			String execute_and_validate_flag = "";
			String text_to_look_for_in_response = "";
			
			if (object_to_look_for_after_object_event.contains("--"))
			{
				String[] objecttolookforafterobjectevent_arr = object_to_look_for_after_object_event.split("--");
				text_to_look_for_in_response = objecttolookforafterobjectevent_arr[0];
				num_loops = Integer.valueOf(objecttolookforafterobjectevent_arr[1]);
				wait_per_loop = Long.valueOf(objecttolookforafterobjectevent_arr[2]);
				
				execute_and_validate_flag = "YES";
				
				log.fw_writeLogEntry("", "NA");
				log.fw_writeLogEntry("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", "NA");
				log.fw_writeLogEntry("Webservice Loop Check.......", "NA");
				log.fw_writeLogEntry("     Webservice:          " + webservice_name, "NA");
				log.fw_writeLogEntry("     # Loops:             " + num_loops, "NA");
				log.fw_writeLogEntry("     Msecs Wait per Loop: " + wait_per_loop, "NA");
				log.fw_writeLogEntry("", "NA");
				
			}
			
						
			String return_code = "";
			String text_msg = "";
			String ret_cd = "";
			String text_to_look_for_returned = "";
			String return_code_output = "";
			int difference = 0;
			
			for (int r=0;r<num_loops;r++)
			{
				
				//String env_twoway_ssl_value = fw_get_variable("ENVTWOWAYSSL");
				
				//if (webservice_name.contains("TWOWAYSSL") && env_twoway_ssl_value.equals("YES"))
				//{
				//	fw_execute_xml2WaySSL(webservice_name, current_endpoint, current_credentials, tc_test_data, 0);
				//}
				//else
				//{
					fw_execute_xml(webservice_name, current_endpoint, current_credentials, tc_test_data, 0);
				//}
				
				if (execute_and_validate_flag.equals("YES"))
				{
					text_msg = "";
					
					return_code_output = fw_validate_text_in_xml_response(webservice_name, text_to_look_for_in_response, "NO", 0);
					
					String[] return_code_arr = return_code_output.split("--");
					return_code = return_code_arr[0];
					text_to_look_for_returned = return_code_arr[1];
					
					if (return_code.equals("0"))
					{
						ret_cd = "0";
						log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + webservice_name + ", Text to Look for: " + text_to_look_for_returned + ")" + text_msg, ret_cd);
						break;
					}
					else
					{
						difference = num_loops - r;
						if (difference == 1)
						{
							ret_cd = "1";
							text_msg = "*****" + text_to_look_for_returned + " NOT found in Webservice response " + webservice_name + "***";
							log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + webservice_name + ", Text to Look for: " + text_to_look_for_returned + ")" + text_msg, ret_cd);

							log.fw_writeLogEntry("", "NA");
							log.fw_writeLogEntry("End of Webservice Loop Check.......", "NA");
							log.fw_writeLogEntry("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", "NA");
							log.fw_writeLogEntry("", "NA");
							
							// Added on 8/1/2017 - Write out xml request, response, endpoint here if failure
							
							String xml_endpoint = fw_get_variable("XMLENDPOINT");
							String xml_request = fw_get_variable("XMLREQUEST");
							String xml_response = fw_get_variable("XMLRESPONSE");
							
							log.fw_writeLogEntry("", "NA");
							log.fw_writeLogEntry("ENDPOINT: " + xml_endpoint, "NA");
							log.fw_writeLogEntry("", "NA");
							log.fw_writeLogEntry("REQUEST: " + xml_request, "NA");
							log.fw_writeLogEntry("", "NA");
							log.fw_writeLogEntry("RESPONSE: " + xml_response, "NA");
							log.fw_writeLogEntry("", "NA");	
							
							// End of Added on 8/1/2017 - Write out xml request, response, endpoint here if failure
							
							
						}
						else
						{
							ret_cd = "NA";
							text_msg = "*****" + text_to_look_for_returned + " NOT found in Webservice response " + webservice_name + "***";
							log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + webservice_name + ", Text to Look for: " + text_to_look_for_returned + ")" + text_msg, ret_cd);
							Thread.sleep(wait_per_loop);							
						}
					}
				}
			}
			
		}
		else if (tc_event_name.equals("XMLExecuteHTTP"))
		{
			String workspacename = fw_get_workspace_name();
			String[] tc_object_name_arr = tc_object_name.split("_");
			int arr_length = tc_object_name_arr.length;
		
			String webservice_type = "";
			String webservice_name = "";
			String column_name = "";
			
			if (arr_length == 3)
			{
				webservice_type = tc_object_name_arr[0];
				webservice_name = tc_object_name_arr[1];
				column_name = tc_object_name_arr[2];
			}
			else
			{
				webservice_type = tc_object_name_arr[0];
				webservice_name = tc_object_name_arr[1];
				column_name = webservice_name;
			}
			
			fw_set_variable("webservice_type", webservice_type);
			
			String current_endpoint = "";
			String current_credentials = "";
			
			current_endpoint = fw_get_variable("ENV" + column_name + "ENDPOINT");
			 
			// Substitute Dynamic Endpoint
			
			for (int x=1;x<10;x++)
			{
				int left_caret_pos = current_endpoint.indexOf("{");
				
				if (left_caret_pos == -1)
				{
					break;
				}
				else
				{
					int right_caret_pos = current_endpoint.indexOf("}");
					String file_name = current_endpoint.substring(left_caret_pos + 1, right_caret_pos);
					String entity_value = fw_get_variable(file_name);
					
					String first_string = current_endpoint.substring(0, left_caret_pos);
					String second_string = entity_value;
					String third_string = current_endpoint.substring(right_caret_pos + 1, current_endpoint.length());

					current_endpoint = first_string + second_string + third_string;
					
				}
			}
			
			// End of Substitute Dynamic Endpoint
			
			current_credentials = fw_get_variable("ENV" + column_name + "CREDS");
			
			if (current_credentials.contains(":"))
			{
				String[] current_credentials_arr = current_credentials.split(":");
				String userid = current_credentials_arr[0];
				String passid = current_credentials_arr[1];
				fw_set_variable("WSUSERID", userid);
				fw_set_variable("WSPASSID", passid);
			}
			else
			{
				fw_set_variable("WSUSERID", "");
				fw_set_variable("WSPASSID", "");
			}	
			
			
			int num_loops = 1;
			long wait_per_loop = 0;
			String execute_and_validate_flag = "";
			String text_to_look_for_in_response = "";
			
			if (object_to_look_for_after_object_event.contains("--"))
			{
				String[] objecttolookforafterobjectevent_arr = object_to_look_for_after_object_event.split("--");
				text_to_look_for_in_response = objecttolookforafterobjectevent_arr[0];
				num_loops = Integer.valueOf(objecttolookforafterobjectevent_arr[1]);
				wait_per_loop = Long.valueOf(objecttolookforafterobjectevent_arr[2]);
				
				execute_and_validate_flag = "YES";
				
				log.fw_writeLogEntry("", "NA");
				log.fw_writeLogEntry("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", "NA");
				log.fw_writeLogEntry("Webservice Loop Check.......", "NA");
				log.fw_writeLogEntry("     Webservice:          " + webservice_name, "NA");
				log.fw_writeLogEntry("     # Loops:             " + num_loops, "NA");
				log.fw_writeLogEntry("     Msecs Wait per Loop: " + wait_per_loop, "NA");
				log.fw_writeLogEntry("", "NA");
				
			}
			
						
			String return_code = "";
			String text_msg = "";
			String ret_cd = "";
			String text_to_look_for_returned = "";
			String return_code_output = "";
			int difference = 0;
			
			for (int r=0;r<num_loops;r++)
			{
				
				fw_execute_xml_http(webservice_name, current_endpoint, current_credentials, tc_test_data, 0);
				
				if (execute_and_validate_flag.equals("YES"))
				{
					text_msg = "";
					
					return_code_output = fw_validate_text_in_xml_response(webservice_name, text_to_look_for_in_response, "NO", 0);
					
					String[] return_code_arr = return_code_output.split("--");
					return_code = return_code_arr[0];
					text_to_look_for_returned = return_code_arr[1];
					
					if (return_code.equals("0"))
					{
						ret_cd = "0";
						log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + webservice_name + ", Text to Look for: " + text_to_look_for_returned + ")" + text_msg, ret_cd);
						break;
					}
					else
					{
						difference = num_loops - r;
						if (difference == 1)
						{
							ret_cd = "1";
							text_msg = "*****" + text_to_look_for_returned + " NOT found in Webservice response " + webservice_name + "***";
							log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + webservice_name + ", Text to Look for: " + text_to_look_for_returned + ")" + text_msg, ret_cd);

							log.fw_writeLogEntry("", "NA");
							log.fw_writeLogEntry("End of Webservice Loop Check.......", "NA");
							log.fw_writeLogEntry("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", "NA");
							log.fw_writeLogEntry("", "NA");
						}
						else
						{
							ret_cd = "NA";
							text_msg = "*****" + text_to_look_for_returned + " NOT found in Webservice response " + webservice_name + "***";
							log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + webservice_name + ", Text to Look for: " + text_to_look_for_returned + ")" + text_msg, ret_cd);
							Thread.sleep(wait_per_loop);							
						}
					}
				}
			}
			
		}
		else if (tc_event_name.equals("SQLExecute"))
		{
			String[] tc_object_name_arr = tc_object_name.split("_");
			String data_source_name = tc_object_name_arr[1];
			
			String[] tc_test_data_arr = tc_test_data.split("--");
			String column_values_to_get = tc_test_data_arr[0];
			String sql_to_execute = tc_test_data_arr[1];
			
			if (!object_to_look_for_after_object_event.contains("--"))
			{
				fw_execute_sql(data_source_name, sql_to_execute, column_values_to_get);
			}
			else
			{
				int num_loops = 1;
				long wait_per_loop = 0;
				String expected_file = "";
				String expected_value = "";
				String actual_file = "";
				
				String[] objecttolookforafterobjectevent_arr = object_to_look_for_after_object_event.split("--");
				expected_value = objecttolookforafterobjectevent_arr[0];
				if (expected_value.contains("FILE_"))
				{
					String[] expected_value_arr = expected_value.split("_");
					expected_file = expected_value_arr[1];
					expected_value = fw_get_variable(expected_file);
				}
				actual_file = objecttolookforafterobjectevent_arr[1];
				num_loops = Integer.valueOf(objecttolookforafterobjectevent_arr[2]);
				wait_per_loop = Long.valueOf(objecttolookforafterobjectevent_arr[3]);
				
				log.fw_writeLogEntry("", "NA");
				log.fw_writeLogEntry("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", "NA");
				log.fw_writeLogEntry("Database Loop Check.......", "NA");
				log.fw_writeLogEntry("     Object Name:         " + tc_object_name, "NA");
				log.fw_writeLogEntry("     # Loops:             " + num_loops, "NA");
				log.fw_writeLogEntry("     Msecs Wait per Loop: " + wait_per_loop, "NA");
				log.fw_writeLogEntry("", "NA");
			
				String ret_cd = "";
				String actual_value = "";
				int difference = 0;
				
				for (int r=0;r<num_loops;r++)
				{
					
					fw_execute_sql(data_source_name, sql_to_execute, column_values_to_get);
					actual_value = fw_get_variable(actual_file).trim();
										
					if (expected_value.equals(actual_value))
					{
						ret_cd = "0";
						log.fw_writeLogEntry("Validate SQL Execute (Expected: " + expected_value + ", Actual: " + actual_value + ")", ret_cd);
						break;
					}
					else
					{
						difference = num_loops - r;
						if (difference == 1)
						{
							ret_cd = "1";
							log.fw_writeLogEntry("Validate SQL Execute (Expected: " + expected_value + ", Actual: " + actual_value + ") - *** ACTUAL DOES NOT MATCH EXPECTED ***", ret_cd);

							log.fw_writeLogEntry("", "NA");
							log.fw_writeLogEntry("End of Webservice Loop Check.......", "NA");
							log.fw_writeLogEntry("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", "NA");
							log.fw_writeLogEntry("", "NA");
						}
						else
						{
							ret_cd = "NA";
							log.fw_writeLogEntry("Validate SQL Execute (Expected: " + expected_value + ", Actual: " + actual_value + ") - *** ACTUAL DOES NOT MATCH EXPECTED ***", ret_cd);
							Thread.sleep(wait_per_loop);							
						}
					}
					
				}
				
			}
			
		}
		else if (tc_event_name.equals("ConvertStringCase"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String file_name_temp = tc_test_data_arr[0];
			String case_to_convert_to = tc_test_data_arr[1];
			
			String[] file_name_arr = file_name_temp.split("_");
			String file_name = file_name_arr[1];
			
			fw_convert_string_case(file_name, case_to_convert_to);
		}
		else if (tc_event_name.equals("ROBOTEnterDataInfoField"))
		{		
			fw_robot_keypress_enterdata(tc_test_data, "0");
		}
		else if (tc_event_name.equals("ROBOTHitRightArrowKey"))
		{		
			fw_robot_keypress_rightarrow("0");
		}
		else if (tc_event_name.equals("ROBOTHitDownArrowKey"))
		{		
			fw_robot_keypress_downarrow(tc_test_data, "0");
		}
		else if (tc_event_name.equals("ROBOTShiftKey"))
		{		
			fw_robot_keypress_shiftkey(tc_test_data, "0");
		}
		else if (tc_event_name.equals("ROBOTHitTabKey"))
		{		
			fw_robot_keypress_tab(tc_test_data, "0");
		}
		else if (tc_event_name.equals("ROBOTHitDeleteKey"))
		{		
			fw_robot_keypress_delete("0");
		}
		else if (tc_event_name.equals("ROBOTHitEnterKey"))
		{		
			fw_robot_keypress_enterkey("0");
		}
		else if (tc_event_name.equals("ROBOTClickMouseButton"))
		{		
			String[] tc_test_data_arr = tc_test_data.split(",");
			String button_value = tc_test_data_arr[0];
			String num_clicks = tc_test_data_arr[1];
			
			fw_robot_mouseclick(button_value, num_clicks, "0");
		}
		else if (tc_event_name.equals("ROBOTMoveMouse"))
		{		
			String[] tc_test_data_arr = tc_test_data.split(",");
			String xvalue = tc_test_data_arr[0];
			String yvalue = tc_test_data_arr[1];
			
			fw_robot_mousemove(xvalue, yvalue, "0");
		}
		else if (tc_event_name.equals("ROBOTAltKey"))
		{		
			fw_robot_keypress_altkey(tc_test_data, "0");
		}
		else if (tc_event_name.equals("ROBOTWindowsKey"))
		{		
			fw_robot_keypress_windowskey(tc_test_data, "0");
		}
		else if (tc_event_name.equals("ROBOTCtlKey"))
		{		
			fw_robot_keypress_ctlkey(tc_test_data, "0");
		}
		else if (tc_event_name.equals("GetCurrentURL"))
		{
			fw_get_current_url();
		}
		else if (tc_event_name.equals("GetEmail"))
		{
			fw_testdata_get_email();
		}
		else if (tc_event_name.equals("UpdateEmail"))
		{
			fw_testdata_update_email();
		}
		else if (tc_event_name.equals("GetDevice"))
		{	
			String fake_real_flag = "";
			String device_type = "";
			String device_type_temp = "";
			String device_type_file = "";
			String port_type = "";
			String index_value = "";
			
			if (tc_test_data.contains("--"))
			{
				String[] tc_test_data_arr = tc_test_data.split("--");
				fake_real_flag = tc_test_data_arr[0];
				
				device_type_temp = tc_test_data_arr[1];
				if (device_type_temp.contains("FILE_"))
				{
					String[] device_type_arr = device_type_temp.split("_");
					device_type_file = device_type_arr[1];
					device_type = fw_get_variable(device_type_file);
				}
				else
				{
					device_type = device_type_temp;
				}
				
				port_type = tc_test_data_arr[2];
				index_value = tc_test_data_arr[3];
			}
			else
			{
				String[] tc_test_data_arr = tc_test_data.split("_");
				fake_real_flag = tc_test_data_arr[0];
				device_type = tc_test_data_arr[1];
				port_type = tc_test_data_arr[2];
				index_value = tc_test_data_arr[3];
			}
			fw_testdata_get_device(fake_real_flag, device_type, port_type, index_value);
		}
		else if (tc_event_name.equals("GetAccount"))
		{	
			
			String[] tc_test_data_arr = tc_test_data.split("---");
			String teamval = tc_test_data_arr[0];
			String envval = tc_test_data_arr[1];
			String sysval = tc_test_data_arr[2];
			String prinval = tc_test_data_arr[3];
			String agentval = tc_test_data_arr[4];
			String ratecenterval = tc_test_data_arr[5];
			String packageidnameval = tc_test_data_arr[6];
			String indexval = tc_test_data_arr[7];
			
			fw_testdata_get_account(teamval, envval, sysval, prinval, agentval, ratecenterval, packageidnameval, indexval);
			
		}
		else if (tc_event_name.equals("GetLocation"))
		{	
			
			String[] tc_test_data_arr = tc_test_data.split("---");
			String teamval = tc_test_data_arr[0];
			String sysval = tc_test_data_arr[1];
			String prinval = tc_test_data_arr[2];
			String agentval = tc_test_data_arr[3];
			String indexval = tc_test_data_arr[4];
			
			fw_testdata_get_location(teamval, sysval, prinval, agentval, indexval);
			
		}
		else if (tc_event_name.equals("ValidateTextNotEqual"))
		{	
			String[] tc_test_data_arr = tc_test_data.split("---");
			String string1 = tc_test_data_arr[0];
			String string2 = tc_test_data_arr[1];
			
			fw_validate_text_not_equal(tc_object_name, string1, string2);
		}
		else if (tc_event_name.equals("ReturnAccount"))
		{	
			fw_testdata_return_account(tc_test_data);
		}
		else if (tc_event_name.equals("ReturnLocation"))
		{	
			fw_testdata_return_location(tc_test_data);
		}
		else if (tc_event_name.equals("ReplaceString"))
		{	
			fw_testdata_replace_string(tc_test_data);
		}
		else if (tc_event_name.equals("GetSPALocation"))
		{	
			
			String[] tc_test_data_arr = tc_test_data.split("--");
			String env_value = tc_test_data_arr[0];
			String switch_type_value = tc_test_data_arr[1];
			String rate_center_value = tc_test_data_arr[2];
			String account_type_value = tc_test_data_arr[3];
			String index_value = tc_test_data_arr[4];
			
			String device_type = "";
			int arr_length = tc_test_data_arr.length;
			if (arr_length == 6)
			{
				device_type = tc_test_data_arr[5];
			}
			
			fw_testdata_get_spalocation(env_value, switch_type_value, rate_center_value, account_type_value, index_value, device_type);
			
		}
		else if (tc_event_name.equals("GenerateRandomCharacters"))
		{	
			
			String[] tc_test_data_arr = tc_test_data.split("_");
			String candidate_characters = tc_test_data_arr[0];
			String length_of_characters = tc_test_data_arr[1];
			
			fw_generate_random_characters(candidate_characters, length_of_characters);
		}
		else if (tc_event_name.equals("GetAddress"))
		{	
			
			String[] tc_test_data_arr = tc_test_data.split(",");
			String state_val = tc_test_data_arr[0];
			String environment_val = tc_test_data_arr[1];
			
			fw_testdata_get_address(state_val, environment_val);
		}
		else if (tc_event_name.equals("XMLValidateTextinXMLResponse"))
		{
			String[] tc_object_name_arr = tc_object_name.split("_");
			String webservice_name = tc_object_name_arr[1];
			
			fw_validate_text_in_xml_response(webservice_name, tc_test_data, "YES", 0);
		}
		else if (tc_event_name.equals("XMLValidateTextNOTinXMLResponse"))
		{
			String[] tc_object_name_arr = tc_object_name.split("_");
			String webservice_name = tc_object_name_arr[1];
			
			fw_validate_text_notin_xml_response(webservice_name, tc_test_data, "YES", 0);
		}
		else if (tc_event_name.equals("XMLGetValueByTagname"))
		{
			String[] tc_object_name_arr = tc_object_name.split("_");
			String webservice_name = tc_object_name_arr[1];
			
			fw_get_value_from_xml_based_on_tagname(webservice_name, tc_test_data, 0);
		}
		else if (tc_event_name.equals("XMLGetValueBySearchCriteria"))
		{
			String[] tc_object_name_arr = tc_object_name.split("_");
			String webservice_name = tc_object_name_arr[1];
			
			fw_get_value_from_xml_based_on_searchcriteria(webservice_name, tc_test_data, 0);
		}
		else if (tc_event_name.equals("ExcelGetCellValue"))
		{
			String[] tc_test_data_arr = tc_test_data.split("---");
			String file_name = tc_test_data_arr[0];
			String sheet_name = tc_test_data_arr[1];
			String row_num = tc_test_data_arr[2];
			String col_num = tc_test_data_arr[3];
			
			fw_get_excel_cell_value(file_name, sheet_name, row_num, col_num);
		}
		else if (tc_event_name.equals("FILERename"))
		{		
			String[] tc_test_data_arr = tc_test_data.split("---");
			String original_file = tc_test_data_arr[0];
			String new_file = tc_test_data_arr[1];
			fw_file_rename(original_file,new_file);
		}
		else if (tc_event_name.equals("FILECopy"))
		{		
			String[] tc_test_data_arr = tc_test_data.split("---");
			String from_file = tc_test_data_arr[0];
			String to_file = tc_test_data_arr[1];
			fw_file_copy(from_file,to_file);
		}
		else if (tc_event_name.equals("CalculateMathOperation"))
		{		
			String[] tc_test_data_arr = tc_test_data.split(",");
			String first_number = tc_test_data_arr[0];
			String operation = tc_test_data_arr[1];
			String second_number = tc_test_data_arr[2];
			String result_type = tc_test_data_arr[3];
			fw_testdata_calculate_math(first_number, operation, second_number, result_type);
		}
		else if (tc_event_name.equals("FILEDelete"))
		{		
			fw_file_delete(tc_test_data);
		}
		else if (tc_event_name.equals("XMLGetValueByMultipleTagnames"))
		{
			String[] tc_object_name_arr = tc_object_name.split("_");
			String webservice_name = tc_object_name_arr[1];
			
			fw_get_value_from_xml_based_on_multiple_tagnames(webservice_name, tc_test_data, 0);
		}
		else if (tc_event_name.equals("GetTN"))
		{	
			String[] tc_data_arr = tc_test_data.split("_");
			String port_type = tc_data_arr[0];
			String phone_switch_type = tc_data_arr[1];
			String account_type = tc_data_arr[2];
			String index_value = tc_data_arr[3];
			fw_testdata_get_phone_number(port_type, phone_switch_type, account_type, index_value);
		}
		else if (tc_event_name.equals("GetSequence"))
		{	
			String[] tc_data_arr = tc_test_data.split("_");
			String sequenceentity = tc_data_arr[0];
			String index_value = tc_data_arr[1];
			fw_testdata_get_sequence(sequenceentity, index_value);
		}
		else if (tc_event_name.equals("IncrementValueByOne"))
		{			
			fw_increment_value_by_one(tc_test_data);
		}
		else if (tc_event_name.equals("NavigateBack"))
		{			
			fw_browser_navigate_back();
		}
		else if (tc_event_name.equals("NavigateRefresh"))
		{			
			fw_browser_navigate_refresh();
		}
		else if (tc_event_name.equals("CloseDriver"))
		{			
			fw_close_driver();
		}
		else if (tc_event_name.equals("GetCurrentDate"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String variable_name = tc_test_data_arr[0];
			String date_format = tc_test_data_arr[1];
			
			dt.fw_generate_datetime_current(variable_name, date_format);
		}
		else if (tc_event_name.equals("GetFutureDate"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String variable_name = tc_test_data_arr[0];
			String date_format = tc_test_data_arr[1];
			int number_of_days = Integer.valueOf(tc_test_data_arr[2]);
			
			dt.fw_generate_datetime_future(variable_name, date_format, number_of_days);
		}
		else if (tc_event_name.equals("GetFutureDateBasedOnBaseDate"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String variable_name = tc_test_data_arr[0];
			String start_date = tc_test_data_arr[1];
			if (start_date.contains("FILE_"))
			{
				String[] current_string_arr = start_date.split("_");
				String varname = current_string_arr[1];
				start_date = fw_get_variable(varname);
			}
			String date_format = tc_test_data_arr[2];
			int number_of_days = Integer.valueOf(tc_test_data_arr[3]);
			
			dt.fw_get_future_date_basedonbasedate(variable_name, start_date, date_format, number_of_days);
		}
		else if (tc_event_name.equals("ChangeDateFormat"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String filename = tc_test_data_arr[0];
			String oldDateString = tc_test_data_arr[1];
			if (oldDateString.contains("FILE_"))
			{
				String[] current_string_arr = oldDateString.split("_");
				String varname = current_string_arr[1];
				oldDateString = fw_get_variable(varname);
			}
			String olddateformat = tc_test_data_arr[2];
			String newdateformat = tc_test_data_arr[3];
			
			dt.fw_change_date_format(filename, oldDateString, olddateformat, newdateformat);
		}
		else if (tc_event_name.equals("SetVariable"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String variable_name = tc_test_data_arr[0];
			
			int pos_first_comma = tc_test_data.indexOf(",");
			int temp_value = tc_test_data.length();
			String variable_value = tc_test_data.substring(pos_first_comma+1, temp_value);
			
			fw_set_variable(variable_name, variable_value);
		}
		else if (tc_event_name.equals("GetWindowHandle"))
		{
			fw_get_window_handle();
		}
		else if (tc_event_name.equals("LoginToSSO"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String user_id = tc_test_data_arr[0];
			String pass_id = tc_test_data_arr[1];
			
			fw_login_to_SSO(user_id, pass_id);
		}
		else if (tc_event_name.equals("SwitchToWindow"))
		{
			fw_switch_to_window(tc_test_data);
		}
		else if (tc_event_name.equals("SwitchToNewWindow"))
		{
			fw_switch_to_new_window();
		}
		else if (tc_event_name.equals("SwitchToNewWindowExclusionList"))
		{
			fw_switch_to_new_window_exclusionlist(tc_test_data);
		}
		else if (tc_event_name.equals("SwitchToWindowByTitle"))
		{
			fw_switch_to_window_by_title(tc_test_data);
		}
		else if (tc_event_name.equals("ValidateText"))
		{
			
			String split_delimiter = "";
			
			if (tc_test_data.contains("--"))
			{
				split_delimiter = "--";
			}
			else
			{
				split_delimiter = ",";
			}
			
			if (tc_test_data.contains("STOPEXECUTIONONFAIL"))
			{
				fw_set_variable("stopexecutionuponfailure","yes");
			}
			else
			{
				fw_set_variable("stopexecutionuponfailure","no");
			}
			
			String[] tc_test_data_arr = tc_test_data.split(split_delimiter);
			String expected_value = tc_test_data_arr[0];
			String actual_value = tc_test_data_arr[1];
			
			if (actual_value.contains("FILE_"))
			{
				String[] actual_value_arr = actual_value.split("_");
				String actual_value_file = actual_value_arr[1];
				actual_value = fw_get_variable(actual_value_file);
			}

			if (expected_value.contains("FILE_"))
			{
				String[] expected_value_arr = expected_value.split("_");
				String expected_value_file = expected_value_arr[1];
				expected_value = fw_get_variable(expected_value_file);
			}
						
			fw_validate_text(tc_object_name, expected_value, actual_value);
		}
		else if (tc_event_name.equals("LaunchBrowser"))
		{
			fw_launch_browser(tc_test_data);
		}
		else if (tc_event_name.equals("QuitDriver"))
		{
			fw_quit_driver();
		}
		else if (tc_event_name.equals("NavigateToURL"))
		{
			String output_url_value = fw_get_variable("ENV" + tc_test_data);	
			fw_navigate_to_url(output_url_value);
		}
		else if (tc_event_name.equals("TerminateWindowProcesses"))
		{
			fw_terminate_window_processes(tc_test_data);	
		}
		else if (tc_event_name.equals("EnterDataTextbox"))
		{
			fw_enter_data_into_text_field(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("MoveToElement"))
		{
			fw_move_to_element(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("ClearDataFromField"))
		{
			fw_clear_data_from_field(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("SwitchToFrame"))
		{
			fw_switch_frame(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("ClickAndEnterData"))
		{
			fw_click_and_enter_data(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("GetAttribute"))
		{
			fw_get_attribute_value(object_attribute, object_attributevalue, tc_test_data, 0);
		}
		else if (tc_event_name.equals("GetCSS"))
		{
			fw_get_css_value(object_attribute, object_attributevalue, tc_test_data, 0);
		}
		else if (tc_event_name.equals("WriteLogHeader"))
		{
			log.fw_writeLogEntry(tc_test_data, "LOGHEADER");
		}
		else if (tc_event_name.equals("CheckForElementExistence"))
		{
			fw_check_element_existence(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("CheckElementIsNotDisplayed"))
		{
			fw_element_is_not_displayed(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("CheckElementDoesNOTExist"))
		{
			fw_check_element_doesnot_exist(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("SwitchToDefaultContent"))
		{
			fw_switchto_defaultcontent();
		}
		else if (tc_event_name.equals("AcceptAlert"))
		{
			fw_accept_alert();
		}
		else if (tc_event_name.equals("SwitchToDriver"))
		{
			fw_switch_to_driver(tc_test_data);
		}
		else if (tc_event_name.equals("SelectCheckbox"))
		{
			fw_select_checkbox(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);
			
			if (!object_extrainfo.contains("NO"))
			{
				if (out_object_extrainfo.isEmpty())
				{
					out_object_extrainfo = object_extrainfo;
				}
				else
				{
					out_object_extrainfo = out_object_extrainfo + "," + object_extrainfo;
				}
			}	
		}
		else if (tc_event_name.equals("ClickJAVASCRIPT"))
		{
			fw_click_element_using_javascript(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);
			
			if (!object_extrainfo.contains("NO"))
			{
				if (out_object_extrainfo.isEmpty())
				{
					out_object_extrainfo = object_extrainfo;
				}
				else
				{
					out_object_extrainfo = out_object_extrainfo + "," + object_extrainfo;
				}
			}
			
		}
		else if (tc_event_name.equals("ClickButton"))
		{
			fw_click_button(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);
		}
		else if (tc_event_name.equals("GetSumOfNumbers"))
		{
			fw_testdata_getsum_numbers(tc_test_data);
		}
		else if (tc_event_name.equals("ValidateNumberInRange"))
		{
			fw_testdata_validate_number_in_range(tc_test_data);
		}
		else if (tc_event_name.equals("ClickSecurityCertificate"))
		{
			fw_click_security_certificate();
		}
		else if (tc_event_name.equals("SelectListValueByValue"))
		{
			fw_select_from_a_list_by_value(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("SelectListValueByIndex"))
		{
			fw_select_from_a_list_by_index(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("UNIXConnectToServer"))
		{
			String[] tc_test_data_arr = tc_test_data.split("---");
			String hostname = tc_test_data_arr[0];
			String userid = tc_test_data_arr[1];
			String passid = tc_test_data_arr[2];
			
			fw_connect_to_unix(hostname, userid, passid);
		}
		else if (tc_event_name.equals("UNIXCopyFileFromWindowsToUnix"))
		{
			String[] tc_test_data_arr = tc_test_data.split("---");
			String sourcefolder = tc_test_data_arr[0];
			String targetfolder = tc_test_data_arr[1];
			String filename = tc_test_data_arr[2];
				
			fw_copy_file_from_windows_to_unix(sourcefolder, targetfolder, filename);
		}
		else if (tc_event_name.equals("UNIXExecuteCommand"))
		{
			fw_execute_unix_command(tc_test_data);
		}
		else if (tc_event_name.equals("UNIXDisconnect"))
		{	
			fw_unix_disconnect();
		}
		else if (tc_event_name.equals("SelectListValueByVisibleText"))
		{
			fw_select_from_a_list_by_visible_text(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("GetText"))
		{
			fw_get_text(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);
		}
		else if (tc_event_name.equals("GetTextFromListbox"))
		{
			fw_get_text_from_listbox(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);
		}
		else if (tc_event_name.equals("StopExecution"))
		{
			
			log.fw_writeLogEntry(" ", "NA");
			log.fw_writeLogEntry(" ***** Stopping Execution ****** ", "NA");
			log.fw_writeLogEntry(" ", "NA");
			
			String runidvalue2 = fw_get_variable("RUNIDVALUE");
			Object runidval = runidvalue2;
			String almtestid = fw_get_variable("ALMTESTID");
			fwalm.fw_update_hpalm_test_case_execution_status(runidval, almtestid, "Blocked");
			
			System.exit(0);
		}
		
		if (!object_to_look_for_after_object_event.equals("NA") && !object_to_look_for_after_object_event.isEmpty()) 
		{
			if (tc_event_name.equals("XMLExecute") || tc_event_name.equals("SQLExecute"))
			{
				
			}
			else
			{
				String[] objecttolookforafterobjectevent_arr = object_to_look_for_after_object_event.split(",");
				int arr_length = objecttolookforafterobjectevent_arr.length;
				
				String tagname = objecttolookforafterobjectevent_arr[0];
				String text_to_look_for = objecttolookforafterobjectevent_arr[1];
				String number_loops = objecttolookforafterobjectevent_arr[2];
				String milliseconds_to_wait_per_loop = objecttolookforafterobjectevent_arr[3];
				
				String attribute_name = "NA";
				if (arr_length == 5)
				{
					attribute_name = objecttolookforafterobjectevent_arr[4];
				}
				
				fw_check_for_loading_page(tagname, text_to_look_for, number_loops, milliseconds_to_wait_per_loop, attribute_name);
			}
		}

		if (!milliseconds_to_wait_after_object_event.equals("0") && !milliseconds_to_wait_after_object_event.isEmpty())
		{
			Thread.sleep(Long.valueOf(milliseconds_to_wait_after_object_event));		
		}
		
	}
	
	/**
	 *
	 * This function executes an XML Request HTTP.
	 *  
	 * @param fileInput
	 * @param endpoint
	 * @param creds
	 * @param in_string
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @since 8/16/2017
	 */
	
	public String fw_execute_xml_http(String fileInput, String endpoint, String creds, String in_string, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException 
	{

		String text_msg = "";
		String ret_cd = "0";
		String outputString = "";
		String xml_request = "";
		String xml_response = "";
		
		if (!fileInput.isEmpty() && !endpoint.isEmpty())
		{
			String workspace_name = fw_get_workspace_name();
			String template_input_file = workspace_name + "\\webservices\\templates\\" + fileInput;
			
			// Get input file and convert to string
			
			String xmlInput = "";				
			FileReader fileReader = null;
			BufferedReader bfrReader = null;
			String strLine = null;
	
			fileReader = new FileReader(template_input_file);
			bfrReader = new BufferedReader(fileReader);
			while ((strLine = bfrReader.readLine()) != null)
			{
				
				String[] in_string_subs_arr = in_string.split("--");
				
				for (int f=0;f<in_string_subs_arr.length;f++)
				{
					String in_string_sub = in_string_subs_arr[f];
					
					String[] in_string_arr = in_string_sub.split(",");
					String in_string_variable_name = in_string_arr[0];
					String in_string_value = in_string_arr[1];
					
					if (strLine.contains(in_string_variable_name))
					{
						int pos_string = strLine.indexOf(in_string_variable_name);
						String first_str = strLine.substring(0, pos_string);
						int len_strLine = strLine.length();
						int len_in_string_variable_name = in_string_variable_name.length();
						int len_first_str = first_str.length();
						int start_pos = len_first_str + len_in_string_variable_name;
						String third_str = strLine.substring(start_pos, len_strLine);
						
						String sub_value = in_string_value;
						
						// Get dynamic value out of the file
						
						if (in_string_value.toUpperCase().contains("FILE"))
						{
							String[] in_string_value_arr = in_string_value.split("_");
							String in_variable_name = in_string_value_arr[1];
							String varInput = fw_get_variable(in_variable_name);
							sub_value = varInput;
						}
						
						if (in_string_value.toUpperCase().contains("FUNCTION_"))
						{
							String[] in_string_value_arr = in_string_value.split("_");
							String in_function_name = in_string_value_arr[1];
							
							if (in_function_name.equals("WSTIMESTAMP"))
							{
								int in_expired_time_in_seconds = Integer.valueOf(in_string_value_arr[2]);
								sub_value = fw_get_webservice_security_header_timestamp(in_expired_time_in_seconds);
							}
							else if (in_function_name.equals("WSUSERTOKEN"))
							{
								String[] creds_arr = creds.split(":");
								String username = creds_arr[0];
								String password = creds_arr[1];
								sub_value = fw_get_webservice_security_header_usertoken(username, password);
							}
							else
							{
								sub_value = "WS_FUNCTION_NOT_FOUND - FUNCTION NAME SHOULD BE WSTIMESTAMP or WSUSERTOKEN";
							}
						}
						
						// End of Get dynamic value out of the file
						
						strLine = first_str + sub_value + third_str;
					}
				
				}
				
				xmlInput = xmlInput + strLine;
				
			}
			if (null != bfrReader)
			{
				bfrReader.close();
			}
			if (fileReader != null)
			{
				fileReader.close();
			}		
			
			// Write runtime request xml into output runtime request file

			String runtime_request_output_file = workspace_name + "\\webservices\\runtime\\requests\\" + fileInput;
			
			try(PrintWriter out = new PrintWriter(runtime_request_output_file))
			{
			    out.println(xmlInput);
			    out.close();
			}
			// End of Write runtime request xml into output runtime request file
	
			xml_request = xmlInput;
			
			
			// Execute Webservice
			
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpPost postRequest = new HttpPost(endpoint);
	        StringEntity input = new StringEntity(xml_request);
	        input.setContentType("text/xml");
	        
	        if (fileInput.toUpperCase().contains("TWC"))
			{
				String encodeCreds = "Basic " + "UHJlVFdlc2JOQzo1MTkyZDZjYy0zNjA1LTMxZGMtODAwMC02ZjIzMWQwZDZiOWY=";
				postRequest.addHeader("Authorization", encodeCreds);
			}
			else if (!creds.isEmpty())
			{
				String encodeCreds = "Basic " + new String(new Base64().encode(creds.getBytes()));
				postRequest.addHeader("Authorization", encodeCreds);
			}
	        
	        postRequest.setEntity(input);
	        HttpResponse response = httpClient.execute(postRequest);
	        
	        BufferedReader br = new BufferedReader(
	           new InputStreamReader((response.getEntity().getContent())));
	
		    String output;
		    while ((output = br.readLine()) != null) 
		    {
		    	outputString = output;
		    }
		    
		    xml_response = outputString;
		    
		    // Write runtime request xml into output runtime request file
			
			String runtime_response_output_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
			
			try(PrintWriter outresponse = new PrintWriter(runtime_response_output_file))
			{
				outresponse.println(xml_response);
				outresponse.close();
			}
			
			// End of Write runtime request xml into output runtime request file
			
			ret_cd = "0";
			
		}
		else
		{

			if (fileInput.isEmpty())
			{
				text_msg = "*************** fileInput is Not Defined ****************";
				ret_cd = "1";	
			}
			if (endpoint.isEmpty())
			{
				text_msg = "*************** endpoint is Not Defined ****************";
				ret_cd = "1";
			}
			
		}
		
				
		log.fw_writeLogEntry("Execute XML HTTP (Name: " + fileInput + ", Value: " + in_string + ")" + text_msg, ret_cd);

		fw_set_variable("SKIP--XMLREQUEST", xml_request);
		fw_set_variable("SKIP--XMLRESPONSE", xml_response);
		fw_set_variable("SKIP--XMLENDPOINT", endpoint);
		
		Thread.sleep(milliseconds_to_wait);		
		
		return outputString;
	
	}
	
	/**
	 *
	 * This function executes an XML Request.
	 *  
	 * @param fileInput
	 * @param endpoint
	 * @param creds
	 * @param in_string
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @since 12/30/2016
	 */
	
	public String fw_execute_xml(String fileInput, String endpoint, String creds, String in_string, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException 
	{

		String text_msg = "";
		String ret_cd = "0";
		String outputString = "";
		String xml_request = "";
		String xml_response = "";
		
		if (!fileInput.isEmpty() && !endpoint.isEmpty())
		{
			String workspace_name = fw_get_workspace_name();
			String template_input_file = workspace_name + "\\webservices\\templates\\" + fileInput;
			
			//Code to make a webservice HTTP request
			String responseString = "";
			
			/* Start of Fix */
			
	        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
	            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
	            public void checkServerTrusted(X509Certificate[] certs, String authType) { }

	        } };

	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) { return true; }
	        };
	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	        
	        /* End of the fix*/
	        
	        
			URL url = new URL(endpoint);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection)connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			
			// Get input file and convert to string
			
			String xmlInput = "";				
			FileReader fileReader = null;
			BufferedReader bfrReader = null;
			String strLine = null;
	
			fileReader = new FileReader(template_input_file);
			bfrReader = new BufferedReader(fileReader);
			while ((strLine = bfrReader.readLine()) != null)
			{
				
				String[] in_string_subs_arr = in_string.split("--");
				
				for (int f=0;f<in_string_subs_arr.length;f++)
				{
					String in_string_sub = in_string_subs_arr[f];
					
					String[] in_string_arr = in_string_sub.split(",");
					String in_string_variable_name = in_string_arr[0];
					String in_string_value = in_string_arr[1];
					
					if (strLine.contains(in_string_variable_name))
					{
						int pos_string = strLine.indexOf(in_string_variable_name);
						String first_str = strLine.substring(0, pos_string);
						int len_strLine = strLine.length();
						int len_in_string_variable_name = in_string_variable_name.length();
						int len_first_str = first_str.length();
						int start_pos = len_first_str + len_in_string_variable_name;
						String third_str = strLine.substring(start_pos, len_strLine);
						
						String sub_value = in_string_value;
						
						// Get dynamic value out of the file
						
						if (in_string_value.toUpperCase().contains("FILE"))
						{
							String[] in_string_value_arr = in_string_value.split("_");
							String in_variable_name = in_string_value_arr[1];
							String varInput = fw_get_variable(in_variable_name);
							sub_value = varInput;
						}
												
						if (in_string_value.toUpperCase().contains("FUNCTION_"))
						{
							String[] in_string_value_arr = in_string_value.split("_");
							String in_function_name = in_string_value_arr[1];
							
							if (in_function_name.equals("WSTIMESTAMP"))
							{
								int in_expired_time_in_seconds = Integer.valueOf(in_string_value_arr[2]);
								sub_value = fw_get_webservice_security_header_timestamp(in_expired_time_in_seconds);
							}
							else if (in_function_name.equals("WSUSERTOKEN"))
							{
								String[] creds_arr = creds.split(":");
								String username = creds_arr[0];
								String password = creds_arr[1];
								sub_value = fw_get_webservice_security_header_usertoken(username, password);
							}
							else
							{
								sub_value = "WS_FUNCTION_NOT_FOUND - FUNCTION NAME SHOULD BE WSTIMESTAMP or WSUSERTOKEN";
							}
						}						
						
						// End of Get dynamic value out of the file
						
						strLine = first_str + sub_value + third_str;
					}
				
				}
				
				xmlInput = xmlInput + strLine;
				
			}
			if (null != bfrReader)
			{
				bfrReader.close();
			}
			if (fileReader != null)
			{
				fileReader.close();
			}		
			
			// Write runtime request xml into output runtime request file

			String runtime_request_output_file = workspace_name + "\\webservices\\runtime\\requests\\" + fileInput;
			
			try(PrintWriter out = new PrintWriter(runtime_request_output_file))
			{
			    out.println(xmlInput);
			    out.close();
			}
			// End of Write runtime request xml into output runtime request file
	
			xml_request = xmlInput;
			
			// Execute webservice
			
			byte[] buffer = new byte[xmlInput.length()];
			buffer = xmlInput.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();
			
			String webservice_type = fw_get_variable("webservice_type");
			
			if (fileInput.toUpperCase().contains("SONUS"))
			{
				httpConn.setRequestProperty("SOAPAction", "retrieveRequest");
			}
            
			if (webservice_type.equals("JSON"))
			{
				httpConn.setRequestProperty("Content-Type", "application/json");
				httpConn.setRequestProperty("accept", "application/json");
				httpConn.setRequestProperty("accept-encoding", "gzip, deflate");
				httpConn.setRequestProperty("accept-language", "en-US,en;q=0.8");	
			}
			else if (webservice_type.equals("SETCOOKIE"))
			{
				httpConn.setRequestProperty("Content-Type", "text/xml");
				String jsessionid = fw_get_variable("jsessionid");
				httpConn.setRequestProperty("Cookie", jsessionid);
			}
			else if (webservice_type.equals("APPXML"))
			{
				httpConn.setRequestProperty("Content-Type", "application/xml");	
			}
			else if (webservice_type.startsWith("BPS"))
			{
				String soapaction_value = webservice_type.substring(3, webservice_type.length());
				System.out.println("SOAP Action value: " + soapaction_value);
				
				httpConn.setRequestProperty("Content-Type", "text/xml");
				httpConn.setRequestProperty("SOAPAction", soapaction_value);
			}
			else if (webservice_type.startsWith("NEUSTAR"))
			{
				String soapaction_value = webservice_type.substring(7, webservice_type.length());
				System.out.println("SOAP Action value: " + soapaction_value);
				
				httpConn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
				httpConn.setRequestProperty("SOAPAction", soapaction_value);
			}
			else
			{
				httpConn.setRequestProperty("Content-Type", "text/xml");					
			}
			
			if (fileInput.toUpperCase().contains("META"))
			{
				String encodeCreds = "Basic " + "VmlydE1ldGFUTU8tQVVUTzpiMWVfMil4NQ==";
				httpConn.setRequestProperty("Authorization", encodeCreds);
			}
			else if (!creds.isEmpty())
			{
				String encodeCreds = "Basic " + new String(new Base64().encode(creds.getBytes()));
				httpConn.setRequestProperty("Authorization", encodeCreds);
			}
			
			httpConn.setRequestMethod("POST");
			//httpConn.setRequestMethod("GET");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			OutputStream out = httpConn.getOutputStream();
			//Write the content of the request to the outputstream of the HTTP Connection.
			out.write(b);
			out.close();
			//Ready with sending the request.
			
			// Added 204 condition on 4/11/2017
			// HTTP Status Code 204: The server has successfully fulfilled the request 
			// and that there is no additional content to send in the response payload body.
			
			if(httpConn.getResponseCode() == 204)
			{
				ret_cd = "0";
			}
			else
			{
				BufferedReader in = null;
				
				int junk = httpConn.getResponseCode();
				
				if(httpConn.getResponseCode() == 200 || httpConn.getResponseCode() == 307 || httpConn.getResponseCode() == 202)
				{
					in = new BufferedReader(new
							InputStreamReader(httpConn.getInputStream()));
				}
				else
				{
					in = new BufferedReader(new
							InputStreamReader(httpConn.getErrorStream()));
				}
				
				//Read the response.
				//InputStreamReader isr =
				//new InputStreamReader(httpConn.getInputStream());
				//BufferedReader in = new BufferedReader(isr);
				 
				//Write the SOAP message response to a String.
				while ((responseString = in.readLine()) != null) 
				{
					outputString = outputString + responseString;
				}
				
	
				// Write runtime request xml into output runtime request file
		
				String runtime_response_output_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
				
				try(PrintWriter outresponse = new PrintWriter(runtime_response_output_file))
				{
					outresponse.println(outputString);
					outresponse.close();
				}
				
				// End of Write runtime request xml into output runtime request file
				
				xml_response = outputString;
				
				ret_cd = "0";
			
			}
			
			// Get Response Header Set-Cookie in support of T3
			
			/*
			if (webservice_type.equals("GETCOOKIE"))
			{
				String JID=httpConn.getHeaderField("Set-Cookie");
				fw_set_variable("jsessionid",JID);
			}
			*/
			
			if (webservice_type.equals("GETCOOKIE"))
            {
                  List<String> JID=httpConn.getHeaderFields().get("Set-Cookie");
                  int jsessionSize=JID.size();
                  String JsessionId="";
                  for (int i=0; i<jsessionSize; i++)
                  {
                         String sessionIteration=JID.get(i);
                         String[] envname_arr = sessionIteration.split(";");
                         JsessionId = envname_arr[0]+";"+JsessionId;     
                  }
                  fw_set_variable("jsessionid",JsessionId);
            }
			
		}
		else
		{

			if (fileInput.isEmpty())
			{
				text_msg = "*************** fileInput is Not Defined ****************";
				ret_cd = "1";	
			}
			if (endpoint.isEmpty())
			{
				text_msg = "*************** endpoint is Not Defined ****************";
				ret_cd = "1";
			}
			
		}
		
				
		log.fw_writeLogEntry("Execute XML (Name: " + fileInput + ", Value: " + in_string + ")" + text_msg, ret_cd);

		fw_set_variable("NOSUB!!XMLREQUEST", xml_request);
		fw_set_variable("NOSUB!!XMLRESPONSE", xml_response);
		fw_set_variable("NOSUB!!XMLENDPOINT", endpoint);
		
		Thread.sleep(milliseconds_to_wait);
		
		
		return outputString;
	
	}
	
	/**
	 *
	 * This function gets a value from an XML file based on tagname.
	 *  
	 * @param fileInput
	 * @param tagname_to_look_for
	 * @param milliseconds_to_wait
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 1/27/2017
	 */
	
	public void fw_get_value_from_xml_based_on_tagname(String fileInput, String tagname_to_look_for, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String ret_cd = "0";
		
		String workspace_name = fw_get_workspace_name();
		String xml_response_input_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
		String xmlInput = fw_get_value_from_file(xml_response_input_file);
		
		// Find String
		
		String input_string = xmlInput;
		String current_character;
		String output_value = "";
		String start_index_found_flag = "";
		
		int pos_string = input_string.indexOf("<" + tagname_to_look_for + ">");
		int start_index = 0;
		int end_index = 0;
		
		if (pos_string == -1)
		{
			text_msg = "*****<" + tagname_to_look_for + "> NOT found in Webservice response " + fileInput + "***";
			ret_cd = "1";
		}
		else
		{
			for (int p=1;p<75;p++)
			{
				current_character = input_string.substring(pos_string + p, pos_string + p + 1);
				if (current_character.equals(">"))
				{
					start_index = pos_string + p + 1;
					start_index_found_flag = "yes";
				}
				
				if (current_character.equals("<") && start_index_found_flag.equals("yes"))
				{
					end_index = pos_string + p;
					output_value = input_string.substring(start_index, end_index);
					ret_cd = "0";
					break;
				}
			}
		}
		
		
		// End of Find String
		
		// Write variable value into variable file
		
		String runtime_request_output_file = workspace_name + "\\variables\\" + tagname_to_look_for;
		
		try(PrintWriter out = new PrintWriter(runtime_request_output_file))
		{
		    out.println(output_value);
		    out.close();
		}
		
		// End of Write variable value into variable file
		
		log.fw_writeLogEntry("Get Value from XML Response by Tag Name (XML Response File: " + fileInput + ", Tagname to Look For: " + tagname_to_look_for + ", Value: " + output_value + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
	
	}

	/**
	 *
	 * This function gets a value from an XML file based on search criteria.
	 *  
	 * @param fileInput
	 * @param tagname_to_look_for
	 * @param milliseconds_to_wait
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 4/27/2017
	 */
	
	public void fw_get_value_from_xml_based_on_searchcriteria(String fileInput, String search_criteria, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String ret_cd = "0";
		
		String workspace_name = fw_get_workspace_name();
		String xml_response_input_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
		String xmlInput = fw_get_value_from_file(xml_response_input_file);
		
		// Find String
		
		String input_string = xmlInput;	

		int pos_search_criteria = input_string.indexOf(search_criteria);
		int pos_end_carat = input_string.indexOf(">", pos_search_criteria + 1);
		int pos_value = input_string.indexOf("value=", pos_search_criteria + 1);
		String out_value = "";
		
		if (pos_search_criteria == -1)
		{
			text_msg = "*****" + search_criteria + " NOT found in Webservice response " + fileInput + "***";
			ret_cd = "1";
		}
		else if ((pos_end_carat > pos_value) && (pos_value != -1))
		{
			int start_index = input_string.indexOf("\"", pos_value);
			int end_index = input_string.indexOf("\"", start_index+1);
			out_value = input_string.substring(start_index + 1, end_index);
			
			ret_cd = "0";
		}
		else if (pos_value == -1)
		{
			text_msg = "*****value= NOT found for this search criteria " + search_criteria + " in Webservice response " + fileInput + "***";
			ret_cd = "1";
		}
		else
		{
			text_msg = "***** > not found in Webservice response " + fileInput + "***";
			ret_cd = "1";
		}
		
		
		// End of Find String
		
		// Write variable value into variable file
		
		String runtime_request_output_file = workspace_name + "\\variables\\" + "outputvaluebasedonsearchcriteria";
		
		try(PrintWriter out = new PrintWriter(runtime_request_output_file))
		{
		    out.println(out_value);
		    out.close();
		}
		
		// End of Write variable value into variable file
		
		log.fw_writeLogEntry("Get Value from XML Response by Search Criteria (XML Response File: " + fileInput + ", Search Criteria: " + search_criteria + ", Value: " + out_value + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
	
	}
	
	/**
	 *
	 * This function gets a value from an XML file based on multiple tagnames.
	 *  
	 * @param fileInput
	 * @param input_string
	 * @param milliseconds_to_wait
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 2/7/2017
	 */
	
	public void fw_get_value_from_xml_based_on_multiple_tagnames(String fileInput, String tagname_string, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String ret_cd = "0";
		String output_string = "";
		
		String workspace_name = fw_get_workspace_name();
		String xml_response_input_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
		String xmlInput = fw_get_value_from_file(xml_response_input_file);
		
		// Find String
		
		String[] tagname_string_arr = tagname_string.split("&&");
		String tagname_list = tagname_string_arr[0];
		String tagname_output_file = tagname_string_arr[1];
		String[] tagname_list_arr = tagname_list.split(",");
		int tagname_list_len = tagname_list_arr.length;
		
		String xml_response_string = xmlInput;
		boolean continue_flag = true;
		int initial_pos_string = 0;
		int cur_pos_string = 1;
		String current_string = "";
		String tags_exceeded = "";
		
		while (continue_flag) 
		{
			
			tags_exceeded = "NO";
			text_msg = "";
			
			for (int i=0;i<tagname_list_len;i++)
			{
				String[] tagname_arr = tagname_list_arr[i].split("--");
				String tagname_event = tagname_arr[0];
				String tagname_string_to_look_for = tagname_arr[1];
				
				// Substitute FILE_
				if (tagname_string_to_look_for.contains("FILE_"))
				{
					String[] tagname_string_to_look_for_arr = tagname_string_to_look_for.split("_");
					String text_to_validate_file = tagname_string_to_look_for_arr[1];
					
					String[] text_to_validate_file_arr = text_to_validate_file.split("##");
					String text_filename = text_to_validate_file_arr[0];
					String text_remaining = text_to_validate_file_arr[1];
					
					String variable_file = workspace_name + "\\variables\\" + text_filename;			

					FileReader fileReader2 = null;
					BufferedReader bfrReader2 = null;
					String strLine2;
					
					fileReader2 = new FileReader(variable_file);
					bfrReader2 = new BufferedReader(fileReader2);
					tagname_string_to_look_for = "";
					while ((strLine2 = bfrReader2.readLine()) != null)
					{	
						tagname_string_to_look_for = tagname_string_to_look_for + strLine2;	
					}
					if (null != bfrReader2)
					{
						bfrReader2.close();
					}
					if (fileReader2 != null)
					{
						fileReader2.close();
					}
					
					tagname_string_to_look_for = tagname_string_to_look_for + text_remaining;
					
				}
				// End of Substitute FILE_
				
				if (!tagname_event.contains("("))
				{
					cur_pos_string = xml_response_string.indexOf(tagname_string_to_look_for, cur_pos_string);
					initial_pos_string = cur_pos_string;
					if (cur_pos_string == -1)
					{
						continue_flag = false;
						ret_cd = "1";
						text_msg = tagname_string_to_look_for + " not found";
						break;
					}
				}
				else
				{
					int left_paren_pos = tagname_event.indexOf("(");
					int right_paren_pos = tagname_event.indexOf(")");
					String number_tags = tagname_event.substring(left_paren_pos + 1, right_paren_pos);
					int num_tags = Integer.valueOf(number_tags);
					int cur_tags = 0;
					int tagname_string_to_look_for_len = tagname_string_to_look_for.length();
					String count_tag_string = "";
					
					if (tagname_event.contains("(") && tagname_event.contains("SEARCHFORWARD"))
					{	
						for (int j=cur_pos_string;j<10000000;j++)
						{
							current_string = xml_response_string.substring(j, j + tagname_string_to_look_for_len);
							count_tag_string = xml_response_string.substring(j, j + 2);
							
							if (current_string.equals(tagname_string_to_look_for))
							{
								cur_pos_string = j;
								break;
							}
							
							// Added on 12/11/2017
							String tag_string_value = "";
							if (fileInput.contains("-DSB"))
							{
								tag_string_value = ">";
							}
							else
							{
								tag_string_value = "</";
							}
							// End of Added on 12/11/2017
							
							//if (count_tag_string.equals("</"))
							if (count_tag_string.equals(tag_string_value))
							{
								cur_tags = cur_tags + 1;
								if (cur_tags == num_tags)
								{
									tags_exceeded = "YES";
									ret_cd = "1";
									text_msg = "Forward Current tags greater than the number expected";
									break;
								}
							}
							
						}
					}
					else if (tagname_event.contains("(") && tagname_event.contains("SEARCHBACKWARD"))
					{
						for (int j=cur_pos_string;j>0;j--)
						{
							current_string = xml_response_string.substring(j, j + tagname_string_to_look_for_len);
							count_tag_string = xml_response_string.substring(j, j + 2);
							
							if (current_string.equals(tagname_string_to_look_for))
							{
								cur_pos_string = j;
								break;
							}
							
							// Added on 12/11/2017
							String tag_string_value = "";
							if (fileInput.contains("-DSB"))
							{
								tag_string_value = ">";
							}
							else
							{
								tag_string_value = "</";
							}
							// End of Added on 12/11/2017
							
							//if (count_tag_string.equals("</"))
							if (count_tag_string.equals(tag_string_value))
							{
								cur_tags = cur_tags + 1;
								if (cur_tags == num_tags)
								{
									tags_exceeded = "YES";
									ret_cd = "1";
									text_msg = "Backward Current tags greater than the number expected";
									break;
								}
							}
							
						}
					}	
				}
				
			}
			
			if (tags_exceeded.equals("YES"))
			{
				cur_pos_string = initial_pos_string + 5;	
			}
			else
			{
				
				// Added on 12/11/2017
				//int pos_output_begin = xml_response_string.indexOf(">",cur_pos_string + 2);
				//int pos_output_end = xml_response_string.indexOf("<",cur_pos_string + 2);
				int pos_output_begin = 0;
				int pos_output_end = 0;
				if (fileInput.contains("-DSB"))
				{
					pos_output_begin = xml_response_string.indexOf("\"", cur_pos_string + 2);
					pos_output_end = xml_response_string.indexOf("\"",pos_output_begin + 1);
				}
				else
				{
					pos_output_begin = xml_response_string.indexOf(">",cur_pos_string + 2);
					pos_output_end = xml_response_string.indexOf("<",cur_pos_string + 2);
				}
				// End of Added on 12/11/2017
				
				output_string = xml_response_string.substring(pos_output_begin + 1, pos_output_end);
				
				// Write variable value into variable file
				String runtime_request_output_file = workspace_name + "\\variables\\" + tagname_output_file;
				
				try(PrintWriter out = new PrintWriter(runtime_request_output_file))
				{
				    out.println(output_string);
				    out.close();
				}
				// End of Write variable value into variable file
				
				ret_cd = "0";
				continue_flag=false;
				
			}
			
		}
		
		log.fw_writeLogEntry("Get Value from XML Response based on Multiple Tagnames (XML Response File: " + fileInput + ", Tagname String: " + tagname_string + ", Output File: " + tagname_output_file + ", Output String: " + output_string + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
	
	}
	
	/**
	 *
	 * This function get the content of a file and put into a string value.
	 *  
	 * @param file_name
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 2/5/2017
	 */

	public String fw_get_value_from_file(String file_name) throws IOException 
	{
		
		String variable_value = "";				
		FileReader fileReader = null;
		BufferedReader bfrReader = null;
		String strLine = null;
		
		fileReader = new FileReader(file_name);
		bfrReader = new BufferedReader(fileReader);
		
		while ((strLine = bfrReader.readLine()) != null)
		{	
			variable_value = variable_value + strLine;
		}
		if (null != bfrReader)
		{
			bfrReader.close();
		}
		if (fileReader != null)
		{
			fileReader.close();
		}		
		
		return variable_value;
		
	}
	
	/**
	 *
	 * This function gets workspace name.
	 *  
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @since 2/15/2017
	 */

	public String fw_get_workspace_name() throws IOException, InterruptedException 
	{
		
		String workspace_name = System.getProperty("user.dir");
		if (!workspace_name.contains("\\")) 
		{
			workspace_name.replace("\\","\\\\");	
		}
		
		return workspace_name;
		
	}
	
	/**
	 *
	 * This function validates text in XML response.
	 *  
	 * @param fileInput
	 * @param text_to_validate
	 * @param write_out_log_entry_flag
	 * @param milliseconds_to_wait
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 1/27/2017
	 */

	public String fw_validate_text_in_xml_response(String fileInput, String text_to_validate, String write_out_log_entry_flag, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String ret_cd = "";
		
		String workspace_name = fw_get_workspace_name();
		String xml_response_input_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
		String xmlInput = fw_get_value_from_file(xml_response_input_file);
		
		// Build Text to Validate String
		
		String[] text_to_validate_arr = text_to_validate.split(",");
		int text_to_validate_arr_length = text_to_validate_arr.length;
		String text_to_validate_current;
		
		String strLine2;
		String text_to_validate_final = "";
		
		for (int p=0;p<text_to_validate_arr_length;p++)
		{
			text_to_validate_current = text_to_validate_arr[p];
			
			if (text_to_validate_current.contains("FILE_"))
			{
				String[] text_to_validate_current_arr = text_to_validate_current.split("_");
				String text_to_validate_file = text_to_validate_current_arr[1];
						
				String variable_file = workspace_name + "\\variables\\" + text_to_validate_file;
				strLine2 = fw_get_variable(text_to_validate_file);
				text_to_validate_final = text_to_validate_final + strLine2;			
			}
			else
			{
				text_to_validate_final = text_to_validate_final + text_to_validate_current;
			}
		}
		
		// End of Build Text to Validate String
		
		// Find String
		
		int pos_string = xmlInput.indexOf(text_to_validate_final);
		
		if (pos_string == -1)
		{
			text_msg = "*****" + text_to_validate_final + " NOT found in Webservice response " + fileInput + "***";
			ret_cd = "1";
		}
		else
		{
			ret_cd = "0";
		}
		
		String return_string = ret_cd + "--" + text_to_validate_final;
		
		// End of Find String

		if (write_out_log_entry_flag.toUpperCase().equals("YES"))
		{
			log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + fileInput + ", Text to Look for: " + text_to_validate_final + ")" + text_msg, ret_cd);
			
			// Added on 8/1/2017 - Write out xml request, response, endpoint here if failure
			if (ret_cd == "1")
			{
				String xml_endpoint = fw_get_variable("XMLENDPOINT");
				String xml_request = fw_get_variable("XMLREQUEST");
				String xml_response = fw_get_variable("XMLRESPONSE");
				
				log.fw_writeLogEntry("", "NA");
				log.fw_writeLogEntry("ENDPOINT: " + xml_endpoint, "NA");
				log.fw_writeLogEntry("", "NA");
				log.fw_writeLogEntry("REQUEST: " + xml_request, "NA");
				log.fw_writeLogEntry("", "NA");
				log.fw_writeLogEntry("RESPONSE: " + xml_response, "NA");
				log.fw_writeLogEntry("", "NA");	
			}
			// End of Added on 8/1/2017 - Write out xml request, response, endpoint here if failure
		}
		
		Thread.sleep(milliseconds_to_wait);
	
		return return_string;
		
	}

	/**
	 *
	 * This function validates text NOT in XML response.
	 *  
	 * @param fileInput
	 * @param text_to_validate
	 * @param write_out_log_entry_flag
	 * @param milliseconds_to_wait
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 1/27/2017
	 */

	public String fw_validate_text_notin_xml_response(String fileInput, String text_to_validate, String write_out_log_entry_flag, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String ret_cd = "";
		
		String workspace_name = fw_get_workspace_name();
		String xml_response_input_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
		String xmlInput = fw_get_value_from_file(xml_response_input_file);
		
		// Build Text to Validate String
		
		String[] text_to_validate_arr = text_to_validate.split(",");
		int text_to_validate_arr_length = text_to_validate_arr.length;
		String text_to_validate_current;
		
		String strLine2;
		String text_to_validate_final = "";
		
		for (int p=0;p<text_to_validate_arr_length;p++)
		{
			text_to_validate_current = text_to_validate_arr[p];
			
			if (text_to_validate_current.contains("FILE_"))
			{
				String[] text_to_validate_current_arr = text_to_validate_current.split("_");
				String text_to_validate_file = text_to_validate_current_arr[1];
				strLine2 = fw_get_variable(text_to_validate_file);
				text_to_validate_final = text_to_validate_final + strLine2;
			}
			else
			{
				text_to_validate_final = text_to_validate_final + text_to_validate_current;
			}
		}
		
		// End of Build Text to Validate String
		
				
		// Find String
		
		int pos_string = xmlInput.indexOf(text_to_validate_final);
		
		if (pos_string == -1)
		{
			ret_cd = "0";
		}
		else
		{
			ret_cd = "1";
			text_msg = "*****" + text_to_validate_final + " unexpectedly found in Webservice response " + fileInput + "***";
		}
		
		String return_string = ret_cd + "--" + text_to_validate_final;
		
		// End of Find String

		if (write_out_log_entry_flag.toUpperCase().equals("YES"))
		{
			log.fw_writeLogEntry("Validate Text NOT in XML Response (XML Response File: " + fileInput + ", Text to Look for: " + text_to_validate_final + ")" + text_msg, ret_cd);
		}
		
		Thread.sleep(milliseconds_to_wait);
	
		return return_string;
		
	}
	
	/**
	 *
	 * This function sets a variable.
	 *  
	 * @param variable_name
	 * @param variable_value
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 1/30/2017
	 */
	
	public void fw_set_variable(String variable_name, String variable_value) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String output_string = "";
		String file_name = "";
		String skiplog = "";
		String nosub = "";
		
		if (variable_name.contains("SKIP"))
		{
			String[] variable_name_arr = variable_name.split("--");
			variable_name = variable_name_arr[1];
			skiplog = "YES";
		}
		
		if (variable_name.contains("NOSUB!!"))
		{
			String[] variable_name_arr = variable_name.split("!!");
			variable_name = variable_name_arr[1];
			nosub = "YES";
			skiplog = "YES";
		}
		
		String workspace_name = fw_get_workspace_name();
		
		if (nosub.equals("YES"))
		{
			output_string = variable_value;
		}
		else if (variable_value.contains("FILE_"))
		{
			
			String[] variable_value_arr = variable_value.split("--");
			
			int num_strings = variable_value_arr.length;
			String current_string = "";
			
			for (int y=0;y<num_strings;y++)
			{
				current_string = variable_value_arr[y];
			
				if (current_string.contains("FILE_"))
				{
					String[] current_string_arr = current_string.split("_");
					file_name = current_string_arr[1];
					String varInput = fw_get_variable(file_name);
					output_string = output_string + varInput;
				}
				else
				{
					output_string = output_string + current_string;
				}
			}
		}
		else
		{
			output_string = variable_value;
		}		
		
		String variable_file = "";
		
		if (variable_name.equals("WORKSPACE"))
		{
			variable_file = "C:\\Temp\\" + variable_name;	
		}
		else
		{
			variable_file = workspace_name + "\\variables\\" + variable_name;	
		}
		
		try(PrintWriter out = new PrintWriter(variable_file))
		{
		    out.println(output_string);
		    out.close();
		}
		
		if (!skiplog.equals("YES"))
		{
			log.fw_writeLogEntry("Set Variable (Name: " + variable_name + ", Input Value: " + variable_value + ", Output Value: " + output_string + ")" + text_msg, "0");
		}
	}
	
	/**
	 *
	 * This function completes TPV.
	 *  
	 * @param phone_number_file
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 2/8/2017
	 */
	
	public void fw_complete_tpv(String phone_number_file) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String input_value = "";
		
		String workspace_name = fw_get_workspace_name();
		//String completetpvexecutable = "C:\\Program Files\\Microsoft Office\\Office12\\excel.exe " + workspace_name + "\\webservices\\runtime\\tpv\\CompleteTPV.xls";
		String completetpvexecutable = workspace_name + "\\webservices\\tpv\\CompleteTPV.xls";
		input_value = fw_get_variable(phone_number_file);
		
		String output_file = workspace_name + "\\webservices\\tpv\\completeTPVphone";
		
		try(PrintWriter out = new PrintWriter(output_file))
		{
		    out.println(input_value);
		    out.close();
		}
		
		try
		{    
			//Runtime.getRuntime().exec(completetpvexecutable);
			Desktop.getDesktop().open(new File(completetpvexecutable));
        }
		catch(IOException  e)
		{
			e.printStackTrace();
        }  
		
		log.fw_writeLogEntry("XML Complete TPV (Variable File: " + phone_number_file + ", Runtime File: " + output_file + ")" + text_msg, "0");
	
	}
	
	/**
	 *
	 * This function increments a value by 1.
	 *  
	 * @param variable_name
	 * @param variable_value
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 2/8/2017
	 */
	
	public void fw_increment_value_by_one(String file_name) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String input_value = "";
		
		String workspace_name = fw_get_workspace_name();
		String variable_file = workspace_name + "\\variables\\" + file_name;
		input_value = fw_get_variable(file_name);
		
		int output_value = Integer.valueOf(input_value) + 1;
		
		try(PrintWriter out = new PrintWriter(variable_file))
		{
		    out.println(output_value);
		    out.close();
		}
		
		log.fw_writeLogEntry("Increment Value by 1 (Name: " + file_name + ", New Value: " + output_value + ")" + text_msg, "0");
	
	}
	
	/**
	 * This function checks to see if element exists.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/13/2017
	 * 
	 */
	
	public Boolean fw_check_element_existence(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{

		String text_msg = "";
		String ret_cd = "";
		String field_ready_flag = "";
		boolean field_isEmpty=true;
		boolean field_isEnabled=false;
		boolean field_isDisplayed=false;
		return_existence=false;
		int num_loops = 1;
		long wait_time_per_loop = 1000;
		
		
		try
		{
			
			if (!fieldvalue.equals(""))
			{
				String[] fieldvalue_arr = fieldvalue.split(",");
				num_loops = Integer.valueOf(fieldvalue_arr[0]);
				wait_time_per_loop = Long.valueOf(fieldvalue_arr[1]);
				
				System.out.println("num_loops: " + fieldvalue_arr[0].trim());
				System.out.println("wait_time_per_loop: " + fieldvalue_arr[1].trim());				
			}
			
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				for (int m=1;m<num_loops;m++)
				{
					field_isEmpty = driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty();
					
					try
					{
						field_isEnabled = driver.findElement(fw_get_element_object(locator, locatorvalue)).isEnabled();
					}
					catch (Exception e) 
					{
						System.out.println("Field isEnabled Check, Inside Loop: " + m + ".  Continuing check....");
					}

					try
					{
						field_isDisplayed = driver.findElement(fw_get_element_object(locator, locatorvalue)).isDisplayed();
					}
					catch (Exception e) 
					{
						System.out.println("Field isDisplayed Check, Inside Loop: " + m + ".  Continuing check....");
					}
					
					if(!field_isEmpty && field_isEnabled && field_isDisplayed)
					{
						System.out.println("BREAKING OUT OF LOOP (" + m + " of " + num_loops + ")");
						field_ready_flag = "YES";
						//break;
						m = num_loops + 1;
					}
					else
					{
						Thread.sleep(wait_time_per_loop);
					}
					
				}
			}
			else if (locator.equals("NA")) 
			{
				
				for (int m=1;m<num_loops;m++)
				{
					List<WebElement> rows = driver.findElements(By.tagName(tagname));
					Iterator<WebElement> iter = rows.iterator();
					
					while (iter.hasNext()) {
						WebElement item = iter.next();
						String label = item.getText().trim();
						
						if (label.contains(locatorvalue))
						{
							field_isEmpty = false;
							field_isEnabled = item.isEnabled();
							field_isDisplayed = item.isDisplayed();
							
							if(field_isEnabled && field_isDisplayed)
							{
								field_ready_flag = "YES";
								//break;
								m = num_loops + 1;
							}
						}
					}
					
					if (field_ready_flag.equals("YES"))
					{
						//break;
						m = num_loops + 1;
					}
				}
			}
			
			if (field_ready_flag.equals("YES"))
			{
				ret_cd = "0";
				return_existence = true;
			}
			else
			{
				text_msg = "*************** Field NOT Found or Field Disabled or Field Not Displayed ****************";
				ret_cd = "1";
				return_existence = false;
			}
			
			log.fw_writeLogEntry("Check Element Existence (Name: " + fieldname + ", Value: " + fieldvalue + ", Element Exists? " + return_existence + ", isEmpty: " + field_isEmpty + ", isEnabled: " + field_isEnabled + ", isDisplayed: " + field_isDisplayed + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Check Element Existence (Name: " + fieldname + ", Value: " + fieldvalue + ", Element Exists? " + return_existence + ", isEmpty: " + field_isEmpty + ", isEnabled: " + field_isEnabled + ", isDisplayed: " + field_isDisplayed + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
		
		return return_existence;
			
	}

	/**
	 * This function checks to see if element does not exist.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 6/22/2017
	 * 
	 */
	
	public void fw_check_element_doesnot_exist(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{

		String text_msg = "";
		String ret_cd = "";
		String field_found_flag = "no";
		
		boolean field_isEmpty=false;
		boolean field_isEnabled=false;
		boolean field_isDisplayed=false;
		
		try
		{
			
			if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
			{
				
				field_isEmpty = driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty();
				
				try
				{
					field_isEnabled = driver.findElement(fw_get_element_object(locator, locatorvalue)).isEnabled();
				}
				catch (Exception e) 
				{
					System.out.println("Field isEnabled Check is FALSE");
				}

				try
				{
					field_isDisplayed = driver.findElement(fw_get_element_object(locator, locatorvalue)).isDisplayed();
				}
				catch (Exception e) 
				{
					System.out.println("Field isDisplayed Check is FALSE");
				}
				
				if(!field_isEmpty || field_isEnabled || field_isDisplayed)
				{
					field_found_flag = "yes";
				}	
				
			}
			else if (locator.equals("NA")) 
			{
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) 
				{
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(locatorvalue))
					{
						field_found_flag = "yes";
						break;
					}
				}
				
			}

			if (locator == "")
			{
				ret_cd = "1";
				text_msg = "*************** Locator value is NULL.  Perhaps Object NOT Found in Object Repository ****************";
			}
			else if (field_found_flag.equals("yes"))
			{
				ret_cd = "1";
				text_msg = "*************** Field Found Unexpectedly ****************";
			}
			else
			{
				ret_cd = "0";
			}
			
			log.fw_writeLogEntry("Check Element Does NOT Exist (Name: " + fieldname + ")" + text_msg, ret_cd);
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			text_msg = "*************** EXCEPTION ****************";
			ret_cd = "1";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("Check Element Does NOT Exist (Name: " + fieldname + ") - " + text_msg + " - " + exception_string, ret_cd);
		}
			
	}
	
	/**
	 * This function returns the attribute value for the given attribute name.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		attribute_name - the attribute name of the object which you want the value for.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param locator
	 * @param locatorvalue
	 * @param attribute_name
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/15/2017
	 * 
	 */
	
	public String fw_get_attribute_value(String locator, String locatorvalue, String attribute_name, long milliseconds_to_wait) throws InterruptedException, IOException 
	{

		String attribute_value = "";
		
		try
		{
			
			if (locator.equals("name")) 
			{
				attribute_value = driver.findElement(By.name(locatorvalue)).getAttribute(attribute_name);
			}
			else if (locator.equals("id")) 
			{
				attribute_value = driver.findElement(By.id(locatorvalue)).getAttribute(attribute_name);
			}
			else if (locator.equals("class")) 
			{
				attribute_value = driver.findElement(By.className(locatorvalue)).getAttribute(attribute_name);
			}
			else if (locator.equals("css")) 
			{
				attribute_value = driver.findElement(By.cssSelector(locatorvalue)).getAttribute(attribute_name);
			}
			else if (locator.equals("xpath")) 
			{
				attribute_value = driver.findElement(By.xpath(locatorvalue)).getAttribute(attribute_name);
			}
			
			fw_set_variable("attributevalue", attribute_value);
			
			log.fw_writeLogEntry("     Get Attribute Value (Locator: " + locator + ", Locator Value: " + locatorvalue + ", Attribute Name: " + attribute_name + ", Attribute Value: " + attribute_value + ")", "NA");
			
			Thread.sleep(milliseconds_to_wait);
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
			String text_msg = "*************** EXCEPTION ****************";
			
			String exception_string = ExceptionUtils.getStackTrace(e);			
			log.fw_writeLogEntry("     Get Attribute Value (Locator: " + locator + ", Locator Value: " + locatorvalue + ", Attribute Name: " + attribute_name + ", Attribute Value: " + attribute_value + ") - " + text_msg + " - " + exception_string, "NA");
		}
		
		return attribute_value;
		
	}
	
	/**
	 * 
	 * * This function generates random string based on characters and length provided.
	 * 
	 * @param candidateChars
	 * @param length
	 * @author Mark Elking
	 * @since 4/26/2017
	 * @return 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	
	public void fw_generate_random_characters(String candidateChars, String length) throws MalformedURLException, IOException, InterruptedException 
	{
		int temp_length = Integer.valueOf(length);
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < temp_length; i++) {
	        sb.append(candidateChars.charAt(random.nextInt(candidateChars
	                .length())));
	    }

	    fw_set_variable("generaterandomcharacter", sb.toString());
	    
	    log.fw_writeLogEntry("Generate Random Characters (Candidate of Characters: " + candidateChars + ", Length Desired: " + length + ", Generated String: " + sb.toString() + ")", "0");
	    
	}

	/**
    * This method writes out a webservice security header timestamp.
    * 
    * @param expiredTimeInSeconds - accepts any time in seconds
    * @return - Dynamically generated security time stamp with expiration based on seconds parameter along with Security tag
    * @author Danny M. Byers
    * @since 04/26/2017
    * 
    */
	
	public String fw_get_webservice_security_header_timestamp(int expiredTimeInSeconds) throws IOException, InterruptedException
	{
		
		Logging log = new Logging();
      
		log.fw_writeLogEntry("", "NA");
      
		// takes input of seconds to and coverts it to milliseconds
		int timeToAdd = (expiredTimeInSeconds * 1000);
      
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      
		// Convert timestamp to instant
		Instant instant = timestamp.toInstant();
      
		// adds two minutes to expired timestamp
		Instant instantExpired = instant.plusMillis(timeToAdd);
      
		// converts back to epoch timestamp format to append to end of TS values
		long instantEpoch = instant.toEpochMilli();
      
		String xmlInput =
         "<wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
         "    <wsu:Timestamp wsu:Id=\"TS-5162820DAF979C7B8E" + instantEpoch + "\">\n" +
         "        <wsu:Created>" + instant + "</wsu:Created>\n" +
         "        <wsu:Expires>" + instantExpired + "</wsu:Expires>\n" +
         "    </wsu:Timestamp>\n\n";

		//log.fw_writeLogEntry("     XML Security Header Timestamp: " + xmlInput, "NA");
      
		return xmlInput;
		
   }
   
   /**
    * This method writes out a webservice security header usertoken.  Must be called after GetWebServiceSecurityHeaderTimeStamp(int expiredTimeInSeconds).
    * Takes time stamp 
    * 
    * @param expiredTimeInSeconds - accepts any time in seconds
    * @param wsUsername - webservice username credential
    * @param wsPassword - webservice password credential
    * @return - Dynamically generated security time stamp and user token with expiration based on seconds parameter along with Security closing tag
    * @author Danny M. Byers
    * @since 04/26/2017
    * 
    */
   
	public String fw_get_webservice_security_header_usertoken(String wsUsername, String wsPassword) throws IOException, InterruptedException
	{
		
		Logging log = new Logging();
    
		log.fw_writeLogEntry("", "NA");
      
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      
		// Convert timestamp to instant
		Instant instant = timestamp.toInstant();
      
		// converts back to epoch timestamp format to append to end of TS values
		long instantEpoch = instant.toEpochMilli();
      
		// this is the key to the password. It takes the timestamp's epoch value, and combines it with the password and
		// encodes it in base64
		// used the full path here java.util.Base64 since you use a different Base64 package above.
		byte[] encodedBytes = java.util.Base64.getEncoder().encode((instant + "/" + wsPassword).getBytes());
      
		String xmlInput =
         "    <wsse:UsernameToken wsu:Id=\"UsernameToken-5162820DAF979C7B8E" + instantEpoch + "\">\n" +
         "        <wsse:Username>" + wsUsername + "</wsse:Username>\n" +
         "        <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">"
            + wsPassword + "</wsse:Password>\n" +
         "        <wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">"
            + new String(encodedBytes) + "</wsse:Nonce>\n" +
         "        <wsu:Created>" + instant + "</wsu:Created>\n" +
         "    </wsse:UsernameToken>\n" +
         "</wsse:Security>\n\n";

		//log.fw_writeLogEntry("     XML Security Header UserToken: " + xmlInput, "NA");
      
		return xmlInput;
		
	}
	
	/**
	 * This function copies a file.
	 * 
	 * @since: 5/22/2017
	 * @author: Mark Elking
	 * @throws IOException 
	 */
	
	public void fileCopy( File in, File out ) throws IOException
    {
        FileChannel inChannel = new FileInputStream( in ).getChannel();
        FileChannel outChannel = new FileOutputStream( out ).getChannel();
        try
        {
            // Try to change this but this is the number I tried.. for Windows, 64Mb - 32Kb)
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = inChannel.size();
            long position = 0;
            while ( position < size )
            {
               position += inChannel.transferTo( position, maxCount, outChannel );
            }
            System.out.println("File Successfully Copied..");
        }
        finally
        {
            if ( inChannel != null )
            {
               inChannel.close();
            }
            if ( outChannel != null )
            {
                outChannel.close();
            }
        }
    }

	/**
	 * This function launches an executable.
	 *  
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @since 9/27/2017
	 */
	
	public void fw_launch_executable(String application_name, String application_path) throws InterruptedException, IOException 
	{
		
		try
		{    
			
			
			//String command = "C:\\Temp\\junk.bat";

			Runtime.getRuntime().exec("cmd.exe /c start C:\\csg\\wccad\\wccad.exe -run ACSRX WIN_32");
			
			//ProcessBuilder pb = new ProcessBuilder("C:\\csg\\wccad\\wccad.exe", "-run", "ACSRX", "\\WIN_32");
			//Process p = pb.start();
		    
		    /*
			String line = "C:\\csg\\wccad\\wccad.exe -run ACSRX WIN_32";
			CommandLine commandLine = CommandLine.parse(line);
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(1);
			int exitValue = executor.execute(commandLine);
			*/
		    
			//Runtime.getRuntime().exec("C:\\Temp\\junk.bat");
			//Thread.sleep(10000);
			
			//ProcessBuilder p = new ProcessBuilder();
	        //p.command(application_path, arg1);   
	        //p.start();
		}
		catch(IOException  e)
		{
			e.printStackTrace();
		}  
		
		log.fw_writeLogEntry("Launch Executable (Name: " + application_name + ", Path: " + application_path + ")", "0");
	
	}
	
}