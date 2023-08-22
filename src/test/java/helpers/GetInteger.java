package helpers;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GetInteger {

	public static int getIntFromMixedString(String s) {
	    try (Scanner in = new Scanner(s).useDelimiter("[^0-9]+")) {
	        if (in.hasNextInt()) {
	            int integer = in.nextInt();
	            return integer;
	        } else {
	            throw new InputMismatchException("Input string does not contain an integer.");
	        }
	    }
	}
	
	public static String getIntFromMixedStringAsString(String s) {
	    try (Scanner in = new Scanner(s).useDelimiter("[^0-9]+")) {
	        if (in.hasNextInt()) {
	            int integer = in.nextInt();
	            return String.valueOf(integer);
	        } else {
	            throw new InputMismatchException("Input string does not contain an integer.");
	        }
	    }
	}

	public static int intStringToInt(WebElement element) {
		String s =element.getText();
		int i = Integer.parseInt(s);
		return i;
	}
	
	public static String getIdFromUrl(String url) {
	    String[] urlParts = url.split("/");
	    String lastPart = urlParts[urlParts.length - 1];
	    if (lastPart.isEmpty() || lastPart.contains(".")) {
	        lastPart = urlParts[urlParts.length - 2];
	    }
	    return lastPart;
	}

    public static void printElementCSSStyles(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String computedStyles = (String) js.executeScript("var styles = window.getComputedStyle(arguments[0]);" +
                "var result = ''; for (var i = 0; i < styles.length; i++) {" +
                "result += styles[i] + ': ' + styles.getPropertyValue(styles[i]) + ';\\n';} return result;", element);

        System.out.println("CSS Styles:");

        if (computedStyles.isEmpty()) {
            System.out.println("No styles found for the element.");
        } else {
            System.out.println(computedStyles);
        }
    }
    
    public static String getUsernameFromURL(String url) {
        int lastIndex = url.lastIndexOf('/');
        if (lastIndex != -1 && lastIndex < url.length() - 1) {
            return url.substring(lastIndex + 1);
        } else {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }


}