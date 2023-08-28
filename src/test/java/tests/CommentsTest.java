package tests;

import helpers.Config;
import helpers.Drivers;
import helpers.Logins;
import helpers.PageActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.CommentsPage;
import pages.SubmissionCardsPage;
import pages.SubmissionModalPage;
import resources.TestConfig;

public class CommentsTest {

    WebDriver driver = Drivers.ChromeDriver();

    CommentsPage comments = new CommentsPage(driver);

    Logins login = new Logins(driver);
    SubmissionModalPage modal = new SubmissionModalPage(driver);
    SubmissionCardsPage card = new SubmissionCardsPage(driver);
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
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void ClickShowComments() throws InterruptedException {
        card.firstCard().click();
        PageActions.findElementWithScrollingElement(driver, modal.commentButton()).click();
        PageActions.findElementWithScrollingElement(driver, comments.disqusFrame());
        comments.switchToDisqusFrame();
        Thread.sleep(3000);
        Assert.assertTrue(comments.commentTextInput().isDisplayed(), "Did not find the comment policy block");
    }

    @Test
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
    @Test
    public void LeaveGuestComment() throws InterruptedException {
        card.firstCard().click();
        PageActions.findElementWithScrollingElement(driver, modal.commentButton()).click();
        comments.switchToDisqusFrame();
        comments.commentTextInput().sendKeys(helpers.Randoms.getRandomString(20));
        comments.submitCommentBtn().click();
        comments.submitCommentBtn().click();
        comments.submitCommentBtn().click();
        WebElement label = comments.enterNameLabel();
        comments.guestCheckbox().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.stalenessOf(label)), "Labels should close on clicking guest checkbox");
        Thread.sleep(1000);
        comments.nameInput().sendKeys("Tester");
        Thread.sleep(1000);
        comments.emailInput().sendKeys("thechivetest+" + helpers.Randoms.getRandomString(10) + "@gmail.com");
        comments.switchToCaptchaFrame();
        comments.captchaCheck().click();
        //Not submitting it because spam
    }

    @Test()
    public void sandbox() throws InterruptedException {
        card.firstCard().click();
        PageActions.findElementWithScrollingElement(driver, modal.commentButton()).click();
    }

    //************************* Teardown ***************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
