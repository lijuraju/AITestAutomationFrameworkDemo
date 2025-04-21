package com.saucedemo.automation.tests;

import com.saucedemo.automation.core.BaseTest;
import com.saucedemo.automation.pages.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for Checkout functionality
 */
public class CheckoutTest extends BaseTest {
    
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    
    @BeforeMethod
    public void setupTest() {
        // Navigate to base URL
        navigateToBaseUrl();
        
        // Login and add items to cart
        loginPage = new LoginPage(driver);
        String username = config.getProperty("standard_user");
        String password = config.getProperty("password");
        
        inventoryPage = loginPage.login(username, password);
        Assert.assertTrue(inventoryPage.isPageLoaded(), "Inventory page should be loaded after login");
        
        // Add items to cart
        inventoryPage.addItemToCart("Sauce Labs Backpack");
        inventoryPage.addItemToCart("Sauce Labs Fleece Jacket");
    }
    
    @Test(description = "Verify complete checkout process")
    public void testCompleteCheckout() {
        // Navigate to cart
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");
        
        // Proceed to checkout
        CheckoutStepOnePage checkoutStepOne = cartPage.clickCheckout();
        Assert.assertTrue(checkoutStepOne.isPageLoaded(), "Checkout step one page should be loaded");
        
        // Fill out customer information
        checkoutStepOne.fillForm("John", "Doe", "12345");
        
        // Continue to checkout step two
        CheckoutStepTwoPage checkoutStepTwo = checkoutStepOne.clickContinue();
        Assert.assertTrue(checkoutStepTwo.isPageLoaded(), "Checkout step two page should be loaded");
        
        // Verify item count in checkout
        Assert.assertEquals(checkoutStepTwo.getCartItemCount(), 2, "Checkout should have 2 items");
        
        // Complete checkout
        CheckoutCompletePage checkoutComplete = checkoutStepTwo.clickFinish();
        Assert.assertTrue(checkoutComplete.isPageLoaded(), "Checkout complete page should be loaded");
        
        // Verify order confirmation
        Assert.assertTrue(checkoutComplete.isOrderSuccessful(), "Order should be successful");
        
        // Return to products page
        InventoryPage inventoryAfterCheckout = checkoutComplete.clickBackHome();
        Assert.assertTrue(inventoryAfterCheckout.isPageLoaded(), "Inventory page should be loaded after checkout");
        
        // Verify cart is empty
        Assert.assertEquals(inventoryAfterCheckout.getCartCount(), 0, "Cart should be empty after checkout");
    }
    
    @Test(description = "Verify validation on checkout information page")
    public void testCheckoutInformationValidation() {
        // Navigate to cart and checkout
        CartPage cartPage = inventoryPage.goToCart();
        CheckoutStepOnePage checkoutStepOne = cartPage.clickCheckout();
        
        // Submit without filling form
        checkoutStepOne.clickContinue();
        Assert.assertTrue(checkoutStepOne.isErrorMessageDisplayed(), "Error should be displayed when form is empty");
        Assert.assertTrue(checkoutStepOne.getErrorMessage().contains("First Name is required"), 
                "Error should indicate first name is required");
        
        // Fill only first name
        checkoutStepOne.enterFirstName("John").clickContinue();
        Assert.assertTrue(checkoutStepOne.isErrorMessageDisplayed(), "Error should be displayed");
        Assert.assertTrue(checkoutStepOne.getErrorMessage().contains("Last Name is required"), 
                "Error should indicate last name is required");
        
        // Fill first and last name
        checkoutStepOne.enterLastName("Doe").clickContinue();
        Assert.assertTrue(checkoutStepOne.isErrorMessageDisplayed(), "Error should be displayed");
        Assert.assertTrue(checkoutStepOne.getErrorMessage().contains("Postal Code is required"), 
                "Error should indicate postal code is required");
        
        // Fill all fields
        checkoutStepOne.enterPostalCode("12345");
        CheckoutStepTwoPage checkoutStepTwo = checkoutStepOne.clickContinue();
        Assert.assertTrue(checkoutStepTwo.isPageLoaded(), "Checkout step two page should be loaded after filling all fields");
    }
    
    @Test(description = "Verify cancel button functionality in checkout flow")
    public void testCancelCheckout() {
        // Navigate to cart
        CartPage cartPage = inventoryPage.goToCart();
        
        // Go to checkout step one
        CheckoutStepOnePage checkoutStepOne = cartPage.clickCheckout();
        
        // Click cancel
        cartPage = checkoutStepOne.clickCancel();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded after canceling checkout step one");
        
        // Go back to checkout and proceed to step two
        checkoutStepOne = cartPage.clickCheckout();
        checkoutStepOne.fillForm("John", "Doe", "12345");
        CheckoutStepTwoPage checkoutStepTwo = checkoutStepOne.clickContinue();
        
        // Click cancel on step two
        inventoryPage = checkoutStepTwo.clickCancel();
        Assert.assertTrue(inventoryPage.isPageLoaded(), "Inventory page should be loaded after canceling checkout step two");
        
        // Verify cart items are still there
        Assert.assertEquals(inventoryPage.getCartCount(), 2, "Cart should still have 2 items after canceling checkout");
    }
}