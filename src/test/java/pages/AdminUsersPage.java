package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

@SuppressWarnings("unused")
public class AdminUsersPage {

    WebDriver driver;

    public AdminUsersPage(WebDriver driver) {
        this.driver = driver;
    }

    //******************** Elements ***********************

    public WebElement actionsDropdown() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("select[dusk='action-select']")));
    }

    public Select actionsDropdownSelect() {
        return new Select(helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("select[dusk='action-select']"))));
    }

    public WebElement banCancelBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[dusk='cancel-action-button']")));
    }

    public WebElement banConfirmBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[dusk='confirm-action-button']")));
    }

    public WebElement banReasonInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("reason-default-text-field")));
    }

    public WebElement deleteUser(String userID) {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[dusk='" + userID + "-delete-button']")));
    }

    public WebElement editUserPage(String userID) {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[dusk='" + userID + "-edit-button']")));
    }

    public WebElement emailLink(String userID) {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr[dusk='" + userID + "-row']"))).findElement(By.cssSelector("a[href^='mailto']"));
    }

    public WebElement menuAllUsers() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.sidebar-section:nth-child(1)")));
    }

    public WebElement menuExpandUsers() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#nova > div:nth-child(2) > div.hidden.lg\\:block.lg\\:absolute.left-0.bottom-0.lg\\:top-\\[56px\\].lg\\:bottom-auto.w-60.px-3.py-8 > div > div:nth-child(2) > button")));
    }

    public WebElement nonChivetteUserRow() {
        WebElement element = null;
        List<WebElement> users = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tr[dusk*='-row']")));
        for (WebElement user : users) {
            List<WebElement> checks = user.findElements(By.cssSelector("svg[xmlns='http://www.w3.org/2000/svg']"));
            if (checks.get(2).getAttribute("class").contains("red")) {
                element = user;
                break;
            }
        }
        return element;
    }

    public WebElement notPrivateUserRow() {
        WebElement element = null;
        List<WebElement> users = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tr[dusk*='-row']")));
        for (WebElement user : users) {
            List<WebElement> checks = user.findElements(By.cssSelector("svg[xmlns='http://www.w3.org/2000/svg']"));
            if (checks.get(3).getAttribute("class").contains("red")) {
                element = user;
                break;
            }
        }
        return element;
    }

    public WebElement profilePic(String userID) {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr[dusk='" + userID + "-row']"))).findElement(By.cssSelector("img"));
    }

    public WebElement searchInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[data-testid='search-input']")));
    }

    public WebElement selectAllUsers() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span[class='fake-checkbox']")));
    }

    public WebElement unbannedUserRow() {
        WebElement element = null;
        List<WebElement> users = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tr[dusk*='-row']")));
        for (WebElement user : users) {
            List<WebElement> checks = user.findElements(By.cssSelector("svg[xmlns='http://www.w3.org/2000/svg']"));
            if (checks.get(0).getAttribute("class").contains("red")) {
                element = user;
                break;
            }
        }
        return element;
    }

    public WebElement unVerifiedUserRow() {
        WebElement element = null;
        List<WebElement> users = helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tr[dusk*='-row']")));
        for (WebElement user : users) {
            List<WebElement> checks = user.findElements(By.cssSelector("svg[xmlns='http://www.w3.org/2000/svg']"));
            if (checks.get(1).getAttribute("class").contains("red")) {
                element = user;
                break;
            }
        }
        return element;
    }

    public List<WebElement> userCheckOrX(String userID) {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr[dusk='" + userID + "-row']"))).findElements(By.cssSelector("svg[xmlns='http://www.w3.org/2000/svg']"));

    }

    public WebElement userRowCheckbox(String userID) {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr[dusk='" + userID + "-row']"))).findElement(By.cssSelector("input[type='checkbox']"));
    }

    public WebElement usersTitle() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[span[text()='Users']")));
    }

    public WebElement viewUserPage(String userName) {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr[dusk*='-row']"))).findElement(By.cssSelector("a[href='/" + userName + "']"));
    }


}