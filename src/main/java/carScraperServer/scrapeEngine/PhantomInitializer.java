package carScraperServer.scrapeEngine;


import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public enum PhantomInitializer {
    instance;


    private WebDriver driver;

    //If phantomjs process died we should just remove our driver because it's nothing to quit now, process already died
    public void killDriver() {
        driver = null;
    }

    public WebDriver getDriver(String proxy, String phantomProxyType, String phantomBinaryPath) {
        closeDriver();
        DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
        List<String> cliArgsCap = new ArrayList<>();

        if (StringUtils.isNotEmpty(proxy)) {
            cliArgsCap.add(String.format("--proxy-type=%s", phantomProxyType));
            cliArgsCap.add(String.format("--proxy=%s", proxy));
        }

        cliArgsCap.add("--webdriver-loglevel=NONE");
        cliArgsCap.add("--web-security=no");
        cliArgsCap.add("--ignore-ssl-errors=yes");
        capabilities.setCapability(
                PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);

        capabilities.setCapability("phantomjs.binary.path", phantomBinaryPath);
        capabilities.setJavascriptEnabled(true);

        driver = new PhantomJSDriver(capabilities);
        driver.manage().timeouts().setScriptTimeout(20000, TimeUnit.MILLISECONDS);


        ((PhantomJSDriver) driver).executePhantomJS(String.format("var page = this;\n" +
                "page.settings.resourceTimeout = 7000;" +
                "page.onInitialized = function () {\n" +
                "    page.evaluate(function () {\n" +
                "        (function () {" +
                "           var width = %d;" +
                "           var height = %d;" +
                "           var navFoo = function(){};" +
                "           navFoo.prototype = window.navigator;" +
                "           navFoo.prototype.constructor = window.navigator.constructor;" +
                "           var customNavigator = new navFoo();" +
                "           customNavigator.platform = '%s';" +
                "           customNavigator.javaEnabled = function() {return false};" +
                "           window.navigator = customNavigator;" +
                "           var screenFoo = function(){};" +
                "           screenFoo.prototype = window.screen;" +
                "           screenFoo.prototype.constructor = window.screen.constructor;" +
                "           var newScreen = new screenFoo();" +
                "           newScreen.width = width;" +
                "           newScreen.availWidth = width;" +
                "           newScreen.height = height;" +
                "           newScreen.availHeight = height;" +
                "           window.screen = newScreen;" +
                "           localStorage.clear();" +
                "})();\n" +
                "    });\n" +
                "};", 1440, 900, "MacIntel"));

        driver.manage().window().setSize(new Dimension(1440, 900));
        driver.manage().deleteAllCookies();

        if (driver.manage().getCookies().size() > 0) {
            throw new RuntimeException("Cookies are not empty");
        }


        return driver;
    }

    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
