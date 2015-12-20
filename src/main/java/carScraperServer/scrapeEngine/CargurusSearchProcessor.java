package carScraperServer.scrapeEngine;

import carScraperServer.entities.ResultItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CargurusSearchProcessor implements CarsSearchProcessor {

    private int threads;
    private boolean isFinished;
    private PageLoader pageLoader;
    private UserSearchQuery userSearchQuery;
    private volatile List<ResultItem> resultItemList = new CopyOnWriteArrayList<>();
    private boolean inProgress;
    private boolean isInitializedCorrectly;
    private CargurusPageProcessor cargurusPageProcessor;
    private CargurusRequestBuilder cargurusRequestBuilder;
    private Map<String, String> customHeaders = new HashMap<>();
    private boolean isError;

    private ExecutorService mainExecutor;


    private final ObjectMapper mapper = new ObjectMapper();

    private final String postRequestUrl = "http://www.cargurus.com/Cars/inventorylisting/ajaxFetchSubsetInventoryListing.action?sourceContext=untrackedWithinSite_false_0";
    private final String itemUrl = "http://www.cargurus.com/Cars/inventorylisting/viewDetailsFilterViewInventoryListing.action?#listing=%s";
    private final String detailsUrl = "http://www.cargurus.com/Cars/inventorylisting/viewListingDetailAjax.action?inventoryListing=%s";

    private Integer maxDaysOnMarket;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CargurusSearchProcessor.class);

    public CargurusSearchProcessor(int threads, UserSearchQuery userSearchQuery, AdditionalSearchParams additionalSearchParams, PageLoader pageLoader) {
        this.threads = threads;

        this.userSearchQuery = userSearchQuery;
        this.pageLoader = pageLoader;

        if (userSearchQuery.getPostDate() != null) {
            switch (userSearchQuery.getPostDate()) {
                case "1day":
                    maxDaysOnMarket = 1;
                    break;
                case "3days":
                    maxDaysOnMarket = 3;
                    break;
                case "7days":
                    maxDaysOnMarket = 7;
                    break;
                case "2weeks":
                    maxDaysOnMarket = 14;
                    break;
                case "month":
                    maxDaysOnMarket = 31;
                    break;
            }
        }

        String makeId = CargurusSearchHelper.getMakeIdByName(userSearchQuery.getMake());
        if (StringUtils.isEmpty(makeId)) {
            LOG.info(String.format("MakeId wasn't found for make %s", userSearchQuery.getMake()));
            this.isInitializedCorrectly = false;
            return;
        }

        String modelId = CargurusSearchHelper.getModelIdByName(userSearchQuery.getModel());
        if (StringUtils.isEmpty(makeId)) {
            LOG.info(String.format("ModelId wasn't found for model %s", userSearchQuery.getModel()));
            this.isInitializedCorrectly = false;
            return;
        }

        cargurusPageProcessor = new CargurusPageProcessor(pageLoader);

        cargurusRequestBuilder = new CargurusRequestBuilder();
        cargurusRequestBuilder.setMakeId(makeId);
        cargurusRequestBuilder.setModelId(modelId);
        cargurusRequestBuilder.setZipCode(userSearchQuery.getZipCode());
        cargurusRequestBuilder.setYear(userSearchQuery.getYear());

        if (userSearchQuery.getPrice() != null) {
            Double maxPrice = userSearchQuery.getPrice() + additionalSearchParams.getPriceSpread();
            cargurusRequestBuilder.setMaxPrice(maxPrice);

            Double minPrice = userSearchQuery.getPrice() - additionalSearchParams.getPriceSpread();
            if (minPrice > 0) {
                cargurusRequestBuilder.setMinPrice(minPrice);
            }
        }

        customHeaders.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        isInitializedCorrectly = true;
    }

    @Override
    public void startScraping(Consumer<CarsSearchProcessor> callback) {
        try {
            if (!isInitializedCorrectly) {
                LOG.info("Wasn't initialized correctly. Scraping wasn't started.");
                return;
            }

            inProgress = true;

            mainExecutor = Executors.newFixedThreadPool(threads);


            String response = pageLoader.postPage(postRequestUrl, cargurusRequestBuilder.renderPostDataMap(), customHeaders);
            JsonNode actualObj = mapper.readTree(response);

            JsonNode listingNode = actualObj.get("listings");
            if (listingNode == null) {
                return;
            }


            Iterator<JsonNode> resultIterator = listingNode.elements();
            while (resultIterator.hasNext()) {
                JsonNode carNode = resultIterator.next();

                mainExecutor.execute(() -> {

                    JsonNode isCertifiedNode = carNode.get("isCertified");
                    if (isCertifiedNode != null) { //all not used cars
                        boolean isCertified = isCertifiedNode.asBoolean();
                        if (isCertified) {
                            return;
                        }
                    }

                    if (maxDaysOnMarket != null) {
                        JsonNode daysOnMarketNode = carNode.get("daysOnMarket");
                        if (daysOnMarketNode != null) {
                            int daysOnMarket = daysOnMarketNode.asInt();
                            if (daysOnMarket > maxDaysOnMarket) {
                                return;
                            }
                        }
                    }

                    ResultItem resultItem = new ResultItem();
                    JsonNode colorNode = carNode.get("exteriorColorName");
                    if (colorNode != null) {
                        resultItem.setColor(colorNode.asText());
                    }

                    JsonNode trimNode = carNode.get("trimName");
                    if (trimNode != null) {
                        resultItem.setTrim(trimNode.asText());
                    }

                    JsonNode priceNode = carNode.get("price");
                    if (priceNode != null) {
                        resultItem.setPrice(priceNode.asDouble());
                    }

                    JsonNode makeNameNode = carNode.get("makeName");
                    if (makeNameNode != null) {
                        resultItem.setMake(makeNameNode.asText());
                    }

                    JsonNode modelNameNode = carNode.get("modelName");
                    if (modelNameNode != null) {
                        resultItem.setModel(modelNameNode.asText());
                    }

                    JsonNode mileageNode = carNode.get("mileage");
                    if (mileageNode != null) {
                        resultItem.setMileage(mileageNode.asInt());
                    }

                    JsonNode carYearNode = carNode.get("carYear");
                    if (carYearNode != null) {
                        resultItem.setYear(carYearNode.asInt());
                    }

                    JsonNode mainPictureNode = carNode.get("mainPictureUrl");
                    if (mainPictureNode != null) {
                        resultItem.getImageUrls().add(mainPictureNode.asText());
                    }

                    JsonNode zipNode = carNode.get("zip");
                    if (zipNode != null) {
                        resultItem.getZipcodeList().add(zipNode.asLong());
                    }

                    JsonNode idNode = carNode.get("id");
                    String id = null;
                    if (idNode != null) {
                        id = idNode.asText();
                    }

                    if (id != null) {
                        resultItem.setUrl(String.format(itemUrl, id));

                        String detailsFinalUrl = String.format(detailsUrl, id);
                        resultItem = cargurusPageProcessor.process(detailsFinalUrl, resultItem);
                    }

                    resultItem.setSource("carguruscom");
                    resultItemList.add(resultItem);

                });

                //need to get VIN and more pics
                //http://www.cargurus.com/Cars/inventorylisting/viewListingDetailAjax.action?inventoryListing=118134234

            }

            mainExecutor.shutdown();
            mainExecutor.awaitTermination(600, TimeUnit.SECONDS);


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

    @Override
    public boolean isInProgress() {
        return this.inProgress;
    }

    public boolean isError() {
        return isError;
    }
}
