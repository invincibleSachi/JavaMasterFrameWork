package com.inspire.app.pages;
import org.openqa.selenium.WebDriver;

import com.inspire.ui.automation.AbstractPage;
public class MainPage extends AbstractPage{
	
	WebDriver driver;

	public MainPage( WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public void browseURL(String url) {
		driver.get(url);
	}

}
