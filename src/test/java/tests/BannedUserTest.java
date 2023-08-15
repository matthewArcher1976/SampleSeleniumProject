package tests;

import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.BannedUserPage;
import pages.PageHeaderPage;
import pages.SubmissionCardsPage;


@SuppressWarnings("DefaultAnnotationParam")
public class BannedUserTest {
    WebDriver driver = Drivers.ChromeDriver();
    Logins login = new Logins(driver);
    BannedUserPage banned = new BannedUserPage(driver);
    SubmissionCardsPage card = new SubmissionCardsPage(driver);
    PageHeaderPage header = new PageHeaderPage(driver);

    @BeforeTest
    @Parameters({"bannedEmail", "password", "url"})
    public void login(@Optional("thechivetest+banned@gmail.com") String unpaidEmail, @Optional("Chive1234") String password, @Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
        driver.get(url);
        login.unpaidLogin(unpaidEmail, password, driver);
        Thread.sleep(1000);
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

    @Test(enabled = true, priority = 1)
    public void BanedScreenOnLoginTest() throws InterruptedException {
        Assert.assertTrue(banned.bannedScreen().isDisplayed(), "Banned User did not see banned screen on login");
        Assert.assertTrue(banned.attention().isDisplayed(), "Attention header did not display");
        Assert.assertTrue(banned.reasonsText().isDisplayed(), "Your account has been banned.. text did not display");
        Assert.assertTrue(banned.reasons().isDisplayed(), "Reason text did not display");
        Assert.assertTrue(banned.bannedText().isDisplayed(), "As a banned user... text did not display");
    }

    @Test(enabled = true, priority = 1)
    public void BanedUserLinkTest() {
        banned.contactLink().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("contact"));
        Assert.assertTrue(driver.getCurrentUrl().contains("contact"), "Contact page did not load");
        driver.navigate().back();

    }


    @Test(enabled = true, priority = 1)
    @Parameters({"url"})
    public void FeaturedBannedTest(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        header.menuFeatured().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Downvote button did not bring up banned screen");
        driver.navigate().back();
    }


    @Test(enabled = true, priority = 1)
    @Parameters({"url"})
    public void FollowingBannedTest(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        header.menuFollowing().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned"));
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Favorite button did not bring up banned screen");
        driver.navigate().back();
    }

    @Test(enabled = true, priority = 1)
    @Parameters({"url"})
    public void LatestBannedTest(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        header.menuLatest().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Comment button did not bring up banned screen");
    }



    @Test(enabled = true, priority = 1)
    @Parameters({"url"})
    public void SettingsBtnBannedTest(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        header.userMenu().click();
        header.settingsBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned"));
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Create button did not bring up banned screen");
    }

    @Test(enabled = true, priority = 99)
    @Parameters({"url"})
    public void SignoutBannedTest(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        header.userMenu().click();
      //  header.yourProfileBtn().click();
        header.signoutBtn().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("alert-success"))).isDisplayed(), "Create button did not bring up banned screen");
    }

    @Test(enabled = true, priority = 1)
    @Parameters({"url"})
    public void SubmitButtonBannedTest(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        card.createBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned"));
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Create button did not bring up banned screen");
    }

    @Test(enabled = true, priority = 1)
    @Parameters({"url"})
    public void TopChiversannedTest(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        header.menuTopChivers().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned"));
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Favorite button did not bring up banned screen");
    }

    @Test(enabled = true, priority = 1)
    @Parameters({"url"})
    public void YourProfileBannedTest(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        header.userMenu().click();
        header.yourProfileBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned"));
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("user-banned")), "Create button did not bring up banned screen");
    }

}
