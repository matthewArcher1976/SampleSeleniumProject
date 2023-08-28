package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@SuppressWarnings({"UnnecessaryBoxing", "unused", "BoxingBoxedValue"})
public class CustomExpectedConditions {

   public static ExpectedCondition<Boolean> textToDisappear(final By locator, final String text) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement element = driver.findElement(locator);
                String elementText = element.getText().trim();
                return !elementText.contains(text);
            }

            @Override
            public String toString() {
                return String.format("text ('%s') to disappear from element found by %s", text, locator);
            }
        };
    }
    public static ExpectedCondition<Boolean> pageLoaded() {
        return driver -> {
            // Inject jQuery into the page if it's not already present
            assert driver != null;
            if (!((Boolean) ((JavascriptExecutor) driver).executeScript("return window.jQuery != undefined"))) {
                ((JavascriptExecutor) driver).executeScript(
                        "var script = document.createElement('script');" +
                                "script.src = 'https://code.jquery.com/jquery-3.6.0.min.js';" +
                                "document.head.appendChild(script);"
                );
            }

            // Wait for jQuery to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.jsReturnsValue("return window.jQuery != undefined"));

            Boolean documentReady = Boolean.valueOf(((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));

            Boolean noActiveAjax = Boolean.valueOf((Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return jQuery.active == 0 || document.readyState != 'complete'"));

            return documentReady && noActiveAjax;
        };
    }


}
