package com.obs.seleniumbasics;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SeleniumCommands {
    public WebDriver driver;

    public void testInitialize(String browser,String url){
        if(browser.equalsIgnoreCase("chrome")) {
           WebDriverManager.chromedriver().setup();//driver.exe download
            driver=new ChromeDriver();
        } else if(browser.equalsIgnoreCase("edge")){
            WebDriverManager.edgedriver().setup();
            driver=new EdgeDriver();
        } else if(browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver=new FirefoxDriver();
        } else {
            try {
                throw new Exception("Browser value not defined");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();//to delete all cookies and get a fresh page
            //driver.get(url);
           // driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
    }
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser","url"})
    public void setUp(String browserName,String baseUrl) {
       // testInitialize(browserName,baseUrl);
        testInitialize("chrome","http://demowebshop.tricentis.com/");
    }
    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        if(result.getStatus()==ITestResult.FAILURE) {
            TakesScreenshot scrshot = (TakesScreenshot) driver;
            File screenshot = scrshot.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot,new File("./Screenshots/"+result.getName()+".png"));
        }
        driver.quit();
    }

    @Test(priority = 1,enabled = true,description = "TC_001_Verify Homepage Titile",groups = {"smoke","regression"})
    public void verifyHomePageTitle() {
        driver.get("http://demowebshop.tricentis.com/");
        String expTitle="Demo Web Shop";
        String actTitle=driver.getTitle();
        Assert.assertEquals(actTitle, expTitle,"Error:Invalid home page title found");
    }

    @Test
    public  void verifyWindowHandle() throws InterruptedException {
        driver.get("https://demo.guru99.com/popup.php");
        String parentWindow = driver.getWindowHandle();
        //System.out.println(parentWindow);
        driver.findElement(By.xpath("//a[text()='Click Here']")).click();
        Set<String> handleIds = driver.getWindowHandles();
        // System.out.println(handleIds);
        Iterator<String> itr = handleIds.iterator();
        while (itr.hasNext()) {
            String child = itr.next();
            if (!parentWindow.equals(child)) {
                driver.switchTo().window(child);
                driver.findElement(By.xpath("//input[@name='emailid']")).sendKeys("test@gmail.com");
                driver.findElement(By.xpath("//input[@name='btnLogin']")).click();
            }
        }
        driver.switchTo().window(parentWindow);
    }

    @Test
    public void verifyFileUploading() throws InterruptedException {
        driver.get("https://demo.guru99.com/test/upload/");
        driver.findElement(By.id("uploadfile_0")).sendKeys("C:\\Users\\davis\\Desktop\\assignment-selenium.docx");
        driver.findElement(By.id("terms")).click();
        driver.findElement(By.id("submitbutton")).click();
    }

    @Test
    public void verifyFileUploadingRobotClass() throws AWTException, InterruptedException {
        driver.get("http://my.monsterindia.com/create_account.html");
        StringSelection s = new StringSelection("C:\\Users\\davis\\Desktop\\assignment-selenium.docx");
        // Clipboard copyC:\Users\davis\Desktop\assignment-selenium.docx
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s,null);
        //identify element and click
        driver.findElement(By.xpath("//*[text()='Choose CV']")).click();
        Thread.sleep(5000);
        Robot r = new Robot();
        Thread.sleep(5000);
        r.keyPress(KeyEvent.VK_ENTER);
        Thread.sleep(5000);
        r.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(5000);
        r.keyPress(KeyEvent.VK_CONTROL);
        Thread.sleep(10000);
        r.keyPress(KeyEvent.VK_V);
        Thread.sleep(10000);
        r.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(10000);
        r.keyRelease(KeyEvent.VK_V);
        Thread.sleep(10000);
        r.keyPress(KeyEvent.VK_ENTER);
        Thread.sleep(5000);
        r.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(5000);
    }

    @Test
    public void verifyJavaScriptClickAndSendkeys(){
        driver.get("http://demowebshop.tricentis.com/");
        JavascriptExecutor js=(JavascriptExecutor) driver;
        String s1="document.getElementById('newsletter-email').value='tes@test.com'";
        String s2="document.getElementById('newsletter-subscribe-button').click()";
        js.executeScript(s1);
        js.executeScript(s2);
    }

    @Test(priority = 2,enabled = true,description = "TC_002_verify Valid login Excel",groups = {"smoke"})
    public void verifyValidloginExcel() throws IOException {
        ExcelUtility excelUtility = new ExcelUtility();
        //driver.get("http://demowebshop.tricentis.com/");
        driver.findElement(By.xpath("//a[text()='Log in']")).click();
        WebElement user = driver.findElement(By.xpath("//input[@id='Email']"));
        String user_value = excelUtility.readData(1, 0, "login");
        user.sendKeys(user_value);
        WebElement pass = driver.findElement(By.xpath("//input[@id='Password']"));
        String pass_value = excelUtility.readData(1, 1, "login");
        pass.sendKeys(pass_value);
        driver.findElement(By.xpath("//input[@class='button-1 login-button']")).click();
    }
    @Test(dataProvider = "loginExcelDataprovider")
    public void verifyLoginUsingDataProvider(String username, String password){
        driver.get("http://demowebshop.tricentis.com/");
        driver.findElement(By.xpath("//a[text()='Log in']")).click();
        WebElement user = driver.findElement(By.xpath("//input[@id='Email']"));
        user.sendKeys(username);
        WebElement pass = driver.findElement(By.xpath("//input[@id='Password']"));
        pass.sendKeys(password);
        driver.findElement(By.xpath("//input[@class='button-1 login-button']")).click();
    }
    @DataProvider(name="userlogindata")
    public Object[][] loginData(){
        Object[][] data=new Object[2][2];
        data[0][0]="selenium121@test.com";
        data[0][1]="12345678";
        data[1][0]="selenium111@test.com";
        data[1][1]="12345678";
        return data;
    }
    @DataProvider(name="loginExcelDataprovider")
    public Object[][] loginExcelData() throws IOException {
        ExcelUtility excelUtility=new ExcelUtility();
        String filepath = System.getProperty("user.dir") + "\\src\\main\\resources\\testdata.xlsx";
        Object[][] excelData=excelUtility.readDataFromExcel(filepath,"login");
        return excelData;
    }

}
