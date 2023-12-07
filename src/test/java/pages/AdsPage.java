package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AdsPage {
    WebDriver driver;

    public AdsPage(WebDriver driver) {
        this.driver = driver;
    }

    //****************************** Elements **********************************

    public By adFrame() {
        return By.cssSelector("iframe[id*=google_ads_iframe]");
    }
    public WebElement adImage() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("google_image_div")));
    }

    public WebElement closeAdButton() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("cbb")));
    }

    public WebElement sendFeedbackButton() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("report_text")));
    }
}
