package com.saucedemo.automation.pages;

import com.saucedemo.automation.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for Checkout Step Two page
 */
public class CheckoutStepTwoPage extends BasePage {
    
    @FindBy(className = "subheader")
    private WebElement pageTitle;
    
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;
    
    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;
    
    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;
    
    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;
    
    @FindBy(xpath = "//button[contains(text(), 'FINISH')]")
    private WebElement finishButton;
    
    @FindBy(xpath = "//button[contains(text(), 'CANCEL')]")
    private WebElement cancelButton;
    
    private final String itemNameXpath = ".//div[@class='inventory_item_name']";
    private final String itemPriceXpath = ".//div[@class='inventory_item_price']";
    
    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if page is loaded
     *
     * @return true if page is loaded, false otherwise
     */
    public boolean isPageLoaded() {
        return isElementDisplayed(pageTitle) && isElementDisplayed(subtotalLabel) 
                && isElementDisplayed(totalLabel);
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
     * Get item names in checkout
     *
     * @return List of item names
     */
    public List<String> getItemNames() {
        return cartItems.stream()
                .map(item -> item.findElement(By.xpath(itemNameXpath)).getText())
                .collect(Collectors.toList());
    }
    
    /**
     * Get subtotal amount
     *
     * @return Subtotal amount as text
     */
    public String getSubtotal() {
        return getText(subtotalLabel).replaceAll("Item total: ", "");
    }
    
    /**
     * Get tax amount
     *
     * @return Tax amount as text
     */
    public String getTax() {
        return getText(taxLabel).replaceAll("Tax: ", "");
    }
    
    /**
     * Get total amount
     *
     * @return Total amount as text
     */
    public String getTotal() {
        return getText(totalLabel).replaceAll("Total: ", "");
    }
    
    /**
     * Click finish button
     *
     * @return CheckoutCompletePage
     */
    public CheckoutCompletePage clickFinish() {
        logger.info("Clicking finish");
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }
    
    /**
     * Click cancel button
     *
     * @return InventoryPage
     */
    public InventoryPage clickCancel() {
        logger.info("Clicking cancel");
        click(cancelButton);
        return new InventoryPage(driver);
    }
}