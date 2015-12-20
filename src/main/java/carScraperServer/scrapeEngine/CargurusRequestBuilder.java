package carScraperServer.scrapeEngine;


import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CargurusRequestBuilder {
    private String makeId;
    private String modelId;
    private Long zipCode;
    private Integer radius;
    private Integer year;
    private Double minPrice;
    private Double maxPrice;


    public Map<String, String> renderPostDataMap() {

        Map<String, String> result = new HashMap<>();

        if (radius == null) {
            radius = 50;
        }


        result.put("distance", radius.toString());


        result.put("page", "1");
        result.put("displayFeaturedListings", "true");
        result.put("isRecentSearchView", "false");


        if (StringUtils.isNotEmpty(modelId)) {
            result.put("advancedSearchAutoEntities[0].selectedEntity", modelId);
        }

        if (year != null) {
            result.put("startYear", year.toString());
            result.put("endYear", year.toString());
        }

        if (minPrice != null) {
            result.put("minPrice", String.valueOf(minPrice.intValue()));
        }

        if (maxPrice != null) {
            result.put("maxPrice", String.valueOf(maxPrice.intValue()));
        }

        if (zipCode != null) {
            result.put("zip", zipCode.toString());
        }

        result.put("displayFeaturedListings", "true");
        result.put("inventorySearchWidgetType", "ADVANCED");
        result.put("transmission", "ANY");
        result.put("newUsed", "2");
        result.put("isRecentSearchView", "false");

        return result;
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
