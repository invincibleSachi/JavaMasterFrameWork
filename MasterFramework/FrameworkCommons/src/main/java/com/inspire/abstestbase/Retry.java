package com.inspire.abstestbase;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.inspire.constants.UtilConstants;


/**
 * @author sachi
 *
 */
public class Retry extends AbstractTestBase implements IRetryAnalyzer {
	public static Logger log = MasterLogger.getInstance();
	private int retryCount = 0;
	private int maxRetryCount = UtilConstants.MAX_RETRY_COUNT;

	// Below method returns 'true' if the test method has to be retried else
	// 'false'
	// and it takes the 'Result' as parameter of the test method that just ran
	public boolean retry(ITestResult result) {
		boolean isKnownIssue = false;
		if (attributes != null) {
			isKnownIssue = (attributes.getAttribute("DEFECTID") != null);
		}
		if (retryCount < maxRetryCount && (result.getStatus() == 2) && (isKnownIssue == false)) {
			log.info("Retrying test " + result.getName() + " with status " + getResultStatusName(result.getStatus())
					+ " for the " + (retryCount + 1) + " time(s).");
			retryCount++;
			return true;
		}
		return false;
	}

	public String getResultStatusName(int status) {
		String resultName = null;
		if (status == 1)
			resultName = "SUCCESS";
		if (status == 2)
			resultName = "FAILURE";
		if (status == 3)
			resultName = "SKIP";
		return resultName;
	}

}
