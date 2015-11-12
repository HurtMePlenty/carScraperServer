package carScraperServer.scrapeEngine;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@Component
public class TorPageLoader implements PageLoader {

    private int requestsPerIP;
    private volatile int requestsMade;
    private int connectionTimeout;
    private volatile boolean reInitCauseOfFail;

    private Object lock = new Object();
    private final int maxAttempts = 5;

    private final int BUFFER_SIZE = 1024;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TorPageLoader.class);

    @PostConstruct
    private void init() {
        TorProxyService.instance.establishTor();
    }


    @Override
    public String getPage(String urlString) {
        return getPage(urlString, 0);
    }

    private String getPage(String urlString, int attempt) {
        try {
            HttpURLConnection uc;
            URL url = new URL(urlString);


            synchronized (lock) {
                if (requestsMade >= requestsPerIP || reInitCauseOfFail) {
                    TorProxyService.instance.establishTor();
                    requestsMade = 0;
                    if (reInitCauseOfFail) {
                        Thread.sleep(connectionTimeout);
                    }
                    reInitCauseOfFail = false;
                }
            }

            requestsMade++;

            String proxyIp = TorProxyService.instance.getProxyIp();
            int proxyPort = TorProxyService.instance.getProxyPort();
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyIp, proxyPort));

            uc = (HttpURLConnection) url.openConnection(proxy);


            uc.setConnectTimeout(connectionTimeout);
            uc.setReadTimeout(connectionTimeout);
            uc.connect();

            StringBuilder page = new StringBuilder();

            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            /*while ((line = in.readLine()) != null) {
                page.append(line).append("\n");
            }*/


            char[] buffer = new char[BUFFER_SIZE]; // or some other size,
            int charsRead;
            while ((charsRead = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                page.append(buffer, 0, charsRead);
            }

            in.close();

            return page.toString();

        } catch (SocketException e) {
            LOG.warn(String.format("Exception in page loader: %s", e.toString()));
            reInitCauseOfFail = true;
            return getPage(urlString, attempt);

        } catch (SocketTimeoutException e) {
            LOG.warn(String.format("Exception in page loader: %s", e.toString()));
            if (attempt >= maxAttempts) {
                LOG.warn(String.format("Failed to load item for url: %s. Exception: %s", urlString, e.toString()));
                throw new RuntimeException(e);
            }
            return getPage(urlString, attempt + 1);
        } catch (IOException e) {
            LOG.warn(String.format("Exception in page loader: %s", e.toString()));

            if (attempt >= maxAttempts) {
                LOG.warn(String.format("Failed to load item for url: %s. Exception: %s", urlString, e.toString()));
                throw new RuntimeException(e);
            }
            reInitCauseOfFail = true;
            return getPage(urlString, attempt + 1);
        } catch (Exception e) {
            LOG.warn(String.format("Exception in page loader: %s", e.toString()));
            LOG.warn(String.format("Failed to load item for url: %s. Exception: %s", urlString, e.toString()));

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
