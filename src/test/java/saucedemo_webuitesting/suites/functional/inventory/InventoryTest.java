package saucedemo_webuitesting.suites.functional.inventory;

import core.extent_report.TestReportManager;
import core.test_execution.BaseTest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import saucedemo_webuitesting.suites.SauceDemoBaseTest;
import saucedemo_webuitesting.uiview.pages.login.LoginPage;
import saucedemo_webuitesting.uiview.pages.main.inventory.InventoryPage;

public class InventoryTest extends SauceDemoBaseTest {
    protected boolean isLogin = false;

    @DataProvider
    @Override
    protected Object[] testDataSet() {
        return fetchDataToDataSet("functional/inventory/AddToCartTest.json");
    }

}
