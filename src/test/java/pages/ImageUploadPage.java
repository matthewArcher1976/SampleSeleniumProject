package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ImageUploadPage {
    WebDriver driver;

    public ImageUploadPage(WebDriver driver) {
        this.driver = driver;
    }

    //************************** Begin Tests ********************************************

    public WebElement dragDrop() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-arrow-pointer")));
    }
    public WebElement dragDropMobile() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-upload")));
    }

}
