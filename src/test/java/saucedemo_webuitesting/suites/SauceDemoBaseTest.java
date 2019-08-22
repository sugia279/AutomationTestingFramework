package saucedemo_webuitesting.suites;


import core.test_execution.BaseTest;

public abstract class SauceDemoBaseTest extends BaseTest {
    public SauceDemoBaseTest(){
        runtimeVars.put("loginPageURL","https://www.saucedemo.com/index.html");
        highLevelActionFolder= "saucedemo_webuitesting.highlevel_action.";
        webAction.setAttributeAsElementName("aria-label","ng-click","ng-model");
        testDataManager.setTestDataFolder("src/test/java/saucedemo_webuitesting/suites/");
    }
}
