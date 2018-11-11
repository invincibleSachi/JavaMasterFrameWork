package com.inspire.interfaces;

import com.inspire.utils.EmailUtility;

/**
 * @author sachi
 *
 */
public interface EmailConditions extends ExpectedConditions {
	boolean getEmailConditions(EmailUtility e);
}
