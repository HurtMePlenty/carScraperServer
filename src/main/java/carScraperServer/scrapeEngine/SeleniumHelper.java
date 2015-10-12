package carScraperServer.scrapeEngine;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumHelper {
    public static WebElement waitForElement(WebDriver webDriver, By by, long delay, int attempts) {
        try {
            WebElement result = null;
            while (result == null && attempts-- > 0) {
                result = findByOrNull(webDriver, by);
                if (result == null) {
                    Thread.sleep(delay);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static WebElement findByOrNull(WebDriver webDriver, By by) {
        try {
            return webDriver.findElement(by);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
