package com.inspire.abstest;

import java.util.Date;

import org.apache.log4j.Logger;
import org.testng.IExecutionListener;

import com.inspire.abstestbase.MasterLogger;
import com.inspire.constants.UtilConstants;
import com.inspire.utils.CSVUtils;
import com.inspire.utils.FileIOUtility;
import com.inspire.utils.ProcessBuilderUtility;
import com.inspire.utils.PropertyFileUtils;
import com.inspire.utils.TestDataGenerator;

public class SuiteListener extends AbstractTest implements IExecutionListener {
	Logger log = MasterLogger.getInstance();
	static boolean firsttimeLog = false;
	public String saveServerLogs = "false";
	public String processLogs = "false";
	String startTime = null;

	private void printBanner(String s) {
		log.info("========================================= " + s + " ================================");

	}

	@Override
	public void onExecutionStart() {
		Date date = new Date();
		startTime = TestDataGenerator.getformatedDateinTimeZone(date, "yyyy-MM-dd HH:mm", "GMT");
		log.info("start time for logs ->" + startTime);
		String env = System.getProperty("env");
		envoir = env;
		log.info("before env inside " + env);
		if (saveServerLogs.equalsIgnoreCase("true")) {
			verifyMachinesAreUp();
			log.info("all machines are up for testing");
		}

		if (firsttimeLog == false) {
			log.info("setting up for first time ");
			FileIOUtility.deleteFileIfExists("extentReport/ExtentReportTestNG.html");
			setExtentReport();
			log.info("extent report set");
			printBanner("Test AUTOMATION FRAMEWORK LOG STARTS BELOW");

			csv = new CSVUtils("./testcaseList.csv");
			csvtracers = new CSVUtils("./extentReport/tracerIdsList.csv");
			csvknownIssues = new CSVUtils("./extentReport/knownIssues.csv");
			csv.createNewCSV("./testcaseList.csv");
			csvtracers.createNewCSV("./extentReport/tracerIdsList.csv");
			csvtracers.append2CSV("testClass,testCase,tracerIdsList,,,");
			csvknownIssues.append2CSV("ids,url,priority,,");
			csv.append2CSV(
					"TestName,TestGroups,TestCaseName,Description,Parameters,URI,INPUT,OUTPUT,Status,DefectId, DefectUrl,DefectPrty");
			firsttimeLog = true;
			log.info("set first time logging true");
			FileIOUtility.deleteAllFilesNSubfolders("./target/test-classes/api/");
			FileIOUtility.deleteAllFilesNSubfolders("./src/test/resources/snapshots");
		}
		saveServerLogs = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "saveserverlogs");
		processLogs = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "processServerLogsAfterRun");
		FileIOUtility.deleteAllFilesNSubfolders("./target/surefire-reports/html");
		FileIOUtility.deleteAllFilesNSubfolders("./target/surefire-reports/xml");
		FileIOUtility.createIfFolderNotExists("./target/surefire-reports/html");
		FileIOUtility.createIfFolderNotExists("./target/surefire-reports/xml");
	}

	@Override
	public void onExecutionFinish() {
		extent.flush();
		log.info(saveServerLogs);
		if (saveServerLogs.equalsIgnoreCase("true")) {
			log.info("saving logs at server ...");
		
		}

		if (saveServerLogs.equalsIgnoreCase("true")) {
			log.info("downloading server logs");
			log.info("deleting temp server logs");
		}
		if (processLogs.equalsIgnoreCase("true")) {
			log.info("processing log files");
			log.info(
					"Report generated for server Exceptions during test run:  ServerLogsExceptionReport.log verify serverlogs folder");
		}
		if (FileIOUtility.isFileExists("./testcaseList.csv") && (saveAtS3.equals("true"))) {
			try {
				FileIOUtility.deleteFileIfExists("./testExecutionSummary.html");
				ProcessBuilderUtility.executeComand("python testCaesReportGenerator.py");
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}
		FileIOUtility.createIfFolderNotExists("./target/surefire-reports/html");
		FileIOUtility.createIfFolderNotExists("./target/surefire-reports/xml");
		pgutilg.closeDBConnection();
		log.info("current dir---" + System.getProperty("user.dir"));
		FileIOUtility.deleteAllFilesNSubfolders("./target/test-classes/api/video/*");
		FileIOUtility.deleteAllFilesNSubfolders("./target/test-classes/api/*");
		for (String s : knownIssues) {
			csvknownIssues.append2CSV(s);
		}
		/*
		 * String cmd = "sudo rm -rf " + System.getProperty("user.dir") +
		 * "/src/test/resources/snapshots/*"; if
		 * (ProcessBuilderUtility.executeCommandTimeout5Minutes(cmd).exitValue() == 0) {
		 * log.info("snapshot contents deleted"); } else {
		 * log.info("snapshot folder not deleted"); }
		 */
		printBanner("TEST SUIT(s) FINISHED");
	}

	public void verifyMachinesAreUp() {
		
	}

}
