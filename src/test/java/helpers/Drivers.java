package helpers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;

@SuppressWarnings({"unused", "MissingSerialAnnotation"})
public class Drivers {

	public static WebDriver ChromeDriver() {
		ChromeOptions co = new ChromeOptions();
		co.addArguments("--remote-allow-origins=*");//workaround a showstopping bug in ChromeDriver 112 
		co.addArguments("--disable-blink-features=AutomationControlled");//gets around Google's block of logins from WebDriver
		return new ChromeDriver(co);
	}
	
	public static WebDriver ChromeSauce() {
	    ChromeOptions browserOptions = new ChromeOptions();
	    browserOptions.setPlatformName("Windows 11");
	    browserOptions.setBrowserVersion("latest");

	    Map<String, Object> sauceOptions = new HashMap<>();
	    sauceOptions.put("username", "oauth-matt.archer-ff614");
	    sauceOptions.put("accessKey", "48c2e9ca-6c85-470e-a332-588e7e6fde98");
	    sauceOptions.put("build", "selenium-build-373YO");
	    sauceOptions.put("name", "iChive Tests");
		sauceOptions.put("screenResolution", "2560x1600");
	    
	    browserOptions.setCapability("sauce:options", sauceOptions);
	    browserOptions.addArguments("--disable-blink-features=AutomationControlled");
	    
	    try {
	        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
	        return new RemoteWebDriver(url, browserOptions);
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public static WebDriver ChromeInsecure() {
		ChromeOptions co = new ChromeOptions();
		co.addArguments("--remote-allow-origins=*");
		co.addArguments("--disable-web-security"); 
	    co.addArguments("--allow-running-insecure-content");
		return new ChromeDriver(co);
	}
	
	public static WebDriver ChromeMobile() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", 
                new HashMap<String, String>() {private static final long serialVersionUID = 1L;
				{
                    put("userAgent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
                }});
        chromeOptions.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().window().setSize(new Dimension(390, 844));
		return driver;
    }

	public static WebDriver SafariDriver() {
		WebDriver driver = new SafariDriver();
		driver.manage().window().setSize(new Dimension(1200, 1200));
		return driver;
	}
}
