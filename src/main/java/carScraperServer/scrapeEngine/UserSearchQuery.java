package carScraperServer.scrapeEngine;

public class UserSearchQuery {
    private String make;
    private String model;
    private Double price;
    private Integer year;
    private Long zipCode;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long getZipCode() {
        return zipCode;
    }

    public void setZipCode(long zipCode) {
        this.zipCode = zipCode;
    }

    public String getQueryToken() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSearchQuery that = (UserSearchQuery) o;

        if (make != null ? !make.equals(that.make) : that.make != null) return false;
        if (model != null ? !model.equals(that.model) : that.model != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (year != null ? !year.equals(that.year) : that.year != null) return false;
        if (zipCode != null ? !zipCode.equals(that.zipCode) : that.zipCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = make != null ? make.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "UserSearchQuery{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", year=" + year +
                ", zipCode=" + zipCode +
                '}';
    }
}
