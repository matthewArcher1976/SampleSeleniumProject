package tests;

import helpers.CustomExpectedConditions;
import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.PageHeaderPage;
import pages.SearchAndFiltersPage;
import pages.SubmissionCardsPage;
import pages.SubmissionModalPage;

import java.util.List;

@SuppressWarnings("DefaultAnnotationParam")
public class SearchAndFiltersTest {

    WebDriver driver = Drivers.ChromeDriver();
    SearchAndFiltersPage search = new SearchAndFiltersPage(driver);
    Logins login = new Logins(driver);
    PageHeaderPage header = new PageHeaderPage(driver);
    SubmissionCardsPage card = new SubmissionCardsPage(driver);
    SubmissionModalPage modal = new SubmissionModalPage(driver);
    Actions action = new Actions(driver);
    String searchTerm = "theCHIVERBrady";

    @BeforeTest
    @Parameters({"unpaidEmail", "password", "url"})
    public void login(@Optional("thechivetest@gmail.com") String unpaidEmail, @Optional("Chive1234") String password, @Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
        driver.get(url);
        login.unpaidLogin(unpaidEmail, password, driver);
        Thread.sleep(1000);
    }

    @BeforeMethod
    @Parameters({"url"})
    public void setDriver(@Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
        driver.get(url);
    }

    @AfterClass
    public void TearDown() {
        driver.close();
    }

    //************************** Begin Tests ********************************************
    @Test(enabled = true, priority = 1)
    public void FilterHotness() throws InterruptedException {
        header.filterChange().click();
        search.filterHumanity().click();
        search.filterHumor().click();
        search.goButton().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("category"));
        Assert.assertTrue(driver.getCurrentUrl().contains("category=Hotness&filters=")
                && !driver.getCurrentUrl().contains("category=Humor&filters=")
                && !driver.getCurrentUrl().contains("category=Humanity&filters="), "URL should only contain Hotness");

        List<WebElement> cards = card.allCards();
        int size = cards.size();
        card.firstCard().click();
        for (int count = 0; count < size; count++) {
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


    @Test(enabled = true, priority = 1)
    public void FilterHumanity() throws InterruptedException {
        header.filterChange().click();
        search.filterHotness().click();
        search.filterHumor().click();
        search.goButton().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("category"));
        Assert.assertTrue(driver.getCurrentUrl().contains("category=Humanity&filters=")
                && !driver.getCurrentUrl().contains("category=Hotness&filters=")
                && !driver.getCurrentUrl().contains("category=Humor&filters="), "URL should only contain Humanity");
        List<WebElement> cards = card.allCards();
        int size = cards.size();
        card.firstCard().click();

        helpers.Waiter.customWait(driver, CustomExpectedConditions.pageLoaded());

        for (int count = 0; count < size; count++) {
            List<WebElement> allTags = modal.tags();
            boolean tagFound = false;
            for (WebElement tag : allTags) {
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

    @Test(enabled = true, priority = 1)
    public void FilterHumor() {
        header.filterChange().click();
        search.filterHumanity().click();
        search.filterHotness().click();
        search.goButton().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("category"));
        Assert.assertTrue(driver.getCurrentUrl().contains("category=Humor&filters=")
                && !driver.getCurrentUrl().contains("category=Hotness&filters=")
                && !driver.getCurrentUrl().contains("category=Humanity&filters="), "URL should only contain Humor");
    }

    @Test(enabled = true, priority = 1)
    public void FollowButtonDisplays() {
        search.magnifyIcon().click();
        search.searchInput().click();
        action.sendKeys(searchTerm).sendKeys(Keys.ENTER).build().perform();
        helpers.Waiter.customWait(driver, CustomExpectedConditions.pageLoaded());
        Assert.assertTrue(search.followBtn().isDisplayed(), "Did not find follow button");
    }

    @Test(enabled = true, priority = 1)
    public void HashTagExactMatchDisplays() {
        search.magnifyIcon().click();
        search.searchInput().click();
        action.sendKeys(searchTerm).sendKeys(Keys.ENTER).build().perform();
        Assert.assertEquals(search.resultsTag().getText(), "theCHIVERBrady", "HashTagExactMatchDisplays - found wrong hashtag");
    }

    @Test(enabled = true, priority = 1)
    public void HashTagPartialMatchDisplays() throws InterruptedException {
        search.magnifyIcon().click();
        search.searchInput().click();
        action.sendKeys(searchTerm).sendKeys(Keys.ENTER).build().perform();
        search.searchInput().sendKeys(Keys.HOME, Keys.SHIFT, Keys.END);
        search.searchInput().sendKeys(Keys.BACK_SPACE);
        search.searchInput().sendKeys("brady");
        search.searchInput().sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        for (WebElement card : search.allTagCards()) {
            Assert.assertTrue(search.resultsTag().getText().toLowerCase().matches(".*\\b" + "brady" + "\\b.*"), card.getText() + " didn't match search term");
        }
    }

    @Test(enabled = true, priority = 1)
    public void InfiniteScrollingSearch() throws InterruptedException {
        search.magnifyIcon().click();
        search.searchInput().click();
        action.sendKeys(searchTerm).sendKeys(Keys.ENTER).build().perform();
        search.searchInput().sendKeys(Keys.HOME, Keys.SHIFT, Keys.END);
        search.searchInput().sendKeys(Keys.BACK_SPACE);
        search.searchInput().sendKeys("hack");
        search.searchInput().sendKeys(Keys.ENTER);

        int firstTags = search.allTagCards().size();
        int firstUsers = search.allUserCards().size();
        helpers.PageActions.scrollDown(driver, 3);
        Thread.sleep(3000);
        int secondTags = search.allTagCards().size();
        int secondUsers = search.allUserCards().size();
        Assert.assertTrue(secondTags > firstTags && secondUsers > firstUsers, "Results do not seem to be loading when you scroll down");
    }

    @Test(enabled = true, priority = 1)
    public void ResultsTextDisplays() {
        search.magnifyIcon().click();
        search.searchInput().click();
        action.sendKeys(searchTerm).sendKeys(Keys.ENTER).build().perform();
        Assert.assertTrue(search.searchResultHeader().getText().contains("Here's what we found for"), "ResultsTextDisplays - Did not find results Text");
    }

    @Test(enabled = true, priority = 1)
    public void UsersPartialMatchDisplays() {
        search.magnifyIcon().click();
        search.searchInput().click();
        action.sendKeys(searchTerm).sendKeys(Keys.ENTER).build().perform();
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

    @Test(enabled = true, priority = 1)
    public void VerifiedFilterFeatured() throws InterruptedException {
        boolean hasCheck = true;
        header.menuFeatured().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        header.filterChange().click();
        search.filterVerified().click();
        search.goButton().click();

        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("Verified+Users"));
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

    @Test(enabled = true, priority = 1)
    public void VerifiedFilterFollowing() {
        boolean hasCheck = true;
        header.menuFollowing().click();
        helpers.Waiter.quickWait(driver).until(ExpectedConditions.urlContains("following"));
        header.filterChange().click();
        search.filterVerified().click();
        search.goButton().click();
        helpers.Waiter.quickWait(driver).until(ExpectedConditions.urlContains("Verified+Users"));
        System.out.println(driver.getCurrentUrl());
        List<WebElement> cards = card.allCards();
        if (cards.size() == 0) {
            System.out.println("Follow some users, including a verified and chivette");
            Assert.fail();
        }
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
        Assert.assertTrue(hasCheck, "Found a card from unverified user on following page");
    }

    @Test(enabled = true, priority = 1)
    public void VerifiedFilterLatest() {
        boolean hasCheck = true;
        header.filterChange().click();
        search.filterVerified().click();
        search.goButton().click();
        helpers.Waiter.quickWait(driver).until(ExpectedConditions.urlContains("Verified+Users"));
        List<WebElement> cards = card.allCards();
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
        Assert.assertTrue(hasCheck, "Found a card from unverified user on latest");
    }

}
