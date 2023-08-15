package tests;

import helpers.Drivers;
import helpers.Logins;
import helpers.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EditProfilePage;

@SuppressWarnings("DefaultAnnotationParam")
public class MembershipTest {

    WebDriver driver = Drivers.ChromeDriver();
    Actions action = new Actions(driver);
    EditProfilePage profile = new EditProfilePage(driver);
    Logins login = new Logins(driver);

    @BeforeTest
    @Parameters({"unpaidEmail", "password", "url"})
    public void login(@Optional("thechivetest@gmail.com") String unpaidEmail, @Optional("Chive1234") String password, @Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
        driver.get(url);
        login.unpaidLogin(unpaidEmail, password, driver);
        Thread.sleep(1000);
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
    public void ClickMembershipButton() {
        profile.membershipTab().click();
        profile.addMembershipBtn().click();
        Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2)), "Did not find second window on clicking Membership");
        helpers.WindowUtil.switchToWindow(driver, 1);
        Assert.assertTrue(driver.getTitle().contains("Membership")
                && driver.getCurrentUrl().contains("membership")
                && driver.getCurrentUrl().contains("utm_source"));
        driver.close();
        helpers.WindowUtil.switchToWindow(driver, 0);
    }

    @Test(enabled = true, priority = 1)
    public void CreditCardInputs() throws InterruptedException {
        profile.membershipTab().click();
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe")));
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[scrolling='no']")));//yo dawg, we heard you like iframes
        profile.membershipCreditCardsTab().click();
        profile.membershipAddCardBtn().click();
        Thread.sleep(4000);//need it
        Assert.assertTrue(profile.membershipCreditCardExp().isDisplayed() && profile.membershipCreditCardExp().isEnabled(), "CC Exp Input Not found");
    }

    @Test(enabled = true, priority = 1)
    public void CreditCCNumber() {

        profile.membershipTab().click();
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe")));
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[scrolling='no']")));//yo dawg, we heard you like iframes
        profile.membershipCreditCardsTab().click();
        profile.membershipAddCardBtn().click();
        Waiter.wait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[sandbox='allow-scripts allow-same-origin']")));
        Assert.assertTrue(profile.membershipCreditCardNumber().isDisplayed() && profile.membershipCreditCardNumber().isEnabled(), "CC Number Input Not found");
        profile.membershipCreditCardNumber().sendKeys("4111111111111111");
    }

    @Test
    public void HoverAddMembershipButton() throws InterruptedException {
        profile.membershipTab().click();
        Assert.assertEquals(profile.addMembershipBtn().getCssValue("background-color"), "rgba(0, 194, 0, 1)", "Color before hover while active should be rgba(0, 195, 0, 1), found: " + profile.profileTab().getCssValue("color"));
        action.moveToElement(profile.addMembershipBtn()).perform();
        Thread.sleep(2000);//Yes, you get different values if you don't wait
        Assert.assertEquals(profile.addMembershipBtn().getCssValue("background-color"), "rgba(0, 158, 0, 1)", "Color on hover while active should be rgba(0, 158, 0, 1), found: " + profile.profileTab().getCssValue("color"));
    }
}
