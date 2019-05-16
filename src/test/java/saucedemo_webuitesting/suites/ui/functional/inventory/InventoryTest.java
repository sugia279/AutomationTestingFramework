package saucedemo_webuitesting.suites.ui.functional.inventory;

import core.extentreport.TestReportManager;
import core.testexecution.BaseTest;
import org.json.simple.JSONArray;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import saucedemo_webuitesting.uiview.controls.headercontainer.HeaderContainer;
import saucedemo_webuitesting.uiview.controls.inventoryitem.InventoryItem;
import saucedemo_webuitesting.uiview.controls.mainmenu.MainMenu;
import saucedemo_webuitesting.uiview.pages.login.LoginPage;
import saucedemo_webuitesting.uiview.pages.main.inventory.InventoryPage;

import java.awt.*;

public class InventoryTest extends BaseTest {
    protected boolean isLogin = false;
    @BeforeMethod
    public void beforeMethod(Object[] params, ITestContext testContext){
        super.beforeMethod(params,testContext);

        //1. Login
        String user = (String) curTestCase.getParamValueFromTestStep(0, "user");
        String password = (String) curTestCase.getParamValueFromTestStep(0, "password");
        LoginPage lg =  new LoginPage(webAction);
        TestReportManager.getInstance().setStepInfo("Login to SauceDemo main page with user name ='" + user + "', password ='" + password + "'");
        lg.navigate("https://www.saucedemo.com/index.html")
                    .login(user, password);
    }

    @DataProvider
    protected Object[] addToCartTestDataSet(){
        return fetchDataToDataSet("saucedemo_webuitesting/suites/ui/functional/inventory/AddToCartTest.json");
    }

    @Test(dataProvider = "addToCartTestDataSet")
    public void AddToCartTest(Object[] params){
        Object[] item_names = curTestCase.getParamArrayValueFromTestStep(1, "Item Names");
        int counter = Math.toIntExact((long)curTestCase.getParamValueFromTestStep(1, "Counter"));

        //2. Navigate to Inventory page
        InventoryPage invPage = new InventoryPage(webAction);
        invPage.waitForPageLoadComplete();

        TestReportManager.getInstance().setStepInfo("Add item [" + item_names + "] to cart, and then validating the Remove button is presented.");
        for(Object item: item_names){
            invPage.Map().getInventoryItem((String)item)
                            .clickAddToCart()
                    .Validator().validateAddedItemToCart();
        }

        TestReportManager.getInstance().setStepInfo("Validate the shopping cart counter");
        invPage.getHeaderContainer().Validator().validateShoppingCartCounter(counter);
    }

}
