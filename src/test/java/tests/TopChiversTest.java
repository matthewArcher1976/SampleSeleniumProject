package tests;

import helpers.Logins;
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
import resources.TestRunner;

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
    TopChiversPage topChiversPage;
    PageHeaderPage header;
    Shadow shadow;

    //************************** Setup ******************************************

    private float convertPointsToNumber(String pointsText) {
        pointsText = pointsText.replace(" Points", "").trim();
        if (pointsText.endsWith("M")) {
            return Float.parseFloat(pointsText.replace("M", "")) * 1000000;
        } else if (pointsText.endsWith("k")) {
            return Float.parseFloat(pointsText.replace("k", "")) * 1000;
        } else {
            return Float.parseFloat(pointsText);
        }
    }
    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        shadow = new Shadow(driver);
        login = new Logins(driver);
        header = new PageHeaderPage(driver);
        action = new Actions(driver);
        topChiversPage = new TopChiversPage(driver);
    }

    @BeforeTest
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, System.getenv("TEST_PWD"));
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() throws InterruptedException {
        driver.get(config.url);
        Thread.sleep(3000);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void AvatarImages() throws InterruptedException {

        header.menuTopChivers().click();
        Thread.sleep(10000);//debugging
        List<WebElement> rows = topChiversPage.allRows();
        int i = 1;
        for (WebElement row : rows) {
            Assert.assertTrue(row.findElement(topChiversPage.ChivetteAvatarBy()).isDisplayed(), "Did not find an avatar image on row " + i);
            i++;
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void DisplayMonthFirst() throws InterruptedException {
        header.menuTopChivers().click();
        Thread.sleep(20000);//https://resignationmedia.atlassian.net/browse/MYCHIVE-1131
        Assert.assertEquals(topChiversPage.tabPastMonth().getAttribute("aria-selected"), "true", "Past Month should be selected by default");
    }

    @Test
    public void FollowButtonClick() throws InterruptedException {
        header.menuTopChivers().click();
        topChiversPage.FollowBtn().getText();
        if (topChiversPage.FollowBtn().getText().contentEquals("FOLLOW")) {
            topChiversPage.FollowBtn().click();
            Thread.sleep(2000);//i know
            Assert.assertTrue(topChiversPage.FollowBtn().getText().contentEquals("FOLLOWING"), "Clicking FOLLOW button did not change it to FOLLOWING, got " + topChiversPage.FollowBtn().getText());
            Assert.assertTrue(topChiversPage.followCheck().isDisplayed(), "Missing checkmark in FOLLOWING");
        } else if (topChiversPage.FollowBtn().getText().contentEquals("FOLLOWING")) {
            topChiversPage.FollowBtn().click();
            Thread.sleep(2000);//yeah
            Assert.assertTrue(topChiversPage.FollowBtn().getText().contentEquals("FOLLOW"), "Clicking FOLLOWING button did not change it to FOLLOW");
            Assert.assertTrue(topChiversPage.followCircle().isDisplayed(), "Missing checkmark in FOLLOWING");
        } else if (!topChiversPage.FollowBtn().getText().contentEquals("FOLLOW") && !topChiversPage.FollowBtn().getText().contentEquals("FOLLOWING")) {
            Assert.fail("Something is wrong with the follow button");
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void FollowButtonHover() throws InterruptedException {
        header.menuTopChivers().click();
        if (topChiversPage.FollowBtn().getText().contentEquals("FOLLOWING")) {
            topChiversPage.FollowBtn().click();
            Thread.sleep(2000);
            driver.navigate().refresh();//for some reason the color doesn't change back in the test though it does if you do it manually. refresh as a workaround
        }
        String beforeHover = topChiversPage.FollowBtn().getCssValue("border-color");
        action.moveToElement(topChiversPage.FollowBtn()).perform();
        Thread.sleep(2000);
        Assert.assertNotEquals(topChiversPage.FollowBtn().getCssValue("border-color"), beforeHover, "Before mouseover: " + beforeHover + " after mouseover: " + topChiversPage.FollowBtn().getCssValue("border-color"));
    }

    @Test
    public void FollowButtonsAllTime(){
        header.menuTopChivers().click();
        topChiversPage.tabAllTime().click();
        List<WebElement> rows = topChiversPage.allRows();
        int i = 1;
        for(WebElement row:rows){
            Assert.assertTrue(row.findElement(topChiversPage.FollowBtnBy()).isDisplayed(), "Missing the follow button on row " + i);
            i++;
        }
    }

    @Test
    public void FollowButtonsMonthly(){
        header.menuTopChivers().click();
        topChiversPage.tabPastMonth().click();
        List<WebElement> rows = topChiversPage.allRows();
        int i = 1;
        for(WebElement row:rows){
            Assert.assertTrue(row.findElement(topChiversPage.FollowBtnBy()).isDisplayed(), "Missing the follow button on row " + i);
            i++;
        }
    }

    @Test
    public void PointsInOrderAllTime(){
        header.menuTopChivers().click();
        topChiversPage.tabAllTime().click();
        List<WebElement> rows = topChiversPage.allRows();
        float lastPoint = convertPointsToNumber(topChiversPage.firstRow().findElement(By.cssSelector(topChiversPage.pointsSelector())).getText());
        float thisPoint;
        for(WebElement row : rows){
            thisPoint = convertPointsToNumber(row.findElement(By.cssSelector(topChiversPage.pointsSelector())).getText());
            Assert.assertTrue(lastPoint >= thisPoint, "thisPoint is " + thisPoint + " lastPoint is " + lastPoint + ", The rows may be out of order");
            lastPoint = thisPoint;
        }
    }


    @Test
    public void PointsInOrderPastMonth(){
        header.menuTopChivers().click();
        topChiversPage.tabPastMonth().click();
        List<WebElement> rows = topChiversPage.allRows();
        float lastPoint = convertPointsToNumber(topChiversPage.firstRow().findElement(By.cssSelector(topChiversPage.pointsSelector())).getText());
        float thisPoint;
        for(WebElement row : rows){
            thisPoint = convertPointsToNumber(row.findElement(By.cssSelector(topChiversPage.pointsSelector())).getText());
            Assert.assertTrue(lastPoint >= thisPoint, "thisPoint is " + thisPoint + " lastPoint is " + lastPoint + ", The rows may be out of order");
            lastPoint = thisPoint;
        }
    }

    @Test//TODO - do another test for each element in the rows
    public void PositionStarsAllTime() {
        header.menuTopChivers().click();
        topChiversPage.tabAllTime().click();
        List<WebElement> rows = topChiversPage.allRows();
        int i = 1;
        for (WebElement row : rows) {
            Assert.assertEquals(row.findElement(By.className(topChiversPage.starIconClass())).getText(), String.valueOf(i),
                    "Number in this star should be " + i + ", found" + row.findElement(By.className(topChiversPage.starIconClass())).getText());
            i++;
        }
    }

    @Test
    public void PositionStarsPastMonth() {
        header.menuTopChivers().click();
        topChiversPage.tabPastMonth().click();
        List<WebElement> rows = topChiversPage.allRows();
        int i = 1;
        for (WebElement row : rows) {
            Assert.assertEquals(row.findElement(By.className(topChiversPage.starIconClass())).getText(), String.valueOf(i),
                    "Number in this star should be " + i + ", found" + row.findElement(By.className(topChiversPage.starIconClass())).getText());
            i++;
        }
    }

    @Test
    public void RecentlyVerified() throws InterruptedException {
        header.menuTopChivers().click();
        String verifiedUser = topChiversPage.recentlyVerifiedUser().getText().replace("@", "");
        //ButtonDisplays
        try {
            Assert.assertTrue(topChiversPage.recentlyVerified().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("Recently Verified not found");
            Assert.fail();
        }
        topChiversPage.recentlyVerifiedUser().click();
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
        topChiversPage.tabAllTime().click();
        List<WebElement> rows = topChiversPage.allRows();
        List<String> userNames = new ArrayList<>();
        Set<String> uniqueNames = new HashSet<>();
        for (WebElement row : rows) {
            userNames.add(row.findElement(By.cssSelector(topChiversPage.UserNameSelector())).getText());
        }
        for (String userName : userNames) {
            Assert.assertTrue(uniqueNames.add(userName), "Found a duplicate entry: " + userName);
        }
    }

    @Test
    public void UserNamesPastMonth() {
        header.menuTopChivers().click();
        topChiversPage.tabPastMonth().click();
        List<WebElement> rows = topChiversPage.allRows();
        List<String> userNames = new ArrayList<>();
        Set<String> uniqueNames = new HashSet<>();
        for (WebElement row : rows) {
            userNames.add(row.findElement(By.cssSelector(topChiversPage.UserNameSelector())).getText());
        }
        for (String userName : userNames) {
            Assert.assertTrue(uniqueNames.add(userName), "Found a duplicate entry: " + userName);
        }
    }

    @Test
    public void ShareIcon() {
        header.menuTopChivers().click();
        Assert.assertTrue(topChiversPage.shareIcon().isDisplayed(), "Missing share icon");
    }


    @Test
    public void SocialLinksFacebook() throws InterruptedException {
        header.menuTopChivers().click();
        topChiversPage.socialFacebookLink().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Thread.sleep((1000));
        Assert.assertTrue(driver.getCurrentUrl().contains("facebook"), "Expected facebook.com, found url: " + driver.getCurrentUrl());
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SocialLinksInsta() {
        header.menuTopChivers().click();
        topChiversPage.socialInstagramLink().click();
        WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getCurrentUrl().contains("instagram"), "Expected instagram.com, found url: " + driver.getCurrentUrl());

        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SocialLinksTwitter() throws InterruptedException {
        header.menuTopChivers().click();
        topChiversPage.socialTwitterLink().click();
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
            Assert.assertTrue(topChiversPage.tag().isDisplayed(), "Trending tag not found");
        } else {
            System.out.println("Skipped, Trending Tags won't load on " + config.url);
        }
    }

    @Test
    public void TopChiversRibbon() {
        //TODO - figure out how to grab that orphaned Top Chivers text
        header.menuTopChivers().click();
        Assert.assertTrue(topChiversPage.topChiversHeader().isDisplayed(), "Top Chivers logo didn't load");
    }

    @Test
    public void TopChiversArrow() {
        header.menuTopChivers().click();
        Assert.assertTrue(topChiversPage.trendArrowIcon().isDisplayed());
    }

    @Test
    public void TopChiversAvatar() {
        header.menuTopChivers().click();
        Assert.assertTrue(topChiversPage.topChiversProfilePic().isDisplayed(), "Did not find the profile pic ");
    }

    @Test
    public void UserNameLink() throws InterruptedException {
        header.menuTopChivers().click();
        String userName = topChiversPage.UserName().getText().replace("@", "");
        topChiversPage.UserName().click();
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


