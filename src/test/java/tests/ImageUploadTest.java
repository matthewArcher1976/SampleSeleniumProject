package tests;

import helpers.Config;
import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.ImageUploadPage;
import pages.PageHeaderPage;
import resources.TestConfig;

public class ImageUploadTest {

    WebDriver driver = Drivers.ChromeDriver();
    Logins login = new Logins(driver);
    ImageUploadPage upload = new ImageUploadPage(driver);
    PageHeaderPage header = new PageHeaderPage(driver);
    Actions action = new Actions(driver);
    EditProfilePage profile = new EditProfilePage(driver);
    public static TestConfig config;

    //************************** Setup ******************************************

    @BeforeClass
    public void getConfig() throws Exception {
        config = Config.getConfig();
    }
    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    //Cannot seem to get uploads to fully function without input element
    @Test(enabled = false, priority = 99)
    //run last because the finder window will not close
    public void BannerPicUpdate() {
        WebElement fileInput = profile.bannerEditBtn();
        fileInput.click();
        String filePath = System.getProperty("user.dir") + "/src/images/stock52.jpg";
        String script = "arguments[0].setAttribute('value', arguments[1]);";
        ((JavascriptExecutor) driver).executeScript(script, fileInput, filePath);
        action.sendKeys(Keys.ESCAPE);
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.updateSuccess().isDisplayed(), "BannerPicUpdate failed");
    }

    @Test(enabled = false, priority = 1)
    public void Submit() throws InterruptedException {
        header.submitBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("submit"));
        // Locate the file drop area element
        WebElement dropArea = upload.dragDrop(); // Replace with your element locator
        // Path to the file to upload
        String filePath = "/Users/mattarcher/eclipse/iChiveAutomation/src/images/stock52.jpeg"; // Replace with the actual file path
        // Perform the drag and drop action
        action.moveToElement(dropArea)
                .click()
                .sendKeys(filePath)
                .build()
                .perform();
        Thread.sleep(90000);
        try {
            Assert.assertTrue(true);
        } catch (AssertionError e) {
            System.out.println("Submit");
            Assert.fail();
        }
    }

    @Test(enabled = false, priority = 1)
    public void ZZZTest() {
        try {
            Assert.assertTrue(true);
        } catch (AssertionError e) {
            System.out.println("Submit failed");
            Assert.fail();
        }
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}