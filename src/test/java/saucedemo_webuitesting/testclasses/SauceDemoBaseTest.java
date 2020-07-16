package saucedemo_webuitesting.testclasses;

import core.base_action.RestAction;
import core.base_action.WebAction;
import core.test_execution.BaseTest;

public abstract class SauceDemoBaseTest extends BaseTest {
    public SauceDemoBaseTest(){
        actions.setWebAction(new WebAction());
        actions.setRestAction(new RestAction());

        setUserKeywordPackage("saucedemo_webuitesting.keywords");
        actions.getWebAction().setAttributeAsElementName("id","class");
        testDataManager.setTestDataPath("src/test/java/saucedemo_webuitesting/suites/");

        testVars.getConfigVars().put("loginPageURL","https://www.saucedemo.com/index.html");
    }
}
