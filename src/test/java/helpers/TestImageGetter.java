package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import resources.Drivers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class TestImageGetter {

    private final WebDriver driver = Drivers.ChromeDriver();
    @SuppressWarnings("unused")
    @Test(enabled = false)
    public void imageGetter() throws InterruptedException {
        driver.get("https://www.reddit.com/r/wtfstockphotos/");
        PageActions.scrollDown(driver, 4);
        Thread.sleep(10);
        List<WebElement> posts = driver.findElements(By.cssSelector("img[role='presentation']"));
        System.out.println(posts.size() + " posts found");
        int i = 1;
        for (WebElement post:posts){
            String imageUrl = post.getAttribute("src");
            try {
                URL url = new URL(imageUrl);
                try (InputStream inputStream = url.openStream()) {
                    Path localFilePath = Path.of("/Users/mattarcher/Downloads/stockdownloads/stock" + Randoms.getRandomString(15) +".jpg");
                    // Save the image to the local directory
                    Files.copy(inputStream, localFilePath, StandardCopyOption.ATOMIC_MOVE);
                    System.out.println("Image saved successfully to: " + localFilePath);
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
