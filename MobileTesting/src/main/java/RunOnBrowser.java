import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;

public class RunOnBrowser {
	
	WebDriver driver;
	DesiredCapabilities capbility;
	
	@BeforeClass
	public void setup(){
		capbility=new DesiredCapabilities();
		capbility.setCapability("browserName", "Chrome");
		capbility.setCapability("platformVersion", "4.4.2");
		capbility.setCapability("deviceName", "59QKMREAYS8TP7DE");
		//get device name from adb devices command
		capbility.setCapability("platformName", "Android");
		capbility.setCapability("device", "Android");
	}
	
	@Test
	public void runtest() throws MalformedURLException{
		driver =new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),capbility);
		driver.manage().timeouts().implicitlyWait(20l, TimeUnit.SECONDS);
		driver.get("https://www.google.co.in/");
		driver.findElement(By.name("q")).sendKeys("appium");
		driver.findElement(By.name("btnG")).click();
		
	}

}
