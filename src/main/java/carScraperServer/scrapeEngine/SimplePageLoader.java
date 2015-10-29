package carScraperServer.scrapeEngine;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

@Component
public class SimplePageLoader implements PageLoader {

    private Proxy.Type proxyType;
    private String proxyIp;
    private Integer proxyPort;
    private int connectionTimeout;

    private final int maxAttempts = 1;

    private final int BUFFER_SIZE = 1024;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SimplePageLoader.class);

    @Override
    public String getPage(String urlString) {
        return getPage(urlString, 0);
    }

    private String getPage(String urlString, int attempt) {
        try {
            HttpURLConnection uc;
            URL url = new URL(urlString);

            if (proxyType != null) {
                Proxy proxy = new Proxy(proxyType, new InetSocketAddress(proxyIp, proxyPort));

                uc = (HttpURLConnection) url.openConnection(proxy);
            } else {
                uc = (HttpURLConnection) url.openConnection();
            }


            uc.setConnectTimeout(connectionTimeout);
            uc.setReadTimeout(connectionTimeout);
            uc.connect();

            StringBuilder page = new StringBuilder();
            String line;
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            while ((line = in.readLine()) != null) {
                page.append(line).append("\n");
            }


            char[] buffer = new char[BUFFER_SIZE]; // or some other size,
            int charsRead;
            while ((charsRead = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                page.append(buffer, 0, charsRead);
            }

            in.close();

            return page.toString();

        } catch (IOException e) {
            LOG.warn(String.format("Exception in page loader: %s", e.toString()));

            //handle 403? access denied error
            if(e.toString().contains("Server returned HTTP response code: 403 for URL")){

            }

            if (attempt >= maxAttempts) {
                LOG.warn(String.format("Failed to load item for url: %s. Exception: %s", urlString, e.toString()));
                throw new RuntimeException(e);
            }

            return getPage(urlString, attempt + 1);
        } catch (Exception e) {
            LOG.warn(String.format("Exception in page loader: %s", e.toString()));
            LOG.warn(String.format("Failed to load item for url: %s. Exception: %s", urlString, e.toString()));

            throw new RuntimeException(e);
        }
    }


    public void setProxy(String proxy) { //format: 127.0.0.1:1080|socks5
        try {
            if (StringUtils.isNotEmpty(proxy)) {
                String proxyTypeStr = proxy.split("\\|")[1];
                if ("socks5".equals(proxyTypeStr) || "socks4".equals(proxyTypeStr)) {
                    this.proxyType = Proxy.Type.SOCKS;
                } else if ("http".equals(proxyTypeStr)) {
                    this.proxyType = Proxy.Type.HTTP;
                } else {
                    throw new RuntimeException("Unknown proxy type. Allowed types: socks5, socks4, http");
                }
                this.proxyIp = proxy.split(":")[0];
                this.proxyPort = Integer.parseInt(proxy.split("\\|")[0].split(":")[1]);
            }
        } catch (Exception e) {
            throw new RuntimeException("Was unable to parse proxy string", e);
        }
    }

    @Required
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
