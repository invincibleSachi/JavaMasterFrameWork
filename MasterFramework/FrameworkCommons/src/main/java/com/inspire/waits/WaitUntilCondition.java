package com.inspire.waits;

import org.apache.log4j.Logger;
import com.inspire.abstestbase.MasterLogger;
import com.inspire.interfaces.EmailConditions;
import com.inspire.interfaces.EmailFilterConditions;
import com.inspire.interfaces.ExpectedDBConditions;
import com.inspire.utils.EmailUtility;

/**
 * @author sachi
 *
 */
public class WaitUntilCondition {

	private int start = 5000;
	private int maxTime = 60000;
	private int polling = 5000;

	Logger log = MasterLogger.getInstance();

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public int getPolling() {
		return polling;
	}

	public void setPolling(int polling) {
		this.polling = polling;
	}

	public void waitUntilCondition(ExpectedDBConditions condition, String query) {
		boolean b = condition.getDBConditions(query);
		for (int i = start; i < maxTime && (b == true); i = i + polling) {
			try {
				b = condition.getDBConditions(query);
				Thread.sleep(polling);
				log.info("polling done to get db conditions " + i);
			} catch (InterruptedException e) {
				log.info("Thread.sleep interupted");
			}
		}
	}

	public void waitUntilCondition(EmailConditions condition, EmailUtility eu) {
		boolean b = condition.getEmailConditions(eu);
		for (int i = start; i < maxTime && (b == true); i = i + polling) {
			try {
				b = condition.getEmailConditions(eu);
				Thread.sleep(polling);
				log.info("polling done to get Email conditions " + i);
			} catch (InterruptedException e) {
				log.info("Thread.sleep interupted");
			}
		}
	}

	public void waitUntilCondition(EmailFilterConditions verifyNewEmailArrived, EmailUtility emailUtil,String emailSubject) {
		boolean b = verifyNewEmailArrived.getEmailFilterConditions(emailUtil, emailSubject);
		for (int i = start; i < maxTime && (b == true); i = i + polling) {
			try {
				b = verifyNewEmailArrived.getEmailFilterConditions(emailUtil, emailSubject);
				Thread.sleep(polling);
				log.info("polling done to get Email conditions " + i);
			} catch (InterruptedException e) {
				log.info("Thread.sleep interupted");
			}
		}
		
	}

}
