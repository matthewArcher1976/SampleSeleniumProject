package pages;

import helpers.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

@SuppressWarnings("unused")
public class TopChiversPage {
    WebDriver driver;

    public TopChiversPage(WebDriver driver) {
        this.driver = driver;
    }

    //*********************** WebElements *******************************

    public List<WebElement> allRows() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("[id^='leaderboard-row-']:not([class='text-lg group-hover:text-primary break-all']):not([id*='position']):not([id^='leaderboard-row-points-'])")
        ));
    }

    public WebElement ChivetteAvatar() {
        return Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[style*='background-image']")));
    }

    public By ChivetteAvatarBy() {

        return By.cssSelector("div[style*='background-image']");
    }

    public WebElement firstRow() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='leaderboard-row-']:not([class='text-lg group-hover:text-primary break-all']):not([id*='position']):not([id^='leaderboard-row-points-'])")));
    }

    public WebElement FollowBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='btn-follow-']"))).findElement(By.className("btn"));
    }

    public By FollowBtnBy() {
        return By.cssSelector("button[id*='follow-btn']");
    }

    public WebElement topChiversHeader() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-award")));
    }

    public WebElement followCheck() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-check")));
    }

    public WebElement followCircle() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-circle-plus")));
    }

    public WebElement points() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id^='leaderboard-row-points-']")));
    }

    public int pointsNumber() {
        String points = Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id^='leaderboard-row-points-']"))).getText();
        return Integer.parseInt(points);
    }

    public String pointsSelector() {
        return "div[id^='leaderboard-row-points-']";
    }

    public WebElement recentlyVerifiedUser() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='row-recently-verified-username-']")));
    }

    public WebElement recentlyVerified() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Recently Verified')]")));
    }

    public WebElement shareIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("svg[data-icon='share']")));
    }

    public WebElement socialFacebookLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-facebook-f")));
    }

    public WebElement socialInstagramLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-instagram")));
    }

    public WebElement socialTwitterLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-twitter")));
    }

    public WebElement starIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.className("mask-star-2")));
    }

    public String starIconClass() {
        return "mask-star-2";
    }

    public WebElement tabAllTime() {
        return Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='All Time']")));
    }

    public WebElement tabPastMonth() {
        return Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Past Month']")));
    }

    public WebElement tag() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='tag-']")));
    }

    public WebElement topChiversPosition() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='leaderboard-row-position-']")));
    }

    public WebElement topChiversProfilePic() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='leaderboard-row-']"))).findElement(By.className("inline-block"));
    }

    public WebElement topChiversRow() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='leaderboard-row-']")));
    }

    public WebElement topChiversTab() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("menu-Top-Chivers")));
    }

    public WebElement UserName() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id^='leaderboard-row-username-']")));
    }

    public String UserNameSelector() {
        return "div[id^='leaderboard-row-username-']";
    }

    public WebElement trendArrowIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-arrow-trend-up")));
    }

    public WebElement trendingText() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Top Chivers')]")));
    }
}
