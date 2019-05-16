package saucedemo_webuitesting.suites.ui.functional.logon;

import core.extentreport.TestReportManager;
import core.testexecution.BaseTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import saucedemo_webuitesting.uiview.pages.login.LoginPage;
import saucedemo_webuitesting.uiview.pages.main.inventory.InventoryPage;

public class LogOnTest extends BaseTest {

    @DataProvider
    protected Object[] loginTestDataSet(){
        return fetchDataToDataSet("saucedemo_webuitesting/suites/ui/functional/logon/LogOnTest.json");
    }

    @Test(dataProvider = "loginTestDataSet")
    public void LoginTestExecution(Object[] params) {
        String user = (String)curTestCase.getParamValueFromTestStep(0,"user");
        String password = (String)curTestCase.getParamValueFromTestStep(0,"password");
        String errorMessage = (String)curTestCase.getParamValueFromTestStep(0,"errorMessage");

        TestReportManager.getInstance().setStepInfo("Login to SauceDemo Page with user name ='" + user + "', password ='" + password + "'");
        LoginPage lg = new LoginPage(webAction);
        lg.navigate("https://www.saucedemo.com/index.html")
                .login(user,password);

        if(errorMessage == null){
            TestReportManager.getInstance().setStepInfo("Navigate to Inventory page");
            new InventoryPage(webAction).Validator().validateInventoryPageIsDisplayed();

        }
        else {
            TestReportManager.getInstance().setStepInfo("The error login show message '" + errorMessage + "'");
            lg.Validator().validateLoginError(errorMessage);
        }
    }
}
