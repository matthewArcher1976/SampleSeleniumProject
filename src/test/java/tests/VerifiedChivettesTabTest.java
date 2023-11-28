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
    PageHeaderPage header;
    EditProfilePage profile;
    Logins login;
    SubscriptionPage subscription;
    SubmissionCardsPage card;
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
        header = new PageHeaderPage(driver);
        profile = new EditProfilePage(driver);
        profilePage = new ProfilePage(driver);
        subscription = new SubscriptionPage(driver);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test(priority = 1)
    public void LoggedOutUserBuysMonthly() throws InterruptedException {
        header.menuChivettes().click();
        PageActions.findElementWithScrolling(driver, By.cssSelector(subscription.monthlyJoinBtnSelector())).click();
        login.emailInput().sendKeys(config.defaultEmail);
        login.passwordInput().sendKeys(config.password);
        login.signIn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("auth")));
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        //driver.navigate().refresh();
      //  Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        action.moveToElement(subscription.monthlyJoinBtn()).click().perform();
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(subscription.chargebeeFrame()));
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(subscription.chargebeeOrderSummary()), "Did not see the order summary");
        subscription.chargebeeClose().click();
        driver.switchTo().defaultContent();
        login.logout();
    }

    @Test
    public void LoggedOutUserHitsPaywall() throws InterruptedException {
        header.menuChivettes().click();
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(PageActions.findElementWithScrolling(driver, By.cssSelector(subscription.monthlyJoinBtnSelector()))), "Did not hit the paywall");
    }

    @Test
    public void ProfilePagePaywall() throws InterruptedException {
        header.menuChivettes().click();
        PageActions.findElementWithScrolling(driver, By.cssSelector(subscription.monthlyJoinBtnSelector()));
        PageActions.scrollToTop(driver);
        card.firstCard().findElement(By.cssSelector(card.userNameSelector())).click();
        Waiter.wait(driver).until(CustomExpectedConditions.profileLoaded());
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(subscription.monthlyJoinBtn()), "Paywall should display on Chivette profile page");
    }

    @Test
    public void UnsubscribedUserStillSeesCards() throws InterruptedException {
        header.menuChivettes().click();
        PageActions.findElementWithScrolling(driver, By.cssSelector(subscription.monthlyJoinBtnSelector()));
        header.menuLatest().click();
        header.menuChivettes().click();
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