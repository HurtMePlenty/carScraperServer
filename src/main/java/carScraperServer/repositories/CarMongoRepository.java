package carScraperServer.repositories;


import carScraperServer.entities.ResultItem;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CarMongoRepository extends MongoRepository<ResultItem, String> {
    public ResultItem findByUrl(String url);
}