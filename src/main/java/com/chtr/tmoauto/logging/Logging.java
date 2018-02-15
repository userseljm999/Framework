package com.chtr.tmoauto.logging;

import com.chtr.tmoauto.webui.GUI;
import com.chtr.tmoauto.util.MSExcel;
import com.chtr.tmoauto.util.ALM;
import com.chtr.tmoauto.util.DateTime;

import com.mercury.qualitycenter.otaclient.ClassFactory;
import com.mercury.qualitycenter.otaclient.ICommand;
import com.mercury.qualitycenter.otaclient.IRecordset;
import com.mercury.qualitycenter.otaclient.ITDConnection;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class Logging 
{
 
	GUI comm = new GUI();
	
	public static ALM fwalm = new ALM();
	public static Logging log = new Logging();
	public static DateTime dt = new DateTime();
	
	public static int step_total_cnt = 0;
	public static int step_passed_cnt = 0;
	public static int step_failed_cnt = 0;
	public static int step_fatal_cnt = 0;
	public static int step_error_cnt = 0;
	public static int step_warn_cnt = 0;
	public static int step_info_cnt = 0;
	public static int step_debug_cnt = 0;
	
	public static String outputlogfile = "";
	
	public static String hpalm_url = "";
	public static String hpalm_domain = "";
	public static String hpalm_project = "";
	public static String hpalm_username = "";
	public static String hpalm_password = "";
	public static String hpalm_testsetname = "";
	public static String hpalm_testsetfolder = "";
	
	/**
	 * This function creates a Video Device output log text file.
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @since 10/14/2017 
	 */
	
	public void fw_TDM_VideoDevice_create_output_log_file() throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException, SQLException, ParseException 
	{
		
		String workspace_name = comm.fw_get_workspace_name();
		
		dt.fw_generate_datetime("yyyyMMddkkmmss");
		String date_and_time = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("yyyy-MM-dd");
		String date_value = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("kk:mm:ss a");
		String time_value = dt.generated_date_time_output;
		
		
		FileWriter fw;
		
		String outputfilename = "JSF VIDEO DEVICE AUTOMATED OUTPUT LOG FILE - " + date_and_time + ".txt";
		
		outputlogfile = workspace_name + "\\logs\\" + outputfilename;
		
		fw = new FileWriter(outputlogfile,true);
		fw.close();
		
		// New LAN Output Log File
		String lan_output_log_file = "X:\\TDM\\Video Device Java Selenium\\Output\\" + outputfilename;
		comm.fw_set_variable("SKIP--lanoutputlogfile",lan_output_log_file);
		FileWriter fw2;
		fw2 = new FileWriter(lan_output_log_file,true);
		fw2.close();
		// End of New LAN Output Log File
		
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("JSF VIDEO DEVICE AUTOMATED OUTPUT LOG FILE","NA");
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("Start Date: " + date_value,"NA");
		log.fw_writeLogEntry("Start Time: " + time_value,"NA");
		log.fw_writeLogEntry("","NA");		
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
	}
	
	/**
	 * This function creates a TN output log text file in the logs directory.
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @since 8/23/2017 
	 */
	
	public void fw_TDM_TNCleanup_create_output_log_file() throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException, SQLException, ParseException 
	{
		
		String workspace_name = comm.fw_get_workspace_name();
		
		dt.fw_generate_datetime("yyyyMMddkkmmss");
		String date_and_time = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("yyyy-MM-dd");
		String date_value = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("kk:mm:ss a");
		String time_value = dt.generated_date_time_output;
		
		
		FileWriter fw;
		
		String outputfilename = "JSF TN CLEANUP AUTOMATED OUTPUT LOG FILE - " + date_and_time + ".txt";
		
		outputlogfile = workspace_name + "\\logs\\" + outputfilename;
		
		fw = new FileWriter(outputlogfile,true);
		fw.close();
		
		// New LAN Output Log File
		String lan_output_log_file = "X:\\TDM\\TN Cleanup Java Selenium\\TNOutput\\" + outputfilename;
		comm.fw_set_variable("SKIP--lanoutputlogfile",lan_output_log_file);
		FileWriter fw2;
		fw2 = new FileWriter(lan_output_log_file,true);
		fw2.close();
		// End of New LAN Output Log File
		
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("JSF TN CLEANUP AUTOMATED OUTPUT LOG FILE","NA");
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("Start Date: " + date_value,"NA");
		log.fw_writeLogEntry("Start Time: " + time_value,"NA");
		log.fw_writeLogEntry("","NA");		
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
	}

	/**
	 * This function ends writing to log file output.
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @since 9/20/2017 
	 */
	
	public void fw_TDM_TNCleanup_end_output_log_file() throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException, SQLException, ParseException 
	{
		
		dt.fw_generate_datetime("yyyy-MM-dd");
		String date_value = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("kk:mm:ss a");
		String time_value = dt.generated_date_time_output;
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("End Date: " + date_value,"NA");
		log.fw_writeLogEntry("End Time: " + time_value,"NA");
		log.fw_writeLogEntry("","NA");		
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
	}
	
	/**
	 * This function creates a Location Creation output log text file in the logs directory.
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @since 8/23/2017 
	 */
	
	public void fw_TDM_LocationCreation_create_output_log_file() throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException, SQLException, ParseException 
	{
		
		String workspace_name = comm.fw_get_workspace_name();
		
		dt.fw_generate_datetime("yyyyMMddkkmmss");
		String date_and_time = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("yyyy-MM-dd");
		String date_value = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("kk:mm:ss a");
		String time_value = dt.generated_date_time_output;
		
		
		FileWriter fw;
		
		String outputfilename = "JSF LOCATION CREATION AUTOMATED OUTPUT LOG FILE - " + date_and_time + ".txt";
		
		outputlogfile = workspace_name + "\\logs\\" + outputfilename;
		
		fw = new FileWriter(outputlogfile,true);
		fw.close();
		
		// New LAN Output Log File
		String lan_output_log_file = "X:\\TDM\\Create Locations\\LocationOutput\\" + outputfilename;
		comm.fw_set_variable("SKIP--lanoutputlogfile",lan_output_log_file);
		FileWriter fw2;
		fw2 = new FileWriter(lan_output_log_file,true);
		fw2.close();
		// End of New LAN Output Log File
		
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("JSF LOCATION CREATION AUTOMATED OUTPUT LOG FILE","NA");
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("Start Date: " + date_value,"NA");
		log.fw_writeLogEntry("Start Time: " + time_value,"NA");
		log.fw_writeLogEntry("","NA");		
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
	}
	
	/**
	 * This function creates an Account Creation output log text file in the logs directory.
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @since 9/6/2017 
	 */
	
	public void fw_TDM_AccountCreation_create_output_log_file() throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException, SQLException, ParseException 
	{
		
		String workspace_name = comm.fw_get_workspace_name();
		
		dt.fw_generate_datetime("yyyyMMddkkmmss");
		String date_and_time = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("yyyy-MM-dd");
		String date_value = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("kk:mm:ss a");
		String time_value = dt.generated_date_time_output;
		
		
		FileWriter fw;
		
		String outputfilename = "JSF ACCOUNT CREATION AUTOMATED OUTPUT LOG FILE - " + date_and_time + ".txt";
		
		outputlogfile = workspace_name + "\\logs\\" + outputfilename;
		
		fw = new FileWriter(outputlogfile,true);
		fw.close();
		
		// New LAN Output Log File
		String lan_output_log_file = "X:\\TDM\\Create Accounts\\AccountOutput\\" + outputfilename;
		comm.fw_set_variable("SKIP--lanoutputlogfile",lan_output_log_file);
		FileWriter fw2;
		fw2 = new FileWriter(lan_output_log_file,true);
		fw2.close();
		// End of New LAN Output Log File
		
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("JSF ACCOUNT CREATION AUTOMATED OUTPUT LOG FILE","NA");
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("Start Date: " + date_value,"NA");
		log.fw_writeLogEntry("Start Time: " + time_value,"NA");
		log.fw_writeLogEntry("","NA");		
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
	}
	
	/**
	 * This function creates an output log text file in the logs directory.
	 * The input required for this function is the ALM test ID.
	 * The file gets the test steps from ALM for the corresponding test case
	 * and puts those test case design steps into the log file.
	 * 
	 * @param alm_test_id
	 * @param test_description
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @since 11/01/2016 
	 */
	
	public void fw_create_output_log_file(String alm_test_id, String input_filename) throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException, SQLException {
		
		step_total_cnt = 0;
		step_passed_cnt = 0;
		step_failed_cnt = 0;
		
		String workspace_name = comm.fw_get_workspace_name();
		
		dt.fw_generate_datetime("yyyyMMddkkmmss");
		String date_and_time = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("yyyy-MM-dd");
		String date_value = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("kk:mm:ss a");
		String time_value = dt.generated_date_time_output;
		
		comm.fw_get_computername();
		
		comm.fw_set_variable("SKIP--failedstepnumber", "NA");
		comm.fw_set_variable("SKIP--failedstepdesc", "NA");
		
		FileWriter fw;
		
		String outputfilename = "SELENIUM OUTPUT LOG FILE - TESTID" + alm_test_id + " - " + date_and_time + ".txt";
		
		outputlogfile = workspace_name + "\\logs\\" + outputfilename;
		
		fw = new FileWriter(outputlogfile,true);
		fw.close();
		
		// New LAN Output Log File
		String lan_output_log_file = "X:\\Test Utility\\Log Files\\" + outputfilename;
		comm.fw_set_variable("SKIP--lanoutputlogfile",lan_output_log_file);
		FileWriter fw2;
		fw2 = new FileWriter(lan_output_log_file,true);
		fw2.close();
		// End of New LAN Output Log File
		
		String appname = comm.fw_get_variable("appname");
		String fname = comm.fw_get_variable("fname");
		String testtype = comm.fw_get_variable("TESTTYPE");
		String almuseridlogin = comm.fw_get_variable("ALMUSERIDLOGIN");
		String almtestsetcategory = comm.fw_get_variable("ALMTESTSETCATEGORY");
		String environmentselected = comm.fw_get_variable("ENVSELECTED");
		String total_tests_to_execute = comm.fw_get_variable("totaltestcasecount");
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("SELENIUM AUTOMATED OUTPUT FILE","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("Start Date: " + date_value,"NA");
		log.fw_writeLogEntry("Start Time: " + time_value,"NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("BEGIN TEST CASE SUMMARY","NA");
		log.fw_writeLogEntry("    App Name     : " + appname,"NA");
		log.fw_writeLogEntry("    Test ID      : TC" + alm_test_id,"NA");
		log.fw_writeLogEntry("    User         : " + fname,"NA");
		log.fw_writeLogEntry("    ALM Category : " + almtestsetcategory,"NA");
		log.fw_writeLogEntry("    ALM User ID  : " + almuseridlogin,"NA");
		log.fw_writeLogEntry("    Test Type    : " + testtype,"NA");
		log.fw_writeLogEntry("    Environment  : " + environmentselected,"NA");
		log.fw_writeLogEntry("    Total Tests  : " + total_tests_to_execute,"NA");
		log.fw_writeLogEntry("","NA");
		
		
		//********************** Global *************************
		
		log.fw_set_global_parms();
		
		//********************** Workspace *************************
		
		comm.fw_set_variable("WORKSPACENAME", workspace_name);
		
		String workspace_name_double_slashes = workspace_name.replace("\\", "\\\\");
		comm.fw_set_variable("WORKSPACENAMEDOUBLESLASHES", workspace_name_double_slashes);
		
		String workspace_name_user = "";
		if (workspace_name.toUpperCase().contains("JENKINS"))
		{
			workspace_name_user = "svc_automation";
		}
		else
		{
			String[] workspace_name_arr = workspace_name.split("\\\\");
			workspace_name_user = workspace_name_arr[2];
		}
		comm.fw_set_variable("WORKSPACENAMEUSER", workspace_name_user);
		
		//********************** ALM *************************
		
		log.fw_get_alm_config_parms(input_filename);
		comm.fw_set_variable("OUTPUTLOGFILE", outputfilename);
		
		//********************** ENVIRONMENT *************************
		
		//String env_value = comm.fw_get_value_from_file(workspace_name + "\\variables\\ENVSELECTED");
		String env_value = comm.fw_get_variable("ENVSELECTED");
		comm.fw_create_environment_variables(env_value);
		
		//********************** EmailTO *************************
		
		log.fw_set_emailto_parms();
				
		//********************** UPDATE ALM STATUS TO NOT COMPLETED *************************
		
		Object runidvalue = fwalm.fw_update_hpalm_test_case_execution_status(null, alm_test_id, "Not Completed");
		String string_run_id_value = String.valueOf(runidvalue);
		comm.fw_set_variable("RUNIDVALUE", string_run_id_value);
		comm.fw_set_variable("ALMTESTID", alm_test_id);
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");			
		log.fw_writeLogEntry("","NA");
		
		ITDConnection itdc = ClassFactory.createTDConnection();
		itdc.initConnectionEx(hpalm_url);
		itdc.connectProjectEx(hpalm_domain, hpalm_project, hpalm_username, hpalm_password);		
		ICommand cmd = (itdc.command()).queryInterface(ICommand.class);  	
		
		// Get Test Case Details and Write to Log File
		
		log.fw_writeLogEntry("ALM DESIGN STEPS","NA");
		log.fw_writeLogEntry("","NA");

		cmd.commandText("select ds_step_name, ds_description, ds_expected from dessteps where ds_test_id = " + alm_test_id + " order by ds_step_order");
		IRecordset recordes =(cmd.execute()).queryInterface(IRecordset.class);
				
		String cur_ds_step_name = "";
		String cur_ds_description = "";
		String cur_ds_expected = "";
		
		recordes.first();
		for (int r=0 ;r<recordes.recordCount();r++)
		{
			
			cur_ds_step_name = recordes.fieldValue(0).toString();
			cur_ds_description = recordes.fieldValue(1).toString();
			cur_ds_expected = recordes.fieldValue(2).toString();
			
			cur_ds_description = cur_ds_description.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
			cur_ds_expected = cur_ds_expected.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
			
			cur_ds_description = cur_ds_description.replaceAll("&quot;", "");
			cur_ds_expected = cur_ds_expected.replaceAll("&quot;", "");
			
			cur_ds_description = cur_ds_description.replaceAll("&nbsp;", "");
			cur_ds_expected = cur_ds_expected.replaceAll("&nbsp;", "");
			
			cur_ds_description = cur_ds_description.replaceAll("&amp;", "and");
			cur_ds_expected = cur_ds_expected.replaceAll("&amp;", "and");
			
			log.fw_writeLogEntry("Step Name:        " + cur_ds_step_name,"NA");
			log.fw_writeLogEntry("Description:     " + cur_ds_description,"NA");
			log.fw_writeLogEntry("Expected Result: " + cur_ds_expected,"NA");
			log.fw_writeLogEntry("","NA");
			
			recordes.next();

		}
		
		itdc.disconnectProject();
		
		
		
		// End of Get Test Case Details and Write to Log File
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");				
		log.fw_writeLogEntry("AUTOMATION EXECUTION STEPS","NA");
		log.fw_writeLogEntry("","NA");
		
		//log.fw_writeLogEntry("Input filename: " + input_filename,"NA");
		//log.fw_writeLogEntry(System.lineSeparator(), "NA");
		
		comm.fw_set_variable("EXITONFAIL", "");
		
	}		
	
	/**
	 * This function gets the ALM connection information within the 
	 * corresponding ALM spreadsheet in the project corresponding to
	 * the workspace which this test is being run from (i.e. Gateway).
	 * The ALM spreadsheet resides in the Gateway\config directory.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @since 11/1/2016
	 * 
	 */
	
	public void fw_get_alm_config_parms(String in_filename) throws MalformedURLException, IOException, InterruptedException, SQLException, ClassNotFoundException 
	{
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("ALM PARMS","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
		//String workspace_name = comm.fw_get_workspace_name();
		//String variables_path = workspace_name + "\\variables\\";
		//String appname = comm.fw_get_value_from_file(variables_path + "appname");
		String appname = comm.fw_get_variable("appname");
		String almtestsetcategory = comm.fw_get_variable("ALMTESTSETCATEGORY");
		
		String userName = comm.fw_get_variable("usernameTESTDB");
		String password = comm.fw_get_variable("passwordTESTDB");
		String connection_string = comm.fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
		
		String sql_query = "select hpalm_url, hpalm_domain, hpalm_project, hpalm_username, hpalm_password, hpalm_testsetname, hpalm_testsetfolder from tbl_hpalm where hpalm_application = '" + appname + "' and hpalm_testsetcategory = '" + almtestsetcategory + "'";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		while (rs.next())
		{
			hpalm_url = rs.getString("hpalm_url");
			hpalm_domain = rs.getString("hpalm_domain");
			hpalm_project = rs.getString("hpalm_project");
			hpalm_username = rs.getString("hpalm_username");
			hpalm_password = rs.getString("hpalm_password");
			hpalm_testsetname = rs.getString("hpalm_testsetname");
			hpalm_testsetfolder = rs.getString("hpalm_testsetfolder");
			
			comm.fw_set_variable("HPALMURL", hpalm_url);
			comm.fw_set_variable("HPALMDomain", hpalm_domain);
			comm.fw_set_variable("HPALMProject", hpalm_project);
			comm.fw_set_variable("HPALMUsername", hpalm_username);
			comm.fw_set_variable("HPALMPassword", hpalm_password);
			comm.fw_set_variable("HPALMTestSet", hpalm_testsetname);
			comm.fw_set_variable("HPALMTestSetFolder", hpalm_testsetfolder);
			
		}
		
		connection.close();
								
	}

	/**
	 * This function sets the Global parms from the Global worksheet in the Map spreadsheet.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @since 2/21/2017
	 * 
	 */
	
	public void fw_set_global_parms() throws IOException, InterruptedException, SQLException, ClassNotFoundException 
	{
		
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("GLOBAL PARMS","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
		//String workspace_name = comm.fw_get_workspace_name();
		//String variables_path = workspace_name + "\\variables\\";
		//String appname = comm.fw_get_value_from_file(variables_path + "appname");
		String appname = comm.fw_get_variable("appname");
		
		String sql_query = "";
		
		String userName = comm.fw_get_variable("usernameTESTDB");
		String password = comm.fw_get_variable("passwordTESTDB");
		String connection_string = comm.fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
				
		sql_query = "select Global_attribute, Global_value from tbl_Global where global_application = '" + appname + "'";
		
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String global_attribute_val = "";
		String global_value_val = "";
		
		while (rs.next())
		{
			global_attribute_val = rs.getString("Global_attribute");
			global_value_val = rs.getString("Global_value");
			
			comm.fw_set_variable(global_attribute_val, global_value_val);
			
		}
		
		connection.close();
		
		log.fw_writeLogEntry("Set Global Parms (App Name: " + appname + ")", "0");
								
	}

	/**
	 * This function sets the EmailTO parms from the EmailTO worksheet.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @since 8/11/2017
	 * 
	 */
	
	public void fw_set_emailto_parms() throws IOException, InterruptedException, SQLException, ClassNotFoundException 
	{
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("EMAILTO PARMS","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
		//String workspace_name = comm.fw_get_workspace_name();
		//String variables_path = workspace_name + "\\variables\\";
		//String appname = comm.fw_get_value_from_file(variables_path + "appname");
		String appname = comm.fw_get_variable("appname");
		
		String sql_query = "";
		
		String userName = comm.fw_get_variable("usernameTESTDB");
		String password = comm.fw_get_variable("passwordTESTDB");
		String connection_string = comm.fw_get_variable("connectionstringTESTDB");
		
		Connection connection = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connection_string, userName, password);
				
		sql_query = "select emailto_address_smoke_pass, emailto_address_smoke_fail, emailto_address_reg_pass, emailto_address_reg_fail from tbl_emailto where emailto_application = '" + appname + "'";
				
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sql_query);
		
		String emailto_address_smoke_pass = "";
		String emailto_address_smoke_fail = "";
		String emailto_address_reg_pass = "";
		String emailto_address_reg_fail = "";
		
		while (rs.next())
		{
			emailto_address_smoke_pass = rs.getString("emailto_address_smoke_pass");
			emailto_address_smoke_fail = rs.getString("emailto_address_smoke_fail");
			emailto_address_reg_pass = rs.getString("emailto_address_reg_pass");
			emailto_address_reg_fail = rs.getString("emailto_address_reg_fail");
		}
		
		if (emailto_address_smoke_pass == null)
		{
			emailto_address_smoke_pass = "";
		}
		if (emailto_address_smoke_fail == null)
		{
			emailto_address_smoke_fail = "";
		}
		if (emailto_address_reg_pass == null)
		{
			emailto_address_reg_pass = "";
		}
		if (emailto_address_reg_fail == null)
		{
			emailto_address_reg_fail = "";
		}
		
		comm.fw_set_variable("emailtoaddresssmokepass", emailto_address_smoke_pass);
		comm.fw_set_variable("emailtoaddresssmokefail", emailto_address_smoke_fail);
		comm.fw_set_variable("emailtoaddressregpass", emailto_address_reg_pass);
		comm.fw_set_variable("emailtoaddressregfail", emailto_address_reg_fail);
		
		connection.close();
		
		log.fw_writeLogEntry("Set EmailTO Parms (App Name: " + appname + ")", "0");
								
	}
	
	/**
	 * This function writes a log entry into an output file and displays output to 
	 * output console.
	 * 
	 * @param log_message
	 * @param rc_value (valid values include the following)
	 * 			0 - PASSED
	 * 			1 - FAILED
	 * 			NA - don't apply step status for the log entry, just write out the log message
	 * 			
	 * @author Mark Elking
	 * @throws InterruptedException, IOException 
	 * @throws  
	 * @throws Exception 
	 * @since 9/3/2016
	 * 
	 */
	
	public void fw_writeLogEntry(String log_message, String rc_value) throws InterruptedException, IOException 
	{
		
		try 
		{
			
			String step_status = "";
			String file_log_entry;
			String systemout_log_entry;
			
			dt.fw_generate_datetime("kk:mm:ss a");
			
			FileWriter fw;
			String logging_file = outputlogfile;
			fw = new FileWriter(logging_file,true);
			
			String lanoutputfile = comm.fw_get_variable("lanoutputlogfile");
			FileWriter fw2;
			fw2 = new FileWriter(lanoutputfile,true);
			
			if (rc_value == "LOGHEADER")
			{	
				
				//String appname = comm.fw_get_variable("appname");
				String firstname = comm.fw_get_variable("fname");
				//String testtype = comm.fw_get_variable("TESTTYPE");
				String almtestsetcategory = comm.fw_get_variable("ALMTESTSETCATEGORY");
				String almtestid = comm.fw_get_variable("ALMTESTID");
				
				log_message = log_message + "   -   (TC" + almtestid + ", " + almtestsetcategory + ", " + firstname + ")";
				
				int length_log_message = log_message.length();
				
				int length_border = length_log_message + 6;
				String border_string = "";
				String border_string2 = "";
				for (int s=1;s<length_border+1;s++)
				{
					border_string = border_string + "*";
					border_string2 = border_string2 + "-";
				}
				fw.write(System.lineSeparator());
				fw.write(border_string + System.lineSeparator());
				fw.write(border_string2 + System.lineSeparator());
				fw.write("   " + log_message + System.lineSeparator());
				fw.write(border_string2 + System.lineSeparator());
				fw.write(border_string + System.lineSeparator());
				fw.write(System.lineSeparator());
				
				System.out.println("");
				System.out.println("");
				System.out.println(border_string);
				System.out.println(border_string2);
				System.out.println("   " + log_message);
				System.out.println(border_string2);
				System.out.println(border_string);
				System.out.println("");
								
				fw2.write(System.lineSeparator());
				fw2.write(border_string + System.lineSeparator());
				fw2.write(border_string2 + System.lineSeparator());
				fw2.write("   " + log_message + System.lineSeparator());
				fw2.write(border_string2 + System.lineSeparator());
				fw2.write(border_string + System.lineSeparator());
				fw2.write(System.lineSeparator());
				
			}
			else if (rc_value == "NA")
			{
				
				file_log_entry = log_message + System.lineSeparator();
				systemout_log_entry = log_message;
				
				fw.write(file_log_entry);
				System.out.println(systemout_log_entry);
				fw2.write(file_log_entry);
			}
			else
			{
								
				step_total_cnt = step_total_cnt + 1;
				
				if (rc_value == "0")
				{
					step_passed_cnt = step_passed_cnt + 1;
					step_status = "PASSED";
				}
				if (rc_value == "1")
				{
					step_failed_cnt = step_failed_cnt + 1;
					step_status = "FAILED";
					
					comm.fw_set_variable("SKIP--failedstepnumber", String.valueOf(step_total_cnt));
					
					// New Code Added by Mark on 10/10/2017
					//comm.fw_set_variable("SKIP--failedstepdesc", log_message);
					String failure_value = comm.fw_get_variable("failedstepdesc");
					if (failure_value.equals("NA"))
					{
						comm.fw_set_variable("SKIP--failedstepdesc", log_message);
					}
					else
					{
						comm.fw_set_variable("SKIP--failedstepdesc", failure_value + "----" + log_message);
					}
					// End of New Code Added by Mark on 10/10/2017
					
					
					
					//fw_capture_screenshot();
					comm.fw_take_screenshot();
				}
				
				file_log_entry = "Step " + step_total_cnt + " - " + step_status + " - " + log_message + " - " + dt.generated_date_time_output + System.lineSeparator();
				systemout_log_entry = "Step " + step_total_cnt + " - " + step_status + " - " + log_message + " - " + dt.generated_date_time_output;
			
				fw.write(file_log_entry);
				System.out.println(systemout_log_entry);
				fw2.write(file_log_entry);
			}
			
			fw.close();
			fw2.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This function determines the overall test case execution status
	 * and writes out that result in the output log file.
	 * 
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @since 11/1/2016
	 * 
	 */
	
	public void fw_closedown_test() throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException, SQLException 
	{
		
		// Quit Driver
		
		//String workspace_name = comm.fw_get_workspace_name();
		//String variables_path = workspace_name + "\\variables\\";
		//String debug_value = comm.fw_get_value_from_file(variables_path + "Debug");
		String debug_value = comm.fw_get_variable("Debug");
		
		//if (step_failed_cnt == 0)
		//{	
		//	comm.fw_quit_driver();
		//}
		//else
		//{
			//if (debug_value.toUpperCase().equals("OFF"))
			//{
			//	comm.fw_quit_driver();
			//}
			//else
			//{
			
			if (debug_value.toUpperCase().equals("ON"))
			{
				log.fw_writeLogEntry(" ", "NA");
				log.fw_writeLogEntry(" ***** Stopping Execution because DEBUG is set to ON and there was a FAILURE to debug ****** ", "NA");
				log.fw_writeLogEntry(" ", "NA");
				
				//String runidvalue2 = comm.fw_get_value_from_file(variables_path + "RUNIDVALUE");
				String runidvalue2 = comm.fw_get_variable("RUNIDVALUE");
				
				Object runidval = runidvalue2;
				//String almtestid = comm.fw_get_value_from_file(variables_path + "ALMTESTID");
				String almtestid = comm.fw_get_variable("ALMTESTID");
				
				fwalm.fw_update_hpalm_test_case_execution_status(runidval, almtestid, "Blocked");
				
				System.exit(0);
			}	
			//}
		//}
		
		// Close Log File
		
		String overall_test_case_status = "";
		String hpalm_status = "";
		
		dt.fw_generate_datetime("yyyyMMddkkmmss");
		String date_and_time = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("yyyy-MM-dd");
		String date_value = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("kk:mm:ss a");
		String time_value = dt.generated_date_time_output;
		
		if (step_failed_cnt == 0)
		{
			overall_test_case_status = "PASSED";	
			hpalm_status = "Passed";
		}
		else
		{
			overall_test_case_status = "FAILED";
			hpalm_status = "Failed";
		}
		
		comm.fw_set_variable("SKIP--OVERALLTESTCASESTATUS", overall_test_case_status);
		
		String appname = comm.fw_get_variable("appname");
		String firstname = comm.fw_get_variable("fname");
		String testtype = comm.fw_get_variable("TESTTYPE");
		String almtestsetcategory = comm.fw_get_variable("ALMTESTSETCATEGORY");
		String almtstid = comm.fw_get_variable("ALMTESTID");
		String almuseridlogin = comm.fw_get_variable("ALMUSERIDLOGIN");
		String environmentselected = comm.fw_get_variable("ENVSELECTED");
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("END TEST CASE SUMMARY","NA");
		log.fw_writeLogEntry("    App Name     : " + appname,"NA");
		log.fw_writeLogEntry("    Test ID      : TC" + almtstid,"NA");
		log.fw_writeLogEntry("    User         : " + firstname,"NA");
		log.fw_writeLogEntry("    ALM Category : " + almtestsetcategory,"NA");
		log.fw_writeLogEntry("    ALM User ID  : " + almuseridlogin,"NA");
		log.fw_writeLogEntry("    Test Type    : " + testtype,"NA");
		log.fw_writeLogEntry("    Environment  : " + environmentselected,"NA");
		log.fw_writeLogEntry("    Test Status  : " + overall_test_case_status,"NA");
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("    Total Steps  : " + step_total_cnt,"NA");
		log.fw_writeLogEntry("    Total Passed : " + step_passed_cnt,"NA");
		log.fw_writeLogEntry("    Total Failed : " + step_failed_cnt,"NA");
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("OVERALL TEST CASE STATUS: " + overall_test_case_status,"NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("End Date: " + date_value,"NA");
		log.fw_writeLogEntry("End Time: " + time_value,"NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
		// Added by Mark on 8/3/2017
		String totaltestcasecount = comm.fw_get_variable("totaltestcasecount");
		String totaltestcasecountpassed = comm.fw_get_variable("totaltestcasecountpassed");
		String totaltestcasecountfailed = comm.fw_get_variable("totaltestcasecountfailed");
		
		String fileName = comm.fw_get_variable("OUTPUTLOGFILE");
		
		if (overall_test_case_status.equals("PASSED"))
		{
			int temppassed = Integer.valueOf(totaltestcasecountpassed) + 1;
			totaltestcasecountpassed = String.valueOf(temppassed);
			comm.fw_set_variable("SKIP--totaltestcasecountpassed", totaltestcasecountpassed);
			
			// Added by Mark on 8/9/2017
			String totaltestcasenamespassed = comm.fw_get_variable("totaltestcasenamespassed");
	   		comm.fw_set_variable("SKIP--totaltestcasenamespassed", totaltestcasenamespassed + "TC" + almtstid + ",");
	   		
	   		String totaltestcasefilespassed = comm.fw_get_variable("totaltestcasefilespassed");
	   		comm.fw_set_variable("SKIP--totaltestcasefilespassed", totaltestcasefilespassed + "X:\\Test Utility\\Log Files\\" + fileName + "<br>");
	   		// End of Added by Mark on 8/9/2017
		}
		else if (overall_test_case_status.equals("FAILED"))
		{
			int tempfailed = Integer.valueOf(totaltestcasecountfailed) + 1;
			totaltestcasecountfailed = String.valueOf(tempfailed);
			comm.fw_set_variable("SKIP--totaltestcasecountfailed", totaltestcasecountfailed);
			
			// Added by Mark on 8/9/2017
			String totaltestcasenamesfailed = comm.fw_get_variable("totaltestcasenamesfailed");
	   		comm.fw_set_variable("SKIP--totaltestcasenamesfailed", totaltestcasenamesfailed + "TC" + almtstid + ",");
	   		
	   		String totaltestcasefilesfailed = comm.fw_get_variable("totaltestcasefilesfailed");
	   		comm.fw_set_variable("SKIP--totaltestcasefilesfailed", totaltestcasefilesfailed + "X:\\Test Utility\\Log Files\\" + fileName + "<br>");
	   		// End of Added by Mark on 8/9/2017
		}
		
		
		log.fw_writeLogEntry("JOB SUMMARY","NA");	
		log.fw_writeLogEntry("    JOB Total Tests  : " + totaltestcasecount,"NA");
		log.fw_writeLogEntry("    JOB Total Passed : " + totaltestcasecountpassed,"NA");
		log.fw_writeLogEntry("    JOB Total Failed : " + totaltestcasecountfailed,"NA");
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("Test: " + overall_test_case_status + " - TC" + almtstid + " (Total Tests: " + totaltestcasecount + ", Passed: " + totaltestcasecountpassed + ", Failed: " + totaltestcasecountfailed + ")","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("End Date: " + date_value,"NA");
		log.fw_writeLogEntry("End Time: " + time_value,"NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");
		
		// End of Added by Mark on 8/3/2017
		
		//String runidvalue2 = comm.fw_get_value_from_file(variables_path + "RUNIDVALUE");
		String runidvalue2 = comm.fw_get_variable("RUNIDVALUE");
		Object runidval = runidvalue2;
		//String almtestid = comm.fw_get_value_from_file(variables_path + "ALMTESTID");
		String almtestid = comm.fw_get_variable("ALMTESTID");
		fwalm.fw_update_hpalm_test_case_execution_status(runidval, almtestid, hpalm_status);
		
		Thread.sleep(2000);
		
	}
	
	/**
	 * This function takes a screen shot whenever a failure is encountered during execution.
	 * 
	 * @since: 5/4/2017
	 * @author: Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	
	public void fw_capture_screenshot() 
	{
		
		try
		{
			
			String workspace_name = comm.fw_get_workspace_name();
			String screenshot_path = workspace_name + "\\screenshots\\";
			//String variables_path = workspace_name + "\\variables\\";
			//String alm_test_id_value = comm.fw_get_value_from_file(variables_path + "ALMTESTID");
			//String first_name = comm.fw_get_value_from_file(variables_path + "fname");
			String alm_test_id_value = comm.fw_get_variable("ALMTESTID");
			String first_name = comm.fw_get_variable("fname");
			
			dt.fw_generate_datetime("yyyyMMddkkmmss");
			String date_and_time = dt.generated_date_time_output;
			
			String fileName = screenshot_path + "SCREENSHOT - " + date_and_time + " - TC" + alm_test_id_value + " - " + first_name + ".png";
			
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle screenRectangle = new Rectangle(screenSize);
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(screenRectangle);
			ImageIO.write(image, "png", new File(fileName));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
}