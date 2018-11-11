package com.inspire.abstestbase;

import java.util.ArrayList;
import java.util.Arrays;

import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * @author sachi
 *
 */
public class TestListener extends AbstractTestBase implements ITestListener, ITest {

	@Override
	public void onFinish(ITestContext r) {
		reportLog("Test Suite " + r.getSuite().getName() + " finished on " + System.currentTimeMillis());
	}

	@Override
	public void onStart(ITestContext r) {
		reportLog("Test Suite " + r.getSuite().getName() + " Started on " + System.currentTimeMillis());
	}

	@Override
	public void onTestFailure(ITestResult r) {
		long executionTime=(r.getEndMillis()-r.getStartMillis())/1000;
		attributes.getAttribute("URI");
		reportFailure("Test case failed:- " + r.getName());
		reportFailure("Error stacktrace:- " + r.getThrowable().getMessage());
		String str = r.getTestClass().getName() + "," + Arrays.toString(r.getMethod().getGroups()).replaceAll(",", ";")
				+ "," + r.getName() + "," + r.getMethod().getDescription() + ","
				+ Arrays.toString(r.getParameters()).replaceAll(",", ";") + "," + attributes.getAttribute("URI") + ","
				+ attributes.getAttribute("INPUT") + "," + attributes.getAttribute("OUTPUT") + ","
				+ getTestStatus(r.getStatus()) + "," + attributes.getAttribute("DEFECTID") + ","
				+ attributes.getAttribute("DEFECTURL") + "," + attributes.getAttribute("DEFECTPRTY")+executionTime;
		csv.append2CSV(str.replaceAll("\"", ""));
		String stringTracerArray = getStringFromArrayList(attributes.getTracerIdList());
		String tracerdetails = r.getTestClass().getName() + "," + r.getName() + "," + stringTracerArray;
		if (attributes.getAttribute("DEFECTID") != null) {
			knownIssues.add(attributes.getAttribute("DEFECTID") + "," + attributes.getAttribute("DEFECTURL") + ","
					+ attributes.getAttribute("DEFECTPRTY"));
		}
		csvtracers.append2CSV(tracerdetails);
	}

	@Override
	public void onTestSkipped(ITestResult r) {
		long executionTime=(r.getEndMillis()-r.getStartMillis())/1000;
		reportSuccess("Test " + r.getName() + " Skipped");
		String str = r.getTestClass().getName() + "," + Arrays.toString(r.getMethod().getGroups()).replaceAll(",", ";")
				+ "," + r.getName() + "," + r.getMethod().getDescription() + ","
				+ Arrays.toString(r.getParameters()).replaceAll(",", ";") + "," + attributes.getAttribute("URI") + ","
				+ attributes.getAttribute("INPUT") + "," + attributes.getAttribute("OUTPUT") + ","
				+ getTestStatus(r.getStatus()) + ",,"+executionTime;
		csv.append2CSV(str.replaceAll("\"", ""));
	}

	@Override
	public void onTestStart(ITestResult r) {

	}

	@Override
	public void onTestSuccess(ITestResult r) {
		long executionTime=(r.getEndMillis()-r.getStartMillis())/1000;
		reportSuccess("Test " + r.getName() + " Passed");
		String str = r.getTestClass().getName() + "," + Arrays.toString(r.getMethod().getGroups()).replaceAll(",", ";")
				+ "," + r.getName() + "," + r.getMethod().getDescription() + ","
				+ Arrays.toString(r.getParameters()).replaceAll(",", ";") + "," + attributes.getAttribute("URI") + ","
				+ attributes.getAttribute("INPUT") + "," + attributes.getAttribute("OUTPUT") + ","
				+ getTestStatus(r.getStatus()) + ",,"+executionTime;
		csv.append2CSV(str.replaceAll("\"", ""));
		String stringTracerArray = getStringFromArrayList(attributes.getTracerIdList());

		String tracerdetails = r.getTestClass().getName() + "," + r.getName() + "," + stringTracerArray;
		csvtracers.append2CSV(tracerdetails);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
	}

	private String getStringFromArrayList(ArrayList<String> l) {
		String tmp = "";
		for (int i = 0; i < l.size(); i++) {
			tmp = tmp + "," + l.get(i);
		}
		return tmp;
	}

	private String getTestStatus(int status) {
		if (status == 1) {
			return "PASSED";
		} else if (status == 2) {
			return "FAILED";
		} else if (status == 3) {
			return "SKIPPED";
		} else
			return "STARTED";
	}

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
