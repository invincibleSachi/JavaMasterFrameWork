package com.inspire.abstest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.inspire.abstestbase.AbstractTestBase;
import com.inspire.constants.UtilConstants;
import com.inspire.interfaces.ExpectedDBConditions;
import com.inspire.ui.constants.UiConstants;
import com.inspire.ui.utils.WebDriverCapabilities;
import com.inspire.utils.AWSutils;
import com.inspire.utils.CSVUtils;
import com.inspire.utils.FileIOUtility;
import com.inspire.utils.PostgresqlUtil;
import com.inspire.utils.ProcessBuilderUtility;
import com.inspire.utils.PropertyFileUtils;
import com.inspire.utils.TestDataGenerator;
import com.sun.jersey.api.client.ClientResponse;

public abstract class AbstractTest extends AbstractTestBase {

	public static PostgresqlUtil pgutilg = null;
	public static PostgresqlUtil update = null;
	public static String browser = null;
	public static String envoir = null;
	public static String sessionId = null;
	public static boolean isrunOnRemote = false;
	public static String testResultsFolder = null;

	@Parameters({ "env", "browser" })
	@BeforeSuite(alwaysRun = true)
	public void beforesuite(@Optional("qa") String env, @Optional("chrome") String browser) {
		printBanner("TEST SUITE STARTED");
		
		setup(env,browser);
	}

	private void printBanner(String s) {
		log.info("========================================= " + s + " ================================");

	}
	
	public void setup(String env, String browser) {
		envoir = env;
		log.info("before env->" + env);
		log.info("before browser->" + browser);
		if (env.equals("") || env.equals(null)) {
			env = System.getProperty("env");
			log.info("before env inside" + env);
		}
		loadEnv(env);
		setReportNG();
		csv = new CSVUtils("./testcaseList.csv");
		if (PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "createnewsnapshsotfolder").equals("true")) {
			log.info("creating snapshot folder ");
			if (snapshotfolder != null && !snapshotfolder.isEmpty()) {
				snapshotfolder = getfolderStr();
			}
			testResultsFolder = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "jenkinsTestResultsFolder");
			File directory = new File(UiConstants.SNAPSHOT_PATH + "/" + snapshotfolder);

			if (!directory.exists()) {
				directory.mkdirs();
			}

		}
		if (saveAtS3.equals("true")) {
			// S3Utils.createSubFolderInSnapshot(snapshotfolder);
			File dirServer = new File(testResultsFolder + "/" + snapshotfolder);
			testResultsFolder = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "jenkinsTestResultsFolder");
			if (!dirServer.exists()) {
				dirServer.mkdirs();
			}
		} else {
			setExtentReport();
		}
		if (PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "deleteprevScreenshot").equals("true"))
			FileIOUtility.getAllFilesInFolder(UiConstants.SNAPSHOT_PATH);
		if (PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "deleteprevTestResults").equals("true"))
			clearPrevReports();

		update = new PostgresqlUtil(environment.get("dbCon"), environment.get("dbuserwrite"), environment.get("dbpwdwrite"));
		log.info("pgutil " + pgutilg);
		
		pgutilg = new PostgresqlUtil(environment.get("dbCon"), environment.get("dbuser"), environment.get("dbpwd"));
		log.info("pgutil " + pgutilg);
		if (pgutilg == null) {
			log.info("DB Connection failed first attempt");
			pgutilg = new PostgresqlUtil(environment.get("dbCon"), environment.get("dbuser"), environment.get("dbpwd"));
			if (pgutilg == null) {
				log.info("DB Connection failed second attempt");
				pgutilg = new PostgresqlUtil(environment.get("dbCon"), environment.get("dbuser"),
						environment.get("dbpwd"));
				if (pgutilg == null) {
					log.info("DB Connection failed Third attempt Aborting test....");
					Assert.fail("Aborting test due to db connection failed ...");
				}
			}

		}
		AbstractTest.browser = browser;
	}

	public ExpectedDBConditions getDBCondition = (String query) -> {
		String result = pgutilg.executeSelectQuery(query);
		log.info("SQL Query " + query + "result: -" + result + " is  :" + result);
		return result.isEmpty();
	};

	private String getfolderStr() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public static void loadEnv(String env) {
		getenv("environment.xml", "environment", "id", env);
	}

	public static WebDriver getDriver() {
		String remoteHub = null;
		String proxy = null;
		URL remoteHubUrl = null;
		String targetOS = null;
		DesiredCapabilities cap = null;
		isrunOnRemote = Boolean.valueOf(PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "runOnRemoteMachine"));
		log.info(isrunOnRemote);
		if (isrunOnRemote == true) {
			remoteHub = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "remoteHubIP");
			targetOS = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "targetOS");
			log.info(remoteHub);
			proxy = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "proxy");
			try {
				remoteHubUrl = new URL(remoteHub);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			WebDriverCapabilities webcap = new WebDriverCapabilities(browser, proxy);
			cap = webcap.getDesiredCapabilities(targetOS);
		}
		WebDriver driver = null;
		if (browser == null) {
			browser = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "defaultBrowser");
		}
		log.info("running on browser " + browser);

		log.info(browser);
		if (browser.equalsIgnoreCase("firefox")) {
			if (os.equalsIgnoreCase("linux")) {
				if (!isrunOnRemote && !FileIOUtility.isFileExists("./src/test/resources/drivers/geckodriver_linux")) {
					AWSutils.getFileFromS3("data/drivers", "geckodriver_linux", "./src/test/resources/drivers");
				}
				System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver_linux");
			} else if (os.startsWith("Windows")) {
				if (!isrunOnRemote && !FileIOUtility.isFileExists("./src/test/resources/drivers/geckodriver.exe")) {
					AWSutils.getFileFromS3("data/drivers", "geckodriver.exe", "./src/test/resources/drivers");
				}
				System.setProperty("webdriver.gecko.driver", "./src/test/resources/drivers/geckodriver.exe");
			} else if (os.startsWith("mac")) {
				if (!isrunOnRemote && !FileIOUtility.isFileExists("./src/test/resources/drivers/geckodriver_mac")) {
					AWSutils.getFileFromS3("data/drivers", "geckodriver_mac", "./src/test/resources/drivers");
				}
				System.setProperty("webdriver.gecko.driver", "./src/test/resources/drivers/geckodriver_mac");
			}
			if (isrunOnRemote == true) {
				log.info("starting gecko driver on server");
				driver = new RemoteWebDriver(remoteHubUrl, cap);
			} else {
				log.info("starting gecko driver on local");
				driver = new FirefoxDriver();
			}

		} else if (browser.equalsIgnoreCase("chrome")) {
			if (os.equalsIgnoreCase("linux")) {
				if (!isrunOnRemote && !FileIOUtility.isFileExists("./src/test/resources/drivers/chromedriver_linux")) {
					log.info("driver downloading");
					AWSutils.getFileFromS3("data/drivers", "chromedriver_linux", "./src/test/resources/drivers");
				}
				System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver_linux");
			} else if (os.startsWith("Windows")) {
				if (!isrunOnRemote && !FileIOUtility.isFileExists("./src/test/resources/drivers/chromedriver.exe")) {
					AWSutils.getFileFromS3("data/drivers", "chromedriver.exe", "./src/test/resources/drivers");
				}
				System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
			} else if (os.startsWith("mac")) {
				if (!isrunOnRemote && !FileIOUtility.isFileExists("./src/test/resources/drivers/chromedriver_mac")) {
					AWSutils.getFileFromS3("data/drivers", "chromedriver_mac", "./src/test/resources/drivers");
				}
				System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver_mac");
			}
			if (isrunOnRemote == true) {
				log.info("starting chrome driver on server");
				log.info(remoteHubUrl.toString());
				log.info("cap " + cap.getBrowserName());
				driver = new RemoteWebDriver(remoteHubUrl, cap);
			} else {
				log.info("starting chrome driver on local");
				driver = new ChromeDriver();
			}
		} else if (browser.equalsIgnoreCase("edge")) {

			if (os.startsWith("Windows")) {
				if (!isrunOnRemote
						&& !FileIOUtility.isFileExists("./src/test/resources/drivers/MicrosoftWebDriver.exe")) {
					AWSutils.getFileFromS3("data/drivers", "MicrosoftWebDriver.exe", "./src/test/resources/drivers");
				}
				System.setProperty("webdriver.edge.driver", "./src/test/resources/drivers/MicrosoftWebDriver.exe");
			}
			if (isrunOnRemote == true) {
				log.info("starting edge driver on server");
				log.info(remoteHubUrl.toString());
				log.info("cap " + cap.getBrowserName());
				driver = new RemoteWebDriver(remoteHubUrl, cap);
			} else {
				log.info("starting chrome driver on local");
				driver = new EdgeDriver();
			}

		} else if (browser.equalsIgnoreCase("ie")) {
			if (!isrunOnRemote && !FileIOUtility.isFileExists("./src/test/resources/drivers/IEDriverServer.exe")) {
				AWSutils.getFileFromS3("data/drivers", "IEDriverServer.exe", "./src/test/resources/drivers");
			}
			System.setProperty("webdriver.ie.driver", "./src/test/resources/drivers/IEDriverServer.exe");
			if (isrunOnRemote == true) {
				log.info("starting ie driver on server");
				driver = new RemoteWebDriver(remoteHubUrl, cap);
			} else {
				log.info("starting ie driver on local");
				driver = new InternetExplorerDriver();
			}
		} else if (browser.equalsIgnoreCase("safari")) {
			log.info("starting safari driver on local");
			driver = new SafariDriver();
		} else
			driver = new FirefoxDriver();

		driver.manage().timeouts().pageLoadTimeout(UiConstants.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(UiConstants.SCRIPT_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(UiConstants.IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		return driver;

	}

	public void takeSnapShotonError(WebDriver driver) {
		String fileWithPath = UiConstants.SNAPSHOT_PATH + snapshotfolder + "/" + TestDataGenerator.createText()
				+ ".png";
		if (isrunOnRemote) {
			fileWithPath = testResultsFolder + snapshotfolder + "/" + TestDataGenerator.createText() + ".png";
		}
		TakesScreenshot scrShot = null;
		if (isrunOnRemote == false) {
			scrShot = ((TakesScreenshot) driver);

		} else {
			WebDriver augmentedDriver = new Augmenter().augment(driver);
			scrShot = ((TakesScreenshot) augmentedDriver);
		}
		File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
		File destFile = new File(fileWithPath);
		try {
			FileUtils.copyFile(srcFile, destFile);
			reportLogScreenshot(destFile, "onError");
		} catch (IOException e) {
			log.info(e.getMessage());
		}
	}
	
	

	protected String getCookie(WebDriver driver, String propertyName) {
		try {
			Thread.sleep(UiConstants.COOKIE_LOAD_WAIT_TIME);
		} catch (InterruptedException e) {
			log.info(e.getMessage());
		}
		return driver.manage().getCookieNamed(propertyName).getValue();
	}

	public void reportLogScreenshot(File file, String priority) {
		String absolute = file.getName();
		String filePath = System.getProperty("user.dir") + "/src/test/resources/snapshots/" + snapshotfolder + "/"
				+ absolute;
		if (isrunOnRemote) {
			filePath = testResultsFolder + snapshotfolder + "/" + absolute;
		}
		Reporter.log(
				"<a href=\"" + filePath + "\"><p align=\"left\">" + priority + " screenshot at " + new Date() + "</p>");
		Reporter.log("<p><img width=\"256\" src=\"" + filePath + "\" alt=\"screenshot at " + new Date()
				+ "\"/></p></a><br />");
		try {
			test.addScreenCaptureFromPath(filePath);
		} catch (IOException e) {
			log.info(e.getMessage());
		}
	}


	public void imbedResponseinReport(ClientResponse response, String res) {
		if (response == null) {
			return;
		}
		log.info("response " + res);
		String name = TestDataGenerator.createText() + ".json";
		String filename = System.getProperty("user.dir") + "/src/test/resources/snapshots/" + snapshotfolder + "/"
				+ name;
		if (saveAtS3.equals("true")) {
			filename = testResultsFolder + snapshotfolder + "/" + name;
		}
		FileWriter fw;
		try {
			fw = new FileWriter(new File(filename));
			fw.write(res);
			fw.close();
		} catch (IOException e) {
			reportLog("exception in writing response json to file", e.getMessage());
		}
		String size = Long.toString(FileIOUtility.getFileSize(filename));
		attributes.setAttribute("OUTPUT", getPublicUrl(filename));
		// log.info(filename);
		Reporter.log("<tr><td></td><td></td><td><font size=\"1\" face=\"Comic sans MS\" color=\"blue\"><a href="
				+ filename + "><p style='color:blue;'><strong>Response file/json (size: " + size
				+ " bytes)</strong></p> </a></font></td>");
		test.log(Status.INFO, "<font size=\"2\" face=\"Comic sans MS\" color=\"blue\"><a href=" + filename
				+ "><p style='color:blue;'><strong>Response file/json (size: " + size + " bytes)</strong></p>");
	}

	public void imbedRequestinReport(String src) {
		if (!saveAtS3.equals("true") && src.length()<5000) {
			log.info("request " + src);
		}
		String name = TestDataGenerator.createText() + ".json";
		String filename = System.getProperty("user.dir") + "/src/test/resources/snapshots/" + snapshotfolder + "/"
				+ name;
		FileWriter fw;
		if (saveAtS3.equals("true")) {
			filename = testResultsFolder + snapshotfolder + "/" + name;
		}
		try {
			fw = new FileWriter(filename);
			fw.write(src);
			fw.close();
		} catch (IOException e) {
			reportLog("exception in writing response json to file", e.getMessage());
		}
		String size = Long.toString(FileIOUtility.getFileSize(filename));
		attributes.setAttribute("INPUT", getPublicUrl(filename));

		Reporter.log("<tr><td></td><td></td><td><font size=\"1\" face=\"Comic sans MS\" color=\"blue\"><a href="
				+ filename + "><p style='color:blue;'><strong>Request file/json (size: " + size
				+ " bytes)</strong></p> </a></font></td>");
		test.log(Status.INFO, "<font size=\"2\" face=\"Comic sans MS\" color=\"blue\"><a href=" + filename
				+ "><p style='color:blue;'><strong>Request file/json (size: " + size + " bytes)</strong></p>");
	}

	public void pieChartReport(int pass, int fail, int skip) {
		// Creating a simple pie chart with
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("PASS", pass);
		pieDataset.setValue("FAIL", fail);
		pieDataset.setValue("N/A", skip);

		JFreeChart piechart = ChartFactory.createPieChart3D("Test Case Execution Status", pieDataset, true, true,
				false);

		try {
			ChartUtilities.saveChartAsJPEG(new File(UiConstants.SNAPSHOT_PATH + snapshotfolder + "/" + "chart.jpeg"),
					piechart, 200, 200);
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public static void clearPrevReports() {
		File snapshotdir = new File(UtilConstants.TEST_REPORT);
		for (File file : snapshotdir.listFiles()) {
			if (file.exists())
				file.delete();
		}
	}

	public void waitThread(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			log.info("Thread.sleep interrupted");
		}
	}

	public String getTimeZoneString() {
		String timeZone = "GMT";
		if (envoir.equals("dev")) {
			timeZone += "+530";
		}
		return timeZone;
	}

	@SuppressWarnings("deprecation")
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method, ITestContext context) {
		Test testngTest = method.getAnnotation(Test.class);
		test = extent.createTest(method.getName());
		test.log(Status.INFO,
				MarkupHelper.createLabel("ClassName: " + this.getClass().getSimpleName(), ExtentColor.RED));
		test.log(Status.INFO, MarkupHelper.createLabel("Suite Name: " + context.getSuite().getName(), ExtentColor.RED));
		test.log(Status.INFO,
				MarkupHelper.createLabel("Test Description: " + testngTest.description(), ExtentColor.ORANGE));
		if (testngTest.parameters().length > 0) {
			test.log(Status.INFO, MarkupHelper.createLabel("DataProvider: " + testngTest.dataProvider()
					+ "$$ Parameters: " + Arrays.toString(testngTest.parameters()), ExtentColor.BROWN));
		}
		if (testngTest.dependsOnMethods().length > 0) {
			test.log(Status.INFO, MarkupHelper.createLabel(
					"dependsOnMethods: " + Arrays.toString(testngTest.dependsOnMethods()), ExtentColor.BROWN));
		}
		if (testngTest.dependsOnGroups().length > 0) {
			test.log(Status.INFO, MarkupHelper.createLabel(
					"dependsOnGroups: " + Arrays.toString(testngTest.dependsOnGroups()), ExtentColor.BROWN));
		}
	}
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		long startTime=TestDataGenerator.getepochtime(0);
		attributes.setAttribute("startTime", startTime);
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE)
			test.fail(result.getThrowable());
		else if (result.getStatus() == ITestResult.SKIP)
			test.skip(result.getThrowable());
		else
			test.pass("Test passed");
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {

		if (saveAtS3.equals("true")) {
			// AWSutils.uploadAllFiles(UiConstants.SNAPSHOT_PATH, snapshotfolder);
			// cleaning previous run results
			String cmd = "rsync -r " + testResultsFolder + snapshotfolder + "/ " + testResultsFolder + snapshotfolder
					+ "/";

			if (ProcessBuilderUtility.executeComand(cmd) == 0) {
				log.info("test results has been copied");
			}
			// pgutil.closeDBConnection();
			printBanner("TEST SUITE FINISHED");
		}
		
	}
}
