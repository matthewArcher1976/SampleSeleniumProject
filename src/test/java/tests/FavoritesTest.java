package tests;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.interactions.Actions;
import resources.Config;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
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
    SubmissionCardsPage cards;
    SubmissionModalPage modal;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        action = new Actions(driver);
        login = new Logins(driver);

        cards = new SubmissionCardsPage(driver);
        favorites = new FavoritesPage(driver);
        header = new PageHeaderPage(driver);
        modal = new SubmissionModalPage(driver);
        profile = new ProfilePage(driver);
    }

    @BeforeTest
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void FavoriteDisplaysOnProfile() throws InterruptedException {
        driver.manage().window().fullscreen();
        header.userMenu().click();
        header.yourProfileBtn().click();
        String yourName = ("@" + helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl()));
        header.menuLatest().click();
        Thread.sleep(5000);//yes
        helpers.PageActions.scrollDown(driver, 1);
        String submissionID = "";
        List<WebElement> cards = favorites.allCards();
        for (WebElement card : cards) {
            if (!favorites.isHeartFilledCard(card)) {
                try {
                    card.findElement(By.cssSelector("[id^='submission-image']")).click();
                }catch (ElementClickInterceptedException e){//the gif spinner glitches it, just skp it an pick another card
                    continue;
                }
                helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
                submissionID = Integer.toString(helpers.GetInteger.getIntFromMixedString(driver.getCurrentUrl()));
                modal.closeModal().click();
                Thread.sleep(3000);
                System.out.println(submissionID + " is our submission id");
                card.findElement(By.cssSelector("[id='toggle-favorite-" + submissionID + "']")).click();
                break;
            }
        }

        header.userMenu().click();
        header.yourProfileBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains(yourName.replace("@", "")));
        profile.tabFavorite().click();
        helpers.PageActions.scrollDown(driver, 3);
        Thread.sleep(5000);
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("favorites"));
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
    public void FaveIconFillsWhenClicked() {
        WebElement ourCard = cards.cardNotFavorited();
        String id = cards.cardNotFavorited().getAttribute("id");
        Assert.assertEquals(ourCard.findElement(By.className("fa-heart")).getCssValue("color"), "rgba(255, 255, 255, 1)", "Background color of icon should be rgba(255, 255, 255, 1) when not favorited");
        ourCard.findElement(By.className("fa-heart")).click();
        ourCard = driver.findElement(By.id(id));
        Assert.assertEquals(ourCard.findElement(By.className("fa-heart")).getCssValue("color"), "rgba(0, 195, 0, 1)", "Background color of icon should be rgba(0, 195, 0, 1) when favorited");

    }

    @Test(priority = 2)
    public void FavoriteGoneOnLogout() throws InterruptedException {

        boolean filled = favorites.isHeartFilled();
        if (!filled) {
            favorites.toggleFave().click();
        }
        login.logout();
        header.menuLatest().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("login")));
        driver.navigate().refresh();
        Assert.assertFalse(favorites.isHeartFilled(), "The favorite icon should not be filled for logged out user");
    }

    @Test
    public void NoDupesInFavorites() throws InterruptedException {
        header.userMenu().click();
        header.yourProfileBtn().click();
        profile.tabFavorite().click();
        Thread.sleep(2000);//yes
        helpers.PageActions.scrollDown(driver, 2);
        List<WebElement> cards = favorites.allCards();
        Set<String> ids = new HashSet<>();
        boolean hasDuplicates = false;
        for (WebElement card : cards) {
            String id = card.getAttribute("id");
            if (!ids.add(id)) {
                hasDuplicates = true;
                break;
            }
        }
        Assert.assertFalse(hasDuplicates, "Duplicate id found in favorites");
    }
    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.close();
    }
}
