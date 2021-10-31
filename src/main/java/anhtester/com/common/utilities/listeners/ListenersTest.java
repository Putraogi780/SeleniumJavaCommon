package anhtester.com.common.utilities.listeners;

import static anhtester.com.common.utilities.extentreports.ExtentManager.getExtentReports;
import static anhtester.com.common.utilities.extentreports.ExtentTestManager.getTest;

import anhtester.com.common.browsers.BrowserFactory;
import anhtester.com.common.utilities.extentreports.ExtentTestManager;
import anhtester.com.common.utilities.logs.Log;
import anhtester.com.common.helpers.CaptureHelpers;
import com.aventstack.extentreports.Status;
import io.qameta.allure.Attachment;
import lombok.SneakyThrows;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Objects;

public class ListenersTest implements ITestListener {

    WebDriver driver = BrowserFactory.getDriver();

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    /**
     * Gets the test name.
     *
     * @param result the result
     * @return the test name
     */
    public String getTestName(ITestResult result) {
        return result.getTestName() != null ? result.getTestName()
                : result.getMethod().getConstructorOrMethod().getName();
    }

    /**
     * Gets the test description.
     *
     * @param result the result
     * @return the test description
     */
    public String getTestDescription(ITestResult result) {
        return result.getMethod().getDescription() != null ? result.getMethod().getDescription() : getTestName(result);
    }

    //Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    //HTML attachments for Allure
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    //Text attachments for Allure
    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @SneakyThrows
    @Override
    public void onStart(ITestContext iTestContext) {
        Log.info("Start testing " + iTestContext.getName());
        iTestContext.setAttribute("WebDriver", driver);
        //Gọi hàm startRecord video trong CaptureHelpers class
        CaptureHelpers.startRecord(iTestContext.getName());
    }

    @SneakyThrows
    @Override
    public void onFinish(ITestContext iTestContext) {
        Log.info("End testing " + iTestContext.getName());

        //Kết thúc và thực thi Extents Report
        getExtentReports().flush();
        //Gọi hàm stopRecord video trong CaptureHelpers class
        CaptureHelpers.stopRecord();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        Log.info(getTestName(iTestResult) + " test is starting...");
        ExtentTestManager.startTest(iTestResult.getName(), "");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        Log.info(getTestName(iTestResult) + " test is passed.");
        //ExtentReports log operation for passed tests.
        ExtentTestManager.logMessage(Status.PASS, getTestName(iTestResult) + " test is passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Log.info(getTestName(iTestResult) + " test is failed.");

        //Allure ScreenShotRobot and Screenshot custom
        if (driver != null) {
            System.out.println("Screenshot captured for test case: " + getTestName(iTestResult));
            saveScreenshotPNG(driver);
            CaptureHelpers.captureScreenshot(driver, getTestName(iTestResult));
        }
        //Save a log on Allure report.
        saveTextLog(getTestName(iTestResult) + " failed and screenshot taken!");

        ExtentTestManager.addScreenShot(Status.FAIL, getTestName(iTestResult));
        ExtentTestManager.logMessage(Status.FAIL, getTestName(iTestResult) + " test is failed");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Log.info(getTestName(iTestResult) + " test is skipped.");
        ExtentTestManager.logMessage(Status.SKIP, getTestName(iTestResult) + " test is skipped.");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Log.info("Test failed but it is in defined success ratio " + getTestName(iTestResult));
        ExtentTestManager.logMessage("Test failed but it is in defined success ratio " + getTestName(iTestResult));
    }
}
