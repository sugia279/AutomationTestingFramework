package saucedemo_webuitesting.uiview.pages.main.inventory;

import core.base_ui.BaseWebUIMap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import saucedemo_webuitesting.uiview.controls.inventoryitem.InventoryItem;

import java.util.ArrayList;
import java.util.List;

public class InventoryPageMap extends BaseWebUIMap {
    @FindBy(css = ".product_sort_container")
    private WebElement selectSortContainer;

    @FindBy(id = "inventory_container")
    private WebElement divInventoryContainer;

    public List<InventoryItem> getInventoryItems(){
        List<WebElement> items = webAction.findElements(By.cssSelector("div.inventory_list > div.inventory_item"));
        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (WebElement item: items){
            inventoryItems.add(new InventoryItem(item, webAction));
        }
        return inventoryItems;
    }

    public InventoryItem getInventoryItem(String itemName){
        for(InventoryItem i: getInventoryItems()){
            if(i.Map().getLblName().getText().equals(itemName))
            {
                return i;
            }
        }
        return null;
    }

    public WebElement getSelectSortContainer() {
        return selectSortContainer;
    }

    public WebElement getDivInventoryContainer() {
        return divInventoryContainer;
    }
}
