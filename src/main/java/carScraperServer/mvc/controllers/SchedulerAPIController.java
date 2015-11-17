package carScraperServer.mvc.controllers;


import carScraperServer.httpresults.SchedulerJSonResult;
import carScraperServer.mvc.models.ScheduledTaskRequest;
import carScraperServer.services.ScheduledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scheduler")
public class SchedulerAPIController {
    @Autowired
    ScheduledTaskService scheduledTaskService;

    @RequestMapping(value = "/newTask", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SchedulerJSonResult scheduleTask(ScheduledTaskRequest scheduledTaskRequest) {
        String error = scheduledTaskRequest.validate();
        if (error != null) {
            return new SchedulerJSonResult(error, true);
        }

        return new SchedulerJSonResult("Task was successfully added");
    }
}
