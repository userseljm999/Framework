package com.chtr.tmoauto.selenium;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.chtr.tmoauto.logging.LoggingMultiThreaded;

public class SeleniumMultihthreaded {

	public LoggingMultiThreaded log = null;
	public String tcID = null;

	public SeleniumMultihthreaded(LoggingMultiThreaded log, String tcID) {
		this.log = log;
		this.tcID = tcID;
	}

	// ******************************************************************************
	// *
	// * Name: fw_set_chrome_web_driver
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to set Chrome as the driver
	// *
	// ******************************************************************************

	public WebDriver fw_set_chrome_web_driver() {
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("disable-extensions");
		options.addArguments("--start-maximized");
		options.addArguments("no-sandbox");
		options.addArguments("privileged");

		// code to set proxy as direct
		Proxy proxy = new Proxy();
		proxy.setProxyType(org.openqa.selenium.Proxy.ProxyType.DIRECT);
		DesiredCapabilities dc = DesiredCapabilities.chrome();
		dc.setCapability(CapabilityType.PROXY, proxy);
		dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		dc.setCapability(ChromeOptions.CAPABILITY, options);
		dc.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
		System.setProperty("webdriver.chrome.driver", "driver\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver(dc);

		return driver;

	}

	// ******************************************************************************
	// *
	// * Name: fw_set_ie_driver
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to set Internet Explorer as the driver
	// *
	// ******************************************************************************

	public WebDriver fw_set_ie_driver() {
		// setting no proxy for the browser
		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setNoProxy(null);

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		ieCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
		System.setProperty("webdriver.ie.driver", "driver\\IEDriverServer.exe");
		WebDriver driver = new InternetExplorerDriver(ieCapabilities);

		return driver;
	}

	// ******************************************************************************
	// *
	// * Name: fw_set_firefox_driver
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to set Mozilla FireFox as the driver
	// *
	// ******************************************************************************

	public WebDriver fw_set_firefox_driver() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("network.proxy.type", 0);

		WebDriver driver = new FirefoxDriver(profile);
		return driver;
	}

	// ******************************************************************************
	// *
	// * Name: fw_go_to_url
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to go to a given url
	// *
	// ******************************************************************************

	public WebDriver fw_go_to_url(String url, WebDriver driver) {
		driver.get(url);
		driver.manage().window().maximize();
		return driver;

	}

	// ******************************************************************************
	// *
	// * Name: fw_submit_element
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to submit the web element
	// *
	// ******************************************************************************

	public void fw_submit_element(String locator, String locatorValue, WebDriver driver) throws Exception {
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement element = driver.findElement(fw_get_element_object(locator, locatorValue));
			element.submit();
		} catch (Exception e) {

			log.fw_writeLogEntry("Error -     Unable to find element having" + locator + "=" + locatorValue, "NA");
			throw new Exception("    Unable to find element having" + locator + "=" + locatorValue);
		}

		log.fw_writeLogEntry("   Clicked on Submit Button having " + locator + "=" + locatorValue, "NA");

	}

	// ******************************************************************************
	// *
	// * Name: fw_click_element
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to click on a web element
	// *
	// ******************************************************************************

	public void fw_click_element(String locator, String locatorValue, WebDriver driver) throws Exception {
		try {

			// create a wait with 10-second timeout
			WebDriverWait wait = new WebDriverWait(driver, 10);

			// wait for the element to become visible

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement element = driver.findElement(fw_get_element_object(locator, locatorValue));
			element = wait.until(ExpectedConditions.visibilityOf(element));
			element.click();
		} catch (Exception e) {
			e.printStackTrace();

			log.fw_writeLogEntry("Error -     Unable to find element having" + locator + "=" + locatorValue, "NA");
			throw new Exception("    Unable to find element having" + locator + "=" + locatorValue);

		}

		log.fw_writeLogEntry("   Clicked on Element having " + locator + "=" + locatorValue, "NA");

	}

	// ******************************************************************************
	// *
	// * Name: fw_set_element_text
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to set a value for a web element
	// *
	// ******************************************************************************

	public void fw_set_element_text(String locator, String locatorValue, String value, WebDriver driver)
			throws Exception {

		for (int retry = 0; retry < 60; retry++) {

			try {

				WebElement element = driver.findElement(fw_get_element_object(locator, locatorValue));
				element.sendKeys(value);
			} catch (Exception e) {
				if (retry > 60) {
					log.fw_writeLogEntry("Error -     Unable to find element having" + locator + "=" + locatorValue,
							"NA");
					throw new Exception("    Unable to find element having" + locator + "=" + locatorValue);
				} else {
					Thread.sleep(1000);
					continue;
				}

			}
			break;
		}

		log.fw_writeLogEntry("   Setting element  having " + locator + "=" + locatorValue + " value as : " + value,
				"NA");

	}

	// ******************************************************************************
	// *
	// * Name: fw_get_element_text
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to obtain value of a web element
	// *
	// ******************************************************************************
	public String fw_get_element_text(String locator, String locatorValue, WebDriver driver) throws Exception {
		String textReturn = "";
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement element = driver.findElement(fw_get_element_object(locator, locatorValue));
			textReturn = element.getText();
		} catch (Exception e) {

			log.fw_writeLogEntry("Error -     Unable to find element having" + locator + "=" + locatorValue, "NA");
			throw new Exception("    Unable to find element having" + locator + "=" + locatorValue);

		}

		log.fw_writeLogEntry("   Retrieved  text from element  having " + locator + "=" + locatorValue, "NA");

		return textReturn;
	}

	// ******************************************************************************
	// *
	// * Name: fw_dropdown_select_element
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to select an option in a dropdown by a given value
	// *
	// ******************************************************************************

	public void fw_dropdown_select_element_byText(String locator, String locatorValue, String selectValue,
			WebDriver driver) throws Exception {
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Select dropdownT = new Select(driver.findElement(fw_get_element_object(locator, locatorValue)));
			dropdownT.selectByVisibleText(selectValue);

		} catch (Exception e) {

			log.fw_writeLogEntry("Error -     Unable to find element having" + locator + "=" + locatorValue, "NA");
			throw new Exception("    Unable to find element having" + locator + "=" + locatorValue);

		}

		log.fw_writeLogEntry(
				"   Selected value " + selectValue + " from selectbox  having " + locator + "=" + locatorValue, "NA");

	}

	// ******************************************************************************
	// *
	// * Name: fw_get_attribute_value
	// * Author: Rohit
	// * Date: 12/26/2016
	// * Method to get attribute value using attribute name
	// *
	// ******************************************************************************

	public String fw_get_attribute_value(String locatorValue, String locator, String attributeName, WebDriver driver)
			throws Exception {
		String getText = null;
		try {
			getText = driver.findElement(fw_get_element_object(locator, locatorValue)).getAttribute(attributeName);

		} catch (Exception e) {
			log.fw_writeLogEntry("Error -    Unable to find element having" + locator + "=" + locatorValue, "NA");
			throw new Exception("    Unable to find element having" + locator + "=" + locatorValue);

		}
		log.fw_writeLogEntry("	Attribute of element  having " + locator + "=" + locatorValue + " value as" + getText,
				"NA");
		return getText;

	}

	// ******************************************************************************
	// *
	// * Name: fw_dropdown_select_element
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to select an option in a dropdown by a given value
	// *
	// ******************************************************************************

	public void fw_dropdown_select_element(String locator, String locatorValue, String selectValue, WebDriver driver)
			throws Exception {
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Select dropdownT = new Select(driver.findElement(fw_get_element_object(locator, locatorValue)));
			dropdownT.selectByValue(selectValue);

		} catch (Exception e) {

			log.fw_writeLogEntry("Error -     Unable to find element having" + locator + "=" + locatorValue, "NA");
			throw new Exception("    Unable to find element having" + locator + "=" + locatorValue);

		}

		log.fw_writeLogEntry(
				"   Selected value " + selectValue + " from selectbox  having " + locator + "=" + locatorValue, "NA");

	}

	// ******************************************************************************
	// *
	// * Name: fw_move_to_element
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to switch to a web element
	// *
	// ******************************************************************************

	public void fw_move_to_element(String locator, String locatorValue, WebDriver driver) throws Exception {

		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			Actions action1 = new Actions(driver);
			action1.moveToElement(driver.findElement(fw_get_element_object(locator, locatorValue))).perform();
		} catch (Exception e) {

			log.fw_writeLogEntry("Error -     Unable to find element having" + locator + "=" + locatorValue, "NA");
			throw new Exception("    Unable to find element having" + locator + "=" + locatorValue);

		}

		log.fw_writeLogEntry("   Moved to element  having " + locator + "=" + locatorValue, "NA");

	}

	public boolean fw_validate_element_is_displayed(String locator, String locatorValue, WebDriver driver) {

		Boolean b = driver.findElement(fw_get_element_object(locator, locatorValue)).isDisplayed();
		if (b)
			return true;
		else
			return false;
	}

	public boolean fw_validate_element_is_selected(String locator, String locatorValue, WebDriver driver) {

		Boolean b = driver.findElement(fw_get_element_object(locator, locatorValue)).isSelected();
		if (b)
			return true;
		else
			return false;
	}

	// ******************************************************************************
	// *
	// * Name: fw_validate_element_is_enabled
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to check whether a web element is enabled
	// *
	// ******************************************************************************

	public boolean fw_validate_element_is_enabled(String locator, String locatorValue, WebDriver driver) {

		Boolean b = driver.findElement(fw_get_element_object(locator, locatorValue)).isEnabled();
		if (b)
			return true;
		else
			return false;
	}

	// ******************************************************************************
	// *
	// * Name: fw_call_execute_through_javascript
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to execute Javascript through selenium driver
	// *
	// ******************************************************************************

	public WebDriver fw_call_execute_through_javascript(String locator, String locatorValue, WebDriver driver)
			throws Exception {

		try {
			int countForEnabled = 0;
			int countFordisplayed = 0;

			while (fw_validate_element_is_displayed(locator, locatorValue, driver)) {
				Thread.sleep(500);
				countFordisplayed++;
				if (countFordisplayed > 3)
					break;
			}

			while (fw_validate_element_is_enabled(locator, locatorValue, driver)) {
				Thread.sleep(500);
				countForEnabled++;
				if (countForEnabled > 3)
					break;
			}

			WebElement webelement = driver.findElement(fw_get_element_object(locator, locatorValue));
			JavascriptExecutor js0 = (JavascriptExecutor) driver;
			js0.executeScript("arguments[0].click();", webelement);

		} catch (Exception e) {

			log.fw_writeLogEntry("Error -     Unable to find element having" + locator + "=" + locatorValue, "NA");
			throw new Exception("    Unable to find element having" + locator + "=" + locatorValue);

		}

		log.fw_writeLogEntry("   Clicked on element  having " + locator + "=" + locatorValue, "NA");

		return driver;

	}

	public void waitForPageLoaded(WebDriver driver) throws Exception {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(expectation);
		} catch (Throwable error) {
			throw new Exception("Timeout waiting for Page Load Request to complete.");
		}
	}

	// ******************************************************************************
	// *
	// * Name: fw_get_element_object
	// * Author: Gaurav Kumar
	// * Date: 11/16/2016
	// * Method to get By object according to passed parameter values for
	// locator
	// *
	// ******************************************************************************

	public By fw_get_element_object(String locName, String locValue) {

		// Find by element's xpath
		if (locName.equalsIgnoreCase("XPATH")) {

			return By.xpath(locValue);
		}

		// Find by element's id
		if (locName.equalsIgnoreCase("ID")) {

			return By.id(locValue);
		}
		// find by element's class
		else if (locName.equalsIgnoreCase("CLASSNAME")) {

			return By.className(locValue);

		}
		// find by element's name
		else if (locName.equalsIgnoreCase("NAME")) {

			return By.name(locValue);

		}
		// Find by element's css selector
		else if (locName.equalsIgnoreCase("CSS")) {

			return By.cssSelector(locValue);

		}
		// find by link
		else if (locName.equalsIgnoreCase("LINK")) {

			return By.linkText(locValue);

		}
		// find by partial link
		else if (locName.equalsIgnoreCase("PARTIALLINK")) {

			return By.partialLinkText(locValue);

		} else {
			System.out.println("Wrong entry ");
		}
		return null;

	}

	public static void saveSnapShots(WebDriver driver, String testCaseID, String filePath) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
			Date dt = new Date();
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			FileUtils.copyFile(scrFile,
					new File(filePath + "\\" + dateFormat.format(dt) + " - TC -" + testCaseID + " - Screenshot.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean fw_element_Present(String Loacator, String LocatorValue, WebDriver driverInstance) {
		if (driverInstance.findElements(fw_get_element_object(Loacator, LocatorValue)).size() > 0)
			return true;
		else
			return false;
	}

}
