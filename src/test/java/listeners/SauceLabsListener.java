package listeners;

import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.IOException;
import java.util.Base64;

public class SauceLabsListener extends TestListenerAdapter {
    private static final String SAUCE_LABS_URL = "https://saucelabs.com/rest/v1/oauth-matt.archer-ff614/jobs/";
    private static final String AUTH_STRING = "Basic " + java.util.Base64.getEncoder().encodeToString("oauth-matt.archer-ff614:48c2e9ca-6c85-470e-a332-588e7e6fde98".getBytes());

    @Override
    public void onTestStart(ITestResult result) {
        log("Test is starting: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logTestStatus(result, "Failed");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logTestStatus(result, "Passed");
    }

    private void logTestStatus(ITestResult result, String status) {
        String methodName = result.getMethod().getMethodName();
        log("Test Case: " + methodName);
        log("Status: " + status);
        String jobId = (String) result.getTestContext().getAttribute("JobId");
        updateSauceLabsStatus(jobId, "Passed".equals(status));
    }

    public static void updateSauceLabsStatus(String jobId, boolean passed) {
        try (StringEntity entity = new StringEntity("{\"passed\": " + passed + "}", ContentType.APPLICATION_JSON)) {
            executeHttpPut(SAUCE_LABS_URL + jobId, entity);
            log("Successfully updated Sauce Labs job status.");
        } catch (IOException e) {
            log("Failed to update Sauce Labs job status: " + e.getMessage());
        }
    }

    private static void executeHttpPut(String url, HttpEntity entity) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut putRequest = new HttpPut(url);
            putRequest.setHeader("Authorization", SauceLabsListener.AUTH_STRING);
            putRequest.setHeader("Content-Type", "application/json");
            putRequest.setEntity(entity); // <--- This line sets the entity for the request
            try (CloseableHttpResponse response = httpClient.execute(putRequest)) {
                handleResponse(response);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void updateSauceTestName(String jobId, String testName) throws IOException {
        String auth = "Basic " + new String(Base64.getEncoder().encode(("oauth-matt.archer-ff614:48c2e9ca-6c85-470e-a332-588e7e6fde98").getBytes()));
        String url = "https://saucelabs.com/rest/v1/username/jobs/" + jobId;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut putRequest = new HttpPut(url);
        putRequest.setHeader("Authorization", auth);
        putRequest.setHeader("Content-Type", "application/json");

        String json = "{\"name\":\"" + testName + "\"}";
        HttpEntity entity = new StringEntity(json);
        putRequest.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(putRequest);
        // Handle the response
    }


    private static void handleResponse(CloseableHttpResponse response) throws IOException, ParseException {
        log("HTTP Status Code: " + response.getCode());
        Header[] headers = response.getHeaders();
        for (Header header : headers) {
            log("Header Name: " + header.getName() + ", Header Value: " + header.getValue());
        }
        if (response.getCode() == HttpStatus.SC_OK) {
            log("HTTP request successful.");
        } else {
            log("HTTP request on handleResponse failed. Response: " + EntityUtils.toString(response.getEntity()));
        }
    }

    private static void log(String message) {
        System.out.println(message);
    }
}
