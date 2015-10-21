package carScraperServer.repositories;

import carScraperServer.entities.SearchQueryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

public interface SearchQueryRepository extends MongoRepository<SearchQueryEntity, String> {
    public SearchQueryEntity findByDateGreaterThanAndToken(Date date, String token);
}
