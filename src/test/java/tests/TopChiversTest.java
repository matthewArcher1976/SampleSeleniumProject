package tests;

import helpers.Logins;
import helpers.PrettyAsserts;
import helpers.WindowUtil;
import io.github.sukgu.Shadow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.PageHeaderPage;
import pages.TopChiversPage;
import resources.Config;
import resources.RetryAnalyzer;
import resources.TestConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class TopChiversTest {
    WebDriver driver;
    private static TestConfig config;
    Actions action;
    Logins login;
    TopChiversPage top;
    PageHeaderPage header;
    Shadow shadow;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        shadow = new Shadow(driver);
        login = new Logins(driver);
        header = new PageHeaderPage(driver);
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
        Thread.sleep(3000);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void AvatarImages() {
        header.menuTopChivers().click();
        List<WebElement> rows = top.allRows();
        int i = 1;
        for (WebElement row : rows) {
            Assert.assertTrue(PrettyAsserts.isElementDisplayed(row.findElement(By.cssSelector(top.ChivetteAvatarSelector()))), "Did not find an avatar image on row " + i);
            i++;
        }
    }

    @Test
    public void FollowButtonClick() throws InterruptedException {
        header.menuTopChivers().click();
        top.FollowBtn().getText();
        if (top.FollowBtn().getText().contentEquals("FOLLOW")) {
            top.FollowBtn().click();
            Thread.sleep(2000);//i know
            Assert.assertTrue(top.FollowBtn().getText().contentEquals("FOLLOWING"), "Clicking FOLLOW button did not change it to FOLLOWING, got " + top.FollowBtn().getText());
            Assert.assertTrue(top.followCheck().isDisplayed(), "Missing checkmark in FOLLOWING");
        } else if (top.FollowBtn().getText().contentEquals("FOLLOWING")) {
            top.FollowBtn().click();
            Thread.sleep(2000);//yeah
            Assert.assertTrue(top.FollowBtn().getText().contentEquals("FOLLOW"), "Clicking FOLLOWING button did not change it to FOLLOW");
            Assert.assertTrue(top.followCircle().isDisplayed(), "Missing checkmark in FOLLOWING");
        } else if (!top.FollowBtn().getText().contentEquals("FOLLOW") && !top.FollowBtn().getText().contentEquals("FOLLOWING")) {
            Assert.fail("Something is wrong with the follow button");
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void FollowButtonHover() throws InterruptedException {
        header.menuTopChivers().click();
        if (top.FollowBtn().getText().contentEquals("FOLLOWING")) {
            top.FollowBtn().click();
            Thread.sleep(2000);
            driver.navigate().refresh();//for some reason the color doesn't change back in the test though it does if you do it manually. refresh as a workaround
        }
        Assert.assertEquals(top.FollowBtn().getCssValue("border-color"), "rgb(0, 194, 0)", "before mouseover color was wrong " + top.FollowBtn().getCssValue("border-color"));

        action.moveToElement(top.FollowBtn()).perform();
        Thread.sleep(2000);
        System.out.println(top.FollowBtn().getCssValue("border-color"));
        Assert.assertEquals(top.FollowBtn().getCssValue("border-color"), "rgb(0, 158, 0)", "after mouseover " + top.FollowBtn().getCssValue("border-color"));
    }

    @Test
    public void FollowButtonsAllTime(){
        header.menuTopChivers().click();
        top.tabAllTime().click();
        List<WebElement> rows = top.allRows();
        int i = 1;
        for(WebElement row:rows){
            Assert.assertTrue(PrettyAsserts.isElementDisplayed(row.findElement(By.cssSelector(top.FollowBtnSelector()))), "Missing the follow button on row " + i);
            i++;
        }
    }

    @Test
    public void FollowButtonsMonthly(){
        header.menuTopChivers().click();
        top.tabPastMonth().click();
        List<WebElement> rows = top.allRows();
        int i = 1;
        for(WebElement row:rows){
            Assert.assertTrue(PrettyAsserts.isElementDisplayed(row.findElement(By.cssSelector(top.FollowBtnSelector()))), "Missing the follow button on row " + i);
            i++;
        }
    }
    @Test
    public void PointsInOrderAllTime(){
        header.menuTopChivers().click();
        top.tabAllTime().click();
        List<WebElement> rows = top.allRows();
        int lastPoint = Integer.parseInt(top.firstRow().findElement(By.cssSelector(top.pointsSelector())).getText().replace(" Points", ""));
        int thisPoint = 0;
        for(WebElement row:rows){
            thisPoint = Integer.parseInt(row.findElement(By.cssSelector(top.pointsSelector())).getText().replace(" Points", ""));
            Assert.assertTrue(lastPoint >= thisPoint, "thisPoint is " + thisPoint + " lastPoint is " + lastPoint + ", The rows may be out of order");
            lastPoint = thisPoint;
        }
    }

    @Test
    public void PointsInOrderPastMonth(){
        header.menuTopChivers().click();
        top.tabPastMonth().click();
        List<WebElement> rows = top.allRows();
        int lastPoint = Integer.parseInt(top.firstRow().findElement(By.cssSelector(top.pointsSelector())).getText().replace(" Points", ""));
        int thisPoint = 0;
       for(WebElement row:rows){
            thisPoint = Integer.parseInt(row.findElement(By.cssSelector(top.pointsSelector())).getText().replace(" Points", ""));
            Assert.assertTrue(lastPoint >= thisPoint, "thisPoint is " + thisPoint + " lastPoint is " + lastPoint + ", The rows may be out of order");
            lastPoint = thisPoint;
        }
    }

    @Test//TODO - do another test for each element in the rows
    public void PositionStarsAllTime() {
        header.menuTopChivers().click();
        top.tabAllTime().click();
        List<WebElement> rows = top.allRows();
        int i = 1;
        for (WebElement row : rows) {
            Assert.assertEquals(row.findElement(By.className(top.starIconClass())).getText(), String.valueOf(i),
                    "Number in this star should be " + i + ", found" + row.findElement(By.className(top.starIconClass())).getText());
            i++;
        }
    }

    @Test
    public void PositionStarsPastMonth() {
        header.menuTopChivers().click();
        top.tabPastMonth().click();
        List<WebElement> rows = top.allRows();
        int i = 1;
        for (WebElement row : rows) {
            Assert.assertEquals(row.findElement(By.className(top.starIconClass())).getText(), String.valueOf(i),
                    "Number in this star should be " + i + ", found" + row.findElement(By.className(top.starIconClass())).getText());
            i++;
        }
    }

    @Test
    public void RecentlyVerified() throws InterruptedException {
        header.menuTopChivers().click();
        String verifiedUser = top.recentlyVerifiedUser().getText().replace("@", "");
        //ButtonDisplays
        try {
            Assert.assertTrue(top.recentlyVerified().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("Recently Verified not found");
            Assert.fail();
        }

        top.recentlyVerifiedUser().click();

        Thread.sleep(3000);

        try {
            Assert.assertTrue(driver.getCurrentUrl().contains(verifiedUser));
        } catch (AssertionError e) {
            System.out.println("Did not get to the verified user's page");
            Assert.fail();
        }
    }

    @Test
    public void UserNamesAllTime() {
        header.menuTopChivers().click();
        top.tabAllTime().click();
        List<WebElement> rows = top.allRows();
        List<String> userNames = new ArrayList<>();
        Set<String> uniqueNames = new HashSet<>();
        for (WebElement row : rows) {
            userNames.add(row.findElement(By.cssSelector(top.UserNameSelector())).getText());
        }
        for (String userName : userNames) {
            Assert.assertTrue(uniqueNames.add(userName), "Found a duplicate entry: " + userName);
        }
    }

    @Test
    public void UserNamesPastMonth() {
        header.menuTopChivers().click();
        top.tabPastMonth().click();
        List<WebElement> rows = top.allRows();
        List<String> userNames = new ArrayList<>();
        Set<String> uniqueNames = new HashSet<>();
        for (WebElement row : rows) {
            userNames.add(row.findElement(By.cssSelector(top.UserNameSelector())).getText());
        }
        for (String userName : userNames) {
            Assert.assertTrue(uniqueNames.add(userName), "Found a duplicate entry: " + userName);
        }
    }

    @Test
    public void ShareIcon() {
        header.menuTopChivers().click();
        Assert.assertTrue(top.shareIcon().isDisplayed(), "Missing share icon");
    }


    @Test
    public void SocialLinksFacebook() throws InterruptedException {
        header.menuTopChivers().click();
        top.socialFacebookLink().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Thread.sleep((1000));
        Assert.assertTrue(driver.getCurrentUrl().contains("facebook"), "Expected facebook.com, found url: " + driver.getCurrentUrl());
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SocialLinksInsta() {
        header.menuTopChivers().click();
        top.socialInstagramLink().click();
        WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getCurrentUrl().contains("instagram"), "Expected instagram.com, found url: " + driver.getCurrentUrl());

        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SocialLinksTwitter() throws InterruptedException {
        header.menuTopChivers().click();
        top.socialTwitterLink().click();
        WindowUtil.switchToWindow(driver, 1);
        Thread.sleep(2000);
        Assert.assertTrue(driver.getCurrentUrl().contains("twitter"), "Expected twitter.com, found url: " + driver.getCurrentUrl());
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void TagsDisplay() {
        if (driver.getCurrentUrl().equals("https://mychive.com")) {
            header.menuTopChivers().click();
            Assert.assertTrue(top.tag().isDisplayed(), "Trending tag not found");
        } else {
            System.out.println("Skipped, Trending Tags won't load on " + config.url);
        }
    }

    @Test
    public void TopChiversRibbon() {
        //TODO - figure out how to grab that orphaned Top Chivers text
        header.menuTopChivers().click();
        Assert.assertTrue(top.topChiversHeader().isDisplayed(), "Top Chivers logo didn't load");
    }

    @Test
    public void TopChiversArrow() {
        header.menuTopChivers().click();
        Assert.assertTrue(top.trendArrowIcon().isDisplayed());
    }

    @Test
    public void TopChiversAvatar() {
        header.menuTopChivers().click();
        Assert.assertTrue(top.topChiversProfilePic().isDisplayed(), "Did not find the profile pic ");
    }

    @Test
    public void UserNameLink() throws InterruptedException {
        header.menuTopChivers().click();
        String userName = top.UserName().getText().replace("@", "");
        top.UserName().click();
        Thread.sleep(2000);
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains(userName), "User's page did not load on clicking username  ");
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}


