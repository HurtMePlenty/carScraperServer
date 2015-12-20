package carScraperServer;

import carScraperServer.scrapeEngine.TorPageLoader;
import carScraperServer.scrapeEngine.TorProxyService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.management.ManagementFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class TorTest {

    @Autowired
    TorPageLoader torPageLoader;

    @Test
    public void testTor() throws InterruptedException {

        int successfullConnection = 0;
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        int mb = 1024 * 1024;
        Runtime instance = Runtime.getRuntime();
        torPageLoader.setRequestsPerIP(1);
        //torPageLoader.setConnectionTimeout(99999999);
        for (int i = 0; i < 99999; i++) {
            String page = torPageLoader.getPage("http://whoer.net");
            Document doc = Jsoup.parse(page);
            String myIp = doc.select("strong.your-ip").get(0).html();
            System.out.println(myIp);
            System.out.println(++successfullConnection);
            System.out.println(String.format("Threads before stop: %d", Thread.getAllStackTraces().keySet().size()));
            TorProxyService.instance.stopTor();
            /*while (!CustomNetlibProxy.isReallyStopped()) {
                System.out.println("Waiting until threads are dead...");
                Thread.sleep(1000);
            }*/
            System.out.println(String.format("Threads after stop: %d", Thread.getAllStackTraces().keySet().size()));

            // available memory
            System.out.println("Total Memory: " + instance.totalMemory() / mb);

            // free memory
            System.out.println("Free Memory: " + instance.freeMemory() / mb);

            // used memory
            System.out.println("Used Memory: "
                    + (instance.totalMemory() - instance.freeMemory()) / mb);
        }


        Thread.sleep(5000);

        int a = 1;
    }

    public void testTorMultithread() throws InterruptedException {
        int mb = 1024 * 1024;
        Runtime instance = Runtime.getRuntime();
        torPageLoader.setRequestsPerIP(1);
        //torPageLoader.setConnectionTimeout(99999999);
        for (int i = 0; i < 99999; i++) {
            String page = torPageLoader.getPage("http://whoer.net");
            Document doc = Jsoup.parse(page);
            String myIp = doc.select("#remote_addr").get(0).html();
            System.out.println(myIp);


            // available memory
            System.out.println("Total Memory: " + instance.totalMemory() / mb);

            // free memory
            System.out.println("Free Memory: " + instance.freeMemory() / mb);

            // used memory
            System.out.println("Used Memory: "
                    + (instance.totalMemory() - instance.freeMemory()) / mb);
        }


        Thread.sleep(5000);

        int a = 1;
    }

}
