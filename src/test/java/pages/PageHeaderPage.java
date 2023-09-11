package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings("unused")
public class PageHeaderPage {

    WebDriver driver;

    public PageHeaderPage(WebDriver driver) {
        this.driver = driver;
    }

    //*********************** WebElements *******************************

    public WebElement chiveryLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-cart-shopping")));
    }

    public WebElement dopamineDumpCounter() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("countdown")));
    }

    public WebElement dopamineDump() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("dopamine-dump-counter")));
    }

    public String dopamineDumpHour() {
        // Find the countdown element
        WebElement countdownElement = helpers.Waiter.wait(driver)
                .until(ExpectedConditions.presenceOfElementLocated(By.className("countdown")));
        List<WebElement> timeElements = countdownElement.findElements(By.tagName("span"));
        return timeElements.get(0).getAttribute("style").replaceAll("[^\\d]", "");
    }

    public String dopamineDumpMinute() {
        // Find the countdown element
        WebElement countdownElement = helpers.Waiter.wait(driver)
                .until(ExpectedConditions.presenceOfElementLocated(By.className("countdown")));
        List<WebElement> timeElements = countdownElement.findElements(By.tagName("span"));
        return timeElements.get(1).getAttribute("style").replaceAll("[^\\d]", "");
    }

    public String dopamineDumpSecond() {
        // Find the countdown element
        WebElement countdownElement = helpers.Waiter.wait(driver)
                .until(ExpectedConditions.presenceOfElementLocated(By.className("countdown")));
        List<WebElement> timeElements = countdownElement.findElements(By.tagName("span"));
        return timeElements.get(2).getAttribute("style").replaceAll("[^\\d]", "");
    }

    public WebElement dropDown() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[id^='headlessui-menu-button-']")));
    }

    public WebElement dropDownChivery() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='Chivery']")));
    }

    public WebElement dropDownCharities() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='Chive Charities']")));
    }

    public WebElement dropDownChive() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='The Chive']")));
    }

    public WebElement dropDownChiveTV() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='Chive TV']")));
    }

    public WebElement filterChange() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '(change filter)')]")));
    }

    public WebElement filterHotness() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='Hotness']")));
    }

    public WebElement filterHumanity() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='Humanity']")));
    }

    public WebElement filterHumor() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='Humor']")));
    }

    public WebElement firstTag() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id^='tag-']")));
    }

    public WebElement headerAvatar() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-id='header-user-menu']")));
    }

    public WebElement ichiveLogo() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='IChive']")));
    }

    public WebElement linkMenu() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[id^='headlessui-menu-button-']")));
    }

    public WebElement loginBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("avatar")));
    }

    public WebElement menuFeatured() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("menu-Featured")));
    }

    public WebElement menuFollowing() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("menu-Following")));
    }

    public WebElement menuLatest() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("menu-Latest")));
    }

    public WebElement menuTopChivers() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("menu-Top-Chivers")));
    }

    public WebElement searchButton() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("search-form-button")));
    }

    public WebElement settingsBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Settings")));
    }

    public WebElement signoutBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Sign out')]")));
    }


    public WebElement submitBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("submission-create")));
    }

    public WebElement trophyIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-trophy")));
    }

    public WebElement userMenu() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("btn-circle")));
    }

    public WebElement yourProfileBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Profile")));
    }

    public List<WebElement> yourProfileByPosition() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("li.flex.items-center.hover\\:bg-primary.text-white.px-4.py-2.text-sm")));
    }


}
