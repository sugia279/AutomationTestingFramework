package saucedemo_webuitesting.testclasses;


import core.base_action.BrowserType;
import core.test_execution.BaseTest;

public abstract class SauceDemoBaseTest extends BaseTest {
    public SauceDemoBaseTest(){
        runtimeVars.put("loginPageURL","https://www.saucedemo.com/index.html");
        setHighLevelActionPackage("saucedemo_webuitesting.highlevel_action");
        baseAction.getWebAction().setAttributeAsElementName("id","class");
        testDataManager.setTestDataPath("src/test/java/saucedemo_webuitesting/suites/");
    }
}
