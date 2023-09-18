package tests;

import helpers.CustomExpectedConditions;
import helpers.Waiter;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.EditProfilePage;
import pages.LoginModalPage;
import pages.PageHeaderPage;
import resources.Config;
import resources.RetryAnalyzer;
import resources.TestConfig;
import resources.UserNameBlacklist;

import java.util.ArrayList;

import static resources.getDriverType.getDriver;

public class SignupTestTest {

    WebDriver driver;
    private static TestConfig config;
    EditProfilePage editProfile;
    LoginModalPage loginModal;
    PageHeaderPage header;

    //************************** Setup ******************************************

    public final String USER_NAME = "DeleteMeTest";

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        editProfile = new EditProfilePage(driver);
        header = new PageHeaderPage(driver);
        loginModal = new LoginModalPage(driver);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test(enabled = false)//check the logic so it continues if it misses the notification toast
    public void BannedName() throws InterruptedException {
        ArrayList<String> wordList = UserNameBlacklist.getWordList();
        ArrayList<String> failed = new ArrayList<>();
        header.loginBtn().click();
        Thread.sleep(2000);
        loginModal.signUpTab().click();
        Thread.sleep(2000);
        loginModal.emailInput().sendKeys("thechivetest+" + helpers.Randoms.getRandomString(10) + "@gmail.com");
        loginModal.birthdayInput().click();
        loginModal.birthdayInput().sendKeys("12121990");
        loginModal.passwordInput().sendKeys("Chive1234");
        loginModal.passwordConfirmInput().sendKeys("Chive1234");
        loginModal.termsCheckbox().click();
        Thread.sleep(2000);
        for (String word : wordList) {
            boolean assertionFailed = false;
            loginModal.userName().clear();
            loginModal.userName().sendKeys(word);
            Thread.sleep(1000);
            loginModal.createAccountBtn().click();
            Thread.sleep(3000);
            try {
                Assert.assertEquals(loginModal.notificationToast().getText(), "The selected username is invalid.");
            } catch (AssertionError e) {
                assertionFailed = true;
            }
            if (assertionFailed) {
                loginModal.createAccountBtn().click();
                Thread.sleep(3000);
                try {
                    Assert.assertEquals(loginModal.notificationToast().getText(), "The selected username is invalid.");
                    loginModal.invalidUserNameClose().click();
                } catch (AssertionError e) {
                    System.out.println("Did not see the invalid username toast for " + word + " second try");
                    failed.add(word);
                }
            } else {
                continue;
            }
            try {
                loginModal.invalidUserNameClose().click();
            } catch (ElementNotInteractableException e) {
                e.printStackTrace();
            }
        }
        if (!failed.isEmpty()) {
            System.out.println("Failed usernames: " + failed);
            Assert.fail();
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void EmailTaken() throws InterruptedException {
        header.loginBtn().click();
        loginModal.signUpTab().click();
        loginModal.userName().sendKeys(USER_NAME);
        loginModal.emailInput().sendKeys(config.unpaidEmail);
        loginModal.birthdayInput().click();
        loginModal.birthdayInput().sendKeys("12121990");
        loginModal.passwordInput().sendKeys(config.password);
        loginModal.passwordConfirmInput().sendKeys(config.password);
        loginModal.termsCheckbox().click();
        loginModal.createAccountBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(loginModal.notificationToast()));
        Assert.assertTrue(loginModal.notificationToast().getText().contains("The email has already been taken"));
    }

    @Test
    public void PasswordMismatch() {
        header.loginBtn().click();
        loginModal.signUpTab().click();
        loginModal.userName().sendKeys(USER_NAME);
        loginModal.emailInput().sendKeys("thechivetest+" + USER_NAME + "@gmail.com");
        loginModal.birthdayInput().click();
        loginModal.birthdayInput().sendKeys("12121990");
        loginModal.passwordInput().sendKeys(config.password);
        loginModal.passwordConfirmInput().sendKeys(config.password + "567");
        loginModal.termsCheckbox().click();
        loginModal.createAccountBtn().click();
        Assert.assertTrue(loginModal.errorText().getText().contains("The value must be equal to the other value"));
    }

    @Test
    public void SignupWithEmail() throws InterruptedException {
        header.loginBtn().click();
        loginModal.signUpTab().click();
        loginModal.userName().sendKeys(USER_NAME);
        loginModal.emailInput().sendKeys("thechivetest+" + USER_NAME + "@gmail.com");
        loginModal.birthdayInput().click();
        loginModal.birthdayInput().sendKeys("12121990");
        loginModal.passwordInput().sendKeys(config.password);
        loginModal.passwordConfirmInput().sendKeys(config.password);
        loginModal.termsCheckbox().click();
        loginModal.createAccountBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("auth")));
        Assert.assertTrue(header.verifyEmailHeader().isEnabled(), "Account creation may have failed");
        //Teardown - delete the account
        Thread.sleep(5000);//getting a 500 sometimes because it tries to delete too soon
        header.userMenu().click();
        header.settingsBtn().click();
        editProfile.accountTab().click();
        editProfile.deleteAccountLink().click();
        editProfile.deleteConfirmBtn().click();
        Thread.sleep(2000);//let it delete
        driver.get(config.url + USER_NAME);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Assert.assertTrue(driver.getPageSource().contains("Whatever you were hoping to find isn't here."), "Account may not have been deleted");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void TooYoung() {
        header.loginBtn().click();
        loginModal.signUpTab().click();
        loginModal.userName().sendKeys(USER_NAME);
        loginModal.emailInput().sendKeys("thechivetest+" + USER_NAME + "@gmail.com");
        loginModal.birthdayInput().click();
        loginModal.birthdayInput().sendKeys("12122020");
        loginModal.passwordInput().sendKeys(config.password);
        loginModal.passwordConfirmInput().sendKeys(config.password);
        loginModal.termsCheckbox().click();
        loginModal.createAccountBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(loginModal.notificationToast()));
        Assert.assertTrue(loginModal.notificationToast().getText().contains("You should have 18 years to use the site"));
    }

    @Test
    public void TOSNotSelected() {
        header.loginBtn().click();
        loginModal.signUpTab().click();
        Assert.assertEquals(loginModal.termsCheckbox().getAttribute("value"), "false", "TOS Checkbox should not be selected before user clicks it");
        loginModal.termsCheckbox().click();
        Assert.assertEquals(loginModal.termsCheckbox().getAttribute("value"), "true", "TOS Checkbox should be selected after user clicks it");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void UserNameTaken() {
        header.loginBtn().click();
        loginModal.signUpTab().click();
        loginModal.userName().sendKeys("TheChiveTest");
        loginModal.emailInput().sendKeys("thechivetest+" + USER_NAME + "@gmail.com");
        loginModal.birthdayInput().click();
        loginModal.birthdayInput().sendKeys("12121990");
        loginModal.passwordInput().sendKeys(config.password);
        loginModal.passwordConfirmInput().sendKeys(config.password);
        loginModal.termsCheckbox().click();
        loginModal.createAccountBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(loginModal.notificationToast()));
        Assert.assertTrue(loginModal.notificationToast().getText().contains("The username has already been taken."));
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}


