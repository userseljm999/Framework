package com.chtr.tmoauto.util;

import com.mercury.qualitycenter.otaclient.IBaseFactory;
import com.mercury.qualitycenter.otaclient.IList;
//import com.mercury.qualitycenter.otaclient.IProjectDescriptor;
import com.mercury.qualitycenter.otaclient.IRun;
import com.mercury.qualitycenter.otaclient.IRunFactory;
import com.mercury.qualitycenter.otaclient.ITSTest;
import com.mercury.qualitycenter.otaclient.ICommand;
import com.mercury.qualitycenter.otaclient.IRecordset;
import com.mercury.qualitycenter.otaclient.ITestSet;
import com.mercury.qualitycenter.otaclient.ITestSetFolder;
import com.mercury.qualitycenter.otaclient.ITestSetTreeManager;
import com4j.Com4jObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.chtr.tmoauto.logging.Logging;
import com.chtr.tmoauto.webui.GUI;
import com.mercury.qualitycenter.otaclient.ClassFactory;
import com.mercury.qualitycenter.otaclient.ITDConnection;
import com.mercury.qualitycenter.otaclient.IAttachmentFactory;
import com.mercury.qualitycenter.otaclient.IAttachment;
import com.mercury.qualitycenter.otaclient.IExtendedStorage;

//import com.chtr.tmoauto.webui.GUI;

public class ALM {

	public static GUI comm = new GUI();
	public static Logging log = new Logging();
	
	public static Object fw_update_hpalm_test_case_execution_status(Object run_id_value, String strTestCaseId, String strStatus) throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException, SQLException 
	{
			
		String workspace_name = comm.fw_get_workspace_name();
        String variables_path = workspace_name + "\\variables\\";
        
		String testidslist = comm.fw_get_value_from_file(variables_path + "testids");
		String configidslist = comm.fw_get_value_from_file(variables_path + "configids");
		String fname = comm.fw_get_value_from_file(variables_path + "fname");
		String appname = comm.fw_get_value_from_file(variables_path + "appname");
		String outputlogfile = comm.fw_get_value_from_file(variables_path + "OUTPUTLOGFILE");
		String almtestid = "TC" + strTestCaseId;
		String testtype = comm.fw_get_value_from_file(variables_path + "TESTTYPE");
		String tempstatus = strStatus.toUpperCase();
		String computername = comm.fw_get_value_from_file(variables_path + "hostname");		
		comm.fw_write_logging_database(fname, appname, "USERID", "EXECUTION", computername, testtype, almtestid, "This is an execution log message", tempstatus, "", "", outputlogfile);
		
		String folderName= workspace_name + "\\logs\\";
        String fileName = comm.fw_get_value_from_file(variables_path + "OUTPUTLOGFILE");        
        String url = comm.fw_get_value_from_file(variables_path + "HPALMURL");
        String domain = comm.fw_get_value_from_file(variables_path + "HPALMDOMAIN");
        String project = comm.fw_get_value_from_file(variables_path + "HPALMPROJECT");
        String username = comm.fw_get_value_from_file(variables_path + "HPALMUSERNAME");
        String password = comm.fw_get_value_from_file(variables_path + "HPALMPASSWORD");
        String testsetfolder = comm.fw_get_value_from_file(variables_path + "HPALMTESTSETFOLDER");
        String testset = comm.fw_get_value_from_file(variables_path + "HPALMTESTSET");
		
		String updated_alm = "NO";
		
		String automated_flag_value = "";
		
		String cur_test_id = "";
		String expected_config_id = "";
		
		String[] testidslist_arr = testidslist.split(",");
		String[] configidslist_arr = configidslist.split(",");
		int testidslist_len = testidslist_arr.length;
		for (int i=0;i<testidslist_len;i++)
		{
			cur_test_id = testidslist_arr[i];
			if (cur_test_id.contains(strTestCaseId))
			{
				expected_config_id = configidslist_arr[i];
				break;
			}
		}
		
        ITDConnection connection=null;
		
        
        try
        {
          
           	//QC Connection
           	connection = ClassFactory.createTDConnection();
           	connection.initConnectionEx(url);
           
           	connection.initConnectionEx(url);
           	connection.connectProjectEx(domain, project, username, password);
           
           //To get the Test Set folder in Test Lab        
           ITestSetTreeManager objTestSetTreeManager = (connection.testSetTreeManager()).queryInterface(ITestSetTreeManager.class);
           ITestSetFolder objTestSetFolder =(objTestSetTreeManager.nodeByPath(testsetfolder)).queryInterface(ITestSetFolder.class);
                   
           IList tsTestList = objTestSetFolder.findTestSets(null, true, null);
                   
           for (int i=1;i<=tsTestList.count();i++) 
           {
               Com4jObject comObj = (Com4jObject) tsTestList.item(i);
               ITestSet tst = comObj.queryInterface(ITestSet.class); 
               
               if(tst.name().equalsIgnoreCase(testset)){
                           
                   IBaseFactory testFactory = tst.tsTestFactory().queryInterface(IBaseFactory.class);
             
                   IList testInstances = testFactory.newList("");
                   
                   //To get Test Case ID instances
                   for (Com4jObject testInstanceObj : testInstances)
                   {  
                       ITSTest testInstance = testInstanceObj.queryInterface(ITSTest.class);  
                       
                       //System.out.println("Current Looping Test ID: " + testInstance.testId());
                       
                       String continue_flag = "no";
                       
                       // GET CONFIGURATION ID from the Database
	                    // Added for TestID-ConfigID input (if needed)
	               		if (!expected_config_id.equals("NA"))
	               		{
	                       String tc_testcycle_id = testInstance.id().toString();
	                       //System.out.println("Test Instance ID: " + tc_testcycle_id);
	                       
	                       ICommand cmd = (connection.command()).queryInterface(ICommand.class);  
	                       cmd.commandText("SELECT tc_test_config_id FROM TESTCYCL where tc_testcycl_id = " + tc_testcycle_id);
	
	                       String config_id = "";
	                       
	                       IRecordset recordes =(cmd.execute()).queryInterface(IRecordset.class);;
	                       for (int j=0 ;j<recordes.colCount();j++)
	                       {
	                    	   config_id = recordes.fieldValue(recordes.colName(j)).toString();   
	                    	   //System.out.println("Key: "+recordes.colName(j)+" Val:"+recordes.fieldValue(recordes.colName(j)));
	                    	   break;
	                       }
	                       //System.out.println("Current Config ID: " + config_id);
                       
	                       if (config_id.equals(expected_config_id))
	                       {
	                    	  continue_flag = "yes"; 
	                       }
	               		}
	               		else
	               		{
	               			continue_flag = "yes";
	               		}
	               		// End of Added for TestID-ConfigID input (if needed)
                       // End of GET CONFIGURATION ID from the Database
                       
                       
                       //System.out.println("Test Name Value: " + testInstance.testName());
                       
                       //if(testInstance.testName().equalsIgnoreCase(strTestCaseId)){
                       //if(testInstance.testId().equals(strTestCaseId)){
                       
	               		
	               		
                       
                       if(testInstance.testId().equals(strTestCaseId) && continue_flag.contains("yes"))
                       {
                    	   
                    	   
                           IRunFactory runfactory = testInstance.runFactory().queryInterface(IRunFactory.class);
                           
                           if (strStatus.equals("Not Completed"))
                           {
                        	   
                        	   String alm_login = comm.fw_get_value_from_file(variables_path + "ALMUSERIDLOGIN");
                        	   
                        	   updated_alm = "YES";
                        	   
                        	   //IRun run= runfactory.addItem("Selenium" + testtype).queryInterface(IRun.class);
                        	   IRun run= runfactory.addItem("Selenium" + testtype + "-" + appname).queryInterface(IRun.class);
	                           run.status("Not Completed");
	                           run.field("RN_HOST",computername);
	                           //run.field("RN_TESTER_NAME", alm_login);
	                           
	                           try
	                           {
	                        	   //run.field("RN_TESTER_NAME", "melkins");
	                        	   run.field("RN_TESTER_NAME", alm_login);
	                           }
	                           catch (Exception e)
	                           {
	                        	   run.field("RN_TESTER_NAME", "svc_automation");
	                           }
	                           
	                           // Added by Mark on 5/26/2017
	                           if (testtype.equals("JENKINSREGRESSION") || testtype.equals("JENKINSSMOKE") || testtype.equals("JENKINSUNIT"))
	                           {
	                        	   automated_flag_value = "Y";
	                           }
	                           else
	                           {
	                        	   automated_flag_value = "D";
	                           }
	                           run.field("RN_USER_TEMPLATE_01", automated_flag_value);
	                           // End of Added by Mark on 5/26/2017
	                           
	                           run.field("RN_COMMENTS", outputlogfile);
	                           run.post();
	                           run_id_value = run.id();
	                   		
	                           long startTime = System.currentTimeMillis();
	                           comm.fw_set_variable("STARTTIME", String.valueOf(startTime));
	                           
	                           
	                           
	                           // Update Tester in Test Lab section
                        	   
                        	   String tc_testcycle_id = testInstance.id().toString();
                        	   ICommand cmd = (connection.command()).queryInterface(ICommand.class);
                        	   //String sql_string = "SELECT tc_test_id FROM TESTCYCL where tc_testcycl_id = " + tc_testcycle_id + " and tc_test_id = " + strTestCaseId;
                        	   String sql_string = "update testcycl set tc_actual_tester = '" + alm_login + "' where tc_testcycl_id = " + tc_testcycle_id + " and tc_test_id = " + strTestCaseId;
                        	   //System.out.println("ALM SQL: " + sql_string);
                        	   
                        	   if (!alm_login.trim().equals("") && !tc_testcycle_id.trim().equals("") && !strTestCaseId.trim().equals(""))
                        	   {
                        		   cmd.commandText(sql_string);
                        		   IRecordset recordes =(cmd.execute()).queryInterface(IRecordset.class);;
                        	   }
                        	   
                        	   // End of Update Tester in Test Lab section
                        	   
                        	   
                           }
                           else
                           {
                        	
                        	   try
                        	   {
                        	   
                        		   updated_alm = "YES";
                        	   
		                           IRun run = runfactory.item(run_id_value).queryInterface(IRun.class);
		                           run.status(strStatus);
		                           
		                           long endTime = System.currentTimeMillis();
		                           String start_Time = comm.fw_get_value_from_file(variables_path + "STARTTIME");
		                           long duration_value = (endTime - Long.valueOf(start_Time))/1000;
		                           run.field("RN_DURATION",duration_value);
		                           
		                           run.post();
			
		                           String alm_login = comm.fw_get_value_from_file(variables_path + "ALMUSERIDLOGIN");
		                           
		                           // Update Tester in Test Lab section
	                        	   
	                        	   String tc_testcycle_id = testInstance.id().toString();
	                        	   ICommand cmd = (connection.command()).queryInterface(ICommand.class);
	                        	   //String sql_string = "SELECT tc_test_id FROM TESTCYCL where tc_testcycl_id = " + tc_testcycle_id + " and tc_test_id = " + strTestCaseId;
	                        	   String sql_string = "update testcycl set tc_actual_tester = '" + alm_login + "' where tc_testcycl_id = " + tc_testcycle_id + " and tc_test_id = " + strTestCaseId;
	                        	   //System.out.println("ALM SQL: " + sql_string);
	                        	   
	                        	   if (!alm_login.trim().equals("") && !tc_testcycle_id.trim().equals("") && !strTestCaseId.trim().equals(""))
	                        	   {
	                        		   cmd.commandText(sql_string);
	                        		   IRecordset recordes =(cmd.execute()).queryInterface(IRecordset.class);;
	                        	   }
	                        	   
	                        	   // End of Update Tester in Test Lab section
	                        	   
			                       try
			                       {
			                        	IAttachmentFactory attachfac = run.attachments().queryInterface(IAttachmentFactory.class);
			                        	IAttachment attach = attachfac.addItem(fileName).queryInterface(IAttachment.class);
			                        	IExtendedStorage extAttach = attach.attachmentStorage().queryInterface(IExtendedStorage.class);
			                        	extAttach.clientPath(folderName);
			
			                        	extAttach.save(fileName, true);
			                        	attach.description("test");
				
			                        	attach.post();
			                        	attach.refresh();
			                        	   
			                        }
			                        catch(Exception e) 
			                        {
			                        	System.out.println("QC Exception : "+e.getMessage());
			                        }

                        	   }
		                       catch(Exception e) 
		                       {
		                    	   System.out.println("Attempting to update ALM: (Test ID: " + strTestCaseId + ", Status: " + strStatus + ") - " + e.getMessage());
		                    	   updated_alm = "NOT";
		                       }
                           }
                           
                           break;
                       }
                   } 
               }
           }
           
           // JOB SUMMARY EMAIL
           
           if (!strStatus.equals("Not Completed"))
           {
	           if (testtype.equals("JENKINSREGRESSION") || testtype.equals("JENKINSSMOKE"))
	           {
	        	   String[] testidslistarr = testidslist.split(",");
	    	   	   int testidslistlen = testidslistarr.length;
	    	   	   String last_testid_value = testidslistarr[testidslistlen-1];
	    	   	   if (almtestid.equals(last_testid_value))
	    	   	   {
		        	   String env_selected = comm.fw_get_variable("ENVSELECTED");
		        	   String almtestsetcategory = comm.fw_get_variable("ALMTESTSETCATEGORY");
		        	   String almuseridlogin = comm.fw_get_variable("ALMUSERIDLOGIN");
		        	   
		        	   String OVERALLTESTCASESTATUS = comm.fw_get_variable("OVERALLTESTCASESTATUS");
		        	   String email_subject = "JSF JOB SUMMARY - " + appname + " " + testtype + " in " + env_selected + " env " + OVERALLTESTCASESTATUS;
		        	   
		        	   String totaltestpassedcount = comm.fw_get_variable("totaltestcasecountpassed");
		        	   String totaltestfailedcount = comm.fw_get_variable("totaltestcasecountfailed");
		        	   String totaltestcasenamespassed = comm.fw_get_variable("totaltestcasenamespassed");
		        	   String totaltestcasefilespassed = comm.fw_get_variable("totaltestcasefilespassed");		   	   		
		        	   String totaltestcasenamesfailed = comm.fw_get_variable("totaltestcasenamesfailed");
		        	   String totaltestcasefilesfailed = comm.fw_get_variable("totaltestcasefilesfailed");
		        	   
		        	   String emailto_address_smoke_pass = comm.fw_get_variable("emailtoaddresssmokepass");
		       		   String emailto_address_smoke_fail = comm.fw_get_variable("emailtoaddresssmokefail");
		       		   String emailto_address_reg_pass = comm.fw_get_variable("emailtoaddressregpass");
		       		   String emailto_address_reg_fail = comm.fw_get_variable("emailtoaddressregfail");
		       		
		        	   String a0 = "<br>";
		        	   String b1 = "JOB SUMMARY" + a0;
		        	   String b2 = "	Total Tests: " + testidslistlen + a0;
		        	   String b3 = "	Total Passed: " + totaltestpassedcount + a0;
		        	   String b4 = "	Total Failed: " + totaltestfailedcount + a0;
		        	   String b5 = "" + a0;
		        	   String b6 = "	Tests Passed: " + totaltestcasenamespassed + a0;
		        	   String b7 = "	Tests Failed: " + totaltestcasenamesfailed + a0;
		        	   String b8 = "" + a0;
		        	   
		        	   String b9 = "Application: " + appname + a0;
		        	   String b10 = "ALM Test Set Category: " + almtestsetcategory + a0;
		        	   String b11 = "ALM Test Set: " + testset + a0;
		        	   String b12 = "ALM Test Set Folder: " + testsetfolder + a0;
		        	   String b13 = "Execution User: " + fname + a0;
		        	   String b14 = "Test Type: " + testtype + a0;
		        	   String b15 = "Workstation: " + computername + a0;
		        	   String b16 = "Tester: " + almuseridlogin + a0;
		        	   String b17 = "Environment: " + env_selected + a0 + a0;
		        	   String b18 = "PASSED Output Files" + a0;
		        	   String b19 = totaltestcasefilespassed + a0;
		        	   String b20 = "FAILED Output Files" + a0;
		        	   String b21 = totaltestcasefilesfailed + a0;
		        	   
			        	// Updated on 1/15/2018
			   	   		
			   			String userName = comm.fw_get_variable("usernameTESTDB");
			   			String autopassword = comm.fw_get_variable("passwordTESTDB");
			   			String connection_string = comm.fw_get_variable("connectionstringTESTDB");
			   			
			   			Connection autoconnection = null;
			   			Class.forName("oracle.jdbc.driver.OracleDriver");
			   			autoconnection = DriverManager.getConnection(connection_string, userName, autopassword);
			   			
			   			String sql_query = "select logging_teststep_desc_failed outputvalue from tbl_tclogging where logging_outputfile = '" + outputlogfile + "'";
			   			
			   			ResultSet rs = null;
			   			Statement st = autoconnection.createStatement();
			   			rs = st.executeQuery(sql_query);
			   			
			   			System.out.println("SQL Query: " + sql_query);
			   			String outputvalue = "";
			   			
			   			if (rs.next())
			   			{
			   				outputvalue = rs.getString("outputvalue");
			   				System.out.println("outputvalue: " + outputvalue);
			   				autoconnection.close();
			   			}
			   			System.out.println("outputvalue2: " + outputvalue);
			   			
			   	   		// End of Updated on 1/15/2018
		        	   
		        	   String email_body = b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9 + b10 + b11 + b12 + b13 + b14 + b15 + b16 + b17 + b18 + b19 + b20 + b21 + a0 + a0 + "Failed Steps" + a0 + outputvalue;
		               
		        	   String jobsummary_email_to_recipients = "";
		        	   
		        	   if (!emailto_address_smoke_pass.equals("") && totaltestfailedcount.equals("0") && testtype.equals("JENKINSSMOKE"))
		        	   {
		        		   jobsummary_email_to_recipients = emailto_address_smoke_pass;
		        	   }
		        	   else if (!emailto_address_smoke_fail.equals("") && !totaltestfailedcount.equals("0") && testtype.equals("JENKINSSMOKE"))
		        	   {
		        		   jobsummary_email_to_recipients = emailto_address_smoke_fail;
		        	   }
		        	   
		        	   else if (!emailto_address_reg_pass.equals("") && totaltestfailedcount.equals("0") && testtype.equals("JENKINSREGRESSION"))
		        	   {
		        		   jobsummary_email_to_recipients = emailto_address_reg_pass;
		        	   }
		        	   else if (!emailto_address_reg_fail.equals("") && !totaltestfailedcount.equals("0") && testtype.equals("JENKINSREGRESSION"))
		        	   {
		        		   jobsummary_email_to_recipients = emailto_address_reg_fail;
		        	   }
		        	   
		        	   if (!jobsummary_email_to_recipients.equals(""))
		        	   {
		        		   connection.sendMail(jobsummary_email_to_recipients, "mark.elking@charter.com", email_subject, email_body, "", "");
		        	   }
         
	    	   	   }
	           }
           }
           
           // END OF JOB SUMMARY EMAIL
           

           // INDIVIDUAL TEST CASE SUMMARY EMAIL
           
           /*
           if (!strStatus.equals("Not Completed"))
           {
        	   if (testtype.equals("JENKINSREGRESSION") || testtype.equals("JENKINSSMOKE"))
	           {
	        	   String env_selected = comm.fw_get_variable("ENVSELECTED");
	        	   String almtestsetcategory = comm.fw_get_variable("ALMTESTSETCATEGORY");
	        	   String email_subject = "JSF " + appname + " " + testtype + " " + almtestid + " " + tempstatus + " in " + env_selected + " env";
	        	   
	        	   String a0 = "<br>";
	        	   String a1 = "Application: " + appname + a0;
	        	   String a2 = "ALM Test Set Category: " + almtestsetcategory + a0;
	        	   String a3 = "ALM Test Set: " + testset + a0;
	        	   String a4 = "ALM Test Set Folder: " + testsetfolder + a0;
	        	   String a5 = "Execution User: " + fname + a0;
	        	   String a6 = "Test Type: " + testtype + a0;
	        	   String a7 = "Workstation: " + computername + a0;
	        	   String a8 = "Environment: " + env_selected + a0;
	        	   String a9 = "Test ID: " + almtestid + a0;
	        	   String a10 = "Test Status: " + tempstatus + a0;
	        	   String a11 = "Output File: " + "X:\\Test Utility\\Log Files\\" + fileName + a0;

	        	   String email_body = a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9 + a10 + a11;
	        	   
	        	   String jobsummary_email_to_recipients = "mark.elking@charter.com";
	        	   	        	   
	               if (!jobsummary_email_to_recipients.equals(""))
	               {
	            	   connection.sendMail(jobsummary_email_to_recipients, "mark.elking@charter.com", email_subject, email_body, "", "");
	               }
	           }
           }
           */
           
           // END OF INDIVIDUAL TEST CASE SUMMARY EMAIL
           
           
           if (updated_alm.equals("NO"))
           {
        	   log.fw_writeLogEntry(" ", "NA");
        	   log.fw_writeLogEntry(" ***** Stopping Execution because Test Case NOT Found in Test Set ****** ", "NA");
        	   log.fw_writeLogEntry(" ***** Test ID: " + strTestCaseId + " ****** ", "NA");
        	   log.fw_writeLogEntry(" ***** Test Set: " + testset + " ****** ", "NA");
        	   log.fw_writeLogEntry(" ***** Test Set Folder: " + testsetfolder + " ****** ", "NA");
        	   log.fw_writeLogEntry(" ", "NA");
        	   
        	   System.exit(0);
        	   
           }
           else if (updated_alm.equals("NOT"))
           {
        	   log.fw_writeLogEntry(" ", "NA");
        	   log.fw_writeLogEntry(" ***** Attempted to update ALM but FAILED ****** ", "NA");
        	   log.fw_writeLogEntry(" ***** Test ID: " + strTestCaseId + " ****** ", "NA");
        	   log.fw_writeLogEntry(" ***** Test Set: " + testset + " ****** ", "NA");
        	   log.fw_writeLogEntry(" ***** Test Set Folder: " + testsetfolder + " ****** ", "NA");
        	   log.fw_writeLogEntry(" ", "NA");
        	   
        	   System.exit(0);
        	   
           }
       }
       catch(Exception e)
       {
           System.out.println(e.getMessage());
           
           System.exit(0);
       }
       finally
       {
    	   connection.disconnectProject();
           connection.releaseConnection();
       }
       
       return run_id_value;
       
   }
		 
}