package tests;

import resources.Config;
import helpers.Logins;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.EditProfilePage;

import resources.TestConfig;

import static resources.getDriverType.getDriver;
@Listeners(listeners.SauceLabsListener.class)
public class EditProfileAccountTest {

    WebDriver driver;
    EditProfilePage profile;
    Logins login;

    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);

        login = new Logins(driver);
        profile = new EditProfilePage(driver);
    }
    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }
    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
        profile.userMenu().click();
        profile.settingsBtn().click();
    }

    //************************** Begin Tests ********************************************
    //TODO All the test cases for the Account tab
    @Test
    public void UserName(){
        Assert.assertTrue(true);
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
