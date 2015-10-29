package carScraperServer;

import carScraperServer.entities.ResultItem;
import carScraperServer.httpresults.JsonResult;
import carScraperServer.scrapeEngine.*;
import carScraperServer.services.CarsScrapeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedReader;
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
    CarsScrapeService carsScrapeService;

    @Autowired
    TorPageLoader torPageLoader;

    @Autowired
    SimplePageLoader simplePageLoader;


    @Test
    public void testScraper() {

        UserSearchQuery userSearchQuery = new UserSearchQuery();
        userSearchQuery.setMake("BMW");
        userSearchQuery.setYear(2011);
        userSearchQuery.setModel("X3");
        userSearchQuery.setZipCode(92627);
        //userSearchQuery.setZipCode(92626);
        //userSearchQuery.setZipCode(10001);

        carsScrapeService.execute(userSearchQuery);

        try {
            Thread.sleep(10000000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //carsComScrapeService.execute()
    }

    @Test
    public void testAutotraderScraper() throws InterruptedException {
        UserSearchQuery userSearchQuery = new UserSearchQuery();
        userSearchQuery.setMake("BMW");
        userSearchQuery.setYear(2011);
        userSearchQuery.setModel("X3");
        userSearchQuery.setZipCode(92627);

        Object lock = new Object();

        AdditionalSearchParams additionalSearchParams = new AdditionalSearchParams(1000.0);

        AutotraderSearchProcessor autotraderSearchProcessor = new AutotraderSearchProcessor(1, userSearchQuery, additionalSearchParams, simplePageLoader);
        autotraderSearchProcessor.startScraping((processor) -> {
            List<ResultItem> resultItemList = processor.getResultItemList();
            System.out.println(String.format("Collected: %d", resultItemList.size()));
            synchronized (lock) {
                lock.notifyAll();
            }
        });
        synchronized (lock) {
            lock.wait();
        }

    }

    @Test
    public void testDataFind() {
        UserSearchQuery userSearchQuery = new UserSearchQuery();
        userSearchQuery.setMake("BMW");
        userSearchQuery.setYear(2011);
        userSearchQuery.setModel("X3");
        userSearchQuery.setZipCode(92626);

        JsonResult jsonResult = carsScrapeService.renderResponseFromDB(userSearchQuery);
        int a = 1;
    }

    @Test
    public void testBasicQuery() {
        String removeQuery = "{'vin':{'$in':['sdfsf','sdfsdf','sdfsdf']}}";
        BasicQuery basicQuery = new BasicQuery(removeQuery);
        int a = 1;
    }

    @Test
    public void testDirectRequest() {
        try {

            CarsComSearchRequestBuilder carsComSearchRequestBuilder = new CarsComSearchRequestBuilder();
            carsComSearchRequestBuilder.setMakeId("20001");
            carsComSearchRequestBuilder.setModelId("20773");
            carsComSearchRequestBuilder.setZipCode(10001l);

            URL url = new URL(carsComSearchRequestBuilder.renderUrl());
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
