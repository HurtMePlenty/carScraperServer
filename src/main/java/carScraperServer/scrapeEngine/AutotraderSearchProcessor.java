package carScraperServer.scrapeEngine;

import carScraperServer.entities.ResultItem;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class AutotraderSearchProcessor implements CarsSearchProcessor {
    private int threads;
    private boolean isFinished;
    private String searchUrl;
    private PageLoader pageLoader;
    private AutotraderPageProcessor autotraderPageProcessor;
    private UserSearchQuery userSearchQuery;
    private volatile List<ResultItem> resultItemList = new CopyOnWriteArrayList<>();
    private ExecutorService mainExecutor;
    private Semaphore semaphore;
    private boolean isError = false;
    private Integer totalItemsExpected;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AutotraderSearchProcessor.class);

    public AutotraderSearchProcessor(int threads, UserSearchQuery userSearchQuery, AdditionalSearchParams additionalSearchParams, PageLoader pageLoader) {
        this.threads = threads;
        this.semaphore = new Semaphore(threads);
        this.userSearchQuery = userSearchQuery;

        AutotraderRequestBuilder autotraderRequestBuilder = new AutotraderRequestBuilder();
        autotraderRequestBuilder.setMakeId(AutotraderSearchHelper.getMakeIdByName(userSearchQuery.getMake()));
        autotraderRequestBuilder.setModelId(AutotraderSearchHelper.getModelIdByName(userSearchQuery.getModel()));
        autotraderRequestBuilder.setZipCode(userSearchQuery.getZipCode());
        autotraderRequestBuilder.setYear(userSearchQuery.getYear());

        if (userSearchQuery.getPrice() != null) {
            Double maxPrice = userSearchQuery.getPrice() + additionalSearchParams.getPriceSpread();
            autotraderRequestBuilder.setMaxPrice(maxPrice);

            Double minPrice = userSearchQuery.getPrice() - additionalSearchParams.getPriceSpread();
            if (minPrice > 0) {
                autotraderRequestBuilder.setMinPrice(minPrice);
            }
        }

        String searchUrl = autotraderRequestBuilder.renderUrl();

        this.pageLoader = pageLoader;
        this.autotraderPageProcessor = new AutotraderPageProcessor(pageLoader);

        this.searchUrl = searchUrl;
    }

    @Override
    public void startScraping(Consumer<CarsSearchProcessor> callback) {
        try {

            Document doc = loadSearchResultsPage(searchUrl);
            //Elements carNodes = j_id_1_3wu_6


            Elements totalItems = doc.select("span.num-listings");
            String html = totalItems.get(0).html(); //always not empty because it's a condition in loadSearchResultPage method
            if (StringUtils.isNotEmpty(html)) {
                totalItemsExpected = Integer.parseInt(html.replace(",", ""));
                LOG.info(String.format("%s Items expected: %d", userSearchQuery.getQueryToken(), totalItemsExpected));
            }


            int currentPage = 0;
            while (true) {
                Elements carNodes = doc.select("span#j_id_1_3wu_6 h2 a");
                if (carNodes.isEmpty()) {
                    break;
                }

                mainExecutor = Executors.newFixedThreadPool(threads);

                for (Element carNode : carNodes) {
                    String carUrl = carNode.attr("href");

                    semaphore.acquire();

                    mainExecutor.execute(() -> {
                        System.out.println("item requested");
                        ResultItem resultItem = autotraderPageProcessor.process("http://www.autotrader.com" + carUrl);
                        if (resultItem != null) {
                            resultItemList.add(resultItem);
                            System.out.println("item added");
                        } else {
                            System.out.println("item is NULL");
                        }
                        semaphore.release();
                    });
                }

                mainExecutor.shutdown();
                mainExecutor.awaitTermination(600, TimeUnit.SECONDS);


                int firstRecord = ++currentPage * 100 + 1; //use first record to navigate through pages
                String nextPageUrl = String.format("%s&firstRecord=%d", searchUrl, firstRecord);
                doc = loadSearchResultsPage(nextPageUrl);

            }

        } catch (Exception e) {
            LOG.error("Error: " + e.toString());
            this.isError = true;
            throw new RuntimeException(e);
        } finally {
            isFinished = true;
            callback.accept(this);
        }
    }

    private Document loadSearchResultsPage(String url) throws InterruptedException {
        int currentAttempt = 0;
        while (currentAttempt < 3) {
            String searchResult = pageLoader.getPage(searchUrl);
            Document doc = Jsoup.parse(searchResult);

            Elements totalItems = doc.select("span.num-listings");
            if (totalItems.size() > 0) {
                return doc;
            }
            currentAttempt++;
            Thread.sleep(1000);
        }
        throw new RuntimeException(String.format("Was unable to load search result page correctly: %s", url));
    }

    @Override
    public UserSearchQuery getUserSearchQuery() {
        return userSearchQuery;
    }

    @Override
    public List<ResultItem> getResultItemList() {
        return resultItemList;
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    public boolean isError() {
        return isError;
    }
}
