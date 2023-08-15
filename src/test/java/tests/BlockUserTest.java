package tests;

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

import java.util.List;

public class BlockUserTest {


    WebDriver driver = Drivers.ChromeDriver();
    SearchAndFiltersPage search = new SearchAndFiltersPage(driver);
    Logins login = new Logins(driver);
    BlockUserPage blocked = new BlockUserPage(driver);

    @BeforeTest
    @Parameters({"unpaidEmail", "password", "url"})
    public void login(@Optional("thechivetest@gmail.com") String unpaidEmail, @Optional("Chive1234") String password, @Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
        driver.get(url);
        login.unpaidLogin(unpaidEmail, password, driver);
        Thread.sleep(1000);
    }

    @BeforeMethod
    @Parameters({"url"})
    public void setDriver(@Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
        driver.get(url);
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

    @AfterClass
    public void TearDown() {
        driver.close();
    }

    //************************** Begin Tests ********************************************

    @Test
    public void BlockButton() throws InterruptedException {
        blocked.blockBtn().click();
        try {
            Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.attributeContains(blocked.blockBtn().findElement(By.cssSelector("svg")), "class", "fa-check")));
            blocked.blockBtn().click(); //unblock the user inside this try/catch, so we don't end up blocking every user running this test
        } catch (Exception e) { //catch any exception here to undo the block
            blocked.blockBtn().click();
            System.out.println("Block button did not change after click ");
            Thread.sleep(5000000);
            Assert.fail();
        }
    }

    @Test
    public void BlockedUserNotInSearch() {
        //Setup
        String userURL = driver.getCurrentUrl();
        String blockedUser = helpers.GetInteger.getIdFromUrl(userURL);
        blocked.blockBtn().click();

        try {
            helpers.Waiter.wait(driver).until(ExpectedConditions.attributeContains(blocked.blockBtn().findElement(By.cssSelector("svg")), "class", "fa-check"));
        } catch (Exception e) {
            System.out.println("Clicking the block button failed");
            if (blocked.blockBtn().findElement(By.cssSelector("svg")).getAttribute(blockedUser).contains("fa-check")) {
                blocked.blockBtn().click();
            }
            Assert.fail();
        }
        //Test if this user is found in the search
        search.searchInput().sendKeys("x");
        search.searchInput().sendKeys(Keys.ENTER);
        helpers.PageActions.scrollDown(driver, 1);
        List<WebElement> users = search.allUserCards();
        for (WebElement user : users) {
            try {
                Assert.assertFalse(user.findElement(By.cssSelector("div[class*='text-lg']")).getText().replace("@", "").contains(blockedUser));
            } catch (AssertionError e) {
                driver.get(userURL);
                blocked.blockBtn().click();
                System.out.println("Blocked user was found in search results");
                Assert.fail();
            }
        }
        //teardown
        driver.get(userURL);
        blocked.blockBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.attributeContains(blocked.blockBtn().findElement(By.cssSelector("svg")), "class", "fa-ban"));
    }
}
