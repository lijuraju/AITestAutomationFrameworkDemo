package com.saucedemo.automation.pages;

import com.saucedemo.automation.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Login page
 */
public class LoginPage extends BasePage {
    
    @FindBy(id = "user-name")
    private WebElement usernameInput;
    
    @FindBy(id = "password")
    private WebElement passwordInput;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(xpath = "//h3[@data-test='error']")
    private WebElement errorMessage;
    
    @FindBy(className = "login_logo")
    private WebElement loginLogo;
    
    @FindBy(className = "bot_column")
    private WebElement botImage;
    
    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if page is loaded
     *
     * @return true if page is loaded, false otherwise
     */
    public boolean isPageLoaded() {
        return isElementDisplayed(loginLogo) && isElementDisplayed(usernameInput) 
                && isElementDisplayed(passwordInput) && isElementDisplayed(loginButton);
    }
    
    /**
     * Enter username
     *
     * @param username Username to enter
     * @return LoginPage for method chaining
     */
    public LoginPage enterUsername(String username) {
        logger.info("Entering username: {}", username);
        sendKeys(usernameInput, username);
        return this;
    }
    
    /**
     * Enter password
     *
     * @param password Password to enter
     * @return LoginPage for method chaining
     */
    public LoginPage enterPassword(String password) {
        logger.info("Entering password");
        sendKeys(passwordInput, password);
        return this;
    }
    
    /**
     * Click login button
     *
     * @return InventoryPage if login is successful
     */
    public InventoryPage clickLoginButton() {
        logger.info("Clicking login button");
        click(loginButton);
        // Check if error message is displayed
        if (isErrorMessageDisplayed()) {
            logger.warn("Login failed: {}", getErrorMessage());
            return null;
        }
        return new InventoryPage(driver);
    }
    
    /**
     * Login with provided credentials
     *
     * @param username Username
     * @param password Password
     * @return InventoryPage if login is successful
     */
    public InventoryPage login(String username, String password) {
        logger.info("Logging in with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
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