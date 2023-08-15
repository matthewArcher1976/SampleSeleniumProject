package tests;

import helpers.Drivers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginModalPage;
import pages.PageHeaderPage;

@SuppressWarnings("DefaultAnnotationParam")
public class LoginFaceBookTest {

    WebDriver driver = Drivers.ChromeDriver();

    LoginModalPage modal = new LoginModalPage(driver);
    PageHeaderPage header = new PageHeaderPage(driver);

    @BeforeTest
    @Parameters({"url"})
    public void login(@Optional("https://qa.chive-testing.com") String url) {
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
    @Parameters({"facebookEmail", "password"})
    public void FaceBookLoginValid(@Optional("matt.archer@chivemediagroup.com") String facebookEmail, @Optional("Chive1234") String password) {
        header.loginBtn().click();
        modal.loginFacebook().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("Facebook"));
        modal.facebookEmail().sendKeys(facebookEmail);
        modal.facebookPassword().sendKeys(password);
        modal.facebookLoginBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(1));
        Assert.assertTrue(header.userMenu().isDisplayed(), "FaceBookLoginValid - User is not logged in");
    }
}