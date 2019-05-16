package saucedemo_webuitesting.uiview.pages.main.inventory;

import core.uibase.BaseWebUIValidator;

public class InventoryPageValidator extends BaseWebUIValidator<InventoryPageMap> {

    public void validateInventoryPageIsDisplayed(){
        softAssert.assertEquals(Map().getDivInventoryContainer().isDisplayed(), true, "Ensure the Inventory Container is displayed.");
    }
}
