package saucedemo_webuitesting.uiview.pages.main.inventory;

import core.base_action.WebAction;
import org.openqa.selenium.support.ui.ExpectedConditions;
import saucedemo_webuitesting.uiview.pages.main.MainPage;

public class InventoryPage extends MainPage<InventoryPageMap> {
    public InventoryPage(WebAction action){
        super(new InventoryPageMap(), action);
    }

    public InventoryPage waitForPageLoadComplete(){
        webAction.waitUntil(ExpectedConditions.visibilityOf(Map().getSelectSortContainer()));
        //webAction.waitUntil(ExpectedConditions.visibilityOf(Map().getInventoryItems().get(0).Map().getInventoryItem()));

        return this;
    }
}
