package tests;

import helpers.Config;
import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import resources.TestConfig;

public class PrivateUserTest {
    private static TestConfig config;
    WebDriver driver = Drivers.ChromeSauce();
    Logins login = new Logins(driver);

    //************************** Setup ******************************************

    @BeforeTest
    public static void configs() throws Exception {
        config = Config.getConfig();
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
