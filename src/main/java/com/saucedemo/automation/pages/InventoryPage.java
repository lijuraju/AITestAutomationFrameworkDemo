package com.saucedemo.automation.pages;

import com.saucedemo.automation.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for Inventory page
 */
public class InventoryPage extends BasePage {
    
    @FindBy(className = "product_label")
    private WebElement productLabel;
    
    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;
    
    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;
    
    @FindBy(xpath = "//button[contains(text(), 'ADD TO CART')]")
    private List<WebElement> addToCartButtons;
    
    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;
    
    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;
    
    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;
    
    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;
    
    private final String itemNameXpath = ".//div[@class='inventory_item_name']";
    private final String itemPriceXpath = ".//div[@class='inventory_item_price']";
    private final String addToCartButtonXpath = ".//button[contains(text(), 'ADD TO CART')]";
    
    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public InventoryPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if page is loaded
     *
     * @return true if page is loaded, false otherwise
     */
    public boolean isPageLoaded() {
        return isElementDisplayed(productLabel) && !inventoryItems.isEmpty();
    }
    
    /**
     * Get number of inventory items
     *
     * @return Number of inventory items
     */
    public int getInventoryItemCount() {
        return inventoryItems.size();
    }
    
    /**
     * Get item names
     *
     * @return List of item names
     */
    public List<String> getItemNames() {
        return inventoryItems.stream()
                .map(item -> item.findElement(By.xpath(itemNameXpath)).getText())
                .collect(Collectors.toList());
    }
    
    /**
     * Get item prices
     *
     * @return List of item prices
     */
    public List<String> getItemPrices() {
        return inventoryItems.stream()
                .map(item -> item.findElement(By.xpath(itemPriceXpath)).getText())
                .collect(Collectors.toList());
    }
    
    /**
     * Add item to cart by name
     *
     * @param itemName Name of the item to add
     * @return InventoryPage for method chaining
     */
    public InventoryPage addItemToCart(String itemName) {
        logger.info("Adding item to cart: {}", itemName);
        for (WebElement item : inventoryItems) {
            WebElement nameElement = item.findElement(By.xpath(itemNameXpath));
            if (nameElement.getText().equals(itemName)) {
                WebElement addButton = item.findElement(By.xpath(addToCartButtonXpath));
                scrollIntoView(addButton);
                click(addButton);
                break;
            }
        }
        return this;
    }
    
    /**
     * Sort items by option
     *
     * @param sortOption Sort option value
     * @return InventoryPage for method chaining
     */
    public InventoryPage sortItemsBy(String sortOption) {
        logger.info("Sorting items by: {}", sortOption);
        Select select = new Select(sortDropdown);
        select.selectByValue(sortOption);
        return this;
    }
    
    /**
     * Get cart badge count
     *
     * @return Cart badge count or 0 if not displayed
     */
    public int getCartCount() {
        try {
            if (isElementDisplayed(cartBadge)) {
                return Integer.parseInt(cartBadge.getText());
            }
        } catch (Exception e) {
            logger.warn("Error getting cart count", e);
        }
        return 0;
    }
    
    /**
     * Navigate to cart page
     *
     * @return CartPage
     */
    public CartPage goToCart() {
        logger.info("Navigating to cart");
        click(cartLink);
        return new CartPage(driver);
    }
    
    /**
     * Open menu
     *
     * @return InventoryPage for method chaining
     */
    public InventoryPage openMenu() {
        logger.info("Opening menu");
        click(menuButton);
        return this;
    }
    
    /**
     * Logout from application
     *
     * @return LoginPage
     */
    public LoginPage logout() {
        logger.info("Logging out");
        openMenu();
        waitForElementVisible(logoutLink);
        click(logoutLink);
        return new LoginPage(driver);
    }
}