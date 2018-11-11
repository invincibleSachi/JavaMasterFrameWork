package com.inspire.ui.automation;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;

public class InvalidLocator extends Exception {

	/**
	 * @author sachi
	 */
	public static Logger log = MasterLogger.getInstance();
	private static final long serialVersionUID = 3990909229327303440L;

	public InvalidLocator(String locator, String message) {
		super(message);
		log.info(message + "\n" + message);
	}

	public InvalidLocator(String message) {
		super(message);
		log.info(message);
	}

}
