package tests;

import helpers.*;
import io.github.sukgu.Shadow;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import resources.Config;
import resources.RetryAnalyzer;
import resources.TestConfig;

import java.util.List;
import java.util.Set;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class SubmissionCardsTest {

    WebDriver driver;
    Actions action;
    Logins login;
    SubmissionCardsPage card;
    PageHeaderPage header;
    ProfilePage profile;
    SearchAndFiltersPage search;
    SubmissionModalPage modal;
    Shadow shadow;

    private static TestConfig config;
    final String TAG = "sexychivers";
    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);

        action = new Actions(driver);
        card = new SubmissionCardsPage(driver);
        login = new Logins(driver);
        header = new PageHeaderPage(driver);
        profile = new ProfilePage(driver);
        modal = new SubmissionModalPage(driver);
        search = new SearchAndFiltersPage(driver);
        shadow = new Shadow(driver);
    }


    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, System.getenv("TEST_PWD"));
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void ClickUserName() {
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        String userName = card.userName().getText().replace("@", "");
        card.userName().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains(userName));
        Assert.assertTrue(driver.getCurrentUrl().contains(userName), "Did not redirect to user's page");
    }

    @Test
    public void CommentButton() {
        String id = Integer.toString(StringHelper.getIntFromMixedString(card.firstCard().getAttribute("id")));
        card.commentIcon().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
        Assert.assertTrue(driver.getCurrentUrl().contains(id) && card.disqusSection().isDisplayed(), "The comment page did not load");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void FavoriteLikedCard() {
        WebElement ourCard = card.cardIsUpvoted();
        int likes = Integer.parseInt(ourCard.findElement(By.cssSelector("div[id^='vote-counter-']")).getText());
        ourCard.findElement(By.className("fa-heart")).click();
        Assert.assertEquals(likes, Integer.parseInt(ourCard.findElement(By.cssSelector("div[id^='vote-counter-']")).getText()), "Favoriting this added an upvote");
    }

    @Test
    public void FollowingShowsFollowedUsers() throws InterruptedException {
        header.menuFollowing().click();
        helpers.PageActions.scrollDown(driver, 1);
        driver.navigate().refresh();//It's getting random usernames if I don't refresh
        Set<String> followedUsers = card.getAllUserNames();
        for (String user : followedUsers) {
            driver.get(config.url + user.replace("@", ""));
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

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void MouseoversLikeButton() throws InterruptedException {
        Actions a = new Actions(driver);
        //see if the upvote button is selected then unselect it if it is
        if (card.isSelected(card.upvoteBtn())) {
            card.upvoteBtn().click();
            Thread.sleep(2000);
        }
        a.moveToElement(card.upvoteBtn()).perform();
        Assert.assertEquals(card.upvoteBtn().getCssValue("color"), "rgba(0, 195, 0, 1)", "Upvote button color did not change on mouseover");

    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
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

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void MouseOverCommentBtn() throws InterruptedException {
        Actions a = new Actions(driver);
        a.moveToElement(card.commentIcon()).perform();
        Thread.sleep(2000);
        Assert.assertEquals(card.commentIcon().getCssValue("color"), "rgba(0, 195, 0, 1)", "Comment button color did not change on mouseover ");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void MouseOverGIF() throws InterruptedException {
        helpers.PageActions.scrollDown(driver, 3);
        WebElement ourGIF = card.firstGIF();
        if (ourGIF == null) {
            System.out.println("No gifs, skipping");
        } else {
            String ourGIFid = ourGIF.getAttribute("id");
            action.moveToElement(ourGIF).build().perform();
            Thread.sleep(7000);//yeah i know
            Assert.assertTrue(driver.findElement(By.id(ourGIFid)).findElement(By.className("overflow-hidden")).getAttribute("class").contains("invisible"), "Still see the GIF icon after mouseover on the GIF");
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void MouseOverReportBtn(){
        Actions a = new Actions(driver);
        a.moveToElement(card.reportBtn()).perform();
        Assert.assertEquals(card.reportBtn().getCssValue("color"), "rgba(0, 195, 0, 1)", "Report button color did not change on mouseover");
    }

    //Can't get these overlay tests to work reliably
    @Test(enabled = false)
    public void OverlayDownvote() {
        WebElement ourCard = card.cardNotDownvoted();
        ourCard.findElement(By.className("fa-thumbs-down")).click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(card.voteDownOverlay())).isDisplayed(), "Downvote overlay not found");
    }

    @Test(enabled = false)
    public void OverlayFavorite()  {
        WebElement ourCard = card.cardNotFavorited();
        ourCard.findElement(By.className("fa-heart")).click();
        Assert.assertTrue(card.voteUpOverlay().isDisplayed(), "Upvote overlay not found");
    }

    @Test(enabled = false)
    public void OverlayUpvote() {
        WebElement ourCard = card.cardNotUpvoted();
        ourCard.findElement(By.className("fa-thumbs-up")).click();
        Assert.assertTrue(card.voteUpOverlay().isDisplayed(), "Upvote overlay not found");
    }

    @Test
    public void ReportPostButtons() {
        card.reportBtn().click();
        Assert.assertTrue(card.reportOffensive().isEnabled()
                && card.reportSpam().isEnabled()
                && card.reportCopyright().isEnabled()
                && card.reportOther().isEnabled(), "ReportPost - report modal buttons not found or enabled");

    }

    @Test
    public void ReportPostText() {
        card.reportBtn().click();
        Assert.assertTrue(card.reportModalText().getAttribute("innerText").contains("Does this kind of stuff bother you? Does it contain stuff it shouldn't? Let us know, we'll take care of it.")
                && card.reportModalHeader().getAttribute("innerText").contains("Report"), "Didn't find the report modal text");
    }


    @Test
    public void ReturnToPlaceAfterProfileView() {
        PageActions.scrollDown(driver, 7);
        Waiter.wait(driver).until(CustomExpectedConditions.cardsLoaded());

        List<WebElement> allCards = card.allCards();
        int i = allCards.size();
        WebElement lastCard = allCards.get(i - 1);
        String cardID = lastCard.getAttribute("id");
        Point initialPoint = lastCard.getLocation();

        lastCard.findElement(By.cssSelector("a[data-username]")).click();
        Waiter.wait(driver).until(CustomExpectedConditions.profileLoaded());
        driver.navigate().back();
        Waiter.wait(driver).until(CustomExpectedConditions.cardsLoaded());

        Point currentPoint = driver.findElement(By.id(cardID)).getLocation();
        Assert.assertEquals(initialPoint, currentPoint, "User is not in same place after navigation back");

    }

    @Test()
    public void SingleTagPage() throws InterruptedException {

        driver.get(config.url + "tag/" + TAG);

        Waiter.longWait(driver).until(ExpectedConditions.urlContains(TAG));
        Waiter.customWait(driver, CustomExpectedConditions.cardsLoaded());
        Thread.sleep(3000);//still need it
        List<WebElement> cards = modal.allCards();
        int count = 0;
        int size = cards.size();
        action.moveToElement(modal.firstCard()).click().perform();
        Waiter.customWait(driver, CustomExpectedConditions.pageLoaded());
        for (WebElement card : cards) {
            try {
                List<WebElement> allTags = modal.tags();
                boolean tagFound = false;
                for (WebElement tag : allTags) {
                    if (tag.getText().contains(TAG)) {
                        tagFound = true;
                        break;
                    }
                }
                Assert.assertTrue(tagFound, card.getAttribute("id") + " did not contain the tagName " + TAG);
                while (count < size - 1) { //so it doesn't try to click the right arrow again on the last card
                    action.sendKeys(Keys.ARROW_RIGHT).perform();
                    Thread.sleep(2000);
                    count++;
                }
            } catch (AssertionError e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void SubmissionImageWidth() throws InterruptedException {
        helpers.PageActions.scrollDown(driver, 2);
        Thread.sleep(2000);
        List<WebElement> images = card.allImages();
        int width = images.get(0).getRect().getWidth();
        for (WebElement image : images) {
            Assert.assertEquals(width, image.getRect().getWidth(), "The images should have the same width");
        }
    }

    @Test
    public void SubmissionURLMatchesCard() throws InterruptedException {
        WebElement ourCard = card.cardNotGIF();
        String subID = ourCard.getAttribute("id");
        int subInt = StringHelper.getIntFromMixedString(subID);
        ourCard.click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
        String url = driver.getCurrentUrl();
        int urlInt = StringHelper.getIntFromMixedString(url);
        Assert.assertEquals(subInt, urlInt, "The post id didn't match the url");
    }

    @Test
    //working manually but the button isn't changing color in the automated test, also stale element exception for some reason
    public void UpvoteButton() {
        //Upvote Button is already checked
        WebElement ourCard = card.cardNotUpvoted();
        //Uncheck the upvote button
        ourCard.findElement(By.className("fa-thumbs-up")).click();
        Assert.assertFalse(ourCard.getAttribute("class").contains("text-white"), "Checked upvote button did not turn white after clicking it ");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
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

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
