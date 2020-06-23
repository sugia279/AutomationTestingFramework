package saucedemo_webuitesting.keywords;

import core.base_action.Action;
import core.extent_report.TestReportManager;
import saucedemo_webuitesting.uiview.controls.inventoryitem.InventoryItem;
import saucedemo_webuitesting.uiview.pages.main.inventory.InventoryPage;

import java.util.ArrayList;

public class InventoryAction extends Action {
    public InventoryAction(Action action){
        super(action);
    }

    public void addToCartTest(ArrayList<String> item_names, int counter){

        //2. Navigate to Inventory page
        InventoryPage invPage = new InventoryPage(getWebAction());
        invPage.waitForPageLoadComplete();

        for(String item: item_names){
            InventoryItem inItem =invPage.Map().getInventoryItem(item)
                    .clickAddToCart();
            TestReportManager.getInstance().setStepInfo("validate the Remove button is presented.");

            getSoftAssert().assertEquals(inItem.Map().isBtnRemovePresent(), true, "Ensure Remove button is presented.");
            getSoftAssert().assertEquals(inItem.Map().isBtnAddToCartPresent(), false, "Ensure Add To Cart button is not presented.");

        }

        TestReportManager.getInstance().setStepInfo("Validate the shopping cart counter");
        getSoftAssert().assertEquals(invPage.getHeaderContainer().Map().getLinkShoppingCart().getText(),Integer.toString(counter), "Ensure number of added items is [" + getSoftAssert() + "]");
    }
}
