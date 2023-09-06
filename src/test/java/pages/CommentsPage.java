package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("unused")
public class CommentsPage {
    private final WebDriver driver;

    public CommentsPage(WebDriver driver) {
        this.driver = driver;
    }

    //***********************  Web Elements  **********************

    public WebElement captchaCheck() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("recaptcha-anchor")));
    }
    public WebElement commentPolicy() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("comment-policy")));
    }
    public WebElement commentTextInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class='textarea']")));
    }
    public WebElement commentTextPlaceholder() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("placeholder")));
    }
    public WebElement disqusFrame() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe[id*= 'dsq-'")));
    }
    public WebElement emailInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='email']")));
    }
    public WebElement enterEmailLabel() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(),'Please enter your email address.')]")));
    }
    public WebElement enterNameLabel() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(),'Please enter your name.')]")));
    }
    public WebElement enterPasswordLabel() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(),'Please enter a password.')]")));
    }
    public WebElement guestCheckbox() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.name("author-guest")));
    }
    public WebElement nameInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='display_name']")));
    }
    public WebElement proceedButton() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[aria-label='Post']")));
    }
    public WebElement submitCommentBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']")));
    }

    //************Actions*****************
    public void switchToCaptchaFrame() {
        helpers.Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[title='reCAPTCHA'")));
    }
    public void switchToDisqusFrame() {
        helpers.Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[id^= 'dsq-'")));
    }
}
