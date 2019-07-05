package saucedemo_webuitesting.uiview.controls.inventoryitem;

import core.base_action.WebAction;
import org.openqa.selenium.WebElement;
import saucedemo_webuitesting.uiview.controls.UIControl;

public class InventoryItem extends UIControl<InventoryItemMap> {
    public InventoryItem(WebElement item, WebAction action){
        super(new InventoryItemMap(), action);
        Map().setInventoryItem(item);
    }

    public InventoryItem clickAddToCart(){
        webAction.click(Map().getBtnAddToCart(), "Add To Cart");
        return this;
    }
}
