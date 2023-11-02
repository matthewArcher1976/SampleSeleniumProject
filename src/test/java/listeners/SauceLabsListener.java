package listeners;


import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class SauceLabsListener extends TestListenerAdapter {
    private static final String SAUCE_LABS_URL = "https://saucelabs.com/rest/v1/oauth-matt.archer-ff614/jobs/";
    private static final String AUTH_STRING = "Basic " + java.util.Base64.getEncoder().encodeToString("oauth-matt.archer-ff614:48c2e9ca-6c85-470e-a332-588e7e6fde98".getBytes());

    @Override
    public void onTestStart(ITestResult result) {
        log("Test is starting: " + result.getMethod().getMethodName());
    }

    private static void log(String message) {
        System.out.println(message);
    }
}
