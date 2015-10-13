package carScraperServer;

import carScraperServer.scrapeEngine.PhantomInitializer;
import carScraperServer.services.CarsComScrapeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class ScraperTest {

    @Autowired
    CarsComScrapeService carsComScrapeService;

    @Test
    public void testScrape() throws Exception {
        carsComScrapeService.execute();
    }

    @Test
    public void testPhantom() {
        WebDriver driver = PhantomInitializer.instance.getDriver(null, null, "src/main/webapp/phantomjs");
        driver.get("http://www.cars.com");
        //driver.get("http://www.whoer.net");

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        System.out.println(scrFile.getPath());
    }
}
