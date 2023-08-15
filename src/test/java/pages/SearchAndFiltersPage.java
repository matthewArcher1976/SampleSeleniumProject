package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class SearchAndFiltersPage {

    WebDriver driver;

    public SearchAndFiltersPage(WebDriver driver) {
        this.driver = driver;
    }

    //******************** Page Actions ***********************

    public List<WebElement> allCards() {
        return driver.findElements(By.cssSelector("[id^='submission-']:not([id='submission-create']):not([id='submission-list']):not([id^='submission-image'])"));
    }

    public List<WebElement> allTagCards() {
        return driver.findElements(By.cssSelector("[id^='tag-card-']"));
    }

    public List<WebElement> allUserCards() {
        return driver.findElements(By.cssSelector("[id^='user-card-']:not([id*='follow-btn'])"));
    }

    public WebElement filterHotness() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[data-slug='hotness']")));
    }

    public WebElement filterHumanity() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[data-slug='humanity']")));
    }

    public WebElement filterHumor() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[data-slug='humor']")));
    }

    public WebElement filterVerified() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[value='verified']")));
    }

    public WebElement followBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[id^='user-card-'][id*='-follow-btn']")));
    }

    public WebElement firstUser() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='user-card-']"))).findElement(By.className("flex-col"));
    }

    public WebElement goButton() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[value='Go!']")));
    }

    public WebElement searchResultHeader() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".text-lg.mb-4.mt-4")));
    }


    public WebElement magnifyIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-magnifying-glass")));
    }

    public WebElement recentlyVerifiedUser() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='row-recently-verified-username-']")));
    }

    public WebElement recentlyVerifiedText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Recently Verified')]")));
    }

    public WebElement resultsTag() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='tag-card-']")));
    }

    public WebElement resultsText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'Here's what we found for:')]")));
    }

    public WebElement searchInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("search-form-input")));
    }

    public WebElement socialFacebookLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-facebook-f")));
    }

    public WebElement socialInstagramLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-instagram")));
    }

    public WebElement socialTwitterLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-twitter")));
    }

    public WebElement tag() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='tag-']")));
    }

    public WebElement trendArrowIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-arrow-trend-up")));
    }

    public WebElement userCard() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='user-card-']")));
    }


}
