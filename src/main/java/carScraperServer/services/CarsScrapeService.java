package carScraperServer.services;

import carScraperServer.entities.ResultItem;
import carScraperServer.entities.SearchQueryEntity;
import carScraperServer.httpresults.JsonDataResult;
import carScraperServer.httpresults.JsonResult;
import carScraperServer.repositories.CarMongoRepository;
import carScraperServer.repositories.CarsComMakeModelRepository;
import carScraperServer.repositories.SearchQueryRepository;
import carScraperServer.scrapeEngine.*;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class CarsScrapeService {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    CarMongoRepository carMongoRepository;

    @Autowired
    CarsComMakeModelRepository carsComMakeModelRepository;

    @Autowired
    SearchQueryRepository searchQueryRepository;

    @Autowired
    ServletContext servletContext;

    @Autowired
    TorPageLoader torPageLoader;

    @Autowired
    MongoTemplate mongoTemplate;

    private Double priceSpread;

    private final Map<UserSearchQuery, CarsSearchProcessor> tasks = new ConcurrentHashMap<>();

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CarsScrapeService.class);

    private ExecutorService taskRunner = Executors.newCachedThreadPool();

    private int maxThreads;

    public synchronized JsonResult execute(UserSearchQuery userSearchQuery) {

        String queryToken = userSearchQuery.getQueryToken();

        if (tasks.containsKey(userSearchQuery)) {
            LOG.info(String.format("Requested query is already in progress. Query: %s", queryToken));
            return renderResponse(tasks.get(userSearchQuery));
        }


        AdditionalSearchParams additionalSearchParams = new AdditionalSearchParams(priceSpread);

        Date lastDayDate = getBorderDateForSearch();

        SearchQueryEntity searchQueryEntity = searchQueryRepository.findByDateGreaterThanAndToken(lastDayDate, queryToken);
        if (searchQueryEntity != null) {
            LOG.info(String.format("Return cached data for query: %s", userSearchQuery.getQueryToken()));
            return renderResponseFromDB(userSearchQuery);
        }

        taskRunner.execute(() -> {
            CarsComSearchProcessor carsComSearchProcessor = new CarsComSearchProcessor(maxThreads, userSearchQuery, additionalSearchParams, torPageLoader);
            tasks.put(userSearchQuery, carsComSearchProcessor);
            LOG.info(String.format("Task started: %s", queryToken));
            carsComSearchProcessor.startScraping(this::taskFinished);
        });

        return new JsonDataResult(new ArrayList<>(), false);
    }

    private synchronized void taskFinished(CarsSearchProcessor carsSearchProcessor) {
        UserSearchQuery userSearchQuery = carsSearchProcessor.getUserSearchQuery();
        Long currentZipCode = userSearchQuery.getZipCode();
        try {
            LOG.info(String.format("Task finished: %s", userSearchQuery.getQueryToken()));
            List<ResultItem> resultItemList = carsSearchProcessor.getResultItemList();

            if (resultItemList.isEmpty()) {
                return;
            }

            LOG.info(String.format("task %s finished with %d items",
                    userSearchQuery.getQueryToken(), resultItemList.size()));

            //remove old data by VIN

            //result item must contain VIN
            resultItemList = resultItemList.stream().filter((item) -> StringUtils.isNotEmpty(item.getVin())).collect(Collectors.toList());

            List<String> vinList = resultItemList.stream().map(ResultItem::getVin).collect(Collectors.toList());
            List<ResultItem> existingItems = carMongoRepository.findByVinIn(vinList);

            resultItemList = resultItemList.stream().filter((resultItem) -> {
                return !existingItems.stream().anyMatch((existingItem) -> existingItem.getVin().equals(resultItem.getVin()));
            }).collect(Collectors.toList());

            resultItemList.addAll(existingItems);

            for (ResultItem resultItem : resultItemList) {
                if (!resultItem.getZipcodeList().contains(currentZipCode)) {
                    resultItem.getZipcodeList().add(currentZipCode);
                    resultItem.setDate(new Date());
                }
            }

            carMongoRepository.save(resultItemList);

            SearchQueryEntity searchQueryEntity = new SearchQueryEntity();
            searchQueryEntity.setDate(new Date());
            searchQueryEntity.setToken(userSearchQuery.getQueryToken());
            searchQueryRepository.save(searchQueryEntity);

        } catch (Exception e) {
            LOG.error(String.format("Error during data save: %s", e.toString()));
            throw new RuntimeException(e);
        } finally {
            tasks.remove(userSearchQuery);
        }
    }

    private JsonResult renderResponse(CarsSearchProcessor carsSearchProcessor) {
        return new JsonDataResult(carsSearchProcessor.getResultItemList(), carsSearchProcessor.isFinished());
    }

    public JsonResult renderResponseFromDB(UserSearchQuery userSearchQuery) {

        QueryBuilder mainQuery = QueryBuilder.start();
        if (userSearchQuery.getMake() != null) {
            mainQuery.and(QueryBuilder.start().put("make").is(userSearchQuery.getMake()).get());
        }

        if (userSearchQuery.getModel() != null) {
            mainQuery.and(QueryBuilder.start().put("model").is(userSearchQuery.getModel()).get());
        }

        if (userSearchQuery.getYear() != null) {
            mainQuery.and(QueryBuilder.start().put("year").is(userSearchQuery.getYear()).get());
        }

        if (userSearchQuery.getZipCode() != null) {
            mainQuery.and(QueryBuilder.start().put("zipcodeList").is(userSearchQuery.getZipCode()).get());
        }

        if (userSearchQuery.getPrice() != null) {
            Double minPrice = userSearchQuery.getPrice() - priceSpread;
            if (minPrice > 0) {
                mainQuery.and(QueryBuilder.start().put("price").greaterThanEquals(minPrice).get());
            }
            mainQuery.and(QueryBuilder.start().put("price").lessThanEquals(userSearchQuery.getPrice() + priceSpread).get());
        }

        mainQuery.and(QueryBuilder.start().put("date").greaterThanEquals(getBorderDateForSearch()).get());


        BasicQuery basicQuery = new BasicQuery(mainQuery.get());


        List<ResultItem> data = mongoTemplate.find(basicQuery, ResultItem.class);
        return new JsonDataResult(data, true);
    }

    public List<String> getRequestsInProgress() {
        List<String> result = new ArrayList<>();
        for (UserSearchQuery userSearchQuery : tasks.keySet()) {
            result.add(userSearchQuery.getQueryToken());
        }
        return result;
    }


    private Date getBorderDateForSearch() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @Required
    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    @Required
    public void setPriceSpread(Double priceSpread) {
        this.priceSpread = priceSpread;
    }
}
