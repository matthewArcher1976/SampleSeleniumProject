package tests;

import helpers.Drivers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginModalPage;
import pages.PageHeaderPage;

@SuppressWarnings("DefaultAnnotationParam")
public class LoginGoogleTest {

    WebDriver driver = Drivers.ChromeDriver();
    LoginModalPage modal = new LoginModalPage(driver);
    PageHeaderPage header = new PageHeaderPage(driver);

    @BeforeTest
    @Parameters({"url"})
    public void login(@Optional("https:qa.chive-testing.com") String url) {
        driver.get(url);
    }

    @BeforeMethod
    @Parameters({"url"})
    public void setDriver(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
    }

    @AfterClass
    public void TearDown() {
        driver.close();
    }

    //************************** Begin Tests ********************************************

    @Test(enabled = true, priority = 99)
    @Parameters({"unpaidEmail", "password"})
    public void GoogleCreateAccount(@Optional("thechivetest@gmail.com") String unpaidEmail, @Optional("Chive1234") String password) throws InterruptedException {
        header.loginBtn().click();
        modal.loginGoogle().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("Google"));
        modal.googleCreateAccount().click();
        modal.googleFirstName().sendKeys("Matt");
        modal.googleLastName().sendKeys("Archer");
        modal.googleNameNext().click();
    }

    @Test(enabled = true, priority = 99)
    @Parameters({"unpaidEmail", "password"})
    public void GoogleLoginValid(@Optional("thechivetest@gmail.com") String unpaidEmail, @Optional("Chive1234") String password) throws InterruptedException {
        header.loginBtn().click();
        modal.loginGoogle().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("Google"));
        modal.googleEmailInput().sendKeys(unpaidEmail);
        modal.googleEmailNext().click();
        Thread.sleep(5000);//I need this even with the wait for not staleness for some reason
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("pwd"));
        helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.stalenessOf(modal.googlePasswordInput())));
        helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(modal.googlePasswordInput()));
        modal.googlePasswordInput().sendKeys(password);
        modal.googlePasswordNext().click();
        Assert.assertTrue(header.userMenu().isDisplayed(), "GoogleLoginValid - User is not logged in");

    }


}