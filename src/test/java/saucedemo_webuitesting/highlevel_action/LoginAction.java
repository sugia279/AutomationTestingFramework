package saucedemo_webuitesting.highlevel_action;

import core.base_action.BaseAction;
import core.base_action.WebAction;
import core.extent_report.TestReportManager;
import core.testdata_manager.TestStep;
import saucedemo_webuitesting.uiview.pages.login.LoginPage;
import saucedemo_webuitesting.uiview.pages.main.inventory.InventoryPage;

public class LoginAction extends BaseAction {
    public LoginAction(WebAction action){
        super(action);
    }

    public void login(TestStep step){
        String user = (String)step.getTestParams().get("user");
        String password = (String)step.getTestParams().get("password");
        String errorMessage = (String)step.getTestParams().get("errorMessage");

        LoginPage lg = new LoginPage(webAction);
        lg.navigate("https://www.saucedemo.com/index.html")
                .login(user,password);

        if(errorMessage == null){
            TestReportManager.getInstance().setStepInfo("Navigate to Inventory page");
            webAction.getSoftAssert().assertEquals(new InventoryPage(webAction).Map().getDivInventoryContainer().isDisplayed(), true, "Ensure the Inventory Container is displayed.");
        }
        else {
            webAction.getSoftAssert().assertEquals(lg.Map().getDivLoginError().getText(),errorMessage,"Ensure the error login show message [" + errorMessage + "]");
        }
    }

}
