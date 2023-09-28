package tests;

import helpers.Waiter;
import helpers.WindowUtil;
import resources.Config;
import helpers.Logins;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.ProfilePage;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class VerifiedUsersTest {

    WebDriver driver;
    ProfilePage profilePage;
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
        profilePage = new ProfilePage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.verifiedEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);

    }

    //************************** Begin Tests ********************************************

    @Test
    public void VerifiedCheck() {
        Assert.assertTrue(profile.verifiedCheck().isDisplayed(), "Did not find checkmark by username");
    }

    @Test
    public void WebSiteInputDisplays() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        Assert.assertTrue(profile.websiteInput().isDisplayed(), "Did not find website input");
    }

    @Test
    public void WebSiteLink() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.websiteInput().clear();
        profile.websiteInput().sendKeys("https://www.google.com");
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Assert.assertTrue(profilePage.websiteIcon().isDisplayed(), "Did not find website icon");
        profilePage.websiteIcon().click();
        WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getTitle().contains("Google"), "Website tab did not open");
        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }
    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}