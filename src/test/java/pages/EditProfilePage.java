package pages;

import helpers.Waiter;
import io.github.sukgu.Shadow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

@SuppressWarnings("unused")
public class EditProfilePage {

    private final WebDriver driver;

    public EditProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    //***********************  Web Elements  **********************


    public WebElement accountTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-id='account-tab-button']")));
    }

    public WebElement addMembershipBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("add-membership")));
    }

    public WebElement amazonInvalid() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'The wishlist url must be a valid URL.')]")));
    }

    public WebElement amazonEditBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("edit-wishlist-url")));
    }

    public WebElement amazonEditBtnCancel() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("cancel-edit-wishlist-url")));
    }

    public WebElement amazonInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("wishlist_url")));
    }

    public WebElement amazonInputInactive() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[contains(@placeholder,'wantfor.me')]")));
    }

    public WebElement amazonTooltip() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-tip='Click to Copy to Clipboard']")));
    }

    public WebElement amazonShortLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-tip='Click to Copy to Clipboard']")));
    }

    public WebElement autoRenewToggle() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("toggler")));
    }

    public WebElement bannerEditBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("update-cover-image-button")));
    }

    public WebElement bannerInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("profileCover")));
    }

    public WebElement bannerPic() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("profile-cover")));
    }

    public WebElement basicInfoTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-id='basic-info-tab-button']")));
    }

    public WebElement birthDayValue() {
        Shadow shadow = new Shadow(driver);
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span[aria-label='Day']")));
    }

    public WebElement birthDayInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("date-dob")));
    }

    public WebElement birthDayDay(String day) {
        String xpathSelector = "//div[contains(@class, 'dp__cell_inner') and text()='" + day + "']";
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathSelector)));
    }

    public WebElement birthDayMonth(String month) {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-test='" + month + "']")));
    }

    public WebElement birthDayYear(String year) {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-test='" + year + "']")));
    }

    public WebElement chapterSelect() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("chiveChapter")));
    }
    public WebElement deleteAccountLink() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span[class='link']")));
    }
    public WebElement deleteCloseModelBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-xmark")));
    }
    public WebElement deleteConfirmBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("modal-confirm-button")));
    }
    public WebElement deleteNeverMindBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("modal-cancel-button")));
    }
    public WebElement editProfileBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("edit-profile-button")));
    }
    public WebElement emailCharities() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("chive_charities-enable")));
    }
    public WebElement emailChiveNation() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("chive_nation-enable")));
    }
    public WebElement emailHotnessDaily() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("thechive_hotness-daily")));
    }

    public WebElement emailHotnessText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(), \"Turn up your inbox's temperature with the hottest galleries of Mind and Gaps, FLBPs, and more! Is it hot in here? Yes. It is.\")]")));
    }

    public WebElement emailHotnessWeekly() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("thechive_hotness-weeklyKey")));
    }

    public WebElement emailHumanityDaily() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("thechive_humanity-daily")));
    }

    public WebElement emailHumanityText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'Wanna lift your spirits on the regular? This weekly email comes with a big helping of The Warm Fuzzies on the side.')]")));
    }

    public WebElement emailHumanityWeekly() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("thechive_humanity-weeklyKey")));
    }

    public WebElement emailHumorDaily() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("thechive_humor-daily")));
    }

    public WebElement emailHumorText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'We mix up the best comedy posts of the day in a sort of gumbo of LMAO, LOL with even some HAHA WTF?!? sprinkled in.')]")));
    }

    public WebElement emailHumorWeekly() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("thechive_humor-weeklyKey")));
    }

    public WebElement emailInvalid() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'The email must be a valid email address.')]")));
    }

    public WebElement emailInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
    }

    public WebElement emailNewsLetterDaily() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("thechive-daily")));
    }

    public WebElement emailNewsLetterText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'Let our crack team of wise-asses find the best of the net and deliver it to your inbox on a platter of rich mahogany.')]")));
    }

    public WebElement emailNewsLetterWeekly() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("thechive-weeklyKey")));
    }

    public WebElement emailRequired() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'The email field is required.')]")));
    }

    public WebElement emailTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Email Preferences")));
    }
    public WebElement emailWilliamMurray() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("wmg-enable")));
    }
    public WebElement facebookBadURL() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'The facebook format is invalid.')]")));
    }

    public WebElement facebookInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("facebook")));
    }

    public WebElement firstNameInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("firstname")));
    }

    public WebElement genderDropdown() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("gender")));
    }

    public WebElement genderMale() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("option[value='male']")));
    }

    public WebElement genderFemale() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("option[value='female']")));
    }

    public WebElement genderOther() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("option[value='other']")));
    }

    public Select genderSelect() {
        return new Select(helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("gender"))));
    }

    public WebElement headerSubText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains(text(),' we can make your inbox a lot more fun! ')]")));
    }

    public WebElement headerText() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),' Not pumped to check your email? Well, my friend ')]")));
    }

    public String hotnessText() {
        return "Show me Hotness in theCHIVE App";
    }

    public WebElement hotnessToggle() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("allow-nsfw-content")));
    }

    public WebElement instagramBadURL() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'The instagram format is invalid.')]")));
    }

    public WebElement instagramInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("instagram")));
    }

    public WebElement lastNameInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("lastname")));
    }

    public WebElement makePrivateAcceptBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("modal-confirm-button")));
    }

    public WebElement makePrivateNvmBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("modal-cancel-button")));
    }

    public WebElement makePrivateWarningPopup() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h3[id^='headlessui-dialog-panel']")));
    }//TODO - get that warning text too

    public WebElement makePrivateTitle() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()=' Confirmation ']")));
    }

    public WebElement makePrivateToggle() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("private")));
    }

    public WebElement membershipActiveTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[heading='Active']")));
    }

    public WebElement membershipAddCardBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.elementToBeClickable(By.className("btn-add-card")));
    }

    public WebElement membershipCreditCardCVV() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("cvv")));
    }

    public WebElement membershipCreditCardExp() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='expiration']")));
    }

    public WebElement membershipCreditCardHolder() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='cardholder']")));
    }

    public WebElement membershipCreditCardNumber() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[title='credit card number']")));
    }

    public WebElement membershipCreditCardsTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[data-item='wallet']")));
    }

    public WebElement membershipCreditCardZip() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='zipcode']")));
    }

    public WebElement membershipExpiredTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[heading='Expired']")));
    }

    public WebElement membershipManageDropdown() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul[class='manage-dropdown']")));
    }

    public WebElement membershipPaymentsTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'Payments & history')]")));
    }

    public WebElement membershipSubscriptionsTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'Subscriptions')]")));
    }

    public WebElement membershipTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-gear")));
    }

    public WebElement membershipUpdatePayment() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'Update renewal payment method')]")));
    }

    public WebElement notificationsTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-id='notifications-tab-button']")));
    }

    public WebElement notificationsToggle() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("subscribed-to-digest")));
    }

    public WebElement password() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
    }

    public WebElement passwordError() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='The password confirmation does not match.']")));
    }

    public WebElement passwordVerify() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("password-confirmation")));
    }

    public WebElement profilePic() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("profile-picture")));
    }

    public WebElement profilePicInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("profilePicture")));
    }

    public WebElement privacyDefHeader() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[contains(text(),' Privacy Definitions ')]")));
    }

    public WebElement privacyDefSubHeader() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),' Confused about what your privacy settings mean? Take a look at our definitions to help make your decision. ')]")));
    }

    public WebElement privacyInfoIcon() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("fa-circle-info")));
    }
    public By privacyInfoIconBy() {
        return By.className("fa-circle-info");
    }
    public WebElement privacyPrivateBold() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//strong[contains(text(),'Features lost by having a private profile:')]")));
    }

    public WebElement privacyPrivateHeader() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//strong[contains(text(),'Private')]")));
    }

    public WebElement privacyPrivateBullet1() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(),'Your iCHIVE profile is not accessible by other users')]")));
    }

    public WebElement privacyPrivateBullet2() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(),'You cannot earn CHIVE Points for your activity on iCHIVE')]")));
    }

    public WebElement privacyPrivateBullet3() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(),' Users cannot follow your profile and any current followers will be removed on changing to private ')]")));
    }

    public WebElement privacyPrivateBullet4() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(),' Except as stated above, your submissions are fully subject to the Terms of Use and Privacy Policy ')]")));
    }

    public WebElement privacyPrivateSubHeader() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#app main article div div.flex.md\\:ml-4.w-full.bg-base-100.card.card-compact form div div div.w-full p:nth-child(5)")));
    }

    public WebElement privacyPublicBold() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//strong[contains(text(),'Advantages gained from having a public profile:')]")));
    }

    public WebElement privacyPublicBullet1() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(),'We credit your profile directly when your submissions are posted on theCHIVE')]")));
    }

    public WebElement privacyPublicHeader() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//strong[contains(text(),'Public')]")));
    }

    public WebElement privacyPublicBullet2() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(),'Your submissions are eligible to be featured in the Dopamine Dump')]")));
    }

    public WebElement privacyPublicBullet3() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(),'Your activity on myCHIVE will earn CHIVE Points that you can redeem on The Chivery')]")));
    }

    public WebElement privacyPublicBullet4() {
        return helpers.Waiter.quickWait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(),'Earn badges, amass an army of followers, and dominate the Internet')]")));
    }

    public WebElement privacyPublicSubHeader() {
        return driver.findElement(By.cssSelector("#app > main > article > div > div.flex.md\\:ml-4.w-full.bg-base-100.card.card-compact > form > div > div > div.w-full > p:nth-child(3)"));
    }

    public WebElement profileTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-id='profile-tab-button']")));
    }

    public String  privacyToggleText() {
        return "Make my profile private";
    }

    public WebElement saveEmailPrefBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("save-email-preferences")));
    }

    public WebElement saveProfileBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("save-profile-button")));
    }

    public WebElement settingsBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Settings")));
    }

    public WebElement socialLinksTab() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-id='social-tab-button']")));
    }

    public WebElement tiktokBadURL() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'The tiktok format is invalid.')]")));
    }

    public WebElement tikTokInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("tiktok")));
    }

    public WebElement tipURLInput(){
        return Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("tip_url")));
    }
    public WebElement twitterBadURL() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'The twitter format is invalid.')]")));
    }

    public WebElement twitterInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("twitter")));
    }

    public WebElement updateProfilePicBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("update-profile-picture-button")));
    }

    public WebElement updateSuccess() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("alert-success")));
    }
    public By updateSuccessBy() {
        return By.className("alert-success");
    }

    public WebElement uploadFailToast() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("Vue-Toastification__toast--error")));
    }

    public WebElement userMenu() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("avatar")));
    }

    public WebElement userName() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
    }

    public WebElement verifiedCheck() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("label-verified")));
    }

    public WebElement websiteInput() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("website")));
    }

    public WebElement yourProfileBtn() {
        return helpers.Waiter.wait(driver).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Your Profile")));
    }


    //***************************** Actions *****************************

    public void birthdayPickYear(String randomYear) {

        List<WebElement> divElements = driver.findElements(By.className("dp__overlay_cell"));
        WebElement yearElement;
        for (WebElement divElement : divElements) {
            String divText = divElement.getText();
            if (divText.contains(randomYear)) {
                yearElement = divElement;
                yearElement.click();
                break;
            }
        }
    }

    public String monthToNumber(String month) {
        return switch (month) {
            case "Jan" -> "01";
            case "Feb" -> "02";
            case "Mar" -> "03";
            case "Apr" -> "04";
            case "May" -> "05";
            case "Jun" -> "06";
            case "Jul" -> "07";
            case "Aug" -> "08";
            case "Sep" -> "09";
            case "Oct" -> "10";
            case "Nov" -> "11";
            case "Dec" -> "12";
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }


}
