package tests;

import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.ProfilePage;

public class VerifiedUsersTest {

    WebDriver driver = Drivers.ChromeDriver();
    ProfilePage profilePage = new ProfilePage(driver);
    EditProfilePage profile = new EditProfilePage(driver);
    Logins login = new Logins(driver);

    @BeforeTest
    @Parameters({"verifiedEmail", "password", "url"})
    public void login(@Optional("thechivetest+verified@gmail.com") String unpaidEmail, @Optional("Chive1234") String password, @Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
        driver.get(url);
        login.unpaidLogin(unpaidEmail, password, driver);
        Thread.sleep(1000);
    }

    @BeforeMethod
    @Parameters({"url"})
    public void setDriver(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        profile.userMenu().click();
        profile.yourProfileBtn().click();
    }

    @AfterClass
    public void TearDown() {
        driver.close();
    }

    //************************** Begin Tests ********************************************

    @Test
    public void VerifiedCheck() {
        Assert.assertTrue(profile.verifiedCheck().isDisplayed(), "Did not find checkmark by username");
    }

    @Test
    public void WebSiteInputDisplays() {
        profile.yourProfileBtn().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        Assert.assertTrue(profile.websiteInput().isDisplayed(), "Did not find website input");
    }

    @Test
    public void WebSiteLink() {
        profile.yourProfileBtn().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.websiteInput().clear();
        profile.websiteInput().sendKeys("https://www.google.com");
        profile.saveProfileBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Assert.assertTrue(profilePage.websiteIcon().isDisplayed(), "Did not find website icon");
        profilePage.websiteIcon().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getTitle().contains("Google"), "Website tab did not open");
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

}
