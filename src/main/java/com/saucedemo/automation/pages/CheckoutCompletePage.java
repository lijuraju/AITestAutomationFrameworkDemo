package com.saucedemo.automation.pages;

import com.saucedemo.automation.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Checkout Complete page
 */
public class CheckoutCompletePage extends BasePage {
    
    @FindBy(className = "subheader")
    private WebElement pageTitle;
    
    @FindBy(className = "complete-header")
    private WebElement completeHeader;
    
    @FindBy(className = "complete-text")
    private WebElement completeText;
    
    @FindBy(className = "pony_express")
    private WebElement ponyExpressImage;
    
    @FindBy(xpath = "//button[contains(text(), 'BACK HOME')]")
    private WebElement backHomeButton;
    
    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if page is loaded
     *
     * @return true if page is loaded, false otherwise
     */
    public boolean isPageLoaded() {
        return isElementDisplayed(pageTitle) && isElementDisplayed(completeHeader) 
                && isElementDisplayed(completeText);
    }
    
    /**
     * Get complete header text
     *
     * @return Complete header text
     */
    public String getCompleteHeaderText() {
        return getText(completeHeader);
    }
    
    /**
     * Get complete text
     *
     * @return Complete text
     */
    public String getCompleteText() {
        return getText(completeText);
    }
    
    /**
     * Click back home button
     *
     * @return InventoryPage
     */
    public InventoryPage clickBackHome() {
        logger.info("Clicking back home");
        click(backHomeButton);
        return new InventoryPage(driver);
    }
    
    /**
     * Check if order completion is successful
     *
     * @return true if order completion is successful, false otherwise
     */
    public boolean isOrderSuccessful() {
        return isElementDisplayed(completeHeader) && 
                getCompleteHeaderText().contains("THANK YOU FOR YOUR ORDER");
    }
}