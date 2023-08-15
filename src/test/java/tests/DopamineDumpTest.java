package tests;

import helpers.Drivers;
import helpers.Logins;
import helpers.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.PageHeaderPage;
import pages.SubmissionCardsPage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("DefaultAnnotationParam")
public class DopamineDumpTest {

    WebDriver driver = Drivers.ChromeDriver();

    SubmissionCardsPage card = new SubmissionCardsPage(driver);
    PageHeaderPage header = new PageHeaderPage(driver);
    Logins login = new Logins(driver);


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
    @Test(enabled = true)
    public void CommentButtonFeatured() {
        header.menuFeatured().click();
        card.commentBtn().click();
        Assert.assertTrue(card.disqusSection().isDisplayed(), "CommentButtonFeatured -Comments did not open");
    }

    @Test(enabled = true)
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
    @Test(enabled = true)
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

    @Test(enabled = true)
    public void SingleDumpPage() {
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[id^='submission-image-']"), 6));
        header.menuFeatured().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        card.featuredIcon().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlMatches(".*/\\d+.*")), "SingleDumpPage - URL does not contain the dump number");
    }

}
