package tests;


import helpers.Drivers;
import helpers.Logins;
import helpers.Waiter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.PageHeaderPage;

@SuppressWarnings("DefaultAnnotationParam")
public class EditProfileTest {

    WebDriver driver = Drivers.ChromeDriver();

    EditProfilePage profile = new EditProfilePage(driver);
    Logins login = new Logins(driver);

    PageHeaderPage header = new PageHeaderPage(driver);
    Actions action = new Actions(driver);

	@BeforeTest
	@Parameters({"unpaidEmail", "password", "url"})
	public void login(@Optional("thechivetest@gmail.com") String unpaidEmail, @Optional("Chive1234") String password, @Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
		driver.get(url);
		login.unpaidLogin(unpaidEmail, password, driver);
		Thread.sleep(2000);
	}

    @BeforeMethod
    @Parameters({"url"})
    public void setDriver(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        profile.userMenu().click();
        profile.settingsBtn().click();
    }

    @AfterClass
    public void TearDown() {
        driver.close();
    }

    //************************** Begin Tests ********************************************

    @Test(enabled = true, priority = 1)
    public void AddMembership() {
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

    @Test(enabled = false, priority = 1)
    public void BannerPicOver4MB() {
        //The image file is there and the happy path update test works, however when I try this too big file it just hangs and the finder window never closes
        WebElement fileInput = profile.bannerEditBtn();
        fileInput.click();
        String filePath = System.getProperty("user.dir") + "/src/images/over4mb.jpg";
        String script = "arguments[0].setAttribute('value', arguments[1]);";
        ((JavascriptExecutor) driver).executeScript(script, fileInput, filePath);
        Assert.assertTrue(profile.uploadFailToast().isDisplayed(), "BannerPicOver4MB failed");
    }

    @Test(enabled = true, priority = 99)
    //run last because the finder window will not close
    public void BannerPicUpdate() {
        WebElement fileInput = profile.bannerEditBtn();
        fileInput.click();
        String filePath = System.getProperty("user.dir") + "/src/images/stock52.jpg";
        String script = "arguments[0].setAttribute('value', arguments[1]);";
        ((JavascriptExecutor) driver).executeScript(script, fileInput, filePath);
        action.sendKeys(Keys.ESCAPE);
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.updateSuccess().isDisplayed(), "BannerPicUpdate - Update Success not found");
    }

    @Test
    public void BirthdayPicker() throws InterruptedException {
        String year = helpers.Randoms.getRandomYear();
        String month = helpers.Randoms.getRandomMonth();
        String day = helpers.Randoms.getRandomDay();
        String birthday = profile.monthToNumber(month) + "/" + helpers.Randoms.formatDay(day) + "/" + year;
        profile.birthDayInput().click();
        profile.birthDayYear(year).click();
        profile.birthDayMonth(month).click();
        Thread.sleep(1000);
        profile.birthDayDay(day).click();
        Thread.sleep(2000);
        Assert.assertEquals(profile.birthDayValue().getAttribute("value"), birthday, "Birthday did not update properly");
    }

    @Test
    public void EmailInputNoEmail() {
        profile.emailInput().click();
        profile.emailInput().sendKeys(Keys.HOME, Keys.SHIFT, Keys.END);
        profile.emailInput().sendKeys(Keys.BACK_SPACE);
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.emailRequired().isDisplayed(), "Did not get the Email required error message");
    }

    @Test(enabled = false)
    public void EmailInputInvalidEmail() {
        //TODO - find a way to find the error popup that displays; i cannot find it in the DOM
        profile.emailInput().clear();
        profile.emailInput().sendKeys("foobar");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.emailInvalid().isDisplayed(), "Did not get the The Email Must Be A Valid Email Address error message");

        profile.emailInput().clear();
        profile.emailInput().sendKeys("thechivetest@gmail");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.emailInvalid().isDisplayed(), "Did not get the The Email Must Be A Valid Email Address error message");
    }

    //TODO - figure out why this does not work in the automation. The gender selection reverts after refreshing, tried several ways and can't make it work
    @Test(enabled = false)
    public void GenderSelect() throws InterruptedException {

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
        System.out.println(profile.genderDropdown().getText() + " is the getText()"+ " and " + profile.genderSelect().getFirstSelectedOption().getText() + " is the getFirstSelected()");
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
    public void HoverAccountTab() throws InterruptedException {
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
    public void HoverEmailTab() throws InterruptedException {
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
    public void HoverMembershipTab() throws InterruptedException {
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
    public void HoverProfileTab() throws InterruptedException {
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
    @Test(enabled = false)//Notifications tab removed
    public void Notifications() throws InterruptedException {

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
        //Enter mismatched passwords
        profile.accountTab().click();
        profile.password().sendKeys("Chive12345");
        profile.passwordVerify().sendKeys("asdf");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.passwordError().isDisplayed(), "Password error text not found");
    }

    @Test
    public void PasswordChangeValid() {

        profile.accountTab().click();
        profile.password().sendKeys("Chive1234");
        profile.passwordVerify().sendKeys("Chive1234");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.updateSuccess().isDisplayed(), "Matching passwords did now save");
    }

    @Test(enabled = false) //TODO - fix image uploading, figure out what to assert to verify
    public void ProfilePicAndBanner() throws InterruptedException {
        profile.bannerInput().sendKeys(System.getProperty("user.dir") + "/src/images/stock52.jpeg");
        Thread.sleep(10000);
        profile.profilePicInput().sendKeys(System.getProperty("user.dir") + "/src/images/stock53.jpeg");
        Assert.assertTrue(profile.profilePic().isDisplayed(), "Profile pic is missing when in the edit screen");
        Assert.assertTrue(profile.bannerPic().isDisplayed(), "Banner pic does not display when in the edit screen");

        profile.updateProfilePicBtn().click();
    }

    @Test
    public void ProfileTabSelectedByDefault() {
        Waiter.wait(driver).until(ExpectedConditions.urlContains("settings"));
        Assert.assertTrue(profile.profileTab().getAttribute("aria-selected").contains("true"), "Profile tab was not selected by default");
    }

    @Test(priority = 99)
    public void RedirectLoggedOutUser() throws InterruptedException {
        Waiter.wait(driver).until(ExpectedConditions.urlContains("settings"));
        login.logout(driver);
        header.menuFollowing().click();
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.urlContains("auth")), "User was not redirected to the login when they tried to access settings while logged out");
    }

    @Test
    @Parameters({"url"})
    public void UpdateUserName(@Optional("https://qa.chivetesting.com/") String url) {
        profile.userMenu().click();
        profile.yourProfileBtn().click();

        Waiter.wait(driver).until(ExpectedConditions.urlMatches("https://qa.chive-testing.com/.+"));

        String ourName = helpers.GetInteger.getUsernameFromURL(driver.getCurrentUrl());
        String newName = helpers.Randoms.getRandomString(10);
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

        } catch (AssertionError e) {
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

    @Test
    public void UpdateFirstName() {

        String nameAfter = helpers.Randoms.getRandomString(10);
        profile.firstNameInput().clear();
        profile.firstNameInput().sendKeys(nameAfter);
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        driver.navigate().refresh();
        Assert.assertEquals(nameAfter, profile.firstNameInput().getAttribute("value"), "New first name did not save");
    }

    @Test
    public void UpdateLastName() {
        String nameAfter = helpers.Randoms.getRandomString(10);
        //System.out.println("nameAfter = " + nameAfter );
        profile.lastNameInput().clear();
        profile.lastNameInput().sendKeys(nameAfter);
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        driver.navigate().refresh();
        Assert.assertEquals(nameAfter, profile.lastNameInput().getAttribute("value"), "New last name did not save");
    }

    @Test
    public void UpdateSuccess() {
        profile.firstNameInput().clear();
        profile.firstNameInput().sendKeys(helpers.Randoms.getRandomString(10));
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.updateSuccess().isDisplayed(), "Did not see the Your profile has been successfully updated! message");
    }

}