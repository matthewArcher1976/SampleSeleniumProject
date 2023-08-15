package helpers;

import java.util.Set;

import org.openqa.selenium.WebDriver;

	public class WindowUtil {
		
		public static void switchToWindow(WebDriver driver, int index) {
        Set<String> handles = driver.getWindowHandles();
        String[] array = handles.toArray(new String[0]);
        driver.switchTo().window(array[index]);
		}
		
}
