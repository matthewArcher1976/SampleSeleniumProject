package tests;

import helpers.Config;
import helpers.Drivers;
import helpers.Logins;
import helpers.Waiter;
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

public class DopamineDumpTest {

    WebDriver driver = Drivers.ChromeDriver();

    SubmissionCardsPage card = new SubmissionCardsPage(driver);
    PageHeaderPage header = new PageHeaderPage(driver);
    Logins login = new Logins(driver);
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
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void CommentButtonFeatured() {
        header.menuFeatured().click();
        card.commentBtn().click();
        Assert.assertTrue(card.disqusSection().isDisplayed(), "CommentButtonFeatured -Comments did not open");
    }

    @Test
    public void FeaturedCardsDisplay() {
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), 6));
        header.menuFeatured().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        Assert.assertTrue(card.featuredIcon().isDisplayed(), "FeaturedCardsDisplay - Did not see Featured icon");
    }

    @Test
    public void InfiniteScrollFeatured() {
        header.menuFeatured().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), 0));
        int first = card.allFeaturedImages().size();
        helpers.PageActions.scrollDown(driver, 3);
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), 0));
        Assert.assertNotNull(Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), first)), "InfiniteScrollFeatured - Dopamine Dump cards do not seem to be loading when you scroll down");
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "ConstantValue"})
    @Test
    public void NoDuplicates() {
        header.menuFeatured().click();
        helpers.PageActions.scrollDown(driver, 3);
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
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), 6));
        header.menuFeatured().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        card.featuredIcon().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlMatches(".*/\\d+.*")), "SingleDumpPage - URL does not contain the dump number");
    }

    //************************* Teardown ***************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}
