package anhtester.com.common.browsers;

import anhtester.com.common.helpers.UIHelpers;
import anhtester.com.common.utilities.Constants;
import com.google.common.base.Supplier;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BrowserFactory {

    private static WebDriver driver;
    public static final int IMPLICIT_WAIT = Constants.IMPLICIT_WAIT;
    public static final int PAGE_LOAD_TIMEOUT  = Constants.PAGE_LOAD_TIMEOUT;

    private static Supplier<WebDriver> chromeDriver = () -> {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
        return driver;
    };

    private static Supplier<WebDriver> firefoxDriver = () -> {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
        return driver;
    };

    private static Supplier<WebDriver> edgeDriver = () -> {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        driver = new EdgeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
        return driver;
    };

    private static Supplier<WebDriver> operaDriver = () -> {
        WebDriverManager.operadriver().setup();
        OperaOptions options = new OperaOptions();
        driver = new OperaDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
        return driver;
    };

    private static Map<String, Supplier<WebDriver>> driverMap = new HashMap<>();

    public BrowserFactory() {
        driverMap.put("chrome", chromeDriver);
        driverMap.put("firefox", firefoxDriver);
        driverMap.put("edge", edgeDriver);
        driverMap.put("opera", operaDriver);
    }

    public static WebDriver createDriver(String browser) {
        System.out.println(browser + " browser running...");
        return driverMap.getOrDefault(browser.toLowerCase(), chromeDriver).get();
    }

//  Driver Global

    /**
     * Khai báo biến driverInstance thuộc đối tượng WebDriver từ TheadLocal
     */
    private static InheritableThreadLocal<WebDriver> driverInstance = new InheritableThreadLocal<>();

    /**
     * Lấy giá trị driver từ WebDriver trong ThreadLocal
     * @return một giá trị driver thuộc đối tượng WebDriver
     */
    public static WebDriver getDriver() {
        if (driverInstance.get() == null) {
            return null;
//            throw new IllegalStateException(
//                    "WebDriver has not been set, Please set WebDriver instance by BrowserFactory.setDriver...");
        } else
            return driverInstance.get();
    }

    /**
     * Khởi tạo giá trị tổng cho WebDriver.
     * @param driver giá trị driver mới
     */
    public static void setDriver(WebDriver driver) {
        driverInstance.set(driver);
    }

    /**
     * Removes the driver.
     */
    public static void closeDriver() {
        driverInstance.get().quit();
        driverInstance.remove();
    }

    public static String getInfo() {
        Capabilities cap = ((RemoteWebDriver) getDriver()).getCapabilities();
        String browserName = cap.getBrowserName();
        String platform = cap.getPlatform().toString();
        String version = cap.getVersion();
        return String.format("Browser: %s, Version: %s, Platform: %s", browserName, version, platform);
    }
}