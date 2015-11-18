package carScraperServer.schedulerRepositories;


import carScraperServer.schedulerEntities.ScheduledTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduledTaskRepository extends JpaRepository<ScheduledTask, Long> {
    List<ScheduledTask> findTasksByExpirationDateGreaterThan(Date date);
}
