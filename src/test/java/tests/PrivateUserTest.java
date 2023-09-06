package tests;

import resources.Config;
import helpers.Logins;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import resources.TestConfig;

import static helpers.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class PrivateUserTest {
    private static TestConfig config;
    WebDriver driver;
    Logins login;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Test Cases ****************************

    //TODO - Test all the things related to private accounts

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
