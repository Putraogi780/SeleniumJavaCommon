package anhtester.com.common.helpers;

import anhtester.com.common.utilities.Constants;
import anhtester.com.common.utilities.Props;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UIHelpers {

    private static WebDriver driver;

    private Actions action;
    private Robot robot;
    private Select select;
    private WebDriverWait wait;
    private static JavascriptExecutor js;
    public static final int IMPLICIT_WAIT = Constants.IMPLICIT_WAIT;
    public static final int PAGE_LOAD_TIMEOUT  = Constants.PAGE_LOAD_TIMEOUT;

    public UIHelpers(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, IMPLICIT_WAIT);
        js = (JavascriptExecutor) driver;
        action = new Actions(driver);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm một element
     *
     * @param by là đối tượng By
     * @return Trả về là một đối tượng WebElement
     */
    public WebElement findWebElement(By by) {
        return driver.findElement(by);
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm nhiều element
     *
     * @param by là đối tượng By
     * @return Trả về là Danh sách đối tượng WebElement
     */
    public List<WebElement> findWebElements(By by) {
        return driver.findElements(by);
    }

    /**
     * In ra câu message trong Console log
     *
     * @param object truyền vào object bất kỳ
     */
    public static void logConsole(@Nullable Object object) {
        System.out.println(object);
    }

    /**
     * Chờ đợi ép buộc với đơn vị là Giây
     *
     * @param second là số nguyên dương tương ứng số Giây
     */
    public static void sleep(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Upload file kiểu click hiện form chọn file local trong máy tính của bạn
     * @param by truyền vào element dạng đối tượng By
     * @param filePath đường dẫn tuyệt đối đến file trên máy tính của bạn
     */
    public void uploadFileForm(By by, String filePath) {

        //js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(by));
        //Click để mở form upload
        findWebElement(by).click();
        sleep(2);

        // Khởi tạo Robot class
        Robot rb = null;
        try {
            rb = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // Copy File path vào Clipboard
        StringSelection str = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);

        // Nhấn Control+V để dán
        rb.keyPress(KeyEvent.VK_CONTROL);
        rb.keyPress(KeyEvent.VK_V);

        // Xác nhận Control V trên
        rb.keyRelease(KeyEvent.VK_CONTROL);
        rb.keyRelease(KeyEvent.VK_V);

        // Nhấn Enter
        rb.keyPress(KeyEvent.VK_ENTER);
        rb.keyRelease(KeyEvent.VK_ENTER);
    }

    /**
     * Upload file kiểu kéo link trực tiếp vào ô input
     * @param by truyền vào element dạng đối tượng By
     * @param filePath đường dẫn tuyệt đối đến file
     */
    public void uploadFileSendkeys(By by, String filePath) {

        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(by));
        sleep(1);
        //Dán link file vào ô upload
        findWebElement(by).sendKeys(filePath);
    }

    public String getCurrentUrl() {
        waitForPageLoaded();
        System.out.println("Current Url page: " + driver.getCurrentUrl());
        return driver.getCurrentUrl();
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

    public boolean verifyPageContains(String text) {
        return driver.getPageSource().contains(text);
    }

    public boolean verifyPageLoaded(String pageUrl) {
        waitForPageLoaded();
        boolean res = false;

        if (driver.getCurrentUrl().trim().toLowerCase().contains(pageUrl.trim().toLowerCase())) {
            res = true;
        } else {
            res = false;
            System.out.println("Page " + pageUrl + " NOT loaded.");
        }
        return res;
    }

    public boolean verifyPageUrl(String pageUrl) {
        System.out.println("Current URL: " + driver.getCurrentUrl());
        return driver.getCurrentUrl().contains(pageUrl);
    }

    //Handle dropdown select option

    /**
     * Chọn giá trị trong dropdown với dạng động (không phải Select Option thuần)
     * @param bys element cùng loại (nhiều giá trị) dạng đối tượng By
     * @param text giá trị cần chọn dạng Text của item
     * @return click chọn một item chỉ định với giá trị Text
     */
    public boolean selectOptionOther(By bys, String text) {
        //Đối với dropdown động (div, li, span,...không phải dạng select option)
        try {
            List<WebElement> elements = driver.findElements(bys);

            for (WebElement element : elements) {
                System.out.println(element.getText());
                if (element.getText().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                    element.click();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void selectOptionByText(By by, String Text) {
        select = new Select(findWebElement(by));
        select.selectByVisibleText(Text);
    }

    public void selectOptionByValue(By by, String Value) {
        select = new Select(findWebElement(by));
        select.selectByValue(Value);
    }

    public void selectOptionByIndex(By by, int Index) {
        select = new Select(findWebElement(by));
        select.selectByIndex(Index);
    }

    public void verifyOptionTotal(By element, int total) {
        select = new Select(findWebElement(element));
        Assert.assertEquals(total, select.getOptions().size());
    }

    public boolean verifySelectedByText(By by, String Text) {
        Select select = new Select(findWebElement(by));
        System.out.println("Option Selected: " + select.getFirstSelectedOption().getText());
        return select.getFirstSelectedOption().getText().equals(Text);
    }

    public boolean verifySelectedByValue(By by, String optionValue) {
        Select select = new Select(findWebElement(by));
        System.out.println("Option Selected: " + select.getFirstSelectedOption().getAttribute("value"));
        return select.getFirstSelectedOption().getAttribute("value").equals(optionValue);
    }

    public boolean verifySelectedByIndex(By by, int Index) {
        Boolean res = false;
        Select select = new Select(findWebElement(by));
        int indexFirstOption = select.getOptions().indexOf(select.getFirstSelectedOption());
        System.out.println("First Option Selected Index: " + indexFirstOption);
        System.out.println("Expected Index: " + Index);
        if (indexFirstOption == Index) {
            res = true;
        } else {
            res = false;
        }
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
        //driver.switchTo().frame(findWebElement(by));
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

    public List<String> getListElementsText(By by) {
        waitForPageLoaded();
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));

        List<WebElement> listElement = findWebElements(by);
        List<String> listText = new ArrayList<>();

        for (WebElement e : listElement) {
            listText.add(e.getText());
        }

        return listText;
    }

    public boolean verifyElementExists(By by) {
        waitForPageLoaded();
        boolean res = false;

        List<WebElement> elementList = findWebElements(by);
        if (elementList.size() > 0) {
            res = true;
            System.out.println("Element existing");
        } else {
            res = false;
            System.out.println("Element not exists");
        }
        return res;
    }

    public boolean verifyElementText(By by, String text) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return getTextElement(by).equals(text);
    }

    public boolean verifyElementToBeClickable(By by) {
        try{
            wait.until(ExpectedConditions.elementToBeClickable(by));
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean verifyElementPresent(By by) {
        try{
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean verifyElementVisibility(By by) {
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void scrollToElement(By element) {
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(element));
    }

    public void scrollToPosition(int X, int Y) {
        js.executeScript("window.scrollTo(" + X + "," + Y + ");");
    }

    public boolean hoverElement(By by) {
        try {
            action.moveToElement(findWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean MouseHover(By by) {
        try {
            action.moveToElement(findWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dragAndDrop(By fromElement, By toElement) {
        try {
            waitForPageLoaded();
            action.dragAndDrop(findWebElement(fromElement), findWebElement(toElement));
            //action.clickAndHold(findWebElement(fromElement)).moveToElement(findWebElement(toElement)).release(findWebElement(toElement)).build().perform();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean pressENTER() {
        try {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean pressESC() {
        try {
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean pressF11() {
        try {
            robot.keyPress(KeyEvent.VK_F11);
            robot.keyRelease(KeyEvent.VK_F11);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     *
     * @param by truyền vào đối tượng element dạng By
     * @return Tô màu viền đỏ cho Element trên website
     */
    public WebElement highLightElement(By by) {
        // draw a border around the found element
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", findWebElement(by));
            sleep(1);
        }
        return findWebElement(by);
    }

    /**
     * Điền giá trị vào ô Text
     * @param by element dạng đối tượng By
     * @param value giá trị cần điền vào ô text
     */
    public void setText(By by, String value) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        elementWaited.sendKeys(value);
    }

    /**
     * Click chuột vào đối tượng Element trên web
     * @param by element dạng đối tượng By
     */
    public void clickElement(By by) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        elementWaited.click();
    }

    /**
     * Click chuột vào Element trên web với Javascript
     * @param by element dạng đối tượng By
     */
    public void clickElementWithJs(By by) {
        waitForPageLoaded();
        //Đợi đến khi element đó tồn tại
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        //Scroll to element với Js
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement(by));
        //click với js
        js.executeScript("arguments[0].click();", findWebElement(by));
    }

    public void clickLinkText(String linkText) {
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkText)));
        elementWaited.click();
    }

    /**
     * Click chuột phải vào đối tượng Element trên web
     * @param by element dạng đối tượng By
     */
    public void rightClickElement(By by) {
        waitForPageLoaded();
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        action.contextClick().build().perform();
    }

    public String getTextElement(By by){
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getText();
    }

    public String getAttributeElement(By by, String attributeValue){
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getAttribute(attributeValue);
    }

    public String getCssValueElement(By by, String cssAttribute){
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getCssValue(cssAttribute);
    }

    public Dimension getSizeElement(By by){
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getSize();
    }

    public Point getLocationElement(By by){
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getLocation();
    }

    public String getTagNameElement(By by){
        waitForPageLoaded();
        WebElement elementWaited = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elementWaited.getTagName();
    }

    //Handle Table
    public static void checkContainsSearchTableByColumn(int column, String value) {
        List<WebElement> totalRows = driver.findElements(By.xpath("//tbody/tr"));
        UIHelpers.sleep(1);
        System.out.println("");
        System.out.println("Số kết quả cho từ khóa (" + value + "): " + totalRows.size());

        for (int i = 1; i <= totalRows.size(); i++) {
            boolean res = false;
            WebElement title = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
            js.executeScript("arguments[0].scrollIntoView(true);", title);
            res = title.getText().toUpperCase().contains(value.toUpperCase());
            System.out.println("Dòng thứ " + i + ": " + res + " - " + title.getText());
            Assert.assertTrue(res, "Dòng thứ " + i + " (" + title.getText() + ")" + " không chứa giá trị " + value);
        }
    }

    public static ArrayList getValueTableByColumn(int column) {
        List<WebElement> totalRows = driver.findElements(By.xpath("//tbody/tr"));
        UIHelpers.sleep(1);
        System.out.println("");
        System.out.println("Số kết quả cho cột (" + column + "): " + totalRows.size());

        ArrayList arrayList = new ArrayList<String>();

        for (int i = 1; i <= totalRows.size(); i++) {
            boolean res = false;
            WebElement title = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
            arrayList.add(title.getText());
            System.out.println("Dòng thứ " + i + ":" + title.getText()); //Không thích in coi chơi thì xóa nhen
        }

        return arrayList;
    }

    // Wait Page loaded

    public void waitForPageLoaded() {
        // wait for jQuery to loaded
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };

        // wait for Javascript to loaded
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        try {
            wait = new WebDriverWait(driver, PAGE_LOAD_TIMEOUT );
            wait.until(jQueryLoad);
            wait.until(jsLoad);
        } catch (Throwable error) {
            Assert.fail("Quá thời gian load trang.");
        }
    }

    //Wait for Angular Load
    protected void waitForAngularLoad() {
        final String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        //Wait for ANGULAR to load
        ExpectedCondition<Boolean> angularLoad = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    return Boolean.valueOf(((JavascriptExecutor) driver).executeScript(angularReadyScript).toString());
                }
                catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };

        //Get Angular is Ready
        boolean angularReady = Boolean.valueOf(js.executeScript(angularReadyScript).toString());

        //Wait ANGULAR until it is Ready!
        if(!angularReady) {
            System.out.println("ANGULAR is NOT Ready!");
            //Wait for Angular to load
            wait.until(angularLoad);
        } else {
            System.out.println("ANGULAR is Ready!");
        }
    }

}
