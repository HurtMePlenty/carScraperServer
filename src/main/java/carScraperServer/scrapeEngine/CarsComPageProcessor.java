package carScraperServer.scrapeEngine;

import carScraperServer.entities.ResultItem;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;


public class CarsComPageProcessor {

    TorPageLoader torPageLoader;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CarsComPageProcessor.class);

    CarsComPageProcessor(TorPageLoader torPageLoader) {
        this.torPageLoader = torPageLoader;
    }

    public ResultItem process(String url) {
        try {
            ResultItem resultItem;

            String page = torPageLoader.getPage(url);
            Document document = Jsoup.parse(page);

            Elements elements = document.select("a.js-send-to-phone");
            if (elements.size() == 0) {
                return null;
            }

            Element dataElem = elements.get(0);

            resultItem = new ResultItem();
            resultItem.setUrl(url);
            resultItem.setMake(dataElem.attr("data-make-name"));
            resultItem.setModel(dataElem.attr("data-model-name"));
            String year = dataElem.attr("data-year");
            if (StringUtils.isNotEmpty(year)) {
                resultItem.setYear(Integer.parseInt(year));
            }

            resultItem.setTrim(dataElem.attr("data-trim"));
            String price = dataElem.attr("data-price");
            if (StringUtils.isNotEmpty(price)) {
                resultItem.setPrice(Double.parseDouble(price));
            }

            elements = document.select("ul.vehicle-details li strong");
            for (Element element : elements) {
                if (element.html().equals("VIN")) {
                    String vin = element.parent().nextElementSibling().html().trim();
                    resultItem.setVin(vin);
                } else if (element.html().equals("Exterior Color")) {
                    String color = element.parent().nextElementSibling().html().trim();
                    resultItem.setColor(color);
                } else if (element.html().equals("Mileage")) {
                    String mileage = element.parent().nextElementSibling().html().trim().replaceAll(",", "");
                    if (StringUtils.isNotEmpty(mileage)) {
                        resultItem.setMileage(Integer.parseInt(mileage));
                    }
                }
            }

            return resultItem;
        } catch (Exception e) {
            LOG.warn(String.format("Failed to load item with url %s \n Exception: %s", url, e.toString()));
            return null;
            //throw new RuntimeException(e);
        }
    }
}
