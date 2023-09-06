package helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.time.Duration;
import java.util.Arrays;

@SuppressWarnings("unused")
public class PageActions {
	
	public static void hitEscape(WebDriver driver) {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).build().perform();
	}//for in pages classes, may be a better way

	public static void findElementWithScrolling(WebDriver driver, By by) throws InterruptedException {
	    boolean isElementFound = false;
	    WebElement element = null;
	    int tries = 0; 
	    while(!isElementFound && tries < 4) { 
	        Thread.sleep(2000);
	        try {
	            element = driver.findElement(by);
	            isElementFound = true;
	        } catch (NoSuchElementException e) {
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            js.executeScript("window.scrollBy(0, 500)");
	        }
	        tries++; 
	    }
	}
	
	public static WebElement findElementWithScrollingElement(WebDriver driver, WebElement elementToFind) throws InterruptedException {
	    boolean isElementFound = false;
	    WebElement element = null;
	    int tries = 0; 
	    while(!isElementFound) {
	        Thread.sleep(2000);
	        try {
	            element = elementToFind;
	            isElementFound = true;
	        } catch (NoSuchElementException e) {
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            js.executeScript("window.scrollBy(0, 500)");
	        }
	        tries++; 
	    }
	    return element;
	}
	public static void multiClicker(WebDriver driver, WebElement element, int maxTries){
		int tries = 0;
		while (tries <= maxTries) {
			try {
				helpers.Waiter.quickWait(driver).until(ExpectedConditions.elementToBeClickable(element)).click();
				break;
			} catch (TimeoutException e) {
				tries++;
				if (tries > maxTries) {
					Assert.fail("Tried " + tries + " to click " + element.getAttribute("class") + ", giving up");
				}
			}
		}
	}
	public static void scrollDown(WebDriver driver, int times) {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    for (int i = 0; i < times; i++) {
	        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	        try {
	            Thread.sleep(2000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public static void touchScroll(WebDriver driver, int startX, int startY, int endX, int endY, int times) {
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		for (int i = 0; i < times; i++) {
			Sequence scrollDown = new Sequence(finger, 1);
			scrollDown.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
			scrollDown.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
			scrollDown.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), endX, endY));
			scrollDown.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

			((Interactive) driver).perform(Arrays.asList(scrollDown));
		}
	}

	public static void swipeDown(WebDriver driver, int times) {
		Dimension d = driver.manage().window().getSize();
		int x = d.getWidth() / 2;
		int startY = (int)(d.getHeight() * .20);
		int endY = (int)(d.getHeight() * 0.8);

		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		for (int i = 0; i < times; i++) {
			Sequence scrollDown = new Sequence(finger, 1);
			scrollDown.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, startY));
			scrollDown.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
			scrollDown.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), x, endY));
			scrollDown.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
			((Interactive) driver).perform(Arrays.asList(scrollDown));
		}
	}

	public static void swipeUp(WebDriver driver, int times) {
		Dimension d = driver.manage().window().getSize();
		int x = d.getWidth()/2;
		int startY = (int)(d.getHeight() * .80);
		int endY = (int)(startY - d.getHeight() * 0.20);
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		for (int i = 0; i < times; i++) {
			Sequence scrollDown = new Sequence(finger, 1);
			scrollDown.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, startY));
			scrollDown.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
			scrollDown.addAction(finger.createPointerMove(Duration.ofMillis(100), PointerInput.Origin.viewport(), x, endY));
			scrollDown.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
			((Interactive) driver).perform(Arrays.asList(scrollDown));
		}
	}
	public static void swipeLeft(WebDriver driver, int times) {
		Dimension d = driver.manage().window().getSize();
		int y = d.getHeight()/2;
		int startX = (int)(d.getWidth() * .80);
		int endX = (int)(startX + d.getWidth() * 0.2);
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

		for (int i = 0; i < times; i++) {
			Sequence scrollDown = new Sequence(finger, 1);
			scrollDown.addAction(finger.createPointerMove(Duration.ofMillis(200), PointerInput.Origin.viewport(), startX, y));
			scrollDown.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
			scrollDown.addAction(finger.createPointerMove(Duration.ofMillis(2000), PointerInput.Origin.viewport(), startX, y));
			scrollDown.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
			((Interactive) driver).perform(Arrays.asList(scrollDown));
		}
	}
	public static void swipeRight(WebDriver driver, int times) {
		Dimension d = driver.manage().window().getSize();
		int y = d.getHeight()/2;
		int startX = d.getWidth()/2;
		int endX = (int)(startX - d.getWidth() * 0.4);
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		for (int i = 0; i < times; i++) {
			Sequence scrollDown = new Sequence(finger, 1);
			scrollDown.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, y));
			scrollDown.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
			scrollDown.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), startX, y));
			scrollDown.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
			((Interactive) driver).perform(Arrays.asList(scrollDown));
		}
	}

}
