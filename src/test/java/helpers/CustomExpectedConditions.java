package helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


public class CustomExpectedConditions {


    public static ExpectedCondition<Boolean> cardsLoaded(){
        return driver -> {
            boolean notEmpty = false;
            assert driver != null;
            List <WebElement> allCards = driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image-'])"));
            if(allCards.size() > 16){
                notEmpty = true;
            }
            return notEmpty;
        };
    }
    public static ExpectedCondition<Boolean> tabLoaded(WebElement tab) {

        return driver -> {
            assert driver != null;
            try {
                Waiter.wait(driver).until(ExpectedConditions.stalenessOf(tab)); // aria-current attribute is added to the tab element when it loads which makes your tab element stale
                return true;
            }catch (TimeoutException e){
                return false;
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
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete'"));
            wait.until(ExpectedConditions.jsReturnsValue("return window.jQuery != undefined"));

            Boolean documentReady = ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete");
            Boolean noActiveAjax = (Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return jQuery.active == 0 || document.readyState != 'complete'");
            return documentReady && noActiveAjax;

        };
    }

    public static ExpectedCondition<Boolean> profileLoaded() {
        return driver -> {
            assert driver != null;
            String currentUrl = driver.getCurrentUrl();
            return currentUrl.matches(".+mychive\\.com/.+");
        };
    }


}
