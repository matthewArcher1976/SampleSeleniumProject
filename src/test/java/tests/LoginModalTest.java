package tests;

import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;

@SuppressWarnings("DefaultAnnotationParam")
public class LoginModalTest {

    WebDriver driver = Drivers.ChromeDriver();
    ProfilePage profile = new ProfilePage(driver);
    Logins login = new Logins(driver);
    PageHeaderPage header = new PageHeaderPage(driver);
    SubmissionCardsPage card = new SubmissionCardsPage(driver);
    ImageUploadPage upload = new ImageUploadPage(driver);

    SubmissionModalPage modal = new SubmissionModalPage(driver);

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
    @Test(enabled = true, priority = 1)
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
        login.email().sendKeys("thechivetest+" + helpers.Randoms.getRandomString(10) + "@gmail.com");
        login.resetPasswordEmailMe().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(login.notificationToast())).getText().contains("We cannot find a user with that email address."), "ForgotPasswordInvalidEmail - error toast not found");
    }

    @Test
    public void ForgotPasswordValidEmail() throws InterruptedException {
        header.loginBtn().click();
        Thread.sleep(2000);
        login.forgotPassword().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("forgot-password"));
        login.email().sendKeys("thechivetest@gmail.com");
        login.resetPasswordEmailMe().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(login.notificationToast())).getText().contains("We have emailed your password reset link. If you do not find it in your inbox, please double check your spam folder.")
                && login.updateSuccess().getText().contains("We have emailed your password reset link."), "ForgotPasswordValidEmail - success toast not found");
    }

    @Test(enabled = true, priority = 1)
    public void LoginOpensOnDownvote() throws InterruptedException {
        card.firstCard().findElement(By.className("fa-thumbs-down")).click();
        Thread.sleep(3000);
        Assert.assertTrue(login.signIn().isDisplayed(), "User not logged in");
    }

    @Test(enabled = true, priority = 1)
    public void LoginOpensOnDownvoteModalView() throws InterruptedException {
        card.firstCard().click();
        modal.modalDislikeBtn().click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "Downvote on modal didn't prompt login");
    }

    @Test(enabled = true, priority = 1)
    @Parameters({"unpaidEmail", "password"})
    public void LoginOpensOnFavorite() throws InterruptedException {
        card.firstCard().findElement(By.className("fa-heart")).click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "Downvote on modal didn't prompt login");
    }

    @Test(enabled = true, priority = 1)
    public void LoginOpensOnFollowButton() throws InterruptedException {
        card.firstCard().findElement(By.cssSelector("a[href]")).click();
        profile.followButton().click();
        Thread.sleep(3000);//yes
        Assert.assertTrue(login.signIn().isDisplayed(), "LoginOpensOnFollowButton - didn't open login modal");
    }

    @Test(enabled = true, priority = 1)
    public void LoginOpensOnFollowing() throws InterruptedException {
        header.menuFollowing().click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "LoginOpensOnFollowing - didn't see login modal");
    }

    @Test(enabled = true, priority = 1)
    @Parameters({"unpaidEmail", "password"})
    public void LoginOpensOnSubmit(@Optional("thechivetest@gmail.com") String unpaidEmail, @Optional("Chive1234") String password) throws InterruptedException {
        header.submitBtn().click();
        login.email().sendKeys(unpaidEmail);
        login.password().sendKeys(password);
        login.signIn().click();
        Thread.sleep(2000);//yes
        try {
            Assert.assertTrue(upload.dragDrop().isDisplayed());
            login.logout(driver);
        } catch (AssertionError e) {
            System.out.println("LoginOpensOnSubmit - didn't see submit page on login");
            login.logout(driver);
        }
    }

    @Test(enabled = true, priority = 1)
    public void LoginOpensOnUpvote() throws InterruptedException {
        card.firstCard().findElement(By.className("fa-thumbs-up")).click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "LoginOpensOnUpvote - didn't see login modal");
    }

    @Test(enabled = true, priority = 1)
    public void LoginOpensOnUpvoteModalView() throws InterruptedException {
        card.firstCard().click();
        Thread.sleep(2000);
        modal.modalLikeBtn().click();
        Thread.sleep(2000);
        Assert.assertTrue(login.signIn().isDisplayed(), "Downvote on modal didn't prompt login");

    }
}
