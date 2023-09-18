package tests;

import resources.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginModalPage;
import pages.PageHeaderPage;
import resources.RetryAnalyzer;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
@Test(retryAnalyzer = RetryAnalyzer.class)

public class LoginFaceBookTest {

    WebDriver driver;
    LoginModalPage login;
    PageHeaderPage header;

    private static TestConfig config;

    //*********************** Setup *********************************
    @BeforeClass
    public void setConfig() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);

        header = new PageHeaderPage(driver);
        login = new LoginModalPage(driver);
      }

    @BeforeMethod
    public void setDriver(){
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test(priority = 99, enabled = false)//getting Zucced
    public void FaceBookLoginValid() {
        header.loginBtn().click();
        login.loginFacebook().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("Facebook"));
        login.facebookEmail().sendKeys(config.facebookEmail);
        login.facebookPassword().sendKeys(config.password);
        login.facebookLoginBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(1));
        Assert.assertTrue(header.userMenu().isDisplayed(), "FaceBookLoginValid - User is not logged in");
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}