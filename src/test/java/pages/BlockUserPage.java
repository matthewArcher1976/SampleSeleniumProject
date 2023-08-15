package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BlockUserPage {

    WebDriver driver;

    public BlockUserPage(WebDriver driver) {
        this.driver = driver;
    }
    //******************** Elements ***********************

    public WebElement blockBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[id*='block-btn']")));
    }
}
