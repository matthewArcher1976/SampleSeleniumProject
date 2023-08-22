package tests;

import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.CommentsPage;
import pages.PageHeaderPage;
import pages.SubmissionCardsPage;
import pages.SubmissionModalPage;

public class CommentsTest {

    WebDriver driver = Drivers.ChromeDriver();
    Actions action = new Actions(driver);
    CommentsPage comments = new CommentsPage(driver);

    Logins login = new Logins(driver);
    SubmissionModalPage modal = new SubmissionModalPage(driver);
    SubmissionCardsPage card = new SubmissionCardsPage(driver);
    PageHeaderPage header = new PageHeaderPage(driver);

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

    @Test
    public void ClickShowComments() throws InterruptedException {
        card.firstCard().click();
        modal.commentButton().click();
        comments.switchToDisqusFrame();
        Thread.sleep(3000);
        Assert.assertTrue(comments.commentTextInput().isDisplayed(), "Did not find the comment policy block");
    }

    @Test
    public void EnterNameEmailPasswordLabels() throws InterruptedException {
        card.firstCard().click();
        modal.commentButton().click();
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
        modal.commentButton().click();
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

    @Test(enabled = false)
    public void sandbox() throws InterruptedException {
        card.firstCard().click();
        modal.commentButton().click();
        modal.switchToDisqusFrame();
        try {comments.commentTextPlaceholder().click();
            }catch (Exception ignored){}
        comments.commentTextInput().sendKeys("asdf");
        action.moveToElement(comments.submitCommentBtn()).click().perform();
        WebElement label = comments.enterNameLabel();
        comments.guestCheckbox().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.stalenessOf(label)), "piece of shit");
    }
}
