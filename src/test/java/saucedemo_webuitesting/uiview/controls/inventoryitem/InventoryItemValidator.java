package saucedemo_webuitesting.uiview.controls.inventoryitem;

import core.uibase.BaseWebUIValidator;

public class InventoryItemValidator extends BaseWebUIValidator<InventoryItemMap> {
    public void validateAddedItemToCart(){
        softAssert.assertEquals(Map().isBtnRemovePresent(), true, "Ensure Remove button is presented.");
        softAssert.assertEquals(Map().isBtnAddToCartPresent(), false, "Ensure Add To Cart button is not presented.");
        softAssert.assertAll();
    }
}
