package tests;

import resources.Config;
import listeners.SauceLabsListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import resources.TestConfig;

import java.io.IOException;
import java.lang.reflect.Method;

import static resources.getDriverType.getDriver;

@Listeners(listeners.SauceLabsListener.class)
public class HelloWorldTest {

    WebDriver driver;
    private static TestConfig config;


    @BeforeTest
    public void setUp(ITestContext context) throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        context.setAttribute("WebDriver", this.driver);
        context.setAttribute("allTestsPassed", true);
    }

    @BeforeClass
    public void loadPage() {
        driver.get(config.url);
    }


    @BeforeMethod
    public void refreshPage(ITestContext testContext, Method method) {
        String jobId = ((RemoteWebDriver) driver).getSessionId().toString();
        testContext.setAttribute("JobId", jobId);

        String testName = method.getName(); // Get the current test name

        try {
            SauceLabsListener.updateSauceTestName(jobId, testName); // Update test name in Sauce Labs
        } catch (IOException e) {
            System.out.println("Failed to update test name in Sauce Labs: " + e.getMessage());
        }

        driver.get(config.url);
    }

    //***************************Begin Tests************************
    @Test
    public void passTest() {
        Assert.assertTrue(true);
        System.out.println("This is the 'example' test case");
    }

    @Test
    public void failTest() {
        Assert.fail();
    }

    //************************** Teardown ********************************************
    @AfterMethod
    public void result(ITestResult result, ITestContext context) {
        if (result.getStatus() != ITestResult.SUCCESS) {
            context.setAttribute("allTestsPassed", false); // Set to false if any test fails
        }
    }

    @AfterClass
    public void TearDown(ITestContext context) {
        String jobId = ((RemoteWebDriver) driver).getSessionId().toString();
        boolean allTestsPassed = (Boolean) context.getAttribute("allTestsPassed");
        SauceLabsListener.updateSauceLabsStatus(jobId, allTestsPassed); // Update Sauce Labs once
        driver.close();
    }
}
