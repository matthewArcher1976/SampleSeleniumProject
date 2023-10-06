package tests;

import resources.Config;
import helpers.Logins;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.MobileViewPage;
import pages.SubmissionCardsPage;
import resources.RetryAnalyzer;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class MobileViewHeaderTest {
    WebDriver driver;
    Logins login;
    MobileViewPage mobile;
    SubmissionCardsPage card;
    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverTypeMobile);

        card = new SubmissionCardsPage(driver);
        login = new Logins(driver);
        mobile = new MobileViewPage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void refresh() {

        driver.get(config.url);
        driver.manage().window().setSize(new Dimension(380, 844));
    }

    //************************** Begin Tests ********************************************

    @Test(enabled = false)//the ad frames changed this, no longer relevant
    public void AvatarPosition(){
        WebElement avatar = mobile.mobileAvatar();
        Dimension viewportSize = driver.manage().window().getSize();
        int viewportWidth = viewportSize.getWidth();
        Point elementLocation = avatar.getLocation();
        int elementX = elementLocation.getX();
        int elementWidth = avatar.getSize().getWidth();
        Assert.assertEquals(viewportWidth - elementX - elementWidth, 23, "The avatar is too close or too far to the edge of the screen");
    }
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void CommentButtonFeatured() {
        mobile.hamburgerMenu().click();
        mobile.mobileFeatured().click();
        card.commentIcon().click();
        Assert.assertTrue(card.disqusSection().isDisplayed(), "CommentButtonFeatured - comments did not display");
    }

    @Test
    public void CommentButtonLatest() {
        card.commentIcon().click();
        Assert.assertTrue(card.disqusSection().isDisplayed(), "CommentButtonLatest - comments did not display");
    }

    @Test
    public void HamburgerMenuOptionsDisplay() {
        mobile.hamburgerMenu().click();
        WebElement latest = mobile.mobileLatest();
        WebElement featured = mobile.mobileFeatured();
        WebElement following = mobile.mobileFollowing();
        WebElement top = mobile.mobileTop();
        Assert.assertTrue(latest.isDisplayed()
                && featured.isDisplayed()
                && following.isDisplayed()
                && top.isDisplayed(), "Menu Items did not display after clicking the menu");

        mobile.hamburgerMenu().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.stalenessOf(latest))
                && helpers.Waiter.wait(driver).until(ExpectedConditions.stalenessOf(featured))
                && helpers.Waiter.wait(driver).until(ExpectedConditions.stalenessOf(following))
                && helpers.Waiter.wait(driver).until(ExpectedConditions.stalenessOf(top)), "Menu Items should not display after closing the menu");
    }

    @Test
    public void MenuFeatured() throws InterruptedException {
        mobile.hamburgerMenu().click();
        WebElement featured = mobile.mobileFeatured();
        featured.click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"))
                && helpers.Waiter.wait(driver).until(ExpectedConditions.stalenessOf(featured)), "Menu should close and DD page should load");
        mobile.hamburgerMenu().click();
        Thread.sleep(2000);//debug
        Assert.assertTrue(mobile.mobileLatest().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)")
                && mobile.mobileFeatured().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 1)")
                && mobile.mobileFollowing().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)")
                && mobile.mobileTop().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)"), "Featured  should be selected by now");
    }

    @Test
    public void MenuFollowing() throws InterruptedException {
        mobile.hamburgerMenu().click();
        WebElement following = mobile.mobileFollowing();
        following.click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("following"))
                && helpers.Waiter.wait(driver).until(ExpectedConditions.stalenessOf(following)), "Menu should close and Following page should load");
        mobile.hamburgerMenu().click();
        Thread.sleep(2000);
        Assert.assertTrue(mobile.mobileLatest().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)")
                && mobile.mobileFeatured().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)")
                && mobile.mobileFollowing().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 1)")
                && mobile.mobileTop().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)"), "Following should be selected by now");
    }

    @Test
    public void MenuLatestIsSelected() throws InterruptedException {
        mobile.hamburgerMenu().click();
        Thread.sleep(2000);//debug
        Assert.assertTrue(mobile.mobileLatest().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 1)")
                && mobile.mobileFeatured().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)")
                && mobile.mobileFollowing().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)")
                && mobile.mobileTop().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)"), "Latest should be selected by default");
    }

    @Test
    public void MenuTopChivers() throws InterruptedException {
        mobile.hamburgerMenu().click();
        WebElement top = mobile.mobileTop();
        top.click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("leaderboard"))
                && helpers.Waiter.wait(driver).until(ExpectedConditions.stalenessOf(top)), "Menu should close and Top Chivers page should load");
        mobile.hamburgerMenu().click();
        Thread.sleep(2000);//debug
        Assert.assertTrue(mobile.mobileLatest().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)")
                && mobile.mobileFeatured().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)")
                && mobile.mobileFollowing().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 0.1)")
                && mobile.mobileTop().getCssValue("borderBottomColor").equals("rgba(0, 195, 0, 1)"), "Following should be selected by now");
    }

    @Test
    public void OpenHamburgerMenu() {
        Assert.assertEquals(mobile.hamburgerMenu().getAttribute("aria-expanded"), "false", "Hamburger menu should be closed by default");
        mobile.hamburgerMenu().click();
        Assert.assertEquals(mobile.hamburgerMenu().getAttribute("aria-expanded"), "true", "Hamburger menu should open on tap");
    }
    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
