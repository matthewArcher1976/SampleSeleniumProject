package tests;


import helpers.CustomExpectedConditions;
import helpers.Logins;
import helpers.PrettyAsserts;
import helpers.Waiter;
import io.github.sukgu.Shadow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import resources.Config;
import resources.RetryAnalyzer;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

@Test(retryAnalyzer = RetryAnalyzer.class)

public class MobileLoginModalTest {

    WebDriver driver;
    Actions action;
    ImageUploadPage upload;
    Logins login;
    PageHeaderPage header;
    EditProfilePage editProfile;
    ProfilePage profile;

    SubmissionCardsPage card;
    SubmissionModalPage modal;
    MobileViewPage mobile;
    Shadow shadow;
    private static TestConfig config;
    public final String USER_NAME = "DeleteMeTest";

    //*********************** Setup *********************************
    @BeforeClass
    public void setConfig() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverTypeMobile);
        action = new Actions(driver);
        shadow =  new Shadow(driver);
        card = new SubmissionCardsPage(driver);
        header = new PageHeaderPage(driver);
        login = new Logins(driver);
        profile = new ProfilePage(driver);
        mobile = new MobileViewPage(driver);
        modal = new SubmissionModalPage(driver);
        upload = new ImageUploadPage(driver);
        editProfile = new EditProfilePage(driver);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void ForgotPasswordText() throws InterruptedException {
        header.loginBtn().click();
        Thread.sleep(2000);
        login.forgotPassword().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("forgot-password"));
        Assert.assertTrue(login.resetPasswordText().getText().contains("Enter your username or email address and we will send you a link to reset your password."), "ForgotPasswordText - text not found");
    }

    @Test
    public void ForgotPasswordInvalidEmail() throws InterruptedException {
        header.loginBtn().click();
        Thread.sleep(2000);
        login.forgotPassword().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("forgot-password"));
        login.emailInput().sendKeys("thechivetest+" + helpers.Randoms.getRandomString(10) + "@gmail.com");
        login.resetPasswordEmailMe().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.visibilityOf(login.notificationToast())).getText().contains("We cannot find a user with that email address."), "ForgotPasswordInvalidEmail - error toast not found");
    }

    @Test
    public void ForgotPasswordValidEmail() throws InterruptedException {
        header.loginBtn().click();
        Thread.sleep(2000);
        login.forgotPassword().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("forgot-password"));
        login.emailInput().sendKeys(config.unpaidEmail);
        login.resetPasswordEmailMe().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.visibilityOf(login.notificationToast())).getText().contains("We have emailed your password reset link. If you do not find it in your inbox, please double check your spam folder.")
                && login.updateSuccess().getText().contains("We have emailed your password reset link."), "ForgotPasswordValidEmail - success toast not found");
    }

    @Test(enabled = false)
    public void LoginOpensOnDownvote() throws InterruptedException {
        card.firstCard().findElement(By.className("fa-thumbs-down")).click();
        Thread.sleep(3000);
        Assert.assertTrue(login.signIn().isDisplayed(), "Login modal not found after clicking downvote");
    }

    @Test(enabled = false)
    public void LoginOpensOnDownvoteModalView() throws InterruptedException {
        card.firstCard().click();
        modal.modalDislikeBtn().click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "Downvote on modal didn't prompt login");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void LoginOpensOnFavorite() throws InterruptedException {
        card.firstCard().findElement(By.className("fa-heart")).click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "Downvote on modal didn't prompt login");
    }

    @Test
    public void LoginOpensOnFollowButton() throws InterruptedException {
        card.firstCard().findElement(By.cssSelector("a[data-username]")).click();
        profile.followButton().click();
        Thread.sleep(2000);//yes
        Assert.assertTrue(login.signIn().isDisplayed(), "LoginOpensOnFollowButton - didn't open login modal");
    }

    @Test
    public void LoginOnFollowingTab() {
        mobile.hamburgerMenu().click();
        mobile.mobileFollowing().click();
        login.emailInput().sendKeys(config.unpaidEmail);
        login.passwordInput().sendKeys(System.getenv("TEST_PWD"));
        login.signIn().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.urlContains("following")));
        header.userMenu().click();


    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void LoginOpensOnSubmit() throws InterruptedException {
        header.submitBtn().click();
        login.emailInput().sendKeys(config.unpaidEmail);
        login.passwordInput().sendKeys(System.getenv("TEST_PWD"));
        login.signIn().click();
        Thread.sleep(4000);
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(upload.dragDropMobile()), "Did not go to submit page on login");

    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void LoginOpensOnUpvote() throws InterruptedException {
        card.firstCard().findElement(By.className("fa-thumbs-up")).click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "LoginOpensOnUpvote - didn't see login modal");
    }

    @Test
    public void LoginOpensOnUpvoteModalView() throws InterruptedException {
        card.firstCard().click();
        Thread.sleep(2000);
        modal.modalLikeBtn().click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "Downvote on modal didn't prompt login");
    }
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void EmailTaken()  {
        header.loginBtn().click();
        login.signUpTab().click();
        login.userName().sendKeys(USER_NAME);
        login.emailInput().sendKeys(config.unpaidEmail);
        login.birthdayInput().click();
        login.birthdayInput().sendKeys("12121990");
        login.passwordInput().sendKeys(System.getenv("TEST_PWD"));
        login.passwordConfirmInput().sendKeys(System.getenv("TEST_PWD"));
        login.termsCheckbox().click();
        login.createAccountBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(login.notificationToast()));
        Assert.assertTrue(login.notificationToast().getText().contains("The email has already been taken"));
    }

    @Test
    public void PasswordMismatch() {
        header.loginBtn().click();
        login.signUpTab().click();
        login.userName().sendKeys(USER_NAME);
        login.emailInput().sendKeys("thechivetest+" + USER_NAME + "@gmail.com");
        login.birthdayInput().click();
        login.birthdayInput().sendKeys("12121990");
        login.passwordInput().sendKeys(System.getenv("TEST_PWD"));
        login.passwordConfirmInput().sendKeys(System.getenv("TEST_PWD") + "567");
        login.termsCheckbox().click();
        login.createAccountBtn().click();
        Assert.assertTrue(login.errorText().getText().contains("The value must be equal to the other value"));
    }

    @Test
    public void SignupWithEmail() throws InterruptedException {
        header.loginBtn().click();
        login.signUpTab().click();
        login.userName().sendKeys(USER_NAME);
        login.emailInput().sendKeys("thechivetest+" + USER_NAME + "@gmail.com");
        login.birthdayInput().click();
        login.birthdayInput().sendKeys("12121990");
        login.passwordInput().sendKeys(System.getenv("TEST_PWD"));
        login.passwordConfirmInput().sendKeys(System.getenv("TEST_PWD"));
        login.termsCheckbox().click();
        login.createAccountBtn().click();
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
        login.signUpTab().click();
        login.userName().sendKeys(USER_NAME);
        login.emailInput().sendKeys("thechivetest+" + USER_NAME + "@gmail.com");
        login.birthdayInput().click();
        login.birthdayInput().sendKeys("12122020");
        login.passwordInput().sendKeys(System.getenv("TEST_PWD"));
        login.passwordConfirmInput().sendKeys(System.getenv("TEST_PWD"));
        login.termsCheckbox().click();
        login.createAccountBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(login.notificationToast()));
        Assert.assertTrue(login.notificationToast().getText().contains("You should have 18 years to use the site"));
    }

    @Test
    public void TOSNotSelected() {
        header.loginBtn().click();
        login.signUpTab().click();
        Assert.assertEquals(login.termsCheckbox().getAttribute("value"), "false", "TOS Checkbox should not be selected before user clicks it");
        login.termsCheckbox().click();
        Assert.assertEquals(login.termsCheckbox().getAttribute("value"), "true", "TOS Checkbox should be selected after user clicks it");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void UserNameTaken() {
        header.loginBtn().click();
        login.signUpTab().click();
        login.userName().sendKeys("TheChiveTest");
        login.emailInput().sendKeys("thechivetest+" + USER_NAME + "@gmail.com");
        login.birthdayInput().click();
        login.birthdayInput().sendKeys("12121990");
        login.passwordInput().sendKeys(System.getenv("TEST_PWD"));
        login.passwordConfirmInput().sendKeys(System.getenv("TEST_PWD"));
        login.termsCheckbox().click();
        login.createAccountBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(login.notificationToast()));
        Assert.assertTrue(login.notificationToast().getText().contains("The username has already been taken."));
    }

    //************************** Teardown ********************************************

    @AfterMethod
    public void logout(){
        try {login.logout();}catch (Exception ignore){}
    }
    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
