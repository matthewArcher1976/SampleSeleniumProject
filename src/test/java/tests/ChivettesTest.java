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
        logins.unpaidLogin(config.chivetteEmail, System.getenv("TEST_PWD"));
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void ChivetteIconTest() {
        editProfilePage.userMenu().click();
        editProfilePage.yourProfileBtn().click();
        Assert.assertTrue(profilePage.chivetteIcon().isDisplayed(), "Did not find Chivette icon by username");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, enabled = false)
    public void CreateForumPost(){
        pageHeaderPage.userMenu().click();
        pageHeaderPage.yourProfileBtn().click();
        profilePage.tabForum().click();
        profilePage.newForumPostBtn().click();
        profilePage.postTitleInput().sendKeys("Automated test post ID:" + Randoms.getRandomString(5));
        profilePage.postMessageInput().sendKeys("This test post was brought to you be The Department of Automated Testing and the Ad Council");
        profilePage.postCreateBtn().click();
        Assert.assertTrue(profilePage.postToast().isDisplayed(), "Post no toast");
    }

    @Test()
    public void PaywallClosesOnLogin() throws InterruptedException {
        logins.logout();
        driver.navigate().refresh();
        pageHeaderPage.menuChivettes().click();
        PageActions.scrollDown(driver, 5);
        WebElement subscriptionFooter = subscriptionPage.subscriptionFooter();
        subscriptionPage.payWallSignIn().click();
        loginModalPage.emailInput().sendKeys(config.chivetteEmail);
        loginModalPage.passwordInput().sendKeys(System.getenv("TEST_PWD"));
        loginModalPage.signIn().click();
        driver.navigate().refresh();
        Thread.sleep(5000);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.stalenessOf(subscriptionFooter)), "Should not see footer");//test may break, watch
    }

    @Test
    public void TipJar() throws InterruptedException {
        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.socialLinksTab().click();
        editProfilePage.tipURLInput().sendKeys("https://cash.app/$SamiNiceGirl");
        editProfilePage.saveProfileBtn().click();
        pageHeaderPage.userMenu().click();
        pageHeaderPage.yourProfileBtn().click();
        Thread.sleep(1000);
        Waiter.wait(driver).until(CustomExpectedConditions.profileLoaded());
        Assert.assertTrue(PrettyAsserts.isDisplayed(profilePage.TipLinkBy(), driver), "Did not see tip jar link on profile");
        Assert.assertEquals(profilePage.TipLink().getAttribute("target"), "_blank");

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
