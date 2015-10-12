package carScraperServer.repositories;

import carScraperServer.entities.CarsComMakeModelPair;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarsComMakeModelRepository extends MongoRepository<CarsComMakeModelPair, String> {
    public CarsComMakeModelPair findByMakeAndModel(String make, String model);
}
