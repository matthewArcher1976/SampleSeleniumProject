package listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class SauceLabsTestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        // Capture the failure and report it in Sauce Labs
        String testName = result.getName();
        String failureMessage = result.getThrowable().getMessage();
        Reporter.log("Test Failed: " + testName + " - " + failureMessage, true);
    }
}
