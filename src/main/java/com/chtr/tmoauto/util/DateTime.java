package com.chtr.tmoauto.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.chtr.tmoauto.logging.Logging;
import com.chtr.tmoauto.webui.GUI;

public class DateTime {

	public static String generated_date_time_output;
	public static Logging log = new Logging();
	public static GUI fwgui = new GUI();
	
	/**
	 * This function changes date format.
	 * 
	 * oldDateString = 12/08/2010
	 * olddateformat = dd/MM/yyyy
	 * newdateformat = yyyy/MM/dd
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since 10/03/2017
	 */
	
	public static void fw_change_date_format(String outfilename, String oldDateString, String olddateformat, String newdateformat) throws ParseException, MalformedURLException, IOException, InterruptedException 
	{
		
    	String newDateString;

    	SimpleDateFormat sdf = new SimpleDateFormat(olddateformat);
    	Date d = sdf.parse(oldDateString);
    	sdf.applyPattern(newdateformat);
    	newDateString = sdf.format(d);  
    	
    	fwgui.fw_set_variable("SKIP--" + outfilename, newDateString);
    	
    	log.fw_writeLogEntry("Change Date Format (OutFileName: " + outfilename + ", oldDateString: " + oldDateString + ", olddateformat: " + olddateformat + ", newdateformat: " + newdateformat + ", newDateString: " + newDateString + ")", "0");
    	
	}
	
	/**
	 * This function generates a future date based on any date.
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since 10/03/2017
	 */
	
	public static void fw_get_future_date_basedonbasedate(String outfilename, String startdate, String dateformat, int numdaysinfuture) throws ParseException, MalformedURLException, IOException, InterruptedException 
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat( dateformat );   
    	Calendar cal = Calendar.getInstance();    
    	cal.setTime( dateFormat.parse(startdate));    
    	cal.add( Calendar.DATE, numdaysinfuture );    
    	String futuredate = dateFormat.format(cal.getTime());    
    	
    	fwgui.fw_set_variable("SKIP--" + outfilename, futuredate);
    	
    	log.fw_writeLogEntry("Generate Future Date based on Base Date (Name: " + outfilename + ", Start Date: " + startdate + ", Date Format: " + dateformat + ", # Days in Future: " + numdaysinfuture + ", Future Date: " + futuredate + ")", "0");
    	
	}
	
	/**
	 * This function generates a date and/or time in any format that you provide.
	 * Examples include the following:
	 * 
	 * 		yyyyMMddkkmmss 	yields 	20161123023155		
	 * 		kk:mm:ss a 		yields 	12:19:17 PM
	 * 
	 * @author Mark Elking
	 * @throws FileNotFoundException 
	 * @since 12/05/2016
	 */
	
	public static void fw_generate_datetime(String datetime_format) throws FileNotFoundException 
	{
				
		generated_date_time_output = "";
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(datetime_format);
		String formatteddate = df.format(date);
		generated_date_time_output = formatteddate;
		
	}
	
	/**
	 * This function generates a FUTURE date and/or time in any format that you provide.
	 * Examples include the following:
	 * 
	 * 		yyyyMMddkkmmss 	yields 	20161123023155		
	 * 		kk:mm:ss a 		yields 	12:19:17 PM
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 1/30/2017
	 */
	
	public static void fw_generate_datetime_future(String variable_name, String datetime_format, int number_days_in_future) throws ParseException, InterruptedException, IOException 
	{

		String text_msg = "";
		
		generated_date_time_output = "";
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(datetime_format);
		String formatteddate = df.format(date);
		Calendar c = Calendar.getInstance();
		c.setTime(df.parse(formatteddate));
		c.add(Calendar.DATE, number_days_in_future);  // number of days to add
		formatteddate = df.format(c.getTime());  // dt is now the new date
		
		
		String workspace_name = System.getProperty("user.dir");
		if (!workspace_name.contains("\\")) 
		{
			workspace_name.replace("\\","\\\\");	
		}
		
		// Write variable value into variable file
		
		String outputfile = workspace_name + "\\variables\\" + variable_name;
		
		try(PrintWriter out = new PrintWriter(outputfile))
		{
		    out.println(formatteddate);
		    out.close();
		}
		
		// End of Write variable value into variable file
		
		log.fw_writeLogEntry("Generate Future Date (Name: " + variable_name + ", Format: " + datetime_format + ", # Days in Future: " + number_days_in_future + ", Future Date: " + formatteddate + ")" + text_msg, "0");
		
	}
	
	/**
	 * This function generates a FUTURE date and/or time in any format that you provide.
	 * Examples include the following:
	 * 
	 * 		yyyyMMddkkmmss 	yields 	20161123023155		
	 * 		kk:mm:ss a 		yields 	12:19:17 PM
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 1/30/2017
	 */
	
	public static void fw_generate_datetime_current(String variable_name, String datetime_format) throws ParseException, InterruptedException, IOException 
	{

		String text_msg = "";
		
		generated_date_time_output = "";
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(datetime_format);
		String formatteddate = df.format(date);
		
		String workspace_name = System.getProperty("user.dir");
		if (!workspace_name.contains("\\")) 
		{
			workspace_name.replace("\\","\\\\");	
		}
		
		// Write variable value into variable file
		
		String outputfile = workspace_name + "\\variables\\" + variable_name;
		
		try(PrintWriter out = new PrintWriter(outputfile))
		{
		    out.println(formatteddate);
		    out.close();
		}
		
		// End of Write variable value into variable file
		
		log.fw_writeLogEntry("Generate Current Date (Name: " + variable_name + ", Format: " + datetime_format + ", Current Date: " + formatteddate + ")" + text_msg, "0");
		
	}
	
}
