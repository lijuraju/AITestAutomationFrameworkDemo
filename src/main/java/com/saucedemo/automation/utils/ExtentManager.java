package com.saucedemo.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class to manage ExtentReports
 */
public class ExtentManager {
    
    private static final String REPORT_DIR = "test-output/extent-reports";
    private static ExtentReports extentReports;
    
    /**
     * Create and configure ExtentReports
     *
     * @return Configured ExtentReports instance
     */
    public static ExtentReports createExtentReports() {
        if (extentReports == null) {
            extentReports = new ExtentReports();
            
            // Create report directory if it doesn't exist
            File reportDir = new File(REPORT_DIR);
            if (!reportDir.exists() && !reportDir.mkdirs()) {
                System.err.println("Failed to create report directory: " + reportDir.getAbsolutePath());
            }
            
            // Generate report filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportFilePath = REPORT_DIR + "/TestReport_" + timestamp + ".html";
            
            // Configure ExtentSparkReporter
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("SauceDemo Test Automation Report");
            sparkReporter.config().setReportName("SauceDemo Test Results");
            sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
            
            // Attach reporter to ExtentReports
            extentReports.attachReporter(sparkReporter);
            
            // Set system info
            extentReports.setSystemInfo("Application", "SauceDemo v1");
            extentReports.setSystemInfo("Environment", "QA");
            extentReports.setSystemInfo("Browser", ConfigReader.getInstance().getProperty("browser", "Chrome"));
            extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        
        return extentReports;
    }
}