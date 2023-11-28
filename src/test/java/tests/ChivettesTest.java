package tests;


import helpers.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import resources.Config;
import resources.RetryAnalyzer;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

@Test(retryAnalyzer = RetryAnalyzer.class)
public class ChivettesTest {

    WebDriver driver;
    ProfilePage profilePage;
    EditProfilePage editProfilePage;
    Logins logins;
    LoginModalPage loginModalPage;
    PageHeaderPage pageHeaderPage;
    SubscriptionPage subscriptionPage;
    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        logins = new Logins(driver);
        pageHeaderPage = new PageHeaderPage(driver);
        loginModalPage = new LoginModalPage(driver);
        profilePage = new ProfilePage(driver);
        editProfilePage = new EditProfilePage(driver);
        subscriptionPage = new SubscriptionPage(driver);

    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        logins.unpaidLogin(config.chivetteEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void ChivetteIconTest() {
        WebElement bottom = subscriptionPage.subscriptionFooter();
        editProfilePage.userMenu().click();
        editProfilePage.yourProfileBtn().click();
        Assert.assertTrue(profilePage.chivetteIcon().isDisplayed(), "Did not find Chivette icon by username");
    }

    @Test
    public void PaywallClosesOnLogin() throws InterruptedException {
        logins.logout();
        driver.navigate().refresh();
        pageHeaderPage.menuChivettes().click();
        PageActions.scrollDown(driver, 5);
        WebElement subscriptionFooter = subscriptionPage.subscriptionFooter();
        subscriptionPage.monthlyJoinBtn().click();
        loginModalPage.emailInput().sendKeys(config.chivetteEmail);
        loginModalPage.passwordInput().sendKeys(config.password);
        loginModalPage.signIn().click();
        driver.navigate().refresh();
        Thread.sleep(5000);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Assert.assertFalse(PrettyAsserts.isElementDisplayed(subscriptionFooter));
    }

    @Test
    public void TipJar() {
        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.tipURLInput().sendKeys("https://cash.app/$SamiNiceGirl");
        editProfilePage.saveProfileBtn().click();
        pageHeaderPage.userMenu().click();
        pageHeaderPage.yourProfileBtn().click();
        Waiter.wait(driver).until(CustomExpectedConditions.profileLoaded());
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profilePage.TipLink())
                && profilePage.TipLink().getAttribute("target").equals("_blank"));
    }

    @Test
    public void WebSiteInputDisplays() {
        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.socialLinksTab().click();
        Assert.assertTrue(editProfilePage.websiteInput().isDisplayed(), "Did not find website input");
    }

    @Test
    public void WebSiteLink() {
        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.socialLinksTab().click();
        editProfilePage.websiteInput().clear();
        editProfilePage.websiteInput().sendKeys("https://www.google.com");
        editProfilePage.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(editProfilePage.updateSuccess()));
        editProfilePage.userMenu().click();
        editProfilePage.yourProfileBtn().click();

        Assert.assertTrue(profilePage.websiteIcon().isDisplayed(), "Did not find website icon");

        profilePage.websiteIcon().click();
        WindowUtil.switchToWindow(driver, 1);

        Assert.assertTrue(driver.getTitle().contains("Google"), "Website tab did not open");

        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    //************************* Teardown ***************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}
