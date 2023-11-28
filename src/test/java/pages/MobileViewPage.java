package pages;

import helpers.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MobileViewPage {

    WebDriver driver;

    public MobileViewPage(WebDriver driver) {
        this.driver = driver;
    }

    //******************** Elements ***********************

    public WebElement hamburgerMenu() {
        return Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[id*='headlessui-disclosure-button-']")));
    }

    public WebElement mobileAvatar() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("avatar")));
    }

    public WebElement mobileLatest() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("mobile-menu-Latest")));
    }

    public WebElement mobileFeatured() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("mobile-menu-Featured")));
    }

    public WebElement mobileFollowing() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("mobile-menu-Following")));
    }

    public WebElement mobileTop() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("mobile-menu-Top-Chivettes")));
    }

    public WebElement ChivetteButton(){
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("verified-chivettes-shortcut")));
    }

}
