package helpers;

import java.time.Duration;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;


public class Waiter {

	public static FluentWait<WebDriver> longWait(WebDriver driver) {
		return new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(20))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
	}

	public static FluentWait<WebDriver> quickWait(WebDriver driver) {
		return new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
	}

	public static <T> void customWait(WebDriver driver, ExpectedCondition<T> condition) {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(20))
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
		return new FluentWait<>(driver)
	            .withTimeout(Duration.ofSeconds(10))
	            .pollingEvery(Duration.ofMillis(500))
	            .ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
	}

}
