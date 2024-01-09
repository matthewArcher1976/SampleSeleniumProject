package tests;

import helpers.CustomExpectedConditions;
import helpers.Logins;
import helpers.Waiter;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import resources.Config;
import resources.TestConfig;

import java.util.ArrayList;
import java.util.List;

import static resources.getDriverType.getDriver;

public class ResponsiveLayoutTest {
    WebDriver driver;
    private static TestConfig config;
    SubmissionCardsPage card;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        card = new SubmissionCardsPage(driver);

    }

    @BeforeClass
    public void login() throws InterruptedException {
        driver.get(config.url);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
    }

    @BeforeMethod
    public void setDriver() throws InterruptedException {
        driver.get(config.url);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
    }

    //************************** Begin Tests ********************************************

    @Test
    public void ResponsiveLayout() throws InterruptedException {
        Dimension small = new Dimension(800, 650);
        Dimension medium = new Dimension(1000, 750);
        Dimension large = new Dimension(1500, 900);
        Waiter.wait(driver).until(CustomExpectedConditions.cardsLoaded());

        driver.manage().window().fullscreen();
        Thread.sleep(5000);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        List<WebElement> allCards = card.allCards();
        List<Point> cardLocations = new ArrayList<>();

        for (WebElement card : allCards) {
            cardLocations.add(card.getLocation());
        }
        //in fullscreen, four cards in top row
        int i = 0;
        while (i < 4) {

            if(i<=2) {
                Assert.assertEquals(cardLocations.get(i + 1).getY(), cardLocations.get(i).getY(), "Expected to find four cards in the top row in fullscreen, found  "
                        + cardLocations.get(i).getY() + " " + cardLocations.get(i + 1).getY());
            }
            if(i == 3){
                Assert.assertNotEquals(cardLocations.get(i + 1).getY(), cardLocations.get(i).getY(), "The fifth card should be in a new row");
            }
            i++;
        }

        //Spaces between cards should be the same, vertical
        i = 0;
        int referenceSpace = -1; // To store the space between the first pair of cards
        while (i<=4) {
            int bottomEdgeCurrentCard = cardLocations.get(i).getY() + allCards.get(i).getSize().getHeight();
            int topEdgeCardBelow = cardLocations.get(i + 4).getY();
            int spaceBetween = topEdgeCardBelow - bottomEdgeCurrentCard;

            if (i == 0) {
                // Set the reference space using the first pair of cards
                referenceSpace = spaceBetween;
            } else {
                // Assert that the space between the current pair of cards is the same as the reference space
                Assert.assertEquals(referenceSpace, spaceBetween, "Fullscreen - Inconsistent space found at card index " + i);
            }
            i++;
        }


        //Spaces between cards should be the same, horizontal
        i = 0;
        while (i < 2) {
            int rightEdgeFirstCard = cardLocations.get(i).getX() + allCards.get(i).getSize().getWidth();
            int leftEdgeSecondCard = cardLocations.get(i + 1).getX();
            int firstSpaceBetween = leftEdgeSecondCard - rightEdgeFirstCard;

            int rightEdgeNextCard = cardLocations.get(i + 1).getX() + allCards.get(i + 1).getSize().getWidth();
            int leftEdgeNextToNextCard = cardLocations.get(i + 2).getX();
            int nextSpaceBetween = leftEdgeNextToNextCard - rightEdgeNextCard;

            Assert.assertEquals(firstSpaceBetween, nextSpaceBetween,
                    "Large window - spaces between cards should be the same, horizontal. Found " + firstSpaceBetween +
                            " and " + nextSpaceBetween +" for cards " + i + " and " + i+1);
            i++;
        }

        //resize to a large window and reset variables, layout should be same as fullscreen still
        driver.manage().window().setSize(large);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        allCards = card.allCards();
        cardLocations.clear();
        i = 0;
        for (WebElement card : allCards) {
            cardLocations.add(card.getLocation());
        }
        //should still be a row of 4 on top
        while (i < 4) {
            if (i <= 2) {
                Assert.assertEquals(cardLocations.get(i).getY(), cardLocations.get(i + 1).getY(),
                        "Expected to find four cards in the top row in fullscreen, found  " + cardLocations.get(i).getY() + " " + cardLocations.get(i + 1).getY());
            }
            if (i == 3) {
                Assert.assertEquals(cardLocations.get(0).getY(), cardLocations.get(3).getY());
            }
            i++;
        }

        //Spaces between cards should be the same, vertical
        i = 0;
        referenceSpace = -1; // To store the space between the first pair of cards
        while (i<=4) {
            int bottomEdgeCurrentCard = cardLocations.get(i).getY() + allCards.get(i).getSize().getHeight();
            int topEdgeCardBelow = cardLocations.get(i + 4).getY();
            int spaceBetween = topEdgeCardBelow - bottomEdgeCurrentCard;

            if (i == 0) {
                // Set the reference space using the first pair of cards
                referenceSpace = spaceBetween;
            } else {
                // Assert that the space between the current pair of cards is the same as the reference space
                Assert.assertEquals(referenceSpace, spaceBetween, "Fullscreen - Inconsistent space found at card index " + i);
            }
            i++;
        }

        //Spaces between cards should be the same, horizontal
        i = 0;
        while (i < 2) {
            int rightEdgeFirstCard = cardLocations.get(i).getX() + allCards.get(i).getSize().getWidth();
            int leftEdgeSecondCard = cardLocations.get(i + 1).getX();
            int firstSpaceBetween = leftEdgeSecondCard - rightEdgeFirstCard;

            int rightEdgeNextCard = cardLocations.get(i + 1).getX() + allCards.get(i + 1).getSize().getWidth();
            int leftEdgeNextToNextCard = cardLocations.get(i + 2).getX();
            int nextSpaceBetween = leftEdgeNextToNextCard - rightEdgeNextCard;

            Assert.assertEquals(firstSpaceBetween, nextSpaceBetween,
                    "Large window - spaces between cards should be the same, horizontal. Found " + firstSpaceBetween +
                            " and " + nextSpaceBetween + " for cards " + i + " and " + i+1);
            i++;
        }

        //Medium size browser window

        driver.manage().window().setSize(medium);
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}
