package tests;



import helpers.Logins;
import helpers.PageActions;
import helpers.StringHelper;
import helpers.Waiter;
import org.openqa.selenium.interactions.Actions;
import resources.Config;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.BlockUserPage;
import pages.SearchAndFiltersPage;
import resources.RetryAnalyzer;
import resources.TestConfig;

import java.util.List;

import static resources.getDriverType.getDriver;
@Test(retryAnalyzer = RetryAnalyzer.class)
public class BlockUserTest {

    WebDriver driver;
    Actions action;
    SearchAndFiltersPage search;
    Logins login;
    BlockUserPage blocked;

    private static TestConfig config;
    final String SEARCH_TERM ="brady";

    //************************* Setup *************************
    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        action= new Actions(driver);

        search = new SearchAndFiltersPage(driver);
        login = new Logins(driver);
        blocked = new BlockUserPage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, System.getenv("TEST_PWD"));
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void BlockButton() {
        search.searchInput().click();
        search.searchInput().sendKeys(Keys.HOME, Keys.SHIFT, Keys.END);
        search.searchInput().sendKeys(Keys.BACK_SPACE);
        search.searchInput().sendKeys(SEARCH_TERM);
        search.searchInput().sendKeys(Keys.ENTER);
        String userName = search.firstUser().getText().replace("@", "");
        PageActions.scrollDown(driver, 2);
        action.moveToElement(search.firstUser()).click().perform();//fixes click intercepted error
        Waiter.wait(driver).until(ExpectedConditions.urlContains(userName));
        blocked.blockBtn().click();
        try {
            Assert.assertTrue(Waiter.wait(driver).until(ExpectedConditions.attributeContains(blocked.blockBtn().findElement(By.cssSelector("svg")), "class", "fa-check")));
            blocked.blockBtn().click(); //unblock the user inside this try/catch, so we don't end up blocking every user running this test
        } catch (Exception e) { //catch any exception here to undo the block
            blocked.blockBtn().click();
            System.out.println("Block button did not change after click ");
            Assert.fail();
        }
    }

    @Test(enabled = false) //TODO - this test keeps hanging
    public void BlockedUserNotInSearch() {
        search.searchInput().click();
        search.searchInput().sendKeys(Keys.HOME, Keys.SHIFT, Keys.END);
        search.searchInput().sendKeys(Keys.BACK_SPACE);
        search.searchInput().sendKeys(SEARCH_TERM);
        search.searchInput().sendKeys(Keys.ENTER);
        String userName = search.firstUser().getText().replace("@", "");
        PageActions.scrollDown(driver, 2);
        action.moveToElement(search.firstUser()).click().perform();//fixes click intercepted error
        Waiter.wait(driver).until(ExpectedConditions.urlContains(userName));
        //Setup
        String userURL = driver.getCurrentUrl();
        String blockedUser = StringHelper.getIdFromUrl(userURL);
        blocked.blockBtn().click();

        try {
            Waiter.wait(driver).until(ExpectedConditions.attributeContains(blocked.blockBtn().findElement(By.cssSelector("svg")), "class", "fa-check"));
        } catch (Exception e) {
            if (blocked.blockBtn().findElement(By.cssSelector("svg")).getAttribute(blockedUser).contains("fa-check")) {
                blocked.blockBtn().click();
            }
            Assert.fail("Clicking the block button failed");
        }
        //Test if this user is found in the search
        search.searchInput().sendKeys(SEARCH_TERM);
        search.searchInput().sendKeys(Keys.ENTER);
        PageActions.scrollDown(driver, 1);
        List<WebElement> users = search.allUserCards();
        for (WebElement user : users) {
            try {
                Assert.assertNotEquals(blockedUser, user.findElement(By.cssSelector("div[class*='text-lg']")).getText().replace("@", ""));
            } catch (AssertionError e) {
                driver.get(userURL);
                blocked.blockBtn().click();
                Assert.fail("Blocked user " + blockedUser + " found in search results");
            }
        }
        //teardown
        driver.get(userURL);
        blocked.blockBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.attributeContains(blocked.blockBtn().findElement(By.cssSelector("svg")), "class", "fa-ban"));
    }

    //************************* Teardown ***************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
