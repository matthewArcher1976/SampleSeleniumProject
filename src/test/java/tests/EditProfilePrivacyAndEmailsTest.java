package tests;


import helpers.Waiter;
import resources.Config;
import helpers.Logins;
import helpers.PageActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class EditProfilePrivacyAndEmailsTest {

    WebDriver driver;
    private static TestConfig config;

    Actions action;
    EditProfilePage profile;
    Logins login;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);

        action = new Actions(driver);
        profile = new EditProfilePage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
        profile.userMenu().click();
        profile.settingsBtn().click();
    }

    //************************** Begin Tests ********************************************
    @Test
    public void EmailCharities() {
        action.scrollByAmount(0, 300);
        profile.emailTab().click();
        Boolean onOff = profile.emailCharities().isSelected();
        PageActions.scrollDown(driver, 1);
        profile.emailCharities().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        try {
            Assert.assertNotEquals(onOff, profile.emailCharities().isSelected());
        } catch (AssertionError e) {
            System.out.println("EmailCharities - change didn't save");
            Assert.fail();
        }
    }
    @Test
    public void EmailChiveNation() {
        action.scrollByAmount(0, 300);
        profile.emailTab().click();
        Boolean onOff = profile.emailChiveNation().isSelected();
        PageActions.scrollDown(driver, 1);
        profile.emailChiveNation().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        try {
            Assert.assertNotEquals(onOff, profile.emailChiveNation().isSelected());
        } catch (AssertionError e) {
            System.out.println("EmailChiveNation - change didn't save");
            Assert.fail();
        }
    }
    @Test
    public void EmailHotnessDaily() {
        action.scrollByAmount(0, 300);
        profile.emailTab().click();
        Boolean onOff = profile.emailHotnessDaily().isSelected();
        PageActions.scrollDown(driver, 1);
        profile.emailHotnessDaily().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        try {
            Assert.assertNotEquals(onOff, profile.emailHotnessDaily().isSelected());
        } catch (AssertionError e) {
            System.out.println("EmailHotnessDaily - change didn't save");
            Assert.fail();
        }
    }

    @Test
    public void EmailHotnessWeekly() {
        profile.emailTab().click();
        Boolean onOff = profile.emailHotnessWeekly().isSelected();
        profile.emailHotnessWeekly().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        try {
            Assert.assertNotEquals(onOff, profile.emailHotnessWeekly().isSelected());
        } catch (AssertionError e) {
            System.out.println("EmailHotnessWeekly - change didn't save");
            Assert.fail();
        }
    }

    @Test
    public void EmailHumanityDaily() {
        profile.emailTab().click();
        Boolean onOff = profile.emailHumanityDaily().isSelected();
        PageActions.scrollDown(driver, 1);
        profile.emailHumanityDaily().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        try {
            Assert.assertNotEquals(onOff, profile.emailHumanityDaily().isSelected());
        } catch (AssertionError e) {
            System.out.println("EmailHumanityDaily - change didn't save");
            Assert.fail();
        }
    }

    @Test
    public void EmailHumanityWeekly() {
        profile.emailTab().click();
        Boolean onOff = profile.emailHumanityWeekly().isSelected();
        PageActions.scrollDown(driver, 1);
        profile.emailHumanityWeekly().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        try {
            Assert.assertNotEquals(onOff, profile.emailHumanityWeekly().isSelected());
        } catch (AssertionError e) {
            System.out.println("EmailHumanityWeekly - change didn't save");
            Assert.fail();
        }
    }

    @Test
    public void EmailHumorDaily() {
        profile.emailTab().click();
        Boolean onOff = profile.emailHumorDaily().isSelected();
        profile.emailHumorDaily().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        try {
            Assert.assertNotEquals(onOff, profile.emailHumorDaily().isSelected());
        } catch (AssertionError e) {
            System.out.println("EmailHumorDaily - change didn't save");
            Assert.fail();
        }
    }

    @Test
    public void EmailHumorWeekly() {
        profile.emailTab().click();
        Boolean onOff = profile.emailHumorWeekly().isSelected();
        profile.emailHumorWeekly().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        try {
            Assert.assertNotEquals(onOff, profile.emailHumorWeekly().isSelected());
        } catch (AssertionError e) {
            System.out.println("EmailHumorWeekly - change didn't save");
            Assert.fail();
        }
    }

    @Test
    public void EmailNewsletterDaily() {
        profile.emailTab().click();
        Boolean onOff = profile.emailNewsLetterDaily().isSelected();
        profile.emailNewsLetterDaily().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        try {
            Assert.assertNotEquals(onOff, profile.emailNewsLetterDaily().isSelected());
        } catch (AssertionError e) {
            System.out.println("EmailNewsletterDaily - change didn't save");
            Assert.fail();
        }
    }

    @Test
    public void EmailNewsletterWeekly() {
        profile.emailTab().click();
        Boolean onOff = profile.emailNewsLetterWeekly().isSelected();
        profile.emailNewsLetterWeekly().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        try {
            Assert.assertNotEquals(onOff, profile.emailNewsLetterWeekly().isSelected());
        } catch (AssertionError e) {
            System.out.println("EmailNewsletterWeekly - change didn't save");
            Assert.fail();
        }
    }

    @Test
    public void EmailPrefsHeader() {
        profile.emailTab().click();
        try {
            Assert.assertTrue(
                    profile.headerText().isDisplayed()
                            && profile.headerSubText().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("EmailPrefsHeader - Headers doesn't display");
            Assert.fail();
        }

    }

    @Test
    public void EmailPageText() {
        profile.emailTab().click();
        try {
            Assert.assertTrue(
                    profile.emailNewsLetterText().isDisplayed()
                            && profile.emailHumorText().isDisplayed()
                            && profile.emailHotnessText().isDisplayed()
                            && profile.emailHumanityText().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("EmailPageText");
            Assert.fail();
        }
    }

    @Test
    public void EmailWilliamMurray() {
        action.scrollByAmount(0, 300);
        profile.emailTab().click();
        Boolean onOff = profile.emailWilliamMurray().isSelected();
        PageActions.scrollDown(driver, 1);
        profile.emailWilliamMurray().click();
        profile.saveEmailPrefBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.emailTab().click();
        Assert.assertNotEquals(onOff, profile.emailWilliamMurray().isSelected(), "EmailWilliamMurray - change didn't save");
    }

    @Test
    public void HotnessToggle() throws InterruptedException {
        profile.accountTab().click();
        //  System.out.println(profile.hotnessToggle().getAttribute("value") + " first");
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
        Assert.assertTrue(profile.updateSuccess().isDisplayed(), "Success message not displayed after saving hotness toggle");
    }

    @Test
    public void MakePrivateToggle() throws InterruptedException {
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
            Thread.sleep(5000);
            Waiter.wait(driver).until(ExpectedConditions.attributeContains(profile.makePrivateToggle(), "value", "true"));
                Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.attributeToBe(profile.makePrivateToggle(), "value", "true")), "Make private toggle did not switch to on");
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
    public void privacyDefIcon() {
        profile.accountTab().click();
        Assert.assertTrue(profile.privacyInfoIcon().isDisplayed(), "privacyDefIcon failed");
    }

    @Test
    public void PrivacyPageText() {
        profile.accountTab().click();
        Assert.assertTrue(
                profile.privacyDefHeader().isDisplayed()
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
        profile.accountTab().click();
        Assert.assertTrue(profile.hotnessText().isDisplayed(), "Did not find Show me hotness text");
        Assert.assertTrue(profile.privacyToggleText().isDisplayed(), "Did not find set profile private text");
    }
    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}