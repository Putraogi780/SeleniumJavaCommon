package anhtester.test.common;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import anhtester.automation.utils.logs.Log;
import anhtester.common.helpers.CaptureHelpers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

    //static String driverPath = "src\\main\\resources\\drivers\\";
    public WebDriver driver;
    private final int PageLoadTimeOut = 30;
    private final int ImplicitWaitTimeOut = 20;

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriver initDriverByBrowser(String browserType) {
        System.out.println("Launching Chrome browser...");
        switch (browserType) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "opera":
                WebDriverManager.operadriver().setup();
                driver = new OperaDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default:
                System.out.println("Browser: " + browserType + " is invalid, Launching Chrome as browser of choice...");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(PageLoadTimeOut, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(ImplicitWaitTimeOut, TimeUnit.SECONDS);
        return driver;
    }


    // Khởi tạo cấu hình của các Browser để đưa vào Switch Case

    public WebDriver initChromeDriver(String webURL) {
        System.out.println("Launching Chrome browser...");
        // System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.get(webURL);
        driver.manage().timeouts().pageLoadTimeout(PageLoadTimeOut, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(ImplicitWaitTimeOut, TimeUnit.SECONDS);
        return driver;
    }

    public WebDriver initFirefoxDriver(String webURL) {
        System.out.println("Launching Firefox browser...");
        // System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver.exe");
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.get(webURL);
        System.out.println(driver.getCurrentUrl());
        driver.manage().timeouts().pageLoadTimeout(PageLoadTimeOut, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(ImplicitWaitTimeOut, TimeUnit.SECONDS);
        return driver;
    }

    private WebDriver initOperaDriver(String webURL) {
        System.out.println("Launching Opera browser...");
        // System.setProperty("webdriver.opera.driver", driverPath + "operadriver.exe");
        WebDriverManager.operadriver().setup();
        driver = new OperaDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.get(webURL);
        driver.manage().timeouts().pageLoadTimeout(PageLoadTimeOut, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(ImplicitWaitTimeOut, TimeUnit.SECONDS);
        return driver;
    }

    // Hàm này để tùy chọn Browser
    private void setDriver(String browserType, String webURL) {
        switch (browserType) {
            case "chrome":
                driver = initChromeDriver(webURL);
                break;
            case "firefox":
                driver = initFirefoxDriver(webURL);
                break;
            case "opera":
                driver = initOperaDriver(webURL);
                break;
            default:
                System.out.println("Browser: " + browserType + " is invalid, Launching Chrome as browser of choice...");
                driver = initChromeDriver(webURL);
        }
    }

    // Chạy hàm initializeTestBaseSetup trước nhất trong project này (Theo TestNG)
    @Parameters({"browserType", "webURL"})
    @BeforeClass
    public void initializeTestBaseSetup(String browserType, String webURL) {
        try {
            // Thực thi để khởi tạo driver và browser
            setDriver(browserType, webURL);
            Log.info("Tests are starting!");
        } catch (Exception e) {
            System.out.println("Error initialize driver..." + e.getStackTrace());
        }
    }


    @AfterClass()
    public void tearDown() throws Exception {
        driver.quit();
        Log.info("Tests are ending!");
    }
}
