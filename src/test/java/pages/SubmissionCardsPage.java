package pages;

import helpers.PageActions;
import helpers.PrettyAsserts;
import helpers.Waiter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class SubmissionCardsPage {

    WebDriver driver;

    public SubmissionCardsPage(WebDriver driver) {
        this.driver = driver;
    }

    Actions actions;
    
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
                if (PrettyAsserts.isIconSelected(card.findElement(By.className("fa-thumbs-up"))) && card.findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("src").endsWith("jpeg")) {
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

    public WebElement blurredImage(){
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("blur-md")));
    }

    public WebElement tryIt(){
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("blur-md")));
    }

    public By blurredImageBy(){
        return By.className("blur-md");
    }

    public WebElement cardFromUser(String userName) throws InterruptedException {
        Actions action = new Actions(driver);
        PageActions.scrollDown(driver, 1);
        Thread.sleep(2000);
        WebElement usersCard = null;
        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        for (WebElement card : allCards) {
            System.out.println("Looking for " + userName);
            System.out.println(card.findElement(By.className("px-4")).findElement(By.cssSelector("a[class*='text-primary']")).getText());

            if (card.findElement(By.className("px-4")).findElement(By.cssSelector("a[class*='text-primary']")).getText().contains(userName)) {
                usersCard = card;
                break;
            }
        }
        if(usersCard == null){
            Assert.fail("User need to submit an image for test to continue ");
        }
        return usersCard;
    }

    public WebElement cardNotDownvoted() {
        WebElement firstNotUpvotedCard = null;
        for (int i = 0; i < 2; i++) {
            List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
            for (WebElement card : allCards) {
                if (!PrettyAsserts.isIconSelected(card.findElement(By.className("fa-thumbs-down")))) {
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
                if (!PrettyAsserts.isIconSelected(card.findElement(By.cssSelector("[id^='toggle-favorite-']")).findElement(By.className("fa-heart"))) && card.findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("src").endsWith("jpeg")) {
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
            System.out.println("Can't find an un-favorited card, giving up");
            Assert.fail();
        }
        return firstNotUpvotedCard;
    }

    public WebElement cardNotGIF() throws InterruptedException {
        Actions a = new Actions(driver);
        PageActions.scrollDown(driver, 2);
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
        PageActions.scrollDown(driver, 2);
        Thread.sleep(2000);
        WebElement notMyCard = null;
        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        for (WebElement card : allCards) {
            if (!card.findElement(By.className("px-4")).findElement(By.cssSelector("a[class*='text-primary']")).getText().contains(userName)) {
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
                if (!PrettyAsserts.isIconSelected(card.findElement(By.className("fa-thumbs-up"))) && card.findElement(By.cssSelector("img[id^='submission-image-']")).getAttribute("src").endsWith("jpeg")) {
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

    public WebElement cardWithTag() throws InterruptedException {

        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        WebElement firstCardWithTag = null;
        for (WebElement card : allCards) {
            Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(card)).click();
            Thread.sleep(2000);
            WebElement cardAfterClick = Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id='" + card.getAttribute("id") + "']")));
            try {
                driver.findElement(By.className("badge-outline"));
                firstCardWithTag = cardAfterClick;
                actions.sendKeys(Keys.ESCAPE);
               
                break;
            } catch (NoSuchElementException e) {
                actions.sendKeys(Keys.ESCAPE);
            }
        }
        return firstCardWithTag;
    }

    //get one with more than one tag on it
    public WebElement cardWithTags() throws InterruptedException {
        PageActions.scrollDown(driver, 3);
        Thread.sleep(2000);
        List<WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
        WebElement cardWithMultipleTags = null;
        List<WebElement> tags;
        for (WebElement card : allCards) {
            Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(card)).click();
            Thread.sleep(2000);
            WebElement cardAfterClick = Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id='" + card.getAttribute("id") + "']")));
            try {
                driver.findElement(By.className("badge-outline")); //.cssSelector("div")).getAttribute("class").contains("overflow-hidden"))
                tags = driver.findElements(By.cssSelector("div[id^='tag-']"));
                if (tags.size() > 1 && !card.findElement(By.cssSelector("div")).getAttribute("class").contains("overflow-hidden")) {
                    cardWithMultipleTags = cardAfterClick;
                    actions.sendKeys(Keys.ESCAPE);
                    break;
                } else {
                    actions.sendKeys(Keys.ESCAPE);
                }
            } catch (NoSuchElementException e) {
                actions.sendKeys(Keys.ESCAPE);
            }
        }
        return cardWithMultipleTags;
    }

    public WebElement chivetteIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("label-chivette")));
    }

    public WebElement commentBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='comment']")));
    }

    public WebElement commentIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='comment']"))).findElement(By.className("fa-comment"));
    }

    public WebElement createBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("submission-create")));
    }

    public WebElement disqusSection() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("disqus_thread")));
    }

    public WebElement downvoteBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-thumbs-down")));
    }

    public WebElement faceBookBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-facebook-f")));
    }

    public WebElement favoriteBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id^='toggle-favorite-']"))).findElement(By.className("fa-heart"));
    }

    public WebElement favoriteOverlay() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='card-image-overlay']")));
    }
    public WebElement featuredCommentIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-comment")));
    }
    public WebElement featuredIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='Featured on myCHIVE']")));
    }

    public WebElement firstCard() {
        return Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])")));
    }

    public WebElement firstCardEnhanced() {
        WebElement first = null;
        try {
            first = Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id^='submission-image']")));
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
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("animate-spin")));
    }

    public WebElement reportBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-flag")));
    }

    public WebElement reportCopyright() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("report-copyright")));
    }

    public WebElement reportModalHeader() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("header"))).findElement(By.cssSelector("div"));
    }

    public WebElement reportModalText() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='bg-dark-char']")));
    }

    public WebElement reportOffensive() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("report-offensive")));
    }

    public WebElement reportOther() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("report-other")));
    }

    public WebElement reportSpam() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("report-spam")));
    }

    public WebElement submissionTitle() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"))).findElement(By.className("text-sm"));
    }

    public WebElement tag() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("badge-outline")));
    }

    public WebElement cardAtPosition(int xCoordinate, int yCoordinate){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (WebElement) js.executeScript(
                "return document.elementFromPoint(arguments[0], arguments[1]);",
                xCoordinate,
                yCoordinate);
    }

    public WebElement upvoteBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-thumbs-up")));
    }
    public String upvoteBtnClass() {
        return "fa-thumbs-up";
    }
    public WebElement userName() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-username]")));
    }
    public String userNameSelector(){
        return "a[data-username]";
    }
    public WebElement verifiedIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("label-verified")));
    }

    public WebElement voteCounter() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id^='vote-counter-']")));
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

    public double getCardHeight(WebElement card) {
        return card.findElement(By.cssSelector("div[id^='card-image']")).getRect().getHeight();
    }

    public double getCardRatio(double height, double width){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(height / width));
    }
    public double getCardWidth(WebElement card) {
        return card.findElement(By.cssSelector("div[id^='card-image']")).getRect().getWidth();
    }

}
