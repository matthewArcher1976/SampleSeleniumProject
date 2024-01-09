package tests;

import helpers.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.*;
import resources.Config;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import resources.TestConfig;

import java.util.List;

import static resources.getDriverType.getDriver;

public class VerifiedChivettesTabTest {

    WebDriver driver;
    ProfilePage profilePage;
    PageHeaderPage pageHeaderPage;
    EditProfilePage profile;
    Logins login;
    SubscriptionPage subscription;
    SubmissionCardsPage card;
    SubmissionModalPage submissionModalPage;
    Actions action;

    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
        action = new Actions(driver);
        card = new SubmissionCardsPage(driver);
        pageHeaderPage = new PageHeaderPage(driver);
        profile = new EditProfilePage(driver);
        profilePage = new ProfilePage(driver);
        submissionModalPage = new SubmissionModalPage(driver);
        subscription = new SubscriptionPage(driver);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void BlurredImageCard(){
       pageHeaderPage.menuChivettes().click();
       PageActions.scrollDown(driver, 4);
       Assert.assertTrue(PrettyAsserts.isDisplayed(card.blurredImageBy(), driver), "Did not find a blurred image");
    }

    @Test
    public void BlurredImageModal(){
        pageHeaderPage.menuChivettes().click();
        PageActions.scrollDown(driver, 4);
        card.blurredImage().click();
        Assert.assertTrue(PrettyAsserts.isDisplayed(submissionModalPage.blurredImageBy(), driver), "The image on the submission modal is not blurred");
    }

    @Test()//may need to set back to priority = 1
    public void LoggedOutUserBuysMonthly() throws InterruptedException {
        pageHeaderPage.menuChivettes().click();
        PageActions.findElementWithScrolling(driver, subscription.monthlyJoinBtnBy()).click();
        login.emailInput().sendKeys(config.defaultEmail);
        login.passwordInput().sendKeys(System.getenv("TEST_PWD"));
        login.signIn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("auth")));
        Thread.sleep(5000);
        Assert.assertTrue(subscription.chargebeeFrame().isDisplayed(), "Chargebee frame should be open");
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(subscription.chargebeeFrame()));
        action.moveToElement(subscription.chargebeeClose()).click().perform();//TODO - it's not actually closing the fucking iframe
       Thread.sleep(5000);
    }

    @Test
    public void LoggedOutUserHitsPaywall() throws InterruptedException {
        pageHeaderPage.menuChivettes().click();
        Assert.assertTrue(PageActions.findElementWithScrolling(driver, subscription.monthlyJoinBtnBy()).isDisplayed(), "Did not hit the paywall");
    }

    @Test
    public void ProfilePagePaywall() throws InterruptedException {
        pageHeaderPage.menuChivettes().click();
        PageActions.findElementWithScrolling(driver, subscription.monthlyJoinBtnBy());
        PageActions.scrollToTop(driver);
        card.firstCard().findElement(By.cssSelector(card.userNameSelector())).click();
        Waiter.wait(driver).until(CustomExpectedConditions.profileLoaded());
        Assert.assertTrue(PrettyAsserts.isDisplayed(subscription.monthlyJoinBtnBy(), driver), "Paywall should display on Chivette profile page");
    }

    @Test
    public void UnsubscribedUserStillSeesCards() throws InterruptedException {
        pageHeaderPage.menuChivettes().click();
        PageActions.findElementWithScrolling(driver, subscription.monthlyJoinBtnBy());
        pageHeaderPage.menuLatest().click();
        pageHeaderPage.menuChivettes().click();
        driver.navigate().refresh();
        List<WebElement> cards = card.allCards();
        Assert.assertTrue(cards.size() >= 1);
    }

    //************************** Teardown ****************************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}