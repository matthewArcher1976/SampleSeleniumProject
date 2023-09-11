package resources;

import helpers.Drivers;
import org.openqa.selenium.WebDriver;

public class getDriverType {
    public static WebDriver getDriver(String driverType) {
        return switch (driverType) {
            case "ChromeDriver" -> Drivers.ChromeDriver();
            case "ChromeSauce" -> Drivers.ChromeSauce();
            case "ChromeInsecure" -> Drivers.ChromeInsecure();
            case "ChromeMobile" -> Drivers.ChromeMobile();
            case "SafariDriver" -> Drivers.SafariDriver();
            default -> throw new IllegalArgumentException("Driver type not supported: " + driverType);
        };
    }

}
