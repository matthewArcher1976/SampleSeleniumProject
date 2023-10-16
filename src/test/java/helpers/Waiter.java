package helpers;

import java.time.Duration;
import java.util.function.BooleanSupplier;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;


public class Waiter {
	public static FluentWait<WebDriver> longWait(WebDriver driver) {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(20))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		return wait;
	}
	public static FluentWait<WebDriver> quickWait(WebDriver driver) {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(5))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		return wait;
	}

	public static <T> void customWait(WebDriver driver, ExpectedCondition<T> condition) {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(10))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class)
				.withMessage("Timeout waiting for custom condition to be satisfied");

		wait.until(condition);
	}

	public static void waitForCSSValue(WebDriver driver, WebElement element, String property, String value) {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(10))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);

		wait.until(d -> element.getCssValue(property).equals(value));
	}

	public static FluentWait<WebDriver> wait(WebDriver driver) {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
	            .withTimeout(Duration.ofSeconds(10))
	            .pollingEvery(Duration.ofMillis(500))
	            .ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		return wait;
	}
	
	public static void waitUntilBooleanMethodReturns(WebDriver driver, final BooleanSupplier booleanMethod, final boolean expectedValue) {
	    // Define the FluentWait
	    FluentWait<WebDriver> wait = new FluentWait<>(driver)
	            .withTimeout(Duration.ofSeconds(20))
	            .pollingEvery(Duration.ofMillis(500))
	            .ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);

	    // Wait for the boolean method to return the expected value
	    wait.until(driver1 -> booleanMethod.getAsBoolean() == expectedValue);
	}




}
