package com.inspire.abstestbase;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.inspire.constants.UtilConstants;
import com.inspire.interfaces.ExpectedDBConditions;
import com.inspire.utils.CSVUtils;
import com.inspire.utils.PropertyFileUtils;
import com.inspire.waits.WaitUntilCondition;
import com.sun.jersey.api.client.ClientResponse;


/**
 * @author sachi
 *
 */
public abstract class AbstractTestBase {

	public static HashMap<String, String> environment = new HashMap<String, String>();
	public static Logger log = MasterLogger.getInstance();
	protected static String os = System.getProperty("os.name");
	public static String snapshotfolder = "";
	public static MyAttributes attributes = new MyAttributes();
	public static MyAttributes bddAttributes = new MyAttributes();
	public static String saveAtS3 = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "savesnapshotsAtS3");
	public static String S3Url = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "S3serverurl");
	public static String S3endpointUrl = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE,
			"jenkinsTestResultsFolder");
	private static final String JIRA_ADMIN_USERNAME = "admin";
	private static final String JIRA_ADMIN_PASSWORD = "admin";
	private static final String JIRA_URL = "https://inspire.atlassian.net/";
	public static CSVUtils csv = null;
	public static CSVUtils csvtracers = null;
	public static CSVUtils csvknownIssues = null;
	public static ExtentHtmlReporter htmlReporter = null;
	public static ExtentReports extent = null;
	public static ExtentTest test = null;
	public static HashSet<String> knownIssues = new HashSet<String>();

	public static void loadEnv(String env) {
		getenv("environment.xml", "environment", "id", env);

	}

	public static void getenv(String fileName, String tagName, String attribuateName, String attribuateValue) {
		NodeList nList = null;
		Node nNode = null;

		File inputFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
			XPath xPath = XPathFactory.newInstance().newXPath();

			doc.getDocumentElement().normalize();
			nList = doc.getElementsByTagName(tagName);
		} catch (ParserConfigurationException e) {
			log.info(e.getMessage());
		} catch (SAXException e) {
			log.info(e.getMessage());
		} catch (IOException e) {
			log.info(e.getMessage());
		}

		for (int temp = 0; temp < nList.getLength(); temp++) {
			nNode = nList.item(temp);
			String key = nNode.getAttributes().getNamedItem("id").getNodeValue();
			if (key.equals(attribuateValue)) {
				NodeList childnodes = nNode.getChildNodes();
				for (int j = 0; j < childnodes.getLength(); j++) {
					Node n = childnodes.item(j);
					environment.put(n.getNodeName().trim(), n.getTextContent().trim());
				}
			}
		}
	}

	public void reportLog(String label, String inputValue) {
		Reporter.log("<tr><td></td><td></td><td></td><td><font size=\"2\" face=\"Comic sans MS\" color=\"green\"><b>"
				+ label + ": " + inputValue + "</b></font></td>");
		if (label.equalsIgnoreCase("URI")) {
			attributes.setAttribute("URI", inputValue);
		}
		log.info(label + ": " + inputValue);
		test.log(Status.INFO, label + ": " + inputValue);

	}

	public void reportFailure(String inputValue) {
		log.info("test failed");
		Reporter.log("<tr><td></td><td></td><td></td><td><font size=\"2\" face=\"Comic sans MS\" color=\"red\"><b>"
				+ inputValue + "</b></font></td>");
		test.log(Status.FAIL, inputValue);
	}

	public void reportLog(String str) {
		log.info(str);
		Reporter.log("<tr><td></td><td></td><td></td><td><font size=\"2\" face=\"Comic sans MS\" color=\"green\"><b>"
				+ str + "</b></font></td>");
		test.log(Status.INFO, str);
	}

	public void imbedInputFileinReport(File file) {
		String filename = file.getAbsoluteFile() + "";
		log.info("input file : " + filename);
		Reporter.log("<tr><td></td><td></td><td><font size=\"1\" face=\"Comic sans MS\" color=\"blue\"><a href="
				+ filename + "><p style='color:blue;'><strong>Input file/json (size: " + file.length()
				+ " bytes)</strong></p> </a></font></td>");
		attributes.setAttribute("INPUT", getPublicUrl(filename));
		test.log(Status.INFO,
				"<font size=\"2\" face=\"Comic sans MS\" color=\"blue\"><a href=" + filename
						+ "><p style='color:blue;'><strong>Response file/json (size: " + file.length()
						+ " bytes)</strong></p> </a></font>");
	}

	protected Object getPublicUrl(String filename) {
		// TODO Auto-generated method stub
		return "/data/test/artifacts";
	}

	public void imbedResponseinReport(ClientResponse response, File file) {
		String filename = System.getProperty("user.dir") + file.getPath();
		log.info("output file : " + filename);
		List<String> tracerId = response.getHeaders().get("X-IDMS-Tracer");
		if (tracerId != null) {
			reportLog("TracerID", tracerId.toString());
		}
		//attributes.setAttribute("OUTPUT", getPublicUrl(filename));
		if (saveAtS3.equals("true")) {
			filename = S3endpointUrl + snapshotfolder + "/" + file.getName();
		}
		attributes.addTracerId(tracerId.toString());
		Reporter.log("<tr><td></td><td></td><td><font size=\"1\" face=\"Comic sans MS\" color=\"blue\"><a href="
				+ filename + "><p style='color:blue;'><strong>Response file/json (size: " + file.length()
				+ " bytes)</strong></p> </a></font></td>");
		test.log(Status.INFO,
				"<font size=\"2\" face=\"Comic sans MS\" color=\"blue\"><a href=" + filename
						+ "><p style='color:blue;'><strong>Response file/json (size: " + file.length()
						+ " bytes)</strong></p> </a></font>");
	}

	public void imbedUrl(String legend, String url) {
		Reporter.log("<tr><td></td><td></td><td><font size=\"1\" face=\"Comic sans MS\" color=\"blue\"><a href="
				+ legend + "><p style='color:blue;'><strong> " + url + " </strong></p> </a></font></td>");
		test.log(Status.INFO, "<font size=\"1\" face=\"Comic sans MS\" color=\"blue\"><a href=" + legend
				+ "><p style='color:blue;'><strong> " + url + " </strong></p> </a></font>");
	}

	public static void reportSuccess(String str) {
		log.info("test case passed");
		Reporter.log("<tr><td></td><td></td><td></td><td><font size=\"2\" face=\"Comic sans MS\" color=\"green\"><b>"
				+ str + "</b></font></td>");
		// test.log(Status.PASS, "test passed");
	}

	public static void reportFail(String str) {
		log.info("test case failed");
		Reporter.log("<tr><td></td><td></td><td></td><td><font size=\"2\" face=\"Comic sans MS\" color=\"red\"><b>"
				+ str + "</b></font></td>");
		test.log(Status.FAIL, "test failed");
	}

	public static void reporError(String str) {
		log.info("error reported " + str);
		Reporter.log("<tr><td></td><td></td><td></td><td><font size=\"2\" face=\"Comic sans MS\" color=\"red\"><b>"
				+ str + "</b></font></td>");
		test.log(Status.FATAL, "test error!!");
	}

	public static String reportExistingDefectInJira(String defectId) {
		String jiraurl = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "jiraPath") + defectId;
		log.info("test failed due to existing defect " + defectId);
		String defectPriority = getDefect(defectId).getPriority().getName();
		String defectStatus = getDefect(defectId).getStatus().getName();
		String reportedDate = getDefect(defectId).getCreationDate().toString().substring(0, 10);
		attributes.setAttribute("DEFECTID", defectId);
		attributes.setAttribute("DEFECTURL", jiraurl);
		attributes.setAttribute("DEFECTPRTY", defectPriority);
		if (defectStatus.equals("Done") || defectStatus.equals("Verified")) {
			Reporter.log(
					"<tr><td></td><td></td><td></td><td><marquee><font size=\"2\" face=\"Comic sans MS\" color=\"red\"><b><a href="
							+ jiraurl + "><p style='color:red;'><strong>Existing Defect: " + defectId + " (priority: "
							+ defectPriority + ", status: " + defectStatus + ", reportedOn: " + reportedDate + ")"
							+ "</strong></p> </a></b></font></marquee></td>");
			test.log(Status.WARNING,
					"<font size=\"6\" face=\"Comic sans MS\" color=\"red\"><b><a href=" + jiraurl
							+ "><p style='color:red;'><strong>Existing Defect (Status:- Closed): " + defectId
							+ " (priority: " + defectPriority + ", status: " + defectStatus + ", reportedOn: "
							+ reportedDate + ")" + "</strong></p> </a></b></font>");
		} else {
			Reporter.log(
					"<tr><td></td><td></td><td></td><td><font size=\"4\" face=\"Comic sans MS\" color=\"red\"><b><a href="
							+ jiraurl + "><p style='color:red;'><strong>Existing Defect : " + defectId + " (priority: "
							+ defectPriority + ", status: " + defectStatus + ", reportedOn: " + reportedDate + ")"
							+ "</strong></p> </a></b></font></td>");
			test.log(Status.WARNING,
					"<font size=\"2\" face=\"Comic sans MS\" color=\"red\"><b><a href=" + jiraurl
							+ "><p style='color:red;'><strong>Existing Defect: (Status:- Open)" + defectId
							+ " (priority: " + defectPriority + ", status: " + defectStatus + ", reportedOn: "
							+ reportedDate + ")" + "</strong></p> </a></b></font>");
		}
		Assert.fail("Test case failed due to existing defect " + defectId + " priority - " + defectPriority);

		return "";
	}

	public static void setReportNG() {
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		System.setProperty("org.uncommons.reportng.show-expected-exceptions", "true");
		System.setProperty("org.uncommons.reportng.title",
				"inspire Cloud Testing Framework: - Test Execution Report");
		System.setProperty("org.uncommons.reportng.velocity-log", "true");

	}

	public static void setExtentReport() {
		htmlReporter = new ExtentHtmlReporter("extentReport/ExtentReportTestNG.html");
		htmlReporter.setAppendExisting(false);
		htmlReporter.config().setDocumentTitle("inspire Cloud Test Automation Framework");
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setReportName("inspire Cloud Test Automation Framework");
		htmlReporter.setAppendExisting(true);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		test = extent.createTest("beforeSuite");

	}

	public static Issue getDefect(String defectId) {
		JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		Issue issue = null;
		try {
			URI uri = new URI(JIRA_URL);
			JiraRestClient client = factory.createWithBasicHttpAuthentication(uri, JIRA_ADMIN_USERNAME,
					JIRA_ADMIN_PASSWORD);
			Promise<Issue> promise = client.getIssueClient().getIssue(defectId);
			issue = promise.claim();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return issue;
	}

	public void waitUntilDBCondition(int start, int maxTime, int polling, ExpectedDBConditions condition,
			String query) {

		WaitUntilCondition wait = new WaitUntilCondition();
		wait.setMaxTime(maxTime);
		wait.setPolling(polling);
		wait.setStart(start);
		wait.waitUntilCondition(condition, query);
	}

}
