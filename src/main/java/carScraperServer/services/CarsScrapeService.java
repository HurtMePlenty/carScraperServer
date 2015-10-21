package carScraperServer.services;

import carScraperServer.entities.ResultItem;
import carScraperServer.entities.SearchQueryEntity;
import carScraperServer.httpresults.JsonDataResult;
import carScraperServer.httpresults.JsonResult;
import carScraperServer.repositories.CarMongoRepository;
import carScraperServer.repositories.CarsComMakeModelRepository;
import carScraperServer.repositories.SearchQueryRepository;
import carScraperServer.scrapeEngine.*;
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
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date lastDayDate = cal.getTime();


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

    private void taskFinished(CarsSearchProcessor carsSearchProcessor) {
        UserSearchQuery userSearchQuery = carsSearchProcessor.getUserSearchQuery();
        try {
            LOG.info(String.format("Task finished: %s", userSearchQuery.getQueryToken()));
            List<ResultItem> resultItemList = carsSearchProcessor.getResultItemList();

            //remove old data by VIN

            //result item must contain VIN
            resultItemList = resultItemList.stream().filter((item) -> StringUtils.isNotEmpty(item.getVin())).collect(Collectors.toList());
            StringBuilder vins = new StringBuilder();

            for (ResultItem resultItem : resultItemList) {
                vins.append("'").append(resultItem.getVin()).append("',");
            }

            String removeQuery = String.format("{'vin':{$in:[%s]}}", vins.toString());
            BasicQuery basicQuery = new BasicQuery(removeQuery);
            mongoTemplate.remove(basicQuery, ResultItem.class);
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

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("{");
        if (userSearchQuery.getMake() != null) {
            queryBuilder.append(String.format("'%s':'%s',", "make", userSearchQuery.getMake()));
        }
        if (userSearchQuery.getModel() != null) {
            queryBuilder.append(String.format("'%s':'%s',", "model", userSearchQuery.getModel()));
        }
        if (userSearchQuery.getYear() != null) {
            queryBuilder.append(String.format("'%s':%d,", "year", userSearchQuery.getYear()));
        }
        queryBuilder.append("}");


        BasicQuery basicQuery = new BasicQuery(queryBuilder.toString());

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


    @Required
    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    @Required
    public void setPriceSpread(Double priceSpread) {
        this.priceSpread = priceSpread;
    }
}
