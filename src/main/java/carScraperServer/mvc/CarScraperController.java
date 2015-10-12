package carScraperServer.mvc;

import carScraperServer.services.CarsComScrapeService;
import carScraperServer.services.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;

@Controller
@RequestMapping("/carsComScraper")
public class CarScraperController {

    @Autowired
    CarsComScrapeService carsComScrapeService;

    @Autowired
    ServletContext servletContext;

    @Autowired
    CarsService carsService;

    @RequestMapping(value = "", method = RequestMethod.GET)
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
    }
}