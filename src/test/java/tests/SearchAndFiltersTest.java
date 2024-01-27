package tests;

import helpers.*;

import resources.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.PageHeaderPage;
import pages.SearchAndFiltersPage;
import pages.SubmissionCardsPage;
import pages.SubmissionModalPage;
import resources.RetryAnalyzer;
import resources.TestConfig;

import java.util.List;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class SearchAndFiltersTest {

    WebDriver driver;
    private static TestConfig config;
    SearchAndFiltersPage search;
    Logins login;
    PageHeaderPage header;
    SubmissionCardsPage card;
    SubmissionModalPage modal;
    Actions action;
    final String SEARCH_TERM = "theCHIVERBrady";
    final String SHORT_TERM = "qw";

    //************************** Setup ******************************************

    //Disabled all filter tests since they were removed https://resignationmedia.atlassian.net/browse/MYCHIVE-1061

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);

        action = new Actions(driver);
        card = new SubmissionCardsPage(driver);
        header = new PageHeaderPage(driver);
        modal = new SubmissionModalPage(driver);
        search = new SearchAndFiltersPage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.defaultEmail, System.getenv("TEST_PWD"));
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() throws InterruptedException {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void ClickDropdownRow()  {
        //clicking on the row, not the search term, whole row should be interactable
        driver.get(config.url + "search");
        search.searchInput().sendKeys(SHORT_TERM);
        String suggestion = search.searchSuggestions().getText();
        search.searchSuggestions().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.urlContains(suggestion)), "The click on the search suggestion row may have missed");
    }
    @Test(enabled = false)
    public void FilterHotness() throws InterruptedException {
        header.filterChange().click();
        if(search.filterHumanity().isSelected()){
            search.filterHumanity().click();
        }
        if(!search.filterHotness().isSelected()){
            search.filterHotness().click();
        }
        if(search.filterHumor().isSelected()){
            search.filterHumor().click();
        }
        Thread.sleep(4000);
        search.goButton().click();
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Waiter.wait(driver).until(ExpectedConditions.stalenessOf(search.goButton()));
        List<WebElement> cards = card.allCards();
        int size = cards.size();
        card.firstCard().click();
        for (int count = 0; count < size; count++) {
            action.scrollByAmount(0, 400);
            List<WebElement> allTags = modal.tags();
            boolean tagFound = false;
            for (WebElement tag : allTags) {
                if (tag.getText().contains("hotness")) {
                    tagFound = true;
                    break;
                }
            }
            Assert.assertTrue(tagFound, "Might be non-hotness card found in hotness filter results" + driver.findElement(By.cssSelector("div[id*='tag-']")).getAttribute("id"));
            if (count < size - 1) {
                action.sendKeys(Keys.ARROW_RIGHT).perform();
                Thread.sleep(2000);
            }
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, enabled = false)
    public void FilterHumanity() throws InterruptedException {
        header.filterChange().click();
        if(!search.filterHumanity().isSelected()){
            search.filterHumanity().click();
        }
        if(search.filterHotness().isSelected()){
            search.filterHotness().click();
        }
        if(search.filterHumor().isSelected()){
            search.filterHumor().click();
        }
        search.goButton().click();
        Waiter.wait(driver).until(ExpectedConditions.stalenessOf(search.goButton()));
        List<WebElement> cards = card.allCards();
        int size = cards.size();
        card.firstCard().click();
        helpers.Waiter.customWait(driver, CustomExpectedConditions.pageLoaded());

        for (int count = 0; count < size; count++) {
            List<WebElement> allTags = modal.tags();
            boolean tagFound = false;
            for (WebElement tag : allTags) {
                action.scrollByAmount(0, 400);
                if (tag.getText().contains("humanity")) {
                    tagFound = true;
                    break;
                }
            }
            Assert.assertTrue(tagFound, "Might be non-humanity card found in humanity filter results " + driver.findElement(By.cssSelector("div[id*='tag-']")).getAttribute("id"));
            if (count < size - 1) {
                // Perform the action to move to the next card only if it's not the last card
                action.sendKeys(Keys.ARROW_RIGHT).perform();
                Thread.sleep(2000);
            }
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, enabled = false)
    public void FilterHumor() throws InterruptedException {
        header.filterChange().click();
        if(search.filterHumanity().isSelected()){
            search.filterHumanity().click();
        }
        if(search.filterHotness().isSelected()){
            search.filterHotness().click();
        }
        if(!search.filterHumor().isSelected()){
            search.filterHumor().click();
        }

        search.goButton().click();
        Waiter.wait(driver).until(ExpectedConditions.stalenessOf(search.goButton()));
        List<WebElement> cards = card.allCards();
        int size = cards.size();
        card.firstCard().click();
        helpers.Waiter.customWait(driver, CustomExpectedConditions.pageLoaded());
        for (int count = 0; count < size; count++) {
            List<WebElement> allTags = modal.tags();
            boolean tagFound = false;
            for (WebElement tag : allTags) {
                action.scrollByAmount(0, 400);
                if (tag.getText().contains("humor")) {
                    tagFound = true;
                    break;
                }
            }
            Assert.assertTrue(tagFound, "Might be non humor card found in humanity filter results " + driver.findElement(By.cssSelector("div[id*='tag-']")).getAttribute("id"));
            if (count < size - 1) {
                // Perform the action to move to the next card only if it's not the last card
                action.sendKeys(Keys.ARROW_RIGHT).perform();
                Thread.sleep(2000);
            }
        }

    }

    @Test(enabled = false)//button was removed
    public void FollowButtonDisplays() {
        search.magnifyIcon().click();
        search.searchInput().click();
        action.sendKeys(SEARCH_TERM).sendKeys(Keys.ENTER).build().perform();
        Waiter.customWait(driver, CustomExpectedConditions.pageLoaded());
        Assert.assertTrue(search.followBtn().isDisplayed(), "Did not find follow button");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void HashTagExactMatchDisplays() throws InterruptedException {
        search.magnifyIcon().click();
        search.searchInput().click();
        action.sendKeys(SEARCH_TERM).sendKeys(Keys.ENTER).build().perform();
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Thread.sleep(3000);//yeah
        Assert.assertEquals(search.resultsTag().getText(), "theCHIVERBrady", "HashTagExactMatchDisplays - looking for theCHIVERBrady found " + search.resultsTag().getText());
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void HashTagPartialMatchDisplays() throws InterruptedException {
        search.magnifyIcon().click();
        search.searchInput().click();
        action.sendKeys(SEARCH_TERM).sendKeys(Keys.ENTER).build().perform();
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        search.searchInput().sendKeys(Keys.HOME, Keys.SHIFT, Keys.END);
        search.searchInput().sendKeys(Keys.BACK_SPACE);
        search.searchInput().sendKeys("brady");
        search.searchInput().sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        for (WebElement card : search.allTagCards()) {
            Assert.assertTrue(search.resultsTag().getText().toLowerCase().matches(".*\\b" + "brady" + "\\b.*"), card.getText() + " didn't match search term");
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void InfiniteScrollingSearch() throws InterruptedException {
        search.magnifyIcon().click();
        search.searchInput().click();
        search.searchInput().sendKeys(Keys.HOME, Keys.SHIFT, Keys.END);
        search.searchInput().sendKeys(Keys.BACK_SPACE);
        search.searchInput().sendKeys("hack");
        search.searchInput().sendKeys(Keys.ENTER);

        int firstTags = search.allTagCards().size();
        int firstUsers = search.allUserCards().size();
        PageActions.scrollDown(driver, 3);
        Thread.sleep(3000);
        int secondTags = search.allTagCards().size();
        int secondUsers = search.allUserCards().size();
        Assert.assertTrue(secondTags > firstTags && secondUsers > firstUsers, "Results do not seem to be loading when you scroll down");
    }

    @Test
    public void UsersPartialMatchDisplays() {
        search.magnifyIcon().click();
        search.searchInput().click();
        search.searchInput().sendKeys(Keys.HOME, Keys.SHIFT, Keys.END);
        search.searchInput().sendKeys(Keys.BACK_SPACE);
        search.searchInput().sendKeys("brady");
        search.searchInput().sendKeys(Keys.ENTER);

        for (WebElement card : search.allUserCards()) {
            List<WebElement> textElements = card.findElements(By.cssSelector(".text-lg"));
            if (!textElements.isEmpty()) {
                WebElement textElement = textElements.get(0);
                Assert.assertTrue(search.resultsTag().getText().toLowerCase().matches(".*\\b" + "brady" + "\\b.*"), textElement.getText() + " did not match the search term");
            }
        }
    }

    @Test(enabled = false)
    public void VerifiedFilterFeatured() {
        boolean hasCheck = true;
        header.menuFeatured().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        header.filterChange().click();
        search.filterVerified().click();
        search.goButton().click();
        Waiter.wait(driver).until(ExpectedConditions.stalenessOf(search.goButton()));
        List<WebElement> cards = card.allFeaturedCards();
        for (WebElement card : cards) {
            try {
                card.findElement(By.id("label-verified"));
            } catch (NoSuchElementException e) {
                try {
                    card.findElement(By.id("label-chivette"));
                } catch (NoSuchElementException f) {
                    hasCheck = false;
                }
            }
        }
        Assert.assertTrue(hasCheck, "Found unverified user on feature page");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, enabled = false)
    public void VerifiedFilterFollowing() throws InterruptedException {
        boolean hasCheck = true;
        header.menuFollowing().click();
        Waiter.quickWait(driver).until(ExpectedConditions.urlContains("following"));
        header.filterChange().click();
        search.filterVerified().click();
        search.goButton().click();
        Waiter.quickWait(driver).until(ExpectedConditions.stalenessOf(search.goButton()));
        Thread.sleep(2000);//yeah
        List<WebElement> cards = card.allCards();
        if (cards.size() == 0) {
            Assert.fail();
        }
        for (WebElement card : cards) {
            try {
                System.out.println(card.getAttribute("id") + " is the id");
                card.findElement(By.id("label-verified"));
            } catch (NoSuchElementException e) {
                try {
                    card.findElement(By.id("label-chivette"));
                } catch (NoSuchElementException f) {
                    hasCheck = false;
                }
            }
        }
        Assert.assertTrue(hasCheck, "Found a card from unverified user on following page");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, enabled = false)
    public void VerifiedFilterLatest() {
        boolean hasCheck = true;
        header.filterChange().click();
        search.filterVerified().click();
        search.goButton().click();
        Waiter.quickWait(driver).until(ExpectedConditions.stalenessOf(search.goButton()));
        List<WebElement> cards = card.allCards();
        for (WebElement card : cards) {
            try {
                card.findElement(By.id("label-verified"));
            } catch (NoSuchElementException e) {
                try {
                    card.findElement(By.id("label-chivette"));
                } catch (NoSuchElementException f) {
                    System.out.println(card.findElement(By.cssSelector("img")).getAttribute("id"));
                    hasCheck = false;
                }
            }
        }
        Assert.assertTrue(hasCheck, "Found a card from unverified user on latest");
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
