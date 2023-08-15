package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class TopChiversPage {
    WebDriver driver;

    public TopChiversPage(WebDriver driver) {
        this.driver = driver;
    }

    //*********************** WebElements *******************************
    public List<WebElement> allRows() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("[id^='leaderboard-row-']")));
    }

    public WebElement followCheck() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-check")));
    }

    public WebElement followCircle() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-circle-plus")));
    }

    public WebElement recentlyVerifiedUser() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='row-recently-verified-username-']")));
    }

    public WebElement recentlyVerified() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Recently Verified')]")));
    }

    public WebElement shareIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("svg[data-icon='share']")));
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

    public WebElement tag() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='tag-']")));
    }

    public WebElement topChiversFollowBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='btn-follow-']"))).findElement(By.className("btn"));
    }

    public WebElement topChiversHeader() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-award")));
    }

    public WebElement topChiversPosition() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='leaderboard-row-position-']")));
    }

    public WebElement topChiversProfilePic() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='leaderboard-row-']"))).findElement(By.className("inline-block"));
    }

    public WebElement topChiversRow() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='leaderboard-row-']")));
    }

    public WebElement topChiversTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("menu-Top-Chivers")));
    }

    public WebElement topChiversUserName() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='leaderboard-row-username-']")));
    }

    public WebElement trendArrowIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-arrow-trend-up")));
    }

    public WebElement trendingText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Top Chivers')]")));
    }
}
