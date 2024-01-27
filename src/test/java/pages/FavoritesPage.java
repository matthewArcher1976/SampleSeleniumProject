package pages;


import helpers.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

public class FavoritesPage {

    private final WebDriver driver;
    public FavoritesPage(WebDriver driver) {
        this.driver = driver;
    }

    //***********************  Web Elements  **********************

    public List<WebElement> allCards() {
        return driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image'])"));
    }

    public WebElement firstCard() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("bg-charleston")));
    }

    public WebElement toggleFave() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='toggle-favorite']")));
    }

    //**************** Actions/Helpers ***************************

    public boolean isHeartFilled() {
        WebElement heart = Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='toggle-favorite']"))).findElement(By.className("fa-heart"));
        String s = heart.getAttribute("class");
        Boolean filled = null;

        if (s.contains("text-white")) {
            filled = false;
        } else if (s.contains("text-primary") && !s.contains("text-white")) {
            filled = true;
        } else {
            System.out.println("isHeartFilled() failed");
            Assert.fail();
        }
        return filled;
    }

    public Boolean isHeartFilledCard(WebElement card) {
        WebElement heart = card.findElement(By.cssSelector("[id^='toggle-favorite']")).findElement(By.className("fa-heart"));
        String s = heart.getAttribute("class");
        Boolean filled = null;

        if (s.contains("text-white")) {
            filled = false;
        } else if (s.contains("text-primary") && !s.contains("text-white")) {
            filled = true;
        } else {
            System.out.println("isHeartFilled() failed");
            Assert.fail();
        }
        return filled;
    }

}
