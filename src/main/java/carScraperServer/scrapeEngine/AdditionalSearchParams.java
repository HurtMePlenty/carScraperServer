package carScraperServer.scrapeEngine;

public class AdditionalSearchParams {
    private Double priceSpread;

    public AdditionalSearchParams(Double priceSpread) {
        this.priceSpread = priceSpread;
    }

    public Double getPriceSpread() {
        return priceSpread;
    }
}
