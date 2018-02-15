/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  
* File:    SampleScreen.java
* Created: Dec 16, 2016
*
* Description:
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.chtr.tmoauto.pageobjects;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chtr.tmoauto.webui.WebFunctions;


public class SampleScreen
{
   protected static final Logger log = LoggerFactory.getLogger(SampleScreen.class);
   protected WebFunctions webController;
   public static final String SEARCHMENU = "css=input[title='Search']";
   public static final String SEARCHBOX = "css=input[id='lst-ib']";
   public static final String SEARCHBUTTON = "input[value='Google Search']";

   /**
    * 
    * @param webFunctions
    */
   public void setWebFunctions(WebFunctions webFunctions)
   {
      this.webController = webFunctions;
   }


   /**
    * 
    * @return
    */
   public boolean checkAlert()
   {
      return webController.isAlertPresent();
   }
   
   
   public void setSearchText(String someString)
   {
      webController.type(SEARCHBOX, someString);
   }
   
   public void clickSearchButton()
   {
      webController.waitForElementVisible(SEARCHBUTTON, 10);
      log.info("Waiting on Button to appear...");
      webController.click(SEARCHBUTTON);
 //     webController.waitForElementVisible(SEARCHSCREEN_MAIN_ELEMENT, 120);
   }
   
   
}

