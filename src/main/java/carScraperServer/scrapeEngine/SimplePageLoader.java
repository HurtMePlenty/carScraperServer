package carScraperServer.scrapeEngine;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
        return getPage(urlString, null, null, 0);
    }

    @Override
    public String postPage(String urlString, Map<String, String> data, Map<String, String> headers) {
        return getPage(urlString, data, headers, 0);
    }

    private String getPage(String urlString, Map<String, String> data, Map<String, String> headers, int attempt) {
        try {

            URL obj = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            if (headers != null) {
                for (String header : headers.keySet()) {
                    con.setRequestProperty(header, headers.get(header));
                }
            }

            if (data != null) {
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                StringBuilder query = new StringBuilder();
                for (String key : data.keySet()) {
                    String value = data.get(key);
                    query.append(String.format("%s=%s&", key, value));
                }
                String queryStr = query.toString();
                queryStr = queryStr.replaceAll("&$", "");
                out.writeBytes(queryStr);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            InputStream in = con.getInputStream();

            int readed;
            //byte[] chunk = new byte[4096];
            byte[] chunk = new byte[65536];
            while ((readed = in.read(chunk)) > 0) {
                baos.write(chunk, 0, readed);
            }

            in.close();

            Map<String, String> cookieMap = new HashMap<>();
            byte[] pageData = baos.toByteArray();

            return new String(pageData);


        } catch (IOException e) {
            LOG.warn(String.format("Exception in page loader: %s", e.toString()));

            //handle 403? access denied error
            if (e.toString().contains("Server returned HTTP response code: 403 for URL")) {

            }

            if (attempt >= maxAttempts) {
                LOG.warn(String.format("Failed to load item for url: %s. Exception: %s", urlString, e.toString()));
                throw new RuntimeException(e);
            }

            return getPage(urlString, data, headers, attempt + 1);
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
