package com.inspire.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi
 *
 */
public class PropertyUtility {
	public static Logger log = MasterLogger.getInstance();

	public static Properties readProperties(Properties prop, File f) {
		try {
			FileInputStream fis = new FileInputStream(f);
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static Properties readPropertiesData(String propPathLocation) {
		File dir = new File(propPathLocation);
		Properties props = new Properties();
		File[] files = dir.listFiles((getFileNameFilter(".properties")));
		for (File f : files) {
			PropertyUtility.readProperties(props, f);
		}
		return props;
	}

	public static Properties readProperty(String propPathLocation) {
		File dir = new File(propPathLocation);
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(dir);
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static Properties readProperty(InputStream ins) {
		Properties prop = new Properties();
		try {
			prop.load(ins);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static FilenameFilter getFileNameFilter(final String str) {
		FilenameFilter fileFilter = new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				if (arg1.contains(str)) {
					return true;
				}
				return false;
			}
		};
		return fileFilter;
	}

}
