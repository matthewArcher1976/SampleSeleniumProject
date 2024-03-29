package tests;



import helpers.Logins;
import helpers.PageActions;
import helpers.Waiter;
import org.openqa.selenium.interactions.Actions;
import resources.Config;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.PageHeaderPage;
import pages.SubmissionCardsPage;
import resources.TestConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static resources.getDriverType.getDriver;
public class DopamineDumpTest {

    WebDriver driver;
    Actions action;
    SubmissionCardsPage card;
    PageHeaderPage header;
    Logins logins;
    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public  void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        action = new Actions(driver);
        card = new SubmissionCardsPage(driver);
        header = new PageHeaderPage(driver);
        logins = new Logins(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        logins.unpaidLogin(config.defaultEmail, System.getenv("TEST_PWD"));
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void refresh() {

        driver.get(config.url);

    }

    //************************** Begin Tests ********************************************

    @Test
    public void CommentButtonFeatured() {
        System.out.println("before?");
        header.menuFeatured().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        card.featuredCommentIcon().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("comments"));
        System.out.println("here?");
        Assert.assertTrue(card.disqusSection().isDisplayed(), "CommentButtonFeatured -Comments did not open");

    }

    @Test
    public void FeaturedCardsDisplay() {
        Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), 6));
        header.menuFeatured().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        Assert.assertTrue(card.featuredIcon().isDisplayed(), "FeaturedCardsDisplay - Did not see Featured icon");
    }

    @Test
    public void InfiniteScrollFeatured() {
        header.menuFeatured().click();
        Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), 0));
        int first = card.allFeaturedImages().size();
        PageActions.scrollDown(driver, 3);
        Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), 0));
        Assert.assertNotNull(Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), first)), "InfiniteScrollFeatured - Dopamine Dump cards do not seem to be loading when you scroll down");
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "ConstantValue"})
    @Test
    public void NoDuplicates() {
        header.menuFeatured().click();
        PageActions.scrollDown(driver, 3);
        List<WebElement> allCards = card.allFeaturedImages();
        Set<String> ids = new HashSet<>();
        boolean noDupes = true;
        for (WebElement element : allCards) {
            String id = element.getAttribute("id");
            if (ids.contains(id)) {
                noDupes = false;
            }
        }
        Assert.assertTrue(noDupes, "Duplicate DD posts found");//pass test if no dupes found
    }

    @Test
    public void SingleDumpPage() {
        Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), 6));
        header.menuFeatured().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        card.featuredIcon().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.urlMatches(".*/\\d+.*")), "SingleDumpPage - URL does not contain the dump number");
    }

    //************************* Teardown ***************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}
