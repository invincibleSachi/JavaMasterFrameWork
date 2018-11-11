package com.inspire.interfaces;

import com.inspire.utils.EmailUtility;

/**
 * @author sachi
 *
 */
public interface EmailFilterConditions extends ExpectedConditions {
	boolean getEmailFilterConditions(EmailUtility e,String emailSubject);
}
