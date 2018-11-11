package com.inspire.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi
 *
 */
public class PropertyFileUtils {

	public static InputStream inputStream;
	public static Logger log = MasterLogger.getInstance();

	public static String getProperty(String propFileName, String propName) {

		Properties prop = new Properties();
		try {
			inputStream = new FileInputStream(new File(propFileName).getAbsolutePath());
			prop.load(inputStream);
		} catch (IOException e) {
			log.info("exception occurred while reading the property file " +e.getMessage());
		}

		return prop.getProperty(propName);

	}

	public static void updateProperty(String propFileName, String propertyKey, String propertyValue) {
		try {
			FileInputStream in = new FileInputStream(new File(propFileName).getAbsolutePath());
			Properties props = new Properties();
			props.load(in);
			in.close();

			FileOutputStream out = new FileOutputStream(propFileName);
			props.setProperty(propertyKey, propertyValue);
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			log.info("exception occurred while updating the property " +e.getMessage());
		}

	}

}
