package tests;

import helpers.Config;
import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.BlockUserPage;
import pages.SearchAndFiltersPage;
import resources.TestConfig;

import java.util.List;

public class BlockUserTest {


    WebDriver driver = Drivers.ChromeDriver();
    SearchAndFiltersPage search = new SearchAndFiltersPage(driver);
    Logins login = new Logins(driver);
    BlockUserPage blocked = new BlockUserPage(driver);

    private static TestConfig config;


    //************************* Setup *************************
    @BeforeTest
    public static void configs() throws Exception {
        config = Config.getConfig();
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }
    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.unpaidEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.url);
        search.searchInput().click();
        search.searchInput().sendKeys(Keys.HOME, Keys.SHIFT, Keys.END);
        search.searchInput().sendKeys(Keys.BACK_SPACE);
        search.searchInput().sendKeys("x");
        search.searchInput().sendKeys(Keys.ENTER);
        String userName = search.firstUser().getText().replace("@", "");
        search.firstUser().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains(userName));
        if (blocked.blockBtn().findElement(By.cssSelector("svg")).getAttribute("class").contains("fa-check")) {
            System.out.println("Blocked user should not display in search results");
            Assert.fail();
        }
    }



    //************************** Begin Tests ********************************************

    @Test
    public void BlockButton() {
        blocked.blockBtn().click();
        try {
            Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.attributeContains(blocked.blockBtn().findElement(By.cssSelector("svg")), "class", "fa-check")));
            blocked.blockBtn().click(); //unblock the user inside this try/catch, so we don't end up blocking every user running this test
        } catch (Exception e) { //catch any exception here to undo the block
            blocked.blockBtn().click();
            System.out.println("Block button did not change after click ");

            Assert.fail();
        }
    }

    @Test
    public void BlockedUserNotInSearch() throws InterruptedException {
        //Setup
        String userURL = driver.getCurrentUrl();
        String blockedUser = helpers.GetInteger.getIdFromUrl(userURL);
        blocked.blockBtn().click();

        try {
            helpers.Waiter.wait(driver).until(ExpectedConditions.attributeContains(blocked.blockBtn().findElement(By.cssSelector("svg")), "class", "fa-check"));
        } catch (Exception e) {
            if (blocked.blockBtn().findElement(By.cssSelector("svg")).getAttribute(blockedUser).contains("fa-check")) {
                blocked.blockBtn().click();
            }
            Assert.fail("Clicking the block button failed");
        }
        //Test if this user is found in the search
        search.searchInput().sendKeys("an");
        search.searchInput().sendKeys(Keys.ENTER);
        helpers.PageActions.scrollDown(driver, 1);
        List<WebElement> users = search.allUserCards();
        for (WebElement user : users) {
            try {
                Assert.assertFalse(user.findElement(By.cssSelector("div[class*='text-lg']")).getText().replace("@", "").contains(blockedUser));
            } catch (AssertionError e) {
                driver.get(userURL);
                blocked.blockBtn().click();
                Thread.sleep(600000);
                Assert.fail("Blocked user " + blockedUser + " found in search results");
            }
        }
        //teardown
        driver.get(userURL);
        blocked.blockBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.attributeContains(blocked.blockBtn().findElement(By.cssSelector("svg")), "class", "fa-ban"));
    }

    //************************* Teardown ***************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
