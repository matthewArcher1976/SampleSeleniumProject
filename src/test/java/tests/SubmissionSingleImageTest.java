package tests;

import helpers.PrettyAsserts;
import helpers.Waiter;
import resources.Config;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.PageHeaderPage;
import pages.SubmissionCardsPage;
import pages.SubmissionSingleImagePage;
import resources.RetryAnalyzer;
import resources.TestConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class SubmissionSingleImageTest {

    WebDriver driver;
    SubmissionSingleImagePage single;
    SubmissionCardsPage card;
    PageHeaderPage header;
    Actions action;
    Logins login;
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
        single = new SubmissionSingleImagePage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, System.getenv("TEST_PWD"));
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() throws InterruptedException {
        driver.get(config.url);
        Thread.sleep(2000);
    }

    //************************** Begin Tests *******************************************

    @Test
    public void AvatarImage() {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        Assert.assertTrue(single.avatar().isDisplayed(), "AvatarImage didn't display");
    }

    @Test
    public void DDumpDoesNotDisplay() {
        Assert.assertTrue(header.dopamineDump().isDisplayed(), "Dopamine Dump header missing");
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        try {
            helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("dopamine-dump-counter")));
            Assert.fail("Dump should not display");
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void FavoriteButton() throws InterruptedException {
        //Favorite a post from the single page, verify the vote persists
        WebElement submission = card.cardNotFavorited();
        String submissionID = helpers.StringHelper.getIntFromMixedStringAsString(submission.getAttribute("id"));
        action.moveToElement(submission).click().perform();
        driver.navigate().refresh();
        single.favoriteBtn().click();
        // the click on Latest keeps missing for some reason
        int tries = 0;
        boolean isUrlContainsSubmissionID = driver.getCurrentUrl().contains(submissionID);
        while (isUrlContainsSubmissionID && tries < 3) {
            header.menuLatest().click();
            Thread.sleep(2000);
            isUrlContainsSubmissionID = driver.getCurrentUrl().contains(submissionID);
            tries++;
        }

        Thread.sleep(3000);//Won't work without this
        submission = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("submission-" + submissionID)));
        try {
            Assert.assertTrue(PrettyAsserts.isIconSelected(submission.findElement(By.cssSelector("[id^='toggle-favorite-']")).findElement(By.className("fa-heart"))));
            submission.findElement(By.cssSelector("[id^='toggle-favorite-']")).findElement(By.className("fa-heart")).click(); //teardown
        } catch (AssertionError e) {
            submission.findElement(By.cssSelector("[id^='toggle-favorite-']")).findElement(By.className("fa-heart")).click(); //teardown
            Assert.fail("Favorite from single page did not persist on Latest list");
        }
    }

    @Test(enabled = false)//filters have been removed entirely
    public void FiltersDoNotDisplay() {
        helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '(change filter)')]")));
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        try {
            helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '(change filter)')]")));
            Assert.fail("Filters should not display");
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }

    @Test(enabled = false)//we changed this to display at a higher resolution
    public void ImageMaxWidth600() {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
        driver.get(single.submissionImage().getAttribute("src"));
        driver.manage().window().fullscreen();
        int width = Integer.parseInt(single.sourceImage().getCssValue("width").replaceAll("[^0-9]", ""));
        int height = Integer.parseInt(single.sourceImage().getCssValue("height").replaceAll("[^0-9]", ""));

        Assert.assertTrue(width <= 600 && height <= 1200, "ImageDisplays failed, found " + width + " for the width and " + height + " for the height");
    }

    @Test()
    public void ReportPostIconHover() throws InterruptedException {
        header.headerAvatar().click();
        header.yourProfileBtn().click();
        Thread.sleep(2000);
        String userName = helpers.StringHelper.getUsernameFromURL(driver.getCurrentUrl());
        header.menuLatest().click();
        action.moveToElement(card.cardNotMine(userName)).click().perform();
        driver.navigate().refresh();
        WebElement flag = single.reportFlag();
        System.out.println("Here?");
        Assert.assertEquals(flag.getCssValue("color"), "rgba(255, 255, 255, 1)", "Report flag should change color on hover");
        action.moveToElement(flag).perform();
        Assert.assertEquals(flag.getCssValue("color"), "rgba(0, 195, 0, 1)", "Report flag should change color on hover");
    }

    @Test
    public void ShareFacebook() {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        single.shareFacebook().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("Facebook")), "Twitter window title was " + driver.getTitle());
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void ShareTwitter() throws InterruptedException {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        single.shareTwitter().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Thread.sleep(2000);
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.titleContains("X")), "Twitter window title was " + driver.getTitle());
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SocialLinksFacebook() {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        single.followFacebook().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("facebook.com/theCHIVE")), "Link to Facebook was " + driver.getCurrentUrl());
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SocialLinksInsta() {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        single.followInsta().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("https://www.instagram.com/theCHIVE")), "Insta URL was" + driver.getCurrentUrl());
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SocialLinksTwitter() throws InterruptedException {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        single.followTwitter().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Thread.sleep(2000);
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("twitter.com")), "Twitter URL was " + driver.getCurrentUrl());
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SubmissionTagsDisplay() throws InterruptedException {
        WebElement taggedCard = card.cardWithTags();
        List<WebElement> tags = card.allTags();
        List<String> tagStrings = new ArrayList<>();
        for (WebElement tag : tags) {
            tagStrings.add(tag.getText());
        }
        taggedCard.click();
        driver.navigate().refresh();
        List<WebElement> singleTags = single.submissionTags();
        List<String> singleTagStrings = new ArrayList<>();
        for (WebElement tag : singleTags) {
            singleTagStrings.add(tag.getText());
        }
        Collections.sort(tagStrings);
        Collections.sort(singleTagStrings);
        Assert.assertEquals(tagStrings, singleTagStrings, "The tags on the modal and single page don't match");
    }

    @Test
    public void TitleDisplays() {
        String postTitle = card.submissionTitle().getText();
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        System.out.println("postTitle is " + postTitle + " getTitle() is " + driver.getTitle());
        Assert.assertTrue(driver.getTitle().contains(postTitle), "Title for submission was wrong or did not display");
    }

    @Test

    public void TrendingDisplays() {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        Assert.assertTrue(single.trendArrowIcon().isDisplayed() && single.trendingText().isDisplayed(), "Missing the Trending text and icon");
    }

    @Test()//these tags don't show on qa and stage because of the way it is
    public void TrendingTagsDisplay() {
        if(!driver.getCurrentUrl().contains("qa") && !driver.getCurrentUrl().contains("stage")) {
            action.moveToElement(card.firstCard()).click().perform();
            driver.navigate().refresh();
            Assert.assertFalse(single.trendingTags().isEmpty(), "Missing the Trending tags");
        }
    }

    @Test
    public void URLLoadsSingleImagePage() {
        WebElement subCard = card.firstCard();
        action.moveToElement(subCard).click().perform();
        driver.navigate().refresh();
        try {
            helpers.Waiter.quickWait(driver).until(ExpectedConditions.stalenessOf(subCard));
            Assert.assertTrue(true);
        } catch (AssertionError e) {
            Assert.fail("Submission modal should close on refresh");
        }
    }

    @Test
    public void UsersNameDisplays() {
        WebElement ourCard = card.firstCard();
        System.out.println(ourCard.getAttribute("class"));
        String userName = ourCard.findElement(By.cssSelector("a[class*='text-primary']")).getText();
        action.moveToElement(ourCard).click().perform();
        driver.navigate().refresh();
        String userNameSingle = driver.findElement(By.cssSelector("a[class*='text-primary']")).getText();
        Assert.assertEquals(userName, userNameSingle, "User name for submission was wrong or did not display");
    }

    @Test
    public void VerifiedDisplays() {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        Assert.assertTrue(single.verifiedCheckIcon().isDisplayed() && single.verifiedText().isDisplayed(), "Missing the Verified text and icon");
    }

    @Test
    public void VerifiedUserDate() {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        Assert.assertTrue(single.verifiedDate().isDisplayed()
                && !single.verifiedDate().getText().isEmpty()
                && !single.verifiedDate().getText().isBlank(), "Missing the Verified text and icon");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void VerifiedUserDisplays() {
        action.moveToElement(card.firstCard()).click().perform();
        driver.navigate().refresh();
        Assert.assertTrue(single.verifiedRecentUser().isDisplayed()
                && !single.verifiedRecentUser().getText().isEmpty()
                && !single.verifiedText().getText().isBlank(), "Missing the Verified text and icon");
    }

    @Test(enabled = false)
    public void VoteDownButton() {
        //Downvote a post from the single page, verify the vote persists
        WebElement submission = card.cardNotDownvoted();
        String submissionID = helpers.StringHelper.getIntFromMixedStringAsString(submission.getAttribute("id"));
        action.moveToElement(submission).click().perform();
        driver.navigate().refresh();
        single.downvoteBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.attributeContains(single.downvoteBtn(), "class", "text-white")));
        driver.get(config.url);
        submission = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("submission-" + submissionID)));
        try {
            Assert.assertTrue(PrettyAsserts.isIconSelected(submission.findElement(By.className("fa-thumbs-down"))));
            submission.findElement(By.className("fa-thumbs-down")).click(); //teardown
        } catch (AssertionError e) {
            submission.findElement(By.className("fa-thumbs-down")).click(); //teardown
            Assert.fail("Downvote from single page did not persist on Latest list");
        }
    }

    @Test
    public void VoteUpButton() {
        //Like a post from the single page, verify the vote persists
        WebElement submission = card.cardNotUpvoted();
        String submissionID = helpers.StringHelper.getIntFromMixedStringAsString(submission.getAttribute("id"));
        action.moveToElement(submission).click().perform();
        driver.navigate().refresh();
        single.upvoteBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.attributeContains(single.upvoteBtn(), "class", "text-white")));
        driver.get(config.url);
        submission = Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("submission-" + submissionID)));
        try {
            Assert.assertTrue(PrettyAsserts.isIconSelected(submission.findElement(By.className("fa-thumbs-up"))), "Upvote from single page did not persist on Latest list");
        } catch (AssertionError e) {
            submission.findElement(By.className("fa-thumbs-up")).click();
            Assert.fail("Upvote from single page did not persist on Latest list");
        }
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
