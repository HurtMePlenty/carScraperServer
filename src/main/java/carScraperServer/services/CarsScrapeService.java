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
    SimplePageLoader simplePageLoader;

    @Autowired
    MongoTemplate mongoTemplate;

    private Double priceSpread;

    private final Map<UserSearchQuery, List<CarsSearchProcessor>> tasks = new ConcurrentHashMap<>();

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CarsScrapeService.class);

    private ExecutorService taskRunner = Executors.newCachedThreadPool();

    private int maxThreads;

    private boolean parallelSources;

    private boolean carsComUseTor;

    private boolean autotraderUseTor;

    private Set<String> availableSources = new HashSet<>();

    public CarsScrapeService() {
        availableSources.add("carscom");
        availableSources.add("autotradercom");
        availableSources.add("carguruscom");
    }

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

        tasks.put(userSearchQuery, new ArrayList<>());

        Set<String> sources = userSearchQuery.getSourceSet();

        if (sources.contains("carscom")) {
            //prepare cars.com task
            PageLoader pageLoader = carsComUseTor ? torPageLoader : simplePageLoader;
            CarsComSearchProcessor carsComSearchProcessor = new CarsComSearchProcessor(maxThreads, userSearchQuery, additionalSearchParams, pageLoader);
            tasks.get(userSearchQuery).add(carsComSearchProcessor);
        }

        if (sources.contains("autotradercom")) {
            //prepare autotrader.com task
            PageLoader pageLoader = autotraderUseTor ? torPageLoader : simplePageLoader;
            AutotraderSearchProcessor autotraderSearchProcessor = new AutotraderSearchProcessor(maxThreads, userSearchQuery, additionalSearchParams, pageLoader);
            tasks.get(userSearchQuery).add(autotraderSearchProcessor);
        }

        if (sources.contains("carguruscom")) {
            //prepare cargurus.com task
            PageLoader pageLoader = autotraderUseTor ? torPageLoader : simplePageLoader;
            CargurusSearchProcessor cargurusSearchProcessor = new CargurusSearchProcessor(maxThreads, userSearchQuery, additionalSearchParams, pageLoader);
            tasks.get(userSearchQuery).add(cargurusSearchProcessor);
        }


        if (parallelSources) {
            tasks.get(userSearchQuery).forEach(this::startExecuting);
        } else {
            executeNext(userSearchQuery);
        }

        return new JsonDataResult(new ArrayList<>(), false);
    }

    public Set<String> getAvailableSouces() {
        return availableSources;
    }

    private synchronized void executeNext(UserSearchQuery userSearchQuery) {
        List<CarsSearchProcessor> processors = tasks.get(userSearchQuery);
        if (processors == null) {
            return;
        }

        CarsSearchProcessor processorToRun = processors.stream().filter(p -> !p.isInProgress()).findFirst().orElse(null);
        if (processorToRun != null) {
            startExecuting(processorToRun);
        }
    }

    private void startExecuting(CarsSearchProcessor carsSearchProcessor) {
        taskRunner.execute(() -> {
            carsSearchProcessor.startScraping(this::taskFinished);
            LOG.info(String.format("Task started: %s", carsSearchProcessor.getUserSearchQuery().getQueryToken()));
        });
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


            //use URL instead of VIN
            //result item must contain VIN
            //resultItemList = resultItemList.stream().filter((item) -> StringUtils.isNotEmpty(item.getVin())).collect(Collectors.toList());

            List<String> urlList = resultItemList.stream().map(ResultItem::getUrl).collect(Collectors.toList());
            List<ResultItem> existingItems = carMongoRepository.findByUrlIn(urlList);

            for (ResultItem resultItem : resultItemList) {
                ResultItem existingItem = existingItems.stream().filter(item ->
                        item.getUrl().equals(resultItem.getUrl())).findFirst().orElse(null);
                if (existingItem != null) {
                    resultItem.setId(existingItem.getId());
                    resultItem.setZipcodeList(existingItem.getZipcodeList());
                }

                if (!resultItem.getZipcodeList().contains(currentZipCode)) {
                    resultItem.getZipcodeList().add(currentZipCode);
                }
                resultItem.setDate(new Date());

            }

            carMongoRepository.save(resultItemList);

            SearchQueryEntity searchQueryEntity = new SearchQueryEntity();
            searchQueryEntity.setDate(new Date());
            searchQueryEntity.setToken(userSearchQuery.getQueryToken());
            searchQueryRepository.save(searchQueryEntity);
            if (!parallelSources) {
                executeNext(userSearchQuery);
            }
        } catch (Exception e) {
            LOG.error(String.format("Error during data save: %s", e.toString()));
            throw new RuntimeException(e);
        } finally {
            boolean hasTaskInProgress = tasks.get(userSearchQuery).stream().anyMatch((p) -> !p.isFinished());
            if (!hasTaskInProgress) {
                tasks.remove(userSearchQuery);
            }
        }
    }

    private JsonResult renderResponse(List<CarsSearchProcessor> carsSearchProcessors) {
        List<ResultItem> resultItemList = new ArrayList<>();
        boolean allTasksFinished = true;
        for (CarsSearchProcessor carsSearchProcessor : carsSearchProcessors) {
            if (!carsSearchProcessor.isFinished()) {
                allTasksFinished = false;
            }

            resultItemList.addAll(carsSearchProcessor.getResultItemList());
        }
        return new JsonDataResult(resultItemList, allTasksFinished);
    }

    public JsonResult renderResponseFromDB(UserSearchQuery userSearchQuery) {

        QueryBuilder mainQuery = QueryBuilder.start();

        mainQuery.and(QueryBuilder.start().put("source").in(userSearchQuery.getSourceSet()).get());

        if (userSearchQuery.getMake() != null) {
            mainQuery.and(QueryBuilder.start().put("make").is(userSearchQuery.getMake().toLowerCase()).get());
        }

        if (userSearchQuery.getModel() != null) {
            mainQuery.and(QueryBuilder.start().put("model").is(userSearchQuery.getModel().toLowerCase()).get());
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

    @Required
    public void setParallelSources(boolean parallelSources) {
        this.parallelSources = parallelSources;
    }

    @Required
    public void setCarsComUseTor(boolean carsComUseTor) {
        this.carsComUseTor = carsComUseTor;
    }

    @Required
    public void setAutotraderUseTor(boolean autotraderUseTor) {
        this.autotraderUseTor = autotraderUseTor;
    }
}
