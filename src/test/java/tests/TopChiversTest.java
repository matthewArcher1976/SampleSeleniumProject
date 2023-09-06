package tests;

import resources.Config;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.TopChiversPage;
import resources.TestConfig;

import java.util.List;

import static helpers.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
@Ignore
public class TopChiversTest {
    WebDriver driver;
    private static TestConfig config;
    Actions action;
    Logins login;
    TopChiversPage top;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);

        action = new Actions(driver);
        top = new TopChiversPage(driver);
    }

    @BeforeTest
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() throws InterruptedException {
        driver.get(config.url);
        top.topChiversTab().click();
        Thread.sleep(3000);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void FollowButton() throws InterruptedException {
        top.topChiversFollowBtn().getText();
        if (top.topChiversFollowBtn().getText().contentEquals("FOLLOW")) {
            top.topChiversFollowBtn().click();
            Thread.sleep(3000);
            Assert.assertTrue(top.topChiversFollowBtn().getText().contentEquals("FOLLOWING"), "Clicking FOLLOW button did nit change it to FOLLOWING");
            Assert.assertTrue(top.followCheck().isDisplayed(), "Missing checkmark in FOLLOWING");
        } else if (top.topChiversFollowBtn().getText().contentEquals("FOLLOWING")) {
            top.topChiversFollowBtn().click();
            Thread.sleep(2000);
            Assert.assertTrue(top.topChiversFollowBtn().getText().contentEquals("FOLLOW"), "Clicking FOLLOWING button did not change it to FOLLOW");
            Assert.assertTrue(top.followCircle().isDisplayed(), "Missing checkmark in FOLLOWING");
        } else if (!top.topChiversFollowBtn().getText().contentEquals("FOLLOW") && !top.topChiversFollowBtn().getText().contentEquals("FOLLOWING")) {
            Assert.fail("Something is wrong with the follow button");
        }
    }

    @Test
    public void FollowButtonHover() throws InterruptedException {
        if (top.topChiversFollowBtn().getText().contentEquals("FOLLOWING")) {
            top.topChiversFollowBtn().click();
            Thread.sleep(4000);
            driver.navigate().refresh();//for some reason the color doesn't change back in the test though it does if you do it manually. refresh as a workaround
        }
        try {
            //    System.out.println(top.topChiversFollowBtn().getCssValue("border-color"));
            Assert.assertEquals(top.topChiversFollowBtn().getCssValue("border-color"), "rgb(0, 195, 0)");
        } catch (AssertionError e) {
            System.out.println("before mouseover color was wrong " + top.topChiversFollowBtn().getCssValue("border-color"));
            Assert.fail();
        }

        action.moveToElement(top.topChiversFollowBtn()).perform();
        Thread.sleep(2000);
        // System.out.println(top.topChiversFollowBtn().getCssValue("border-color"));
        try {
            Assert.assertEquals(top.topChiversFollowBtn().getCssValue("border-color"), "rgb(0, 156, 0)");
        } catch (AssertionError e) {
            System.out.println("after mouseover " + top.topChiversFollowBtn().getCssValue("border-color"));
            Assert.fail();
        }
    }

    @Test
    public void RecentlyVerified() throws InterruptedException {
        String verifiedUser = top.recentlyVerifiedUser().getText().replace("@", "");
        //ButtonDisplays
        try {
            Assert.assertTrue(top.recentlyVerified().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("Recently Verified not found");
            Assert.fail();
        }

        top.recentlyVerifiedUser().click();
        Thread.sleep(4000);

        try {
            Assert.assertTrue(driver.getCurrentUrl().contains(verifiedUser));
        } catch (AssertionError e) {
            System.out.println("Did not get to the verified user's page");
            Assert.fail();
        }
    }

    @Test
    public void ShareIcon() {
        try {
            Assert.assertTrue(top.shareIcon().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("Missing share icon");
            Assert.fail();
        }
    }

    @Test
    public void SocialLinksFacebook() throws InterruptedException {
        top.socialFacebookLink().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Thread.sleep(2000);
        try {
            Assert.assertEquals(driver.getTitle(), "theCHIVE - Home | Facebook");
        } catch (AssertionError e) {
            System.out.println("Facebook window title was " + driver.getTitle());
            Assert.fail();
        }
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SocialLinksInsta() {
        top.socialInstagramLink().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        try {
            Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("Instagram")));
        } catch (AssertionError e) {
            System.out.println("Insta window title was " + driver.getTitle());
            Assert.fail();
        }
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SocialLinksTwitter() throws InterruptedException {
        top.socialTwitterLink().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Thread.sleep(2000);
        try {
            Assert.assertEquals(driver.getTitle(), "theCHIVE (@theCHIVE) / Twitter");
        } catch (AssertionError e) {
            System.out.println("Twitter window title was " + driver.getTitle());
            Assert.fail();
        }
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void TagsDisplay() {
        //simple text looks to see if a trending tag displays
        try {
            Assert.assertTrue(top.tag().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("Trending tag not found");
            Assert.fail();
        }
    }

    @Test
    public void TopChiversRibbon() {
        //TODO - figure out how to grab that orphaned Top Chivers text
        try {
            Assert.assertTrue(top.topChiversHeader().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("Top Chivers logo didn't load");
            Assert.fail();
        }
    }

    @Test
    public void TopChiversArrow() throws InterruptedException {

        try {
            Thread.sleep(5000);
            Assert.assertTrue(top.trendArrowIcon().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("Top Chivers arrow icon didn't load");
            Assert.fail();
        }
    }

    @Test
    public void TopChiversAvatar() {
        try {
            Assert.assertTrue(top.topChiversProfilePic().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("Did not find the profile pic ");
            Assert.fail();
        }
    }

    @Test
    public void TopChiversRowElements() throws InterruptedException {
        Thread.sleep(2000);
        List<WebElement> rows = top.allRows();
        for (WebElement row : rows) {
            try {
                // row.findElement(By.cssSelector("[id^='leaderboard-row-username']")).click();
                Assert.assertTrue(row.findElement(By.xpath("//div[contains(@id,'leaderboard-row-position-')]")).isDisplayed() &&
                        row.findElement(By.xpath("//label[contains(@class, 'avatar')]")).isDisplayed() &&
                        row.findElement(By.xpath("//div[contains(@id,'leaderboard-row-username-')]")).isDisplayed() &&
                        row.findElement(By.xpath("//div[contains(@id,'leaderboard-row-points-')]")).isDisplayed() &&
                        row.findElement(By.xpath("//div[contains(@id,'btn-follow-')]")).isDisplayed());
            } catch (AssertionError e) {
                System.out.println(row.findElement(By.cssSelector(null)));
            }
        }
    }

    @Test
    public void TopChivesRowsScroll() throws InterruptedException {
        int first = top.allRows().size();
        helpers.PageActions.scrollDown(driver, 3);
        Thread.sleep(1000);
        int second = top.allRows().size();
        try {
            //10 rows should load at first
            Assert.assertTrue(second > first);
        } catch (AssertionError e) {
            System.out.println("Rows not loading when you scroll ");
            Assert.fail();
        }
    }

    @Test
    public void TopChivesUserName() throws InterruptedException {
        String userName = top.topChiversUserName().getText().replace("@", "");
        System.out.println(userName);
        top.topChiversUserName().click();
        Thread.sleep(2000);
        String url = driver.getCurrentUrl();
        System.out.println(url);
        try {
            Assert.assertTrue(url.contains(userName));
        } catch (AssertionError e) {
            System.out.println("User's page did not load on clicking username  ");
            Assert.fail();
        }
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}


