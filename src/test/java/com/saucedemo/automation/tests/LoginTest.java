package com.saucedemo.automation.tests;

import com.saucedemo.automation.core.BaseTest;
import com.saucedemo.automation.pages.InventoryPage;
import com.saucedemo.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for Login functionality
 */
public class LoginTest extends BaseTest {
    
    @Test(description = "Verify successful login with standard user")
    public void testSuccessfulLogin() {
        // Navigate to base URL
        navigateToBaseUrl();
        
        // Initialize LoginPage
        LoginPage loginPage = new LoginPage(driver);
        
        // Verify login page is displayed
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        // Login with standard user
        String username = config.getProperty("standard_user");
        String password = config.getProperty("password");
        
        InventoryPage inventoryPage = loginPage.login(username, password);
        
        // Verify redirect to inventory page
        Assert.assertNotNull(inventoryPage, "Inventory page should not be null after successful login");
        Assert.assertTrue(inventoryPage.isPageLoaded(), "Inventory page should be loaded after successful login");
    }
    
    @Test(description = "Verify login failure with locked out user")
    public void testLockedOutUser() {
        // Navigate to base URL
        navigateToBaseUrl();
        
        // Initialize LoginPage
        LoginPage loginPage = new LoginPage(driver);
        
        // Login with locked out user
        String username = config.getProperty("locked_out_user");
        String password = config.getProperty("password");
        
        loginPage.login(username, password);
        
        // Verify error message
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for locked out user");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Sorry, this user has been locked out"), 
                "Error message should indicate user is locked out");
    }
    
    @Test(description = "Verify login failure with invalid credentials")
    public void testInvalidCredentials() {
        // Navigate to base URL
        navigateToBaseUrl();
        
        // Initialize LoginPage
        LoginPage loginPage = new LoginPage(driver);
        
        // Login with invalid credentials
        loginPage.login("invalid_user", "invalid_password");
        
        // Verify error message
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid credentials");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"), 
                "Error message should indicate invalid credentials");
    }
    
    @Test(description = "Verify login with problem user")
    public void testProblemUser() {
        // Navigate to base URL
        navigateToBaseUrl();
        
        // Initialize LoginPage
        LoginPage loginPage = new LoginPage(driver);
        
        // Login with problem user
        String username = config.getProperty("problem_user");
        String password = config.getProperty("password");
        
        InventoryPage inventoryPage = loginPage.login(username, password);
        
        // Verify redirect to inventory page
        Assert.assertNotNull(inventoryPage, "Inventory page should not be null after successful login");
        Assert.assertTrue(inventoryPage.isPageLoaded(), "Inventory page should be loaded after successful login");
        
        // Additional checks specific to problem user could be added here
    }
    
    @Test(description = "Verify logout functionality")
    public void testLogout() {
        // Navigate to base URL
        navigateToBaseUrl();
        
        // Initialize LoginPage
        LoginPage loginPage = new LoginPage(driver);
        
        // Login with standard user
        String username = config.getProperty("standard_user");
        String password = config.getProperty("password");
        
        InventoryPage inventoryPage = loginPage.login(username, password);
        
        // Verify redirect to inventory page
        Assert.assertTrue(inventoryPage.isPageLoaded(), "Inventory page should be loaded after successful login");
        
        // Logout
        loginPage = inventoryPage.logout();
        
        // Verify redirect to login page
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded after logout");
    }
}