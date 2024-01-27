package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
@SuppressWarnings("unused")
public class LoginModalPage {

    private WebDriver driver;


    public LoginModalPage(WebDriver driver) {
        this.setDriver(driver);
    }

    public WebDriver getDriver() {
        return driver;
    }

    //this was created by Eclipse
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    //********************** Web Elements ********************************
    public WebElement birthdayInput() {
        return getDriver().findElement(By.id("date-dob"));
    }

    public WebElement closeModel() {
        return getDriver().findElement(By.className("fa-xmark"));
    }

    public WebElement createAccountBtn() {
        return getDriver().findElement(By.xpath("//button[contains(text(), 'Create Account')]"));
    }

    public WebElement userName() {
        return getDriver().findElement(By.id("username"));
    }

    public WebElement emailInput() {
        return getDriver().findElement(By.id("email"));
    }
    public WebElement errorText() {
        return getDriver().findElement(By.className("text-red-500"));
    }

    public WebElement facebookEmail() {
        return getDriver().findElement(By.id("email"));
    }

    public WebElement facebookLoginBtn() {
        return getDriver().findElement(By.id("loginbutton"));
    }

    public WebElement facebookPassword() {
        return getDriver().findElement(By.id("pass"));
    }

    public WebElement forgotPassword() {
        return getDriver().findElement(By.linkText("Forgot your password?"));
    }

    public WebElement googleCreateAccount() {
        return getDriver().findElement(By.xpath("//span[contains(text(), 'Create account')]"));
    }

    public WebElement googleEmailInput() {
        return getDriver().findElement(By.id("identifierId"));
    }

    public WebElement googleEmailNext() {
        return getDriver().findElement(By.id("identifierNext"));
    }

    public WebElement googleFirstName() {
        return getDriver().findElement(By.name("firstName"));
    }

    public WebElement googleForgotEmail() {
        return getDriver().findElement(By.xpath("//button[contains(text(), 'Forgot email?')]"));
    }
    public WebElement googleLastName() {
        return getDriver().findElement(By.name("lastName"));
    }
    public WebElement googleNameNext() {
        return getDriver().findElement(By.id("collectNameNext"));
    }
    public WebElement googlePasswordNext() {
        return getDriver().findElement(By.id("passwordNext"));
    }

    public WebElement googlePasswordInput() {
        return getDriver().findElement(By.cssSelector("input[type='password']"));
    }

    public WebElement invalidUserNameClose() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("Vue-Toastification__close-button")));
    }

    public WebElement loginBtn() {
        return getDriver().findElement(By.className("avatar"));
    }

    public WebElement loginFacebook() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-facebook-f")));
    }

    public WebElement loginGoogle() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-google")));
    }

    public WebElement notificationToast() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("Vue-Toastification__toast-body")));
    }

    public WebElement passwordInput() {
        return getDriver().findElement(By.id("password"));
    }

    public WebElement passwordConfirmInput() {
        return getDriver().findElement(By.id("passwordConfirm"));
    }

    public WebElement resetPasswordEmailMe() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit'][class*='w-full'")));
    }

    public WebElement resetPasswordText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span[class*='text-white']")));
    }

    public WebElement showHotness() {
        return getDriver().findElement(By.id("allow_nsfw"));
    }

    public WebElement signIn() {
        return getDriver().findElement(By.xpath("//button[contains(text(), 'Sign In')]"));
    }

    public WebElement signOut() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Sign out')]")));
    }

    public WebElement signUpTab() {
        return getDriver().findElement(By.xpath("//button[contains(text(), 'Signup')]"));
    }

    public WebElement termsCheckbox() {
        return getDriver().findElement(By.id("terms"));
    }

    public WebElement updateSuccess() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("alert-success")));
    }

    public WebElement userMenu() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("avatar")));
    }

}
