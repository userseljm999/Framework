package com.chtr.tmoauto.logging;

import com.chtr.tmoauto.webui.GUI;
import com.chtr.tmoauto.util.MSExcel;
import com.chtr.tmoauto.util.DateTime;
import com.mercury.qualitycenter.otaclient.ClassFactory;
import com.mercury.qualitycenter.otaclient.ICommand;
import com.mercury.qualitycenter.otaclient.IRecordset;
import com.mercury.qualitycenter.otaclient.ITDConnection;

import java.io.FileWriter;
import java.io.IOException;

public class LoggingMultiThreaded 
{
 
	GUI comm = new GUI();	
	
	//public static Logging log = new Logging();
	DateTime dt = new DateTime();
	
	int step_total_cnt = 0;
	int step_passed_cnt = 0;
	int step_failed_cnt = 0;
	int step_fatal_cnt = 0;
	int step_error_cnt = 0;
	int step_warn_cnt = 0;
	int step_info_cnt = 0;
	int step_debug_cnt = 0;
	
	String outputlogfile = "";
	
	String hpalm_url = "";
	String hpalm_domain = "";
	String hpalm_project = "";
	String hpalm_username = "";
	String hpalm_password = "";
	String hpalm_testsetname = "";
	
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
	 * @since 11/01/2016 
	 */
	
	public void fw_create_output_log_file(String alm_test_id, String test_description) throws IOException {
		
		String workspace_name = System.getProperty("user.dir");
		if (!workspace_name.contains("\\"))
		{
			workspace_name.replace("\\","\\\\");	
		}
		
		dt.fw_generate_datetime("yyyyMMddkkmmss");
		String date_and_time = dt.generated_date_time_output;
		
		FileWriter fw;
		outputlogfile = workspace_name + "\\logs\\OUTPUT LOG FILE - TESTID" + alm_test_id + " - " + date_and_time + ".txt";
		
		fw = new FileWriter(outputlogfile,true);
		fw.close();
		
		fw_writeLogEntry(System.lineSeparator(),"NA");
		fw_writeLogEntry("AUTOMATED OUTPUT FILE","NA");
		fw_writeLogEntry(System.lineSeparator(),"NA");
		fw_writeLogEntry("--------------------------------------------","NA");
		fw_writeLogEntry("Start Date/Time: " + date_and_time,"NA");
		fw_writeLogEntry("ALM Domain     : " + hpalm_domain,"NA");
		fw_writeLogEntry("ALM Project    : " + hpalm_project,"NA");
		fw_writeLogEntry("ALM User       : " + hpalm_username,"NA");
		fw_writeLogEntry("ALM Test Set   : " + hpalm_testsetname,"NA");
		fw_writeLogEntry("ALM Test ID    : " + alm_test_id,"NA");
		fw_writeLogEntry("--------------------------------------------","NA");			
		fw_writeLogEntry("","NA");
		
		//********************** ALM *************************
		
		ITDConnection itdc = ClassFactory.createTDConnection();
		itdc.initConnectionEx(hpalm_url);
		itdc.connectProjectEx(hpalm_domain, hpalm_project, hpalm_username, hpalm_password);		
		ICommand cmd = (itdc.command()).queryInterface(ICommand.class);  	
		
		// Get Test Case Details and Write to Log File
		
		fw_writeLogEntry("ALM DESIGN STEPS","NA");
		fw_writeLogEntry("","NA");

		cmd.commandText("select ds_step_name, ds_description, ds_expected from dessteps where ds_test_id = " + alm_test_id);
		IRecordset recordes =(cmd.execute()).queryInterface(IRecordset.class);
		
		int loop_count = 0;
		
		String cur_ds_step_name = "";
		String cur_ds_description = "";
		String cur_ds_expected = "";
		
		recordes.first();
		for (int r=0 ;r<recordes.recordCount();r++)
		{
			loop_count++;
			
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
			
			fw_writeLogEntry("Step Name:       " + cur_ds_step_name,"NA");
			fw_writeLogEntry("Description:     " + cur_ds_description,"NA");
			fw_writeLogEntry("Expected Result: " + cur_ds_expected,"NA");
			fw_writeLogEntry("","NA");
			
			recordes.next();

		}
		
		// End of Get Test Case Details and Write to Log File
		
		fw_writeLogEntry("","NA");
		fw_writeLogEntry("--------------------------------------------","NA");
		fw_writeLogEntry("","NA");				
		fw_writeLogEntry("AUTOMATION EXECUTION STEPS","NA");
		fw_writeLogEntry("","NA");
		
		step_total_cnt = 0;
		step_passed_cnt = 0;
		step_failed_cnt = 0;
		
		itdc.disconnectProject();
		
		fw_writeLogEntry("Test Description: " + test_description,"NA");
		fw_writeLogEntry(System.lineSeparator(), "NA");
		
	}		
	
	/**
	 * This function gets the ALM connection information within the 
	 * corresponding ALM spreadsheet in the project corresponding to
	 * the workspace which this test is being run from (i.e. Gateway).
	 * The ALM spreadsheet resides in the Gateway\config directory.
	 * 
	 * @author Mark Elking
	 * @since 11/1/2016
	 * 
	 */
	
	public void fw_get_alm_config_parms() {
		
		String attribute_value;
		String value_value;
		
		String workspace_name = System.getProperty("user.dir");
		if (!workspace_name.contains("\\"))
		{
			workspace_name.replace("\\","\\\\");	
		}
		
		String alm_path = workspace_name + "\\config\\ALM.xlsx";		
		MSExcel xls = new MSExcel(alm_path);
			
		int Rows = xls.getRowCount("ALM");
		
		for (int x=2;x<Rows+1;x++)
		{
			attribute_value = xls.getCellData("ALM", "Attribute", x);
			value_value = xls.getCellData("ALM", "Value", x);
			
			if (attribute_value.equals("HPALMURL"))
			{
				hpalm_url = value_value;
			}
			if (attribute_value.equals("HPALMDomain"))
			{
				hpalm_domain = value_value;
			}
			if (attribute_value.equals("HPALMProject"))
			{
				hpalm_project = value_value;
			}
			if (attribute_value.equals("HPALMUsername"))
			{
				hpalm_username = value_value;
			}
			if (attribute_value.equals("HPALMPassword"))
			{
				hpalm_password = value_value;
			}
			if (attribute_value.equals("HPALMTestSet"))
			{
				hpalm_testsetname = value_value;
			}
		}
						
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
	 * 			FATAL - log4j fatal
	 * 			ERROR - log4j error
	 * 			WARN - log4j warn
	 * 			INFO - log4j info
	 * 			DEBUG - log4j debug
	 * 
	 * @author Mark Elking
	 * @since 9/3/2016
	 * 
	 */
	
	public void fw_writeLogEntry(String log_message, String rc_value) 
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
			if (rc_value == "NA")
			{
				file_log_entry = log_message + System.lineSeparator();
				systemout_log_entry = log_message;
				
				fw.write(file_log_entry);
				System.out.println(systemout_log_entry);
				
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
				}
				if (rc_value.toUpperCase().equals("FATAL"))
				{
					step_fatal_cnt = step_fatal_cnt + 1;
					step_status = "FATAL";
				}
				if (rc_value.toUpperCase().equals("ERROR"))
				{
					step_error_cnt = step_error_cnt + 1;
					step_status = "ERROR";
				}
				if (rc_value.toUpperCase().equals("INFO"))
				{
					step_info_cnt = step_info_cnt + 1;
					step_status = "INFO";
				}
				if (rc_value.toUpperCase().equals("DEBUG"))
				{
					step_debug_cnt = step_debug_cnt + 1;
					step_status = "DEBUG";
				}
				if (rc_value.toUpperCase().equals("WARN"))
				{
					step_warn_cnt = step_warn_cnt + 1;
					step_status = "WARN";
				}
				file_log_entry = "Step " + step_total_cnt + " - " + step_status + " - " + log_message + " - " + dt.generated_date_time_output + System.lineSeparator();
				systemout_log_entry = "Step " + step_total_cnt + " - " + step_status + " - " + log_message + " - " + dt.generated_date_time_output;
			
				fw.write(file_log_entry);
				System.out.println(systemout_log_entry);
				
				fw.write("--------------------------------------------" + System.lineSeparator());
				System.out.println("--------------------------------------------");
				
			}
			
			fw.close();
		    		      
		} 
		catch (IOException e) 
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
	 * @since 11/1/2016
	 * 
	 */
	
	public void fw_closedown_test() throws IOException 
	{
		
		// Close Log File
		
		String overall_test_case_status = "";
		
		dt.fw_generate_datetime("yyyyMMddkkmmss");
		String date_and_time = dt.generated_date_time_output;
		
		if (step_failed_cnt == 0)
		{
			overall_test_case_status = "PASSED";		
		}
		else
		{
			overall_test_case_status = "FAILED";
		}
		
		fw_writeLogEntry(System.lineSeparator(),"NA");
		fw_writeLogEntry("OVERALL TEST CASE STATUS: " + overall_test_case_status,"NA");
		fw_writeLogEntry("    Total Steps : " + step_total_cnt,"NA");
		fw_writeLogEntry("    Total Passed: " + step_passed_cnt,"NA");
		fw_writeLogEntry("    Total Failed: " + step_failed_cnt,"NA");
		fw_writeLogEntry("--------------------------------------------","NA");
		fw_writeLogEntry(System.lineSeparator(),"NA");
		fw_writeLogEntry("End Date/Time   : " + date_and_time,"NA");
		fw_writeLogEntry(System.lineSeparator(),"NA");
		
	}
	
}
