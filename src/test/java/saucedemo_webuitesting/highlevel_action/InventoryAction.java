package saucedemo_webuitesting.highlevel_action;

import core.base_action.BaseAction;
import core.base_action.WebAction;
import core.extent_report.TestReportManager;
import core.testdata_manager.TestStep;
import org.json.simple.JSONArray;
import saucedemo_webuitesting.uiview.controls.inventoryitem.InventoryItem;
import saucedemo_webuitesting.uiview.pages.main.inventory.InventoryPage;

public class InventoryAction extends BaseAction {
    public InventoryAction(WebAction action){
        super(action);
    }

    public void addToCartTest(TestStep step){
        Object[] item_names = ((JSONArray)step.getTestParams().get("Item Names")).toArray();
        Long counter = (Long)step.getTestParams().get("Counter");

        //2. Navigate to Inventory page
        InventoryPage invPage = new InventoryPage(webAction);
        invPage.waitForPageLoadComplete();

        for(Object item: item_names){
            InventoryItem inItem =invPage.Map().getInventoryItem((String)item)
                    .clickAddToCart();
            TestReportManager.getInstance().setStepInfo("validate the Remove button is presented.");

            webAction.getSoftAssert().assertEquals(inItem.Map().isBtnRemovePresent(), true, "Ensure Remove button is presented.");
            webAction.getSoftAssert().assertEquals(inItem.Map().isBtnAddToCartPresent(), false, "Ensure Add To Cart button is not presented.");

        }

        TestReportManager.getInstance().setStepInfo("Validate the shopping cart counter");
        webAction.getSoftAssert().assertEquals(invPage.getHeaderContainer().Map().getLinkShoppingCart().getText(),Integer.toString(counter.intValue()), "Ensure number of added items is [" + webAction.getSoftAssert() + "]");
    }
}
