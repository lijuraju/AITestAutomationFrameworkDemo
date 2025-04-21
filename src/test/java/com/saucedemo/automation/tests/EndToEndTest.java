package com.saucedemo.automation.tests;

import com.saucedemo.automation.core.BaseTest;
import com.saucedemo.automation.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * End-to-End test for complete user flow
 */
public class EndToEndTest extends BaseTest {
    
    @Test(description = "Complete end-to-end user flow test")
    public void testEndToEndFlow() {
        // Navigate to base URL
        navigateToBaseUrl();
        
        // 1. Login with standard user
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        String username = config.getProperty("standard_user");
        String password = config.getProperty("password");
        
        InventoryPage inventoryPage = loginPage.login(username, password);
        Assert.assertTrue(inventoryPage.isPageLoaded(), "Inventory page should be loaded after login");
        
        // 2. Sort products by price (high to low)
        inventoryPage.sortItemsBy("hilo");
        
        // 3. Add the most expensive product to cart
        String expensiveItem = inventoryPage.getItemNames().get(0);
        inventoryPage.addItemToCart(expensiveItem);
        Assert.assertEquals(inventoryPage.getCartCount(), 1, "Cart should have 1 item");
        
        // 4. Add the second most expensive product to cart
        String secondExpensiveItem = inventoryPage.getItemNames().get(1);
        inventoryPage.addItemToCart(secondExpensiveItem);
        Assert.assertEquals(inventoryPage.getCartCount(), 2, "Cart should have 2 items");
        
        // 5. Go to cart
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");
        
        // 6. Remove the second item from cart
        cartPage.removeItem(secondExpensiveItem);
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item after removing one");
        
        // 7. Proceed to checkout
        CheckoutStepOnePage checkoutStepOne = cartPage.clickCheckout();
        Assert.assertTrue(checkoutStepOne.isPageLoaded(), "Checkout step one page should be loaded");
        
        // 8. Enter customer information
        CheckoutStepTwoPage checkoutStepTwo = checkoutStepOne
                .enterFirstName("John")
                .enterLastName("Doe")
                .enterPostalCode("12345")
                .clickContinue();
        
        Assert.assertTrue(checkoutStepTwo.isPageLoaded(), "Checkout step two page should be loaded");
        
        // 9. Verify item in checkout
        Assert.assertTrue(checkoutStepTwo.getItemNames().contains(expensiveItem), 
                "Checkout should contain the added item");
        
        // 10. Complete purchase
        CheckoutCompletePage checkoutComplete = checkoutStepTwo.clickFinish();
        Assert.assertTrue(checkoutComplete.isPageLoaded(), "Checkout complete page should be loaded");
        
        // 11. Verify order confirmation
        Assert.assertTrue(checkoutComplete.isOrderSuccessful(), "Order should be successful");
        Assert.assertTrue(checkoutComplete.getCompleteText().contains("Your order has been dispatched"), 
                "Confirmation should indicate order was dispatched");
        
        // 12. Return to products
        inventoryPage = checkoutComplete.clickBackHome();
        Assert.assertTrue(inventoryPage.isPageLoaded(), "Inventory page should be loaded after checkout");
        
        // 13. Log out
        loginPage = inventoryPage.logout();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded after logout");
    }
}