package tests;

import helpers.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import resources.Config;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.EditProfilePage;

import resources.RetryAnalyzer;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

public class EditProfileAccountTest {

    WebDriver driver;
    EditProfilePage profile;
    Logins login;

    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
        profile = new EditProfilePage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }
    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void DeleteAccountText(){
        //Actual deletion covered in SignUpTest
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.accountTab().click();
        profile.deleteAccountLink().click();
        Assert.assertTrue(driver.getPageSource().contains(" Before you continue, you should know that by deleting your account you will lose access to ALL of your submissions, followers, followees, and all data related to you."));
        profile.deleteNeverMindBtn().click();
    }

    @Test
    public void HotnessToggle() throws InterruptedException {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.accountTab().click();
        if (profile.hotnessToggle().getAttribute("value").equals("false")) {
            profile.hotnessToggle().click();
            Thread.sleep(1000);
            Assert.assertEquals("true", profile.hotnessToggle().getAttribute("value"), "\"Hotness toggle did not switch to on");
        } else if (profile.hotnessToggle().getAttribute("value").equals("true")) {
            profile.hotnessToggle().click();
            Assert.assertEquals("false", profile.hotnessToggle().getAttribute("value"), "Hotness toggle did not switch to off");
        } else if (!profile.hotnessToggle().getAttribute("value").equals("false") && !profile.hotnessToggle().getAttribute("value").equals("true")) {
            System.out.println("The Hotness toggle may be busted ");
            Assert.fail();
        }
        profile.saveProfileBtn().click();
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profile.updateSuccess()), "Success message not displayed after saving hotness toggle");
    }
    @Test
    public void MakePrivateToggle() throws InterruptedException {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.accountTab().click();
        if (profile.makePrivateToggle().getAttribute("value").equals("false")) {
            profile.makePrivateToggle().click();
            Thread.sleep(1000);//yes
            Assert.assertTrue(profile.makePrivateTitle().isDisplayed(), "Warning popup did not display after toggling make private on");
            profile.makePrivateNvmBtn().click();
            Thread.sleep(2000);//mhm
            Assert.assertEquals("false", profile.makePrivateToggle().getAttribute("value"),  "Make private toggle should not have switched on");

            profile.makePrivateToggle().click();
            profile.makePrivateAcceptBtn().click();
            Thread.sleep(3000);//I know

            profile.saveProfileBtn().click();
            Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
            driver.navigate().refresh();
            profile.accountTab().click();
            Thread.sleep(3000);//ok ok
            Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.attributeToBe(profile.makePrivateToggle(), "value", "true")), "Make private toggle did not switch to on");
            //if it's not set private when you load the page
        } else if (profile.makePrivateToggle().getAttribute("value").equals("true")) {
            profile.makePrivateToggle().click();
            profile.saveProfileBtn().click();
            Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
            driver.navigate().refresh();
            profile.accountTab().click();
            Thread.sleep(1000);
            Assert.assertEquals("false", profile.makePrivateToggle().getAttribute("value"), "Make private toggle did not switch to off");
            profile.makePrivateToggle().click();
            Thread.sleep(1000);
            Assert.assertTrue(profile.makePrivateTitle().isDisplayed(), "Warning popup did not display after toggling make private on");
            profile.makePrivateNvmBtn().click();
            Thread.sleep(2000);
            Assert.assertEquals("false", profile.makePrivateToggle().getAttribute("value"), "Make private toggle should not have switched on");
            profile.makePrivateToggle().click();
            profile.makePrivateAcceptBtn().click();
            Thread.sleep(2000);
            Assert.assertEquals("true", profile.makePrivateToggle().getAttribute("value"), "Make private toggle did not switch to on");

        } else if (!profile.makePrivateToggle().getAttribute("value").equals("false") && !profile.makePrivateToggle().getAttribute("value").equals("true")) {
            Assert.fail("Make private toggle may be busted");
        }

        Thread.sleep(2000);
        //Switch privacy back off
        if (profile.makePrivateToggle().getAttribute("value").equals("true")) {

            profile.makePrivateToggle().click();
            profile.saveProfileBtn().click();
            Thread.sleep(4000);
            Waiter.wait(driver).until(ExpectedConditions.attributeToBe(profile.makePrivateToggle(), "value", "false"));
        }
    }
    @Test
    public void PrivacyDefIcon() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.accountTab().click();
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profile.privacyInfoIcon()), "Did not find the Privacy Definition icon");
    }

    @Test
    public void PrivacyPageText() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.accountTab().click();
        Assert.assertTrue(
                PrettyAsserts.isElementDisplayed(profile.privacyDefHeader())

                        && profile.privacyDefSubHeader().isDisplayed()

                        && profile.privacyPublicHeader().isDisplayed()
                        && profile.privacyPublicSubHeader().getText().contains("privacy setting will allow anyone to view")
                        && profile.privacyPublicBold().isDisplayed()
                        && profile.privacyPublicBullet1().isDisplayed()
                        && profile.privacyPublicBullet2().isDisplayed()
                        && profile.privacyPublicBullet3().isDisplayed()
                        && profile.privacyPublicBullet4().isDisplayed()
                        && profile.privacyPublicBullet2().isDisplayed()

                        && profile.privacyPrivateHeader().isDisplayed()
                        && profile.privacyPrivateSubHeader().getText().contains("privacy setting will hide your profile and submissions")
                        && profile.privacyPrivateBold().isDisplayed()
                        && profile.privacyPublicBullet1().isDisplayed()
                        && profile.privacyPublicBullet2().isDisplayed()
                        && profile.privacyPublicBullet3().isDisplayed()
                        && profile.privacyPublicBullet4().isDisplayed(), "PrivacyPageText - text is broken");
    }

    @Test
    public void PrivacyTabToggleText() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.accountTab().click();
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profile.hotnessText()), "Did not find Show me hotness text");
        Assert.assertTrue(PrettyAsserts.isElementDisplayed(profile.privacyToggleText()), "Did not find set profile private text");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void UpdatePassword(){
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.accountTab().click();
        profile.password().sendKeys(config.password);
        profile.passwordVerify().sendKeys(config.password);
        profile.saveProfileBtn().click();
        Assert.assertTrue(PrettyAsserts.isElementEnabled(profile.updateSuccess()), "Did not see success toast after updating password");
    }

    @Test
    public void UpdatePasswordMismatch(){
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.accountTab().click();
        profile.password().sendKeys(Randoms.getRandomString(8));
        profile.passwordVerify().sendKeys(Randoms.getRandomString(9));
        profile.saveProfileBtn().click();
        Assert.assertEquals(profile.passwordError().getText(), "The password confirmation does not match.");
    }

    @Test
    public void UpdateUserName() throws InterruptedException {
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Thread.sleep(4000);
        String ourName = GetInteger.getUsernameFromURL(driver.getCurrentUrl());
        String newName = Randoms.getRandomString(10);
        profile.editProfileBtn().click();
        profile.accountTab().click();
        profile.userName().clear();
        profile.userName().sendKeys(newName);
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();

        try {
            Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.urlContains(newName)));

        } catch (Exception e) {
            System.out.println("UpdateUserName - The username did not update");
            //Teardown, set username back
            profile.editProfileBtn().click();
            profile.accountTab().click();
            profile.userName().clear();
            profile.userName().sendKeys(ourName);
            profile.saveProfileBtn().click();
            Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
            Assert.fail();
        }
        //Teardown, set username back
        profile.editProfileBtn().click();
        profile.accountTab().click();
        profile.userName().clear();
        profile.userName().sendKeys(ourName);
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
