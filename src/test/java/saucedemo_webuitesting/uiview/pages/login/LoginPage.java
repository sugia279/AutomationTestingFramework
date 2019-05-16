package saucedemo_webuitesting.uiview.pages.login;

import core.actionbase.WebAction;
import core.uibase.BaseWebUI;


public class LoginPage extends BaseWebUI<LoginPageMap,LoginPageValidator> {

    public LoginPage(WebAction action) {
        super(new LoginPageMap(), new LoginPageValidator(), action);
    }

    public LoginPage navigate(String url) {
        webAction.getToUrl(url, true);
        return this;
    }

    public LoginPage login(String user, String password) {
        webAction.type(Map().getTxtUser(), "User Name", user);
        webAction.type(Map().getTxtPassword(), "Password", password);
        webAction.click(Map().getBtnLogIn(), "Login");
        return this;
    }
}
