package tests;

import helpers.Drivers;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginModalPage;

import java.util.ArrayList;

@SuppressWarnings("DefaultAnnotationParam")
public class SignupTestTest {

    WebDriver driver = Drivers.ChromeDriver();
    LoginModalPage modal = new LoginModalPage(driver);

    @BeforeTest
    @Parameters({"url"})
    public void login(@Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
        driver.get(url);
    }

    @BeforeMethod
    @Parameters({"url"})
    public void setDriver(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
    }

    @AfterClass
    public void TearDown() {
        driver.close();
    }

    //************************** Begin Tests ********************************************

    @Test(enabled = false, priority = 2)
    public void BannedName() throws InterruptedException {
        ArrayList<String> wordList = helpers.UserNameBlacklist.getWordList();
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

    @Test(enabled = true)
    public void TOSNotSelected() {
        modal.loginBtn().click();
        modal.signUpTab().click();
        Assert.assertEquals(modal.termsCheckbox().getAttribute("value"), "false", "TOS Checkbox should not be selected before user clicks it");
        modal.termsCheckbox().click();
        Assert.assertEquals(modal.termsCheckbox().getAttribute("value"), "true", "TOS Checkbox should be selected after user clicks it");
    }
}
