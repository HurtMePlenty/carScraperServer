package carScraperServer.services.jobs;


import carScraperServer.schedulerEntities.ScheduledTask;
import carScraperServer.services.ScheduledTaskService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

public class SendMailJob implements Job {

    private Logger logger = Logger.getLogger(SendMailJob.class);


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        ScheduledTask scheduledTask = (ScheduledTask) dataMap.get("task");
        ScheduledTaskService scheduledTaskService = (ScheduledTaskService) dataMap.get("scheduledTaskService");

        logger.info(String.format("SendMailJob was executed. Task: %s", scheduledTask.toString()));

        scheduledTask.executed(new Date());
        scheduledTaskService.saveScheduledTask(scheduledTask);
    }
}
