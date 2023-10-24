package tests;

import helpers.Logins;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.PageHeaderPage;
import resources.Config;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class PrivateUserTest {
    private static TestConfig config;
    WebDriver driver;
    Logins login;
    PageHeaderPage header;
    EditProfilePage profile;


    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
        header = new PageHeaderPage(driver);
        profile = new EditProfilePage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.privateEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Test Cases ****************************

    @Test
    public void SetPrivate() {
        header.userMenu().click();
        header.settingsBtn().click();
        profile.accountTab().click();
        if (profile.makePrivateToggle().getAttribute("value").equals("false")) {
            profile.makePrivateToggle().click();
        } else if (!profile.makePrivateToggle().getAttribute("value").equals("false")
                && !profile.makePrivateToggle().getAttribute("value").equals("true")) {
            Assert.fail("Check the make private toggle");
        }
        login.logout();
        driver.get(config.url + config.privateUsername);


    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
