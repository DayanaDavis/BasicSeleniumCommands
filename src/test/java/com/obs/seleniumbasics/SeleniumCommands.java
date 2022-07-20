package com.obs.seleniumbasics;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

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
            //driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
    }
    @BeforeMethod
    public void setUp() {
        testInitialize("chrome","http://demowebshop.tricentis.com/");
    }
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
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
    public void verifyLogin(){
        System.out.println("Hi ..");
    }

}
