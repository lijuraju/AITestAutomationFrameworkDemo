package com.saucedemo.automation.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for taking screenshots
 */
public class ScreenshotUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "test-output/screenshots";
    
    /**
     * Capture screenshot and save to file
     *
     * @param driver    WebDriver instance
     * @param testName  Name of the test
     * @return          Path to the screenshot file
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            logger.error("Driver is null, cannot capture screenshot");
            return null;
        }
        
        try {
            // Create directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists() && !screenshotDir.mkdirs()) {
                logger.error("Failed to create screenshot directory: {}", screenshotDir.getAbsolutePath());
            }
            
            // Generate unique filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + "/" + filename;
            
            // Take screenshot and save to file
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(screenshotFile, destFile);
            
            logger.info("Screenshot saved to: {}", destFile.getAbsolutePath());
            return destFile.getAbsolutePath();
            
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
            return null;
        }
    }
}