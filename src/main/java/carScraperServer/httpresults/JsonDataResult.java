package carScraperServer.httpresults;

import carScraperServer.entities.ResultItem;

import java.util.List;

public class JsonDataResult implements JsonResult {

    private boolean isFinished;
    private List<ResultItem> data;


    public JsonDataResult(List<ResultItem> data, boolean isFinished) {
        this.data = data;
        this.isFinished = isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public List<ResultItem> getData() {
        return data;
    }
}
