package com.inspire.bdd.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

public class GenerateReports {
	public static void GenerateMasterthoughtReport(String reportDir, String suiteName, List<String> list) {
		try {
			File reportOutputDirectory = new File(reportDir);
			Configuration configuration = new Configuration(reportOutputDirectory, suiteName);
			configuration.setParallelTesting(true);
			configuration.setRunWithJenkins(false);
			ReportBuilder reportBuilder = new ReportBuilder(list, configuration);

			reportBuilder.generateReports();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*public static void GenerateReport(){
		File reportOutputDirectory = new File("target");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/cucumber.json");

        String jenkinsBasePath = "";
        String buildNumber = "1";
        String projectName = "cucumber-jvm";
        boolean skippedFails = true;
        boolean pendingFails = false;
        boolean undefinedFails = true;
        boolean missingFails = true;
        boolean runWithJenkins = false;
        boolean parallelTesting = false;

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        // optionally only if you need
        configuration.setStatusFlags(skippedFails, pendingFails, undefinedFails, missingFails);
        configuration.setParallelTesting(parallelTesting);
        configuration.setJenkinsBasePath(jenkinsBasePath);
        configuration.setRunWithJenkins(runWithJenkins);
        configuration.setBuildNumber(buildNumber);

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
	}*/
}
