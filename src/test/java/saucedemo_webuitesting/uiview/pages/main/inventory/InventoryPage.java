package saucedemo_webuitesting.uiview.pages.main.inventory;

import core.actionbase.WebAction;
import org.openqa.selenium.support.ui.ExpectedConditions;
import saucedemo_webuitesting.uiview.pages.main.MainPage;

public class InventoryPage extends MainPage<InventoryPageMap,InventoryPageValidator> {
    public InventoryPage(WebAction action){
        super(new InventoryPageMap(), new InventoryPageValidator(), action);
    }

    public InventoryPage waitForPageLoadComplete(){
        webAction.waitUntil(ExpectedConditions.visibilityOf(Map().getSelectSortContainer()));
        //webAction.waitUntil(ExpectedConditions.visibilityOf(Map().getInventoryItems().get(0).Map().getInventoryItem()));

        return this;
    }
}
