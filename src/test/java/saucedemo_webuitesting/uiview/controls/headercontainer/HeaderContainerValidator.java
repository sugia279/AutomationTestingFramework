package saucedemo_webuitesting.uiview.controls.headercontainer;

import core.uibase.BaseWebUIValidator;

public class HeaderContainerValidator extends BaseWebUIValidator<HeaderContainerMap> {
    public void validateShoppingCartCounter(int expectedNumber){
        softAssert.assertEquals(Map().getLinkShoppingCart().getText(),Integer.toString(expectedNumber), "Ensure number of added items is [" + expectedNumber + "]");
        softAssert.assertAll();
    }
}
