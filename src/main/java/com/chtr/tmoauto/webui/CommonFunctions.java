/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Nagaraju.Meka
* File:    CommonFunctions.java
* Created: Dec 06, 2016
*
* Description: Class to help normalize startup and usage of
* retrieving the objects from Jenkins/Properties file.
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.chtr.tmoauto.webui;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;

class CommonFunctions implements WebFunctions
{
   protected static final Logger log = LoggerFactory.getLogger(WebFunctions.class);
   private final WebDriver webDriver;
   private static final String CSS_PREFIX = "css=", VALUE = "value";
   private static String sysEnv;
   private static final int FIVE = 5, TEN = 10, FIFTEEN = 15;
   private static final int DEFAULT_KERNEL_RADIUS = 2, BYTE_MASK = 0x000000FF;
   private static final float PIXEL_ERROR_LIMIT = 1000;
   private static final double RED_RATIO = 0.2126f, BLUE_RATIO = 0.0722f, GREEN_RATIO = 0.7152f, PIXEL_ERROR_THRESHOLD_RATIO = 0.2;
   private static final double PIXEL_THRESHOLD_CUBE_LUM_DIFF = PIXEL_ERROR_THRESHOLD_RATIO * PIXEL_ERROR_LIMIT;


   public CommonFunctions(WebDriver webDriver)
   {
      this.webDriver = webDriver;
   }


   /**
    * This method build and return a webDriver instance by which one can use to control the automation of a specified
    * web browser and platform or Operating System.
    *
    * @param url - main test url
    * @param browserType - type of browser to automate
    * @param platform - operating system or platform type
    * @return
    * @return - Instance of WebBrowser
    */
   @SuppressWarnings("resource")
   public static CommonFunctions buildWebDriver(String url, String browserName, Method method)
   {
      Annotation[] a = method.getAnnotations();
      String description = null;
      try
      {
         description = a[0].toString().split("description=")[1].split(", retryAnalyzer=")[0];
      } catch (Exception e)
      {}
      System.gc();
      log.info("");
      log.info("=========================================================");
      log.info("Test: {}", method.getName());
      log.info("Test Description: {}", description);
      log.info("=========================================================");
      WebDriver wd = buildWebDriver(browserName);
      CommonFunctions wDriver = new CommonFunctions(wd);
      log.info("Starting WebDriver: { Browser: {} } { Version: {} } { Platform: {} } ", wDriver.getBrowserName().trim(), wDriver.getBrowserVersion().trim(), wDriver.discoverPlatform().toString());
      log.info("Navigating to: {}", url);
      wd.get(url);
      return new CommonFunctions(wd);
   }


   /**
    * This method build and return a webDriver instance by which one can use to control the automation of a specified
    * web browser and platform or Operating System.
    *
    * @param url - main test url
    * @param browserType - type of browser to automate
    * @param platform - operating system or platform type
    * @param seleniumGridUrl - selenium Grid Url
    * @return
    * @return - Instance of WebBrowser
    */
   @SuppressWarnings("resource")
   public static CommonFunctions buildRemoteWebDriver(String url, String browserName, Method method)
   {
      Annotation[] a = method.getAnnotations();
      String description = null;
      try
      {
         description = a[0].toString().split("description=")[1].split(", retryAnalyzer=")[0];
      } catch (Exception e)
      {}
      System.gc();
      log.info("");
      log.info("=========================================================");
      log.info("Test: {}", method.getName());
      log.info("Test Description: {}", description);
      log.info("=========================================================");
      WebDriver wd = buildRemoteWebDriver(browserName);
      CommonFunctions wDriver = new CommonFunctions(wd);
      log.info("Starting Remote WebDriver: { Browser: {} } { Version: {} } { Platform: {} } ", wDriver.getBrowserName().trim(), wDriver.getBrowserVersion().trim(), wDriver.discoverPlatform().toString());
      log.info("Navigating to: {}", url);
      wd.get(url);
      return new CommonFunctions(wd);
   }


   /**
    * Method that targets an open alert and selects OK to dismiss it.
    */
   @Override
   public void fw_acceptAlert()
   {
      try
      {
         log.info("Accepting Alert ...");
         Alert a = webDriver.switchTo().alert();
         String text = a.getText();
         a.accept();
         log.info("Alert Text: " + text);
      } catch (Exception e)
      {
         log.info(("We are hiding an exception here: " + e.getMessage()));
      }
   }


   @Override
   public void fw_addSelection(String locator, String optionLocator)
   {
      Select sel = new Select(findElement(locator));
      if (sel.isMultiple())
      {
         sel.selectByVisibleText(optionLocator);
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#check (java.lang.String) */
   @Override
   public void check(String locator)
   {
      WebElement e = findElement(locator);
      if ( !e.isSelected())
      {
         e.click();
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#clearField (java.lang.String) */
   @Override
   public void clearField(String locator)
   {
      WebElement j2c = findElement(locator);
      j2c.clear();
   }


   /* This method will perform the right click action
    *
    * @param element - WebElement */
   @Override
   public void rightClick(String locator)
   {
      WebElement e1 = elementToBeClickable(locator);
      Actions action = new Actions(webDriver);
      action.contextClick(e1).build().perform();
      delay(FIVE);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#click (java.lang.String) */
   @Override
   public void click(String locator)
   {
      click(locator, TEN);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#click (java.lang.String) */
   @Override
   public void click(String locator, int timeOut)
   {
      log.debug("Clicking: " + locator);
      findClickableElement(locator, timeOut).click();
   }


   @Override
   public void clickByAction(String locator)
   {
      Actions action = new Actions(webDriver);
      action.click(findClickableElement(locator));
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#clickUsingJavascript (java.lang.String) */
   @Override
   public void clickUsingJavascript(String locator)
   {
      WebElement we = findClickableElement(locator);
      String event = "arguments[0].click()";
      JavascriptExecutor executor = (JavascriptExecutor)webDriver;
      try
      {
         we.click();
      } catch (Exception e)
      {
         executor.executeScript(event, we);
      }
   }


   @Override
   public void quit()
   {
      if (webDriver != null)
      {
         System.gc();
         webDriver.quit();
      }
   }


   @Override
   public void closeSlowly()
   {
      if (webDriver != null)
      {
         webDriver.close();
         delay(5);
      }
      if (webDriver != null && !webDriver.toString().contains("null"))
      {
         log.warn("Web driver is still active or still has windows open...calling quit");
         quit();
      }
   }


   @Override
   public void closeAllChildWindows()
   {
      Set<String> winHandles = webDriver.getWindowHandles();
      String handle = null;
      for (int i = winHandles.size(); i > 1; i--)
      {
         handle = (String)winHandles.toArray()[winHandles.size() - 1];
         webDriver.switchTo().window(handle).close();
      }
      switchToParentWindow();
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#delay (long) */
   @Override
   public void delay(long seconds)
   {
      try
      {
         TimeUnit.SECONDS.sleep(seconds);
      } catch (InterruptedException e)
      {}
   }


   @Override
   public void dismissAlert()
   {
      try
      {
         Alert a = webDriver.switchTo().alert();
         String text = a.getText();
         a.dismiss();
         log.info("Dismissing alert: " + text);
      } catch (Exception e)
      {
         log.info(("We are hiding an exception here: " + e.getMessage()));
      }
   }


   /* (non-Javadoc)
    *
    * @see com.twc.sit.atf.common.webapplications.IWebBrowserController#doubleClick(java.lang.String) */
   @Override
   public void doubleClick(String locator)
   {
      WebElement element = elementToBeClickable(locator);
      Actions action = new Actions(webDriver);
      try
      {
         action.doubleClick(element);
         action.perform();
      } catch (Exception e)
      {
         log.warn("Could not double click : " + e);
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#isVisible (java.lang.String) */
   @Override
   public WebElement elementToBeClickable(final String locator)
   {
      WebDriverWait wait = new WebDriverWait(webDriver, FIVE);
      return wait.until(ExpectedConditions.elementToBeClickable(getSelector(locator)));
   }


   /* (non-Javadoc)
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#enterMultiLineText(java.lang.String,
    * java.util.List) */
   @Override
   public void enterMultiLineText(String textLocation, List<String> values)
   {
      StringBuilder dataEnter = new StringBuilder();
      for (String value : values)
      {
         dataEnter.append(value);
         if (values.indexOf(value) < values.size() - 1)
         {
            dataEnter.append(System.getProperty("line.separator"));
         }
      }
      type(textLocation, dataEnter.toString());
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#findElement (java.lang.String) */
   @Override
   public WebElement findElement(final String locator)
   {
      return findElement(locator, FIVE);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#findElement (java.lang.String) */
   @Override
   public WebElement findElement(final String locator, int timeOut)
   {
      log.debug("debug: Looking for element: " + locator);
      WebElement e = (new WebDriverWait(webDriver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(getSelector(locator)));
      return e;
   }


   @Override
   public WebElement findClickableElement(final String locator)
   {
      return findClickableElement(locator, TEN);
   }


   @Override
   public WebElement findClickableElement(final String locator, int timeout)
   {
      log.debug("debug: Looking for element: {}", locator);
      WebElement e = (new WebDriverWait(webDriver, timeout)).until(ExpectedConditions.elementToBeClickable(getSelector(locator)));
      return e;
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#findElements (java.lang.String) */
   @Override
   public List<WebElement> findElements(String locator)
   {
      return webDriver.findElements(getSelector(locator));
   }


   /* (non-Javadoc)
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#findTextElements(java.lang.String) */
   @Override
   public List<String> findTextElements(String locator)
   {
      List<String> toReturn = new ArrayList<String>();
      for (WebElement w : webDriver.findElements(getSelector(locator)))
      {
         toReturn.add(w.getText());
      }
      return toReturn;
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#fireJsEvent (java.lang.String, java.lang.String) */
   @Override
   public void fireJsEvent(String script)
   {
      ((JavascriptExecutor)webDriver).executeScript(script);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#fireJsEvent (java.lang.String, java.lang.String) */
   @Override
   public void fireJsEvent(String locator, String event)
   {
      String newLocator = locator.replace(CSS_PREFIX, "");
      String script = "$('" + newLocator + "')." + event + "();";
      log.debug(script);
      ((JavascriptExecutor)webDriver).executeScript(script);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#fireJsEvent (org.openqa.selenium.WebElement , java.lang.String) */
   @Override
   public void fireJsEvent(WebElement locator, String event)
   {
      String exec = event;
      if (getBrowserName().toLowerCase().contains("internet"))
      {
         exec = "on" + event;
      }
      ((JavascriptExecutor)webDriver).executeScript(exec, locator);
   }


   /**
    * KEG notes works in chrome for add user
    * doesn't work for create bundle - drop down
    */
   @Override
   public void focus(String locator)
   {
      findElement(locator).sendKeys(Keys.TAB);
   }


   @Override
   public String getAlert()
   {
      return webDriver.switchTo().alert().getText();
   }


   @Override
   public List<String> getAll(String xpathLocator)
   {
      ArrayList<String> ret = new ArrayList<String>();
      String attrName = "name";
      int row = 1;
      while (true)
      {
         try
         {
            String mod = String.format(xpathLocator, row++);
            try
            {
               String attr = webDriver.findElement(By.xpath(mod)).getAttribute(attrName);
               ret.add(attr);
            } catch (Exception e)
            {
               String text = getText(mod);
               ret.add(text);
            }
         } catch (Exception e)
         {
            log.debug("" + e);
            break;
         }
      }
      return ret;
   }


   @Override
   public List<String> getAllText(String locator)
   {
      List<String> textList = new ArrayList<String>();
      List<WebElement> elements = webDriver.findElements(getSelector(locator));
      for (WebElement element : elements)
      {
         try
         {
            textList.add(element.getText());
         } catch (Exception e)
         {}
      }
      return textList;
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#getAttribute (java.lang.String, java.lang.String) */
   @Override
   public String getAttribute(String locator, String attribute)
   {
      return findElement(locator).getAttribute(attribute);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#getBodyText () */
   @Override
   public String getBodyText()
   {
      return webDriver.findElement(By.tagName("body")).getText();
   }


   @Override
   public String getBrowserVersion()
   {
      Capabilities capabilities = ((RemoteWebDriver)webDriver).getCapabilities();
      return WordUtils.capitalize(capabilities.getVersion());
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# getBrowserName() */
   @Override
   public String getBrowserName()
   {
      Capabilities capabilities = ((RemoteWebDriver)webDriver).getCapabilities();
      return WordUtils.capitalize(capabilities.getBrowserName());
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# getBrowserName() */
   @Override
   public String getBrowserInfo()
   {
      String browserInfo = null;
      browserInfo = (String)((JavascriptExecutor)webDriver).executeScript("return navigator.platform");
      browserInfo += ", ";
      browserInfo += (String)((JavascriptExecutor)webDriver).executeScript("return navigator.userAgent");
      return browserInfo;
   }


   @Override
   public String getConfirmation()
   {
      return webDriver.switchTo().alert().getText();
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#getPlatform () */
   @Override
   public String getPlatform()
   {
      Capabilities capabilities = ((RemoteWebDriver)webDriver).getCapabilities();
      return capabilities.getPlatform().toString();
   }


   @Override
   public String getSelectedLabel(String selectLocator)
   {
      return new Select(findElement(selectLocator)).getFirstSelectedOption().getText();
   }


   @Override
   public String[] getSelectedLabels(String selectLocator)
   {
      List<WebElement> options = new Select(findElement(selectLocator)).getAllSelectedOptions();
      String[] retArr = new String[options.size()];
      for (int i = 0; i < options.size(); i++)
      {
         retArr[i] = options.get(i).getText();
      }
      return retArr;
   }


   @Override
   public List<WebElement> getSelectOptions(String selectLocator)
   {
      Select select = new Select(findElement(selectLocator, TEN));
      return select.getOptions();
   }


   /**
    * 
    * @param locator
    * @return
    */
   public By getSelector(String locator)
   {
      String[] prefix = locator.split("=", 2);
      if (prefix[0].equals("css"))
      {
         return By.cssSelector(prefix[1]);
      }
      else if (prefix[0].equals("id"))
      {
         return By.id(prefix[1]);
      }
      else if (prefix[0].equals("class"))
      {
         return By.className(prefix[1]);
      }
      else if (prefix[0].equals("xpath"))
      {
         return By.xpath(prefix[1]);
      }
      else if (prefix[0].equals("link"))
      {
         return By.linkText(prefix[1]);
      }
      else if (prefix[0].equals("name"))
      {
         return By.name(prefix[1]);
      }
      else if (locator.startsWith("//"))
      {
         return By.xpath(locator);
      }
      else if (locator.startsWith(".//"))
      {
         return By.xpath(locator);
      }
      else
      {
         return By.id(locator);
      }
   }


   /**
    * FLAGGED FOR DELETION
    */
   @Override
   public String getTable(String locator)
   {
      log.debug("Getting table ...");
      String[] tableLocation = locator.split("\\.", 3);
      if (tableLocation.length != 3)
      {
         throw new RuntimeException("Incorrect table locator used");
      }
      WebElement table = findElement(tableLocation[0]);
      List<WebElement> allRows = table.findElements(By.tagName("tr"));
      List<WebElement> cells = allRows.get(Integer.parseInt(tableLocation[1])).findElements(By.tagName("td"));
      return cells.get(Integer.parseInt(tableLocation[2])).getText();
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#getText (java.lang.String) */
   @Override
   public String getText(String locator)
   {
      try
      {
         return findElement(locator).getText();
      } catch (StaleElementReferenceException e)
      {
         return findElement(locator).getText();
      }
   }


   @Override
   public String getTitle()
   {
      return webDriver.getTitle();
   }


   @Override
   public String getValue(String locator)
   {
      return findElement(locator).getAttribute(VALUE);
   }


   @Override
   public WebDriver getWebDriver()
   {
      return webDriver;
   }


   /**
    * Return the current url
    *
    * @return string with current url
    */
   @Override
   public String getCurrentUrl()
   {
      return webDriver.getCurrentUrl();
   }


   /**
    * FLAGGED
    */
   @Override
   public int getElementsCount(String locator)
   {
      Number num = webDriver.findElements(getSelector(locator)).size();
      return num.intValue();
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#goBack() */
   @Override
   public void goBack()
   {
      webDriver.navigate().back();
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#goToTop() */
   @Override
   public void goToTop()
   {
      scrollVertical(1, TEN);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#hideElement (java.lang.String) */
   @Override
   public void hideElement(String locator)
   {
      String newlocator = locator.replace(CSS_PREFIX, "");
      String script = "$('" + newlocator + "').style.visibility = 'hidden'";
      ((JavascriptExecutor)webDriver).executeScript(script);
   }


   @Override
   public boolean isChecked(String locator)
   {
      WebElement e = findElement(locator, 1);
      return e.isSelected();
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# isElementPresent(java.lang.String) */
   @Override
   public boolean isElementPresent(String locator)
   {
      return (findElements(locator).size() > 0);
   }


   /* (non-Javadoc)
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#isEnabled(java.lang.String) */
   @Override
   public boolean isElementPresentWithWait(String locator)
   {
      return isElementPresentWithWait(locator, TEN);
   }


   /* (non-Javadoc)
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#isEnabled(java.lang.String) */
   @Override
   public boolean isElementPresentWithWait(String locator, int timeout)
   {
      for (int i = 0; i < timeout; i++)
      {
         if (isElementPresent(locator))
         {
            return true;
         }
         delay(1);
      }
      return false;
   }


   @Override
   public boolean isEnabled(String locator)
   {
      return findElement(locator, 1).isEnabled();
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#isTextPresent (java.lang.String) */
   @Override
   public boolean isTextPresent(String pattern)
   {
      String bodyText = getBodyText();
      return bodyText.contains(pattern);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# isTextPresentElement(java.lang.String, java.lang.String) */
   @Override
   public boolean isTextPresentElement(String locator, String pattern)
   {
      String s = findElement(locator).getText();
      return s.contains(pattern);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#isVisible (java.lang.String) */
   @Override
   public boolean isVisible(final String locator)
   {
      try
      {
         (new WebDriverWait(webDriver, 1)).until(ExpectedConditions.visibilityOfElementLocated(getSelector(locator)));
         return true;
      } catch (Exception e1)
      {
         return false;
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#jsClick (java.lang.String) */
   @Override
   public void jsClick(String locator)
   {
      String newLocator = locator.replace(CSS_PREFIX, "");
      ((JavascriptExecutor)webDriver).executeScript("$('" + newLocator + "').click();");
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#jsClick (java.lang.String) */
   @Override
   public void jsClick(WebElement locator)
   {
      ((JavascriptExecutor)webDriver).executeScript("onclick", locator);
   }


   /**
    * {@inheritDoc} <br/>
    * Similar to sendKeys
    */
   @Override
   public void keyPress(String locator, String key)
   {
      findElement(locator).sendKeys(key);
   }


   @Override
   public void makeElementVisible(String locator)
   {
      WebElement elem = findElement(locator);
      String js = "arguments[0].style.height='1'; arguments[0].style.visibility='visible'; arguments[0].style.display='block';";
      ((JavascriptExecutor)webDriver).executeScript(js, elem);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#mouseOut (java.lang.String) */
   @Override
   public void mouseOut(String locator)
   {
      String newLocator = locator.replace(CSS_PREFIX, "");
      ((JavascriptExecutor)webDriver).executeScript("$('" + newLocator + "').mouseout();");
   }


   @Override
   public Actions mouseOverByActions(String locator)
   {
      Actions action = new Actions(webDriver);
      action.moveToElement(findElement(locator));
      return action;
   }


   /**
    * This method uses the webDriver object to move over the desired child element and click using action class
    *
    * @param locator
    */
   @Override
   public void moveToElementAndClick(String... locatores)
   {
      Actions action = new Actions(webDriver);
      String locator = null, bLocator = null;
      for (String b : locatores)
      {
         locator = b;
         delay(3);
         if ( !isVisible(locator))
         {
            action.moveToElement(findElement(bLocator)).build().perform();
            delay(3);
         }
         action.moveToElement(findElement(locator, 20)).build().perform();
         bLocator = locator;
      }
      log.info("Moving to and Clicking: " + locator);
      try
      {
         action.moveToElement(findElement(locator)).click().build().perform();
      } catch (Exception e)
      {
         click(locator);
      }
   }


   @Override
   public void dragAndDropAnElement(WebElement findLocator, WebElement destination)
   {
      new Actions(webDriver);
      Actions dragdrop = new Actions(webDriver);
      try
      {
         dragdrop.clickAndHold(findLocator).moveToElement(destination).release(destination).build().perform();
      } catch (StaleElementReferenceException se)
      {
         log.error("Stale element:", se);
      } catch (NoSuchElementException ne)
      {
         log.error("no such element: source: {}, destination:{}", findLocator, destination);
         log.error("message: {}", ne);
      } catch (Exception e)
      {
         log.error("unexpected error attempting to drag and drop", e);
      }
   }


   /**
    * This method uses the webDriver object to drag and drop the elements from source to destination
    *
    * @param locator
    */
   @Override
   public void dragAndDropAnElement(String sourceElement, String destinationElement, int timeoutInSeconds)
   {
      Actions builder = new Actions(getWebDriver());
      WebElement sElement = findElement(sourceElement, timeoutInSeconds);
      WebElement dElement = findElement(destinationElement, timeoutInSeconds);
      builder.clickAndHold(sElement).moveToElement(dElement).release(dElement).build().perform();
   }


   /**
    * This method uses the webDriver object to drag and drop the elements from source to destination using javascript
    *
    * @param locator
    */
   @Override
   public void dragAndDropAnElementUsingJavascript(String sourceElement, String destinationElement, int timeoutInSeconds)
   {
      Actions builder = new Actions(getWebDriver());
      WebElement src = findElement(sourceElement, timeoutInSeconds);
      WebElement des = findElement(destinationElement, timeoutInSeconds);
      String xto = Integer.toString(src.getLocation().x);
      String yto = Integer.toString(des.getLocation().y);
      JavascriptExecutor executor = (JavascriptExecutor)webDriver;
      String event = "function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; " + "simulate(arguments[0],\"mousedown\",0,0); simulate(arguments[0],\"mousemove\",arguments[1],arguments[2]); simulate(arguments[0],\"mouseup\",arguments[1],arguments[2]); ";
      try
      {
         src.click();
         des.click();
         builder.dragAndDrop(src, des);
      } catch (Exception e)
      {
         executor.executeScript(event, src, xto, yto);
      }
   }


   @Override
   public Actions mouseOverByActions(WebElement locator)
   {
      Actions action = new Actions(webDriver);
      action.moveToElement(locator);
      return action;
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#mouseOver (java.lang.String) */
   @Override
   public void mouseOverByJavascript(String locator)
   {
      String newLocator = locator.replace(CSS_PREFIX, "");
      ((JavascriptExecutor)webDriver).executeScript("$('" + newLocator + "').mouseover();");
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#mouseOver (org.openqa.selenium.WebElement ) */
   @Override
   public void mouseOverByJavascript(WebElement locator)
   {
      ((JavascriptExecutor)webDriver).executeScript("mouseover()", locator);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#click (java.lang.String) */
   @Override
   public void moveToAndClick(String locator)
   {
      log.debug("Moving to and Clicking: " + locator);
      Actions action = new Actions(webDriver);
      action.moveToElement(findElement(locator)).click().build().perform();
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#click (java.lang.String) */
   @Override
   public void moveToAndClick(WebElement locator)
   {
      log.debug("Moving to and Clicking: " + locator);
      Actions action = new Actions(webDriver);
      action.moveToElement(locator).click().build().perform();
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#open( java.lang.String) */
   @Override
   public void open(String url)
   {
      webDriver.get(url);
   }


   /**
    * This method overrides the IE Browser certificate error
    */
   @Override
   public void overrideIECertificateError()
   {
      String CONTINUE = "css=body.securityError";
      if (isElementVisibleWithWait(CONTINUE, 10))
      {
         try
         {
            log.info("Security Warning Present ...");
            webDriver.get("javascript:document.getElementById('overridelink').click()");
            log.info("Skipping Certificate Warning !!! ...");
         } catch (WebDriverException e)
         {
            webDriver.navigate().to("javascript:document.getElementById('overridelink').click()");
            log.info("Skipping Certificate Warning !!! ...");
         }
      }
   }


   /**
    * Determines if the specified element is visible. An element can be rendered invisible by setting the CSS
    * "visibility" property to "hidden", or the "display" property to "none", either for the element itself or
    * one if its ancestors. This method will fail if the element is not present.
    *
    * @param locator an <a href="#locators">element locator</a>
    * @param timeout int time value
    * @return true if the specified element is visible, false otherwise
    */
   @Override
   public boolean isElementVisibleWithWait(String locator, int timeout)
   {
      try
      {
         (new WebDriverWait(webDriver, timeout)).until(ExpectedConditions.visibilityOfElementLocated(getSelector(locator)));
         return true;
      } catch (Exception e1)
      {
         return false;
      }
   }


   /**
    * This method overrides the Edge Browser certificate error
    */
   @Override
   public void overrideEdgeCertificateError()
   {
      String CONTINUE = "css=id#invalidcert_continue";
      if (isElementVisibleWithWait(CONTINUE, 10))
      {
         try
         {
            log.info("Security Warning Present ...");
            click(CONTINUE);
            log.info("Skipping Certificate Warning !!! ...");
         } catch (Exception e)
         {
            log.info("Certificate Warning not Present");
         }
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#reload() */
   @Override
   public void reload()
   {
      webDriver.navigate().refresh();
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# scrollHorizontal(int, int) */
   @Override
   public void scrollHorizontal(int xlocation, int scrollFrequency)
   {
      String jscript = "window.scrollTo(" + xlocation + ",0);";
      for (int i = 0; i < xlocation; i += scrollFrequency)
      {
         ((JavascriptExecutor)webDriver).executeScript(jscript);
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# scrollVertical(int, int) */
   @Override
   public void scrollVertical(int ylocation, int scrollFrequency)
   {
      String jscript = "window.scrollTo(0," + ylocation + ");";
      for (int i = 0; i < ylocation; i += scrollFrequency)
      {
         ((JavascriptExecutor)webDriver).executeScript(jscript);
      }
   }


   @Override
   public void scrollToEnd()
   {
      JavascriptExecutor js = (JavascriptExecutor)webDriver;
      js.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));", new Object[0]);
   }


   @Override
   public void scrollTotop()
   {
      JavascriptExecutor js = (JavascriptExecutor)webDriver;
      js.executeScript("window.scrollTo(0,Math.min(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));", new Object[0]);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#select (java.lang.String, java.lang.String) */
   @Override
   public void select(String selectLocator, String optionLocator)
   {
      Select select = new Select(findElement(selectLocator));
      List<WebElement> options = select.getOptions();
      log.debug("options found " + options.size() + " elements");
      String[] locator = optionLocator.split("=");
      log.debug("Locator has " + locator.length + " elements");
      String find;
      if (locator.length > 1)
      {
         find = locator[1];
      }
      else
      {
         find = locator[0];
      }
      log.debug(" find is " + find);
      if (locator[0].contains(VALUE))
      {
         log.debug("checking value");
         for (WebElement we : options)
         {
            log.debug("Printing found options: " + we.getAttribute(VALUE));
            if (we.getAttribute(VALUE).equals(find))
            {
               we.click();
               break;
            }
         }
      }
      else if (locator[0].contains("id"))
      {
         log.debug("checking id");
         for (WebElement we : options)
         {
            log.debug("Printing found options: " + we.getAttribute("id"));
            if (we.getAttribute("id").equals(find))
            {
               we.click();
               break;
            }
         }
      }
      else if (locator[0].contains("index"))
      {
         log.debug("checking index");
         for (WebElement we : options)
         {
            log.debug("Printing found options: " + we.getAttribute("index"));
            if (we.getAttribute("index").equals(find))
            {
               we.click();
               break;
            }
         }
      }
      else
      {
         boolean flag = false;
         log.debug("checking text or label, default option");
         for (WebElement we : options)
         {
            log.debug("Printing found options: " + we.getText());
            if (we.getText().equals(find))
            {
               we.click();
               flag = true;
               break;
            }
         }
         if (flag == false)
         {
            click(optionLocator);
         }
      }
   }


   /* (non-Javadoc)
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#isSelectOptionDisable(java.lang.String,
    * java.lang.String) */
   @Override
   public boolean isSelectOptionEnable(String selectLocator, String optionLocator)
   {
      Select select = new Select(findElement(selectLocator));
      List<WebElement> options = select.getOptions();
      log.debug("options found " + options.size() + " elements");
      String[] locator = optionLocator.split("=");
      log.debug("Locator has " + locator.length + " elements");
      String find;
      if (locator.length > 1)
      {
         find = locator[1];
      }
      else
      {
         find = locator[0];
      }
      log.debug(" find is " + find);
      if (locator[0].contains(VALUE))
      {
         log.debug("checking value");
         for (WebElement we : options)
         {
            log.debug("Printing found options: " + we.getAttribute(VALUE));
            if (we.getAttribute(VALUE).equals(find))
            {
               return we.isEnabled();
            }
         }
      }
      else if (locator[0].contains("id"))
      {
         log.debug("checking id");
         for (WebElement we : options)
         {
            log.debug("Printing found options: " + we.getAttribute("id"));
            if (we.getAttribute("id").equals(find))
            {
               return we.isEnabled();
            }
         }
      }
      else if (locator[0].contains("index"))
      {
         log.debug("checking index");
         for (WebElement we : options)
         {
            log.debug("Printing found options: " + we.getAttribute("index"));
            if (we.getAttribute("index").equals(find))
            {
               return we.isEnabled();
            }
         }
      }
      else
      {
         log.debug("checking text or label, default option");
         for (WebElement we : options)
         {
            log.debug("Printing found options: " + we.getText());
            if (we.getText().equals(find))
            {
               return we.isEnabled();
            }
         }
      }
      return false;
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#select (java.lang.String, java.lang.String) */
   @Override
   public void selectAndTab(String selectLocator, String optionLocator)
   {
      select(selectLocator, optionLocator);
      focus(selectLocator);
   }


   @Override
   public void selectFrame(String locator)
   {
      webDriver.switchTo().frame(locator);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#selectWindow (java.lang.String) */
   @Override
   public void selectWindow(String windowID)
   {
      webDriver.switchTo().window(windowID);
   }


   @Override
   public String getWindowHandle()
   {
      return webDriver.getWindowHandle();
   }


   @Override
   public void sendKeys(String locator, Keys k)
   {
      waitForElementFound(locator, FIVE);
      WebElement we = findElement(locator);
      we.sendKeys(k);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#sendKeys( java.lang.String, java.lang.String) */
   @Override
   public void sendKeys(String locator, String value)
   {
      try
      {
         waitForElementFound(locator, FIVE);
         WebElement we = findElement(locator);
         we.sendKeys(value);
      } catch (ElementNotVisibleException e)
      {
         throw new RuntimeException("Unable to send keys to element: " + locator, e);
      }
   }


   /* (non-Javadoc)
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#sendOpenFile(java.lang.String,
    * java.lang.String) */
   @Override
   public void sendOpenFile(String locator, String filePath)
   {
      findElement(locator).sendKeys(filePath);
   }


   /* (non-Javadoc)
    *
    * @see com.twc.sit.atf.common.webapplications.WebBrowserController#sendSaveFile(java.lang.String,
    * java.lang.String) */
   @Override
   public void sendSaveFile(String locator, String filePath)
   {
      findElement(locator).sendKeys(filePath);
   }


   @Override
   public void setCheckBoxState(String checkBoxLocator, boolean isChecked)
   {
      waitForElementFound(checkBoxLocator);
      if (isChecked(checkBoxLocator) != isChecked)
      {
         click(checkBoxLocator);
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#submit (java.lang.String) */
   @Override
   public void submit(String locator)
   {
      findElement(locator).submit();
   }


   @Override
   public TargetLocator switchTo()
   {
      return webDriver.switchTo();
   }


   @Override
   public void switchToFrame(WebElement locator)
   {
      webDriver.switchTo().frame(locator);
   }


   @Override
   public void switchToFrame(int index)
   {
      webDriver.switchTo().frame(index);
   }


   @Override
   public void switchToFrame(String frameLocator)
   {
      webDriver.switchTo().frame(findElement(frameLocator));
   }


   @Override
   public void switchToNewlyOpenedWindow()
   {
      Set<String> winHandles = webDriver.getWindowHandles();
      String handle = (String)winHandles.toArray()[winHandles.size() - 1];
      webDriver.switchTo().window(handle);
   }


   @Override
   public void switchToParentWindow()
   {
      Set<String> winHandles = webDriver.getWindowHandles();
      String handle = (String)winHandles.toArray()[0];
      webDriver.switchTo().window(handle);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#type( java.lang.String, java.lang.String) */
   @Override
   public void type(String locator, String value)
   {
      WebElement we = findElement(locator);
      try
      {
         we.click();
      } catch (Exception e)
      {}
      we.clear();
      we.sendKeys(value);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#typeWithoutClearing( java.lang.String, java.lang.String) */
   @Override
   public void typeWithoutClearing(String locator, String value)
   {
      WebElement we = findElement(locator);
      // try to click otherwise ignore if it fails
      try
      {
         we.click();
      } catch (Exception e)
      {}
      we.sendKeys(value);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#typeUsingJavascript( java.lang.String, java.lang.String) */
   @Override
   public void typeUsingJavascript(String locator, String value)
   {
      WebElement we = findElement(locator);
      String event = "arguments[0].value=\"" + value + "\";";
      JavascriptExecutor executor = (JavascriptExecutor)webDriver;
      // Try to send keys the normal way but if it it fails, type using javascript
      try
      {
         we.sendKeys(value);
      } catch (Exception e)
      {
         executor.executeScript(event, we);
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#typeUsingRobot( java.lang.String, java.lang.String) */
   @Override
   public void typeUsingRobot(String locator, String value)
   {
      WebElement we = findElement(locator);
      // try to click otherwise ignore if it fails
      try
      {
         we.click();
      } catch (Exception e)
      {}
      ClipboardOwner clipboardOwner = new ClipboardOwner()
      {
         @Override
         public void lostOwnership(Clipboard clipboard, Transferable contents)
         {}
      };
      Robot robot;
      try
      {
         robot = new Robot();
         try
         {
            we.sendKeys(value);
         } catch (Exception e)
         {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(value);
            clipboard.setContents(stringSelection, clipboardOwner);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
         }
      } catch (AWTException e1)
      {
         e1.printStackTrace();
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#uncheck (java.lang.String) */
   @Override
   public void uncheck(String locator)
   {
      WebElement e = findElement(locator);
      if (e.isSelected())
      {
         e.click();
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#waitForAlert () */
   @Override
   public void waitForAlert()
   {
      waitForAlert(10000);
   }


   /* @ param timeout in milliseconds
    *
    * @see WebBrowserController#waitForAlert (int) */
   @Override
   public void waitForAlert(int timeOut)
   {
      for (int i = 0; i < timeOut; i++)
      {
         try
         {
            String alertText = getAlert();
            if ( !alertText.isEmpty())
            {
               log.debug("Alert: " + alertText);
               return;
            }
         } catch (Exception e)
         {}
         delay(1);
      }
      throw new RuntimeException("Alert not found");
   }


   /**
    * {@inheritDoc} Wait time is set to 15 seconds
    */
   @Override
   public void waitForElementFound(String guiElementDescription)
   {
      log.debug("debug waitForElementFound looking for " + guiElementDescription);
      waitForElementFound(guiElementDescription, FIFTEEN);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# waitForElementFound(java.lang.String, int) */
   @Override
   public void waitForElementFound(String guiElementDescription, int timeoutInSeconds)
   {
      String msg = "Wait %ss for [%s] on page [%s]";
      log.debug(String.format(msg, timeoutInSeconds, guiElementDescription, this.getClass()));
      try
      {
         WebElement e = (new WebDriverWait(webDriver, timeoutInSeconds)).until(ExpectedConditions.presenceOfElementLocated(getSelector(guiElementDescription)));
         log.debug("DEBUG: Element found");
         if (e != null)
         {
            return;
         }
         else
         {
            throw new ElementNotVisibleException(guiElementDescription + " did not load.");
         }
      } catch (TimeoutException toe)
      {
         throw new ElementNotVisibleException(guiElementDescription + " did not load with TimeoutException " + toe.getMessage());
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# waitForElementHidden(java.lang.String) */
   @Override
   public void waitForElementHidden(String guiElementDescription)
   {
      log.debug("waitForElementHidden looking for " + guiElementDescription);
      waitForElementHidden(guiElementDescription, TEN);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# waitForElementHidden(java.lang.String, int) */
   @Override
   public void waitForElementHidden(String guiElementDescription, int timeoutInSeconds)
   {
      String msg = "Wait %ss for [%s] on page [%s]";
      log.debug(String.format(msg, timeoutInSeconds, guiElementDescription, this.getClass().getSimpleName()));
      boolean visible = true;
      for (int i = 0; i < timeoutInSeconds; i++)
      {
         visible = isVisible(guiElementDescription);
         if ( !visible)
         {
            return;
         }
         delay(1);
      }
      throw new ElementNotVisibleException(guiElementDescription + " is still visible on the Web Page. ");
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# waitForElementNotFound(java.lang.String ) */
   @Override
   public void waitForElementNotFound(String guiElementDescription)
   {
      log.debug("waitForElementFound looking for " + guiElementDescription);
      waitForElementNotFound(guiElementDescription, TEN);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# waitForElementNotFound(java.lang.String , int) */
   @Override
   public void waitForElementNotFound(String guiElementDescription, int timeoutInSeconds)
   {
      String msg = "Wait %ss for [%s] on page [%s]";
      log.debug(String.format(msg, timeoutInSeconds, guiElementDescription, this.getClass().getSimpleName()));
      try
      {
         Boolean e = (new WebDriverWait(webDriver, timeoutInSeconds)).until(ExpectedConditions.invisibilityOfElementLocated(getSelector(guiElementDescription)));
         log.debug("Element found");
         if (e == true)
         {
            return;
         }
         else
         {
            throw new ElementNotVisibleException(guiElementDescription + " is still on the page.");
         }
      } catch (TimeoutException toe)
      {
         return;
      } catch (WebDriverException e)
      {
         return;
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# waitForElementVisible(java.lang.String) */
   @Override
   public void waitForElementVisible(String guiElementDescription)
   {
      log.debug("waitForElementVisible looking for " + guiElementDescription);
      waitForElementVisible(guiElementDescription, TEN);
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController# waitForElementVisible(java.lang.String, int) */
   @Override
   public void waitForElementVisible(String guiElementDescription, int timeoutInSeconds)
   {
      String msg = "Wait %ss for [%s] on page [%s]";
      log.debug("waitForElementVisible should time out in " + timeoutInSeconds);
      log.debug(String.format(msg, timeoutInSeconds, guiElementDescription, this.getClass().getSimpleName()));
      WebElement e = (new WebDriverWait(webDriver, timeoutInSeconds)).until(ExpectedConditions.visibilityOfElementLocated(getSelector(guiElementDescription)));
      log.debug("Element found");
      if (e != null)
      {
         return;
      }
      else
      {
         throw new ElementNotVisibleException(guiElementDescription + " did not become visible.");
      }
   }


   @Override
   public void waitForElementVisible(WebElement locator)
      throws ElementNotVisibleException
   {
      waitForElementVisible(locator, TEN);
   }


   @Override
   public void waitForElementVisible(WebElement locator, int timeoutInSeconds)
      throws ElementNotVisibleException
   {
      WebElement e = null;
      log.debug("Wait for Element Visible should time out in " + timeoutInSeconds);
      e = (new WebDriverWait(webDriver, timeoutInSeconds)).until(ExpectedConditions.visibilityOf(locator));
      log.debug("end wait for Element Visible");
      if (e != null)
      {
         return;
      }
      else
      {
         throw new ElementNotVisibleException(locator.getTagName() + " did not become visible.");
      }
   }


   /* (non-Javadoc)
    *
    * @see WebBrowserController#waitForText () */
   @Override
   public boolean waitForText(String text)
   {
      return waitForText(text, TEN);
   }


   @Override
   public boolean waitForText(String text, int timeOut)
   {
      for (int i = 0; i < timeOut; i++)
      {
         try
         {
            if (isTextPresent(text))
            {
               log.debug("Text " + text + " found.");
               return true;
            }
         } catch (Exception e)
         {}
         delay(1);
      }
      return false;
   }


   @Override
   public void windowMaximize()
   {
      webDriver.manage().window().maximize();
   }


   @Override
   public int getNumberOfOpenWindows()
   {
      int windows = 0;
      try
      {
         windows = webDriver.getWindowHandles().size();
      } catch (Exception e)
      {
         log.warn("All browser sessions appear to be closed: {}", e.getMessage());
      }
      return windows;
   }


   @Override
   public boolean isAlertPresent()
   {
      try
      {
         webDriver.switchTo().alert();
         return true;
      } catch (Exception e)
      {
         return false;
      }
   }


   @Override
   public void waitForElementClickable(String guiElementDescription)
      throws ElementNotVisibleException
   {
      waitForElementClickable(guiElementDescription, TEN);
   }


   @Override
   public void waitForElementClickable(String guiElementDescription, int timeoutInSeconds)
      throws ElementNotVisibleException
   {
      WebElement e = null;
      log.debug("Wait for Element Visible should time out in " + timeoutInSeconds);
      e = (new WebDriverWait(webDriver, timeoutInSeconds)).until(ExpectedConditions.elementToBeClickable(getSelector(guiElementDescription)));
      log.debug("end wait for Element Visible");
      if (e != null)
      {
         return;
      }
      else
      {
         throw new ElementNotVisibleException(guiElementDescription + " did not become visible.");
      }
   }


   @Override
   public boolean waitForPageLoad()
   {
      boolean flag = false;
      webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
      for (int i = 0; i <= 500; i++)
      {
         if (String.valueOf(((JavascriptExecutor)webDriver).executeScript("return document.readyState")).equals("complete"))
         {
            flag = true;
            break;
         }
         else
         {
            flag = false;
         }
      }
      return flag;
   }


   @Override
   public void waitForPageLoad(int timeout)
   {
      webDriver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
      for (int i = 0; i < timeout; i++)
      {
         if (String.valueOf(((JavascriptExecutor)webDriver).executeScript("return document.readyState")).equals("complete"))
         {
            break;
         }
      }
   }


   @Override
   public String getMainFrame()
   {
      String nameOrId = null;
      final List<WebElement> iframes = webDriver.findElements(By.tagName("iframe"));
      if (iframes.size() != 0)
      {
         nameOrId = iframes.get(0).getAttribute("id");
         if (nameOrId == null)
         {
            nameOrId = iframes.get(0).getAttribute("name");
            return nameOrId;
         }
         return nameOrId;
      }
      return null;
   }


   @Override
   public void switchToDefaultContent()
   {
      try
      {
         (webDriver).switchTo().defaultContent();
      } catch (final WebDriverException e)
      {
         log.error("unable to switch to default content", e);
      }
   }


   /**
    * selects a random element in the dropdown list
    *
    * @param selectElementLocator - identified dropdown list
    */
   @Override
   public void selectRandomItemFromDropDown(String selectElementLocator)
   {
      waitForDropdownPopulated(selectElementLocator);
      List<WebElement> options = getSelectOptions(selectElementLocator);
      Random r = new Random();
      select(selectElementLocator, options.get(r.nextInt(options.size())).getText());
   }


   /**
    * selects an item from a dropdown list that contains the parameter stringLike
    *
    * @param guiElementLocator - identifies the dropdown list
    * @param stringLike - string to match in the elements of the dropdown list
    * @return the text name of the "matching" item selected in the dropdown; empty string if no match found
    */
   @Override
   public String selectLikeItemFromDropDown(String guiElementLocator, String stringLike)
   {
      waitForDropdownPopulated(guiElementLocator);
      List<WebElement> options = getSelectOptions(guiElementLocator);
      Iterator<WebElement> iter = options.iterator();
      while (iter.hasNext())
      {
         WebElement el = iter.next();
         String opt = el.getText();
         if (opt.toLowerCase().contains(stringLike.toLowerCase()))
         {
            selectItemFromDropDown(guiElementLocator, opt);
            log.debug("value as selected in dropdown is " + opt);
            return opt;
         }
      }
      log.warn("Returning empty string, No Service name found in dropdown matching " + stringLike);
      return "";
   }


   /**
    * selects the correct item from the given select item in the GUI. If the value is not set (null or "")
    * this method does nothing, else it will try to select the item.<br>
    * <br>
    * If the value does not contain 'label=' it is added automatically.
    */
   @Override
   public void selectItemFromDropDown(String selectLocator, String value)
   {
      delay(1);
      waitForDropdownPopulated(selectLocator);
      if (StringUtils.isNotEmpty(value))
      {
         if (value.startsWith("label="))
         {
            value = value.replace("label=", "");
         }
         select(selectLocator, value);
      }
   }


   /**
    * if dropdown is a required field, will select from the list, depending on content of parameter item if
    * item is empty, selects random element from the dropdown list if item has content, selects it from the
    * dropdown list if dropdown isn't required field, does nothing
    *
    * @param selectElementLocator - identifies the dropdown list
    * @param item - element to select on the dropdown list
    * @param isRequired - is this a required field
    */
   @Override
   public void selectItemFromDropDown(String selectElementLocator, String item, boolean isRequired)
   {
      if (item == null || item.equals(""))
      {
         if (isRequired)
         {
            selectRandomItemFromDropDown(selectElementLocator);
         }
         return;
      }
      waitForDropdownPopulated(selectElementLocator);
      try
      {
         select(selectElementLocator, item);
      } catch (Exception e)
      {
         log.warn(item + " not found in the select, finding a good match");
         List<WebElement> options = getSelectOptions(selectElementLocator);
         Iterator<WebElement> iter = options.iterator();
         while (iter.hasNext())
         {
            WebElement el = iter.next();
            String opt = el.getText();
            if (opt.contains(item))
            {
               select(selectElementLocator, opt);
               return;
            }
         }
         selectItemFromDropDown(selectElementLocator, item);
      }
   }


   private void waitForDropdownPopulated(String guiElementLocator)
   {
      List<WebElement> options = getSelectOptions(guiElementLocator);
      for (int l = 1; l <= 30; l++)
      {
         options = getSelectOptions(guiElementLocator);
         if (options.size() >= 2)
         {
            delay(2);
            break;
         }
         else
         {
            delay(1);
         }
      }
   }


   /**
    * selects an item from a dropdown list that starts with the parameter stringStartsWith
    *
    * @param guiElementLocator - identifies the dropdown list
    * @param stringStartsWith - string to match starts with in elements of the dropdown list
    */
   @Override
   public void selectItemStartWithFromDropDown(String guiElementLocator, String stringStartsWith)
   {
      waitForDropdownPopulated(guiElementLocator);
      List<WebElement> options = getSelectOptions(guiElementLocator);
      Iterator<WebElement> iter = options.iterator();
      while (iter.hasNext())
      {
         WebElement el = iter.next();
         String opt = el.getText();
         if (opt.toLowerCase().startsWith(stringStartsWith.toLowerCase()))
         {
            selectItemFromDropDown(guiElementLocator, opt);
         }
      }
   }


   public static final String findFileName(String path, String name, FileSearchType fileSearchType)
   {
      String systemSeperator = System.getProperty("file.separator");
      log.debug("Starting search for file: {}{}{}", new Object[] { path, systemSeperator, name });
      String myPath = setFileDelimiter(path);
      File file = new File(myPath);
      if ( !file.isDirectory())
      {
         log.debug("Search path for file is not a directory: {}", path);
         return "";
      }
      if (name == null || name.isEmpty())
      {
         log.error("the file name for the search is empty: {}", name);
         return "";
      }
      File[] files = file.listFiles();
      try
      {
         for (File subFile : files)
         {
            String findPath;
            if ((fileSearchType == FileSearchType.Both || fileSearchType == FileSearchType.Directory && subFile.isDirectory() || fileSearchType == FileSearchType.File && subFile.isFile()) && subFile.getName().toLowerCase().matches(name.toLowerCase()))
            {
               log.debug("Search ok, " + subFile.getCanonicalPath());
               return subFile.getCanonicalPath();
            }
            if ( !subFile.isDirectory() || (findPath = findFileName(subFile.getCanonicalPath(), name, fileSearchType)).isEmpty())
            {
               continue;
            }
            return findPath;
         }
      } catch (IOException e)
      {
         log.error("Can't find file: {},{}", path, name);
         log.info("Can't find file " + path + name);
      }
      return "";
   }
   
   
   /**
    * 
    * @author P2193524
    *
    */
   public static enum FileSearchType
   {
    Directory,
    File,
    Both;
      private FileSearchType()
      {}
   }


   
   /**
    * 
    * @param aPath
    * @return
    */
   private static String setFileDelimiter(String aPath)
   {
      String systemFileSeparator = System.getProperty("file.separator");
      String wrongFileSeparator = systemFileSeparator.equals("/") ? "\\" : "/";
      char s = '\\';
      if (aPath.contains(wrongFileSeparator) || wrongFileSeparator.contains(String.valueOf(s)) && aPath.contains(String.valueOf(s)))
      {
         log.debug("replacing (wrong fileSeparator) " + wrongFileSeparator.toString() + " with SystemFileSeparator " + systemFileSeparator + " in " + aPath);
         return new String(aPath.replace(wrongFileSeparator, systemFileSeparator));
      }
      return aPath;
   }


   /**
    * Compares two images, returning true if they are similar enough to constitute a match.
    *
    * @param img1 The first image to compare
    * @param img2 The second image to compare
    * @param errorsPerThousandThreshold The acceptable errors per thousand (pixels); i.e. haw many mismatched
    *           pixels are there allowed to be before the images are considered different. The lower the
    *           number the more strict the comparison will be
    * @return
    * @throws IOException
    */
   public static boolean compareImages(String image1, String image2, long errorsPerThousandThreshold)
   {
      try
      {
         BufferedImage img1 = ImageIO.read(ClassLoader.getSystemResourceAsStream(image1));
         BufferedImage img2 = ImageIO.read(ClassLoader.getSystemResourceAsStream(image2));
         if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight())
         {
            log.trace("The two images are different sizes, automatically returning false");
            return false;
         }
         long ept = compareRGB24Images(img1, img2, 0, 0, img1.getWidth(), img1.getHeight(), DEFAULT_KERNEL_RADIUS);
         boolean match = ept <= errorsPerThousandThreshold;
         log.info("Match:[{}]  Err/1K:[{}]  Limit:[{}]  PixErrLim:[{}]", match, ept, errorsPerThousandThreshold, PIXEL_ERROR_LIMIT);
         return match;
      } catch (Exception ex)
      {
         log.error("Error", ex);
      }
      return false;
   }


   /**
    * Compares a given region of two RGB24 type buffered image objects, returning the errors per thousand
    * (pixels) between the two.
    *
    * @param image1 The first image to compare
    * @param image2 The second image to compare
    * @param xOffset The starting x coordinate of the region that will be compared
    * @param yOffset The starting y coordinate of the region that will be compared
    * @param width The width of the comparison region
    * @param height The height of the comparison region
    * @param kernelRadius The radius used in the local average filter, the larger the number, the fuzzier the
    *           comparison will be
    * @return The errors per thousand (pixels) that was found between the two images
    */
   private static long compareRGB24Images(BufferedImage image1, BufferedImage image2, int xOffset, int yOffset, int width, int height, int kernelRadius)
   {
      double[] luminance1 = computeLuminance(image1, xOffset, yOffset, width, height);
      double[] luminance2 = computeLuminance(image2, xOffset, yOffset, width, height);
      applyLuminanceAdjustment(luminance1, luminance2);
      luminance1 = performLocalAvgFilter(luminance1, width, height, kernelRadius);
      luminance2 = performLocalAvgFilter(luminance2, width, height, kernelRadius);
      double lumDiffSumCubes = 0;
      int pixelMismatchCntr = 0;
      for (int i = 0; i < luminance1.length && i < luminance2.length; i++)
      {
         double lumDiff3 = Math.pow(Math.abs(luminance2[i] - luminance1[i]), 3);
         lumDiffSumCubes += lumDiff3;
         if (lumDiff3 > PIXEL_THRESHOLD_CUBE_LUM_DIFF)
         {
            pixelMismatchCntr++;
         }
      }
      int npixels = width * height;
      int errorsPerThou = pixelMismatchCntr * (int)PIXEL_ERROR_LIMIT / npixels;
      double avgCubeLumDiff = lumDiffSumCubes / npixels;
      log.trace("Mismatched Pixels:[{}]  TotalPixels:[{}]  Errors Per Thousand:[{}]  " + "Total Difference Lum^3:[{}]  Average Difference Lum^3:[{}]  Pixel Error Threshol:[{}]", pixelMismatchCntr, npixels, errorsPerThou, lumDiffSumCubes, avgCubeLumDiff, PIXEL_ERROR_LIMIT);
      return errorsPerThou;
   }


   
   
   /**
    * 
    * @param image
    * @param xStart
    * @param yStart
    * @param width
    * @param height
    * @return
    */
   private static double[] computeLuminance(BufferedImage image, int xStart, int yStart, int width, int height)
   {
      checkBounds(image, xStart, yStart, width, height);
      double[] luminance = new double[width * height];
      for (int y = yStart; y < height; y++)
      {
         for (int x = xStart; x < width; x++)
         {
            int pixel = image.getRGB(x, y);
            int red = (pixel >> 16) & BYTE_MASK;
            int green = (pixel >> 8) & BYTE_MASK;
            int blue = (pixel >> 0) & BYTE_MASK;
            luminance[x * y] = RED_RATIO * red + GREEN_RATIO * green + BLUE_RATIO * blue;
         }
      }
      return luminance;
   }


   
   /**
    * 
    * @param image
    * @param xStart
    * @param yStart
    * @param width
    * @param height
    */
   private static void checkBounds(BufferedImage image, int xStart, int yStart, int width, int height)
   {
      int imgWidth = image.getWidth();
      int imgHeight = image.getHeight();
      if (xStart > imgWidth || width > imgWidth || (xStart + width) > imgWidth)
      {
         throw new RuntimeException("image does not fully contain the comparison region");
      }
      if (yStart > imgHeight || height > imgHeight || (yStart + height) > imgHeight)
      {
         throw new RuntimeException("image does not fully contain the comparison region");
      }
   }


   
   /**
    * 
    * @param luminance1
    * @param luminance2
    */
   private static void applyLuminanceAdjustment(double[] luminance1, double[] luminance2)
   {
      double avgLum1 = averageLuminance(luminance1);
      double avgLum2 = averageLuminance(luminance2);
      double diffAvgLum = avgLum1 - avgLum2;
      for (int i = 0; i < luminance2.length; i++)
      {
         luminance2[i] += diffAvgLum;
      }
   }


   
   /**
    * 
    * @param luminance
    * @return
    */
   private static double averageLuminance(double[] luminance)
   {
      double avgLum = 0;
      for (double element : luminance)
      {
         avgLum += element;
      }
      avgLum = avgLum / luminance.length;
      return avgLum;
   }


   
   
   /**
    * 
    * @param data
    * @param width
    * @param height
    * @param kernelRadius
    * @return
    */
   protected static double[] performLocalAvgFilter(double[] data, int width, int height, int kernelRadius)
   {
      double filteredData[] = new double[data.length];
      for (int ii = 0; ii < data.length; ii++)
      {
         int c = 0;
         for (int i = -kernelRadius; i <= kernelRadius; i++)
         {
            for (int j = -kernelRadius; j <= kernelRadius; j++)
            {
               int pixIndex = ii + (i * width) + j;
               if (pixIndex >= 0 && pixIndex < data.length)
               {
                  filteredData[ii] += data[pixIndex];
                  c++;
               }
            }
         }
         filteredData[ii] = filteredData[ii] / c;
      }
      return filteredData;
   }


   /**
    * This method tries to retrieve the system platform and return if it fails, it logs the exception and returns null
    *
    * @return the system platform, or null
    */
   @Override
   public String discoverPlatform()
   {
      String version = System.getProperty("os.version");
      String os = System.getProperty("os.name").toLowerCase();
      if (os.contains("win"))
      {
         return "Windows " + version;
      }
      else if (os.contains("nux") || os.contains("nix"))
      {
         return "Linux " + version;
      }
      else if (os.contains("mac"))
      {
         return "Mac " + version;
      }
      else
      {
         return "Other " + version;
      }
   }


   /**
    * Throws a SkipException with your string message added; your test method will stop executing at this point
    *
    * @param message appended to the SkipException
    * @throws SkipException with your message appended
    */
   @Override
   public void skip(String message)
   {
      throw new SkipException(message);
   }


   /**
    * This method build a webDriver based on the passed in browser request
    *
    * @param browser
    * @return WebDriver
    * @throws MalformedURLException
    */
   private static WebDriver buildWebDriver(String browserName)
   {
      DesiredCapabilities capability = null;
      BrowserType browserType = BrowserType.getBrowserTypeFromString(browserName);
      switch (browserType)
      {
         case MARIONETTE:
            FirefoxProfile ffProfile = null;
            ffProfile = new FirefoxProfile();
            ffProfile.setAcceptUntrustedCertificates(true);
            ffProfile.setAssumeUntrustedCertificateIssuer(false);
            DesiredCapabilities cap = DesiredCapabilities.firefox();
            cap.setCapability("marionette", true);
            cap.setCapability("firefox_profile", ffProfile);
            cap.setCapability("handlesAlerts", true);
            sysEnv = System.getenv("webdriver.firefox.marionette");
            if (sysEnv == null)
            {
               sysEnv = findFileName("C:/apps/selenium", "geckodriver.exe", FileSearchType.File);
               if (sysEnv == null)
               {
                  log.info("Please set the webdriver.firefox.marionette in system environment variables and restart the PC OR copy all your webdrivers under 'C:/selenium' location");
                  throw new RuntimeException("Failed to instantiate a WebDriver instance for " + browserName);
               }
               else
               {
                  System.setProperty("webdriver.firefox.marionette", sysEnv);
               }
            }
            return new MarionetteDriver(capability);
         case FIREFOX_DRIVER:
            capability = DesiredCapabilities.firefox();
            FirefoxProfile firefoxProfile = new FirefoxProfile();
            firefoxProfile.setAcceptUntrustedCertificates(true);
            firefoxProfile.setEnableNativeEvents(true);
            firefoxProfile.setAssumeUntrustedCertificateIssuer(true);
            capability.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
            return new FirefoxDriver(capability);
         case CHROME_DRIVER:
            sysEnv = System.getenv("webdriver.chrome.driver");
            if (sysEnv == null)
            {
               sysEnv = findFileName("C:/apps/selenium", "chromedriver.exe", FileSearchType.File);
               if (sysEnv == null)
               {
                  log.info("Please set the webdriver.chrome.driver in system environment variables and restart the PC OR copy all your webdrivers under 'C:/selenium' location");
                  throw new RuntimeException("Failed to instantiate a WebDriver instance for " + browserName);
               }
               else
               {
                  System.setProperty("webdriver.chrome.driver", sysEnv);
               }
            }
            capability = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments(new String[] { "--allow-running-insecure-content" });
            options.addArguments(new String[] { "--ignore-certificate-errors" });
            options.addArguments(new String[] { "--enable-npapi" });
            options.addArguments(new String[] { "--disable-extensions" });
            options.addArguments(new String[] { "--start-maximized" });
            capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capability.setCapability(ChromeOptions.CAPABILITY, options);
            return new ChromeDriver(capability);
         case INTERNET_EXPLORER:
            sysEnv = System.getenv("webdriver.ie.driver");
            if (sysEnv == null)
            {
               sysEnv = findFileName("C:/apps/selenium", "IEDriverServer.exe", FileSearchType.File);
               if (sysEnv == null)
               {
                  log.info("Please set the webdriver.ie.driver in system environment variables and restart the PC");
                  throw new RuntimeException("Failed to instantiate a WebDriver instance for " + browserName);
               }
               else
               {
                  System.setProperty("webdriver.ie.driver", sysEnv);
               }
            }
            capability = DesiredCapabilities.internetExplorer();
            capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            capability.setCapability("ignoreProtectedModeSettings", true);
            capability.setCapability("acceptSslCerts", true);
            capability.setCapability("ignoreZoomSetting", true);
            capability.setCapability("nativeEvents", true);
            capability.setCapability("ie.ensureCleanSession", true);
            return new InternetExplorerDriver(capability);
         case SAFARI:
            capability = DesiredCapabilities.safari();
            capability.setCapability("acceptSslCerts", true);
            capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capability.setCapability("ensureCleanSession", true);
            capability.setJavascriptEnabled(true);
            return new SafariDriver(capability);
 /*        case OPERA_DRIVER:
            capability = DesiredCapabilities.opera();
            capability.setCapability("opera.host", "127.0.0.1");
            return new OperaDriver();
  */
         case EDGE:
            capability = DesiredCapabilities.edge();
            EdgeOptions option = new EdgeOptions();
            capability.setCapability("edgeOptions", option);
            return new EdgeDriver(capability);
         default:
            log.info("Currenty support is there for Chrome, Firefox, Firefox Marionette, Internet Explorer, Edge, Safari & Opera. Support is not there for " + browserName);
            capability = DesiredCapabilities.firefox();
            FirefoxProfile firefoxProf = new FirefoxProfile();
            firefoxProf.setAcceptUntrustedCertificates(true);
            firefoxProf.setEnableNativeEvents(true);
            firefoxProf.setAssumeUntrustedCertificateIssuer(true);
            capability.setCapability(FirefoxDriver.PROFILE, firefoxProf);
            return new FirefoxDriver(capability);
      }
   }


   /**
    * This method build a RemoteWebDriver based on the passed in browser request
    *
    * @param browser
    * @return RemoteWebDriver
    *
    */
   private static RemoteWebDriver buildRemoteWebDriver(String browserName)
   {
      DesiredCapabilities capability = null;
      BrowserType browserType = BrowserType.getBrowserTypeFromString(browserName);
      switch (browserType)
      {
         case MARIONETTE:
            FirefoxProfile ffProfile = null;
            ffProfile = new FirefoxProfile();
            ffProfile.setAcceptUntrustedCertificates(true);
            ffProfile.setAssumeUntrustedCertificateIssuer(false);
            DesiredCapabilities cap = DesiredCapabilities.firefox();
            cap.setCapability("marionette", true);
            cap.setCapability("firefox_profile", ffProfile);
            cap.setCapability("handlesAlerts", true);
            sysEnv = System.getenv("webdriver.firefox.marionette");
            if (sysEnv == null)
            {
               sysEnv = findFileName("C:/apps/selenium", "geckodriver.exe", FileSearchType.File);
               if (sysEnv == null)
               {
                  log.info("Please set the webdriver.firefox.marionette in system environment variables and restart the PC OR copy all your webdrivers under 'C:/selenium' location");
                  throw new RuntimeException("Failed to instantiate a WebDriver instance for " + browserName);
               }
               else
               {
                  System.setProperty("webdriver.firefox.marionette", sysEnv);
               }
            }
            return new MarionetteDriver(capability);
         case FIREFOX_DRIVER:
            capability = DesiredCapabilities.firefox();
            FirefoxProfile firefoxProfile = new FirefoxProfile();
            firefoxProfile.setAcceptUntrustedCertificates(true);
            firefoxProfile.setEnableNativeEvents(true);
            capability.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
            capability.setPlatform(capability.getPlatform());
            capability.setVersion(capability.getVersion());
            return new FirefoxDriver(capability);
         case CHROME_DRIVER:
            sysEnv = System.getenv("webdriver.chrome.driver");
            if (sysEnv == null)
            {
               sysEnv = findFileName("C:/apps/selenium", "chromedriver.exe", FileSearchType.File);
               if (sysEnv == null)
               {
                  log.info("Please set the webdriver.chrome.driver in system environment variables and restart the PC OR copy all your webdrivers under 'C:/selenium' location");
                  throw new RuntimeException("Failed to instantiate a WebDriver instance for " + browserName);
               }
               else
               {
                  System.setProperty("webdriver.chrome.driver", sysEnv);
               }
            }
            capability = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments(new String[] { "--allow-running-insecure-content" });
            options.addArguments(new String[] { "--ignore-certificate-errors" });
            options.addArguments(new String[] { "--enable-npapi" });
            options.addArguments(new String[] { "--disable-extensions" });
            options.addArguments(new String[] { "--start-maximized" });
            capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capability.setCapability(ChromeOptions.CAPABILITY, options);
            capability.setPlatform(capability.getPlatform());
            capability.setVersion(capability.getVersion());
            return new ChromeDriver(capability);
         case INTERNET_EXPLORER:
            sysEnv = System.getenv("webdriver.ie.driver");
            if (sysEnv == null)
            {
               sysEnv = findFileName("C:/apps/selenium", "IEDriverServer.exe", FileSearchType.File);
               if (sysEnv == null)
               {
                  log.info("Please set the webdriver.ie.driver in system environment variables and restart the PC");
                  throw new RuntimeException("Failed to instantiate a WebDriver instance for " + browserName);
               }
               else
               {
                  System.setProperty("webdriver.ie.driver", sysEnv);
               }
            }
            capability = DesiredCapabilities.internetExplorer();
            capability.setCapability("ignoreProtectedModeSettings", true);
            String browserVersion = capability.getVersion();
            if (browserVersion != null && browserVersion.equals("10"))
            {
               capability.setPlatform(Platform.WINDOWS);
               capability.setVersion(browserVersion);
            }
            else if (browserVersion != null && browserVersion.equals("11"))
            {
               capability.setPlatform(Platform.WIN8_1);
               capability.setVersion(browserVersion);
            }
            capability.setBrowserName("internet explorer");
            capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            return new InternetExplorerDriver(capability);
         case SAFARI:
            capability = DesiredCapabilities.safari();
            capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capability.setCapability("ensureCleanSession", true);
            capability.setPlatform(capability.getPlatform());
            capability.setVersion(null);
            return new SafariDriver(capability);
/*         case OPERA_DRIVER:
            capability = DesiredCapabilities.opera();
            capability.setCapability("opera.profile", "/test");
            return new OperaDriver();
*/
         case EDGE:
            capability = DesiredCapabilities.edge();
            EdgeOptions option = new EdgeOptions();
            capability.setCapability("edgeOptions", option);
            return new EdgeDriver(capability);
         default:
            log.info("Currenty support is there for Chrome, Firefox, Firefox Marionette, Internet Explorer, Edge, Safari & Opera. Support is not there for " + browserName);
            capability = DesiredCapabilities.firefox();
            firefoxProfile = new FirefoxProfile();
            firefoxProfile.setAcceptUntrustedCertificates(true);
            firefoxProfile.setEnableNativeEvents(true);
            capability.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
            capability.setPlatform(capability.getPlatform());
            capability.setVersion(capability.getVersion());
            return new FirefoxDriver(capability);
      }
   }
   
   
   /**
    * This method holds the data in order to configure proper testing environment
    *
    */
   private enum BrowserType
   {
    MARIONETTE("firefoxm"),
    FIREFOX_DRIVER("firefox"),
    CHROME_DRIVER("chrome"),
    SAFARI("safari"),
    INTERNET_EXPLORER("internetexplorer"),
    OPERA_DRIVER("opera"),
    EDGE("edge");
	   
	   
	   
      private BrowserType(String stringName)
      {
         this.setBrowserName(stringName);
      }


      
      /**
       * 
       * @param stringName
       * @return
       */
      private static BrowserType getBrowserTypeFromString(String stringName)
      {
         String a = stringName.toLowerCase().replaceAll(" ", "").trim();
         if ((a.equals("ff")) || (a.equals("firefox")) || (a.startsWith("firefoxdriver")))
         {
            return BrowserType.FIREFOX_DRIVER;
         }
         else if ((a.equals("ffm")) || (a.equals("firefoxm")) || (a.contains("marionette")) || (a.equals("firefoxmarionette")))
         {
            return BrowserType.MARIONETTE;
         }
         else if ((a.equals("chrome")) || (a.equals("chromedriver")) || (a.equals("googlechrome")))
         {
            return BrowserType.CHROME_DRIVER;
         }
         else if ((a.equals("internetexplorer")) || (a.equals("ie")) || (a.equals("internet_explorer")) || (a.startsWith("ie")))
         {
            return BrowserType.INTERNET_EXPLORER;
         }
         else if (a.equals("safari"))
         {
            return BrowserType.SAFARI;
         }
         else if (a.equals("opera"))
         {
            return BrowserType.OPERA_DRIVER;
         }
         else if (a.equals("edge") || (a.contains("microsoftedge")))
         {
            return BrowserType.EDGE;
         }
         else
         {
            return BrowserType.FIREFOX_DRIVER;
         }
      }


      
      /**
       * 
       * @param stringName
       */
      private void setBrowserName(String stringName)
      {}
   }


   
   /**
    * 
    */
   @Override
   public void close()
      throws Exception
   {
      if (webDriver != null)
      {
         webDriver.quit();
      }
   }
}
