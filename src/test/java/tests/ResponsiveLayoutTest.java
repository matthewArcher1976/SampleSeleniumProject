package tests;

import helpers.CustomExpectedConditions;

import helpers.Waiter;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import resources.Config;
import resources.TestConfig;

import java.util.*;

import static resources.getDriverType.getDriver;

public class ResponsiveLayoutTest {

    /*
     ************************************** Notes ******************************************
     * Test for vertical spacing only works for the top row of cards, after that the order of the cards in allCards() does not
     * always match the placement on screen, ie cardLocations(i+4) should be right below cardLocations(i) but it's not. allCards()
     * is numeric order of the values of allCards.get(i).getAttribute("id"), but on screen they're not always ordered that way.
     * The actual layout changes with the card dimensions and therefore not consistent enough to make a robust test that way. Try
     * finding the card below with Dimension/Point and see if it's robust enough
    */
    WebDriver driver;
    private static TestConfig config;
    SubmissionCardsPage submissionCardsPage;

    Actions actions;

    //************************** Setup ******************************************

    @BeforeTest
    public void configs() throws Exception {
        config = Config.getConfig();
        driver = getDriver(config.driverType);
        actions = new Actions(driver);
        submissionCardsPage = new SubmissionCardsPage(driver);
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

    //************************** Begin BIG ASS Test ********************************************


    @Test
    public void ResponsiveLayout() throws InterruptedException {
        Dimension small = new Dimension(800, 650);
        Dimension medium = new Dimension(1100, 750);
        Dimension large = new Dimension(1500, 900);
        Waiter.wait(driver).until(CustomExpectedConditions.cardsLoaded());

        driver.manage().window().fullscreen();
        Thread.sleep(5000);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        List<WebElement> allCards = submissionCardsPage.allCards();
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
                    "Fullscreen window - spaces between cards should be the same, horizontal. Found " + firstSpaceBetween +
                            " and " + nextSpaceBetween +" for cards " + i + " and " + i+1);
            i++;
        }

        //resize to a large window and reset variables, layout should be same as fullscreen still
        driver.manage().window().setSize(large);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        allCards = submissionCardsPage.allCards();
        cardLocations.clear();
        i = 0;
        for (WebElement card : allCards) {
            cardLocations.add(card.getLocation());
        }

        //should still be a row of 4 on top
        while (i < 4) {
            if (i <= 2) {
                Assert.assertEquals(cardLocations.get(i).getY(), cardLocations.get(i + 1).getY(),
                        "Expected to find four cards in the top row in large window, found  " + cardLocations.get(i).getY() + " " + cardLocations.get(i + 1).getY());
            }
            if (i == 3) {
                Assert.assertNotEquals(cardLocations.get(0).getY(), cardLocations.get(i+1).getY());
            }
            i++;
        }

        //Large window - spaces between cards should be the same, vertical
        i = 0;
        referenceSpace = -1; // To store the space between the first pair of cards
        while (i<=3) {
            int bottomEdgeCurrentCard = cardLocations.get(i).getY() + allCards.get(i).getSize().getHeight();
            int topEdgeCardBelow = cardLocations.get(i + 4).getY();
            int spaceBetween = topEdgeCardBelow - bottomEdgeCurrentCard;

            if (i == 0) {
                // Set the reference space using the first pair of cards
                referenceSpace = spaceBetween;
            } else {
                // Assert that the space between the current pair of cards is the same as the reference space
                Assert.assertEquals(referenceSpace, spaceBetween, "Large window - Inconsistent space found at card index " + i);
            }
            i++;
        }

        //Large window - spaces between cards should be the same, horizontal
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
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        //refresh the cards and locations
        allCards = submissionCardsPage.allCards();
        cardLocations.clear();
        i = 0;
        for (WebElement card : allCards) {
            cardLocations.add(card.getLocation());
        }

        //Should be three cards per row

        while (i < 3) {
            if (i <= 1) {
                Assert.assertEquals(cardLocations.get(i).getY(), cardLocations.get(i + 1).getY(),
                        "Expected to find three cards in the top row in a medium screen, found  " + cardLocations.get(i).getY() + " " + cardLocations.get(i + 1).getY());
            }
            if (i == 2) {
                Assert.assertNotEquals(cardLocations.get(0).getY(), cardLocations.get(i+1).getY());
            }
            i++;
        }

        //Spacing between cards, medium window, vertical
        i = 0;
        referenceSpace = -1; // To store the space between the first pair of cards
        while (i<=2) {
            int bottomEdgeCurrentCard = cardLocations.get(i).getY() + allCards.get(i).getSize().getHeight();
            int topEdgeCardBelow = cardLocations.get(i + 3).getY();
            int spaceBetween = topEdgeCardBelow - bottomEdgeCurrentCard;
            if (i == 0) {
                // Set the reference space using the first pair of cards
                referenceSpace = spaceBetween;
            } else {
              //  System.out.println(allCards.get(i).getAttribute("id"));
                // Assert that the space between the current pair of cards is the same as the reference space
                Assert.assertEquals(referenceSpace, spaceBetween, "Large window - Inconsistent space found at card index " + i);
            }
            i++;
        }
        //Horizontal space between cards in medium browser window
        i = 0;
        while (i <= 1) {
            int rightEdgeCurrentCard = cardLocations.get(i).getX() + allCards.get(i).getSize().getWidth();
            int leftEdgeNextCard = cardLocations.get(i + 3).getX();
            int spaceBetween = leftEdgeNextCard - rightEdgeCurrentCard;
            if (i == 0) {
                // Set the reference space using the first pair of cards
                referenceSpace = spaceBetween;
            } else {
                //  System.out.println(allCards.get(i).getAttribute("id"));
                // Assert that the space between the current pair of cards is the same as the reference space
                Assert.assertEquals(referenceSpace, spaceBetween, "Large window - Inconsistent space found at card index " + i);
            }
            i++;
        }

        //Resize to small window

        driver.manage().window().setSize(small);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        //refresh the cards and locations
        allCards = submissionCardsPage.allCards();
        cardLocations.clear();
        i = 0;
        for (WebElement card : allCards) {
            cardLocations.add(card.getLocation());
        }
        //Should be two cards per row in small window
        while (i < 2) {
            if (i <= 0) {
                Assert.assertEquals(cardLocations.get(i).getY(), cardLocations.get(i + 1).getY(),
                        "Expected to find three cards in the top row in a medium screen, found  " + cardLocations.get(i).getY() + " " + cardLocations.get(i + 1).getY());
            }
            if (i == 1) {
                Assert.assertNotEquals(cardLocations.get(0).getY(), cardLocations.get(i+1).getY());
            }
            i++;
        }
    }

    @Test(enabled = false)//come back to this
    public void TryVerticalsByPos() throws InterruptedException {
        //trying to use card positions to find the right one below
        Dimension large = new Dimension(1500, 900);
        driver.manage().window().setSize(large);
        Thread.sleep(5000);
        Waiter.wait(driver).until(CustomExpectedConditions.pageLoaded());
        List<WebElement> allCards = submissionCardsPage.allCards();

        for (int i = 0; i < allCards.size()-5 ; i++){
            WebElement topCard = allCards.get(i);
            Point topCardLocation = topCard.getLocation();
            Dimension topCardSize = topCard.getSize();

            // Calculating bottom card's expected Y coordinate
            int belowCardY = topCardLocation.getY() + topCardSize.getHeight() + 16;
            Point belowCardLoc = new Point(topCardLocation.getX() + topCardSize.getWidth() / 2, belowCardY);

            WebElement belowCard = submissionCardsPage.cardAtPosition(belowCardLoc.getX(), belowCardLoc.getY());

            System.out.println("Below card for index 3 should be " + allCards.get(7).getAttribute("id") + " at "  + allCards.get(7).getLocation());

            System.out.println("At index " + i + " Top card is " + topCard.getAttribute("id") + " located at " + topCardLocation +
                    " belowCard is " + belowCard.getAttribute("id") + " located at at " + belowCardLoc);

        }
    }

    //************************** Teardown ********************************************

    @AfterClass
    public void TearDown() {
        driver.quit();
    }

}
