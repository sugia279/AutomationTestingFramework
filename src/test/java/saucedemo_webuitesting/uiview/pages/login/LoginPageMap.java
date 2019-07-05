package saucedemo_webuitesting.uiview.pages.login;

import core.base_ui.BaseWebUIMap;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPageMap extends BaseWebUIMap {
    @FindBy(id = "user-name")
    private WebElement txtUser;

    @FindBy(id = "password")
    private WebElement txtPassword;

    @FindBy(css = ".btn_action")
    private WebElement btnLogIn;

    @FindBy(css = "h3[data-test='error']")
    private WebElement divLoginError;

    public WebElement getTxtUser()
    {
        return txtUser;
    }

    public WebElement getTxtPassword()
    {
        return txtPassword;
    }

    public WebElement getBtnLogIn()
    {
        return btnLogIn;
    }


    public WebElement getDivLoginError() {
        return divLoginError;
    }
}
