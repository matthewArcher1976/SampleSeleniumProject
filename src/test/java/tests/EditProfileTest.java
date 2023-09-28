package tests;

import helpers.*;
import io.github.sukgu.Shadow;
import pages.ProfilePage;
import resources.Config;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.EditProfilePage;

import resources.TestConfig;

import static resources.getDriverType.getDriver;


public class EditProfileTest {
    private static TestConfig config;
    WebDriver driver;

    Actions action;
    EditProfilePage profile;
    Logins login;
    ProfilePage profilePage;
    Shadow shadow;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        shadow = new Shadow(driver);
        action = new Actions(driver);
        login = new Logins(driver);
        profile = new EditProfilePage(driver);
        profilePage = new ProfilePage(driver);

    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);

    }

    //************************** Begin Tests ********************************************

    @Test
    public void AddMembership() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.membershipTab().click();
        profile.addMembershipBtn().click();
        Waiter.quickWait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
        helpers.WindowUtil.switchToWindow(driver, 1);
        try {
            Assert.assertTrue(driver.getCurrentUrl().contains("membership"));
            driver.close();
            helpers.WindowUtil.switchToWindow(driver, 0);
        } catch (AssertionError e) {
            System.out.println("Did not find Membership page");
            driver.close();
            helpers.WindowUtil.switchToWindow(driver, 0);
            Assert.fail();
        }
    }

    @Test(enabled = false)
    public void BannerPicOver4MB() {
        //The image file is there and the happy path update test works, however when I try this too big file it just hangs and the finder window never closes
        WebElement fileInput = profile.bannerEditBtn();
        fileInput.click();
        String filePath = System.getProperty("user.dir") + "/src/images/over4mb.jpg";
        String script = "arguments[0].setAttribute('value', arguments[1]);";
        ((JavascriptExecutor) driver).executeScript(script, fileInput, filePath);
        Assert.assertTrue(profile.uploadFailToast().isDisplayed(), "BannerPicOver4MB failed");
    }

    @Test(priority = 99)
    //run last because the finder window will not close
    public void BannerPicUpdate() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        WebElement fileInput = profile.bannerEditBtn();
        fileInput.click();
        String filePath = System.getProperty("user.dir") + "/src/images/stock52.jpg";
        String script = "arguments[0].setAttribute('value', arguments[1]);";
        ((JavascriptExecutor) driver).executeScript(script, fileInput, filePath);
        action.sendKeys(Keys.ESCAPE);
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.updateSuccess().isDisplayed(), "BannerPicUpdate - Update Success not found");
    }

    @Test()//this does not work because the date is in the shadow DOM which does not display in the Selenium browser. Can try with a js executor?
    public void BirthdayPicker() throws InterruptedException {
        profile.userMenu().click();
        profile.settingsBtn().click();

        String initialBirthday = profile.birthDayInput().getText();
        System.out.println(initialBirthday + " is initial birthday");

        String year = Randoms.getRandomYear();
        String month = Randoms.getRandomMonth();
        String day = Randoms.getRandomDay();
        String birthday = profile.monthToNumber(month) + helpers.Randoms.formatDay(day) + year;
        System.out.println(birthday + " is birthday");
        profile.birthDayInput().click();
        profile.birthDayInput().sendKeys(birthday);
        profile.saveProfileBtn().click();
        Thread.sleep(3000);
        Assert.assertEquals(GetInteger.dateToMMddyyyy(profile.birthDayInput().getAttribute("value")), birthday, "Birthday did not update properly");
    }

    @Test
    public void CMGLink() throws InterruptedException {
        profile.userMenu().click();
        profile.settingsBtn().click();
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Thread.sleep(2000);
        profilePage.cmgLink().click();
        Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getCurrentUrl().contains("https://www.chivemediagroup.com/?utm_source=ichive"), "CMG Link broken");
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void EmailInputNoEmail() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.emailInput().click();
        profile.emailInput().sendKeys(Keys.HOME, Keys.SHIFT, Keys.END);
        profile.emailInput().sendKeys(Keys.BACK_SPACE);
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.emailRequired().isDisplayed(), "Did not get the Email required error message");
    }

    @Test(enabled = false)
    public void EmailInputInvalidEmail() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        //TODO - find a way to find the error popup that displays; I cannot find it in the DOM
        profile.emailInput().clear();
        profile.emailInput().sendKeys("foobar");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.emailInvalid().isDisplayed(), "Did not get the The Email Must Be A Valid Email Address error message");

        profile.emailInput().clear();
        profile.emailInput().sendKeys(config.unpaidEmail);
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.emailInvalid().isDisplayed(), "Did not get the The Email Must Be A Valid Email Address error message");
    }

    //TODO - figure out why this does not work in the automation. The gender selection reverts after refreshing, tried several ways and can't make it work
    @Test(enabled = false)
    public void GenderSelect() throws InterruptedException {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.genderDropdown().click();
        profile.genderMale().click();
        profile.emailInput().click();//maybe click outside the gender dropdown?
        Thread.sleep(4000);
        profile.saveProfileBtn().click();
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.settingsBtn().click();
        Thread.sleep(2000);
        System.out.println(profile.genderDropdown().getText() + " is the getText()" + " and " + profile.genderSelect().getFirstSelectedOption().getText() + " is the getFirstSelected()");
        Assert.assertEquals(profile.genderSelect().getFirstSelectedOption().getText(), "Male", "Male should be selected");

        profile.genderDropdown().click();
        profile.genderSelect().selectByValue("female");
        profile.saveProfileBtn().click();
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        Thread.sleep(5000);
        profile.userMenu().click();
        profile.settingsBtn().click();
        Thread.sleep(5000);
        System.out.println(profile.genderSelect().getFirstSelectedOption().getText() + " female actual");

        Assert.assertEquals(profile.genderSelect().getFirstSelectedOption().getText(), "Female", "Female should be selected");

        profile.genderDropdown().click();
        profile.genderSelect().selectByValue("other");
        profile.saveProfileBtn().click();
        Thread.sleep(4000);
        driver.navigate().refresh();
        Assert.assertEquals(profile.genderDropdown().getText(), "Other", "Other should be selected");
    }

    @Test
    public void HoverAccountTab() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        //Inactive
        Assert.assertEquals(profile.accountTab().getCssValue("color"), "rgba(207, 207, 207, 1)", "Color before hover while active should be rgba(0, 195, 0, 1), found: " + profile.accountTab().getCssValue("color"));
        action.moveToElement(profile.accountTab()).perform();
        Assert.assertEquals(profile.accountTab().getCssValue("color"), "rgba(0, 195, 0, 1)", "Color on hover while active should be rgba(0, 158, 0, 1), found: " + profile.accountTab().getCssValue("color"));
        //Active
        profile.accountTab().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("account"));
        action.moveToElement(profile.membershipTab()).perform();//need to pull the cursor off the account tab from before
        Assert.assertEquals(profile.accountTab().getCssValue("color"), "rgba(0, 195, 0, 1)", "Color on hover while inactive should be rgba(0, 195, 0, 1), found: " + profile.accountTab().getCssValue("color"));
        action.moveToElement(profile.accountTab()).perform();
        Assert.assertEquals(profile.accountTab().getCssValue("color"), "rgba(0, 158, 0, 1)", "Color on hover while inactive should be rgba(0, 158, 0, 1), found: " + profile.accountTab().getCssValue("color"));
    }

    @Test
    public void HoverEmailTab() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        //Inactive
        Assert.assertEquals(profile.emailTab().getCssValue("color"), "rgba(207, 207, 207, 1)", "Color before hover while active should be rgba(0, 195, 0, 1), found: " + profile.emailTab().getCssValue("color"));
        action.moveToElement(profile.emailTab()).perform();
        Assert.assertEquals(profile.emailTab().getCssValue("color"), "rgba(0, 195, 0, 1)", "Color on hover while active should be rgba(0, 158, 0, 1), found: " + profile.emailTab().getCssValue("color"));
        //Active
        profile.emailTab().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("email-preferences"));
        action.moveToElement(profile.membershipTab()).perform();//need to pull the cursor off the membership tab from before
        Assert.assertEquals(profile.emailTab().getCssValue("color"), "rgba(0, 195, 0, 1)", "Color on hover while inactive should be rgba(0, 195, 0, 1), found: " + profile.emailTab().getCssValue("color"));
        action.moveToElement(profile.emailTab()).perform();
        Assert.assertEquals(profile.emailTab().getCssValue("color"), "rgba(0, 158, 0, 1)", "Color on hover while inactive should be rgba(0, 158, 0, 1), found: " + profile.emailTab().getCssValue("color"));
    }

    @Test
    public void HoverMembershipTab() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        //Inactive
        Assert.assertEquals(profile.membershipTab().getCssValue("color"), "rgba(207, 207, 207, 1)", "Color before hover while active should be rgba(0, 195, 0, 1), found: " + profile.membershipTab().getCssValue("color"));
        action.moveToElement(profile.membershipTab()).perform();
        Assert.assertEquals(profile.membershipTab().getCssValue("color"), "rgba(0, 195, 0, 1)", "Color on hover while active should be rgba(0, 158, 0, 1), found: " + profile.membershipTab().getCssValue("color"));
        //Active
        profile.membershipTab().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("membership"));
        action.moveToElement(profile.profileTab()).perform();//need to pull the cursor off the membership tab from before
        Assert.assertEquals(profile.membershipTab().getCssValue("color"), "rgba(0, 195, 0, 1)", "Color on hover while inactive should be rgba(0, 195, 0, 1), found: " + profile.membershipTab().getCssValue("color"));
        action.moveToElement(profile.membershipTab()).perform();
        Assert.assertEquals(profile.membershipTab().getCssValue("color"), "rgba(0, 158, 0, 1)", "Color on hover while inactive should be rgba(0, 158, 0, 1), found: " + profile.membershipTab().getCssValue("color"));
    }

    @Test
    public void HoverProfileTab() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        //Active
        Assert.assertEquals(profile.profileTab().getCssValue("color"), "rgba(0, 195, 0, 1)", "Color before hover while active should be rgba(0, 195, 0, 1), found: " + profile.profileTab().getCssValue("color"));
        action.moveToElement(profile.profileTab()).perform();
        Assert.assertEquals(profile.profileTab().getCssValue("color"), "rgba(0, 158, 0, 1)", "Color on hover while active should be rgba(0, 158, 0, 1), found: " + profile.profileTab().getCssValue("color"));
        //Inactive
        profile.socialLinksTab().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("social"));
        Assert.assertEquals(profile.profileTab().getCssValue("color"), "rgba(207, 207, 207, 1)", "Color on hover while inactive should be rgba(207, 207, 207, 1), found: " + profile.profileTab().getCssValue("color"));
        action.moveToElement(profile.profileTab()).perform();
        Assert.assertEquals(profile.profileTab().getCssValue("color"), "rgba(0, 195, 0, 1)", "Color on hover while inactive should be rgba(0, 158, 0, 1), found: " + profile.profileTab().getCssValue("color"));
    }

    @Test
    public void HoverSocialTab() throws InterruptedException {
        profile.userMenu().click();
        profile.settingsBtn().click();
        //Inactive
        Assert.assertEquals(profile.socialLinksTab().getCssValue("color"), "rgba(207, 207, 207, 1)", "Color on hover while inactive should be rgba(207, 207, 207, 1), found: " + profile.socialLinksTab().getCssValue("color"));
        action.moveToElement(profile.socialLinksTab()).perform();
        Assert.assertEquals(profile.socialLinksTab().getCssValue("color"), "rgba(0, 195, 0, 1)", "Color on hover while inactive should be rgba(0, 158, 0, 1), found: " + profile.socialLinksTab().getCssValue("color"));
        //Active
        driver.navigate().refresh();
        profile.socialLinksTab().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("profile"));
        action.moveToElement(profile.profileTab()).perform();//need to pull the cursor off the social tab from before

        Thread.sleep(2000);//yes
        Assert.assertEquals(profile.socialLinksTab().getCssValue("color"), "rgba(0, 195, 0, 1)", "Color before hover while active should be rgba(0, 195, 0, 1), found: " + profile.socialLinksTab().getCssValue("color"));
        action.moveToElement(profile.socialLinksTab()).perform();
        Assert.assertEquals(profile.socialLinksTab().getCssValue("color"), "rgba(0, 158, 0, 1)", "Color on hover while active should be rgba(0, 158, 0, 1), found: " + profile.socialLinksTab().getCssValue("color"));
    }

    @Test(enabled = false)//Notifications tab removed, may be reintroduced
    public void Notifications() throws InterruptedException {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.notificationsTab().click();

        if (profile.notificationsToggle().getAttribute("value").equals("false")) {
            profile.notificationsToggle().click();
            Thread.sleep(2000);
            Assert.assertEquals("true", profile.notificationsToggle().getAttribute("value"), "Notifications toggle did not switch to on");

        } else if (profile.notificationsToggle().getAttribute("value").equals("true")) {
            profile.notificationsToggle().click();
            Thread.sleep(2000);
            Assert.assertEquals("false", profile.notificationsToggle().getAttribute("value"), "Notifications toggle did not switch to off");

        } else if (!profile.notificationsToggle().getAttribute("value").equals("false") && !profile.notificationsToggle().getAttribute("value").equals("true")) {
            System.out.println("The notifications toggle may be broken");
            Assert.fail();
        }
    }

    @Test
    public void PasswordChangeMismatch() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        //Enter mismatched passwords
        profile.accountTab().click();
        profile.password().sendKeys(config.password);
        profile.passwordVerify().sendKeys("asdf");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.passwordError().isDisplayed(), "Password error text not found");
    }

    @Test
    public void PasswordChangeValid() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.accountTab().click();
        profile.password().sendKeys("Chive1234");
        profile.passwordVerify().sendKeys("Chive1234");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.updateSuccess().isDisplayed(), "Matching passwords did now save");
    }

    @Test(enabled = false) //TODO - fix image uploading, figure out what to assert to verify
    public void ProfilePicAndBanner() throws InterruptedException {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.bannerInput().sendKeys(System.getProperty("user.dir") + "/src/images/stock52.jpeg");
        Thread.sleep(10000);
        profile.profilePicInput().sendKeys(System.getProperty("user.dir") + "/src/images/stock53.jpeg");
        Assert.assertTrue(profile.profilePic().isDisplayed(), "Profile pic is missing when in the edit screen");
        Assert.assertTrue(profile.bannerPic().isDisplayed(), "Banner pic does not display when in the edit screen");

        profile.updateProfilePicBtn().click();
    }

    @Test
    public void ProfileTabSelectedByDefault() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("settings"));
        Assert.assertTrue(profile.profileTab().getAttribute("aria-selected").contains("true"), "Profile tab was not selected by default");
    }

    @Test
    public void UpdateFirstName() {

        String nameAfter = helpers.Randoms.getRandomString(10);
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.firstNameInput().clear();
        profile.firstNameInput().sendKeys(nameAfter);
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        driver.navigate().refresh();
        Assert.assertEquals(nameAfter, profile.firstNameInput().getAttribute("value"), "New first name did not save");
    }

    @Test
    public void UpdateLastName() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        String nameAfter = helpers.Randoms.getRandomString(10);
        profile.lastNameInput().clear();
        profile.lastNameInput().sendKeys(nameAfter);
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        driver.navigate().refresh();
        Assert.assertEquals(nameAfter, profile.lastNameInput().getAttribute("value"), "New last name did not save");
    }

    @Test
    public void UpdateSuccess() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.firstNameInput().clear();
        profile.firstNameInput().sendKeys(helpers.Randoms.getRandomString(10));
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.updateSuccess().isDisplayed(), "Did not see the Your profile has been successfully updated! message");
    }
    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.close();
    }
}
