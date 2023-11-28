package tests;


import helpers.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.*;
import pages.PageHeaderPage;
import pages.SubmissionCardsPage;
import resources.Config;
import resources.TestConfig;



import static resources.getDriverType.getDriver;

public class NotificationsTest {

    WebDriver driver1; //receives the notification
    WebDriver driver2;//clicks to send it

    Logins login1;
    Logins login2;

    PageHeaderPage header1;
    PageHeaderPage header2;

    SubmissionCardsPage card;
    String userName;
    Actions action;
    private static TestConfig config;

    //************************** Setup ******************************************
    //TODO - notifications test cases
    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver1 = getDriver(config.driverType);
        driver2 = getDriver(config.driverType);
        card = new SubmissionCardsPage(driver2);
        login1 = new Logins(driver1);
        login2 = new Logins(driver2);
        header1 = new PageHeaderPage(driver1);
        header2 = new PageHeaderPage(driver2);
        action = new Actions(driver1);
    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver1.get(config.url);
        driver2.get(config.url);
        login1.unpaidLogin(config.defaultEmail, config.password);
        login2.unpaidLogin(config.chivetteEmail, config.password);
        Waiter.wait(driver1).until(ExpectedConditions.urlContains("mychive"));
        Waiter.wait(driver2).until(ExpectedConditions.urlContains("mychive"));
        header1.headerAvatar().click();
        header1.yourProfileBtn().click();
        Waiter.wait(driver1).until(CustomExpectedConditions.profileLoaded());
        userName = StringHelper.getIdFromUrl(driver1.getCurrentUrl());
    }

    @BeforeMethod
    public void refresh() {
        driver2.get(config.url);
        driver1.get(config.url);
    }

    //************************** Test Cases ******************************************

    @Test
    public void LikePost() throws InterruptedException {

        header1.notificationIcon().click();//clear the icon of any pending notifications
        header1.notificationIcon().click();//click again to close the popup
        Waiter.wait(driver1).until(CustomExpectedConditions.cardsLoaded());
        WebElement ourCard = card.cardFromUser(userName);

        if (!card.isSelected(ourCard.findElement(By.className(card.upvoteBtnClass())))){
            PageActions.findElementWithScrollingElement(driver1, ourCard.findElement(By.className(card.upvoteBtnClass())));
           PageActions.scrollDown(driver1, 1);
           Thread.sleep(999999);
            ourCard.findElement(By.className(card.upvoteBtnClass())).click();//
        }

    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver1.close();
        driver2.close();
        driver1.quit();
        driver2.quit();
    }
}


