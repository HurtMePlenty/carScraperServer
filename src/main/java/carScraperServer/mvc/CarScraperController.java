package carScraperServer.mvc;

import carScraperServer.httpresults.JsonResult;
import carScraperServer.scrapeEngine.AutotraderSearchHelper;
import carScraperServer.scrapeEngine.CarsComSearchHelper;
import carScraperServer.scrapeEngine.UserSearchQuery;
import carScraperServer.services.CarsScrapeService;
import carScraperServer.services.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.util.Set;

@Controller
@RequestMapping("/carsScraper")
public class CarScraperController {

    @Autowired
    CarsScrapeService carsScrapeService;

    @Autowired
    ServletContext servletContext;

    @Autowired
    CarsService carsService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String mainPage(ModelMap model) {
        model.addAttribute("itemsInDB", carsService.carsCount());
        model.addAttribute("tasksInProgress", carsScrapeService.getRequestsInProgress());

        return "mainPage";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResult performRequest(@RequestParam String make,
                                     @RequestParam String model,
                                     @RequestParam Integer year,
                                     @RequestParam Long zipcode,
                                     @RequestParam(required = false) Double price,
                                     @RequestParam(required = false) String postDate) {
        UserSearchQuery userSearchQuery = new UserSearchQuery();
        userSearchQuery.setMake(make);
        userSearchQuery.setModel(model);
        userSearchQuery.setYear(year);
        userSearchQuery.setZipCode(zipcode);
        userSearchQuery.setPrice(price);
        userSearchQuery.setPostDate(postDate);

        JsonResult result = carsScrapeService.execute(userSearchQuery);
        //Result res = new Result();
        //res.message = "sdfsdf";
        return result;
    }

    @RequestMapping(value = "/showMakes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Set<String> showMakes() {
        Set<String> totalSet = CarsComSearchHelper.getMakesNames();
        totalSet.addAll(AutotraderSearchHelper.getMakesNames());
        return totalSet;
    }

    @RequestMapping(value = "/showModels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Set<String> showModels() {

        Set<String> totalSet = CarsComSearchHelper.getModelNames();
        totalSet.addAll(AutotraderSearchHelper.getModelNames());
        return totalSet;
    }

    @RequestMapping(value = "/showPostDates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Set<String> showPostDates() {

        Set<String> totalSet = CarsComSearchHelper.getPostDates();
        return totalSet;
    }

   /* @RequestMapping(value = "", method = RequestMethod.GET)
    public String carsComScraper(ModelMap model) {

        if (carsComScrapeService.isRunning()) {
            model.addAttribute("isRunning", true);
            model.addAttribute("itemsLoaded", carsComScrapeService.getTotalItems());
        } else {
            model.addAttribute("isRunning", false);
        }

        model.addAttribute("itemsInDB", carsService.carsCount());

        model.addAttribute("message", "Hello world!");
        return "carsComScraper";
    }

    @RequestMapping(value = "run", method = RequestMethod.POST)
    public String carsComScraperRun(ModelMap model) {
        if (!carsComScrapeService.isRunning()) {
            carsComScrapeService.executeAsync();
        }

        return "redirect:/carsComScraper";
    }

    @RequestMapping(value = "stop", method = RequestMethod.POST)
    public String carsComScraperStop(ModelMap model) {
        if (carsComScrapeService.isRunning()) {
            carsComScrapeService.stop();
        }

        return "redirect:/carsComScraper";
    }*/

}