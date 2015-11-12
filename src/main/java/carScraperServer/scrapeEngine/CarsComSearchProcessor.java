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

public class CarsComSearchProcessor implements CarsSearchProcessor {
    private int threads;
    private boolean isFinished;
    private String searchUrl;
    private PageLoader pageLoader;
    private CarsComPageProcessor carsComPageProcessor;
    private UserSearchQuery userSearchQuery;
    private volatile List<ResultItem> resultItemList = new CopyOnWriteArrayList<>();
    private ExecutorService mainExecutor;
    private Semaphore semaphore;
    private boolean isError = false;
    private boolean inProgress = true;
    private Integer totalItemsExpected;

    private boolean isInitializedCorrectly;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CarsComSearchProcessor.class);

    public CarsComSearchProcessor(int threads, UserSearchQuery userSearchQuery, AdditionalSearchParams additionalSearchParams, PageLoader pageLoader) {
        this.threads = threads;
        this.semaphore = new Semaphore(threads);
        this.userSearchQuery = userSearchQuery;

        CarsComSearchRequestBuilder carsComSearchRequestBuilder = new CarsComSearchRequestBuilder();

        String makeId = CarsComSearchHelper.getMakeIdByName(userSearchQuery.getMake());
        if (StringUtils.isEmpty(makeId)) {
            LOG.info(String.format("MakeId wasn't found for make %s", userSearchQuery.getMake()));
            isInitializedCorrectly = false;
            return;
        }

        String modelId = CarsComSearchHelper.getModelIdByName(userSearchQuery.getModel());
        if (StringUtils.isEmpty(makeId)) {
            LOG.info(String.format("ModelId wasn't found for model %s", userSearchQuery.getModel()));
            isInitializedCorrectly = false;
            return;
        }

        String postDateCode = CarsComSearchHelper.getPostDateCodeByName(userSearchQuery.getPostDate());

        carsComSearchRequestBuilder.setMakeId(makeId);
        carsComSearchRequestBuilder.setModelId(modelId);
        carsComSearchRequestBuilder.setZipCode(userSearchQuery.getZipCode());
        carsComSearchRequestBuilder.setYear(userSearchQuery.getYear());
        carsComSearchRequestBuilder.setPostDateCode(postDateCode);

        if (userSearchQuery.getPrice() != null) {
            Double maxPrice = userSearchQuery.getPrice() + additionalSearchParams.getPriceSpread();
            carsComSearchRequestBuilder.setMaxPrice(maxPrice);

            Double minPrice = userSearchQuery.getPrice() - additionalSearchParams.getPriceSpread();
            if (minPrice > 0) {
                carsComSearchRequestBuilder.setMinPrice(minPrice);
            }
        }

        String searchUrl = carsComSearchRequestBuilder.renderUrl();

        this.pageLoader = pageLoader;
        this.carsComPageProcessor = new CarsComPageProcessor(pageLoader);

        this.searchUrl = searchUrl;
        this.isInitializedCorrectly = true;
    }

    public void startScraping(Consumer<CarsSearchProcessor> callback) {
        try {
            if (!isInitializedCorrectly) {
                LOG.info("Wasn't initialized correctly. Scraping wasn't started.");
                return;
            }
            inProgress = true;
            LOG.info(String.format("Cars.com loading main searchUrl: %s", searchUrl));
            String searchResult = pageLoader.getPage(searchUrl);
            Document doc = Jsoup.parse(searchResult);

            Elements totalItems = doc.select("h3.secondary");
            if (totalItems.size() > 0) {
                String html = totalItems.get(0).html();
                if (html.split(" ").length > 1) {
                    totalItemsExpected = Integer.parseInt(html.split(" ")[0]);
                    LOG.info(String.format("%s Items expected: %d", userSearchQuery.getQueryToken(), totalItemsExpected));
                }
            }

            boolean hasNextPage;
            do {
                Elements carNodes = doc.select("div h4 a");
                mainExecutor = Executors.newFixedThreadPool(threads);

                for (Element carNode : carNodes) {
                    String carUrl = carNode.attr("href");

                    semaphore.acquire();

                    mainExecutor.execute(() -> {
                        System.out.println("item requested");
                        ResultItem resultItem = carsComPageProcessor.process("http://www.cars.com" + carUrl);
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

                hasNextPage = false;

                Elements nextPage = doc.select(".pagination a.right");
                if (nextPage.size() > 0) {
                    String href = nextPage.get(0).attr("href");
                    if (href != null && !href.contains("javascript:")) {
                        String nextPageData = pageLoader.getPage(href);
                        doc = Jsoup.parse(nextPageData);
                        hasNextPage = true;
                    }
                }

            } while (hasNextPage);//page condition here

        } catch (Exception e) {
            LOG.error("Error: " + e.toString());
            this.isError = true;
            throw new RuntimeException(e);
        } finally {
            inProgress = false;
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

    @Override
    public boolean isInProgress() {
        return inProgress;
    }
}
