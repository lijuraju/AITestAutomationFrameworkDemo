package com.saucedemo.automation.core;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base class for all Page Object classes
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final int DEFAULT_TIMEOUT = 15;
    
    /**
     * Constructor to initialize the page objects
     *
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Wait for element to be visible
     *
     * @param element WebElement to wait for
     * @return WebElement once it's visible
     */
    protected WebElement waitForElementVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Element not visible: {}", element);
            throw e;
        }
    }
    
    /**
     * Wait for element to be clickable
     *
     * @param element WebElement to wait for
     * @return WebElement once it's clickable
     */
    protected WebElement waitForElementClickable(WebElement element) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            logger.error("Element not clickable: {}", element);
            throw e;
        }
    }
    
    /**
     * Safe click on element with wait
     *
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        try {
            waitForElementClickable(element).click();
        } catch (StaleElementReferenceException e) {
            logger.warn("Stale element reference, retrying click");
            waitForElementClickable(element).click();
        }
    }
    
    /**
     * Safe sendKeys with wait
     *
     * @param element WebElement to send keys to
     * @param text    Text to enter
     */
    protected void sendKeys(WebElement element, String text) {
        try {
            WebElement visibleElement = waitForElementVisible(element);
            visibleElement.clear();
            visibleElement.sendKeys(text);
        } catch (StaleElementReferenceException e) {
            logger.warn("Stale element reference, retrying sendKeys");
            WebElement visibleElement = waitForElementVisible(element);
            visibleElement.clear();
            visibleElement.sendKeys(text);
        }
    }
    
    /**
     * Get text from element with wait
     *
     * @param element WebElement to get text from
     * @return Text of the element
     */
    protected String getText(WebElement element) {
        return waitForElementVisible(element).getText();
    }
    
    /**
     * Check if element is displayed
     *
     * @param element WebElement to check
     * @return true if element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
    
    /**
     * Scroll element into view
     *
     * @param element WebElement to scroll to
     */
    protected void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            // Small pause to allow the page to settle after scrolling
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            logger.error("Failed to scroll to element", e);
        }
    }
}