package carScraperServer.repositories;


import carScraperServer.entities.ResultItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CarMongoRepository extends MongoRepository<ResultItem, String> {
    public ResultItem findByUrl(String url);
    public List<ResultItem> findByVinIn(List<String> vins);
}