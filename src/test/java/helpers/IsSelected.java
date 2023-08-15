package helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class IsSelected {

	public static boolean isIconSelected(WebElement element) {
		WebElement button = element;
		String s = button.getAttribute("class");
		//System.out.println(s + " is the class found in isIconSelected");
		Boolean filled = null;
		if(s.contains("text-white")) {
			filled = false;
		}else if(s.contains("text-primary") && !s.contains("text-white")) {
			filled = true;
		}else {
			System.out.println(s + "isSelected failed");
			Assert.fail();
		}
		return filled;
	}
	
	public static boolean isElementVisible(WebElement element, WebDriver driver) {
	  
	    return element.isDisplayed();
	}

}
