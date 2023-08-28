package tests;

import helpers.Config;
import helpers.Drivers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginModalPage;
import pages.PageHeaderPage;
import resources.TestConfig;

public class LoginFaceBookTest {

    WebDriver driver = Drivers.ChromeDriver();

    LoginModalPage modal = new LoginModalPage(driver);
    PageHeaderPage header = new PageHeaderPage(driver);
    private static TestConfig config;

    //*********************** Setup *********************************
    @BeforeClass
    public void setConfig() throws Exception {
        config = Config.getConfig();
      }

    @BeforeMethod
    public void setDriver(){
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test(priority = 99)
    public void FaceBookLoginValid() {
        header.loginBtn().click();
        modal.loginFacebook().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("Facebook"));
        modal.facebookEmail().sendKeys(config.facebookEmail);
        modal.facebookPassword().sendKeys(config.password);
        modal.facebookLoginBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(1));
        Assert.assertTrue(header.userMenu().isDisplayed(), "FaceBookLoginValid - User is not logged in");
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}