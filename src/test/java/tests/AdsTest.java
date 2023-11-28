package tests;

import helpers.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.AdsPage;
import pages.PageHeaderPage;
import pages.SubmissionCardsPage;
import resources.Config;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

public class AdsTest {
    WebDriver driver;
    AdsPage ads;
    Logins login;
    Actions action;
    PageHeaderPage header;
    SubmissionCardsPage card;

    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
        ads = new AdsPage(driver);
        action = new Actions(driver);
        header = new PageHeaderPage(driver);
        card = new SubmissionCardsPage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.defaultEmail, config.password);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void AdsOnFeatured() {
        header.menuFeatured().click();
        Waiter.wait(driver).until(ExpectedConditions.and(
                ExpectedConditions.urlContains("dopamine-dump"),
                CustomExpectedConditions.cardsLoaded()));
        PageActions.scrollDown(driver, 2);
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(ads.adFrame()), "Did not find an ad frame on Latest");
    }

    @Test
    public void AdsOnFollowing() {
        header.menuFollowing().click();
        PageActions.scrollDown(driver, 2);
        Waiter.wait(driver).until(ExpectedConditions.and(
                ExpectedConditions.urlContains("following"),
                CustomExpectedConditions.cardsLoaded()));
        PageActions.scrollDown(driver, 2);
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(ads.adFrame()), "Did not find an ad frame on Latest");
    }

    @Test
    public void AdsOnLatest() {
        PageActions.scrollDown(driver, 2);
        Waiter.wait(driver).until(CustomExpectedConditions.cardsLoaded());
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(ads.adFrame()), "Did not find an ad frame on Latest");
    }

    @Test
    public void AdsOnVerifiedChivettes() {
        header.menuChivettes().click();
        PageActions.scrollDown(driver, 2);
        Waiter.wait(driver).until(ExpectedConditions.and(
                ExpectedConditions.urlContains("chivettes"),
                CustomExpectedConditions.cardsLoaded()));
        PageActions.scrollDown(driver, 2);
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(ads.adFrame()), "Did not find an ad frame on Verified");
    }

    @Test
    public void ClickAdsFrame() {
        PageActions.scrollDown(driver, 2);
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(ads.adFrame()));
        ads.adImage().click();
        WindowUtil.switchToWindow(driver, 1);
        try {
            Assert.assertFalse(driver.getCurrentUrl().isEmpty(), "Looks like the ad didn't load");
            driver.close();
            WindowUtil.switchToWindow(driver, 0);
        } catch (Exception e) {
            driver.close();
            WindowUtil.switchToWindow(driver, 0);
        }
    }

    @Test
    public void CloseAd() {
        PageActions.scrollDown(driver, 2);
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(ads.adFrame()));
        action.moveToElement(ads.closeAdButton()).click().perform();
        Assert.assertTrue(PrettyAsserts.isElementEnabled(ads.sendFeedbackButton()), "User may not be able to hide the ad");
    }

    //************************ Teardown ****************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}
