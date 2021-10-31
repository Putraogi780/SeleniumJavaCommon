package anhtester.com.testcases;

import anhtester.com.common.BaseTest;
import anhtester.com.common.browsers.BrowserFactory;
import anhtester.com.common.helpers.*;
import anhtester.com.common.utilities.listeners.ListenersTest;
import anhtester.com.pages.crm.Dashboard.DashboardPage;
import anhtester.com.pages.crm.Projects.ProjectPage;
import anhtester.com.pages.crm.SignIn.SignInPage;
import anhtester.com.common.utilities.Props;
import org.openqa.selenium.*;
import org.testng.annotations.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

// @Listeners(ListenersTest.class)
public class TestHandle {

    WebDriver driver;
    UIHelpers uiHelpers;
    DatabaseHelpers databaseHelpers;
    SignInPage signInPage;
    DashboardPage dashboardPage;
    ProjectPage projectPage;

    @BeforeClass
    public void Setup() {
        driver = BaseTest.createDriver("chrome"); // Cách khởi tạo thứ 1
        // driver = new BrowserFactory().createDriver("chrome"); //Cách khởi tạo thứ 2
        uiHelpers = new UIHelpers(driver);
    }

    @Test
    public void testDragAndDrop() {
        driver.get("https://demos.telerik.com/kendo-ui/dragdrop/index");
        By fromElement = By.id("draggable");
        By toElement = By.id("droptarget");
        uiHelpers.scrollToElement(toElement);
        uiHelpers.dragAndDrop(fromElement, toElement);
        UIHelpers.sleep(3);
    }

    @Test
    public void testHighLightElement() {
        driver.get("https://hrm.anhtester.com/");
        By button = By.xpath("//button[@type='submit']");
        uiHelpers.highLightElement(button); // Tô màu viền đỏ cho Element trên website
        UIHelpers.logConsole(BrowserFactory.getInfo());
        UIHelpers.sleep(2);
    }

    @Test
    public void handleUploadFile() {

        driver.get("https://www.grammarly.com/plagiarism-checker");

        // Cách 1
        // uiHelpers.uploadFileForm(By.xpath("//div[text()='Upload a file']"),
        // "C:\\DOCX_File_01.docx");

        // Cách 2 phải lấy tới thẻ Input
        uiHelpers.uploadFileSendkeys(By.xpath("//input[@name='source_file']"),
                Helpers.getCurrentDir() + "src/test/resources/DOCX_File_01.docx");

        UIHelpers.sleep(5);
    }

    @Test
    public void connectDBMySQL() throws SQLException, ClassNotFoundException {
        // Này connect DB mẫu ở local máy An nhé. Các bạn đổi thông tin connect bên dưới
        // là được
        Connection connection = DatabaseHelpers.getMySQLConnection("localhost", "saleserp", "root", "");

        // Tạo đối tượng Statement.
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM `users` WHERE 1";

        // Thực thi câu lệnh SQL trả về đối tượng ResultSet.
        ResultSet rs = statement.executeQuery(sql);

        UIHelpers.logConsole(rs);

        // Duyệt trên kết quả trả về.
        while (rs.next()) {// Di chuyển con trỏ xuống bản ghi kế tiếp.
            int Id = rs.getInt(1);
            String userID = rs.getString("user_id");
            String last_name = rs.getString("last_name");
            String first_name = rs.getString("first_name");
            System.out.println("--------------------");
            System.out.println("userID:" + userID);
            System.out.println("last_name:" + last_name);
            System.out.println("first_name:" + first_name);
        }

        // Đóng kết nối
        connection.close();

    }

    @Test
    public void handleCreateFolder() {
        Helpers.CreateFolder("src/test/resources/newFolder");
        System.out.println(Helpers.CurrentDateTime());
    }

    @Test
    public void handleTable1() {
        projectPage = new ProjectPage(driver);
        driver.get("http://www.railway2.somee.com/Page/TrainTimeListPage.cshtml");
        System.out.println(uiHelpers.getValueTableByColumn(2));
    }

    @Test
    public void handleTable2() {
        signInPage = new SignInPage(driver);
        driver.get("https://crm.anhtester.com/signin");
        dashboardPage = signInPage.singIn("tld01@mailinator.com", "123456");
        projectPage = dashboardPage.openProjectPage();
        String dataSearch1 = "Project";
        String dataSearch2 = "Test";
        // Search cột 2 Title
        projectPage.searchByValue(dataSearch1);
        projectPage.checkContainsSearchTableByColumn(2, dataSearch1);
        // Search cột 3 Client
        projectPage.searchByValue(dataSearch2);
        projectPage.checkContainsSearchTableByColumn(3, dataSearch2);

    }

    @Test
    public void handlePrintPopup() throws AWTException {
        driver.get("https://pos.anhtester.com/login");
        driver.findElement(By.xpath("//td[normalize-space()='user01@anhtester.com']")).click();
        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        driver.findElement(By.xpath("//a[@role='button']")).click();
        // driver.findElement(By.xpath("//span[normalize-space()='Sale']")).click();
        uiHelpers.sleep(1);
        driver.findElement(By.xpath("//a[normalize-space()='Manage Sale']")).click();
        driver.findElement(By.xpath("//span[normalize-space()='Print']")).click();

        uiHelpers.sleep(1);

        Set<String> windowHandles = driver.getWindowHandles();
        if (!windowHandles.isEmpty()) {
            driver.switchTo().window((String) windowHandles.toArray()[windowHandles.size() - 1]);
        }

        // driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
        uiHelpers.sleep(2);
        Robot robotClass = new Robot();
        robotClass.keyPress(KeyEvent.VK_TAB);
        uiHelpers.sleep(1);
        robotClass.keyPress(KeyEvent.VK_ENTER);

        driver.switchTo().window(driver.getWindowHandles().toArray()[0].toString());
        // if (!windowHandles.isEmpty()) {
        // driver.switchTo().window((String)
        // windowHandles.toArray()[windowHandles.size() - 1]);
        // }
        uiHelpers.sleep(2);
    }

    @Test
    public void TestReadAndWriteTxtFile() {
        TxtFileHelpers.ReadTxtFile(Props.getValue("txtFilePath"));
    }

    @Test
    public void TestExcelFile() {
        // Handle Excel file
        ExcelHelpers.setExcelFile(Props.getValue("excelFilePath"), "Sheet1");
        System.out.println(ExcelHelpers.getCellData("username", 2));
        System.out.println(ExcelHelpers.getCellData("password", 2));
        System.out.println(ExcelHelpers.getCellData("pin", 2));
        ExcelHelpers.setCellData("pass", 1, 3);

    }

    @Test()
    public void handleExcelFile() throws Exception {

        ExcelHelpers.getDataArray("src/test/resources/Magento.xlsx", "storeProduct", 0, 2);
    }

    @Test(dataProvider = "login")
    public void loginDataProviderExcelArray(String Username, String Password) {

        System.out.println(Username);
        System.out.println(Password);

        driver.get("http://demoqa.com/login");
        driver.findElement(By.id("userName")).sendKeys(Username);
        driver.findElement(By.id("password")).sendKeys(Password);
        driver.findElement(By.id("login")).click();
    }

    @DataProvider
    public Object[][] login() {

        Object[][] testObjArray = ExcelHelpers.getDataArray("src/test/resources/Magento.xlsx", "Login", 2, 3);

        return (testObjArray);
    }

    // @DataProvider
    // public Object[][] Authentication2() throws Exception {
    //
    // // Setting up the Test Data Excel file
    //
    // ExcelHelpers.getDataArray("src/test/resources/Magento.xlsx", "Login", 2, 3);
    //
    // //ExcelHelpers.setExcelFile("src/test/resources/Magento.xlsx", "Sheet1");
    //
    // String sTestCaseName = this.toString();
    //
    // // From above method we get long test case name including package and class
    // name etc.
    //
    // // The below method will refine your test case name, exactly the name use
    // have used
    //
    // sTestCaseName = ExcelHelpers.getTestCaseName(this.toString());
    //
    // // Fetching the Test Case row number from the Test Data Sheet
    //
    // // Getting the Test Case name to get the TestCase row from the Test Data
    // Excel sheet
    //
    // int iTestCaseRow = ExcelHelpers.getRowContains(sTestCaseName, 3);
    //
    // Object[][] testObjArray =
    // ExcelHelpers.getDataArray("src/test/resources/Magento.xlsx", "Login", 1,
    // iTestCaseRow);
    //
    // return (testObjArray);
    //
    // }

    @Test
    public void TestPropertiesFile() {
        // Handle Properties file
        System.out.println(Props.getValue("browser"));
        System.out.println(Props.getValue("url"));
        System.out.println(Props.getValue("email"));
        System.out.println(Props.getValue("excelFilePath"));
        Props.setValue("anhtester", "https://anhtetser.com");
    }

    @AfterClass
    public void closeDriver() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

}
