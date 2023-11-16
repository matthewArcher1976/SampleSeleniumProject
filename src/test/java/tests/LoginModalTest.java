package tests;


import helpers.PrettyAsserts;
import helpers.Waiter;
import io.github.sukgu.Shadow;
import org.openqa.selenium.interactions.Actions;
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

@Test(retryAnalyzer = RetryAnalyzer.class)

public class LoginModalTest {

    WebDriver driver;
    Actions action;
    ImageUploadPage upload;
    Logins login;
    PageHeaderPage header;
    ProfilePage profile;

    SubmissionCardsPage card;
    SubmissionModalPage modal;
    Shadow shadow;
    private static TestConfig config;

    //*********************** Setup *********************************
    @BeforeClass
    public void setConfig() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        action = new Actions(driver);
        shadow =  new Shadow(driver);
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

    @Test(retryAnalyzer = RetryAnalyzer.class)
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
        shadow.findElement(card.firstCard(), "a[data-username]").click();
      //  card.firstCard().findElement(By.cssSelector("a[href]")).click();
        Thread.sleep(2000);
        profile.followButton().click();
        Thread.sleep(2000);//yes
        Assert.assertTrue(login.signIn().isDisplayed(), "LoginOpensOnFollowButton - didn't open login modal");
    }

    @Test
    public void LoginOnFollowingTab() {
        header.menuFollowing().click();
        login.emailInput().sendKeys(config.unpaidEmail);
        login.passwordInput().sendKeys(config.password);
        login.signIn().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.urlContains("following")));
        header.userMenu().click();


    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void LoginOpensOnSubmit() {
        header.submitBtn().click();
        login.emailInput().sendKeys(config.unpaidEmail);
        login.passwordInput().sendKeys(config.password);
        login.signIn().click();
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(upload.dragDrop()), "Did not go to submit page on login");

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
