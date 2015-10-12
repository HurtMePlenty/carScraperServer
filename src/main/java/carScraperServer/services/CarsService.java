package carScraperServer.services;

import carScraperServer.repositories.CarMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarsService {
    @Autowired
    CarMongoRepository carMongoRepository;

    public long carsCount() {
        return carMongoRepository.count();
    }
}
