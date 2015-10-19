package carScraperServer;

import carScraperServer.scrapeEngine.PhantomInitializer;
import carScraperServer.services.CarsComScrapeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testDirectRequest() {
        try {

            SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder();
            searchRequestBuilder.setMakeId("20001");
            searchRequestBuilder.setModelId("20773");
            searchRequestBuilder.setZipCode("10001");

            URL url = new URL(searchRequestBuilder.renderUrl());
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.connect();

            StringBuilder page = new StringBuilder();
            String line;
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            while ((line = in.readLine()) != null) {
                page.append(line + "\n");
            }
            String pageStr = page.toString();

            Document document = Jsoup.parse(pageStr);
            Elements elems = document.select("div.row.vehicle h4 a");
            List<String> urls = new ArrayList();

            for (Element element : elems) {
                urls.add(element.attr("href"));
            }
            int a = 1;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
