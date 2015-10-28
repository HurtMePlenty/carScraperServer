package carScraperServer.scrapeEngine;

import org.springframework.util.StringUtils;

public class CarsComSearchRequestBuilder {

    private String makeId;
    private String modelId;
    private Long zipCode;
    private Integer radius;
    private Integer year;
    private Double minPrice;
    private Double maxPrice;

    private final String baseSearchUrl = "http://www.cars.com/for-sale/searchresults.action?searchSource=ADVANCED_SEARCH&rpp=250&stkTyp=U"; //stkTyp=U - used cars only
    private StringBuilder combinedUrl = new StringBuilder();


    public String renderUrl() {

        if(radius == null){
            radius = 30;
        }

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
        if (radius != null) {
            combinedUrl.append(String.format("&rd=%d", radius));
        }

        if (year != null) {
            combinedUrl.append(String.format("&yrMn=%d&yrMx=%d", year, year));
        }

        if (minPrice != null) {
            combinedUrl.append(String.format("&prMn=%d", minPrice.intValue()));
        }

        if (maxPrice != null) {
            combinedUrl.append(String.format("&prMx=%d", maxPrice.intValue()));
        }

        return combinedUrl.toString();
    }

    public void setMakeId(String makeId) {
        this.makeId = makeId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void setZipCode(Long zipCode) {
        this.zipCode = zipCode;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
