package tests;

import listeners.SauceLabsListener;
import org.testng.TestNG;

public class TestRunner {
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        testng.addListener(new SauceLabsListener());
        testng.setTestClasses(new Class[]{HelloWorldTest.class});
        testng.run();
    }
}
