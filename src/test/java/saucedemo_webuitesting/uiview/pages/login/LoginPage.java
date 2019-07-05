package saucedemo_webuitesting.uiview.pages.login;

import core.base_action.WebAction;
import core.base_ui.BaseWebUI;


public class LoginPage extends BaseWebUI<LoginPageMap> {

    public LoginPage(WebAction action) {
        super(new LoginPageMap(), action);
    }

    public LoginPage navigate(String url) {
        webAction.getToUrl(url, true);
        return this;
    }

    public LoginPage login(String user, String password) {
        webAction.type(Map().getTxtUser(), user,"User Name");
        webAction.type(Map().getTxtPassword(), password, "Password");
        webAction.click(Map().getBtnLogIn(), "Login");
        return this;
    }
}
