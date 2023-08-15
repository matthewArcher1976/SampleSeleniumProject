package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings("unused")
public class ProfilePage {
    //TODO - profile page, not the edit
    private final WebDriver driver;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    SubmissionCardsPage subpage = new SubmissionCardsPage(null);


    //******************************** Begin Elements ****************************************
    public List<WebElement> allCards() {
        return driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image'])"));
    }

    public List<WebElement> allFollowers() {
        return driver.findElements(By.cssSelector("[id^='user-card-']:not([id*='follow-btn'])"));
    }

    public WebElement amazonLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-wishlist_url")));
    }

    By amazonIcon = By.id("social-button-wishlist_url");

    public WebElement blockBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[id*='block-btn']")));
    }

    public WebElement chivetteIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("label-chivette")));
    }

    public WebElement cmgLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Chive Media Group")));
    }

    public WebElement editButton() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("edit-profile-button")));
    }

    public WebElement facebookLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-facebook")));
    }

    public WebElement facebookLogo() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fb_logo")));
    }

    public WebElement firstCard() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='submission-']")));
    }

    public WebElement followButton() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id*='follow-btn']")));
    }

    public WebElement instagramLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-instagram")));
    }

    public WebElement otherUserName(String yourName) {
        String otherUser;
        WebElement otherCard = null;
        helpers.PageActions.scrollDown(driver, 2);
        List<WebElement> cards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image'])"));
        for (WebElement card : cards) {
            otherUser = card.findElement(By.partialLinkText("@")).getText();
            if (!otherUser.contains(yourName)) {
                otherCard = card;
                break;
            }
        }
        return otherCard;
    }


    public WebElement privacyTermsLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Terms & Privacy")));
    }

    public WebElement profile404GIF() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='404 Travolta Gif']")));
    }

    public WebElement profile404text() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'Whatever you were hoping to find isn')]")));
    }

    public WebElement profilePic() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt*='Profile picture']")));
    }

    public WebElement tabFeatured() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("user_profile_featured")));
    }

    public WebElement tabFavorite() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("user_profile_favorites")));
    }

    public WebElement tabFollowers() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("user_profile_followers")));
    }

    public WebElement tabFollowing() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("user_profile_following")));
    }

    public WebElement tiktokLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-tiktok")));
    }

    public WebElement twitterLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-twitter")));
    }

    public WebElement userName() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("profile-username")));
    }

    public WebElement websiteIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-website")));
    }


    //******************************** Begin Actions ****************************************

    // pulls the user handle out of the social media link url
    public String userGetter(WebElement element) {
        String link = element.getAttribute("href");
        String[] parts = link.split("/");
        return parts[parts.length - 1];
    }

}
