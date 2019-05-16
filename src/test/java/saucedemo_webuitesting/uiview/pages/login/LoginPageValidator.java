package saucedemo_webuitesting.uiview.pages.login;

import core.uibase.BaseWebUIValidator;

public class LoginPageValidator extends BaseWebUIValidator<LoginPageMap> {
    //validate notify message when logon
    public void validateLoginError(String expectedMessage)
    {
        softAssert.assertEquals(Map().getDivLoginError().getText(),expectedMessage,"Ensure the error login show message [" + expectedMessage + "]");
        softAssert.assertAll();
    }
}
