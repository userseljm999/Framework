/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Nagaraju Meka
* File:    AutomationController.java
* Created: Dec 07, 2016
*
* Description: Class to help normalize startup and usage of
* retrieving the objects from Jenkins/Properties file.

*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/
package com.chtr.tmoauto.webui;




import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AutomationController
{
   protected static final Logger log = LoggerFactory.getLogger(AutomationController.class);
   protected WebFunctions webController;
   protected String url, browser, environment;
   AutomationProperties aProperties = new AutomationProperties();


   /**
    * Method that spawns a specific type of browser, in a specific environment, with a specific URL
    * @param method
    * @return
    */
   public WebFunctions launchBrowser(Method method)
   {
      url = aProperties.getValue("url");
      browser = aProperties.getValue("browser");
      environment = aProperties.getValue("environment");
      webController = WebFunctions.buildWebDriver(url, browser, method);
      webController.windowMaximize();
      return webController;
   }


   /**
    * Method that closes all open browsers 
    */
   public void quitBrowser()
   {
      webController.quit();
      try
      {
         log.info("Closing the instance");
         webController.quit();
      } catch (Exception e)
      {
         log.info("Went wrong, closing the instance");
         webController.quit();
      }
   }


   /**
    * Method that gets the current webDriver
    * @return
    */
   public WebFunctions getwebController()
   {
      return webController;
   }


   /**
    * 
    * @return
    */
   public boolean waitForPageLoad()
   {
      return webController.waitForPageLoad();
   }


   /**
    * 
    * @param seconds
    */
   public void delay(int seconds)
   {
      webController.delay(seconds);
   }
}

