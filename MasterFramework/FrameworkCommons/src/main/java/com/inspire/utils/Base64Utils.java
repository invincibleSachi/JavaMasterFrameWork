package com.inspire.utils;

import java.nio.charset.Charset;
import java.util.Base64;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi
 *
 */
public class Base64Utils {
	Logger log = MasterLogger.getInstance();

	public static String encodeString(String str) {
		String encoded = Base64.getEncoder().encodeToString(str.getBytes());
		return encoded;
	}

	public static String decodeString(String encodedStr) {
		byte[] decodeBytes = Base64.getDecoder().decode(encodedStr.getBytes());
		return new String(decodeBytes, Charset.forName("UTF-8"));
	}
}
