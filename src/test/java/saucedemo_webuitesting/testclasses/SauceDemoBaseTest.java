package saucedemo_webuitesting.testclasses;

import core.base_action.RestAction;
import core.base_action.WebAction;
import core.test_execution.BaseTest;

public abstract class SauceDemoBaseTest extends BaseTest {
    public SauceDemoBaseTest(){
        baseAction.setWebAction(new WebAction());
        baseAction.setRestAction(new RestAction());

        setHighLevelActionPackage("saucedemo_webuitesting.highlevel_action");
        baseAction.getWebAction().setAttributeAsElementName("id","class");
        testDataManager.setTestDataPath("src/test/java/saucedemo_webuitesting/suites/");

        testVars.getRuntimeVars().put("loginPageURL","https://www.saucedemo.com/index.html");
    }
}
