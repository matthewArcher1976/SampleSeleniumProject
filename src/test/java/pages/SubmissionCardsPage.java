package pages;

import helpers.PageActions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class SubmissionCardsPage {

    WebDriver driver;


    public SubmissionCardsPage(WebDriver driver) {
        this.driver = driver;
    }

    //*********************** WebElements *******************************

    public List<WebElement> allCards() {
        return driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
    }

    public List<WebElement> allFeaturedCards() {
        return driver.findElements(By.className("card"));
    }

    public List<WebElement> allImages() {
        return driver.findElements(By.cssSelector("[id^='card-image-']"));
    }

    public List<WebElement> allFeaturedImages() {
        return driver.findElements(By.cssSelector("[id^='submission-image-']"));
    }

    public List<WebElement> allTags() {
        return driver.findElements(By.cssSelector("div[id^='tag-']"));
    }
    public WebElement cardIsUpvoted() {
        WebElement firstNotUpvotedCard = null;
        for (int i = 0; i < 2; i++) {
            List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
            for (WebElement card : allCards) {
                if (helpers.IsSelected.isIconSelected(card.findElement(By.className("fa-thumbs-up"))) && card.findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("src").endsWith("jpeg")) {
                    firstNotUpvotedCard = card;
                    break;
                }
            }
            if (firstNotUpvotedCard != null) {
                break;
            } else {

                PageActions.scrollDown(driver, 2);
            }
        }
        if (firstNotUpvotedCard == null) {
            System.out.println("Can't find an un-upvoted card, giving up");
            Assert.fail();
        }
        return firstNotUpvotedCard;
    }
    public WebElement cardNotDownvoted() {
        WebElement firstNotUpvotedCard = null;
        for (int i = 0; i < 2; i++) {
            List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
            for (WebElement card : allCards) {
                if (!helpers.IsSelected.isIconSelected(card.findElement(By.className("fa-thumbs-down")))) {
                    firstNotUpvotedCard = card;
                    break;
                }
            }
            if (firstNotUpvotedCard != null) {
                break;
            } else {
                helpers.PageActions.scrollDown(driver, 2);
            }
        }
        if (firstNotUpvotedCard == null) {
            System.out.println("Can't find an un-downvoted card, giving up");
            Assert.fail();
        }
        return firstNotUpvotedCard;
    }

    public WebElement cardNotFavorited() {
        WebElement firstNotUpvotedCard = null;
        for (int i = 0; i < 2; i++) {
            List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
            for (WebElement card : allCards) {

                if (!helpers.IsSelected.isIconSelected(card.findElement(By.cssSelector("[id^='toggle-favorite-']")).findElement(By.className("fa-heart"))) && card.findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("src").endsWith("jpeg")) {
                    firstNotUpvotedCard = card;
                    break;
                }
            }
            if (firstNotUpvotedCard != null) {
                break;
            } else {

                helpers.PageActions.scrollDown(driver, 2);
            }
        }
        if (firstNotUpvotedCard == null) {
            System.out.println("Can't find an un-favorited card, giving up");
            Assert.fail();
        }
        return firstNotUpvotedCard;
    }

    public WebElement cardNotGIF() throws InterruptedException {
        Actions a = new Actions(driver);
        helpers.PageActions.scrollDown(driver, 2);
        Thread.sleep(2000);
        WebElement notGIFCard = null;
        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        for (WebElement card : allCards) {
            a.moveToElement(card).perform();

            if (card.findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("src").endsWith("jpeg")) {
                notGIFCard = card;
                break;
            }
        }
        return notGIFCard;
    }

    public WebElement cardNotMine(String userName) throws InterruptedException {
        Actions action = new Actions(driver);
        helpers.PageActions.scrollDown(driver, 2);
        Thread.sleep(2000);
        WebElement notMyCard = null;
        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        for (WebElement card : allCards) {
            action.moveToElement(card).perform();
            // System.out.println(card.findElement(By.cssSelector("span[class='mr-2']")).getText() + " is getText" );
            if (!card.findElement(By.cssSelector("a[class*='text-primary']")).getText().contains(userName)) {
                notMyCard = card;
                break;
            }
        }
        return notMyCard;
    }

    public WebElement cardNotUpvoted() {
        WebElement firstNotUpvotedCard = null;
        for (int i = 0; i < 2; i++) {
            List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
            for (WebElement card : allCards) {
                if (!helpers.IsSelected.isIconSelected(card.findElement(By.className("fa-thumbs-up"))) && card.findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("src").endsWith("jpeg")) {
                    firstNotUpvotedCard = card;
                    break;
                }
            }
            if (firstNotUpvotedCard != null) {
                break;
            } else {

                helpers.PageActions.scrollDown(driver, 2);
            }
        }
        if (firstNotUpvotedCard == null) {
            System.out.println("Can't find an un-upvoted card, giving up");
            Assert.fail();
        }
        return firstNotUpvotedCard;
    }

    public WebElement cardWithTag() throws InterruptedException {

        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        WebElement firstCardWithTag = null;
        for (WebElement card : allCards) {
            helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(card)).click();
            Thread.sleep(2000);
            WebElement cardAfterClick = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id='" + card.getAttribute("id") + "']")));
            try {
                driver.findElement(By.className("badge-outline"));
                firstCardWithTag = cardAfterClick;
                helpers.PageActions.hitEscape(driver);
                break;
            } catch (NoSuchElementException e) {
                helpers.PageActions.hitEscape(driver);
            }
        }
        return firstCardWithTag;
    }

    //get one with more than one tag on it
    public WebElement cardWithTags() throws InterruptedException {
        helpers.PageActions.scrollDown(driver, 3);
        Thread.sleep(2000);
        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        WebElement cardWithMultipleTags = null;
        List<WebElement> tags;
        for (WebElement card : allCards) {
            helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(card)).click();
            Thread.sleep(2000);
            WebElement cardAfterClick = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id='" + card.getAttribute("id") + "']")));

            try {
                driver.findElement(By.className("badge-outline")); //.cssSelector("div")).getAttribute("class").contains("overflow-hidden"))
                tags = driver.findElements(By.cssSelector("div[id^='tag-']"));
                if (tags.size() > 1 && !card.findElement(By.cssSelector("div")).getAttribute("class").contains("overflow-hidden")) {
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

    public WebElement chivetteIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("label-chivette")));
    }

    public WebElement commentBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='comment']")));
    }

    public WebElement commentIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='comment']"))).findElement(By.className("fa-comment"));
    }

    public WebElement createBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("submission-create")));
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
    public WebElement featuredCommentIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-comment")));
    }
    public WebElement featuredIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='featured ichive icon']")));
    }

    public WebElement firstCard() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])")));
    }

    public WebElement firstCardEnhanced() {

        WebElement first = null;
        try {
            first = helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id^='submission-image']")));
        } catch (ElementClickInterceptedException e) {
            driver.findElement(By.className("overflow-hidden")).click();
        }
        return first;
    }

    public WebElement firstGIF() throws InterruptedException {
        helpers.PageActions.scrollDown(driver, 2);
        Thread.sleep(2000);
        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        WebElement element = null;
        for (WebElement card : allCards) {
            try {
                if (card.findElement(By.className("overflow-hidden")).getText().contains("GIF")) {
                    element = card;
                }
            } catch (NoSuchElementException e) {
                System.out.println("Didn't find a GIF");
            }
        }
        return element;
    }

    public Set<String> getAllUserNames() {
        Set<String> userNames = new HashSet<>();
        List<WebElement> cards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image'])"));
        for (WebElement card : cards) {
            String userName = card.findElement(By.cssSelector("a[class='flex gap-x-2 items-center text-primary']")).getText();

            if (!userName.isBlank()) {
                userNames.add(userName);
            }
        }
        return userNames;
    }

    public WebElement imageLoadingSpinner() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("animate-spin")));
    }

    public WebElement reportBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-flag")));
    }

    public WebElement reportCopyright() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("report-copyright")));
    }

    public WebElement reportModalHeader() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("header"))).findElement(By.cssSelector("div"));
    }

    public WebElement reportModalText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='bg-dark-char']")));
    }

    public WebElement reportOffensive() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("report-offensive")));
    }

    public WebElement reportOther() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("report-other")));
    }

    public WebElement reportSpam() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("report-spam")));
    }

    public WebElement submissionTitle() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"))).findElement(By.className("text-sm"));
    }

    public WebElement tag() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("badge-outline")));
    }

    public WebElement upvoteBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-thumbs-up")));
    }

    public WebElement userName() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[class='flex gap-x-2 items-center text-primary']")));
    }

    public WebElement verifiedIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("label-verified")));
    }

    public WebElement voteCounter() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id^='vote-counter-']")));
    }

    public WebElement voteDownOverlay() {
        return driver.findElement(By.className("bg-vote-down-overlay"));
    }

    public WebElement voteUpOverlay() {
        return driver.findElement(By.className("bg-vote-up-overlay"));
    }

    //************************** Helpers ******************************************

    public boolean isSelected(WebElement element) {
        String s = element.getAttribute("class");
        Boolean filled = null;
        if (s.contains("text-white")) {
            filled = false;
        } else if (s.contains("text-primary") && !s.contains("text-white")) {
            filled = true;
        } else {
            System.out.println(s + "isSelected failed");
            Assert.fail();
        }
        return filled;
    }

}
