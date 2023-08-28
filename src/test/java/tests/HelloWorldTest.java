package tests;

import helpers.Config;
import helpers.Drivers;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import resources.TestConfig;


public class HelloWorldTest {

    static WebDriver driver = Drivers.ChromeDriver();
    private static TestConfig config;

    public static WebDriver getDriver() {
        return driver;
    }
    @BeforeTest
    public static void setUp() throws Exception {
       config = Config.getConfig();
    }

    @BeforeClass
    public static void loadPage(){
        driver.get(config.url);
    }
    @BeforeMethod
    public static void refreshPage(){
        driver.get(config.url);
    }

    //***************************Begin Tests************************
    @Test
    public void example() {
        Assert.assertTrue(true);
        System.out.println("Where does this display in Sauce");
    }

    @Test
    public void failTest(){
        Assert.fail("You have fail");
    }

    //********************** Teardown ********************************
    /*@AfterMethod
    public void teardown(ITestResult result) {
        String className = String.valueOf(result.getName().getClass());
        String methodName = result.getMethod().getMethodName();
        String status = result.isSuccess() ? "passed" : "failed";
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "";

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("sauce:context=" + methodName);
        jsExecutor.executeScript("sauce:job-result=" + status);
        jsExecutor.executeScript("sauce:job-name=" + methodName);
        jsExecutor.executeScript("sauce:metadata=Error Message: " + errorMessage);
    }*/

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.close();
    }
}
