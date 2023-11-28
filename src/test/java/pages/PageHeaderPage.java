package pages;

import helpers.Waiter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings("unused")
public class PageHeaderPage {

    WebDriver driver;

    public PageHeaderPage(WebDriver driver) {
        this.driver = driver;
    }

    //*********************** WebElements *******************************
    public WebElement adFrame() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe[id*=google_ads_iframe]")));
    }

    public WebElement adImage() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("google_image_div")));
    }

    public WebElement avatarPic() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("avatar"))).findElement(By.className("rounded-full")).findElement(By.cssSelector("img"));
    }

    public WebElement notificationIcon(){
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-bell")));
    }

    public WebElement chiveryLink() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-cart-shopping")));
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
        return timeElements.get(0).getAttribute("style").replaceAll("\\D", "");
    }

    public String dopamineDumpMinute() {
        // Find the countdown element
        WebElement countdownElement = helpers.Waiter.wait(driver)
                .until(ExpectedConditions.presenceOfElementLocated(By.className("countdown")));
        List<WebElement> timeElements = countdownElement.findElements(By.tagName("span"));
        return timeElements.get(1).getAttribute("style").replaceAll("\\D", "");
    }

    public String dopamineDumpSecond() {
        // Find the countdown element
        WebElement countdownElement = helpers.Waiter.wait(driver)
                .until(ExpectedConditions.presenceOfElementLocated(By.className("countdown")));
        List<WebElement> timeElements = countdownElement.findElements(By.tagName("span"));
        return timeElements.get(2).getAttribute("style").replaceAll("\\D", "");
    }

    public WebElement dropDown() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[id^='headlessui-menu-button-']")));
    }

    public WebElement dropDownChivery() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='Chivery']")));
    }

    public WebElement dropDownCharities() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='Chive Charities']")));
    }

    public WebElement dropDownChive() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='The Chive']")));
    }

    public WebElement dropDownChiveTV() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='Chive TV']")));
    }

    public WebElement filterChange() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '(change filter)')]")));
    }

    public WebElement filterGo() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[value='Go!']")));
    }

    public WebElement filterHotness() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Hotness']")));
    }

    public WebElement filterHumanity() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Humanity']")));
    }

    public WebElement filterHumor() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Humor']")));
    }

    public WebElement firstTag() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id^='tag-']")));
    }

    public WebElement headerAvatar() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-id='header-user-menu']")));
    }

    public WebElement homeButton(){
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href]")));
    }

    public WebElement ichiveLogo() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt='myCHIVE']")));
    }

    public WebElement linkMenu() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[id^='headlessui-menu-button-']")));
    }

    public WebElement loginBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("avatar")));
    }

    public WebElement loginSuccess() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("alert-success")));
    }

    public WebElement menuChivettes() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("menu-Verified-Chivettes")));
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
    public String menuLatestId() {
        return "menu-Latest";
    }
    public WebElement menuTopChivers() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("menu-Top-Chivettes")));
    }

    public WebElement privateUserIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-user-secret")));
    }

    public WebElement privateUserText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("p[class='m']")));
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
        try {
          return Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("btn-circle")));
        }catch (TimeoutException e){
            return Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("avatar")));
        }
    }

    public WebElement verifyEmailHeader() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//strong[contains(text(), 'Verify your email')]")));
    }

    public WebElement yourProfileBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Profile")));
    }

    public List<WebElement> yourProfileByPosition() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("li.flex.items-center.hover\\:bg-primary.text-white.px-4.py-2.text-sm")));
    }


}
