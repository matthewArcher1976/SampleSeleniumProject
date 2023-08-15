package listeners;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import tests.HelloWorldTest;

public class SauceLabsListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult result) {
        logTestFailure(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logTestSuccess(result);
    }

    private void logTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable().getMessage();
        String stackTrace = result.getThrowable().getStackTrace().toString();

        // Log test failure details
        System.out.println("Test Case: " + methodName);
        System.out.println("Status: Failed");
        System.out.println("Error Message: " + errorMessage);
        System.out.println("Stack Trace: " + stackTrace);

        // Set custom job metadata in Sauce Labs
        setSauceMetadata(methodName, "Failed", errorMessage, stackTrace);
    }

    private void logTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();

        // Log test success details
        System.out.println("Test Case: " + methodName);
        System.out.println("Status: Passed");

        // Set custom job metadata in Sauce Labs
        setSauceMetadata(methodName, "Passed", "", "");
    }

    private void setSauceMetadata(String methodName, String status, String errorMessage, String stackTrace) {
        // Get the WebDriver instance from your test class
        WebDriver driver = HelloWorldTest.getDriver(); // Replace with your actual WebDriver instance

        // Execute JavaScript to set custom metadata in Sauce Labs
        ((JavascriptExecutor) driver).executeScript("sauce:context=" + methodName);
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + status);
        ((JavascriptExecutor) driver).executeScript("sauce:job-name=" + methodName);
        ((JavascriptExecutor) driver).executeScript("sauce:metadata=Error Message: " + errorMessage);
        ((JavascriptExecutor) driver).executeScript("sauce:metadata=Stack Trace: " + stackTrace);
    }
}
