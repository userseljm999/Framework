/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Nagaraju.Meka
* File:    WebFunctions.java
* Created: Dec 06, 2016
*
* Description: Interface for selenium actions that interact with
* the browser.
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.chtr.tmoauto.webui;


import java.lang.reflect.Method;
/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
 * Author: Nagaraju.Meka
 * File: CommonFunctions.java
 * Created: 11/25/16
 * Description: Class to help normalize startup and usage of web driver objects.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */
import java.util.List;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;


/*
 * This interface contains wrappers of core Selenium web actions.
 */

public interface WebFunctions extends AutoCloseable
{
	
   /**
    * This method clicks an OK popup alert.
    */
   void fw_acceptAlert();


   /** Add a selection to the set of selected options in a multi-select element using an option locator.
    *
    * @see #doSelect for details of option locators
    * @param locator an <a href="#locators">element locator</a> identifying a multi-select box
    * @param optionLocator an option locator (a label by default) 
    */
   void fw_addSelection(String locator, String optionLocator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#check (java.lang.String); */
   void check(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#clearField (java.lang.String); */
   void clearField(String locator);


   /* This method will perform the right click action
    *
    * @param element - WebElement */
   void rightClick(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#click (java.lang.String); */
   void click(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#click (java.lang.String); */
   void click(String locator, int timeOut);


   /* This method uses the action to click on a webelement
    *
    * @param locator - the element the user want to click */
   void clickByAction(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#clickUsingJavascript (java.lang.String); */
   void clickUsingJavascript(String locator);


   /* This method will quitting the currently opened browser. */
   void quit();


   /* This method will Close the current window, quitting the browser if it's the last window currently open. */
   void closeSlowly();


   /* This method will Switches to any child windows and closes them and then switches back to the parent window. */
   void closeAllChildWindows();


   /* (non-Javadoc);
    *
    * @see WebBrowserController#delay (long); */
   void delay(long seconds);


   /* This method will switches to Alert context and dismisses if found */
   void dismissAlert();


   /* (non-Javadoc);
    *
    * @see com.twc.sit.atf.common.webapplications.IWebBrowserController#doubleClick(java.lang.String); */
   void doubleClick(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#isVisible (java.lang.String); */
   WebElement elementToBeClickable(final String locator);


   /* (non-Javadoc);
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#enterMultiLineText(java.lang.String,
    * java.util.List); */
   void enterMultiLineText(String textLocation, List<String> values);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#findElement (java.lang.String); */
   WebElement findElement(final String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#findElement (java.lang.String); */
   WebElement findElement(final String locator, int timeOut);


   /* (non-Javadoc)
    *
    * @see WebBrowserController#click (java.lang.String) */
   WebElement findClickableElement(final String locator);


   /* (non-Javadoc)
    *
    * @see WebBrowserController#click (java.lang.String) */
   WebElement findClickableElement(final String locator, int timeout);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#findElements (java.lang.String); */
   List<WebElement> findElements(String locator);


   /* (non-Javadoc);
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#findTextElements(java.lang.String); */
   List<String> findTextElements(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#fireJsEvent (java.lang.String, java.lang.String); */
   void fireJsEvent(String script);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#fireJsEvent (java.lang.String, java.lang.String); */
   void fireJsEvent(String locator, String event);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#fireJsEvent (org.openqa.selenium.WebElement , java.lang.String); */
   void fireJsEvent(WebElement locator, String event);


   /* KEG notes works in chrome for add user doesn't work for create bundle - drop down */
   void focus(String locator);


   /** Retrieves the message of a JavaScript alert generated during the previous action, or fail if there were
    * no alerts.
    *
    * <p>
    * Getting an alert has the same effect as manually clicking OK. If an alert is generated but you do not consume it
    * with getAlert, the next Selenium action will fail.
    * </p>
    * <p>
    * Under Selenium, JavaScript alerts will NOT pop up a visible alert dialog.
    * </p>
    * <p>
    * Selenium does NOT support JavaScript alerts that are generated in a page's onload() event handler. In this case a
    * visible dialog WILL be generated and Selenium will hang until someone manually clicks OK.
    * </p>
    *
    * @return The message of the most recent JavaScript alert */
   String getAlert();


   /** This method Uses an xpath locator with an iterator ( [%s] ) and returns all text or attributes found using the
    * locator
    *
    * @param xpathLocator locator that contains the '%s' replacement character
    *
    * @return a list of either text elements or attribute values found using the locator */
   List<String> getAll(String xpathLocator);


   /** This method Uses the xpath locator and return all text returned by the locator;
    *
    * @param xpathLocator String - locator that meets all the requirements
    *
    * @return List< String> a list of String value found using the locator */
   List<String> getAllText(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#getAttribute (java.lang.String, java.lang.String); */
   String getAttribute(String locator, String attribute);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#getBodyText (); */
   String getBodyText();



   /**
    * Method that gets the web browser information. 
    * @return browser information
    */
   String getBrowserVersion();


   /* (non-Javadoc);
    *
    * @see WebBrowserController# getBrowserName(); */
   String getBrowserName();


   /* (non-Javadoc);
    *
    * @see WebBrowserController# getBrowserName(); */
   String getBrowserInfo();


   /** Retrieves the message of a JavaScript confirmation dialog generated during the previous action.
    *
    * <p>
    * By default, the confirm function will return true, having the same effect as manually clicking OK. This can be
    * changed by prior execution of the chooseCancelOnNextConfirmation command.
    * </p>
    * <p>
    * If an confirmation is generated but you do not consume it with getConfirmation, the next Selenium action will
    * fail.
    * </p>
    * <p>
    * NOTE: under Selenium, JavaScript confirmations will NOT pop up a visible dialog.
    * </p>
    * <p>
    * NOTE: Selenium does NOT support JavaScript confirmations that are generated in a page's onload() event handler. In
    * this case a visible dialog WILL be generated and Selenium will hang until you manually click OK.
    * </p>
    *
    * @return the message of the most recent JavaScript confirmation dialog */
   String getConfirmation();


   /* (non-Javadoc);
    *
    * @see WebBrowserController#getPlatform (); */
   String getPlatform();


   /** Gets option label (visible text) for selected option in the specified select element.
    *
    * @param selectLocator an <a href="#locators">element locator</a> identifying a drop-down menu
    *
    * @return the selected option label in the specified select drop-down */
   String getSelectedLabel(String selectLocator);


   /** Gets all option labels (visible text) for selected options in the specified select or multi-select element.
    *
    * @param selectLocator an <a href="#locators">element locator</a> identifying a drop-down menu
    *
    * @return an array of all selected option labels in the specified select drop-down */
   String[] getSelectedLabels(String selectLocator);


   /** This method returns a list of options from a select box
    *
    * @param locator - select box
    *
    * @return List<WebElement> - options of the select box */
   List<WebElement> getSelectOptions(String selectLocator);



   /**
    * Method that takes a locator object and returns the table name of that object 
    * @param locator
    * @return table
    */
   String getTable(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#getText (java.lang.String); */
   String getText(String locator);


   /** Gets the title of the current page.
    *
    * @return the title of the current page 
    */
   String getTitle();


   /** Gets the (whitespace-trimmed) value of an input field (or anything else with a value parameter). For
    * checkbox/radio elements, the value will be "on" or "off" depending on whether the element is checked or
    * not.
    *
    * @param locator an <a href="#locators">element locator</a>
    *
    * @return the element value, or "on/off" for checkbox/radio elements 
    */
   String getValue(String locator);


   /** Gets the webDriver type.
    *
    * @return the webDriver 
    */
   WebDriver getWebDriver();


   /** Return the current url
    *
    * @return string with current url 
    */
   String getCurrentUrl();


   /** Returns the number of nodes that match the specified css/xpath, eg. "css=table" / "//table" would give the number
    * of tables.
    *
    * @param css/xpath the css/xpath expression to evaluate. do NOT wrap this expression in a 'count()' function; we
    * will do that for you.
    *
    * @return the integer value of nodes that match the specified locater 
    */
   int getElementsCount(String xpath);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#goBack(); */
   void goBack();


   /* (non-Javadoc);
    *
    * @see WebBrowserController#goToTop(); */
   void goToTop();


   /* (non-Javadoc);
    *
    * @see WebBrowserController#hideElement (java.lang.String); */
   void hideElement(String locator);


   /** This method returns true if check box is checked, otherwise false
    *
    * @param locator - check box
    *
    * @return boolean - true if checked, false otherwise 
    **/
   boolean isChecked(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController# isElementPresent(java.lang.String); */
   boolean isElementPresent(String locator);


   /* (non-Javadoc);
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#isEnabled(java.lang.String); */
   boolean isElementPresentWithWait(String locator);


   /* (non-Javadoc);
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#isEnabled(java.lang.String); */
   boolean isElementPresentWithWait(String locator, int timeout);


   /* Is the element currently enabled or not? This will generally return true for everything but disabled
    * input elements.
    *
    * @return True if the element is enabled, false otherwise. */
   boolean isEnabled(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#isTextPresent (java.lang.String); */
   boolean isTextPresent(String pattern);


   /* (non-Javadoc);
    *
    * @see WebBrowserController# isTextPresentElement(java.lang.String, java.lang.String); */
   boolean isTextPresentElement(String locator, String pattern);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#isVisible (java.lang.String); */
   boolean isVisible(final String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#jsClick (java.lang.String); */
   void jsClick(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#jsClick (java.lang.String); */
   void jsClick(WebElement locator);


   /* {@inheritDoc} <br/>
    * Similar to sendKeys */
   void keyPress(String locator, String key);


   /* This method fires a javascript jquery show() event
    *
    * @param locator - element the user wants to fire the show event against */
   void makeElementVisible(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#mouseOut (java.lang.String); */
   void mouseOut(String locator);


   /* This method uses the webDriver object to mouse Over By Action of desired element
    *
    * @param locator - element the user wants to mouse over */
   Actions mouseOverByActions(String locator);


   /* This method uses the webDriver object to move over the desired child element and click using action class
    *
    * @param locator */
   void moveToElementAndClick(String... locatores);


   /* This method uses the webDriver object to drag and drop the elements from source to destination
    *
    * @param locator */
   void dragAndDropAnElement(WebElement findLocator, WebElement destination);


   /* This method uses the webDriver object to drag and drop the elements from source to destination
    *
    * @param locator */
   void dragAndDropAnElement(String sourceElement, String destinationElement, int timeoutInSeconds);


   /* This method uses the webDriver object to drag and drop the elements from source to destination using javascript
    *
    * @param locator */
   void dragAndDropAnElementUsingJavascript(String sourceElement, String destinationElement, int timeoutInSeconds);


   /* This method uses the webDriver object to mouse Over By Action of desired element
    *
    * @param locator - webElement the user wants to mouse over */
   Actions mouseOverByActions(WebElement locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#mouseOver (java.lang.String); */
   void mouseOverByJavascript(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#mouseOver (org.openqa.selenium.WebElement ); */
   void mouseOverByJavascript(WebElement locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#click (java.lang.String); */
   void moveToAndClick(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#click (java.lang.String); */
   void moveToAndClick(WebElement locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#open( java.lang.String); */
   void open(String url);


   /* This method overrides the IE Browser certificate error */
   void overrideIECertificateError();


   /* Determines if the specified element is visible. An element can be rendered invisible by setting the CSS
    * "visibility" property to "hidden", or the "display" property to "none", either for the element itself or
    * one if its ancestors. This method will fail if the element is not present.
    *
    * @param locator an <a href="#locators">element locator</a>
    *
    * @param timeout int time value
    *
    * @return true if the specified element is visible, false otherwise */
   boolean isElementVisibleWithWait(String locator, int timeout);


   /* This method overrides the Edge Browser certificate error */
   void overrideEdgeCertificateError();


   /* (non-Javadoc);
    *
    * @see WebBrowserController#reload(); */
   void reload();


   /* (non-Javadoc);
    *
    * @see WebBrowserController# scrollHorizontal(int, int); */
   void scrollHorizontal(int xlocation, int scrollFrequency);


   /* (non-Javadoc);
    *
    * @see WebBrowserController# scrollVertical(int, int); */
   void scrollVertical(int ylocation, int scrollFrequency);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#select (java.lang.String, java.lang.String); */
   void select(String selectLocator, String optionLocator);


   /* (non-Javadoc);
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#isSelectOptionDisable(java.lang.String,
    * java.lang.String); */
   boolean isSelectOptionEnable(String selectLocator, String optionLocator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#select (java.lang.String, java.lang.String); */
   void selectAndTab(String selectLocator, String optionLocator);


   /* This method returns moves the web driver to control a specific frame
    *
    * @param locator - element the user wants to control */
   void selectFrame(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#selectWindow (java.lang.String); */
   void selectWindow(String windowID);


   /* This method will returns the window handle */
   String getWindowHandle();


   /* Sets the value of an input field, as though you typed it in; like type() but without clearing the input
    * or doing a 'click' (needed for file uploads, since selenium won't work with native OS file browser
    * dialog)
    *
    * <p>
    * Can also be used to set the value of combo boxes, check boxes, etc. In these cases, value should be the value of
    * the option selected, not the visible text.
    * </p>
    *
    * @param locator an <a href="#locators">element locator</a>
    *
    * @param value the value to type */
   void sendKeys(String locator, Keys k);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#sendKeys( java.lang.String, java.lang.String); */
   void sendKeys(String locator, String value);


   /* (non-Javadoc);
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#sendOpenFile(java.lang.String,
    * java.lang.String); */
   void sendOpenFile(String locator, String filePath);


   /* (non-Javadoc);
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#sendSaveFile(java.lang.String,
    * java.lang.String); */
   void sendSaveFile(String locator, String filePath);


   /* This method checks or uncheck a checkbox depending on its state
    *
    * @param String checkBoxLocator - check box to complete the action against
    *
    * @param boolean isChecked - check box state */
   void setCheckBoxState(String checkBoxLocator, boolean isChecked);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#submit (java.lang.String); */
   void submit(String locator);


   /* This method switches the web driver to control a specified frame.
    *
    * @param int index - index of the frame, where 0 is the parent window. */
   TargetLocator switchTo();


   /* This method switches the web driver to control a specified frame.
    *
    * @param WebElement locator - locator of the frame, where frame is located in the parent window. */
   void switchToFrame(WebElement locator);


   /* This method switches the web driver to control a specified frame.
    *
    * @param int index - index of the frame, where 0 is the parent window. */
   void switchToFrame(int index);


   /* This method switches the web driver to control a specified frame.
    *
    * @param String locator - locator of the frame, where frame is located in the parent window. */
   void switchToFrame(String frameLocator);


   /* This method will Switches the webDriver to a newly opened web browser window. */
   void switchToNewlyOpenedWindow();


   /* This method will Switches the webDriver back to the parent window. */
   void switchToParentWindow();


   /* (non-Javadoc);
    *
    * @see WebBrowserController#type( java.lang.String, java.lang.String); */
   void type(String locator, String value);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#typeWithoutClearing( java.lang.String, java.lang.String); */
   void typeWithoutClearing(String locator, String value);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#typeUsingJavascript( java.lang.String, java.lang.String); */
   void typeUsingJavascript(String locator, String value);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#typeUsingRobot( java.lang.String, java.lang.String); */
   void typeUsingRobot(String locator, String value);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#uncheck (java.lang.String); */
   void uncheck(String locator);


   /* (non-Javadoc);
    *
    * @see WebBrowserController#waitForAlert (); */
   void waitForAlert();


   /* @ param timeout in milliseconds
    *
    * @see WebBrowserController#waitForAlert (int); */
   void waitForAlert(int timeOut);


   /* {@inheritDoc} Wait time is set to 15 seconds */
   void waitForElementFound(String guiElementDescription);


   /* (non-Javadoc);
    *
    * @see WebBrowserController# waitForElementFound(java.lang.String, int); */
   void waitForElementFound(String guiElementDescription, int timeoutInSeconds);


   /* (non-Javadoc);
    *
    * @see WebBrowserController# waitForElementHidden(java.lang.String); */
   void waitForElementHidden(String guiElementDescription);


   /* (non-Javadoc);
    *
    * @see WebBrowserController# waitForElementHidden(java.lang.String, int); */
   void waitForElementHidden(String guiElementDescription, int timeoutInSeconds);


   /* (non-Javadoc);
    *
    * @see WebBrowserController# waitForElementNotFound(java.lang.String ); */
   void waitForElementNotFound(String guiElementDescription);


   /* (non-Javadoc);
    *
    * @see WebBrowserController# waitForElementNotFound(java.lang.String , int); */
   void waitForElementNotFound(String guiElementDescription, int timeoutInSeconds);


   /* (non-Javadoc);
    *
    * @see WebBrowserController# waitForElementVisible(java.lang.String); */
   void waitForElementVisible(String guiElementDescription);


   /* (non-Javadoc);
    *
    * @see WebBrowserController# waitForElementVisible(java.lang.String, int); */
   void waitForElementVisible(String guiElementDescription, int timeoutInSeconds);


   /* This method Will wait for 10 seconds for the gui element to be displayed on screen.
    *
    * @param guiElementDescription ( xpath, id ,etc...)
    *
    * @throws ElementNotVisibleException if timeout has been reached and GUI Object still not found */
   void waitForElementVisible(WebElement locator)
      throws ElementNotVisibleException;


   /* This method Will wait for 10 seconds for the gui element to be displayed on screen.
    *
    * @param guiElementDescription ( xpath, id ,etc...)
    *
    * @throws ElementNotVisibleException if timeout has been reached and GUI Object still not found */
   void waitForElementVisible(WebElement locator, int timeoutInSeconds)
      throws ElementNotVisibleException;


   /* (non-Javadoc);
    *
    * @see WebBrowserController#waitForText (); */
   boolean waitForText(String text);


   /* This method Waits x seconds for the input text to be found on a GUI page
    *
    * @param text text to search for on page
    *
    * @return True if text is found; false otherwise */
   boolean waitForText(String text, int timeOut);


   /* This method maximizes browser window */
   void windowMaximize();


   /* This method will return the no of windows opened in the current instance. */
   int getNumberOfOpenWindows();


   /* This method will return the presence of the alert in the current instance. */
   boolean isAlertPresent();


   void waitForElementClickable(String guiElementDescription)
      throws ElementNotVisibleException;


   void waitForElementClickable(String guiElementDescription, int timeoutInSeconds)
      throws ElementNotVisibleException;


   /* This method will not execute script when we navigate to next page and the page isn't loaded completely */
   boolean waitForPageLoad();


   /* This method Waits x seconds and execute script when we navigate to next page and the page isn't loaded completely */
   void waitForPageLoad(int timeout);


   /* This method will return the first frame of the current instance */
   String getMainFrame();


   /* This method will Switches the webDriver to the default content. */
   void switchToDefaultContent();


   /* Selects a random element in the drop down list
    *
    * @param selectElementLocator - identified drop down list */
   void selectRandomItemFromDropDown(String selectElementLocator);


   /* Selects an item from a drop down list that contains the parameter stringLike
    *
    * @param guiElementLocator - identifies the drop down list
    *
    * @param stringLike - string to match in the elements of the drop down list
    *
    * @return the text name of the "matching" item selected in the drop down; empty string if no match found */
   String selectLikeItemFromDropDown(String guiElementLocator, String stringLike);


   /* Selects the correct item from the given select item in the GUI. If the value is not set (null or "");
    * this method does nothing, else it will try to select the item.<br>
    * <br>
    * If the value does not contain 'label=' it is added automatically. */
   void selectItemFromDropDown(String selectLocator, String value);


   /* If drop down is a required field, will select from the list, depending on content of parameter item if
    * item is empty, selects random element from the drop down list if item has content, selects it from the
    * drop down list if drop down isn't required field, does nothing
    *
    * @param selectElementLocator - identifies the drop down list
    *
    * @param item - element to select on the drop down list
    *
    * @param isRequired - is this a required field */
   void selectItemFromDropDown(String selectElementLocator, String item, boolean isRequired);


   /* Selects an item from a drop down list that starts with the parameter stringStartsWith
    *
    * @param guiElementLocator - identifies the drop down list
    *
    * @param stringStartsWith - string to match starts with in elements of the drop down list */
   void selectItemStartWithFromDropDown(String guiElementLocator, String stringStartsWith);


   /* This method tries to retrieve the system platform and return if it fails, it logs the exception and returns null
    *
    * @return the system platform, or null */
   String discoverPlatform();


   /** Throws a SkipException with your string message added; your test method will stop executing at this point
    *
    * @param message appended to the SkipException
    *
    * @throws SkipException with your message appended
    */
   void skip(String message);


   /** 
    * This method will Close the current window, quitting the browser if it's the last window currently open. 
    * 
    */
   @Override
   void close()
      throws Exception;


   /**
    * This method builds and returns a webDriver instance by which one can use to control the automation of a specified
    * web browser and platform or Operating System.
    *
    * @param url - main test url
    * @param browserType - type of browser to automate
    * @param platform - operating system or platform type
    * @return - Instance of WebBrowser
    */
   static WebFunctions buildWebDriver(String url, String browserName, Method method)
   {
      return CommonFunctions.buildWebDriver(url, browserName, method);
   }


   /**
    * This method build and return a webDriver instance by which one can use to control the automation of a specified
    * web browser and platform or Operating System.
    *
    * @param url - main test url
    * @param browserType - type of browser to automate
    * @param platform - operating system or platform type
    * @param seleniumGridUrl - selenium Grid Url
    * @return - Instance of WebBrowser
    */
   static WebFunctions buildRemoteWebDriver(String url, String browserName, Method method)
   {
      return CommonFunctions.buildRemoteWebDriver(url, browserName, method);
   }


   /**
    * Method that scrolls a browser window to the bottom.
    * 
    */
   void scrollToEnd();


   /**
    * Method that scrolls a browser window to the top.
    */
   void scrollTotop();
}
