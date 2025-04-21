package com.saucedemo.automation.pages;

import com.saucedemo.automation.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Checkout Step One page
 */
public class CheckoutStepOnePage extends BasePage {
    
    @FindBy(className = "subheader")
    private WebElement pageTitle;
    
    @FindBy(id = "first-name")
    private WebElement firstNameInput;
    
    @FindBy(id = "last-name")
    private WebElement lastNameInput;
    
    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;
    
    @FindBy(xpath = "//input[@value='CONTINUE']")
    private WebElement continueButton;
    
    @FindBy(xpath = "//button[contains(text(), 'CANCEL')]")
    private WebElement cancelButton;
    
    @FindBy(xpath = "//h3[@data-test='error']")
    private WebElement errorMessage;
    
    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if page is loaded
     *
     * @return true if page is loaded, false otherwise
     */
    public boolean isPageLoaded() {
        return isElementDisplayed(pageTitle) && isElementDisplayed(firstNameInput) 
                && isElementDisplayed(lastNameInput) && isElementDisplayed(postalCodeInput);
    }
    
    /**
     * Enter first name
     *
     * @param firstName First name to enter
     * @return CheckoutStepOnePage for method chaining
     */
    public CheckoutStepOnePage enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        sendKeys(firstNameInput, firstName);
        return this;
    }
    
    /**
     * Enter last name
     *
     * @param lastName Last name to enter
     * @return CheckoutStepOnePage for method chaining
     */
    public CheckoutStepOnePage enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        sendKeys(lastNameInput, lastName);
        return this;
    }
    
    /**
     * Enter postal code
     *
     * @param postalCode Postal code to enter
     * @return CheckoutStepOnePage for method chaining
     */
    public CheckoutStepOnePage enterPostalCode(String postalCode) {
        logger.info("Entering postal code: {}", postalCode);
        sendKeys(postalCodeInput, postalCode);
        return this;
    }
    
    /**
     * Fill out form
     *
     * @param firstName  First name
     * @param lastName   Last name
     * @param postalCode Postal code
     * @return CheckoutStepOnePage for method chaining
     */
    public CheckoutStepOnePage fillForm(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        return this;
    }
    
    /**
     * Click continue button
     *
     * @return CheckoutStepTwoPage if successful, null if error
     */
    public CheckoutStepTwoPage clickContinue() {
        logger.info("Clicking continue");
        click(continueButton);
        
        // Check if error message is displayed
        if (isErrorMessageDisplayed()) {
            logger.warn("Checkout error: {}", getErrorMessage());
            return null;
        }
        
        return new CheckoutStepTwoPage(driver);
    }
    
    /**
     * Click cancel button
     *
     * @return CartPage
     */
    public CartPage clickCancel() {
        logger.info("Clicking cancel");
        click(cancelButton);
        return new CartPage(driver);
    }
    
    /**
     * Check if error message is displayed
     *
     * @return true if error message is displayed, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }
    
    /**
     * Get error message text
     *
     * @return Error message text
     */
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return getText(errorMessage);
        }
        return "";
    }
}