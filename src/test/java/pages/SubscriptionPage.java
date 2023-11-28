package pages;

import helpers.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SubscriptionPage {
    WebDriver driver;
    public SubscriptionPage(WebDriver driver){
        this.driver = driver;
    }

    //******************** Web Elements ***********************
    public WebElement chargebeeClose(){
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-cb-id='cb_close']")));
    }
    public WebElement chargebeeFrame(){
        return Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("iframe[id='cb-frame']")));
    }

    public WebElement chargebeeOrderSummary(){
        return Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-cb-id='cart_summary']")));
    }

    public WebElement monthlyJoinBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[data-price-id='myCHIVE-Subscription-USD-Monthly']")));
    }

    public String monthlyJoinBtnSelector(){
        return "button[data-price-id='myCHIVE-Subscription-USD-Monthly']";
    }
    public WebElement subscriptionFooter(){
        return Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("section[class*='fixed bottom']")));
    }

    public String noThanksBtnSelector(){
        return "button[class='mt-8 text-base']";
    }
}
