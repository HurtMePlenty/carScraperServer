package carScraperServer.scrapeEngine;

import carScraperServer.entities.ResultItem;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutotraderPageProcessor {
    PageLoader pageLoader;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AutotraderPageProcessor.class);
    private static int maxAttempts = 5;

    private Pattern mainInfoBlock = Pattern.compile(Pattern.quote("BIRF.extend(BIRF.data") + "[^;]+;");

    private Pattern makePattern = Pattern.compile("\"make\":\\[\"([^\"]+)\"\\]");
    private Pattern modelPattern = Pattern.compile("\"model\":\\[\"([^\"]+)\"\\]");
    private Pattern yearPattern = Pattern.compile("\"year\":\"([^\"]+)\"");
    private Pattern pricePattern = Pattern.compile("\"price\":\"([^\"]+)\"");
    private Pattern trimPattern = Pattern.compile("\"trim\":\\[\"([^\"]+)\"\\]");
    private Pattern colorPattern = Pattern.compile("\"color\":\\[\"([^\"]+)\"\\]");

    AutotraderPageProcessor(PageLoader pageLoader) {
        this.pageLoader = pageLoader;
    }

    public ResultItem process(String url) {
        return process(url, 0);
    }

    private ResultItem process(String url, int attempt) {
        try {

            String page = pageLoader.getPage(url);
            Document document = Jsoup.parse(page);

            ResultItem resultItem = new ResultItem();

            resultItem.setUrl(url);

            Elements elements = document.select("span#j_id_bd-j_id_181-j_id_1e3-j_id_1e5-j_id_1e6-j_id_1eq-j_id_1ey-vinInfoBlock");
            if (!elements.isEmpty()) {
                resultItem.setVin(elements.get(0).html());
            }

            elements = document.select("span.heading-mileage");
            if (!elements.isEmpty()) {
                String html = elements.get(0).html().split(" ")[0];
                html = html.replace(",", "");
                int mileage = Integer.parseInt(html);
                resultItem.setMileage(mileage);
            }

            Matcher matcher = mainInfoBlock.matcher(page);
            String mainInfoBlock = null;
            while (matcher.find()) {
                mainInfoBlock = matcher.group();
            }

            if (mainInfoBlock == null) {
                LOG.warn("Main info block wasn't found");
                return null;
            }

            String make = findInInfoBlock(makePattern, mainInfoBlock);
            String model = findInInfoBlock(modelPattern, mainInfoBlock);
            String year = findInInfoBlock(yearPattern, mainInfoBlock);
            String price = findInInfoBlock(pricePattern, mainInfoBlock);
            String trim = findInInfoBlock(trimPattern, mainInfoBlock);
            String color = findInInfoBlock(colorPattern, mainInfoBlock);


            if (StringUtils.isEmpty(make) || StringUtils.isEmpty(model)) {
                LOG.warn("Make or model wasn't found inside the mainInfoBlock. Probably wrong block.");
                return null;
            }

            resultItem.setMake(make);
            resultItem.setModel(model);
            if (StringUtils.isNotEmpty(year)) {
                resultItem.setYear(Integer.parseInt(year));
            }

            if (StringUtils.isNotEmpty(price)) {
                resultItem.setPrice(Double.parseDouble(price));
            }

            resultItem.setTrim(trim);
            resultItem.setColor(color);

            return resultItem;
        } catch (Exception e) {
            LOG.warn(String.format("Failed to load item with url %s \n Exception: %s", url, e.toString()));
            return null;
            //throw new RuntimeException(e);
        }
    }

    private String findInInfoBlock(Pattern pattern, String mainInfoBlock) {
        Matcher matcher = pattern.matcher(mainInfoBlock);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
