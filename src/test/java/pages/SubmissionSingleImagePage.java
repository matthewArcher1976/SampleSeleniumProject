package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class SubmissionSingleImagePage {

    WebDriver driver;

    public SubmissionSingleImagePage(WebDriver driver) {
        this.driver = driver;
    }

    //******************** Web Elements ***********************

    public List<WebElement> allTags(WebElement card) {
        card.click();
        WebElement cardAfterClick = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id='" + card.getAttribute("id") + "']")));
        return cardAfterClick.findElements(By.cssSelector("div[id^='tag-']"));
    }

    public WebElement avatar() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[style*='background-image']")));
    }

    public WebElement cardWithTags() throws InterruptedException {
        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        WebElement cardWithMultipleTags = null;
        List<WebElement> tags;
        for (WebElement card : allCards) {
            helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(card)).click();
            Thread.sleep(2000);
            WebElement cardAfterClick = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id='" + card.getAttribute("id") + "']")));

            try {
                driver.findElement(By.className("badge-outline"));
                tags = driver.findElements(By.cssSelector("div[id^='tag-']"));
                if (tags.size() > 1) {
                    cardWithMultipleTags = cardAfterClick;
                    helpers.PageActions.hitEscape(driver);
                    break;
                } else {
                    helpers.PageActions.hitEscape(driver);
                }
            } catch (NoSuchElementException e) {
                helpers.PageActions.hitEscape(driver);
            }
        }
        return cardWithMultipleTags;
    }

    public WebElement commentBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='comment']")));
    }

    public WebElement commentIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='comment']"))).findElement(By.className("fa-comment"));
    }

    public WebElement disqusSection() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("disqus_thread")));
    }

    public WebElement downvoteBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-thumbs-down")));
    }

    public WebElement faceBookBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-facebook-f")));
    }

    public WebElement favoriteBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id^='toggle-favorite-']"))).findElement(By.className("fa-heart"));
    }

    public WebElement favoriteOverlay() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='card-image-overlay']")));
    }

    public WebElement followFacebook() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("like-facebook")));
    }

    public WebElement followInsta() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("like-instagram")));
    }

    public WebElement followTwitter() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("like-twitter")));
    }

    public WebElement reportFlag() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-flag")));
    }

    public WebElement shareFacebook() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class*='fa-facebook']:not(#side-bar *)")));
    }

    public WebElement shareTwitter() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class*='fa-twitter']:not(#side-bar *)")));
    }

    public WebElement socialFacebookLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-facebook-f")));
    }

    public WebElement socialInstagramLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-instagram")));
    }

    public WebElement socialTwitterLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-twitter")));
    }

    public WebElement sourceImage() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img")));
    }

    public WebElement submissionImage() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[id^='submission-image-']")));
    }

    public List<WebElement> submissionTags() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("[id^='tag-']:not(#side-bar *)")));
    }

    public WebElement submissionTitle() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"))).findElement(By.className("text-sm"));
    }

    public WebElement trendArrowIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-arrow-trend-up")));
    }

    public List<WebElement> trendingTags() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#side-bar [id^='tag-']")));
    }

    public WebElement trendingText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Trending Tags')]")));
    }

    public WebElement upvoteBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-thumbs-up")));
    }

    public WebElement userName() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[class='text-primary']")));
    }

    public WebElement verifiedCheckIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-check")));
    }

    public WebElement verifiedDate() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id^='row-recently-verified-date-']")));
    }

    public WebElement verifiedRecentUser() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id^='row-recently-verified-username-']")));
    }

    public WebElement verifiedText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Recently Verified')]")));
    }

    public WebElement voteCounter() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='vote-counter-']")));
    }

    public WebElement voteDownOverlay() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("bg-vote-down-overlay")));
    }

    public WebElement voteUpOverlay() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("bg-vote-up-overlay")));
    }


}
