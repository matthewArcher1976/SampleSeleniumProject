package tests;

import helpers.Logins;
import helpers.PrettyAsserts;
import helpers.Waiter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import resources.Config;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

public class LoginProblemUsersTest {

    WebDriver driver;
    Logins login;
    PageHeaderPage header;
    Actions action;
    private static TestConfig config;

    //*********************** Setup *********************************

    @BeforeClass
    public void setConfig() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        action = new Actions(driver);
        header = new PageHeaderPage(driver);
        login = new Logins(driver);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void Natalie() throws InterruptedException {
        login.unpaidLogin(config.natalie, config.nataliePassword);
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(header.loginSuccess()), "Natalie may not have logged in successfully");
        header.userMenu().click();
        action.moveToElement(header.signoutBtn()).click().perform();
        Thread.sleep(2000);//yes
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(header.loginSuccess()));
    }

    @Test
    public void TooManyBlocked() throws InterruptedException {
        login.unpaidLogin(config.tooManyBlockedEmail, config.password);
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(header.loginSuccess()), "User with a lot of blocked users may not have logged in successfully");
        header.userMenu().click();//This logout does not work when I put it in an @Aftermethod for some reason
        action.moveToElement(header.signoutBtn()).click().perform();
        Thread.sleep(2000);
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(header.loginSuccess()));
    }

    @Test
    public void TooManyFollowed() throws InterruptedException {
        Thread.sleep(3000);
        login.unpaidLogin(config.TooManyFollowedEmail, config.password);
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(header.loginSuccess()), "User with a lot of followed users may not have logged in successfully");
        header.userMenu().click();
        action.moveToElement(header.signoutBtn()).click().perform();
        Thread.sleep(2000);
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(header.loginSuccess()));
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();

    }
}
