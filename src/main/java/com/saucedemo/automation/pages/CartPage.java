package com.saucedemo.automation.pages;

import com.saucedemo.automation.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for Cart page
 */
public class CartPage extends BasePage {
    
    @FindBy(className = "subheader")
    private WebElement pageTitle;
    
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;
    
    @FindBy(xpath = "//button[contains(text(), 'CHECKOUT')]")
    private WebElement checkoutButton;
    
    @FindBy(xpath = "//button[contains(text(), 'Continue Shopping')]")
    private WebElement continueShoppingButton;
    
    @FindBy(className = "cart_quantity_label")
    private WebElement quantityLabel;
    
    @FindBy(className = "cart_desc_label")
    private WebElement descriptionLabel;
    
    private final String itemNameXpath = ".//div[@class='inventory_item_name']";
    private final String itemPriceXpath = ".//div[@class='inventory_item_price']";
    private final String removeButtonXpath = ".//button[contains(text(), 'REMOVE')]";
    
    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public CartPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if page is loaded
     *
     * @return true if page is loaded, false otherwise
     */
    public boolean isPageLoaded() {
        return isElementDisplayed(pageTitle) && isElementDisplayed(quantityLabel) 
                && isElementDisplayed(descriptionLabel);
    }
    
    /**
     * Get number of cart items
     *
     * @return Number of cart items
     */
    public int getCartItemCount() {
        return cartItems.size();
    }
    
    /**
     * Get item names in cart
     *
     * @return List of item names
     */
    public List<String> getItemNames() {
        return cartItems.stream()
                .map(item -> item.findElement(By.xpath(itemNameXpath)).getText())
                .collect(Collectors.toList());
    }
    
    /**
     * Get item prices in cart
     *
     * @return List of item prices
     */
    public List<String> getItemPrices() {
        return cartItems.stream()
                .map(item -> item.findElement(By.xpath(itemPriceXpath)).getText())
                .collect(Collectors.toList());
    }
    
    /**
     * Remove item from cart by name
     *
     * @param itemName Name of the item to remove
     * @return CartPage for method chaining
     */
    public CartPage removeItem(String itemName) {
        logger.info("Removing item from cart: {}", itemName);
        for (WebElement item : cartItems) {
            WebElement nameElement = item.findElement(By.xpath(itemNameXpath));
            if (nameElement.getText().equals(itemName)) {
                WebElement removeButton = item.findElement(By.xpath(removeButtonXpath));
                click(removeButton);
                break;
            }
        }
        return this;
    }
    
    /**
     * Click checkout button
     *
     * @return CheckoutStepOnePage
     */
    public CheckoutStepOnePage clickCheckout() {
        logger.info("Clicking checkout");
        click(checkoutButton);
        return new CheckoutStepOnePage(driver);
    }
    
    /**
     * Click continue shopping button
     *
     * @return InventoryPage
     */
    public InventoryPage continueShopping() {
        logger.info("Continuing shopping");
        click(continueShoppingButton);
        return new InventoryPage(driver);
    }
    
    /**
     * Check if item exists in cart
     *
     * @param itemName Item name to check
     * @return true if item exists, false otherwise
     */
    public boolean isItemInCart(String itemName) {
        return getItemNames().contains(itemName);
    }
}