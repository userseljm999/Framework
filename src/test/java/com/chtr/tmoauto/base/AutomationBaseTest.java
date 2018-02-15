/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Nagaraju Meka
* File:    AutomationBaseTest.java
* Created: Dec 08, 2016
*
* Description:
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.chtr.tmoauto.base;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;

import com.chtr.tmoauto.webui.AutomationController;
import com.chtr.tmoauto.webui.WebFunctions;

public class AutomationBaseTest
{
   protected static final Logger log = LoggerFactory.getLogger(AutomationBaseTest.class);
   protected AutomationController aController = new AutomationController();
   protected WebFunctions webController;


   public void getWebFunctions(WebFunctions controller)
   {
      this.webController = controller;
   }


   /**
    * Throws a SkipException with your string message added; your test method will stop executing at this point
    *
    * @param message appended to the SkipException
    * @throws SkipException with your message appended
    */
   public void skip(String message)
   {
      throw new SkipException(message);
   }


   /**
    * Assert the element is present
    *
    * @param locator
    * @param message - print the elementName which is defined by the user
    */
   public void assertElementExist(String locator)
   {
      Assert.assertTrue(webController.isElementPresentWithWait(locator, 120), "Could not locate " + locator + " on Screen");
      log.info(locator + " Present on the screen");
   }


   /**
    * Assert the element is present
    *
    * @param locator
    * @param message - print the elementName which is defined by the user
    */
   public void assertElementExist(String locator, String message)
   {
      Assert.assertTrue(webController.isElementPresentWithWait(locator, 120), "Could not locate " + locator + " on Screen");
      log.info("{}", message);
   }


   /**
    * Assert the expected value equals the actual value
    *
    * @param expectedValue String
    * @param actualValue String
    */
   public void assertTrue(String expectedValue, String actualValue, String message)
   {
      Assert.assertTrue(expectedValue.trim().equals(actualValue.trim()), " Expected Value: " + expectedValue + " Actual Value: " + actualValue);
      log.info("Passed: --> expected { " + expectedValue + " } Matches actual --> { " + actualValue + " }");
   }


   /**
    * Assert the expected value equals the actual value
    *
    * @param expectedValue int
    * @param actualValue int
    * @param message String
    */
   public void assertSame(int expectedValue, int actualValue, String message)
   {
      Assert.assertSame(expectedValue, actualValue, message + " Expected Value: " + expectedValue + " Actual Value: " + actualValue);
      log.info("Passed: --> expected { " + expectedValue + " } Matches actual --> { " + actualValue + " }");
   }


   /**
    * Assert the expected value equals the actual value <br>
    * message is added to describe what is being compared for logs
    *
    * @param expectedValue String
    * @param actualValue String
    * @param message String
    */
   public void assertEquals(String expectedValue, String actualValue, String message)
   {
      Assert.assertEquals(expectedValue.trim(), actualValue.trim(), message);
      log.info("Passed: --> expected { " + expectedValue + " } Equals actual --> { " + actualValue + " }");
   }


   /**
    * Assert the expected value equals the actual value <br>
    * message is added to describe what is being compared for logs
    *
    * @param expectedValue int
    * @param actualValue int
    * @param message String
    */
   public void assertEquals(int expectedValue, int actualValue, String message)
   {
      Assert.assertEquals(expectedValue, actualValue, message);
      log.info("Passed: --> expected { " + expectedValue + " } Equals actual --> { " + actualValue + " }");
   }


   /**
    * Assert if condition is true.
    *
    * Message can be concatenated with comma delimited
    *
    * Example: assertTrue(10==2,"10 does not equal ", "2") will throw error message "10 does not equal 2"
    *
    * @param condition boolean
    * @param message String[]
    */ 
   public void assertTrue(boolean condition, String... message)
   {
      StringBuilder sb = new StringBuilder();
      for (String each : message)
      {
         sb.append(each);
      }
      Assert.assertTrue(condition, sb.toString());
   }
}

