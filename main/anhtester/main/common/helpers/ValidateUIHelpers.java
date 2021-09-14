package anhtester.common.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class ValidateUIHelpers {

    private WebDriver driver;

    private Actions action;
    private Select select;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private final int timeoutWait = 10;
    private final int timeoutWaitForPageLoaded = 20;

    public ValidateUIHelpers(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, timeoutWait);
        js = (JavascriptExecutor) driver;
        action = new Actions(driver);
    }

    public static void sleep(int second)
    {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getPageTitle() {
        waitForPageLoaded();
        String title = driver.getTitle();
        System.out.println("Page Title: " + driver.getTitle());
        return title;
    }

    public boolean verifyPageTitle(String pageTitle) {
        waitForPageLoaded();
        return getPageTitle().equals(pageTitle);
    }

    public boolean verifyPageLoaded(String pageLoadedText) {
        waitForPageLoaded();
        Boolean res = false;
//        res = driver.getPageSource().toString().contains(pageLoadedText);
//        System.out.println("Page loaded (" + res + "): " + pageLoadedText);

        List<WebElement> elementList = driver.findElements(By.xpath("//*[contains(text(),'" + pageLoadedText + "')]"));
        if (elementList.size() > 0) {
            res = true;
            System.out.println("Page loaded (" + res + "): " + pageLoadedText);
        } else {
            res = false;
            System.out.println("Page loaded (" + res + "): " + pageLoadedText);
        }
        return res;
    }

    public boolean verifyElementText(By element, String text) {
        System.out.println("Element Text: " + driver.findElement(element).getText());
        return driver.findElement(element).getText().equals(text);
    }

    public boolean verifyPageUrl(String pageUrl) {
        System.out.println("Current URL: " + driver.getCurrentUrl());
        return driver.getCurrentUrl().contains(pageUrl); //trả ra true/false
    }

    public void setText(By element, String value) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        elementWaited.click();
        elementWaited.clear();
        elementWaited.sendKeys(value);
    }

    public void clickElement(By element) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        elementWaited.click();
    }

    public void clickElementWithJs(By element) {
        waitForPageLoaded();
        //Đợi đến khi element đó tồn tại
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        //Scroll to element với Js
        js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(element));
        //click với js
        js.executeScript("arguments[0].click();", driver.findElement(element));
    }

    public void rightClickElement(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        action.contextClick().build().perform();
    }

    //Handle dropdown select option
    public void selectOptionByText(By by, String Text) {
        select = new Select(driver.findElement(by));
        select.selectByVisibleText(Text);
    }

    public void selectOptionByValue(By by, String Value) {
        select = new Select(driver.findElement(by));
        select.selectByValue(Value);
    }

    public void selectOptionByIndex(By by, int Index) {
        select = new Select(driver.findElement(by));
        select.selectByIndex(Index);
    }

    public void verifyOptionTotal(By element, int total) {
        select = new Select(driver.findElement(element));
        Assert.assertEquals(total, select.getOptions().size());
    }

    public boolean verifySelectedByText(By by, String Text) {
        Select select = new Select(driver.findElement(by));
        System.out.println("Option Selected: " + select.getFirstSelectedOption().getText());
        return select.getFirstSelectedOption().getText().equals(Text);
    }

    public boolean verifySelectedByValue(By by, String Value) {
        Select select = new Select(driver.findElement(by));
        System.out.println("Option Selected: " + select.getFirstSelectedOption().getAttribute("value"));
        return select.getFirstSelectedOption().getAttribute("value").equals(Value);
    }

    public boolean verifySelectedByIndex(By by, int Index) {
        Boolean res = false;
        Select select = new Select(driver.findElement(by));
        int indexFirstOption = select.getOptions().indexOf(select.getFirstSelectedOption());
        System.out.println("First Option Selected Index: " + indexFirstOption);
        System.out.println("Expected Index: " + Index);
        if (indexFirstOption == Index) {
            res = true;
        } else {
            res = false;
        }
        System.out.println("==> " + res);
        return res;
    }

    //Handle frame iframe

    public void switchToFrameByIndex(int Index) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(Index));
        //driver.switchTo().frame(Index);
    }

    public void switchToFrameById(String IdOrName) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IdOrName));
        //driver.switchTo().frame(IdOrName);
    }

    public void switchToFrameByElement(By by) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
        //driver.switchTo().frame(driver.findElement(by));
    }

    public void switchToMainWindow() {
        driver.switchTo().defaultContent();
    }

    //Handle Alert
    public void alertAccept() {
        driver.switchTo().alert().accept();
    }

    public void alertDismiss() {
        driver.switchTo().alert().dismiss();
    }

    public void alertGetText() {
        driver.switchTo().alert().getText();
    }

    public void alertSetText(String text) {
        driver.switchTo().alert().sendKeys(text);
    }

    public boolean verifyAlertPresent() {
        if (wait.until(ExpectedConditions.alertIsPresent()) == null) {
            System.out.println("Alert not exists");
            return false;
        } else {
            System.out.println("Alert exists");
            return true;
        }
    }

    //Handle Elements
    public List<String> getElementsText(By elements, String phrase) {
        wait.until(ExpectedConditions.titleContains(phrase));
        wait.until(ExpectedConditions.visibilityOfElementLocated(elements));

        List<WebElement> listElement = driver.findElements(elements);
        List<String> listText = new LinkedList<>();

        for (WebElement e : listElement) {
            listText.add(e.getText());
        }

        return listText;
    }

    public boolean verifyElementExists(By element) {
        waitForPageLoaded();
        Boolean res = false;

        List<WebElement> elementList = driver.findElements(element);
        if (elementList.size() > 0) {
            res = true;
            System.out.println("Element existing");
        } else {
            res = false;
            System.out.println("Element not exists");
        }
        return res;
    }

    public void waitForPageLoaded() {
        try {
            wait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
//                    System.out.println("Current page load status: "
//                            + String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
                    return String
                            .valueOf(js.executeScript("return document.readyState"))
                            .equals("complete");
                }
            });
            //Thread.sleep(500);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load request.");
        }
    }

}
