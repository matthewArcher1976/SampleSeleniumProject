package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("unused")
public class AdminLoginPage {

    WebDriver driver;

    public AdminLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    //******************** Elements ***********************

    public WebElement emailInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
    }

    public WebElement passwordInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
    }

    public WebElement passwordReset() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href='/admin/password/reset']")));
    }

    public WebElement rememberMe() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("checkbox")));
    }

    public WebElement submitButton() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']")));
    }

}