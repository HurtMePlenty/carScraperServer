package carScraperServer.scrapeEngine;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@Component
public class TorPageLoader {

    private int requestsPerIP;
    private volatile int requestsMade;
    private int connectionTimeout;
    private boolean useTor;

    private Object lock = new Object();

    @PostConstruct
    private void init() {
        TorProxyService.instance.establishTor();
    }


    public String getPage(String urlString) {
        try {
            HttpURLConnection uc;
            URL url = new URL(urlString);

            if (useTor) {
                synchronized (lock) {
                    if (requestsMade >= requestsPerIP) {
                        TorProxyService.instance.establishTor();
                        requestsMade = 0;
                    }
                }

                requestsMade++;

                String proxyIp = TorProxyService.instance.getProxyIp();
                int proxyPort = TorProxyService.instance.getProxyPort();
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyIp, proxyPort));

                uc = (HttpURLConnection) url.openConnection(proxy);
            } else {
                uc = (HttpURLConnection) url.openConnection();
            }

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
            if (useTor) {
                return getPage(urlString);
            }
            throw new RuntimeException(e);
        } catch (IOException e) {
            if (useTor) {
                return getPage(urlString);
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Required
    public void setRequestsPerIP(int requestsPerIP) {
        this.requestsPerIP = requestsPerIP;
    }

    @Required
    public void setUseTor(boolean useTor) {
        this.useTor = useTor;
    }

    @Required
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
