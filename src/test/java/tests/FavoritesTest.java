package tests;

import helpers.Config;
import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import resources.TestConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritesTest {

    WebDriver driver = Drivers.ChromeDriver();

    FavoritesPage favorites = new FavoritesPage(driver);
    Logins login = new Logins(driver);
    PageHeaderPage header = new PageHeaderPage(driver);
    ProfilePage profile = new ProfilePage(driver);
    SubmissionCardsPage cards = new SubmissionCardsPage(driver);
    SubmissionModalPage modal = new SubmissionModalPage(driver);
    private static TestConfig config;

    //************************** Setup ******************************************
    @BeforeTest
    public static void configs() throws Exception {
        config = Config.getConfig();
    }

    @BeforeTest
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void FavoriteDisplaysOnProfile() throws InterruptedException {
        header.userMenu().click();
        header.yourProfileBtn().click();
        String yourName = ("@" + helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl()));
        header.menuLatest().click();
        Thread.sleep(2000);//yes
        helpers.PageActions.scrollDown(driver, 2);
        String submissionID = "";
        List<WebElement> cards = favorites.allCards();
        for (WebElement card : cards) {
            if (!favorites.isHeartFilledCard(card)) {
                card.findElement(By.cssSelector("[id^='card-image']")).click();
                helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
                submissionID = Integer.toString(helpers.GetInteger.getIntFromMixedString(driver.getCurrentUrl()));
                modal.closeModal().click();
                Thread.sleep(3000);
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
