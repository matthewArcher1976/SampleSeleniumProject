package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.LoginModalPage;
import pages.PageHeaderPage;

public class Logins extends LoginModalPage {

	public Logins(WebDriver driver) {
		super(driver);
	}
	
	LoginModalPage login = new LoginModalPage(getDriver());
	PageHeaderPage header = new PageHeaderPage(getDriver());
	
	//************************************ WebElements **************************
	
	public WebElement userMenu () {
		return Waiter.wait(getDriver()).until(ExpectedConditions.presenceOfElementLocated(By.className("avatar")));
	}
	
	//*********************************** Logins ********************************

	public void unpaidLogin(String unpaidEmail, String unpaidPassword) throws InterruptedException {
		login.loginBtn().click();
		login.emailInput().sendKeys(unpaidEmail);
		login.passwordInput().sendKeys(unpaidPassword);
		login.signIn().click();
		Thread.sleep(3000);//for now
	}

	public void logout(){
		login.userMenu().click();
		login.signOut().click();
		Waiter.wait(getDriver()).until(ExpectedConditions.visibilityOf(header.loginSuccess()));
	}
	
	
}
