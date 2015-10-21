package carScraperServer.scrapeEngine;

import carScraperServer.entities.ResultItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class CarsComSearchProcessor implements CarsSearchProcessor {
    private int threads;
    private boolean isFinished;
    private String searchUrl;
    private TorPageLoader torPageLoader;
    private CarsComPageProcessor carsComPageProcessor;
    private UserSearchQuery userSearchQuery;
    private volatile List<ResultItem> resultItemList = new CopyOnWriteArrayList<>();
    private ExecutorService mainExecuter;
    private Semaphore semaphore;
    private boolean isError = false;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CarsComSearchProcessor.class);


    public CarsComSearchProcessor(int threads, UserSearchQuery userSearchQuery, AdditionalSearchParams additionalSearchParams, TorPageLoader torPageLoader) {
        this.threads = threads;
        this.semaphore = new Semaphore(threads);
        this.userSearchQuery = userSearchQuery;

        CarsComSearchRequestBuilder carsComSearchRequestBuilder = new CarsComSearchRequestBuilder();
        carsComSearchRequestBuilder.setMakeId(CarsComSearchHelper.getMakeIdByName(userSearchQuery.getMake()));
        carsComSearchRequestBuilder.setModelId(CarsComSearchHelper.getModelIdByName(userSearchQuery.getModel()));
        carsComSearchRequestBuilder.setZipCode(userSearchQuery.getZipCode());
        carsComSearchRequestBuilder.setYear(userSearchQuery.getYear());

        if (userSearchQuery.getPrice() != null) {
            Double maxPrice = userSearchQuery.getPrice() + additionalSearchParams.getPriceSpread();
            carsComSearchRequestBuilder.setMaxPrice(maxPrice);

            Double minPrice = userSearchQuery.getPrice() - additionalSearchParams.getPriceSpread();
            carsComSearchRequestBuilder.setMinPrice(minPrice);
        }

        String searchUrl = carsComSearchRequestBuilder.renderUrl();

        this.torPageLoader = torPageLoader;
        this.carsComPageProcessor = new CarsComPageProcessor(torPageLoader);

        this.searchUrl = searchUrl;
    }

    public void startScraping(Consumer<CarsSearchProcessor> callback) {
        try {
            String searchResult = torPageLoader.getPage(searchUrl);
            Document doc = Jsoup.parse(searchResult);
            boolean hasNextPage;
            do {
                Elements carNodes = doc.select("div h4 a");
                mainExecuter = Executors.newFixedThreadPool(threads);

                for (Element carNode : carNodes) {
                    String carUrl = carNode.attr("href");

                    semaphore.acquire();

                    mainExecuter.execute(() -> {
                        ResultItem resultItem = carsComPageProcessor.process("http://www.cars.com" + carUrl);
                        if (resultItem != null) {
                            resultItemList.add(resultItem);
                            System.out.println("item added");
                        }
                        semaphore.release();
                    });
                }

                mainExecuter.shutdown();
                mainExecuter.awaitTermination(600, TimeUnit.SECONDS);

                hasNextPage = false;

                Elements nextPage = doc.select(".pagination a.right");
                if (nextPage.size() > 0) {
                    String href = nextPage.get(0).attr("href");
                    String nextPageData = torPageLoader.getPage(href);
                    doc = Jsoup.parse(nextPageData);
                    hasNextPage = true;
                }

            } while (hasNextPage);//page condition here

        } catch (Exception e) {
            LOG.error("Error: " + e.toString());
            this.isError = true;
            throw new RuntimeException(e);
        } finally {
            isFinished = true;
            callback.accept(this);
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public List<ResultItem> getResultItemList() {
        return resultItemList;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    @Override
    public UserSearchQuery getUserSearchQuery() {
        return userSearchQuery;
    }

    public boolean isError() {
        return isError;
    }
}
