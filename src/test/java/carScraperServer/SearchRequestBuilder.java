package carScraperServer;

import org.springframework.util.StringUtils;

public class SearchRequestBuilder {

    private String makeId;
    private String modelId;
    private String zipCode;

    private final String baseSearchUrl = "http://www.cars.com/for-sale/searchresults.action?searchSource=ADVANCED_SEARCH&rpp=250";
    private StringBuilder combinedUrl = new StringBuilder();


    public String renderUrl() {
        combinedUrl.append(baseSearchUrl);
        if (!StringUtils.isEmpty(makeId)) {
            combinedUrl.append(String.format("&mkId=%s", makeId));
        }
        if (!StringUtils.isEmpty(modelId)) {
            combinedUrl.append(String.format("&mdId=%s", modelId));

        }
        if (!StringUtils.isEmpty(zipCode)) {
            combinedUrl.append(String.format("&zc=%s", zipCode));
        }
        return combinedUrl.toString();
    }

    public void setMakeId(String makeId) {
        this.makeId = makeId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
