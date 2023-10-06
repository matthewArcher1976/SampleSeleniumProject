package tests;

import helpers.Logins;
import helpers.PrettyAsserts;
import helpers.Waiter;
import helpers.WindowUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.AdsPage;
import resources.Config;
import resources.TestConfig;

import static resources.getDriverType.getDriver;
public class AdsTest {
    WebDriver driver;
    AdsPage ads;
    Logins login;

    private static TestConfig config;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        login = new Logins(driver);
        ads = new AdsPage(driver);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
    }

    @BeforeMethod
    public void refresh() {
        driver.get(config.url);
    }

    //************************** Begin Tests ********************************************

    @Test
    public void ClickAdsFrame(){
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(ads.adFrame()));
        ads.adImage().click();
        WindowUtil.switchToWindow(driver, 1);
        try {
            Assert.assertFalse(driver.getCurrentUrl().isEmpty(), "Looks like the ad didn't load");
        }catch (Exception e){
            driver.close();
            WindowUtil.switchToWindow(driver, 0);
             }
        driver.close();
        WindowUtil.switchToWindow(driver, 0);
    }

    @Test
    public void CloseAd() {
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(ads.adFrame()));
        ads.closeAdButton().click();
        Assert.assertTrue(PrettyAsserts.isElementEnabled(ads.sendFeedbackButton()), "User may not be able to hide the ad");
    }

    //************************ Teardown ****************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }


}
