package pages;

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
        return helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[id*='headlessui-disclosure-button-']")));
    }

    public WebElement mobileAvatar() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("avatar")));
    }

    public WebElement mobileLatest() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("mobile-menu-Latest")));
    }

    public WebElement mobileFeatured() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("mobile-menu-Featured")));
    }

    public WebElement mobileFollowing() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("mobile-menu-Following")));
    }

    public WebElement mobileTop() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("mobile-menu-Top-Chivers")));
    }

}
