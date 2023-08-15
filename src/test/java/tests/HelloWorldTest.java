package tests;

import helpers.Drivers;
import listeners.SauceLabsListener;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;


@Listeners(SauceLabsListener.class)

public class HelloWorldTest {

    static WebDriver driver = Drivers.ChromeSauce();

    @AfterMethod
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
    }

    public static WebDriver getDriver() {
        return driver;
    }

    //***************************Begin Tests************************
    @Test
    @Parameters("url")
    public void Test(@Optional("https://qa.chive-testing.com") String url) {
        driver.get(url);
        Assert.assertTrue(true);
        System.out.println("Where does this display in Sauce");
    }

    @Test
    public void failThisTest() {
        Assert.fail();
    }

}
