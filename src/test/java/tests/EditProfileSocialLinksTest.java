package tests;

import helpers.Waiter;
import helpers.WindowUtil;
import resources.Config;
import helpers.Logins;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.ProfilePage;
import resources.RetryAnalyzer;
import resources.TestConfig;

import static resources.getDriverType.getDriver;

public class EditProfileSocialLinksTest {
    WebDriver driver;
    private static TestConfig config;
    Actions action;
    Logins login;
    EditProfilePage profile;

    ProfilePage profilePage;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        action = new Actions(driver);
        login = new Logins(driver);
        profile = new EditProfilePage(driver);
        profilePage = new ProfilePage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(2000);//yes
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);

    }

    //************************** Begin Tests ********************************************

    @Test
    public void AmazonEditCancel() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.amazonEditBtn().click();
        Assert.assertTrue(profile.amazonEditBtnCancel().isDisplayed(), "AmazonEditCancel - Did not see the edit cancel button");
        profile.amazonEditBtnCancel().click();
        Assert.assertTrue(profile.amazonEditBtn().isDisplayed(), "AmazonEditCancel - Did not see the edit button after clicking cancel");
    }

    @Test

    public void AmazonLink() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.amazonEditBtn().click();
        profile.amazonInput().clear();
        profile.amazonInput().sendKeys("https://amazon.com/registry/wishlist/28NX67QM7I7D3");
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        Assert.assertTrue(profile.amazonInputInactive().isDisplayed(), "Did not the wantfor.me link");

        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));
        Assert.assertTrue(profilePage.amazonLink().isDisplayed(), "Did not fine the Amazon icon");

        profilePage.amazonLink().click();
        WindowUtil.switchToWindow(driver, 1);

        try {
            Assert.assertTrue(Waiter.quickWait(driver).until(ExpectedConditions.titleContains("Amazon.com")) &&
                    Waiter.quickWait(driver).until(ExpectedConditions.urlContains("wishlist")));
        } catch (AssertionError e) {
            System.out.println("Did not find Amazon window");
            driver.close();
            WindowUtil.switchToWindow(driver, 0);
            System.out.println("Did not fine the Amazon icon");
            Assert.fail();
        } catch (TimeoutException e) {
            driver.close();
            WindowUtil.switchToWindow(driver, 0);
            Assert.fail("Did not find Amazon window");
        }
        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test

    public void AmazonLinkBadURL() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.amazonEditBtn().click();
        profile.amazonInput().clear();
        profile.amazonInput().sendKeys("www.google.com");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.amazonInvalid().isDisplayed(), "Did not get the invalid URL error message");
    }

    @Test

//Nothing I do changes when I mouseover the element or not, even though the tooltip is not visible
    public void AmazonLinkHover() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        WebElement toolTip = profile.amazonTooltip();
        System.out.println(toolTip.getCssValue("color"));
        action.moveToElement(toolTip).perform();
        Assert.assertTrue(profile.amazonTooltip().isDisplayed(), "Did not find Amazon tooltip");
    }

    @Test
    public void AmazonLinkShortened() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.amazonEditBtn().click();
        profile.amazonInput().clear();
        profile.amazonInput().sendKeys("https://amazon.com/registry/wishlist/28NX67QM7I7D3");
        profile.saveProfileBtn().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.amazonInputInactive())).isDisplayed(), "Did not find the short link");
    }

    @Test
    public void FaceBookLink() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.facebookInput().clear();
        profile.facebookInput().sendKeys("asdf");
        profile.saveProfileBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));
        Assert.assertTrue(profilePage.facebookLink().isDisplayed(), "Did not find facebook button");
        profilePage.facebookLink().click();
        WindowUtil.switchToWindow(driver, 1);

        Assert.assertTrue(profilePage.facebookLogo().isDisplayed(), "Did not find facebook window");

        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void FaceBookLinkBadURL() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.facebookInput().clear();
        profile.facebookInput().sendKeys("https://www.faceXXXEbook.com/@asdf");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.facebookBadURL().isDisplayed(), "Should have rejected bad Facebook link");
    }

    @Test
    public void FaceBookLinkPartialURL() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.facebookInput().clear();
        profile.facebookInput().sendKeys("facebook.com/asdf");
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));

        Assert.assertTrue(profilePage.facebookLink().isDisplayed(), "Did not find facebook button");

        profilePage.facebookLink().click();
        WindowUtil.switchToWindow(driver, 1);

        Assert.assertTrue(profilePage.facebookLogo().isDisplayed(), "Did not find facebook window");

        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void FaceBookLinkWholeURL() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.facebookInput().clear();
        profile.facebookInput().sendKeys("http://www.facebook.com/asdf");
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));

        Assert.assertTrue(profilePage.facebookLink().isDisplayed(), "Did not find facebook button");

        profilePage.facebookLink().click();
        WindowUtil.switchToWindow(driver, 1);

        Assert.assertTrue(profilePage.facebookLogo().isDisplayed(), "Did not find facebook window");
        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void FaceBookUserHandle() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.facebookInput().clear();
        profile.facebookInput().sendKeys("@asdf");
        profile.saveProfileBtn().click();

        Assert.assertTrue(profile.updateSuccess().isDisplayed(), "FaceBookUserHandle - Should accept @username");
    }

    @Test
    public void InstagramLink() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.instagramInput().clear();
        profile.instagramInput().sendKeys("asdf");
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));
        // Thread.sleep(5000);

        Assert.assertTrue(profilePage.instagramLink().isDisplayed(), "Did not find insta button");

        String instaHandle = profilePage.userGetter(profilePage.instagramLink());
        profilePage.instagramLink().click();
        WindowUtil.switchToWindow(driver, 1);
        Waiter.wait(driver).until(ExpectedConditions.urlContains(instaHandle));

        if (!config.driverType.contains("Sauce")) {//getting error 429 when I try this on sauce
            Assert.assertTrue(driver.getTitle().contains("Instagram")
                    && driver.getTitle().contains(instaHandle), "Did not find Insta window");
        } else {
            Assert.assertTrue(driver.getCurrentUrl().contains("instagram"), "Did not open instagram page");
        }

        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void InstaLinkBadURL() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.instagramInput().clear();
        profile.instagramInput().sendKeys("https://www.instagram.com/qwerty/asdf");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.instagramBadURL().isDisplayed(), "Should have rejected bad Instagram link");
    }

    @Test
    public void InstaLinkWholeURL() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.instagramInput().clear();
        profile.instagramInput().sendKeys("https://www.instagram.com/asdf");
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));

        Assert.assertTrue(profilePage.instagramLink().isDisplayed(), "Did not find insta button");

        String instaHandle = profilePage.userGetter(profilePage.instagramLink());
        profilePage.instagramLink().click();
        WindowUtil.switchToWindow(driver, 1);
        Waiter.wait(driver).until(ExpectedConditions.urlContains(instaHandle));
        if (!config.driverType.contains("Sauce")) {//getting error 429 when I try this on sauce
            Assert.assertTrue(driver.getTitle().contains("Instagram")
                    && driver.getTitle().contains(instaHandle), "Did not find Insta window");
        } else {
            Assert.assertTrue(driver.getCurrentUrl().contains("instagram"), "Did not open instagram page");
        }

        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void InstaLinkUserHandle() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.instagramInput().clear();
        profile.instagramInput().sendKeys("@asdf");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.updateSuccess().isDisplayed(), "InstaLinkUserHandle - Should accept Instagram handle with the @");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void TikTokLink() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.tikTokInput().clear();
        profile.tikTokInput().sendKeys("asdf");
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));

        Assert.assertTrue(profilePage.tiktokLink().isDisplayed(), "Did not find tiktok button");

        String tiktokHandle = profilePage.userGetter(profilePage.tiktokLink());
        profilePage.tiktokLink().click();
        Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
        WindowUtil.switchToWindow(driver, 1);

        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains(tiktokHandle)), "Did not find Tiktok window");

        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void TiktokBadURL() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.tikTokInput().clear();
        profile.tikTokInput().sendKeys("https://www.tiktok.com/qwerty/asdf");
        profile.saveProfileBtn().click();
        Assert.assertTrue(helpers.Waiter.quickWait(driver).until(ExpectedConditions.visibilityOf(profile.tiktokBadURL())).isDisplayed(), "Should have rejected bad Tiktok link");
    }

    @Test
    public void TiktokUserHandle() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.tikTokInput().clear();
        profile.tikTokInput().sendKeys("@asdf");
        profile.saveProfileBtn().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess())).isDisplayed());
    }

    @Test
    public void TikTokLinkWholeURL() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.tikTokInput().clear();
        profile.tikTokInput().sendKeys("https://www.tiktok.com/@asdf");
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));

        Assert.assertTrue(profilePage.tiktokLink().isDisplayed(), "Did not find tiktok button");

        String tiktokHandle = profilePage.userGetter(profilePage.tiktokLink());
        profilePage.tiktokLink().click();
        WindowUtil.switchToWindow(driver, 1);

        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains(tiktokHandle)), "Did not find Tiktok window");

        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void TwitterBadURL() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.twitterInput().clear();
        profile.twitterInput().sendKeys("https://www.twitter.com/qwerty/asdf");
        profile.saveProfileBtn().click();
        Assert.assertTrue(profile.twitterBadURL().isDisplayed(), "Should have rejected bad Twitter link");
    }

    @Test
    public void TwitterLink() throws InterruptedException {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.twitterInput().clear();
        profile.twitterInput().sendKeys("asdf");
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));
        Assert.assertTrue(profilePage.twitterLink().isDisplayed(), "Did not find Twitter icon");

        profilePage.twitterLink().click();
        Thread.sleep(3000);
        WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getTitle().contains("Log in to X / X"), "Did not find Twitter - wait, sorry, 'X' - window, found " + driver.getTitle());//Fuck you Elon

        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void TwitterLinkWholeURL() throws InterruptedException {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.twitterInput().clear();
        profile.twitterInput().sendKeys("https://www.twitter.com/asdf");
        profile.saveProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));
        Assert.assertTrue(profilePage.twitterLink().isDisplayed(), "Did not find Twitter icon");

        profilePage.twitterLink().click();
        Thread.sleep(3000);
        WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getTitle().contains("Log in to X / X"), "Did not find Twitter - wait, sorry, 'X' - window, found " + driver.getTitle());//Fuck you Elon

        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void WebsiteInputNotFound() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        try {
            Assert.assertTrue(profile.websiteInput().isDisplayed());
            Assert.fail();
            System.out.println("Website input should not display for unverified user");
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}

