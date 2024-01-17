package tests;

import helpers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import resources.Config;
import resources.RetryAnalyzer;
import resources.TestConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
@Test(retryAnalyzer = RetryAnalyzer.class)
public class FavoritesTest {
    private static TestConfig config;
    WebDriver driver;
    Actions action;
    FavoritesPage favorites;
    Logins login;
    PageHeaderPage header;
    ProfilePage profile;
    SubmissionCardsPage submissionCardsPage;
    SubmissionModalPage modal;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        action = new Actions(driver);
        login = new Logins(driver);

        submissionCardsPage = new SubmissionCardsPage(driver);
        favorites = new FavoritesPage(driver);
        header = new PageHeaderPage(driver);
        modal = new SubmissionModalPage(driver);
        profile = new ProfilePage(driver);
    }

    @BeforeTest
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.defaultEmail, System.getenv("TEST_PWD"));
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void FavoriteDisplaysOnProfile()  {

        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        String submissionID = "";
        List<WebElement> cards = favorites.allCards();
        for (WebElement card : cards) {
            submissionID = StringHelper.getIntFromMixedStringAsString(card.getAttribute("id"));
            if (!favorites.isHeartFilledCard(card)) {
                card.findElement(By.cssSelector("[id='toggle-favorite-" + submissionID + "']")).click();
                break;
            }
        }

        header.userMenu().click();
        header.yourProfileBtn().click();
        Waiter.wait(driver).until(CustomExpectedConditions.profileLoaded());
        profile.tabFavorite().click();
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Waiter.wait(driver).until(ExpectedConditions.urlContains("favorites"));
        boolean cardFound = false;
        cards = favorites.allCards();
        for (WebElement card : cards) {
            if (card.getAttribute("id").contains(submissionID)) {
                cardFound = true;
                break;
            }
        }
        Assert.assertTrue(cardFound, "Your favorited card did not show up on your Favorites feed ");
    }

    @Test
    public void FavoriteOnProfileNew() {

        Assert.assertTrue(PrettyAsserts.isDisplayed(By.className("fff"), driver));
    }

    @Test
    public void FaveIconFillsWhenClicked() {
        WebElement ourCard = submissionCardsPage.cardNotFavorited();
        String id = submissionCardsPage.cardNotFavorited().getAttribute("id");
        Assert.assertEquals(ourCard.findElement(By.className("fa-heart")).getCssValue("color"), "rgba(255, 255, 255, 1)", "Background color of icon should be rgba(255, 255, 255, 1) when not favorited");
        ourCard.findElement(By.className("fa-heart")).click();
        ourCard = driver.findElement(By.id(id));
        Assert.assertEquals(ourCard.findElement(By.className("fa-heart")).getCssValue("color"), "rgba(0, 195, 0, 1)", "Background color of icon should be rgba(0, 195, 0, 1) when favorited");
    }

    @Test()
    public void FavoriteGoneOnLogout() {

        boolean filled = favorites.isHeartFilled();
        if (!filled) {
            favorites.toggleFave().click();
        }
        login.logout();
        header.menuLatest().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("login")));
        driver.navigate().refresh();
        Assert.assertFalse(favorites.isHeartFilled(), "The favorite icon should not be filled for logged out user");
    }

    @Test//
    public void NoDupesInFavorites() throws InterruptedException {
        header.userMenu().click();
        header.yourProfileBtn().click();
        profile.tabFavorite().click();
        Thread.sleep(2000);//yes
        PageActions.scrollDown(driver, 2);
        List<WebElement> cards = favorites.allCards();
        Set<String> ids = new HashSet<>();
        boolean hasDuplicates = false;
        for (WebElement card : cards) {
            System.out.println(card.getAttribute("id"));
            String id = card.getAttribute("id");
            if (!ids.add(id)) {
                hasDuplicates = true;
                System.out.println("Dupe is is " + id);
                break;
            }
        }
        Assert.assertFalse(hasDuplicates, "Duplicate id found in favorites");
    }
    //************************** Teardown ********************************************

    @SuppressWarnings("BusyWait")
    @AfterClass
    public void unFavorite() {
        driver.get(config.url);
        try {
            List<WebElement> cards = submissionCardsPage.allCards();
            System.out.println(cards.size() + " is the side of cards");
            //  PageActions.scrollToTop(driver);
            for (int i = 0; i < cards.size() - 1; i++) {
                if (i % 2 == 0) {
                   // System.out.println(cards.get(i).getAttribute("id") + " is our card and heart filled is " + favorites.isHeartFilledCard(cards.get(i)));
                    if (favorites.isHeartFilledCard(cards.get(i))) {
                        String submissionID = StringHelper.getIntFromMixedStringAsString(cards.get(i).getAttribute("id"));
                        cards.get(i).findElement(By.cssSelector("[id='toggle-favorite-" + submissionID + "']")).click();
                        Thread.sleep(4000);//for now yes
                    }
                }
            }
        } catch (Exception e) {
            driver.quit();
        }
    }

    @AfterMethod
    public void relogIfLoggedOut() throws InterruptedException {
        try {
            driver.findElement(By.className(header.notificationIconClassname()));
        }catch (NoSuchElementException e){
            login.unpaidLogin(config.defaultEmail, System.getenv("TEST_PWD"));
        }
    }

    @AfterTest
    public void TearDown() {
        try {
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
