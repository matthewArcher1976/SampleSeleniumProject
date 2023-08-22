package tests;

import helpers.Drivers;
import helpers.GetInteger;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.PageHeaderPage;
import pages.ProfilePage;
import pages.SubmissionCardsPage;

import java.util.List;
import java.util.Set;

@SuppressWarnings("DefaultAnnotationParam")
public class SubmissionCardsTest {

    WebDriver driver = Drivers.ChromeDriver();
    SubmissionCardsPage card = new SubmissionCardsPage(driver);
    PageHeaderPage pageHeader = new PageHeaderPage(driver);
    ProfilePage profile = new ProfilePage(driver);
    Logins login = new Logins(driver);
    Actions action = new Actions(driver);

    @BeforeTest
    @Parameters({"unpaidEmail", "password", "url"})
    public void login(@Optional("thechivetest@gmail.com") String unpaidEmail, @Optional("Chive1234") String password, @Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
        driver.get(url);
        login.unpaidLogin(unpaidEmail, password, driver);
        Thread.sleep(1000);
    }

    @BeforeMethod
    @Parameters({"url"})
    public void setDriver(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
    }

    @AfterClass
    public void TearDown() {
        driver.close();
    }

    //************************** Begin Tests ********************************************

    @Test
    public void ClickUserName() {
        String userName = card.userName().getText().replace("@", "");
        card.userName().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains(userName));
        Assert.assertTrue(driver.getCurrentUrl().contains(userName), "Did not redirect to user's page");
    }

    @Test
    public void CommentButton() {
        String id = Integer.toString(GetInteger.getIntFromMixedString(card.firstCard().getAttribute("id")));
        card.commentIcon().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
        Assert.assertTrue(driver.getCurrentUrl().contains(id) && card.disqusSection().isDisplayed(), "The comment page did not load");
    }

    @Test
    public void DownVoteButton() throws InterruptedException {
        //Check upvote button first
        if (card.upvoteBtn().getAttribute("class").contains("text-white")) {
            card.upvoteBtn().click();
        }
        Thread.sleep(4000);
        if (card.downvoteBtn().getAttribute("class").contains("text-white")) {
            card.downvoteBtn().click();
            Thread.sleep(2000);
            Assert.assertFalse(card.downvoteBtn().getAttribute("class").contains("text-white"), "Downvote button did not turn green after clicking it");
            Assert.assertTrue(card.upvoteBtn().getAttribute("class").contains("text-white"), "After clicking downvote the upvote button is green");
        }
        //Uncheck the upvote button
        card.downvoteBtn().click();
        Thread.sleep(2000);//yes
        Assert.assertTrue(helpers.Waiter.wait(driver).until((ExpectedConditions.attributeContains(card.downvoteBtn(), "class", "text-white"))), "Checked upvote button did not turn white after clicking it");
    }

    @Test(enabled = true, priority = 1)
    public void FilterPageHotness() {
        pageHeader.filterHotness().click();
        //	Thread.sleep(3000);//remove with bug fix
        action.moveToElement(card.firstCard()).click().perform();
        Assert.assertTrue(driver.findElement(By.linkText("Hotness")).isDisplayed(), "FilterPageHotness - found a card not tagged Hotness");
    }

    @Test(enabled = true, priority = 1)
    public void FilterPageHumanity() throws InterruptedException {
        pageHeader.filterHumanity().click();
        Thread.sleep(3000);//remove with bug fix
        action.moveToElement(card.firstCard()).click().perform();
        Assert.assertTrue(driver.findElement(By.linkText("Humanity")).isDisplayed(), "FilterPageHumanity - found a card not tagged Hotness");
    }

    @Test(enabled = true, priority = 1)
    public void FilterPageHumor() {
        pageHeader.filterHumor().click();
        //Thread.sleep(3000);//remove with bug fix
        action.moveToElement(card.firstCard()).click().perform();
        Assert.assertTrue(driver.findElement(By.linkText("Humor")).isDisplayed(), "FilterPageHumor - found a card not tagged Hotness");
    }

    @Test(enabled = true, priority = 2)
    @Parameters({"url"})
    public void FollowingShowsFollowedUsers(@Optional("https://qa.chive-testing.com/") String url) throws InterruptedException {
        pageHeader.menuFollowing().click();
        helpers.PageActions.scrollDown(driver, 1);
        driver.navigate().refresh();//I don't know why but it's getting random usernames if I don't refresh
        Set<String> followedUsers = card.getAllUserNames();
        for (String user : followedUsers) {
            driver.get(url + user.replace("@", ""));
            Thread.sleep(3000);//can't get it to work without this
            Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.followButton().findElement(By.className("fa-check")))).isDisplayed(), "Found an unfollowed user in the Following posts: " + user);
            driver.navigate().back();
        }
    }

    @Test
    public void InfiniteScroll() throws InterruptedException {
        Thread.sleep(3000);
        int first = card.allCards().size();
        helpers.PageActions.scrollDown(driver, 3);
        Thread.sleep(3000);
        int second = card.allCards().size();
        Assert.assertTrue(second > first, "Cards do not seem to be loading when you scroll down");
    }

    @Test(enabled = true, priority = 1)
    public void MouseoversVoteBtns() throws InterruptedException {
        Actions a = new Actions(driver);
        //see if the upvote button is selected then unselect it if it is
        if (card.isSelected(card.upvoteBtn())) {
            card.upvoteBtn().click();
            Thread.sleep(2000);
        }
        a.moveToElement(card.upvoteBtn()).perform();
        Assert.assertEquals(card.upvoteBtn().getCssValue("color"), "rgba(0, 195, 0, 1)", "Upvote button color did not change on mouseover");
        // DownvoteButton
        if (card.isSelected(card.downvoteBtn())) {
            card.downvoteBtn().click();
            Thread.sleep(2000);
        }
        a.moveToElement(card.downvoteBtn()).perform();
        Assert.assertEquals(card.downvoteBtn().getCssValue("color"), "rgba(0, 195, 0, 1)", "Downvote button color did not change on mouseover");
    }

    @Test(enabled = true, priority = 1)// TODO - fix this, border-color may work
    public void MouseOverFavoriteBtn() throws InterruptedException {
        Actions a = new Actions(driver);
        if (card.isSelected(card.favoriteBtn())) {
            card.favoriteBtn().click();
            Thread.sleep(2000);
        }
        a.moveToElement(card.favoriteBtn()).perform();
        Thread.sleep(2000);
        Assert.assertEquals(card.favoriteBtn().getCssValue("color"), "rgba(0, 195, 0, 1)", "Favorite button color did not change on mouseover");
    }

    @Test(enabled = true, priority = 1)
    public void MouseOverCommentBtn() throws InterruptedException {
        Actions a = new Actions(driver);
        a.moveToElement(card.commentIcon()).perform();
        Thread.sleep(2000);
        Assert.assertEquals(card.commentIcon().getCssValue("color"), "rgba(0, 195, 0, 1)", "Comment button color did not change on mouseover ");
    }

    @Test(enabled = true, priority = 1)
    public void MouseOverGIF() throws InterruptedException {
        helpers.PageActions.scrollDown(driver, 3);
        WebElement ourGIF = card.firstGIF();
        String ourGIFid = ourGIF.getAttribute("id");
        action.moveToElement(ourGIF).build().perform();
        Thread.sleep(7000);
        Assert.assertTrue(driver.findElement(By.id(ourGIFid)).findElement(By.className("overflow-hidden")).getAttribute("class").contains("invisible"), "Still see the GIF icon after mouseover on the GIF");
    }

    @Test(enabled = true, priority = 1)
    public void MouseOverReportBtn() throws InterruptedException {
        Actions a = new Actions(driver);
        a.moveToElement(card.reportBtn()).perform();
        Thread.sleep(1000);
        Assert.assertEquals(card.reportBtn().getCssValue("color"), "rgba(0, 195, 0, 1)", "Report button color did not change on mouseover");
    }

    //Can't get these overlay tests to work reliably
    @Test(enabled = true, priority = 1)
    public void OverlayDownvote() {
        WebElement ourCard = card.cardNotDownvoted();
        ourCard.findElement(By.className("fa-thumbs-down")).click();
       // Thread.sleep(1000);//yes
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(card.voteDownOverlay())).isDisplayed(),"Downvote overlay not found");
    }

    @Test(enabled = true, priority = 1)
    public void OverlayFavorite() throws InterruptedException {
        WebElement ourCard = card.cardNotFavorited();
        ourCard.findElement(By.className("fa-heart")).click();
        Thread.sleep(1000);//yes
        Assert.assertTrue(card.voteUpOverlay().isDisplayed(), "Upvote overlay not found");
    }

    @Test(enabled = true, priority = 1)
    public void OverlayUpvote() throws InterruptedException {
        WebElement ourCard = card.cardNotUpvoted();
        ourCard.findElement(By.className("fa-thumbs-up")).click();
        Thread.sleep(1000);//yes
        Assert.assertTrue(card.voteUpOverlay().isDisplayed(), "Upvote overlay not found");
    }

    @Test(enabled = true, priority = 1)
    public void ReportPostButtons() {
        card.reportBtn().click();
        Assert.assertTrue(card.reportOffensive().isEnabled()
                && card.reportSpam().isEnabled()
                && card.reportCopyright().isEnabled()
                && card.reportOther().isEnabled(), "ReportPost - report modal buttons not found or enabled");
    }

    @Test(enabled = true, priority = 1)
    public void ReportPostText() {
        card.reportBtn().click();
        Assert.assertTrue(card.reportModalText().getAttribute("innerText").contains("Does this kind of stuff bother you? Does it contain stuff it shouldn't? Let us know, we'll take care of it.")
                && card.reportModalHeader().getAttribute("innerText").contains("Report"), "Didn't find the report modal text");
    }

    @Test(enabled = true, priority = 1)
    public void SubmissionImageWidth() throws InterruptedException {
        helpers.PageActions.scrollDown(driver, 2);
        Thread.sleep(2000);
        List<WebElement> images = card.allImages();
        int width = images.get(0).getRect().getWidth();
        for (WebElement image : images) {
            Assert.assertEquals(width, image.getRect().getWidth(), "The images should have the same width");
        }
    }

    @Test(enabled = true, priority = 1)
    public void SubmissionURLMatchesCard() throws InterruptedException {
        WebElement ourCard = card.cardNotGIF();
        String subID = ourCard.getAttribute("id");
        int subInt = GetInteger.getIntFromMixedString(subID);
        ourCard.click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
        String url = driver.getCurrentUrl();
        int urlInt = GetInteger.getIntFromMixedString(url);
        Assert.assertEquals(subInt, urlInt, "The post id didn't match the url");
    }

    @Test(enabled = true, priority = 1)
    @Parameters({"unpaidEmail", "password"})
    //working manually but the button isn't changing color in the automated test, also stale element exception for some reason
    public void UpvoteButton() {
        //Upvote Button is already checked
        WebElement ourCard = card.cardNotUpvoted();
        //Uncheck the upvote button
        ourCard.findElement(By.className("fa-thumbs-up")).click();
        Assert.assertFalse(ourCard.getAttribute("class").contains("text-white"), "Checked upvote button did not turn white after clicking it ");
    }

    @Test(enabled = true, priority = 1)
    public void VoteCounter() throws InterruptedException {
        WebElement ourCard = card.cardNotUpvoted();
        String ourID = ourCard.getAttribute("id");
        int votes1 = Integer.parseInt(ourCard.findElement(By.cssSelector("div[id^='vote-counter']")).getText());
        ourCard.findElement(By.className("fa-thumbs-up")).click();
        Thread.sleep(5000);//get the waiter working again
        driver.navigate().refresh();//for some reason the counter doesn't increment after clicking in the automated test though it does when you try manually, this refresh makes it work though
        int votes2 = Integer.parseInt(driver.findElement(By.id(ourID)).findElement(By.cssSelector("div[id^='vote-counter']")).getText());
        Assert.assertTrue(votes2 > votes1, "VoteCounter - counter did not increment, votes1 is " + votes1 + " and votes2 is " + votes2);
    }
}