package com.inspire.abstest;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.IExecutionListener;

import com.inspire.abstestbase.MasterLogger;
import com.inspire.bdd.util.GenerateReports;
import com.inspire.utils.FileIOUtility;

public class BddTestNGExecutionListener extends AbstractTest implements IExecutionListener {
	public static Logger log = MasterLogger.getInstance();

	@Override
	public void onExecutionStart() {
		log.info("BDD TestNG is staring the execution");
	}

	@Override
	public void onExecutionFinish() {
		log.info("Generating the Masterthought Report");
		List<String> suiteList = FileIOUtility.getAllFilesInFolder("./target/BDDsuites/");
		log.info(Arrays.toString(suiteList.toArray()));
		String projectName = "inspire BDD Testing Framework";
		GenerateReports.GenerateMasterthoughtReport("target", projectName, suiteList);
		log.info("TestNG has finished, the execution");
	}

}
