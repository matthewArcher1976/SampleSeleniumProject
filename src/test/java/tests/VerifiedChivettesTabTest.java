package tests;

import helpers.*;
import io.github.sukgu.Shadow;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.PageHeaderPage;
import pages.SubscriptionPage;
import resources.Config;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.ProfilePage;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

public class VerifiedChivettesTabTest {

    WebDriver driver;
    ProfilePage profilePage;
    PageHeaderPage header;
    EditProfilePage profile;
    Logins login;
    SubscriptionPage subscription;
    Actions action;

    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
        action = new Actions(driver);
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



    @Test
    public void LoggedOutUserBuysMonthly() throws InterruptedException {
        header.menuChivettes().click();
        PageActions.findElementWithScrolling(driver, By.cssSelector(subscription.monthlyJoinBtnSelector())).click();
        login.emailInput().sendKeys(config.unpaidEmail);
        login.passwordInput().sendKeys(config.password);
        login.signIn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("auth")));
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        driver.navigate().refresh();
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        action.moveToElement(subscription.monthlyJoinBtn()).click().perform();
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(subscription.chargebeeFrame()));
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(subscription.chargebeeOrderSummary()), "Did not see the order summary");
    }
    @Test
    public void LoggedOutUserHitsPaywall() throws InterruptedException {
        header.menuChivettes().click();
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(PageActions.findElementWithScrolling(driver, By.cssSelector(subscription.monthlyJoinBtnSelector()))), "Did not hit the paywall");
    }
    @Test
    public void NoThanksButton() throws InterruptedException {
        header.menuChivettes().click();
        PageActions.findElementWithScrolling(driver, By.cssSelector(subscription.noThanksBtnSelector())).click();
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.attributeContains(By.id(header.menuLatestId()), "aria-current", "page")), "User should be sent to Latest after clicking No Thanks");
        header.menuChivettes().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.visibilityOf(subscription.monthlyJoinBtn())).isDisplayed());

    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}