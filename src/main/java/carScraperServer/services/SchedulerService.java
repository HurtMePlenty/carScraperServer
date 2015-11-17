package carScraperServer.services;

import carScraperServer.schedulerEntities.ScheduledTask;
import carScraperServer.services.jobs.SendMailJob;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SchedulerService {

    private Logger logger = Logger.getLogger(SchedulerService.class);
    private List<Job> jobList = new ArrayList<>();
    private Scheduler scheduler;

    @Autowired
    ScheduledTaskService scheduledTaskService;

    @PostConstruct
    public void init() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addTask(ScheduledTask scheduledTask) {
        try {
            Date startDate = scheduledTask.getStartDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            Date endDate = scheduledTask.getExpirationDate();

            String triggerIdentity = String.format("trigger_%s", scheduledTask.toString());

            String cronSchedule = String.format("0 %d %d ? * *", minute, hour);

            CronExpression cronExpression = new CronExpression(cronSchedule);
            System.out.println(cronExpression.getNextValidTimeAfter(new Date()));

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerIdentity)
                    .startAt(startDate)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
                    .endAt(endDate)
                    .build();

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("task", scheduledTask);

            String jobIdentity = String.format("job_%s", scheduledTask.toString());
            JobDetail jobDetail = JobBuilder.newJob(SendMailJob.class)
                    .withIdentity(jobIdentity)
                    .usingJobData(jobDataMap)
                    .build();


            scheduler.scheduleJob(jobDetail, cronTrigger);


        } catch (Exception e) {
            logger.error(String.format("Error when tried to start new scheduledTask: %s \n " +
                    "Scheduled task: %s", e.toString(), scheduledTask.toString()));
            throw new RuntimeException(e);
        }

    }


}
