package carScraperServer.mvc.controllers;


import carScraperServer.httpresults.SchedulerJSonResult;
import carScraperServer.mvc.models.ScheduledTaskRequest;
import carScraperServer.schedulerEntities.ScheduledTask;
import carScraperServer.services.ScheduledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/scheduler")
public class SchedulerAPIController {
    @Autowired
    ScheduledTaskService scheduledTaskService;

    @RequestMapping(value = "/newTask", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SchedulerJSonResult scheduleTask(ScheduledTaskRequest scheduledTaskRequest) {
        String error = scheduledTaskRequest.validate();
        if (error != null) {
            return new SchedulerJSonResult(error, true);
        }

        scheduledTaskService.saveScheduledTask(scheduledTaskRequest);

        return new SchedulerJSonResult("Task was successfully added");
    }

    @RequestMapping(value = "/showAllActiveTasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ScheduledTask> showAllActiveTasks() {
        return scheduledTaskService.findAllUnexpiredTasks();
    }

}
