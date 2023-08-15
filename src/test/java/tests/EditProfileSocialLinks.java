package tests;


import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.testng.Assert;
import org.testng.annotations.*;

import helpers.Drivers;
import helpers.Logins;
import pages.EditProfilePage;
import pages.ProfilePage;

@SuppressWarnings({"DefaultAnnotationParam", "NewClassNamingConvention"})

public class EditProfileSocialLinks {

	WebDriver driver = Drivers.ChromeDriver();
	ProfilePage profilePage = new ProfilePage(driver);
	EditProfilePage profile = new EditProfilePage(driver);
	Logins login = new Logins(driver);
	Actions action = new Actions(driver);
	@BeforeTest
	@Parameters({"unpaidEmail", "password", "url"})
	public void login(@Optional("thechivetest@gmail.com") String unpaidEmail, @Optional("Chive1234") String password, @Optional("https://qa.chive-testing.com") String url) throws InterruptedException {
		driver.get(url);
		login.unpaidLogin(unpaidEmail, password, driver);
		Thread.sleep(2000);
	}

	 @BeforeMethod
	 @Parameters ({"url"})
	  public void setDriver(@Optional("https://qa.chive-testing.com") String url) {
		  driver.get(url);
		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  }

	  @AfterClass
	  public void TearDown() {driver.close();}

	  //************************** Begin Tests ********************************************

	  @Test(enabled = true, priority = 1)
	  public void AmazonEditCancel() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.amazonEditBtn().click();
		  Assert.assertTrue(profile.amazonEditBtnCancel().isDisplayed(), "AmazonEditCancel - Did not see the edit cancel button");

		  profile.amazonEditBtnCancel().click();
		  Assert.assertTrue(profile.amazonEditBtn().isDisplayed(), "AmazonEditCancel - Did not see the edit button after clicking cancel");
	  }

	  @Test(enabled = false, priority = 1)
	  public void AmazonLink() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.amazonEditBtn().click();
		  profile.amazonInput().clear();
		  profile.amazonInput().sendKeys("https://amazon.com/registry/wishlist/28NX67QM7I7D3");
		  profile.saveProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
		  Assert.assertTrue(profile.amazonInputInactive().isDisplayed(), "Did not the wantfor.me link");

		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));
		  Assert.assertTrue(profilePage.amazonLink().isDisplayed(), "Did not fine the Amazon icon");

		  profilePage.amazonLink().click();
		  helpers.WindowUtil.switchToWindow(driver, 1);

		  try {
			  Assert.assertTrue(helpers.Waiter.quickWait(driver).until(ExpectedConditions.titleContains("Amazon.com")) &&
					            helpers.Waiter.quickWait(driver).until(ExpectedConditions.urlContains("wishlist")));
		  }catch(AssertionError e) {
			  System.out.println("Did not find Amazon window");
			  driver.close();
			  helpers.WindowUtil.switchToWindow(driver, 0);
			  System.out.println("Did not fine the Amazon icon");
			  Assert.fail();
		  }
		  catch(TimeoutException e) {
			  driver.close();
			  helpers.WindowUtil.switchToWindow(driver, 0);
			  Assert.fail();
		  }
		  driver.close();
		  helpers.WindowUtil.switchToWindow(driver, 0);
	  }
	  @Test(enabled = true, priority = 1)
	  public void AmazonLinkBadURL() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.amazonEditBtn().click();
		  profile.amazonInput().clear();
		  profile.amazonInput().sendKeys("www.google.com");
		  profile.saveProfileBtn().click();
		  Assert.assertTrue(profile.amazonInvalid().isDisplayed(), "Did not get the invalid URL error message");
	  }

	  @Test(enabled = false, priority = 1)//Nothing I do changes when I mouseover the element or not, even though the tooltip is not visible
	  public void AmazonLinkHover() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  WebElement toolTip = profile.amazonTooltip();
		  System.out.println(toolTip.getCssValue("color"));
		  action.moveToElement(toolTip).perform();
		  Assert.assertTrue(profile.amazonTooltip().isDisplayed(), "Did not find Amazon tooltip");
	  }
	  @Test(enabled = true, priority = 1)
	  public void AmazonLinkShortened() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.amazonEditBtn().click();
		  profile.amazonInput().clear();
		  profile.amazonInput().sendKeys("https://amazon.com/registry/wishlist/28NX67QM7I7D3");
		  profile.saveProfileBtn().click();
		  Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.amazonInputInactive())).isDisplayed(), "Did not find the short link");
		 // Assert.assertTrue(profile.amazonInputInactive().isDisplayed(), "Did not find the short link");// this element finds it by the wantfor.me value
	  }
	  @Test(enabled = true, priority = 1)
	  public void FaceBookLink() {
		  //enter your FB username
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.facebookInput().clear();
		  profile.facebookInput().sendKeys("asdf");
		  profile.saveProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));
		  Assert.assertTrue(profilePage.facebookLink().isDisplayed(), "Did not find facebook button");
		  profilePage.facebookLink().click();
		  helpers.WindowUtil.switchToWindow(driver, 1);

		  Assert.assertTrue(profilePage.facebookLogo().isDisplayed(), "Did not find facebook window");

		  driver.close();
		  helpers.WindowUtil.switchToWindow(driver, 0);
	  }
	  @Test(enabled = true, priority = 1)//These inputs will accept any string now and just concatenate it to the base URL for that social media site
	  public void FaceBookLinkBadURL() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.facebookInput().clear();
		  profile.facebookInput().sendKeys("https://www.faceXXXEbook.com/@asdf");
		  profile.saveProfileBtn().click();
		  Assert.assertTrue(profile.facebookBadURL().isDisplayed(), "Should have rejected bad Facebook link");
	  }
	  @Test(enabled = true, priority = 1)
	  public void FaceBookLinkPartialURL() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.facebookInput().clear();
		  profile.facebookInput().sendKeys("facebook.com/asdf");
		  profile.saveProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));

		  Assert.assertTrue(profilePage.facebookLink().isDisplayed(), "Did not find facebook button");

		  profilePage.facebookLink().click();
		  helpers.WindowUtil.switchToWindow(driver, 1);

		  Assert.assertTrue(profilePage.facebookLogo().isDisplayed(), "Did not find facebook window");

		  driver.close();
		  helpers.WindowUtil.switchToWindow(driver, 0);
	  }
	  @Test(enabled = true, priority = 1)
	  public void FaceBookLinkWholeURL() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.facebookInput().clear();
		  profile.facebookInput().sendKeys("http://www.facebook.com/asdf");
		  profile.saveProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));

		  Assert.assertTrue(profilePage.facebookLink().isDisplayed(), "Did not find facebook button");

		  profilePage.facebookLink().click();
		  helpers.WindowUtil.switchToWindow(driver, 1);

		  Assert.assertTrue(profilePage.facebookLogo().isDisplayed(), "Did not find facebook window");
		  driver.close();
		  helpers.WindowUtil.switchToWindow(driver, 0);
	  }

	  @Test(enabled = true, priority = 1)//These inputs will accept any string now and just concatenate it to the base URL for that social media site
	  public void FaceBookUserHandle() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.facebookInput().clear();
		  profile.facebookInput().sendKeys("@asdf");
		  profile.saveProfileBtn().click();

		  Assert.assertTrue(profile.updateSuccess().isDisplayed(), "FaceBookUserHandle - Should accept @username");
	  }

	  @Test(enabled = true, priority = 1)
	  public void instagramLink() throws InterruptedException {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.instagramInput().clear();
		  profile.instagramInput().sendKeys("asdf");
		  profile.saveProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));
		 // Thread.sleep(5000);

		  Assert.assertTrue(profilePage.instagramLink().isDisplayed(), "Did not find insta button");

		  String instaHandle = profilePage.userGetter(profilePage.instagramLink());
		  profilePage.instagramLink().click();
		  helpers.WindowUtil.switchToWindow(driver, 1);
		  helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains(instaHandle));

		  Assert.assertTrue(driver.getTitle().contains("Instagram photos and videos")
				  && driver.getTitle().contains(instaHandle), "Did not find Insta window");

		  driver.close();
		  helpers.WindowUtil.switchToWindow(driver, 0);
	  }
	  @Test(enabled = true, priority = 1)
	  public void InstaLinkBadURL() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.instagramInput().clear();
		  profile.instagramInput().sendKeys("https://www.instagram.com/qwerty/asdf");
		  profile.saveProfileBtn().click();
		  Assert.assertTrue(profile.instagramBadURL().isDisplayed(), "Should have rejected bad Instagram link");
	  }

	  @Test(enabled = true, priority = 1)
	  public void InstaLinkWholeURL() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.instagramInput().clear();
		  profile.instagramInput().sendKeys("https://www.instagram.com/asdf");
		  profile.saveProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));

		  Assert.assertTrue(profilePage.instagramLink().isDisplayed(), "Did not find insta button");

		  String instaHandle = profilePage.userGetter(profilePage.instagramLink());
		  profilePage.instagramLink().click();
		  helpers.WindowUtil.switchToWindow(driver, 1);
		  helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains(instaHandle));
		  Assert.assertTrue(driver.getTitle().contains("Instagram photos and videos")
				  && driver.getTitle().contains(instaHandle), "Did not find Insta window");
		  driver.close();
		  helpers.WindowUtil.switchToWindow(driver, 0);
	  }
	  @Test(enabled = true, priority = 1)
	  public void InstaLinkUserHandle() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.instagramInput().clear();
		  profile.instagramInput().sendKeys("@asdf");
		  profile.saveProfileBtn().click();

		  Assert.assertTrue(profile.updateSuccess().isDisplayed(), "InstaLinkUserHandle - Should accept Instagram handle with the @");
	  }
	  @Test(enabled = true, priority = 1)
	  public void TikTokLink() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.tikTokInput().clear();
		  profile.tikTokInput().sendKeys("asdf");
		  profile.saveProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));

		  Assert.assertTrue(profilePage.tiktokLink().isDisplayed(), "Did not find tiktok button");

		  String tiktokHandle = profilePage.userGetter(profilePage.tiktokLink());
		  profilePage.tiktokLink().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.numberOfWindowsToBe(2));
		  helpers.WindowUtil.switchToWindow(driver, 1);

		  Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains(tiktokHandle)), "Did not find Tiktok window");

		  driver.close();
		  helpers.WindowUtil.switchToWindow(driver, 0);
	  }
	  @Test(enabled = true, priority = 1)
	  public void TiktokBadURL() throws InterruptedException {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.tikTokInput().clear();
		  profile.tikTokInput().sendKeys("https://www.tiktok.com/qwerty/asdf");
		  profile.saveProfileBtn().click();
		 // Thread.sleep(10000);
		  Assert.assertTrue(helpers.Waiter.quickWait(driver).until(ExpectedConditions.visibilityOf(profile.tiktokBadURL())).isDisplayed(), "Should have rejected bad Tiktok link");

	  }
	  @Test(enabled = true, priority = 1)
	  public void TiktokUserHandle() throws InterruptedException {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.tikTokInput().clear();
		  profile.tikTokInput().sendKeys("@asdf");
		  profile.saveProfileBtn().click();
		  Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess())).isDisplayed());
	  }
	  @Test(enabled = true, priority = 1)
	  public void TikTokLinkWholeURL() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.tikTokInput().clear();
		  profile.tikTokInput().sendKeys("https://www.tiktok.com/@asdf");
		  profile.saveProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));

		  Assert.assertTrue(profilePage.tiktokLink().isDisplayed(), "Did not find tiktok button");

		  String tiktokHandle = profilePage.userGetter(profilePage.tiktokLink());
		  profilePage.tiktokLink().click();
		  helpers.WindowUtil.switchToWindow(driver, 1);

		  Assert.assertTrue(helpers.Waiter.wait(driver).until(ExpectedConditions.urlContains(tiktokHandle)), "Did not find Tiktok window");

		  driver.close();
		  helpers.WindowUtil.switchToWindow(driver, 0);
	  }
	  @Test(enabled = true, priority = 1)
	  public void TwitterBadURL() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.twitterInput().clear();
		  profile.twitterInput().sendKeys("https://www.twitter.com/qwerty/asdf");
		  profile.saveProfileBtn().click();
		  Assert.assertTrue(profile.twitterBadURL().isDisplayed(), "Should have rejected bad Twitter link");
	  }
	  @Test(enabled = true, priority = 1)
	  public void TwitterLink() throws InterruptedException {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.twitterInput().clear();
		  profile.twitterInput().sendKeys("asdf");
		  profile.saveProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));
		  Assert.assertTrue(profilePage.twitterLink().isDisplayed(), "Did not find Twitter icon");

		  profilePage.twitterLink().click();
		  Thread.sleep(3000);
		  helpers.WindowUtil.switchToWindow(driver, 1);
		  Assert.assertTrue(driver.getTitle().contains("Twitter"), "Did not find Twitter window");

		  driver.close();
		  helpers.WindowUtil.switchToWindow(driver, 0);
	  }
	  @Test(enabled = true, priority = 1)
	  public void TwitterLinkWholeURL() throws InterruptedException {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  profile.twitterInput().clear();
		  profile.twitterInput().sendKeys("https://www.twitter.com/asdf");
		  profile.saveProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOf(profile.updateSuccess()));
		  profile.userMenu().click();
		  profile.yourProfileBtn().click();
		  helpers.Waiter.wait(driver).until(ExpectedConditions.not(ExpectedConditions.urlContains("settings")));
		  Assert.assertTrue(profilePage.twitterLink().isDisplayed(), "Did not find Twitter icon");

		  profilePage.twitterLink().click();
		  Thread.sleep(3000);
		  helpers.WindowUtil.switchToWindow(driver, 1);
		  Assert.assertTrue(driver.getTitle().contains("Twitter"), "Did not find Twitter window");

		  driver.close();
		  helpers.WindowUtil.switchToWindow(driver, 0);
	  }
	  @Test(enabled = true, priority = 1)
	  public void WebsiteInputNotFound() {
		  profile.editProfileBtn().click();
		  profile.socialLinksTab().click();
		  try {
			  Assert.assertTrue(profile.websiteInput().isDisplayed());
			  Assert.fail();
			  System.out.println("Website input should not display for unverified user");
		  }catch(TimeoutException e) {
			  Assert.assertTrue(true);
		  }
	  }

}

