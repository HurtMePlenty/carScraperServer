package carScraperServer.scrapeEngine;

import org.apache.commons.lang3.StringUtils;

public class AutotraderRequestBuilder {
    private String makeId;
    private String modelId;
    private Long zipCode;
    private Integer radius;
    private Integer year;
    private Double minPrice;
    private Double maxPrice;


    private final String baseSearchUrl = "http://www.autotrader.com/cars-for-sale/Used+Cars%s?listingType=used&listingTypes=used&numRecords=100";
    private StringBuilder combinedUrl = new StringBuilder();


    public String renderUrl() {

        if (radius == null) {
            radius = 30;
        }

        combinedUrl.append(baseSearchUrl);

        if (StringUtils.isNotEmpty(makeId)) {
            combinedUrl.append(String.format("&makeCode1=%s", makeId));
        }

        if (StringUtils.isNotEmpty(modelId)) {
            combinedUrl.append(String.format("&modelCode1=%s", modelId));
        }

        combinedUrl.append(String.format("&searchRadius=%d", radius));

        if (year != null) {
            combinedUrl.append(String.format("&startYear=%d&endYear=%d", year, year));
        }

        if (minPrice != null) {
            combinedUrl.append(String.format("&minPrice==%d", minPrice.intValue()));
        }

        if (maxPrice != null) {
            combinedUrl.append(String.format("&maxPrice=%d", maxPrice.intValue()));
        }


        String searchUrl = combinedUrl.toString();
        if (zipCode != null) {
            searchUrl = String.format(searchUrl, String.format("/%d", zipCode));
        } else {
            searchUrl = String.format(searchUrl, "");
        }

        return searchUrl;
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
