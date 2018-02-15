package com.chtr.tmoauto.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;

/**
 * This is a prototype to writing out webservice security header timestamps and security tokens.
 *
 * @author Danny M. Byers
 * @since 04/25/2017
 */
public class GetWebServiceSecurityHeaderUserToken
{
   public static void main(String args[])
   {

	   
	  String WEBSERVICE_USERNAME = "gateway"; 
	  String WEBSERVICE_PASSWORD = "gatewaypassword";
	  
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());


      // Convert timestamp to instant
      Instant instant = timestamp.toInstant();
      
      Instant instantExpired = instant.plusMillis(120000);
      
      long instantEpoch = instant.toEpochMilli();
      
      // encodes the epoch time stamp in milliseconds + / + gatewaypassword in base64 to make the password token unique every webservice call.
      byte[] encodedBytes = Base64.getEncoder().encode((instant + "/" + WEBSERVICE_PASSWORD).getBytes());

      // can generate this first line of characters after "UsernameToken-" not to exceed HEX values (so only 0-9, A-F)
      // tacks on the epoch timestamp value after that.
      System.out.println("    <wsse:UsernameToken wsu:Id=\"UsernameToken-5162820DAF979C7B8E" + instantEpoch + "\">");
      System.out.println("        <wsse:Username>" + WEBSERVICE_USERNAME + "</wsse:Username>");
      System.out.println(
         "        <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">" + WEBSERVICE_PASSWORD + "</wsse:Password>");
      System.out.println(
         "        <wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">"
            + new String(encodedBytes) + "</wsse:Nonce>");
      System.out.println("        <wsu:Created>" + instant + "</wsu:Created>");
      System.out.println("    </wsse:UsernameToken>");
      System.out.println("</wsse:Security>");

   }
}
