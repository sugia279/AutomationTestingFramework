package saucedemo_webuitesting.uiview.controls.inventoryitem;

import core.base_ui.BaseWebUIMap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class InventoryItemMap extends BaseWebUIMap {
    private WebElement inventoryItem;

    private WebElement lblName;
    private WebElement lblDescription;
    private WebElement lblPrice;
    private WebElement btnAddToCart;
    private WebElement btnRemove;


    public WebElement getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(WebElement item) {
        inventoryItem = item;
    }

    public WebElement getLblDescription() {
        lblDescription = inventoryItem.findElement(By.cssSelector(".inventory_item_desc"));
        return lblDescription;
    }

    public WebElement getLblPrice() {
        lblPrice = inventoryItem.findElement(By.cssSelector(".inventory_item_price"));
        return lblPrice;
    }

    public WebElement getBtnAddToCart() {
        btnAddToCart = inventoryItem.findElement(By.cssSelector(".btn_primary"));
        return btnAddToCart;
    }

    public WebElement getBtnRemove() {
        btnRemove = inventoryItem.findElement(By.cssSelector(".btn_secondary"));
        return btnRemove;
    }

    public boolean isBtnAddToCartPresent(){
        try {
            getBtnAddToCart();
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isBtnRemovePresent(){
        try {
            getBtnRemove();
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public WebElement getLblName() {
        return inventoryItem.findElement(By.cssSelector(".inventory_item_name"));
    }
}
