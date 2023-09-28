package tests;

import helpers.Logins;
import helpers.PrettyAsserts;
import helpers.Waiter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.PageHeaderPage;
import pages.ProfilePage;
import resources.Config;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

public class ProfilePointsTest {
    private static TestConfig config;
    WebDriver driver;
    Logins login;
    ProfilePage profilePage;
    PageHeaderPage header;

    //************************************* Setup *********************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
        header = new PageHeaderPage(driver);
        profilePage = new ProfilePage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //*********************************** Test Cases *****************************************

    @Test
    public void CloseModal(){
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();
        profilePage.morePointsPurchase().click();
        WebElement lionModal = profilePage.lionModal();
        profilePage.lionCloseModal().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.stalenessOf(lionModal)), "Modal still displays after trying to close it");
    }

    @Test
    public void EarnPointsPurchase(){
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();
        profilePage.morePointsPurchase().click();
        Assert.assertEquals(profilePage.lionModalDescription().getText(), "Get 5 points for every $1 you spend in our store", "Missing modal text");
        profilePage.lionGotoSite().click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.thechivery.com/");
    }

    @Test
    public void HistoryTab(){
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();
        profilePage.pointsHistory().click();
        profilePage.pointsHistoryApproved().click();

    }

    @Test
    public void NonClickableCards() {
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profilePage.earnPointsPhotoFeatured()), "Did not see Photo featured on theChive card");
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profilePage.earnPointsUpvotedScore()), "Did not see upvote score card");
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profilePage.earnPointsDopamineDump()), "Did not see Dopamine Dump card");
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profilePage.earnPointsSiteVisited()), "Did not see Site Visited card");
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profilePage.earnPointsNewFollower()), "Did not see new followers card");
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profilePage.earnPointsSubmitPhoto()), "Did not see Points submitted card");
    }

    //************************************* Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
