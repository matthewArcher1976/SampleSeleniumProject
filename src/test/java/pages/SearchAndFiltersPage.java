package pages;

import helpers.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
@SuppressWarnings({"class", "unused"})
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
    public WebElement filterFollowing() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[value='submission.following']")));
    }
    public WebElement filterHotness() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[data-slug='hotness']")));
    }

    public WebElement filterHumanity() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[data-slug='humanity']")));
    }

    public WebElement filterHumor() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[data-slug='humor']")));
    }

    public WebElement filterVerified() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[value='verified']")));
    }

    public WebElement followBtn() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[id^='user-card-'][id*='-follow-btn']")));
    }

    public WebElement firstUser() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='user-card-']"))).findElement(By.className("flex-col"));
    }

    public WebElement goButton() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[value='Go!']")));
    }

    public WebElement searchResultHeader() {
        return Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".text-lg.mb-4.mt-4")));
    }


    public WebElement magnifyIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-magnifying-glass")));
    }

    public WebElement recentlyVerifiedUser() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='row-recently-verified-username-']")));
    }

    public WebElement recentlyVerifiedText() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Recently Verified')]")));
    }

    public WebElement resultsTag() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='tag-card-']")));
    }

    public WebElement resultsText() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'Here's what we found for:')]")));
    }

    public WebElement searchInput() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("search-form-input")));
    }
    public WebElement searchSuggestions() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[id^='search-instant-result-']")));
    }

    public WebElement tag() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='tag-']")));
    }

    public WebElement trendArrowIcon() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-arrow-trend-up")));
    }

    public WebElement userCard() {
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id^='user-card-']")));
    }


}
