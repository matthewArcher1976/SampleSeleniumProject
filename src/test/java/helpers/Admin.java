package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Admin {
	
	//pulls the numeric user id that the admin panel assigns to every user
	public static String getUserIdByUserName(String linkText, WebDriver driver) throws InterruptedException {
	    WebElement anchorElement = driver.findElement(By.linkText(linkText));
	    WebElement parentTrElement = (WebElement) ((JavascriptExecutor) driver)
	    .executeScript("var element = arguments[0]; "
	    		+ "while (element.tagName.toLowerCase() !== 'tr') "
	    		+ "{ element = element.parentNode; } "
	    		+ "return element;", anchorElement);  
	    String duskValue = parentTrElement.getAttribute("dusk");
	    String numericPart = duskValue.replaceAll("\\D+", "");
	    return numericPart;
	}
	
	//get the username in the first row
	public static String getFirstUser(WebDriver driver) throws InterruptedException {
	   String s = driver.findElement(By.cssSelector("tr[dusk*='-row']")).findElement(By.cssSelector("a[class='link-default']")).getText();
	   return s;
	}
	
	//get the username in the first row
		public static String getUserByRow(WebDriver driver, WebElement row) throws InterruptedException {
		   String s = row.findElement(By.cssSelector("a[class='link-default']")).getText();
		   return s;
		}
}
