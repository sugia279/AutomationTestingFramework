package saucedemo_webuitesting.testclasses.login;

import org.testng.annotations.DataProvider;
import saucedemo_webuitesting.testclasses.SauceDemoBaseTest;

public class LoginTest extends SauceDemoBaseTest {
    @DataProvider
    @Override
    protected Object[] testDataSet() {
        return fetchDataToDataSet("functional/login/LoginTest.json");
    }
}
