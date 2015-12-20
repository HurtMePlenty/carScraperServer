package carScraperServer.scrapeEngine;

import java.util.Map;

public interface PageLoader {
    public String getPage(String urlString);
    public String postPage(String urlString, Map<String, String> data, Map<String, String> headers);
}
