package tests;

import helpers.CustomExpectedConditions;
import helpers.Logins;
import helpers.PageActions;
import helpers.Waiter;
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
import resources.TestConfig;

import java.text.DecimalFormat;
import java.util.List;

import static helpers.getDriverType.getDriver;

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
        login.unpaidLogin(config.unpaidEmail, config.password);
        Waiter.customWait(driver, CustomExpectedConditions.pageLoaded());
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    @Test
    public void ClickTagRedirectToTagPage() throws InterruptedException {
        card.firstCard().click();
        PageActions.swipeUp(driver, 2);
        Thread.sleep(2000);
        String tagName = (modal.tag().getText());
        System.out.println(tagName);
        modal.tag().click();
        Thread.sleep(2000);
        Waiter.wait(driver).until(ExpectedConditions.urlContains(tagName));
        Waiter.customWait(driver, CustomExpectedConditions.pageLoaded());
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

    @Test
    public void ClickCommentsIcon() throws InterruptedException {
        modal.firstCard().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
        modal.commentIcon().click();
        //PageActions.scrollDown();
        helpers.PageActions.findElementWithScrolling(driver, By.cssSelector("iframe[id^='dsq-']"));
        modal.switchToDisqusFrame();
        Assert.assertTrue(modal.commentTextInput().isDisplayed(), "Policy text did not display");
    }

    @Test
    public void ClickFBIcon() {
        modal.firstCard().click();
        modal.facebookBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getCurrentUrl().contains("facebook"), "did not find facebook window");
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void ClickTwitterIcon() {
        modal.firstCard().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(modal.twitterBtn()));
        modal.twitterBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.titleContains("Twitter")), "Did not find Twitter login popup");
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test(priority = 1)
    //checks that the proportions on the card image are the same an actual, breaks the hover tests if run before then, IDK knows why
    public void GIFNotCutOff() throws InterruptedException {
        helpers.PageActions.scrollDown(driver, 3);
        WebElement gifCard = card.firstGIF();
        double cardHeight = gifCard.findElement(By.cssSelector("div[id^='card-image']")).getRect().getHeight();
        double cardWidth = gifCard.findElement(By.cssSelector("div[id^='card-image']")).getRect().getWidth();
        double cardRatio = cardHeight / cardWidth;
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedCardRatio = df.format(cardRatio);

        action.moveToElement(gifCard);
        gifCard.click();
        driver.navigate().refresh();
        driver.get(single.submissionImage().getAttribute("src"));

        double height = Integer.parseInt(single.sourceImage().getCssValue("height").replaceAll("[^0-9]", ""));
        double width = Integer.parseInt(single.sourceImage().getCssValue("width").replaceAll("[^0-9]", ""));
        double ratio = height / width;
        String formattedRatio = df.format(ratio);
        System.out.println("formattedRatio is " + formattedCardRatio + " and formattedCardRatio   is " + formattedCardRatio);
        Assert.assertEquals(formattedRatio, formattedCardRatio, "GIFNotCutOff - gif may be getting cut off");
    }

    @Test
    public void HoverFacebookButton() throws InterruptedException {
        modal.firstCard().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(modal.facebookBtn()));
        Assert.assertEquals(modal.facebookBtn().getCssValue("color"), "rgba(255, 255, 255, 1)", "Facebook icon is wrong color to start");
        Thread.sleep(1000);//yes
        action.moveToElement(modal.facebookBtn()).perform();
        Thread.sleep(1000);
        Assert.assertEquals("rgba(0, 195, 0, 1)", modal.facebookBtn().getCssValue("color"), "Facebook button color on hover is wrong: " + modal.facebookBtn().getCssValue("color"));
    }

    @Test
    public void HoverTwitter() throws InterruptedException {
        modal.firstCard().click();
        System.out.println("Twitter color before " + modal.twitterBtn().getCssValue("color"));
        Assert.assertEquals(modal.twitterBtn().getCssValue("color"), "rgba(255, 255, 255, 1)", "Twitter icon is wrong color to start");
        Thread.sleep(1000);//yes
        action.moveToElement(modal.twitterBtn()).perform();
        Assert.assertEquals("rgba(0, 195, 0, 1)", modal.twitterBtn().getCssValue("color"), "Twitter button color on hover is wrong: " + modal.facebookBtn().getCssValue("color"));
    }

    @Test
    public void ImageChanceBetweenSubsArrowKey() throws InterruptedException {
        WebElement card = modal.firstCard();
        action.moveToElement(card).click().perform();
        Thread.sleep(4000);//remove once 731 is fixed
        String firstImage = modal.modalCard().findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("id").replace("submission-image-", "");
        action.sendKeys(Keys.ARROW_RIGHT).perform();
        String secondImage = modal.modalCard().findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("id").replace("submission-image-", "");
        Assert.assertNotEquals(firstImage, secondImage, "ImageChanceBetweenSubsArrowKey - Found the same image after navigating right");
        action.sendKeys(Keys.ARROW_RIGHT).perform();
        Thread.sleep(2000);
        String thirdImage = modal.modalCard().findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("id").replace("submission-image-", "");
        Assert.assertNotEquals(thirdImage, secondImage, "ImageChanceBetweenSubsArrowKey - Found the same image after navigating right second time");
        action.sendKeys(Keys.ARROW_LEFT).perform();
        Thread.sleep(3000);
        Assert.assertEquals(secondImage, modal.modalCard().findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("id").replace("submission-image-", ""), "ImageChanceBetweenSubsArrowKey - Found a different image when navigating back" + secondImage + " , " + modal.modalCard().findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("id").replace("submission-image-", ""));
    }

    @Test
    public void NavBetweenSubs() throws InterruptedException {
        Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("div[id^='submission-']"), 5));
        WebElement card = modal.firstCard();
        action.moveToElement(card).click().perform();
        String firstCardID = helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl());
        Thread.sleep(5000);//remove when bug on 731 is fixed
        modal.navRight().click();
        String secondCardID = helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl());
        Assert.assertNotEquals(firstCardID, secondCardID, "Did not navigate forward when clicking right nav");
        modal.navRight().click();
        Thread.sleep(2000);
        String thirdCardID = helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl());
        Assert.assertNotEquals(thirdCardID, secondCardID, "Did not navigate forward when clicking right nav, second click");
        modal.navLeft().click();
        Thread.sleep(2000);
        Assert.assertEquals(secondCardID, helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl()), "Did not navigate back from third to second card");
    }

    @Test
    public void NavBetweenSubsArrowKeys() throws InterruptedException {
        WebElement card = modal.firstCard();
        action.moveToElement(card).click().perform();
        Thread.sleep(4000);//remove once 731 is fixed
        String firstCardID = helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl());
        action.sendKeys(Keys.ARROW_RIGHT).perform();
        String secondCardID = helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl());
        Assert.assertNotEquals(firstCardID, secondCardID, "Did not navigate forward when pressing right arrow on keyboard");
        action.sendKeys(Keys.ARROW_RIGHT).perform();
        Thread.sleep(2000);
        String thirdCardID = helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl());
        Assert.assertNotEquals(thirdCardID, secondCardID, "Did not navigate forward when clicking right nav, second click");
        action.sendKeys(Keys.ARROW_LEFT).perform();
        Thread.sleep(2000);
        Assert.assertEquals(secondCardID, helpers.GetInteger.getIdFromUrl(driver.getCurrentUrl()), "Did not navigate back from third to second card");
    }

    @Test
    public void RoundedEdgesOnModal() {
        card.firstCard().click();
        Assert.assertEquals(modal.modalCardFull().getCssValue("border-radius"), "8px", "RoundedEdgesOnModal = the border-radius changed, verify it's intentional");
    }

    @Test
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
