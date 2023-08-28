package helpers;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class IsSelected {

	public static boolean isIconSelected(WebElement element) {
		String s = element.getAttribute("class");
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


}
