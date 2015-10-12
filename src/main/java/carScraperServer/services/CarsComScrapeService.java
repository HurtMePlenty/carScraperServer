package carScraperServer.services;

import carScraperServer.entities.CarsComMakeModelPair;
import carScraperServer.entities.ResultItem;
import carScraperServer.repositories.CarMongoRepository;
import carScraperServer.repositories.CarsComMakeModelRepository;
import carScraperServer.scrapeEngine.CarPageProcessor;
import carScraperServer.scrapeEngine.PhantomInitializer;
import carScraperServer.scrapeEngine.SeleniumHelper;
import carScraperServer.scrapeEngine.TorProxyService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component
public class CarsComScrapeService {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    CarMongoRepository carMongoRepository;

    @Autowired
    CarsComMakeModelRepository carsComMakeModelRepository;

    @Autowired
    CarPageProcessor carPageProcessor;

    @Autowired
    ServletContext servletContext;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CarsComScrapeService.class);

    private final int maxThreads = 1;
    private Semaphore semaphore = new Semaphore(maxThreads);
    ExecutorService pageLoadAsyncExecutor;
    ExecutorService startAsyncExecutor = Executors.newFixedThreadPool(maxThreads);

    private String screenshotsFolder;
    private String phantomProxy;
    private String phantomProxyType;
    private String phantomBinaryPath;

    private volatile int totalItems;
    private volatile boolean shouldStop;

    private boolean isRunning;
    private boolean wasPreviousDriverSuccessful;

    private final String priceToFind = "No Max Price";
    private final String milesToFind = "All Miles";
    private final String zipToFind = "10001";
    private final String stockTypeToFind = "all"; //used, new, cpo, all


    public void stop() {
        LOG.info("Was stopped manually.");
        shouldStop = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void executeAsync() {
        startAsyncExecutor.execute(() -> {
            execute();
        });
    }

    public void execute() {
        try {
            if (isRunning) {
                throw new RuntimeException("Service is still working. If it was stopped it might require some time to finish correctly");
            }

            shouldStop = false;
            isRunning = true;
            try {
                File screenShotsDir = new File(screenshotsFolder);
                if (screenShotsDir.exists()) {
                    FileUtils.deleteDirectory(screenShotsDir);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            LOG.info("Initializing phantomjs driver...");
            Resource phantomJSResource = resourceLoader.getResource(phantomBinaryPath);
            if (!phantomJSResource.exists()) {
                throw new RuntimeException(String.format("Phantom js binary wasn't found. Looked in webapp by path %s", phantomBinaryPath));
            }

            String phantomBinaryFullPath = phantomJSResource.getURL().toString().replace("file:", "");
            WebDriver driver = PhantomInitializer.instance.getDriver(phantomProxy, phantomProxyType, phantomBinaryFullPath);

            //WebDriver driver = PhantomInitializer.instance.getDriver("195.58.245.69:3120");
            LOG.info("Phantomjs driver initialized successfully");


            //each cycle will process one make-model pair
            while (true) {
                try {
                    driver.get("http://www.cars.com");
                    if (driver.getCurrentUrl().equals("about:blank")) {
                        throw new RuntimeException("Failed to load cars.com main pages");
                    }
                    Thread.sleep(1000);

                    LOG.info("cars.com page opened successfully");
                    wasPreviousDriverSuccessful = true;

                    Boolean stockTypeSet = false;

                    List<WebElement> stockTypes = driver.findElements(By.name("stock-type"));
                    for (WebElement webElement : stockTypes) {
                        if (webElement.getAttribute("value").trim().toLowerCase().equals(stockTypeToFind.toLowerCase())) {

                            //Bug with selenium & phantom. Click via js
                            //webElement.click();
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
                            stockTypeSet = true;
                            break;
                        }
                    }

                    if (!stockTypeSet) {
                        throw new RuntimeException(String.format("Was unable to set stockType '%s'", stockTypeToFind));
                    }


                    String selectedMake = null;
                    String selectedModel = null;

                    //Find all makes
                    By makesSelector = By.cssSelector("[ng-model=\"selections.make\"]");
                    List<WebElement> makeOptions = new Select(driver.findElement(makesSelector)).getOptions();
                    makeLoop:
                    for (WebElement makeOption : makeOptions) {
                        if (makeOptions.indexOf(makeOption) == 0) {
                            continue; //skip All Makes option
                        }
                        String make = makeOption.getText();
                        makeOption.click();

                        By modelsSelector = By.cssSelector("[ng-model=\"selections.model\"]");
                        List<WebElement> modelOptions = new Select(driver.findElement(modelsSelector)).getOptions();
                        for (WebElement modelOption : modelOptions) {
                            if (modelOptions.indexOf(modelOption) == 0) {
                                continue; //skip All Models option
                            }
                            String model = modelOption.getText();
                            CarsComMakeModelPair loadedPair = carsComMakeModelRepository.findByMakeAndModel(make, model);
                            if (loadedPair == null) {
                                modelOption.click();
                                selectedMake = make;
                                selectedModel = model;
                                break makeLoop;
                            }
                        }
                    }

                    //WOW. we have loaded all makes and model
                    if (selectedMake == null && selectedModel == null) {
                        break;
                    }

                    LOG.info(String.format("Processing make '%s' and model '%s'", selectedMake, selectedModel));

                    selectOption(driver, By.cssSelector("[ng-model=\"selections.price\"]"), priceToFind, "Price");
                    selectOption(driver, By.cssSelector("[ng-model=\"selections.radius\"]"), milesToFind, "Miles");

                    WebElement zipElement = driver.findElement(By.name("zipField"));
                    zipElement.sendKeys(zipToFind);

                    WebElement goBtn = driver.findElement(By.cssSelector("input[value=\"Search\"]"));
                    goBtn.click();


                    makeScreenShot(driver, String.format("searchBtnClicked_%s_%s", selectedMake, selectedModel));

                    if (SeleniumHelper.waitForElement(driver, By.id("resultswrapper"), 5000l, 5) == null) {
                        makeScreenShot(driver, String.format("firstSearchFailed_%s_%s", selectedMake, selectedModel));
                        CarsComMakeModelPair carsComMakeModelPair = new CarsComMakeModelPair(selectedMake, selectedModel, false);
                        carsComMakeModelRepository.save(carsComMakeModelPair);
                        continue;
                    }

                    makeScreenShot(driver, String.format("successfullyLoaded_%s_%s", selectedMake, selectedModel));


                    int loadedItemsCount = processSearchResultPages(driver, selectedMake, selectedModel);

                    //successfully loaded all results for make and model
                    CarsComMakeModelPair carsComMakeModelPair = new CarsComMakeModelPair(selectedMake, selectedModel, true);
                    carsComMakeModelRepository.save(carsComMakeModelPair);
                    LOG.info(String.format("Loading for make '%s' and model '%s' is Finished. Total %d items",
                            selectedMake, selectedModel, loadedItemsCount));
                    if (shouldStop) {
                        return;
                    }
                } catch (UnreachableBrowserException e) {
                    //sometimes phantomjs can just crush so we need to reinitialize it.
                    if (wasPreviousDriverSuccessful) { //reinitialize only if previous run was successful. If not it might
                        //mean that phantomjs binary wasn't found and we should fail with exception
                        driver = PhantomInitializer.instance.getDriver(phantomProxy, phantomProxyType, phantomBinaryFullPath);
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            }

        } catch (Exception e) {

            LOG.error(String.format("SCRAPER ERROR: %s", ExceptionUtils.getStackTrace(e)));
            throw new RuntimeException(e);
        } finally {
            isRunning = false;
            TorProxyService.instance.stopTor();
            PhantomInitializer.instance.closeDriver();
        }
    }

    private int processSearchResultPages(WebDriver driver, String selectedMake, String selectedModel) throws InterruptedException, IOException {
        List<ResultItem> loadedItems = new ArrayList<>();
        final int[] totalLoadedForMakeModel = {0};

        //each cycle will process one search result page
        while (true) {
            List<WebElement> carLinks = driver.findElements(By.cssSelector("h4 a"));
            pageLoadAsyncExecutor = Executors.newFixedThreadPool(maxThreads);
            for (WebElement carLink : carLinks) {
                String url = carLink.getAttribute("href");

                ResultItem foundItem = carMongoRepository.findByUrl(url);
                if (foundItem != null) {
                    continue;
                }


                //if fact with 1 thread there is no need for synchronization
                //use semaphore to prevent queueing in the executor.
                //if implement more threads need to make Tor tunnel a bit more stable
                //and lower the timeout
                semaphore.acquire();
                pageLoadAsyncExecutor.execute(() -> {
                    ResultItem resultItem = carPageProcessor.process(url);
                    if (resultItem != null) {
                        loadedItems.add(resultItem);
                        totalItems++;
                        totalLoadedForMakeModel[0]++;
                    }
                    semaphore.release();
                });

                if (shouldStop) {
                    break;
                }
            }
            //wait unit all "in progress" requests are finished
            pageLoadAsyncExecutor.shutdown();
            pageLoadAsyncExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

            carMongoRepository.save(loadedItems);
            loadedItems.clear();

            if (shouldStop) {
                return totalLoadedForMakeModel[0];
            }

            WebElement nextPageBtn = SeleniumHelper.findByOrNull(driver, By.cssSelector("a.right[rel='next']"));
            if (nextPageBtn != null) {
                driver.get(nextPageBtn.getAttribute("href"));
                Thread.sleep(5000);
                if (SeleniumHelper.waitForElement(driver, By.id("resultswrapper"), 5000l, 5) == null) {
                    makeScreenShot(driver, String.format("failedToNavigateNextPage_%s_%s", selectedMake, selectedModel));
                    throw new RuntimeException("Failed to navigate next page");
                }
            } else {
                break;
            }
        }

        return totalLoadedForMakeModel[0];
    }

    private void makeScreenShot(WebDriver driver, String screenshotName) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File target = new File(screenshotsFolder + File.separator + screenshotName + ".png");
        target.getParentFile().mkdirs();
        FileUtils.copyFile(scrFile, target);

        File infoDir = new File(target.getParentFile(), "info");
        if (!infoDir.exists()) {
            infoDir.mkdir();
        }

        File namedDir = new File(infoDir, screenshotName);
        namedDir.mkdir();

        File urlFile = new File(namedDir, "url.txt");
        FileUtils.writeStringToFile(urlFile, driver.getCurrentUrl());

        File sourceFile = new File(namedDir, "source.txt");
        FileUtils.writeStringToFile(sourceFile, driver.getPageSource());
        LOG.info(String.format("%s:  Screenshot created %s", new Date().toString(), screenshotName));
    }

    private void selectOption(WebDriver webDriver, By by, String optionText, String fieldName) {
        try {
            new Select(webDriver.findElement(by)).selectByVisibleText(optionText);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Was unable to set %s '%s'", fieldName, optionText), e);
        }
    }


    public void setPhantomProxy(String phantomProxy) {
        this.phantomProxy = phantomProxy;
    }

    public void setPhantomProxyType(String phantomProxyType) {
        this.phantomProxyType = phantomProxyType;
    }

    @Required
    public void setPhantomBinaryPath(String phantomBinaryPath) {
        this.phantomBinaryPath = phantomBinaryPath;
    }

    @Required
    public void setScreenshotsFolder(String screenshotsFolder) {
        if (screenshotsFolder.startsWith("/")) {
            this.screenshotsFolder = screenshotsFolder;
        } else {
            this.screenshotsFolder = servletContext.getRealPath("/") + File.separator + screenshotsFolder;
        }
    }

    public int getTotalItems() {
        return totalItems;
    }
}
