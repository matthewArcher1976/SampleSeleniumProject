package tests;


import helpers.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.PageHeaderPage;
import pages.ProfilePage;
import pages.SubmissionCardsPage;
import resources.TestConfig;

import java.util.List;

public class ProfileTest {

    WebDriver driver = Drivers.ChromeDriver();
    ProfilePage profilePage = new ProfilePage(driver);
    EditProfilePage profile = new EditProfilePage(driver);
    Logins login = new Logins(driver);
    PageHeaderPage header = new PageHeaderPage(driver);
    SubmissionCardsPage card = new SubmissionCardsPage(driver);
    Actions action = new Actions(driver);
    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public static void configs() throws Exception {
        config = Config.getConfig();
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() throws InterruptedException {
        driver.get(config.url);
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Thread.sleep(3000);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void BlockUser() throws InterruptedException {
        driver.get(config.url);
        card.firstCard().findElement(By.cssSelector("a[href]")).click();
        profilePage.blockBtn().click();
        Thread.sleep(3000);//yes
        if (profilePage.blockBtn().findElement(By.cssSelector("svg")).getAttribute("class").contains("fa-ban")) {
            profilePage.blockBtn().click();
            Thread.sleep(3000);
            Assert.assertTrue(profilePage.blockBtn().findElement(By.cssSelector("svg")).getAttribute("class").contains("fa-check"), "LoginOpensOnBlockButton - didn't block user after login");
            profilePage.blockBtn().click();
        } else if (profilePage.blockBtn().findElement(By.cssSelector("svg")).getAttribute("class").contains("fa-check")) {
            profilePage.blockBtn().click();
            Thread.sleep(3000);
            Assert.assertTrue(profilePage.blockBtn().findElement(By.cssSelector("svg")).getAttribute("class").contains("fa-ban"), "LoginOpensOnBlockButton - didn't unblock user after login");
        }
    }

    @Test(enabled = false)//It's just a 404 for a banned user now so not really testable
    public void BannedUserIs404() throws InterruptedException {
        driver.get(config.url + config.bannedUsername);
        Waiter.wait(driver).until(ExpectedConditions.urlContains(config.bannedUsername));
        Thread.sleep(1000);
        Assert.assertTrue(profilePage.profile404GIF().isDisplayed() && profilePage.profile404text().isDisplayed(), "Didn't see 404 page for banned user");
    }

    @Test
    public void ClickProfilePic() {
        String profileURL = driver.getCurrentUrl();
        profilePage.tabFollowers().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("followers"));
        profilePage.profilePic().click();
        Waiter.wait(driver).until(ExpectedConditions.urlToBe(profileURL));
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.urlToBe(profileURL)), "Clicking profile pic did not return user to profile page");
    }

    @Test
    public void ClickUserName() {
        String profileURL = driver.getCurrentUrl();
        profilePage.tabFollowers().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("followers"));
        profilePage.userName().click();
        Waiter.wait(driver).until(ExpectedConditions.urlToBe(profileURL));
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.urlToBe(profileURL)), "Clicking user name did not return user to profile page");
    }

    @Test
    public void CMGLink() {
        profilePage.cmgLink().click();
        Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getCurrentUrl().contains("https://www.chivemediagroup.com/?utm_source=ichive"), "CMG Link broken");
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void EditButtonNotDisplayed() {
        String userName = ("@" + helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl()));
        header.menuLatest().click();
        helpers.PageActions.scrollDown(driver, 1);
        List<WebElement> allPosts = profilePage.allCards();
        for (WebElement post : allPosts) {
            if (!post.getText().contains(userName) && post.getText().contains("@")) {
                String otherName = post.getText();
                String[] words = otherName.split("\\s+");
                for (String word : words) {
                    if (word.startsWith("@")) {
                        String otherUserName = word.substring(1);

                        post.findElement(By.cssSelector("a[class='flex gap-x-2 items-center text-primary']")).click();
                        Waiter.wait(driver).until(ExpectedConditions.urlContains(otherUserName));
                        try {
                            Assert.assertTrue(profilePage.editButton().isDisplayed());
                            Assert.fail("Edit button should not display for user");
                        } catch (TimeoutException e) {
                            // edit button is not displayed, test passes
                            Assert.assertTrue(true);
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    @Test
    public void FeaturedPostShowsInProfile() throws InterruptedException {
        header.menuLatest().click();
        header.menuFeatured().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump")), "Edit button should not display for user");
        String userName = card.userName().getText().replace("@", "");
        System.out.println("userName is " + userName);
        card.userName().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains(userName));
        profilePage.tabFeatured().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("featured"));
        Thread.sleep(2000);
        List<WebElement> allCards = card.allCards();
        for (WebElement card : allCards) {
            Assert.assertTrue(card.getText().contains(userName), "FeaturedPostShowsInProfile - Found something in user's featured posts that didn't belong");
        }
    }

    @Test
    public void FollowUserBtn() throws InterruptedException {
        try {
            Assert.assertTrue(profilePage.followButton().isDisplayed());
            Assert.fail("Follow button should not be on your own profile");
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
        String yourUser = helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl());
        header.menuLatest().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains(yourUser)));
        WebElement otherUser = profilePage.otherUserName(yourUser);
        otherUser.findElement(By.cssSelector("a[href]")).click();
        Thread.sleep(4000);
        Assert.assertTrue(profilePage.followButton().isDisplayed(), "Did not find follow button");
    }

    @Test
    public void HoverEditButton() {
        Assert.assertEquals(profilePage.editButton().getCssValue("background-color"), "rgba(84, 79, 79, 1)", "Background color before hover should be rgba(84, 79, 79, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
        action.moveToElement(profilePage.editButton()).perform();
        Assert.assertEquals(profilePage.editButton().getCssValue("background-color"), "rgba(68, 64, 64, 1)", "Background color on hover should be rgba(68, 64, 64, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
    }

    @Test
    public void HoverFacebookLink() throws InterruptedException {
        Assert.assertTrue(profilePage.facebookLink().isDisplayed(), "Add a link to user's Facebook to proceed with this test");
        Assert.assertEquals(profilePage.facebookLink().getCssValue("background-color"), "rgba(59, 89, 152, 1)", "Background color before hover should be rgba(59, 89, 152, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
        action.moveToElement(profilePage.facebookLink()).perform();
        Thread.sleep(500);
        Assert.assertEquals(profilePage.facebookLink().getCssValue("background-color"), "rgba(66, 62, 62, 1)", "Background color on hover should be rgba(66, 62, 62, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
    }

    @Test
    public void HoverFavoritesTab() {
        Assert.assertEquals(profilePage.tabFavorite().getCssValue("background-color"), "rgba(0, 0, 0, 0)", "Background color before hover should be rgba(0, 0, 0, 0), found: " + profilePage.tabFeatured().getCssValue("background-color"));
        action.moveToElement(profilePage.tabFavorite()).perform();
        Assert.assertEquals(profilePage.tabFavorite().getCssValue("background-color"), "rgba(41, 41, 41, 0.9)", "Background color on hover should be rgba(41, 41, 41, 0.9), found: " + profilePage.tabFeatured().getCssValue("background-color"));
    }

    @Test
    public void HoverFeaturedTab() {
        Assert.assertEquals(profilePage.tabFeatured().getCssValue("background-color"), "rgba(0, 0, 0, 0)", "Background color before hover should be rgba(0, 0, 0, 0), found: " + profilePage.tabFeatured().getCssValue("background-color"));
        action.moveToElement(profilePage.tabFeatured()).perform();
        Assert.assertEquals(profilePage.tabFeatured().getCssValue("background-color"), "rgba(41, 41, 41, 0.9)", "Background color on hover should be rgba(41, 41, 41, 0.9), found: " + profilePage.tabFeatured().getCssValue("background-color"));
    }

    @Test
    public void HoverFollowersTab() {
        Assert.assertEquals(profilePage.tabFollowers().getCssValue("background-color"), "rgba(0, 0, 0, 0)", "Background color before hover should be rgba(0, 0, 0, 0), found: " + profilePage.tabFeatured().getCssValue("background-color"));
        action.moveToElement(profilePage.tabFollowers()).perform();
        Assert.assertEquals(profilePage.tabFollowers().getCssValue("background-color"), "rgba(41, 41, 41, 0.9)", "Background color on hover should be rgba(41, 41, 41, 0.9), found: " + profilePage.tabFeatured().getCssValue("background-color"));
    }

    @Test
    public void HoverFollowingTab() {
        Assert.assertEquals(profilePage.tabFollowing().getCssValue("background-color"), "rgba(0, 0, 0, 0)", "Background color before hover should be rgba(0, 0, 0, 0), found: " + profilePage.tabFeatured().getCssValue("background-color"));
        action.moveToElement(profilePage.tabFollowing()).perform();
        Assert.assertEquals(profilePage.tabFollowing().getCssValue("background-color"), "rgba(41, 41, 41, 0.9)", "Background color on hover should be rgba(41, 41, 41, 0.9), found: " + profilePage.tabFeatured().getCssValue("background-color"));
    }

    @Test
    public void HoverInstaLink() throws InterruptedException {
        Assert.assertTrue(profilePage.instagramLink().isDisplayed(), "Add a link to user's Instagram to proceed with this test");
        Assert.assertEquals(profilePage.instagramLink().getCssValue("background-color"), "rgba(138, 58, 185, 1)", "Background color before hover should be rgba(138, 58, 185, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
        action.moveToElement(profilePage.instagramLink()).perform();
        Thread.sleep(500);//getting inconsistent values for background-color without this
        Assert.assertEquals(profilePage.instagramLink().getCssValue("background-color"), "rgba(66, 62, 62, 1)", "Background color on hover should be rgba(66, 62, 62, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
    }

    @Test
    public void HoverTwitterLink() throws InterruptedException {
        Assert.assertTrue(profilePage.twitterLink().isDisplayed(), "Add a link to user's Twitter to proceed with this test");
        Assert.assertEquals(profilePage.twitterLink().getCssValue("background-color"), "rgba(0, 172, 238, 1)", "Background color before hover should be rgba(0, 172, 238, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
        action.moveToElement(profilePage.twitterLink()).perform();
        Thread.sleep(500);//getting inconsistent values for background-color without this
        Assert.assertEquals(profilePage.twitterLink().getCssValue("background-color"), "rgba(66, 62, 62, 1)", "Background color on hover should be rgba(66, 62, 62, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
    }

    @Test
    public void HoverTiktokLink() throws InterruptedException {
        Assert.assertTrue(profilePage.tiktokLink().isDisplayed(), "Add a link to user's Tiktok to proceed with this test");
        Assert.assertEquals(profilePage.tiktokLink().getCssValue("background-color"), "rgba(0, 0, 34, 1)", "Background color before hover should be rgba(0, 0, 34, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
        action.moveToElement(profilePage.tiktokLink()).perform();
        Thread.sleep(500);//getting inconsistent values for background-color without this
        Assert.assertEquals(profilePage.tiktokLink().getCssValue("background-color"), "rgba(66, 62, 62, 1)", "Background color on hover should be rgba(66, 62, 62, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
    }

    @Test
    public void HoverWishlistLink() throws InterruptedException {
        Thread.sleep(10000);
        Assert.assertTrue(profilePage.amazonLink().isDisplayed(), "Add a link to user's Facebook to proceed with this test");
        Assert.assertEquals(profilePage.amazonLink().getCssValue("background-color"), "rgba(255, 153, 0, 1)", "Background color before hover should be rgba(255, 153, 0, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
        action.moveToElement(profilePage.amazonLink()).perform();
        Thread.sleep(500);
        Assert.assertEquals(profilePage.amazonLink().getCssValue("background-color"), "rgba(66, 62, 62, 1)", "Background color on hover should be rgba(66, 62, 62, 1), found: " + profilePage.tabFeatured().getCssValue("background-color"));
    }

    @Test
    public void InfiniteScrollFollowers() throws InterruptedException {
        driver.get(config.url + "ca_pinup_girl"); //user with followers
        profilePage.tabFollowers().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("followers"));
        int first = profilePage.allFollowers().size();
        helpers.PageActions.scrollDown(driver, 3);
        Thread.sleep(2000);
        int second = profilePage.allFollowers().size();
        Assert.assertTrue(second > first, "Follower cards not seem to be loading when you scroll down");
    }

    @Test
    public void InfiniteScrollFollowing() {
        profilePage.tabFollowing().click();
        Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("[id^='user-card-']"), 0));
        int first = profilePage.allFollowers().size();
        PageActions.scrollDown(driver, 2);
        int second = profilePage.allFollowers().size();
        Assert.assertTrue(second > first, "Cards do not seem to be loading when you scroll down");
    }

    @Test
    public void NFTSubmitWallet() {
        profilePage.nftTab().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("nft"));
        PageActions.scrollDown(driver, 1);
        profilePage.nftWalletInput().sendKeys("0x4675C7e5BaAFBFFbca748158bEcBA61ef3b0a263");
        profilePage.nftSubmitButton().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profilePage.nftToast())).getText().contains("You request will be processed and we will send you a confirmation e-mail soon."));
    }

    @Test
    public void NFTSubmitWalletInvalid() {
        profilePage.nftTab().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("nft"));
        PageActions.scrollDown(driver, 1);
        profilePage.nftWalletInput().sendKeys("asdfasdf");
        profilePage.nftSubmitButton().click();
        Assert.assertTrue(profilePage.nftWalletError().getText().contains("The wallet format is invalid."));
    }

    @Test
    public void NFTtabText() {
        profilePage.nftTab().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("nft"));
        Assert.assertTrue(driver.getPageSource().contains(" Once upon a time, if you were one of the lucky, random recipients of the ultra-rare gold bars denominated with the letter V, you were issued a digital certificate of authenticity in the form of a Non-Fungible Token (NFT). "));
    }

    @Test
    public void ProfileIs404() {
        String badUser = helpers.Randoms.getRandomString(20);
        driver.get(config.url + badUser);
        Waiter.wait(driver).until(ExpectedConditions.urlContains(badUser));
        Assert.assertTrue(profilePage.profile404text().getText().contains("Whatever you were hoping to find isn't here.") && profilePage.profile404GIF().isDisplayed(), "Did not see the 404 page for a bad profile url");
    }

    @Test
    public void PrivacyPolicy() {
        profilePage.privacyTermsLink().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getCurrentUrl().contentEquals("https://www.chivemediagroup.com/legal/terms?utm_source=ichive"), "Privacy Policy link broken");
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void SubmissionsDisplay() throws InterruptedException {
        driver.navigate().refresh(); //not sure why this is needed; it was grabbing the cards from favorites even though I was never on there
        Thread.sleep(2000);
        List<WebElement> userPosts = profilePage.allCards();
        String userName = ("@" + helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl()));
        for (WebElement post : userPosts) {
            Assert.assertTrue(post.getText().contains(userName), "Found someone else's post in user's profile");
        }
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}