package com.inspire.interfaces;

/**
 * @author sachi
 *
 */
public interface ExpectedDBConditions extends ExpectedConditions {
	boolean getDBConditions(String query);
}
