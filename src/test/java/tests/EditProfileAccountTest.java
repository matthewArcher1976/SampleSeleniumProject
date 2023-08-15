package tests;

import helpers.Drivers;
import helpers.Logins;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;
import pages.EditProfilePage;
import pages.PageHeaderPage;

public class EditProfileAccountTest {

    WebDriver driver = Drivers.ChromeDriver();
    EditProfilePage profile = new EditProfilePage(driver);
    Logins login = new Logins(driver);
    PageHeaderPage header = new PageHeaderPage(driver);
    Actions action = new Actions(driver);

    @BeforeTest
    @Parameters({"unpaidEmail", "password", "url"})
    public void login(@Optional("thechivetest@gmail.com") String unpaidEmail, @Optional("Chive1234") String password, @Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
        driver.get(url);
        login.unpaidLogin(unpaidEmail, password, driver);
        Thread.sleep(2000);
    }

    @BeforeMethod
    @Parameters({"url"})
    public void setDriver(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        profile.userMenu().click();
        profile.settingsBtn().click();
    }

    @AfterClass
    public void TearDown() {
        driver.close();
    }

    //************************** Begin Tests ********************************************
    @Test
    public void UserName(){
        profile.userName().sendKeys("a");

    }

}
