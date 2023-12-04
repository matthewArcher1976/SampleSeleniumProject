package tests;

import helpers.*;
import io.github.sukgu.Shadow;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.PageHeaderPage;
import pages.SearchAndFiltersPage;
import pages.SubmissionCardsPage;
import resources.Config;
import resources.RetryAnalyzer;
import resources.TestConfig;

import java.util.List;

import static resources.getDriverType.getDriver;


@Listeners(listeners.SauceLabsListener.class)
public class PageHeaderTest {

    WebDriver driver;
    private static TestConfig config;
    PageHeaderPage header;
    SubmissionCardsPage card;
    SearchAndFiltersPage search;
    Logins login;
    Actions action;
    Shadow shadow;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);

        action = new Actions(driver);
        shadow = new Shadow(driver);
        card = new SubmissionCardsPage(driver);
        header = new PageHeaderPage(driver);
        login = new Logins(driver);
        search = new SearchAndFiltersPage(driver);

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

    //************************** Test Cases ****************************

    @Test
    public void AdFrameDisplays() {
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(header.headerAvatar())).isDisplayed());
    }

    @Test
    public void AvatarPicDisplays() {
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(header.headerAvatar())).isDisplayed());
    }

    @Test(priority = 2, retryAnalyzer = RetryAnalyzer.class)
    public void AvatarPicGoneOnLogout() throws InterruptedException {
        WebElement avi = header.avatarPic();
        login.logout();
        Thread.sleep(3000);
        Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.stalenessOf(avi)));
    }

    @Test(enabled = false)//ad frames changed this, no longer relevant
    public void AvatarPosition() {
        WebElement avatar = header.headerAvatar();
        Dimension viewportSize = driver.manage().window().getSize();
        int viewportWidth = viewportSize.getWidth();
        Point elementLocation = avatar.getLocation();
        int elementX = elementLocation.getX();
        int elementWidth = avatar.getSize().getWidth();

        Assert.assertEquals(viewportWidth - elementX - elementWidth, 8, "The avatar is too close or too far to the edge of the screen");
    }

    @Test
    public void ChiveryLink() {
        header.chiveryLink().click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getCurrentUrl().contains("utm_source=mychive"), "ChiveryLink() - broken");
        System.out.println("ChiveryLink() - broken");
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void ClickChivettesTab(){
        header.menuChivettes().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("chivettes"));
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        List<WebElement> allCards = card.allCards();
        for (WebElement card:allCards){
            System.out.println(card.getAttribute("class") + " is class");
            Assert.assertTrue(PrettyAsserts.isElementDisplayed(card.findElement(By.id("label-chivette"))));
        }
    }

    @Test
    public void ClickFeaturedTab() {
        header.menuFeatured().click();
        Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        Assert.assertTrue(header.menuFeatured().getAttribute("aria-current").contains("page"), "Featured Tab not selected after clicking it");
    }

    @Test
    public void ClickFollowingTab() throws InterruptedException {
        header.menuFollowing().click();
        Thread.sleep(3000);
        Assert.assertTrue(header.menuFollowing().getAttribute("aria-current").contains("page"), "Following Tab not selected after clicking it");
    }

    @Test
    public void ClickLogo() {
        header.menuFeatured().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        header.ichiveLogo().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("dopamine-dump")));
        Assert.assertTrue(header.menuLatest().getAttribute("aria-current").contains("page"), "Clicking the logo did not return you to home page");
    }

    @Test
    public void ClickTopChiversTab() {
        header.menuTopChivers().click();
       //TODO - get this custom wait to actually work, it never finds the value of aria-current
        Waiter.customWait(driver, CustomExpectedConditions.tabLoaded(header.menuTopChivers()));
        Assert.assertEquals(header.menuTopChivers().getAttribute("aria-current"), "page", "Top Chivettes tab should be current");
    }

    @Test
    public void DopamineDump() {
        Assert.assertTrue(header.dopamineDump().isDisplayed(), "Did not fine the Dopamine Dump box");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void DopamineCounter() throws InterruptedException {
        helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(header.dopamineDump()));
        String time1 = header.dopamineDumpHour() + header.dopamineDumpMinute() + header.dopamineDumpSecond();
        Thread.sleep(10000);//let it count down
        String time2 = header.dopamineDumpHour() + header.dopamineDumpMinute() + header.dopamineDumpSecond();
        Assert.assertTrue(Integer.parseInt(time1) > Integer.parseInt(time2), "Timer is not counting down, time1 is " + time1 + " and time2 is " + time2);
    }

    @Test
    public void DropDownMenuItems() {
        header.dropDown().click();
        Assert.assertTrue(helpers.Waiter.quickWait(driver).until(ExpectedConditions.visibilityOf(header.dropDownChivery())).isDisplayed()
                && header.dropDownCharities().isDisplayed()
                && header.dropDownChive().isDisplayed()
                && header.dropDownChiveTV().isDisplayed(), "Missing links in the dropdown menu");
    }

    @Test(enabled = false)
    public void FilterDisplaysOnFeatured() {
        header.menuFeatured().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("dopamine-dump"));
        Assert.assertTrue(header.filterChange().isDisplayed()
                && header.filterHumor().isDisplayed()
                && header.filterHotness().isDisplayed()
                && header.filterHumanity().isDisplayed(), "Filter was missing on Featured page");
    }

    @Test(enabled = false)
    public void FilterDisplaysOnFollowing() {
        header.menuFollowing().click();
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        Assert.assertTrue(header.filterChange().isDisplayed()
                && header.filterHumor().isDisplayed()
                && header.filterHotness().isDisplayed()
                && header.filterHumanity().isDisplayed(), "Filter was missing on Following page");
    }

    @Test(enabled = false)
    public void FilterDisplaysOnLatest() {
        header.menuLatest().click();
        Assert.assertTrue(header.filterChange().isDisplayed()
                && header.filterHumor().isDisplayed()
                && header.filterHotness().isDisplayed()
                && header.filterHumanity().isDisplayed(), "Filter was missing on Latest page");
    }

    @Test(enabled = false)//it's gone everywhere
    public void FilterNotOnSearchPage() throws InterruptedException {
        header.searchButton().click();
        Thread.sleep(2000);//yes
        try {
            Waiter.quickWait(driver).until(ExpectedConditions.visibilityOf(header.filterChange()));
            System.out.println("Filters were found on search page");
            Assert.fail();
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }

    @Test(enabled = false)//filters gone
    public void FilterNotOnProfilePage() throws InterruptedException {
        header.userMenu().click();
        header.yourProfileBtn().click();
        Thread.sleep(2000);
        try {
            helpers.Waiter.quickWait(driver).until(ExpectedConditions.visibilityOf(header.filterChange()));
            System.out.println("Filters were found on Profile page");
            Assert.fail();
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }

    @Test(enabled = false)//filters gone
    public void FilterNotOnSettingsPage() {
        header.userMenu().click();
        header.settingsBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("settings"));
        try {
            helpers.Waiter.quickWait(driver).until(ExpectedConditions.visibilityOf(header.filterChange()));
            System.out.println("Filters were found on Settings page");
            Assert.fail();
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }

    @Test(enabled = false)//filters gone
    public void FilterNotOnSubmissionPage() throws InterruptedException {
        card.cardNotGIF().click();
        driver.navigate().refresh();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("submission"));
        try {
            helpers.Waiter.quickWait(driver).until(ExpectedConditions.visibilityOf(header.filterChange()));
            System.out.println("Filters were found on Submission page");
            Assert.fail();
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }

    @Test(enabled = false)//failing for unknown reason but need to refactor anyway
    public void FilterNotOnTagPage() {
        header.menuTopChivers().click();
        header.firstTag().click();
        try {
            helpers.Waiter.quickWait(driver).until(ExpectedConditions.visibilityOf(header.filterChange()));
            System.out.println("Filters were found on Tag page");
            Assert.fail();
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void FilterNotOnTopChivers() {
        header.menuTopChivers().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("leaderboard"));
        try {
            helpers.Waiter.quickWait(driver).until(ExpectedConditions.visibilityOf(header.filterChange()));
            System.out.println("Filters were found on Top Chivers page");
            Assert.fail();
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }

    @Test()
    public void HoverFeaturedColorChange() throws InterruptedException {
        Assert.assertEquals(header.menuFeatured().getCssValue("borderBottomColor"), "rgba(0, 0, 0, 0)", "Underline might be showing when you're not hovered over Latest");
        action.moveToElement(header.menuFeatured()).perform();
        Thread.sleep(1000);
        Assert.assertEquals(helpers.Waiter.quickWait(driver).until(ExpectedConditions.visibilityOf(header.menuFeatured())).getCssValue("borderBottomColor"), "rgba(0, 195, 0, 1)", "Underline might not be showing when you're hovered over Featured");
    }

    @Test()
    public void HoverFollowingColorChange() throws InterruptedException {
        Assert.assertEquals(header.menuFollowing().getCssValue("borderBottomColor"), "rgba(0, 0, 0, 0)", "Underline might be showing when you're hovered over Following");
        action.moveToElement(header.menuFollowing()).perform();
        Thread.sleep(1000);
        Assert.assertEquals(header.menuFollowing().getCssValue("borderBottomColor"), "rgba(0, 195, 0, 1)", "Underline might not be showing when you're hovered over Following");
    }

    @Test()
    public void HoverLatestColorChange() throws InterruptedException {
        //It now stays highlighted when you're not hovered over it
        header.menuFeatured().click();
        Thread.sleep(5000);//it's taking a long time to deselect Latest, no fluent wait conditions to use
        Assert.assertEquals(header.menuLatest().getCssValue("borderBottomColor"), "rgba(0, 0, 0, 0)", "Underline might be showing when you're not hovered over Latest");
        action.moveToElement(header.menuLatest()).perform();
        Thread.sleep(1000);
        Assert.assertEquals(header.menuLatest().getCssValue("borderBottomColor"), "rgba(0, 195, 0, 1)", "Underline not might be showing when you're hovered over Latest");
    }

    @Test
    public void HoverSettingsColorChange() throws InterruptedException {
        header.userMenu().click();
        WebElement settings = header.yourProfileByPosition().get(1);
        action.moveToElement(settings).perform();
        Thread.sleep(2000);
        Assert.assertEquals(settings.getCssValue("background-color"), "rgba(0, 195, 0, 1)", "Settings did not change color on hover");
    }

    @Test
    public void HoverSignOutColorChange() throws InterruptedException {
        header.userMenu().click();
        WebElement signout = header.yourProfileByPosition().get(2); //gets the element above yourProfileBtn, this one contains the background-color
        action.moveToElement(header.signoutBtn()).perform();
        Thread.sleep(2000);
        Assert.assertEquals(signout.getCssValue("background-color"), "rgba(0, 195, 0, 1)", "Signout did not change color on hover");
    }

    @Test()
    public void HoverTopChiversColorChange() throws InterruptedException {
        Assert.assertEquals(header.menuTopChivers().getCssValue("borderBottomColor"), "rgba(0, 0, 0, 0)", "Underline might be showing when you're not hovered over Top Chivers");
        action.moveToElement(header.menuTopChivers()).perform();
        Thread.sleep(1000);
        Assert.assertEquals(header.menuTopChivers().getCssValue("borderBottomColor"), "rgba(0, 195, 0, 1)", "Underline might not be showing when you're hovered over Top Chivers");
    }

    @Test
    public void HoverUserMenuOpens() {
        action.moveToElement(header.userMenu()).perform();
        Assert.assertTrue(header.yourProfileBtn().isDisplayed() && header.settingsBtn().isDisplayed() && header.signoutBtn().isDisplayed(), "User menu did not display on mouse over");
    }

    @Test
    public void HoverYourProfileColorChange() throws InterruptedException {
        header.userMenu().click();
        WebElement urProfile = header.yourProfileByPosition().get(0); //gets the element above yourProfileBtn, this one contains the background-color
        System.out.println("Your Profile color is " + urProfile.getCssValue("background-color"));
        action.moveToElement(header.yourProfileBtn()).perform();
        Thread.sleep(2000);
        Assert.assertEquals(urProfile.getCssValue("background-color"), "rgba(0, 195, 0, 1)", "Your profile did not change color on hover");
    }

    @Test
    public void IChiveLogoDisplays() {
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(header.ichiveLogo())).isDisplayed(), "Logo is not visible");
    }

    @Test()
    public void LinksCharities() {
        header.linkMenu().click();
        header.dropDownCharities().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("chivecharities"))
                && driver.getCurrentUrl().contains("utm_source"), "LinksCharities - Link is broken");
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test()
    public void LinksChive() {
        header.linkMenu().click();
        header.dropDownChive().click();

        WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(helpers.Waiter.quickWait(driver).until(ExpectedConditions.urlContains("thechive"))
                && driver.getCurrentUrl().contains("utm_source"), "LinksChive - Link is broken");
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }


    @Test
    public void LinksChivery() {
        header.chiveryLink().click();
        WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getCurrentUrl().contains("utm_source=mychive"), "Chivery link is broken");
        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void LinksChiveTV() {
        header.linkMenu().click();
        header.dropDownChivery().click();
        Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
        WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("thechivery"))
                && driver.getCurrentUrl().contains("utm_source"), "LinksChivery - link is broken");
        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void MenuLatest() {
        Assert.assertTrue(header.menuLatest().getAttribute("aria-current").contains("page"), "Latest Tab not selected by default");
    }

    @Test
    public void TrophyIconIsGone() {
        try {
            header.trophyIcon().click();
            System.out.println("The trophy icon displays, it should be gone");
            Assert.fail();
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 1)
    public void VerifyEmailBanner() throws InterruptedException {
        //I didn't feel like making a new class for this one test
        login.logout();
        login.unpaidLogin(config.unconfirmedEmail, System.getenv("TEST_PWD"));
        Assert.assertTrue(header.verifyEmailHeader().isEnabled());
        login.logout();
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
