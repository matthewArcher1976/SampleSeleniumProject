package tests;

import helpers.Logins;
import helpers.PrettyAsserts;
import helpers.Waiter;
import io.github.sukgu.*;
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
    Shadow shadow;

    //************************************* Setup *********************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
        header = new PageHeaderPage(driver);
        profilePage = new ProfilePage(driver);
        shadow = new Shadow(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, System.getenv("TEST_PWD"));
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
        profilePage.pointsEarnPoints().click();
        profilePage.morePointsPurchase().click();
        WebElement lionModal = profilePage.lionModal();
        profilePage.lionCloseModal().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.stalenessOf(lionModal)), "Modal still displays after trying to close it");
    }

    @Test(enabled = false)//TODO - enable when this bug is fixed
    public void EarnPointsFacebook(){
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();
        profilePage.pointsEarnPoints().click();
        profilePage.morePointsFacebook().click();
        Assert.assertEquals(profilePage.lionModalTitle().getText(), "Like us on Facebook", "Missing or incorrect title on Facebook item");
        Assert.assertEquals(profilePage.lionGotoSite().getAttribute("href"), "https://www.instagram.com/thechive/", "Expected a link to our Instagram page, found " + profilePage.lionGotoSite().getAttribute("href"));
        driver.navigate().back();
    }

    @Test(enabled = false)//TODO - enable when bug is fixed
    public void EarnPointsInstagram(){
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();
        profilePage.pointsEarnPoints().click();
        profilePage.morePointsInstagram().click();
        Assert.assertEquals(profilePage.lionModalTitle().getText(), "Follow us on Instagram", "Missing or incorrect title on instagram item");
        Assert.assertEquals(profilePage.lionGotoSite().getAttribute("href"), "https://www.instagram.com/thechive/", "Expected a link to our Instagram page, found " + profilePage.lionGotoSite().getAttribute("href"));
        driver.navigate().back();
    }

    @Test
    public void EarnPointsPurchase(){
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();
        profilePage.pointsEarnPoints().click();
        profilePage.morePointsPurchase().click();
        Assert.assertEquals(profilePage.lionModalDescription().getText(), "Get 5 points for every $1 you spend in our store", "Missing modal text");
        Assert.assertTrue(profilePage.lionGotoSite().getAttribute("href").contains("thechivery"), "Expected a link to The Chivery, found: " + profilePage.lionGotoSite().getAttribute("href"));
    }
    @Test()
    public void EarnPointsReferral(){
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();
        profilePage.pointsEarnPoints().click();
        profilePage.morePointsReferral().click();
        Assert.assertEquals(profilePage.lionModalDescription().getText(), "Earn 1,000 points every time you refer a friend who spends over $40", "Expected Refer a friend, found: " + profilePage.lionModalDescription().getText());
        //TODO - complete test when https://resignationmedia.atlassian.net/browse/MYCHIVE-1125 is fixed
    }
    @Test
    public void EarnPointsVisitChivery(){
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();
        profilePage.pointsEarnPoints().click();
        profilePage.morePointsVisitChivery().click();
        Assert.assertEquals(profilePage.lionModalTitle().getText(), "Visit The Chivery", "Missing or incorrect title on visit The Chivery item");
        Assert.assertTrue(profilePage.lionGotoSite().getAttribute("href").contains("thechivery"), "Expected a link to The Chivery, found: " + profilePage.lionGotoSite().getAttribute("href"));
        driver.navigate().back();
    }

    @Test
    public void HistoryTabActivity(){
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();
        profilePage.pointsHistory().click();
        String activity = profilePage.pointsHistoryAction(profilePage.pointsHistoryRow(0)).getText();
        profilePage.pointsHistoryApproved().click();
        Assert.assertEquals(activity, profilePage.pointsHistoryModalTitle().getText(), "Modal title doesn't match modal row");
    }

    @Test
    public void NonClickableCards() {
        header.userMenu().click();
        header.yourProfileBtn().click();
        profilePage.pointsTab().click();

        profilePage.pointsEarnPoints().click();
        Assert.assertTrue(PrettyAsserts.isDisplayed(profilePage.earnPointsPhotoFeaturedBy(), driver), "Did not see Photo featured on theChive card");
        Assert.assertTrue(PrettyAsserts.isDisplayed(profilePage.earnPointsUpvotedScoreBy(), driver), "Did not see upvote score card");
        Assert.assertTrue(PrettyAsserts.isDisplayed(profilePage.earnPointsDopamineDumpBy(), driver), "Did not see Dopamine Dump card");
        Assert.assertTrue(PrettyAsserts.isDisplayed(profilePage.earnPointsSiteVisitedBy(), driver), "Did not see Site Visited card");
        Assert.assertTrue(PrettyAsserts.isDisplayed(profilePage.earnPointsNewFollowerBy(), driver), "Did not see new followers card");
        Assert.assertTrue(PrettyAsserts.isDisplayed(profilePage.earnPointsSubmitPhotoBy(), driver), "Did not see Points submitted card");
    }

    //************************************* Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
