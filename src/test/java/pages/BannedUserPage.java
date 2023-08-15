package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BannedUserPage {

    WebDriver driver;

    public BannedUserPage(WebDriver driver) {
        this.driver = driver;
    }

    //******************** Elements ***********************
    public WebElement attention() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()=' Attention ']")));
    }

    public WebElement bannedScreen() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("ban-container")));
    }

    public WebElement bannedText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[text()=' As a banned user you still have the ability to browse the site but all ability to interact with the community has been suspended. If you feel that this action has been taken by mistake please submit a ticket ']")));
    }

    public WebElement contactLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("here")));
    }

    public WebElement reasons() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("ban-container"))).findElement(By.tagName("blockquote"));
    }

    public WebElement reasonsText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[text()=' Your account has been banned for the following reason: ']")));
    }

    public WebElement topChiversFollowBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='btn-follow-']"))).findElement(By.className("btn"));
    }

}
