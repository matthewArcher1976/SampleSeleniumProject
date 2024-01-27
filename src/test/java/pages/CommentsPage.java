package pages;

import helpers.Waiter;
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
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("recaptcha-anchor")));
    }

    public WebElement commentPolicy() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("comment-policy")));
    }

    public WebElement commentTextInput() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class='textarea']")));
    }

    public WebElement commentTextPlaceholder() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("placeholder")));
    }

    public WebElement disqusFrame() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe[id*= 'dsq-'")));
    }

    public WebElement emailInput() {
        return Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='email']")));
    }

    public WebElement enterEmailLabel() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(),'Please enter your email address.')]")));
    }

    public WebElement enterNameLabel() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(),'Please enter your name.')]")));
    }

    public WebElement enterPasswordLabel() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(),'Please enter a password.')]")));
    }

    public WebElement guestCheckbox() {
        return Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.name("author-guest")));
    }

    public WebElement nameInput() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='display_name']")));
    }

    public WebElement proceedButton() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[aria-label='Post']")));
    }

    public WebElement submitCommentBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']")));
    }

    //************Actions*****************
    public void switchToCaptchaFrame() {
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[title='reCAPTCHA'")));
    }

    public void switchToDisqusFrame() {
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[id^= 'dsq-'")));
    }
}
