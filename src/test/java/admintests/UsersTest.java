package admintests;

import resources.Config;
import helpers.Logins;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.AdminUsersPage;
import resources.TestConfig;

import java.util.List;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class UsersTest {

    WebDriver driver;
    private static TestConfig config;

    AdminUsersPage users;
    Logins login;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);

        users = new AdminUsersPage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        login.unpaidLogin(config.adminEmail, config.password);
        Thread.sleep(1000);
    }

    @BeforeMethod
    public void setDriver() {
        driver.get(config.adminURL);
    }

//************************** Begin Tests ********************************************

    @Test
    public void ActionsDropdownBanUser() throws InterruptedException {

        users.menuExpandUsers().click();
        Thread.sleep(5000);
        users.menuAllUsers().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("tr"), 10));
        String user = helpers.Admin.getUserByRow(driver, users.unbannedUserRow());
        String id = helpers.Admin.getUserIdByUserName(user, driver);
        users.userRowCheckbox(id).click();
        users.actionsDropdownSelect().selectByValue("ban-user");
        users.banReasonInput().sendKeys("being bad");
        users.banConfirmBtn().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("tr"), 10));
        Thread.sleep(3000);//will not work without this
        List<WebElement> checks = helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOfAllElements(users.userCheckOrX(id)));
        try {
            Assert.assertTrue(checks.get(0).getAttribute("class").contains("text-green-500"));
            users.userRowCheckbox(id).click();
            users.actionsDropdownSelect().selectByValue("unban-users");
            users.banConfirmBtn().click();
            Thread.sleep(3000);//needs this too
        } catch (AssertionError e) {
            System.out.println("User does not show as banned in the list");
            users.userRowCheckbox(id).click();
            users.actionsDropdownSelect().selectByValue("unban-users");
            users.banConfirmBtn().click();
            helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(checks.get(0))).getAttribute("class").contains("text-red-500");
            Assert.fail();
        }

    }

    @Test
    public void ActionsDropdownDisplays() throws InterruptedException {
        // Thread.sleep(999999999);
        users.menuExpandUsers().click();
        users.menuAllUsers().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("tr"), 10));
        String user = helpers.Admin.getFirstUser(driver);
        String id = helpers.Admin.getUserIdByUserName(user, driver);
        users.userRowCheckbox(id).click();
        try {
            Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(users.actionsDropdown())).isDisplayed());
        } catch (AssertionError e) {
            System.out.println("Didn't see the actions dropdown");
            Assert.fail();
        }
    }

    @Test
    public void AllUsersPage() {
        users.menuExpandUsers().click();
        users.menuAllUsers().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains("resources/users"));
        try {
            Assert.assertTrue(users.usersTitle().isDisplayed());
        } catch (AssertionError e) {
            System.out.println("AllUsersPage - page did not load");
            Assert.fail();
        }
    }

    @Test
    public void UseriChivePageLink() throws InterruptedException {
        users.menuExpandUsers().click();
        users.menuAllUsers().click();
        helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("tr"), 10));

        String s = helpers.Admin.getFirstUser(driver);
        users.viewUserPage(s).click();
        helpers.WindowUtil.switchToWindow(driver, 1);
        try {
            Assert.assertTrue(driver.getTitle().contains(s)
                    && driver.getCurrentUrl().contains(s));
            driver.close();
            helpers.WindowUtil.switchToWindow(driver, 0);

        } catch (AssertionError e) {
            driver.close();
            helpers.WindowUtil.switchToWindow(driver, 0);
            System.out.println("Users iChive page did not load");
            Assert.fail();
        }
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}