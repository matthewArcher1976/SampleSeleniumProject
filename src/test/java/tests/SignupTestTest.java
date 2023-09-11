package tests;

import resources.Config;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginModalPage;
import resources.TestConfig;
import resources.UserNameBlacklist;

import java.util.ArrayList;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class SignupTestTest {

    WebDriver driver;
    private static TestConfig config;
    LoginModalPage modal;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);

        modal = new LoginModalPage(driver);
    }

    @BeforeClass
    public void login() {
        driver.get(config.url);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test(enabled = false)
    public void BannedName() throws InterruptedException {
        ArrayList<String> wordList = UserNameBlacklist.getWordList();
        ArrayList<String> failed = new ArrayList<>();
        modal.loginBtn().click();
        Thread.sleep(2000);
        modal.signUpTab().click();
        Thread.sleep(2000);
        modal.email().sendKeys("thechivetest+" + helpers.Randoms.getRandomString(10) + "@gmail.com");
        modal.password().sendKeys("Chive1234");
        modal.passwordConfirm().sendKeys("Chive1234");
        modal.termsCheckbox().click();
        Thread.sleep(2000);
        for (String word : wordList) {
            boolean assertionFailed = false;
            modal.displayName().clear();
            modal.displayName().sendKeys(word);
            Thread.sleep(1000);
            modal.createAccountBtn().click();
            Thread.sleep(3000);
            try {
                Assert.assertEquals(modal.notificationToast().getText(), "The selected username is invalid.");
            } catch (AssertionError e) {
                assertionFailed = true;
            }
            if (assertionFailed) {
                modal.createAccountBtn().click();
                Thread.sleep(3000);
                try {
                    Assert.assertEquals(modal.notificationToast().getText(), "The selected username is invalid.");
                    modal.invalidUserNameClose().click();
                } catch (AssertionError e) {
                    System.out.println("Did not see the invalid username toast for " + word + " second try");
                    failed.add(word);
                }
            } else {
                continue;
            }
            try {
                modal.invalidUserNameClose().click();
            } catch (ElementNotInteractableException e) {
                e.printStackTrace();
            }
        }
        if (!failed.isEmpty()) {
            System.out.println("Failed usernames: " + failed);
            Assert.fail();
        }
    }

    @Test
    public void TOSNotSelected() {
        modal.loginBtn().click();
        modal.signUpTab().click();
        Assert.assertEquals(modal.termsCheckbox().getAttribute("value"), "false", "TOS Checkbox should not be selected before user clicks it");
        modal.termsCheckbox().click();
        Assert.assertEquals(modal.termsCheckbox().getAttribute("value"), "true", "TOS Checkbox should be selected after user clicks it");
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}


