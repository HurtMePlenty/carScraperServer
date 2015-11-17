package carScraperServer;


import carScraperServer.mvc.models.ScheduledTaskRequest;
import carScraperServer.services.ScheduledTaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Calendar;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class ScheduleTaskTest {

    @Autowired
    ScheduledTaskService scheduledTaskService;


    @Test
    public void testSave() throws InterruptedException {
        ScheduledTaskRequest scheduledTaskRequest = new ScheduledTaskRequest();
        scheduledTaskRequest.setMessage("sdfsdf");

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        Date startDate = new Date(calendar.getTimeInMillis() + 60 * 1000);
        Date endDate = new Date(calendar.getTimeInMillis() + 2 * 24 * 60 * 60 * 1000);
        scheduledTaskRequest.setStartDate(String.valueOf(startDate.getTime()));
        scheduledTaskRequest.setExpirationDate(String.valueOf(endDate.getTime()));

        scheduledTaskRequest.setFrequency("1");
        scheduledTaskRequest.setTextEmailBoth("1");

        scheduledTaskService.saveScheduledTask(scheduledTaskRequest);

        Thread.sleep(100000000);
    }


}
