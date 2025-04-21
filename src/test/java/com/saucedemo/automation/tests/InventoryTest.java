package com.saucedemo.automation.tests;

import com.saucedemo.automation.core.BaseTest;
import com.saucedemo.automation.pages.InventoryPage;
import com.saucedemo.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Tests for Inventory functionality
 */
public class InventoryTest extends BaseTest {
    
    private InventoryPage inventoryPage;
    
    @BeforeMethod
    public void setupTest() {
        // Navigate to base URL
        navigateToBaseUrl();
        
        // Login with standard user
        LoginPage loginPage = new LoginPage(driver);
        String username = config.getProperty("standard_user");
        String password = config.getProperty("password");
        
        inventoryPage = loginPage.login(username, password);
        Assert.assertTrue(inventoryPage.isPageLoaded(), "Inventory page should be loaded after login");
    }
    
    @Test(description = "Verify inventory items count")
    public void testInventoryItemCount() {
        // Verify number of inventory items
        int itemCount = inventoryPage.getInventoryItemCount();
        Assert.assertEquals(itemCount, 6, "Inventory should have 6 items");
    }
    
    @Test(description = "Verify adding items to cart")
    public void testAddToCart() {
        // Add an item to cart
        String itemName = "Sauce Labs Backpack";
        inventoryPage.addItemToCart(itemName);
        
        // Verify cart badge count
        int cartCount = inventoryPage.getCartCount();
        Assert.assertEquals(cartCount, 1, "Cart count should be 1 after adding an item");
        
        // Add another item
        String secondItem = "Sauce Labs Bike Light";
        inventoryPage.addItemToCart(secondItem);
        
        // Verify cart badge count updated
        cartCount = inventoryPage.getCartCount();
        Assert.assertEquals(cartCount, 2, "Cart count should be 2 after adding second item");
    }
    
    @Test(description = "Verify sorting functionality")
    public void testSorting() {
        // Get default item names (should be alphabetical A to Z)
        List<String> defaultNames = inventoryPage.getItemNames();
        
        // Sort by Z to A
        inventoryPage.sortItemsBy("za");
        List<String> reversedNames = inventoryPage.getItemNames();
        
        // Verify order is reversed
        Assert.assertEquals(reversedNames.get(0), defaultNames.get(defaultNames.size() - 1), 
                "First item after Z-A sort should be last item from default sort");
        
        // Sort by price low to high
        inventoryPage.sortItemsBy("lohi");
        List<String> priceLowHighNames = inventoryPage.getItemNames();
        
        // Sort by price high to low
        inventoryPage.sortItemsBy("hilo");
        List<String> priceHighLowNames = inventoryPage.getItemNames();
        
        // Verify price high to low is reverse of price low to high
        Assert.assertEquals(priceHighLowNames.get(0), priceLowHighNames.get(priceLowHighNames.size() - 1), 
                "First item after high-low sort should be last item from low-high sort");
    }
    
    @Test(description = "Verify navigation to cart")
    public void testNavigationToCart() {
        // Add an item to cart
        String itemName = "Sauce Labs Backpack";
        inventoryPage.addItemToCart(itemName);
        
        // Navigate to cart
        var cartPage = inventoryPage.goToCart();
        
        // Verify cart page loaded
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");
        
        // Verify item in cart
        Assert.assertTrue(cartPage.isItemInCart(itemName), "Added item should be in cart");
    }
}