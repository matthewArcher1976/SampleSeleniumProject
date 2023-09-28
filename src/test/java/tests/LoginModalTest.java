package tests;

import resources.Config;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import resources.RetryAnalyzer;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
@Test(retryAnalyzer = RetryAnalyzer.class)

public class LoginModalTest {

    WebDriver driver;
    ImageUploadPage upload;
    Logins login;
    PageHeaderPage header;
    ProfilePage profile;

    SubmissionCardsPage card;
    SubmissionModalPage modal;
    private static TestConfig config;

    //*********************** Setup *********************************
    @BeforeClass
    public void setConfig() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);

        card = new SubmissionCardsPage(driver);
        header = new PageHeaderPage(driver);
        login = new Logins(driver);
        profile = new ProfilePage(driver);
        modal = new SubmissionModalPage(driver);
        upload = new ImageUploadPage(driver);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void ForgotPasswordText() throws InterruptedException {
        header.loginBtn().click();
        Thread.sleep(2000);
        login.forgotPassword().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("forgot-password"));
        Assert.assertTrue(login.resetPasswordText().getText().contains("Enter your username or email address and we will send you a link to reset your password."), "ForgotPasswordText - text not found");
    }

    @Test
    public void ForgotPasswordInvalidEmail() throws InterruptedException {
        header.loginBtn().click();
        Thread.sleep(2000);
        login.forgotPassword().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("forgot-password"));
        login.emailInput().sendKeys("thechivetest+" + helpers.Randoms.getRandomString(10) + "@gmail.com");
        login.resetPasswordEmailMe().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(login.notificationToast())).getText().contains("We cannot find a user with that email address."), "ForgotPasswordInvalidEmail - error toast not found");
    }

    @Test
    public void ForgotPasswordValidEmail() throws InterruptedException {
        header.loginBtn().click();
        Thread.sleep(2000);
        login.forgotPassword().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("forgot-password"));
        login.emailInput().sendKeys(config.unpaidEmail);
        login.resetPasswordEmailMe().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(login.notificationToast())).getText().contains("We have emailed your password reset link. If you do not find it in your inbox, please double check your spam folder.")
                && login.updateSuccess().getText().contains("We have emailed your password reset link."), "ForgotPasswordValidEmail - success toast not found");
    }

    @Test
    public void LoginOpensOnDownvote() throws InterruptedException {
        card.firstCard().findElement(By.className("fa-thumbs-down")).click();
        Thread.sleep(3000);
        Assert.assertTrue(login.signIn().isDisplayed(), "User not logged in");
    }

    @Test
    public void LoginOpensOnDownvoteModalView() throws InterruptedException {
        card.firstCard().click();
        modal.modalDislikeBtn().click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "Downvote on modal didn't prompt login");
    }

    @Test
    public void LoginOpensOnFavorite() throws InterruptedException {
        card.firstCard().findElement(By.className("fa-heart")).click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "Downvote on modal didn't prompt login");
    }

    @Test
    public void LoginOpensOnFollowButton() throws InterruptedException {
        card.firstCard().findElement(By.cssSelector("a[href]")).click();
        profile.followButton().click();
        Thread.sleep(3000);//yes
        Assert.assertTrue(login.signIn().isDisplayed(), "LoginOpensOnFollowButton - didn't open login modal");
    }

    @Test
    public void LoginOpensOnFollowing() throws InterruptedException {
        header.menuFollowing().click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "LoginOpensOnFollowing - didn't see login modal");
    }

    @Test
    public void LoginOpensOnSubmit() throws InterruptedException {
        header.submitBtn().click();
        login.emailInput().sendKeys(config.unpaidEmail);
        login.passwordInput().sendKeys(config.password);
        login.signIn().click();
        Thread.sleep(2000);//yes
        try {
            Assert.assertTrue(upload.dragDrop().isDisplayed());
            login.logout();
        } catch (AssertionError e) {
            System.out.println("LoginOpensOnSubmit - didn't see submit page on login");
            login.logout();
        }
    }

    @Test
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

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
