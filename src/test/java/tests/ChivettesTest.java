package tests;

import helpers.Config;
import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.ProfilePage;
import resources.TestConfig;

@SuppressWarnings("TestFailedLine")
public class ChivettesTest {

    WebDriver driver = Drivers.ChromeDriver();
    ProfilePage profilePage = new ProfilePage(driver);
    EditProfilePage profile = new EditProfilePage(driver);
    Logins login = new Logins(driver);
    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public static void configs() throws Exception {
        config = Config.getConfig();
    }
    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
        profile.userMenu().click();
        profile.yourProfileBtn().click();
    }

    //************************** Begin Tests ********************************************

    @Test
    public void ChivetteIconTest() {
        Assert.assertTrue(profilePage.chivetteIcon().isDisplayed(), "Did not find Chivette icon by username");
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

    //************************* Teardown ***************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}
