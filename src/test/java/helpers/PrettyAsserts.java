package helpers;

import org.openqa.selenium.*;
import org.testng.Assert;

public class PrettyAsserts {


    public static boolean isIconSelected(WebElement element) {
        String s = element.getAttribute("class");
        Boolean filled = null;
        if (s.contains("text-white")) {
            filled = false;
        } else if (s.contains("text-primary") && !s.contains("text-white")) {
            filled = true;
        } else {
            System.out.println(s + "isSelected failed");
            Assert.fail();
        }
        return filled;
    }

    public static boolean isDisplayed(By by, WebDriver driver) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

}
