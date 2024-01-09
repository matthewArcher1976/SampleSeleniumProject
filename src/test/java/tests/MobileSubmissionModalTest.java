package tests;

import helpers.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.SubmissionCardsPage;
import pages.SubmissionModalPage;
import pages.SubmissionSingleImagePage;
import resources.Config;
import resources.RetryAnalyzer;
import resources.TestConfig;
import java.util.List;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class MobileSubmissionModalTest {

    WebDriver driver;
    SubmissionModalPage modal;
    Logins login;
    SubmissionCardsPage card;
    Actions action;
    SubmissionSingleImagePage single;
    private static TestConfig config;
    
    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverTypeMobile);
        action = new Actions(driver);
        card = new SubmissionCardsPage(driver);
        login = new Logins(driver);
        modal = new SubmissionModalPage(driver);
        single = new SubmissionSingleImagePage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, System.getenv("TEST_PWD"));
        Waiter.customWait(driver, CustomExpectedConditions.pageLoaded());
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, enabled = false)
    public void ClickTagRedirectToTagPage() throws InterruptedException {
        card.firstCard().click();
        PageActions.swipeUp(driver, 2);
        Thread.sleep(2000);//mhm
        String tagName = (modal.tag().getText());
        modal.tag().click();
        Thread.sleep(2000);//yeah
        Waiter.quickWait(driver).until(ExpectedConditions.urlContains(tagName));
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Thread.sleep(3000);//still need it
        List<WebElement> cards = modal.allCards();
        int count = 0;
        int size = cards.size();
        action.moveToElement(modal.firstCard()).click().perform();
        Waiter.customWait(driver, CustomExpectedConditions.pageLoaded());
        for (WebElement card : cards) {
            try {
                List<WebElement> allTags = modal.tags();
                boolean tagFound = false;
                for (WebElement tag : allTags) {
                    if (tag.getText().contains(tagName)) {
                        tagFound = true;
                        break;
                    }
                }
                Assert.assertTrue(tagFound, card.getAttribute("id") + " did not contain the tagName " + tagName);
                while (count < size - 1) { //so it doesn't try to click the right arrow again on the last card
                    action.sendKeys(Keys.ARROW_RIGHT).perform();
                    Thread.sleep(2000);
                    count++;
                }
            } catch (AssertionError e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void ClickCommentsButton() throws InterruptedException {
        modal.firstCard().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
        PageActions.findElementWithScrollingElement(driver, modal.commentButton());
        modal.commentIcon().click();
        PageActions.swipeUp(driver, 2);
        modal.switchToDisqusFrame();
        Thread.sleep(2000);//yes
        action.sendKeys(Keys.PAGE_DOWN).perform();
        Assert.assertTrue(modal.commentTextInput().isDisplayed(), "Policy text did not display");
    }

    @Test//TODO fix
    public void ClickCommentsIcon() {
        modal.firstCard().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
        modal.commentIcon().click();
        PageActions.swipeUp(driver, 3);
        modal.switchToDisqusFrame();
        Assert.assertTrue(modal.commentTextInput().isDisplayed(), "Policy text did not display");
    }

    @Test
    public void ClickFBIcon() {
        modal.firstCard().click();
        modal.facebookBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
        WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getCurrentUrl().contains("facebook"), "did not find facebook window");
        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void ClickTwitterIcon() {
        modal.firstCard().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(modal.twitterBtn()));
        modal.twitterBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
        WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.titleContains("X")), "Did not find Twitter login popup");
        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void HoverFacebookButton() throws InterruptedException {
        modal.firstCard().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(modal.facebookBtn()));
        Assert.assertEquals(modal.facebookBtn().getCssValue("color"), "rgba(255, 255, 255, 1)", "Facebook icon is wrong color to start");
        Thread.sleep(1000);//yes
        action.moveToElement(modal.facebookBtn()).perform();
        Thread.sleep(1000);//i get it
        Assert.assertEquals("rgba(0, 195, 0, 1)", modal.facebookBtn().getCssValue("color"), "Facebook button color on hover is wrong: " + modal.facebookBtn().getCssValue("color"));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void HoverTwitter() throws InterruptedException {
        modal.firstCard().click();
        System.out.println("Twitter color before " + modal.twitterBtn().getCssValue("color"));
        Assert.assertEquals(modal.twitterBtn().getCssValue("color"), "rgba(255, 255, 255, 1)", "Twitter icon is wrong color to start");
        Thread.sleep(1000);//yes
        action.moveToElement(modal.twitterBtn()).perform();
        Assert.assertEquals("rgba(0, 195, 0, 1)", modal.twitterBtn().getCssValue("color"), "Twitter button color on hover is wrong: " + modal.facebookBtn().getCssValue("color"));
    }

    @Test(enabled = false)//Selenium doesn't like modals very much, can't get the navigation to work, tried several ways
    public void ImageChanceBetweenSubsArrowKey() throws InterruptedException {
        WebElement card = modal.firstCard();
        action.moveToElement(card).click().perform();
        Thread.sleep(4000);//yeah
        System.out.println(driver.getCurrentUrl());
        String firstImage = modal.modalCard().findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("id").replace("submission-image-", "");
        action.sendKeys(Keys.ARROW_RIGHT).perform();
        Thread.sleep(4000);// right
        System.out.println(driver.getCurrentUrl());
        driver.navigate().refresh();
        Thread.sleep(4000);
        String secondImage = modal.modalCard().findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("id").replace("submission-image-", "");
        Assert.assertNotEquals(firstImage, secondImage, "ImageChanceBetweenSubsArrowKey - Found the same image after navigating right");
        Thread.sleep(5000);//
        action.sendKeys(Keys.ARROW_RIGHT).perform();
        action.sendKeys(Keys.ARROW_RIGHT).perform();

        Thread.sleep(5000);
        driver.navigate().refresh();//refreshes because the image doesn't load when you do it in Selenium even though it works manually

        Thread.sleep(5000);// i know
        String thirdImage = modal.modalCard().findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("id").replace("submission-image-", "");
        Assert.assertNotEquals(thirdImage, secondImage, "ImageChanceBetweenSubsArrowKey - Found the same image after navigating right second time");
        action.sendKeys(Keys.ARROW_LEFT).perform();
        Thread.sleep(3000);//shut up
        Assert.assertEquals(secondImage, modal.modalCard().findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("id").replace("submission-image-", ""), "ImageChanceBetweenSubsArrowKey - Found a different image when navigating back" + secondImage + " , " + modal.modalCard().findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("id").replace("submission-image-", ""));
    }

    @Test
    public void NavBetweenSubsArrowKeys() throws InterruptedException {
        WebElement card = modal.firstCard();
        action.moveToElement(card).click().perform();
        Thread.sleep(4000);//yeah
        String firstCardID = helpers.StringHelper.getIdFromUrl(driver.getCurrentUrl());
        action.sendKeys(Keys.ARROW_RIGHT).perform();
        String secondCardID = helpers.StringHelper.getIdFromUrl(driver.getCurrentUrl());
        Assert.assertNotEquals(firstCardID, secondCardID, "Did not navigate forward when pressing right arrow on keyboard");
        action.sendKeys(Keys.ARROW_RIGHT).perform();
        Thread.sleep(2000);//it's fine
        String thirdCardID = helpers.StringHelper.getIdFromUrl(driver.getCurrentUrl());
        Assert.assertNotEquals(thirdCardID, secondCardID, "Did not navigate forward when clicking right nav, second click");
        action.sendKeys(Keys.ARROW_LEFT).perform();
        Thread.sleep(2000);//is what it is
        Assert.assertEquals(secondCardID, helpers.StringHelper.getIdFromUrl(driver.getCurrentUrl()), "Did not navigate back from third to second card");
    }

    @Test
    public void RoundedEdgesOnModal() {
        card.firstCard().click();
        Assert.assertEquals(modal.modalCardFull().getCssValue("border-radius"), "8px", "RoundedEdgesOnModal = the border-radius changed, verify it's intentional");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void StickyHeaderOnModal() {
        modal.firstCard().click();
        modal.commentButton().click();
        helpers.PageActions.scrollDown(driver, 1);
        Assert.assertTrue(modal.stickyHeader().isDisplayed(), "Sticky header didn't display on scrolling");
    }

    @Test(enabled = false)
    public void Sandbox() throws InterruptedException {
       modal.firstCard().click();
        Thread.sleep(1000);
        PageActions.swipeLeft(driver, 30);
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
