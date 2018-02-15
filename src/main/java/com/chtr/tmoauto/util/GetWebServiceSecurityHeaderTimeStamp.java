/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2017.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Danny M. Byers
* File:    GetWebServiceSecurityHeaderTimeStamp.java
* Created: Apr 25, 2017
*
* Description: Webservices require a security Header that creates a unique timestamp
* and translates it to epoch time and appends that to the end of a string of characters.
* It also adds a created and expires time stamp.  This can be made 
* customizable later to 
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.chtr.tmoauto.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;





/**
 * This is a prototype to writing out webservice security header timestamps.
 *
 * @author Danny M. Byers
 * @since 04/25/2017
 */
public class GetWebServiceSecurityHeaderTimeStamp
{
   public static void main(String args[])
   {
	   

	  
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      
      // Convert timestamp to instant 
      Instant instant = timestamp.toInstant();
 
      // adds two minutes to expired timestamp
      // TODO make this method accept integers in seconds to be converted to milliseconds (#secs * 1000)
      Instant instantExpired = instant.plusMillis(120000);

      
      // converts back to epoch timestamp format to append to end of TS values      
      long instantEpoch = instant.toEpochMilli();
      
      
      // Convert instant to timestamp
      //Timestamp tsFromInstant = Timestamp.from(instant);
      // System.out.println(tsFromInstant.getTime());
      // System.out.println(instant);
      // System.out.println(instantExpired);

      /**
       * Prints out the Security Header for Webservices Timestamp.  Must get called before User Token.
       */
      
      System.out.println(
         "<wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">");
      System.out.println("    <wsu:Timestamp wsu:Id=\"TS-5162820DAF979C7B8E" + instantEpoch + "\">");
      System.out.println("        <wsu:Created>" + instant + "</wsu:Created>");
      System.out.println("        <wsu:Expires>" + instantExpired + "</wsu:Expires>");
      System.out.println("    </wsu:Timestamp>");
   }
}
