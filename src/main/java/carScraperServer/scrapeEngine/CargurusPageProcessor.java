package carScraperServer.scrapeEngine;


import carScraperServer.entities.ResultItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CargurusPageProcessor {
    private PageLoader pageLoader;

    private Pattern imagePattern = Pattern.compile("activateGalleryPicture[^']+'([^']+)'\\)");

    public CargurusPageProcessor(PageLoader pageLoader) {
        this.pageLoader = pageLoader;
    }

    public ResultItem process(String url, ResultItem resultItem) {
        String page = pageLoader.getPage(url);
        Document document = Jsoup.parse(page);

        Elements elements = document.select("td.attributeLabel");
        for (Element element : elements) {
            if (element.text().equals("VIN:")) {
                String vin = element.nextElementSibling().text();
                resultItem.setVin(vin);
                break;
            }
        }

        Matcher imageMatcher = imagePattern.matcher(page);
        while (imageMatcher.find()) {
            String imageUrl = imageMatcher.group(1);
            resultItem.getImageUrls().add(imageUrl);
        }

        return resultItem;
    }
}
