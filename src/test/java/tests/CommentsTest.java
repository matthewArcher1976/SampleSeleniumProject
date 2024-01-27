package tests;
import helpers.Logins;
import resources.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.CommentsPage;
import pages.SubmissionCardsPage;
import pages.SubmissionModalPage;
import resources.RetryAnalyzer;
import resources.TestConfig;
import helpers.PageActions;

import static resources.getDriverType.getDriver;

public class CommentsTest {

    WebDriver driver;
    Actions action;
    CommentsPage comments;
    Logins logins;
    SubmissionModalPage modal;
    SubmissionCardsPage card;
    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);

        action = new Actions(driver);

        card = new SubmissionCardsPage(driver);
        comments = new CommentsPage(driver);
        logins = new Logins(driver);
        modal = new SubmissionModalPage(driver);
    }

    @BeforeTest
    public void login() throws InterruptedException {
        driver.get(config.url);
        logins.unpaidLogin(config.unpaidEmail, System.getenv("TEST_PWD"));
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void ClickShowComments() throws InterruptedException {
        card.firstCard().click();
        PageActions.findElementWithScrollingElement(driver, modal.commentButton()).click();
        action.moveToElement(modal.image()).scrollToElement(comments.disqusFrame()).perform();
        PageActions.scrollDown(driver, 2);
        PageActions.findElementWithScrollingElement(driver, comments.disqusFrame());
        comments.switchToDisqusFrame();
        Thread.sleep(3000);
        Assert.assertTrue(comments.commentTextInput().isDisplayed(), "Did not find the comment policy block");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void EnterNameEmailPasswordLabels() throws InterruptedException {
        card.firstCard().click();
        PageActions.findElementWithScrollingElement(driver, modal.commentButton()).click();
        comments.switchToDisqusFrame();
        Thread.sleep(3000);
        comments.commentTextPlaceholder().click();
        comments.commentTextInput().sendKeys("asdf");
        comments.submitCommentBtn().click();
        comments.submitCommentBtn().click();//have to click twice because Discus
        comments.commentTextInput().clear();//text entered persists between tests and breaks them
        Assert.assertTrue(comments.enterNameLabel().isDisplayed(), "Did not find the Please enter your name label");
        Assert.assertTrue(comments.enterEmailLabel().isDisplayed(), "Did not find the Please enter your email label");
        Assert.assertTrue(comments.enterPasswordLabel().isDisplayed(), "Did not find the Please enter your password label");
    }


    //************************* Teardown ***************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
