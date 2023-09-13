package pages;

import helpers.PageActions;
import helpers.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ProfilePage {
    //TODO - profile page, not the edit
    private final WebDriver driver;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    //******************************** Begin Elements ****************************************
    public List<WebElement> allCards() {
        return driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image'])"));
    }

    public List<WebElement> allFollowers() {
        return driver.findElements(By.cssSelector("[id^='user-card-']:not([id*='follow-btn'])"));
    }

    public WebElement amazonLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-wishlist_url")));
    }

    public WebElement blockBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[id*='block-btn']")));
    }

    public WebElement chivetteIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("label-chivette")));
    }

    public WebElement cmgLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Chive Media Group")));
    }

    public WebElement editButton() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("edit-profile-button")));
    }

    public WebElement facebookLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-facebook")));
    }

    public WebElement facebookLogo() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fb_logo")));
    }

    public WebElement firstCard() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='submission-']")));
    }

    public WebElement followButton() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id*='follow-btn']")));
    }

    public WebElement instagramLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-instagram")));
    }
    public WebElement nftSubmitButton() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[class='btn float-right btn-primary']")));
    }
    public WebElement nftTab() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("user_profile_nft")));
    }
    public WebElement nftToast() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("Vue-Toastification__toast")));
    }
    public WebElement nftWalletError() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("text-red-500")));
    }
    public WebElement nftWalletInput() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.name("wallet")));
    }
    public WebElement otherUserName(String yourName) {
        String otherUser;
        WebElement otherCard = null;
        PageActions.scrollDown(driver, 2);
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
    public WebElement pointsTab() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("user_profile_points")));
    }
    public WebElement privacyTermsLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Terms & Privacy")));
    }

    public WebElement profile404GIF() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='404 Travolta Gif']")));
    }

    public WebElement profile404text() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'Whatever you were hoping to find isn')]")));
    }

    public WebElement profilePic() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt*='Profile picture']")));
    }

    public WebElement tabFeatured() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("user_profile_featured")));
    }

    public WebElement tabFavorite() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("user_profile_favorites")));
    }

    public WebElement tabFollowers() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("user_profile_followers")));
    }

    public WebElement tabFollowing() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("user_profile_following")));
    }

    public WebElement tiktokLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-tiktok")));
    }

    public WebElement twitterLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-twitter")));
    }

    public WebElement userName() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("profile-username")));
    }

    public WebElement websiteIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("social-button-website")));
    }


    //******************************** Begin Actions ****************************************

    // pulls the user handle out of the social media link url
    public String userGetter(WebElement element) {
        String link = element.getAttribute("href");
        String[] parts = link.split("/");
        return parts[parts.length - 1];
    }

}
