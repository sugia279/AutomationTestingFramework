package saucedemo_webuitesting.testclasses.inventory;

import org.testng.annotations.DataProvider;
import saucedemo_webuitesting.testclasses.SauceDemoBaseTest;

public class InventoryTest extends SauceDemoBaseTest {
    protected boolean isLogin = false;

    @DataProvider
    @Override
    protected Object[] testDataSet() {
        return fetchDataToDataSet("functional/inventory/AddToCartTest.json");
    }

}
