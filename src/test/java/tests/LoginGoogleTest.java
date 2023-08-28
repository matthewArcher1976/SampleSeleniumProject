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

public class LoginGoogleTest {

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

    @Test
    public void GoogleCreateAccount() {
        header.loginBtn().click();
        modal.loginGoogle().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("Google"));
        modal.googleCreateAccount().click();
        modal.googleFirstName().sendKeys("Matt");
        modal.googleLastName().sendKeys("Archer");
        modal.googleNameNext().click();
    }

    @Test(priority = 1)
    public void GoogleLoginValid() throws InterruptedException {
        header.loginBtn().click();
        modal.loginGoogle().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("Google"));
        modal.googleEmailInput().sendKeys(config.unpaidEmail);
        modal.googleEmailNext().click();
        Thread.sleep(5000);//I need this even with the wait for not staleness for some reason
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("pwd"));
        helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.stalenessOf(modal.googlePasswordInput())));
        helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(modal.googlePasswordInput()));
        modal.googlePasswordInput().sendKeys(config.password);
        modal.googlePasswordNext().click();
        Assert.assertTrue(header.userMenu().isDisplayed(), "GoogleLoginValid - User is not logged in");

    }
    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}