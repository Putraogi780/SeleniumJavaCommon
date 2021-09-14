package anhtester.automation.common;

import anhtester.automation.pages.dms.DocumentManagementPage;
import anhtester.common.helpers.ValidateUIHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class CommonTestClass {

    private WebDriver driver;
    private ValidateUIHelpers validateUIHelper;

    public CommonTestClass(WebDriver driver) {
        this.driver = driver;
        validateUIHelper = new ValidateUIHelpers(driver);
    }

    private String headerPinFromText = "Change Confirmation";

    //Commons Element
    private By headerPinForm = By.xpath("//h3[@class='header ng-binding']");
    private By pinInput = By.xpath("//input[@id='changeconfirm_modal_password']");
    private By reasonForChangeInput = By.xpath("//input[@id='changeconfirm_modal_reason']");
    private By cancelBtn = By.xpath("//div[@class='actions']/button[contains(text(),'Cancel')]");
    private By submitBtn = By.xpath("//button[@id='changeconfirm_modal_submit']");

    private By contentAlertMessage = By.cssSelector("div#pht_notification_box>div>p");
    private By closeAlertMessageBtn = By.className("notify-close-btn");

    public void submitPIN(String PIN, String ReasonForChange) throws InterruptedException {
        validateUIHelper.waitForPageLoaded();
        validateUIHelper.verifyElementText(headerPinForm, headerPinFromText);
        validateUIHelper.setText(pinInput, PIN);
        validateUIHelper.setText(reasonForChangeInput, ReasonForChange);
        validateUIHelper.clickElement(submitBtn);
    }

    public void validateAlertMessage(String message) {
        validateUIHelper.waitForPageLoaded();
        WebElement element = driver.findElement(contentAlertMessage);
        System.out.println("Alert message: " + element.getText());
        Assert.assertTrue(element.getText().equals(message), "The message does not match.");
        validateUIHelper.clickElement(closeAlertMessageBtn);
    }

}
