package pages;

import helpers.Waiter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings("unused")
public class SubmissionModalPage {

    WebDriver driver;
    Actions actions;

    public SubmissionModalPage(WebDriver driver) {
        this.driver = driver;
    }

    //******************** Web Elements ***********************

    public List<WebElement> allCards() {
        return driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
    }

    public WebElement blurredImage(){
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[class*='blur-md']")));
    }

    public By blurredImageBy(){
        return By.cssSelector("img[class*='blur-md']");
    }

    public WebElement cardWithTag() throws InterruptedException {

        Thread.sleep(3000);
        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        WebElement firstCardWithTag = null;
        for (WebElement card : allCards) {
            card.click();
            Thread.sleep(2000);
            WebElement cardAfterClick = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id='" + card.getAttribute("id") + "']")));
            try {
                driver.findElement(By.className("badge-outline"));
                firstCardWithTag = cardAfterClick;
                actions.sendKeys(Keys.ESCAPE);

                break;
            } catch (NoSuchElementException e) {
                actions.sendKeys(Keys.ESCAPE);
                Thread.sleep(3000);
            }
        }
        return firstCardWithTag;
    }

    public WebElement closeModal() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-circle-xmark")));
    }

    public WebElement commentButton() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='btn-comment-']")));
    }

    public WebElement commentIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("card-body"))).findElement(By.className("fa-comment"));
    }

    public WebElement commentTextInput() {
        return driver.findElement(By.className("textarea"));
    }

    public WebElement disqusFrame() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe[id^='dsq-'")));
    }

    public WebElement firstCard() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='card-image-']")));
    }

    public WebElement facebookBtn() {
        return driver.findElement(By.className("fa-facebook-f"));
    }

    public WebElement gotItBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("comment-policy-refresh__button")));
    }
    public WebElement image() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[id^='submission-image-'")));
    }
    public WebElement modalCard() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("card-body")));
    }

    public WebElement modalCardFull() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".h-full.w-full.sm\\:w-\\[80vw\\].sm\\:max-w-xl.rounded-lg")));
    }

    public WebElement modalFavoriteBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id^=toggle-favorite-]")));
    }

    public WebElement modalLikeBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.className("card-body"))).findElement(By.cssSelector("svg[data-icon='thumbs-up']"));
    }

    public WebElement modalDislikeBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.className("card-body"))).findElement(By.cssSelector("svg[data-icon='thumbs-down']"));
    }

    public WebElement navLeft() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-chevron-left")));
    }

    public WebElement navRight() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-chevron-right")));
    }

    public WebElement policyText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("comment-policy-refresh__text")));
    }

    public WebElement tag() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id^='tag-']")));
    }

    public List<WebElement> tags() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div[id^='tag-']")));
    }

    public WebElement stickyHeader() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("sticky")));
    }

    public WebElement twitterBtn() {
        return driver.findElement(By.className("fa-twitter"));
    }
//	driver.findElement(By.cssSelector("div[id^=vote-down]")).click();
    //****************************** Actions ******************************

    public void switchToDisqusFrame() {
        helpers.Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[id^= 'dsq-'")));
    }


}
