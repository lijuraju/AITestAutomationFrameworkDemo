package com.saucedemo.automation.core;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.saucedemo.automation.utils.ConfigReader;
import com.saucedemo.automation.utils.ExtentManager;
import com.saucedemo.automation.utils.ScreenshotUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * Base class for all test classes
 */
public class BaseTest {
    
    protected WebDriver driver;
    protected static ExtentReports extentReports;
    protected ExtentTest extentTest;
    protected ConfigReader config = ConfigReader.getInstance();
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    
    @BeforeSuite
    public void beforeSuite() {
        extentReports = ExtentManager.createExtentReports();
    }
    
    @BeforeMethod
    public void beforeMethod(Method method) {
        logger.info("Starting test: {}", method.getName());
        extentTest = extentReports.createTest(method.getName(), method.getAnnotation(Test.class).description());
        
        setupDriver();
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Integer.parseInt(config.getProperty("timeout.element", "15"))));
        
        // Maximize window
        driver.manage().window().maximize();
    }
    
    /**
     * Set up WebDriver based on configuration
     */
    private void setupDriver() {
        String browser = config.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(config.getProperty("headless", "false"));
        
        logger.info("Setting up {} browser, headless: {}", browser, headless);
        
        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver(edgeOptions);
                break;
                
            case "safari":
                // Safari doesn't support headless mode
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
                break;
                
            default:
                logger.warn("Browser {} not recognized, using Chrome", browser);
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }
    }
    
    /**
     * Navigate to base URL
     */
    protected void navigateToBaseUrl() {
        String baseUrl = config.getProperty("url.base");
        logger.info("Navigating to: {}", baseUrl);
        driver.get(baseUrl);
    }
    
    @AfterMethod
    public void afterMethod(ITestResult result) {
        // Capture test result status
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test failed: {}", result.getName());
            extentTest.log(Status.FAIL, result.getThrowable());
            
            // Capture screenshot on failure
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getName());
            extentTest.addScreenCaptureFromPath(screenshotPath);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test passed: {}", result.getName());
            extentTest.log(Status.PASS, "Test passed");
        } else {
            logger.info("Test skipped: {}", result.getName());
            extentTest.log(Status.SKIP, "Test skipped");
        }
        
        // Quit driver
        if (driver != null) {
            driver.quit();
        }
    }
    
    @AfterSuite
    public void afterSuite() {
        // Flush ExtentReports
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}