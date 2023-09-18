package tests;

import helpers.Waiter;
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
public class LoginGoogleTest {
    
    private static TestConfig config;
    WebDriver driver;
    
    LoginModalPage loginModal;
    PageHeaderPage header;

    //*********************** Setup *********************************
    @BeforeTest
    public void setConfig() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);

        header = new PageHeaderPage(driver);
        loginModal = new LoginModalPage(driver);
    }

    @BeforeMethod
    public void setDriver(){
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void GoogleCreateAccount() {
        header.loginBtn().click();
        loginModal.loginGoogle().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("Google"));
        loginModal.googleCreateAccount().click();
        loginModal.googleFirstName().sendKeys("Matt");
        loginModal.googleLastName().sendKeys("Archer");
        loginModal.googleNameNext().click();
    }

    @Test(priority = 1)
    public void GoogleLoginValid() throws InterruptedException {
        header.loginBtn().click();
        loginModal.loginGoogle().click();
        Waiter.wait(driver).until(ExpectedConditions.titleContains("Google"));
        loginModal.googleEmailInput().sendKeys(config.unpaidEmail);
        loginModal.googleEmailNext().click();
        Thread.sleep(5000);//I need this even with the wait for not staleness for some reason
        Waiter.wait(driver).until(ExpectedConditions.urlContains("pwd"));
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.stalenessOf(loginModal.googlePasswordInput())));
        Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(loginModal.googlePasswordInput()));
        loginModal.googlePasswordInput().sendKeys(config.password);
        loginModal.googlePasswordNext().click();

        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.visibilityOf(header.userMenu())).isDisplayed(), "GoogleLoginValid - User is not logged in");

    }
    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}