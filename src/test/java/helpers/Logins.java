package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.AdminLoginPage;
import pages.LoginModalPage;

@SuppressWarnings("unused")
public class Logins extends LoginModalPage {
	
	
	public Logins(WebDriver driver) {
		super(driver);
	}
	
	LoginModalPage login = new LoginModalPage(getDriver());
	AdminLoginPage admin = new AdminLoginPage(getDriver());
	
	//************************************ WebElements **************************
	
	public WebElement userMenu () {
		return Waiter.wait(getDriver()).until(ExpectedConditions.presenceOfElementLocated(By.className("avatar")));
	}
	
	//*********************************** Logins ********************************
	
	public void adminLogin(String adminEmail, String adminPassword) throws InterruptedException {
		admin.emailInput().sendKeys(adminEmail);
		admin.passwordInput().sendKeys(adminPassword);
		admin.submitButton().click();
		Thread.sleep(3000);
	}

	public void unpaidLogin(String unpaidEmail, String unpaidPassword) throws InterruptedException {
		login.loginBtn().click();
		login.email().sendKeys(unpaidEmail);
		login.password().sendKeys(unpaidPassword);
		login.signIn().click();
		Thread.sleep(3000);
	}
	
	public void unpaidLoginMobile(String unpaidEmail, String unpaidPassword) throws InterruptedException {
		login.loginBtn().click();
		login.email().sendKeys(unpaidEmail);
		login.password().sendKeys(unpaidPassword);
		login.signIn().click();
		Thread.sleep(2000);
		//driver.findElement(By.cssSelector("img[alt='IChive']")).click();//this should be removed when bug that takes you to profile page is fixed
		Thread.sleep(5000);
	}
	
	public void logout() throws InterruptedException {
		login.userMenu().click();
		Thread.sleep(2000);
		login.signOut().click();
		Thread.sleep(2000);
		
	}
	
	
}
