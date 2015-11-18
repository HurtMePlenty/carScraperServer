package carScraperServer.services;

import carScraperServer.schedulerEntities.ScheduledTask;
import carScraperServer.services.jobs.SendMailJob;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SchedulerService {

    private Logger logger = Logger.getLogger(SchedulerService.class);
    private Scheduler scheduler;

    @Autowired
    ScheduledTaskService scheduledTaskService;

    @PostConstruct
    public void init() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            List<ScheduledTask> unexpiredExistingTasks = scheduledTaskService.findAllUnexpiredTasks();
            unexpiredExistingTasks.forEach(this::addTask);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addTask(ScheduledTask scheduledTask) {
        try {
            Date startDate = scheduledTask.getStartDate();
            Calendar startDateCalendar = Calendar.getInstance();
            startDateCalendar.setTime(startDate);
            int hour = startDateCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = startDateCalendar.get(Calendar.MINUTE);
            int day = startDateCalendar.get(Calendar.DAY_OF_MONTH);
            int month = startDateCalendar.get(Calendar.MONTH) + 1;
            int year = startDateCalendar.get(Calendar.YEAR);

            int frequency = scheduledTask.getFrequency();


            Date endDate = scheduledTask.getExpirationDate();

            String triggerIdentity = String.format("trigger_%s", scheduledTask.toString());


            String cronSchedule = String.format("0 %d %d %d/%d %d ? %d", minute, hour, day, frequency, month, year);

            CronExpression cronExpression = new CronExpression(cronSchedule);
            Date nextValidDateForExecution = cronExpression.getNextValidTimeAfter(new Date());
            logger.info(String.format("Next execution date %s for task %s", nextValidDateForExecution.toString(), scheduledTask.toString()));
            System.out.println(nextValidDateForExecution);

            //start scheduler a bit earlier to make it execute our task
            //at start time. Otherwise the task will be executed only on the next day
            Date fakeStartDate = new Date(nextValidDateForExecution.getTime() - 1000 * 60 * 30);

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerIdentity)
                    .startAt(fakeStartDate)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
                    .endAt(endDate)
                    .build();

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("task", scheduledTask);
            jobDataMap.put("scheduledTaskService", scheduledTaskService);

            String jobIdentity = String.format("job_%s", scheduledTask.toString());
            JobDetail jobDetail = JobBuilder.newJob(SendMailJob.class)
                    .withIdentity(jobIdentity)
                    .usingJobData(jobDataMap)
                    .build();


            scheduler.scheduleJob(jobDetail, cronTrigger);


        } catch (Exception e) {
            logger.error(String.format("Error when tried to start new scheduledTask: %s \n " +
                    "Scheduled task: %s", e.toString(), scheduledTask.toString()));
            //throw new RuntimeException(e);
        }

    }


}
