package carScraperServer.scrapeEngine;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

@Component
public class TorPageLoader {

    private int requestsPerIP;
    private volatile int requestsMade;
    private int connectionTimeout;

    private Object lock = new Object();

    @PostConstruct
    private void init() {
        TorProxyService.instance.establishTor();
    }


    public String getPage(String urlString) {
        try {
            synchronized (lock) {
                if (requestsMade++ > requestsPerIP) {
                    TorProxyService.instance.establishTor();
                    requestsMade = 0;
                }
            }

            String proxyIp = TorProxyService.instance.getProxyIp();
            int proxyPort = TorProxyService.instance.getProxyPort();
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyIp, proxyPort));
            URL url = new URL(urlString);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
            uc.setConnectTimeout(connectionTimeout);
            uc.connect();

            StringBuilder page = new StringBuilder();
            String line;
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            while ((line = in.readLine()) != null) {
                page.append(line + "\n");
            }
            return page.toString();

        } catch (SocketException e) {
            TorProxyService.instance.establishTor();
            requestsMade = 0;
            return getPage(urlString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Required
    public void setRequestsPerIP(int requestsPerIP) {
        this.requestsPerIP = requestsPerIP;
    }

    @Required
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
