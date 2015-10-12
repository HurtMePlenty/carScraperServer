package carScraperServer.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This entity represents already loaded pair make-model
 */
@Document(collection = "carsComProgress")
public class CarsComMakeModelPair {
    @Id
    private String id;

    private String make;
    private String model;
    private boolean succeeded;

    private CarsComMakeModelPair() {

    }

    public CarsComMakeModelPair(String make, String model, boolean succeeded) {
        this.make = make;
        this.model = model;
        this.succeeded = succeeded;
    }

    public String getId() {
        return id;
    }

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

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }
}
