package tests;

import helpers.Config;
import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.BannedUserPage;
import pages.PageHeaderPage;
import pages.SubmissionCardsPage;
import resources.TestConfig;


@SuppressWarnings("DefaultAnnotationParam")
public class BannedUserTest {
    WebDriver driver = Drivers.ChromeDriver();
    Logins login = new Logins(driver);
    BannedUserPage banned = new BannedUserPage(driver);
    SubmissionCardsPage card = new SubmissionCardsPage(driver);
    PageHeaderPage header = new PageHeaderPage(driver);
    private static TestConfig config;

    @BeforeTest
    public static void configs() throws Exception {
       config = Config.getConfig();
    }

    @BeforeClass
    public void signIn() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.bannedEmail, config.password);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void BanedScreenOnLoginTest() {
        Assert.assertTrue(banned.bannedScreen().isDisplayed(), "Banned User did not see banned screen on login");
        Assert.assertTrue(banned.attention().isDisplayed(), "Attention header did not display");
        Assert.assertTrue(banned.reasonsText().isDisplayed(), "Your account has been banned.. text did not display");
        Assert.assertTrue(banned.reasons().isDisplayed(), "Reason text did not display");
    }

    @Test(enabled = false)//we removed the contact
    public void BanedUserLinkTest() {
        banned.contactLink().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("contact"));
        Assert.assertTrue(driver.getCurrentUrl().contains("contact"), "Contact page did not load");
        driver.navigate().back();

    }

    @Test
    public void FeaturedBannedTest() {
        driver.get(config.url);
        header.menuFeatured().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Downvote button did not bring up banned screen");
        driver.navigate().back();
    }

    @Test
    public void FollowingBannedTest() {
        driver.get(config.url);
        header.menuFollowing().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned"));
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Favorite button did not bring up banned screen");
        driver.navigate().back();
    }

    @Test
    public void LatestBannedTest() {
        driver.get(config.url);
        header.menuLatest().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Comment button did not bring up banned screen");
    }

    @Test
    public void SettingsBtnBannedTest() {
        driver.get(config.url);
        header.userMenu().click();
        header.settingsBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned"));
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Create button did not bring up banned screen");
    }

    @Test(enabled = true, priority = 99)

    public void SignoutBannedTest() {
        driver.get(config.url);
        header.userMenu().click();
        //  header.yourProfileBtn().click();
        header.signoutBtn().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("alert-success"))).isDisplayed(), "Create button did not bring up banned screen");
    }

    @Test

    public void SubmitButtonBannedTest() {
        driver.get(config.url);
        card.createBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned"));
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Create button did not bring up banned screen");
    }

    @Test

    public void TopChiversannedTest() {
        driver.get(config.url);
        header.menuTopChivers().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned"));
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Favorite button did not bring up banned screen");
    }

    @Test

    public void YourProfileBannedTest() {
        driver.get(config.url);
        header.userMenu().click();
        header.yourProfileBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned"));
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Create button did not bring up banned screen");
    }

    //************************ Teardown ****************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}
