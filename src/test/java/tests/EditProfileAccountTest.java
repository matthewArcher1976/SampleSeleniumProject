package tests;

import helpers.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import resources.Config;
import resources.RetryAnalyzer;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

public class EditProfileAccountTest {

    WebDriver driver;
    EditProfilePage editProfilePage;
    Logins login;
    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
        editProfilePage = new EditProfilePage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.defaultEmail, System.getenv("TEST_PWD"));
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void DeleteAccountText() {
        //Actual deletion covered in SignUpTest
        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.accountTab().click();
        editProfilePage.deleteAccountLink().click();
        Assert.assertTrue(driver.getPageSource().contains(" Before you continue, you should know that by deleting your account you will lose access to ALL of your submissions, followers, followees, and all data related to you."));
        editProfilePage.deleteNeverMindBtn().click();
    }

    @Test
    public void HotnessToggle() throws InterruptedException {

        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.accountTab().click();
        if (editProfilePage.hotnessToggle().getAttribute("value").equals("false")) {
            editProfilePage.hotnessToggle().click();
            Thread.sleep(1000);
            Assert.assertEquals("true", editProfilePage.hotnessToggle().getAttribute("value"), "\"Hotness toggle did not switch to on");
        } else if (editProfilePage.hotnessToggle().getAttribute("value").equals("true")) {
            editProfilePage.hotnessToggle().click();
            Assert.assertEquals("false", editProfilePage.hotnessToggle().getAttribute("value"), "Hotness toggle did not switch to off");
        } else if (!editProfilePage.hotnessToggle().getAttribute("value").equals("false") && !editProfilePage.hotnessToggle().getAttribute("value").equals("true")) {
            System.out.println("The Hotness toggle may be busted ");
            Assert.fail();
        }

        editProfilePage.saveProfileBtn().click();
        Thread.sleep(3000);//yeah...
        Assert.assertTrue(PrettyAsserts.isDisplayed(editProfilePage.updateSuccessBy(), driver), "Success message not displayed after saving hotness toggle");

    }

    @Test
    public void MakePrivateToggle() throws InterruptedException {
        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.accountTab().click();
        if (editProfilePage.makePrivateToggle().getAttribute("value").equals("false")) {
            editProfilePage.makePrivateToggle().click();
            Thread.sleep(1000);//yes
            Assert.assertTrue(editProfilePage.makePrivateTitle().isDisplayed(), "Warning popup did not display after toggling make private on");
            editProfilePage.makePrivateNvmBtn().click();
            Thread.sleep(2000);//mhm
            Assert.assertEquals("false", editProfilePage.makePrivateToggle().getAttribute("value"), "Make private toggle should not have switched on");

            editProfilePage.makePrivateToggle().click();
            editProfilePage.makePrivateAcceptBtn().click();
            Thread.sleep(3000);//I know

            editProfilePage.saveProfileBtn().click();
            Waiter.wait(driver).until(ExpectedConditions.visibilityOf(editProfilePage.updateSuccess()));
            driver.navigate().refresh();
            editProfilePage.accountTab().click();
            Thread.sleep(3000);//ok ok
            Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.attributeToBe(editProfilePage.makePrivateToggle(), "value", "true")), "Make private toggle did not switch to on");
            //if it's not set private when you load the page
        } else if (editProfilePage.makePrivateToggle().getAttribute("value").equals("true")) {
            editProfilePage.makePrivateToggle().click();
            editProfilePage.saveProfileBtn().click();
            Waiter.wait(driver).until(ExpectedConditions.visibilityOf(editProfilePage.updateSuccess()));
            driver.navigate().refresh();
            editProfilePage.accountTab().click();
            Thread.sleep(1000);
            Assert.assertEquals("false", editProfilePage.makePrivateToggle().getAttribute("value"), "Make private toggle did not switch to off");
            editProfilePage.makePrivateToggle().click();
            Thread.sleep(1000);
            Assert.assertTrue(editProfilePage.makePrivateTitle().isDisplayed(), "Warning popup did not display after toggling make private on");
            editProfilePage.makePrivateNvmBtn().click();
            Thread.sleep(2000);
            Assert.assertEquals("false", editProfilePage.makePrivateToggle().getAttribute("value"), "Make private toggle should not have switched on");
            editProfilePage.makePrivateToggle().click();
            editProfilePage.makePrivateAcceptBtn().click();
            Thread.sleep(2000);
            Assert.assertEquals("true", editProfilePage.makePrivateToggle().getAttribute("value"), "Make private toggle did not switch to on");

        } else if (!editProfilePage.makePrivateToggle().getAttribute("value").equals("false") && !editProfilePage.makePrivateToggle().getAttribute("value").equals("true")) {
            Assert.fail("Make private toggle may be busted");
        }

        Thread.sleep(2000);
        //Switch privacy back off
        if (editProfilePage.makePrivateToggle().getAttribute("value").equals("true")) {

            editProfilePage.makePrivateToggle().click();
            editProfilePage.saveProfileBtn().click();
            Thread.sleep(4000);
            Waiter.wait(driver).until(ExpectedConditions.attributeToBe(editProfilePage.makePrivateToggle(), "value", "false"));
        }
    }

    @Test
    public void PrivacyDefIcon() throws InterruptedException {
        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.accountTab().click();
        Thread.sleep(3000);//make a new custom expected for this
        Assert.assertTrue(PrettyAsserts.isDisplayed(editProfilePage.privacyInfoIconBy(), driver), "Did not find the Privacy Definition icon");
    }

    //TODO - new test case for privacy page text, get rid of those text elements in the pages file, it's a mess

    @Test
    public void PrivacyTabToggleText() throws InterruptedException {
        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.accountTab().click();
        Thread.sleep(4000);//try then remove
        Assert.assertTrue(driver.getPageSource().contains(editProfilePage.hotnessText()), "Did not find Show me hotness text");
        Assert.assertTrue(driver.getPageSource().contains(editProfilePage.privacyToggleText()), "Did not find set profile private text");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void UpdatePassword() {
        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.accountTab().click();
        editProfilePage.password().sendKeys(System.getenv("TEST_PWD"));
        editProfilePage.passwordVerify().sendKeys(System.getenv("TEST_PWD"));
        editProfilePage.saveProfileBtn().click();
        Assert.assertTrue(PrettyAsserts.isElementEnabled(editProfilePage.updateSuccess()), "Did not see success toast after updating password");
    }

    @Test
    public void UpdatePasswordMismatch() {
        editProfilePage.userMenu().click();
        editProfilePage.settingsBtn().click();
        editProfilePage.accountTab().click();
        editProfilePage.password().sendKeys(Randoms.getRandomString(8));
        editProfilePage.passwordVerify().sendKeys(Randoms.getRandomString(9));
        editProfilePage.saveProfileBtn().click();
        Assert.assertEquals(editProfilePage.passwordError().getText(), "The password confirmation does not match.");
    }

    @Test
    public void UpdateUserName() {
        editProfilePage.userMenu().click();
        editProfilePage.yourProfileBtn().click();
        Waiter.wait(driver).until(CustomExpectedConditions.profileLoaded());
        String ourName = StringHelper.getUsernameFromURL(driver.getCurrentUrl());
        String newName = Randoms.getRandomString(10);
        editProfilePage.editProfileBtn().click();
        editProfilePage.accountTab().click();
        editProfilePage.userName().clear();
        editProfilePage.userName().sendKeys(newName);
        editProfilePage.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(editProfilePage.updateSuccess()));
        editProfilePage.userMenu().click();
        editProfilePage.yourProfileBtn().click();

        try {
            Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.urlContains(newName)));

        } catch (Exception e) {
            System.out.println("UpdateUserName - The username did not update");
            //Teardown, set username back
            editProfilePage.editProfileBtn().click();
            editProfilePage.accountTab().click();
            editProfilePage.userName().clear();
            editProfilePage.userName().sendKeys(ourName);
            editProfilePage.saveProfileBtn().click();
            Waiter.wait(driver).until(ExpectedConditions.visibilityOf(editProfilePage.updateSuccess()));
            Assert.fail();
        }
        //Teardown, set username back
        editProfilePage.editProfileBtn().click();
        editProfilePage.accountTab().click();
        editProfilePage.userName().clear();
        editProfilePage.userName().sendKeys(ourName);
        editProfilePage.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(editProfilePage.updateSuccess()));
    }

    @Test(enabled = false)//TODO - get it working
    public void UpdateUserNameInvalid() throws InterruptedException {
        editProfilePage.userMenu().click();
        editProfilePage.yourProfileBtn().click();
        Waiter.wait(driver).until(CustomExpectedConditions.profileLoaded());
        Thread.sleep(4000);
        editProfilePage.accountTab().click();
        editProfilePage.userName().sendKeys("Test%$");
        editProfilePage.saveProfileBtn().click();
        Assert.assertTrue(PrettyAsserts.isDisplayed(editProfilePage.uploadFailToastBy(), driver));
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
