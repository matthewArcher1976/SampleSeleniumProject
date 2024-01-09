package pages;

import helpers.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SubscriptionPage {
    WebDriver driver;

    public SubscriptionPage(WebDriver driver) {
        this.driver = driver;
    }

    //******************** Web Elements ***********************
    public WebElement chargebeeClose() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("cb-hp__close-icon")));
    }

    public WebElement chargebeeFrame() {
        return Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("iframe[id='cb-frame']")));
    }

    public WebElement chargebeeOrderSummary() {
        return Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-cb-id='cart_summary']")));
    }

    public By chargebeeOrderSummaryBy() {
        return By.cssSelector("div[data-cb-id='cart_summary']");
    }

    public WebElement monthlyJoinBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[data-price-id='myCHIVE-Subscription-USD-Monthly']")));
    }

    public By monthlyJoinBtnBy() {
        return By.cssSelector("button[data-price-id='myCHIVE-Subscription-USD-Monthly']");
    }

    public WebElement payWallSignIn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(),'Sign in here!')]")));
    }

    public WebElement subscriptionFooter() {
        return Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("section[class*='fixed bottom']")));
    }

}
