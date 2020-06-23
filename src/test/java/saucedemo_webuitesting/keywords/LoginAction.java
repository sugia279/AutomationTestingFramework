package saucedemo_webuitesting.keywords;

import core.base_action.Action;
import core.extent_report.TestReportManager;
import saucedemo_webuitesting.uiview.pages.login.LoginPage;
import saucedemo_webuitesting.uiview.pages.main.inventory.InventoryPage;

public class LoginAction extends Action {
    public LoginAction(Action action){
        super(action);
    }

    public void login(String user, String password, String errorMessage){
        LoginPage lg = new LoginPage(getWebAction());
        lg.navigate("https://www.saucedemo.com/index.html")
                .login(user,password);

        if(errorMessage == null){
            TestReportManager.getInstance().setStepInfo("Navigate to Inventory page");
            getSoftAssert().assertEquals(new InventoryPage(getWebAction()).Map().getDivInventoryContainer().isDisplayed(), true, "Ensure the Inventory Container is displayed.");
        }
        else {
            getSoftAssert().assertEquals(lg.Map().getDivLoginError().getText(),errorMessage,"Ensure the error login show message [" + errorMessage + "]");
        }
    }

}
