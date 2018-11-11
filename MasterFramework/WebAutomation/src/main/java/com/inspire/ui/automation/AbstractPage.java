package com.inspire.ui.automation;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;

import com.inspire.abstestbase.AbstractTestBase;
import com.inspire.constants.UtilConstants;
import com.inspire.ui.constants.UiConstants;
import com.inspire.utils.PropertyFileUtils;
import com.inspire.utils.TestDataGenerator;

/**
 * @author sachi
 *
 */
public abstract class AbstractPage extends PageObject {
	int elementWaitTime = 10;
	public static String highLightPropertyName = "outline";
	public static String highlightColor = "#00ff00 solid 3px";
	private static String saveAtS3 = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "savesnapshotsAtS3");
	public static String S3Url = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "S3serverurl");
	WebElement we = null;

	public AbstractPage(WebDriver driver) {
		super(driver);
		this.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		this.driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		this.driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
	}

	public AbstractPage(RemoteWebDriver driver) {
		super(driver);
		this.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		this.driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		this.driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
	}

	public void setPageLoadTimeout(int timeout) {
		try {
			this.driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public void setObjectWaitTimeout(int timeoutSeconds) {
		elementWaitTime = timeoutSeconds;
		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public static By buildLocator(String how, String what) throws Exception {

		switch (how.toLowerCase()) {
		case "id":
			return By.id(what);
		case "name":
			return By.name(what);
		case "class":
		case "classname":
			return By.xpath("//*[@class='" + what + "']");
		// locator = By.className(what);
		case "css":
		case "csslocator":
		case "cssselector":
			return By.cssSelector(what);
		case "linktext":
		case "link":
			return By.linkText(what);
		case "partiallinktext":
		case "partiallink":
			return By.partialLinkText(what);
		case "tagname":
		case "tag":
		case "html tag":
		case "htmltag":
			return By.tagName(what);
		case "xpath":
			return By.xpath(what);
		default:
			throw new Exception("Unknown locator type :" + how);
		}
	}

	public static WebElement findElement(WebDriver driver, By by) {
		if (driver != null) {
			return driver.findElement(by);
		} else
			return null;
	}

	public static By findBy(String propertyFileName, String propertyName) throws Exception {

		String how = getHow(propertyFileName, propertyName);
		String what = getWhat(propertyFileName, propertyName);
		switch (how.toLowerCase()) {
		case "id":
			return By.id(what);
		case "name":
			return By.name(what);
		case "class":
		case "classname":
			return By.xpath("//*[@class='" + what + "']");
		// locator = By.className(what);
		case "css":
		case "csslocator":
		case "cssselector":
			return By.cssSelector(what);
		case "linktext":
		case "link":
			return By.linkText(what);
		case "partiallinktext":
		case "partiallink":
			return By.partialLinkText(what);
		case "tagname":
		case "tag":
		case "html tag":
		case "htmltag":
			return By.tagName(what);
		case "xpath":
			return By.xpath(what);
		default:
			reportFail("Unknown locator type :" + how);
			throw new Exception("Unknown locator type :" + how);
		}

	}

	public String getProperty(String propertyName) {
		String locatorFile = getLocatorPath();
		return PropertyFileUtils.getProperty(locatorFile, propertyName);

	}

	public static String getHow(String propertyFileName, String propertyName) throws InvalidLocator {
		String propVal = PropertyFileUtils.getProperty(propertyFileName, propertyName);
		try {

			if (propVal.split("->").length > 1) {
				return propVal.split("->")[0];
			} else
				return "xpath";
		} catch (NullPointerException e) {
			reportFail("Locator type: " + propVal + " provided is either blank or null " + propertyName
					+ "\n trace the property file" + propertyFileName);
			throw new InvalidLocator(propertyName, "Locator type: " + propVal + " provided is either blank or null "
					+ propertyName + "\n trace the property file" + propertyFileName);
		}
	}

	public static String getWhat(String propertyFileName, String propertyName) throws InvalidLocator {
		String propVal = PropertyFileUtils.getProperty(propertyFileName, propertyName);
		try {

			// log.info(propVal);
			if (propVal.split("->").length > 1) {
				return propVal.split("->")[1];
			} else
				return propVal.split("->")[0];
		} catch (NullPointerException e) {
			reportFail("Locator" + propVal + "provided is either blank or null " + propertyName
					+ "\n trace the property file" + propertyFileName);
			throw new InvalidLocator(propertyName, "Locator" + propVal + "provided is either blank or null "
					+ propertyName + "\n trace the property file" + propertyFileName);
		}

	}

	public static String getProperty(String propertyFileName, String propertyName) {
		String propVal = PropertyFileUtils.getProperty(propertyFileName, propertyName);
		return propVal;

	}

	public void highlightElement(WebElement element) {
		if (highLightPropertyName.trim().isEmpty() || highlightColor.trim().isEmpty())
			return;

		try {
			JavascriptExecutor jsDriver = ((JavascriptExecutor) driver);

			// Retrieve current background color
			// String propertyName = "border"; //"outline";
			// propertyName = "outline";
			String originalColor = "none";
			// String highlightColor = "#00ff00 solid 3px";

			try {
				// This works with internet explorer
				originalColor = jsDriver
						.executeScript("return arguments[0].currentStyle." + highLightPropertyName, element).toString();
			} catch (Exception e) {

				// This works with firefox, chrome and possibly others
				originalColor = jsDriver.executeScript("return arguments[0].style." + highLightPropertyName, element)
						.toString();
			}

			try {
				jsDriver.executeScript("arguments[0].style." + highLightPropertyName + " = '" + highlightColor + "'",
						element);
				Thread.sleep(50);

				jsDriver.executeScript("arguments[0].style." + highLightPropertyName + " = '" + originalColor + "'",
						element);
				Thread.sleep(50);

				jsDriver.executeScript("arguments[0].style." + highLightPropertyName + " = '" + highlightColor + "'",
						element);
				Thread.sleep(50);

				jsDriver.executeScript("arguments[0].style." + highLightPropertyName + " = '" + originalColor + "'",
						element);
			} catch (Exception e2) {
				reportFail("error in highlight function " + e2.getMessage());
			}

		} catch (Exception e) {
			reportFail("error in highlight function " + e.getMessage());
		}
	}

	public void takeSnapShot() {
		String fileWithPath = UiConstants.SNAPSHOT_PATH + AbstractTestBase.snapshotfolder + "/"
				+ TestDataGenerator.createText() + ".png";
		TakesScreenshot scrShot = ((TakesScreenshot) driver);
		File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
		File destFile = new File(fileWithPath);
		try {
			FileUtils.copyFile(srcFile, destFile);
			reportLogScreenshot(destFile, "");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void takeSnapShotonError() {
		String fileName = TestDataGenerator.createText() + ".png";
		String fileWithPath = UiConstants.SNAPSHOT_PATH + AbstractTestBase.snapshotfolder + "/" + fileName;
		TakesScreenshot scrShot = ((TakesScreenshot) driver);
		File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
		File destFile = new File(fileWithPath);

		try {
			FileUtils.copyFile(srcFile, destFile);
			reportLogScreenshot(destFile, "Error occurred");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void performType(WebElement element, String data) {
		if (StringUtils.isBlank(data)) {
			log.info(("Data is not present to perform type action"));
			return;
		}
		highlightElement(element);
		clear(element);
		type(element, data);
		takeSnapShot();
	}

	public boolean isWebElementPresent(String element) {
		if (getWebElements(element).size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isWebElementDisplayed(String element) {
		return getWebElement(element).isDisplayed();

	}

	public boolean isWebElementEnabled(String element) {
		return getWebElement(element).isEnabled();

	}

	public boolean isTextPresentInPage(String text2Search) {
		return driver.getPageSource().contains(text2Search);

	}

	public void selectByOption(WebElement element, String data) {

		try {
			Select sel = new Select(element);
			highlightElement(element);
			sel.selectByVisibleText(data);
			takeSnapShot();
			log("select (by option)" + data + " from the dropdown");
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			reportFail("Webelement selection " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			reportFail("Webelement selection " + e.getMessage());
		}

	}

	public void selectByIndex(WebElement element, int index) {

		try {
			Select sel = new Select(element);
			highlightElement(element);
			sel.selectByIndex(index);
			takeSnapShot();
			log("select (by index)" + index + " from the dropdown");
		} catch (Exception e) {
			log.info("Exception occured while select by Index " + e.getMessage());
			reportFail("Exception occured while select by Index " + e.getMessage());
		}
	}

	public void selectByValue(WebElement element, String value) {

		try {
			Select sel = new Select(element);
			highlightElement(element);
			sel.selectByValue(value);
			takeSnapShot();
			log("select (by value)" + value + " from the dropdown");
		} catch (Exception e) {
			log.info("Exception occured while select by Value " + e.getMessage());
			reportFail("Exception occured while select by Value " + e.getMessage());
		}
	}

	public void type(WebElement element, String data) {

		try {
			log("type " + data + " in input field.");
			highlightElement(element);
			element.sendKeys(data);
			takeSnapShot();		
		} catch (Exception e) {
			reportFail("error while type in text field " + e.getMessage());
		}
	}

	public void log(String str) {
		Reporter.log("<td><font size=\"2\" face=\"Comic sans MS\" color=\"green\"><b>" + str + "</b></font></td></tr>");
	}

	public void clear(WebElement element) {

		try {
			highlightElement(element);
			element.clear();
			takeSnapShot();
		} catch (Exception e) {
			e.printStackTrace();
			reportFail("error while clearing the web field " + e.getMessage());
		}
	}

	public void click(WebElement element) {

		highlightElement(element);
		element.click();
		takeSnapShot();
		log("clicked on webelement");
	}

	public String getTitle() {

		return driver.getTitle();
	}

	public void maximiseWindow() {

		driver.manage().window().maximize();
	}

	// pop up handling

	public void acceptAlert() {

		driver.switchTo().alert().accept();
	}

	public void dismissAlert() {

		driver.switchTo().alert().dismiss();
	}

	public String getAlertText() {

		return driver.switchTo().alert().getText();
	}

	// handlinf frames

	public void switchToFrame(int frame) {
		try {
			driver.switchTo().frame(frame);
			log.info(("frame  navigation through id " + frame));
		} catch (NoSuchFrameException e) {
			log.info(("Unable to locate frame with id " + frame + e.getStackTrace()));
		} catch (Exception e) {
			log.info(("Unable to navigate to frame with id " + frame + e.getStackTrace()));
		}
	}

	public void switchToFrame(String frame) {
		try {
			driver.switchTo().frame(frame);
			log.info(("frame  navigation through id " + frame));
		} catch (NoSuchFrameException e) {
			log.info(("Unable to locate frame with id " + frame + e.getStackTrace()));
		} catch (Exception e) {
			log.info(("Unable to navigate to frame with id " + frame + e.getStackTrace()));
		}
	}

	public void switchToFrame(String ParentFrame, String ChildFrame) {
		try {
			driver.switchTo().frame(ParentFrame).switchTo().frame(ChildFrame);
			log.info(("Navigated to innerframe with id " + ChildFrame + "which is present on frame with id"
					+ ParentFrame));
		} catch (NoSuchFrameException e) {
			log.info(("Unable to locate frame with id " + ParentFrame + " or " + ChildFrame + e.getStackTrace()));
			reportFail("Unable to locate frame with id " + ParentFrame + " or " + ChildFrame + e.getStackTrace());
		} catch (Exception e) {
			log.info(("Unable to navigate to innerframe with id " + ChildFrame + "which is present on frame with id"
					+ ParentFrame + e.getStackTrace()));
			reportFail("Unable to navigate to innerframe with id " + ChildFrame + "which is present on frame with id"
					+ ParentFrame + e.getStackTrace());
		}
	}

	public void switchtoDefaultFrame() {
		try {
			driver.switchTo().defaultContent();
			log.info(("Navigated back to webpage from frame"));
		} catch (Exception e) {
			log.info(("unable to navigate back to main webpage from frame" + e.getStackTrace()));
			Reporter.log("unable to navigate back to main webpage from frame" + e.getStackTrace());
		}
	}

	// quit driver

	public void quitDriver(WebDriver driver) {

		try {
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeDriver(WebDriver driver) {

		try {
			driver.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkCheckBox(WebElement we) {
		if (!we.isSelected()) {
			we.click();
		}
	}

	public void uncheckCheckBox(WebElement we) {
		if (we.isSelected()) {
			we.click();
		}
	}

	public WebElement getWebElement(String locator, boolean... optional) {
		try {
			String locatorFile = getLocatorPath();
			we = driver.findElement(findBy(locatorFile, locator));	
		} catch (Exception e) {
			log.info(e.getMessage());;
			if ((optional != null) && (optional[0] != true)) {
				log.info("Locator not found " + locator);
			} else {
				throwElementNotPresentException(e, locator, optional[0]);
			}
		}
		highlightElement(we);
		return we;
	}

	public String getWebElementText(String locator) {
		return getWebElement(locator).getText();
	}

	public void throwElementNotPresentException(Exception e, String locator, boolean optional) {
		if (optional == true) {
			log.info("Locator " + locator + "was not found however this step was optional");
			reportFail("Locator " + locator + "was not found however this step was optional");
		}
		reportFail("Locator " + locator + "was not found ");
		throw new NoSuchElementException("Webelement not found with locator " + locator + e.getMessage());

	}

	public WebElement getWebElement(String locatorFilePath, String locator, boolean... optional) {
		try {
			we = driver.findElement(findBy(locatorFilePath, locator));
			highlightElement(we);
		} catch (Exception e) {
			e.printStackTrace();
			if (optional[0] != true) {
				throwElementNotPresentException(e, locator, optional[0]);
			}
		}

		return we;
	}

	public List<WebElement> getWebElements(String locator, boolean... optional) {
		List<WebElement> we = null;
		try {
			String locatorFile = getLocatorPath();
			we = driver.findElements(findBy(locatorFile, locator));
		} catch (Exception e) {
			e.printStackTrace();
			if (optional[0] != true) {
				throwElementNotPresentException(e, locator, optional[0]);
			}
		}

		return we;
	}

	public String getLocatorPath() {
		return UiConstants.LOCATORS_PATH + "/" + this.getClass().getSimpleName() + ".properties";
	}

	protected void reportLogScreenshot(File file, String priority) {
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		String absolute = file.getName();
		String filePath = System.getProperty("user.dir") + "/src/test/resources/snapshots/"
				+ AbstractTestBase.snapshotfolder + "/" + absolute;
		if (saveAtS3.equals("true")) {
			filePath = AbstractTestBase.S3endpointUrl + AbstractTestBase.snapshotfolder + "/" + absolute;
		}

		Reporter.log("<tr><td></td><td></td><td><font size=\"1\" face=\"Comic sans MS\" color=\"green\"><a href=\""
				+ filePath + "\"><p align=\"left\">" + priority + "           " + new Date() + "</p>");
		Reporter.log("<p><img width=\"16\" src=\"" + filePath + "\" alt=\"executed on" + new Date()
				+ "\"/></p></a></font></td>");
	}

	public Set<Cookie> getCookies(String cookieName) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			log.info(e.getMessage());
		}
		Set<Cookie> cookies = driver.manage().getCookies();
		return cookies;
	}

	public String getCookieValue(String cookieName) {
		return driver.manage().getCookieNamed(cookieName).getValue();
	}

	public static void reportSuccess(String str) {
		Reporter.log("<tr><td></td><td></td><td></td><td><font size=\"2\" face=\"Comic sans MS\" color=\"green\"><b>"
				+ str + "</b></font></td>");
		log.info(str);
	}

	public static void reportFail(String str) {
		Reporter.log("<tr><td></td><td></td><td></td><td><font size=\"2\" face=\"Comic sans MS\" color=\"red\"><b>"
				+ str + "</b></font></td>");
		log.info(str);
	}
	
	
}
