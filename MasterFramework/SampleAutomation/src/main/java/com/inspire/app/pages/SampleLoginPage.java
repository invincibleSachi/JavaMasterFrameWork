package com.inspire.app.pages;

import org.openqa.selenium.WebDriver;

import com.inspire.ui.automation.AbstractPage;


/**
 
 *
 */
public class SampleLoginPage extends AbstractPage {

	WebDriver driver;

	public SampleLoginPage( WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public void browseURL(String url) {
		driver.get(url);
	}

	public MainPage login(String user, String passwd) {
		type(getWebElement("username"), user);
		type(getWebElement("password"), passwd);
		click(getWebElement("submitbtn"));
		reportSuccess("Login successful");
		return new MainPage(driver);
	}

	public void verifyURLsAlive() {

	}

	public void getUrl(String string) {
		// TODO Auto-generated method stub
		
	}

}
