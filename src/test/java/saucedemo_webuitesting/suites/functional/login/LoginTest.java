package saucedemo_webuitesting.suites.functional.login;

import org.testng.annotations.DataProvider;
import saucedemo_webuitesting.suites.SauceDemoBaseTest;

public class LoginTest extends SauceDemoBaseTest {
    @DataProvider
    @Override
    protected Object[] testDataSet() {
        return fetchDataToDataSet("functional/login/LoginTest.json");
    }
}
