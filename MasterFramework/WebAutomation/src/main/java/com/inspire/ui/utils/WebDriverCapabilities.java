package com.inspire.ui.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi
 *
 */
public class WebDriverCapabilities {
	String browser = null;
	String proxy = null;
	DesiredCapabilities cap = null;
	protected static String os = System.getProperty("os.name");
	public static Logger log = MasterLogger.getInstance();

	public WebDriverCapabilities(String browser, String proxy) {
		this.browser = browser;
		this.proxy = proxy;
	}

	private Proxy getProxy(String proxy) {
		org.openqa.selenium.Proxy p = null;
		if (proxy != null || !proxy.equals("")) {
			String PROXY = proxy;
			p = new org.openqa.selenium.Proxy();
			p.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
		}
		return p;

	}

	private DesiredCapabilities getBrowserCapabilities() {
		if (browser.equalsIgnoreCase("firefox")) {
			FirefoxProfile profile = new FirefoxProfile();
			cap = DesiredCapabilities.firefox();
			cap.setCapability(FirefoxDriver.PROFILE, profile);
			cap = DesiredCapabilities.firefox();
		} else if (browser.equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			cap = DesiredCapabilities.chrome();
			cap.setCapability(ChromeOptions.CAPABILITY, options);

		} else if (browser.equalsIgnoreCase("ie")) {
			cap = DesiredCapabilities.internetExplorer();
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		} else if (browser.equalsIgnoreCase("phantom")) {
			cap = DesiredCapabilities.htmlUnit();
		} else {
			FirefoxProfile profile = new FirefoxProfile();
			cap = DesiredCapabilities.firefox();
			cap.setCapability(FirefoxDriver.PROFILE, profile);
			cap = DesiredCapabilities.firefox();
		}
		return cap;
	}

	public DesiredCapabilities getDesiredCapabilities(String os) {
		DesiredCapabilities cap = getBrowserCapabilities();
		/*
		 * if(proxy!=null||!StringUtils.isEmpty(proxy)){
		 * cap.setCapability(CapabilityType.PROXY, getProxy(proxy));
		 *
		 * }
		 */
		if (os.equalsIgnoreCase("windows")) {
			cap.setPlatform(Platform.WINDOWS);
		}
		log.info("operating system " + os);
		if (os.equalsIgnoreCase("win7")) {
			cap.setPlatform(Platform.VISTA);
		}
		if (os.equalsIgnoreCase("win8")) {
			cap.setPlatform(Platform.WIN8);
		}
		if (os.equalsIgnoreCase("win8_1")) {
			cap.setPlatform(Platform.WIN8_1);
		}
		if (os.equalsIgnoreCase("win10")) {
			cap.setPlatform(Platform.WIN10);
		}
		if (os.equalsIgnoreCase("linux")) {
			cap.setPlatform(Platform.LINUX);
		}

		return cap;
	}
}
