package carScraperServer.services;


import carScraperServer.mvc.models.ScheduledTaskRequest;
import carScraperServer.schedulerEntities.ScheduledTask;
import carScraperServer.schedulerRepositories.ScheduledTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ScheduledTaskService {

    @Autowired
    ScheduledTaskRepository scheduledTaskRepository;

    @Autowired
    SchedulerService schedulerService;

    public void saveScheduledTask(ScheduledTaskRequest scheduledTaskRequest) {

        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.setStartDate(new Date(Long.parseLong(scheduledTaskRequest.getStartDate())));
        scheduledTask.setMessage(scheduledTaskRequest.getMessage());
        scheduledTask.setTextEmailBoth(Integer.parseInt(scheduledTaskRequest.getTextEmailBoth()));
        scheduledTask.setCell(scheduledTaskRequest.getCell());
        scheduledTask.setEmailTo(scheduledTaskRequest.getEmailTo());
        scheduledTask.setEmailFrom(scheduledTaskRequest.getEmailFrom());
        scheduledTask.setFrequency(Integer.parseInt(scheduledTaskRequest.getFrequency()));
        scheduledTask.setExpirationDate(new Date(Long.parseLong(scheduledTaskRequest.getExpirationDate())));
        scheduledTask.setDealerId(scheduledTaskRequest.getDealerId());
        scheduledTask.setLocationID(scheduledTaskRequest.getLocationID());
        scheduledTask.setSource(scheduledTaskRequest.getSource());
        scheduledTask.setSourceID(scheduledTaskRequest.getSourceID());

        schedulerService.addTask(scheduledTask);

        //scheduledTaskRepository.save(scheduledTask);
    }

    private void addToScheduler(ScheduledTask scheduledTask)
    {

    }

}


/*    private Long id;
    private Date startDate;
    private String message;
    private Integer textEmailBoth;
    private String cell;
    private String emailTo;
    private String emailFrom;
    private Integer frequency;
    private Date expirationDate;
    private String dealerId;
    private String locationID;
    private String source;
    private String sourceID;
    */