package com.chtr.tmoauto.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is used to read from properties file.
 * @since: 10/26/2016
 * @author: Sangram Pisal
 */
public class ReadFromProperty
{

	/**
	 * This function is used to read value from property file.
	 * 
	 * @param: PropertyName
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */

	public String fw_Read_From_Property_file(String PropertyName) throws IOException
	{
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream("config.properties");
		prop.load(input);
		return prop.getProperty(PropertyName);

	}
}
