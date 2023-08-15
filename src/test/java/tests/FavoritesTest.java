package tests;

import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;

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

    @Test(priority = 1)
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

    @Test(enabled = true, priority = 1)
    @Parameters()
    public void FaveIconFillsWhenClicked() throws InterruptedException {
        WebElement ourCard = cards.cardNotFavorited();
        String id = cards.cardNotFavorited().getAttribute("id");
        Assert.assertEquals(ourCard.findElement(By.className("fa-heart")).getCssValue("color"), "rgba(255, 255, 255, 1)", "Background color of icon should be rgba(255, 255, 255, 1) when not favorited");
        ourCard.findElement(By.className("fa-heart")).click();
        ourCard = driver.findElement(By.id(id));
        Assert.assertEquals(ourCard.findElement(By.className("fa-heart")).getCssValue("color"), "rgba(0, 195, 0, 1)", "Background color of icon should be rgba(0, 195, 0, 1) when favorited");

    }

    @Test(priority = 99)
    public void FavoriteGoneOnLogout() throws InterruptedException {

        boolean filled = favorites.isHeartFilled();
        if (!filled) {
            favorites.toggleFave().click();
        }
        login.logout(driver);
        header.menuLatest().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("login")));
        driver.navigate().refresh();
        Assert.assertFalse(favorites.isHeartFilled(), "The favorite icon should not be filled for logged out user");
    }

    @Test(priority = 1)
    @Parameters()
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

}
