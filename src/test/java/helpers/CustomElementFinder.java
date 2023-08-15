package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;


import java.util.List;

public class CustomElementFinder {
    public static List<WebElement> findElements(WebDriver driver, WebElement parentElement, String className) {
        helpers.Waiter.wait(driver).until((ExpectedCondition<Boolean>) d -> parentElement.isDisplayed());
        // Find child elements using the specified class name
        return parentElement.findElements(By.className(className));
    }
}
