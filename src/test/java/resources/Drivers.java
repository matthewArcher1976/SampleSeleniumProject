package resources;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v115.network.Network;
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

	public static WebDriver ChromeDriver3g() {
		ChromeOptions co = new ChromeOptions();
		co.addArguments("--remote-allow-origins=*");
		co.addArguments("--disable-blink-features=AutomationControlled");
		ChromeDriver driver = new ChromeDriver(co);
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
		Map<String, Object> params = new HashMap<>();
		params.put("offline", false);
		params.put("latency", 100);
		params.put("download_throughput", 750 * 1024);
		params.put("upload_throughput", 200 * 1024);
		try {
			driver.executeCdpCommand("Network.emulateNetworkConditions", params);
		} catch (Exception e) {
			System.out.println("Could not emulate network conditions: " + e.getMessage());
		}
		return driver;
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
