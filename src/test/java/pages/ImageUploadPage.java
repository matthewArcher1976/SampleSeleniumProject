package pages;

import helpers.Waiter;
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

    public By dragDropBy() {
        return By.className("fa-arrow-pointer");
    }
    public WebElement dragDrop() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(dragDropBy()));
    }
    public By dragDropMobileBy() {
       return By.className("fa-upload");
    }

}
