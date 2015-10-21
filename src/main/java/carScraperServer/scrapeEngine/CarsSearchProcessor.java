package carScraperServer.scrapeEngine;

import carScraperServer.entities.ResultItem;

import java.util.List;
import java.util.function.Consumer;

public interface CarsSearchProcessor {

    public void startScraping(Consumer<CarsSearchProcessor> callback);
    public UserSearchQuery getUserSearchQuery();
    public List<ResultItem> getResultItemList();
    public boolean isFinished();
}
