package tests;

import helpers.PageActions;
import org.openqa.selenium.WebElement;
import pages.PageHeaderPage;
import pages.SubmissionCardsPage;
import resources.Config;
import helpers.Logins;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.ProfilePage;
import resources.RetryAnalyzer;
import resources.TestConfig;

import java.util.List;

import static resources.getDriverType.getDriver;
@Test(retryAnalyzer = RetryAnalyzer.class)
public class ChivettesTest {

    WebDriver driver;
    ProfilePage profilePage;
    EditProfilePage profile;
    Logins login;
    PageHeaderPage header;
    SubmissionCardsPage card;
    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
        card = new SubmissionCardsPage(driver);
        header = new PageHeaderPage(driver);
        profilePage = new ProfilePage(driver);
        profile = new EditProfilePage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.chivetteEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void ChivetteIconTest() {
        profile.userMenu().click();
        profile.yourProfileBtn().click();
        Assert.assertTrue(profilePage.chivetteIcon().isDisplayed(), "Did not find Chivette icon by username");
    }

    @Test//TODO finish this, look for that modal to display and fail the test if it's found, write the unsubscribed user test case first
    public void VerifiedTab(){
        header.menuChivettes().click();
        int cardCount = 0;
        List <WebElement> cards = card.allCards();
        while (cardCount < 40) {
            PageActions.scrollDown(driver, 1);
            cardCount++;
        }
    }
    @Test
    public void WebSiteInputDisplays() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        Assert.assertTrue(profile.websiteInput().isDisplayed(), "Did not find website input");
    }

    @Test
    public void WebSiteLink() {
        profile.userMenu().click();
        profile.settingsBtn().click();
        profile.socialLinksTab().click();
        profile.websiteInput().clear();
        profile.websiteInput().sendKeys("https://www.google.com");
        profile.saveProfileBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
        profile.userMenu().click();
        profile.yourProfileBtn().click();

        Assert.assertTrue(profilePage.websiteIcon().isDisplayed(), "Did not find website icon");

        profilePage.websiteIcon().click();
        helpers.WindowUtil.switchToWindow(driver, 1);

        Assert.assertTrue(driver.getTitle().contains("Google"), "Website tab did not open");

        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    //************************* Teardown ***************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}
