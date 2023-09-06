package resources;

import listeners.SauceLabsListener;
import org.reflections.Reflections;
import org.testng.TestNG;

import java.util.Set;

public class TestRunner {
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        testng.addListener(new SauceLabsListener());

        // Use Reflections to dynamically find all classes in the 'tests' package
        Reflections reflections = new Reflections("tests");
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

        testng.setTestClasses(allClasses.toArray(new Class[0]));
        testng.run();
    }
}
