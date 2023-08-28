package tests;

import helpers.Config;
import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.EditProfilePage;
import resources.TestConfig;

@SuppressWarnings("TestFailedLine")
public class EditProfileAccountTest {

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
        Thread.sleep(2000);
    }
    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
        profile.userMenu().click();
        profile.settingsBtn().click();
    }

    //************************** Begin Tests ********************************************
    @Test
    public void UserName(){
        profile.userName().sendKeys("a");
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }
}
