package tests;


import helpers.Config;
import helpers.Drivers;
import helpers.Logins;
import helpers.PageActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import resources.TestConfig;


@Listeners(org.testng.reporters.EmailableReporter.class)
public class EditProfilePrivacyAndEmailsTest {

    WebDriver driver = Drivers.ChromeDriver();
    EditProfilePage profile = new EditProfilePage(driver);
    Logins login = new Logins(driver);
    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public static void configs() throws Exception {
        config = Config.getConfig();
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
    public void EmailHotnessDaily() {
        profile.emailTab().click();
        Boolean onOff = profile.emailHotnessDaily().isSelected();
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
    public void HotnessToggle() throws InterruptedException {
        profile.accountTab().click();
        //  System.out.println(profile.hotnessToggle().getAttribute("value") + " first");
        if (profile.hotnessToggle().getAttribute("value").equals("false")) {
            profile.hotnessToggle().click();
            Thread.sleep(1000);
            //  System.out.println(profile.hotnessToggle().getAttribute("value") + " did you get here");
            try {
                Assert.assertEquals("true", profile.hotnessToggle().getAttribute("value"));
                // System.out.println(profile.hotnessToggle().getAttribute("value"));
            } catch (AssertionError e) {
                System.out.println("Hotness toggle did not switch to on");
                Assert.fail();
            }
        } else if (profile.hotnessToggle().getAttribute("value").equals("true")) {
            // System.out.println(profile.hotnessToggle().getAttribute("value")+ " else if before");
            profile.hotnessToggle().click();

            try {
                //	  System.out.println(profile.hotnessToggle().getAttribute("value")+ " else if");
                Assert.assertEquals("false", profile.hotnessToggle().getAttribute("value"));
            } catch (AssertionError e) {
                System.out.println(profile.hotnessToggle().getAttribute("value"));
                System.out.println("Hotness toggle did not switch to off");
                Assert.fail();
            }
        } else if (!profile.hotnessToggle().getAttribute("value").equals("false") && !profile.hotnessToggle().getAttribute("value").equals("true")) {
            System.out.println("The Hotness toggle may be busted ");
            Assert.fail();
        }
        profile.saveProfileBtn().click();
        try {
            Assert.assertTrue(profile.updateSuccess().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("Success message not displayed after saving hotness toggle");
            Assert.fail();
        }

    }

    @Test
    public void MakePrivateToggle() throws InterruptedException {
        //Can't get it to save the choice without those sleeps in there, tried everything
        profile.accountTab().click();
        //if it's set private when you load the page
        if (profile.makePrivateToggle().getAttribute("value").equals("false")) {
            profile.makePrivateToggle().click();
            Thread.sleep(1000);
            try {
                Assert.assertTrue(profile.makePrivateTitle().isDisplayed());
            } catch (AssertionError e) {
                Assert.fail("Warning popup did not display after toggling make private on");
            }

            profile.makePrivateNvmBtn().click();
            Thread.sleep(2000);

            try {
                Assert.assertEquals("false", profile.makePrivateToggle().getAttribute("value"));
            } catch (AssertionError e) {
                Assert.fail("Make private toggle should not have switched on");
            }

            profile.makePrivateToggle().click();
            profile.makePrivateAcceptBtn().click();
            Thread.sleep(3000);

            profile.saveProfileBtn().click();
            helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
            driver.navigate().refresh();
            profile.accountTab().click();
            Thread.sleep(5000);
            helpers.Waiter.wait(driver).until(ExpectedConditions.attributeContains(profile.makePrivateToggle(), "value", "true"));
            try {
                Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.attributeToBe(profile.makePrivateToggle(), "value", "true")));
                //  Assert.assertEquals("true", profile.makePrivateToggle().getAttribute("value"));
            } catch (AssertionError e) {
                Assert.fail("Make private toggle did not switch to on");
            }
            //if it's not set private when you load the page
        } else if (profile.makePrivateToggle().getAttribute("value").equals("true")) {
            profile.makePrivateToggle().click();
            profile.saveProfileBtn().click();
            helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
            driver.navigate().refresh();
            profile.accountTab().click();
            Thread.sleep(1000);
            try {
                Assert.assertEquals("false", profile.makePrivateToggle().getAttribute("value"));
            } catch (AssertionError e) {
                Assert.fail("Make private toggle did not switch to off");
            }
            profile.makePrivateToggle().click();
            Thread.sleep(1000);
            try {
                Assert.assertTrue(profile.makePrivateTitle().isDisplayed());
            } catch (AssertionError e) {
                Assert.fail("Warning popup did not display after toggling make private on");
            }

            profile.makePrivateNvmBtn().click();
            Thread.sleep(2000);

            try {
                Assert.assertEquals("false", profile.makePrivateToggle().getAttribute("value"));
            } catch (AssertionError e) {
                Assert.fail("Make private toggle should not have switched on");
            }

            profile.makePrivateToggle().click();
            profile.makePrivateAcceptBtn().click();

            Thread.sleep(2000);
            try {
                Assert.assertEquals("true", profile.makePrivateToggle().getAttribute("value"));
            } catch (AssertionError e) {
                Assert.fail("Make private toggle did not switch to on");
            }

        } else if (!profile.makePrivateToggle().getAttribute("value").equals("false") && !profile.makePrivateToggle().getAttribute("value").equals("true")) {
            Assert.fail("Make private toggle may be busted");
        }

        Thread.sleep(2000);
        //Switch privacy back off
        if (profile.makePrivateToggle().getAttribute("value").equals("true")) {

            profile.makePrivateToggle().click();
            profile.saveProfileBtn().click();
            Thread.sleep(4000);
            helpers.Waiter.wait(driver).until(ExpectedConditions.attributeToBe(profile.makePrivateToggle(), "value", "false"));
        }
    }

    @Test
    public void privacyDefIcon() {
        profile.accountTab().click();
        System.out.println(profile.privacyInfoIcon().getText());
        try {
            Assert.assertTrue(profile.privacyInfoIcon().isDisplayed());
        } catch (AssertionError e) {
            System.out.println();
            Assert.fail("privacyDefIcon failed");
        }
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