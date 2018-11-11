package on.real.device;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;

public class AutomateApps {
	WebDriver driver;
	DesiredCapabilities capbility;
	
	@BeforeClass
	public void setup(){
		capbility=new DesiredCapabilities();
		capbility.setCapability("browserName", "");
		capbility.setCapability("platformVersion", "4.4.2");
		capbility.setCapability("deviceName", "59QKMREAYS8TP7DE");
		//get device name from adb devices command
		capbility.setCapability("platformName", "Android");
		capbility.setCapability("device", "Android");
		capbility.setCapability("appPackage", "com.goibibo");
		capbility.setCapability("appActivity", "com.goibibo.GoibiboSplashScreen");
		/*
		 * TO GET APPACTIVITY AND APPPACAGE ON UBUNTU
		 * adb shell pm list packages -f
		 * THE ABOVE COMMAND WILL LIST ALL THE APK PACKAGES PRESENT ON DEVICE CONNECTED TO USB
		 * YOU CAN USE e.g. adb shell pm list packages -f|grep 'goibibo' to get pacakge name for goibibo
		 * it will display info like
		 * package:/data/app/com.goibibo-1.apk=com.goibibo
		 * com.goibibo is appPackage name
		 * then to get AppActivity 
		 * adb pull /data/app/com.goibibo-1.apk
		 * then
		 * aapt dump badging com.goibibo-1.apk
		 * will display all info including launcher activity name
		 * launchable-activity: name='com.goibibo.GoibiboSplashScreen' 
		 * 
		 * ON WINDOWS AND MAC WE CAN IMPORT APK INSIDE APPIUM UI AND GET THESE INFO
		 * 
		 * 
		 */
		
	}
	
	@Test
	public void runtest() throws MalformedURLException, InterruptedException{
		driver =new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),capbility);
		driver.manage().timeouts().implicitlyWait(20l, TimeUnit.SECONDS);
		Thread.sleep(5000l);
		driver.findElement(By.id("com.goibibo:id/bus_icon")).click();
		//driver.findElement(By.id("com.goibibo:id/got_it")).click();
		driver.findElement(By.id("com.goibibo:id/bus_origin")).click();
		driver.findElement(By.id("com.goibibo:id/search_edit")).sendKeys("Delhi");
		driver.findElement(By.xpath("//android.widget.TextView[@text='Delhi, Delhi'")).click();
		driver.findElement(By.id("com.goibibo:id/search_edit")).click();
		driver.findElement(By.id("com.goibibo:id/search_edit")).sendKeys("Jaipur");
		driver.findElement(By.xpath("//android.widget.TextView[@text='Jaipur, Rajasthan'")).click();
		driver.findElement(By.id("com.goibibo:id/button_apply_filter")).click();
	}

}
