package saucedemo_webuitesting.uiview.pages.main;

import core.base_action.WebAction;
import core.base_ui.BaseWebUI;
import saucedemo_webuitesting.uiview.controls.headercontainer.HeaderContainer;
import saucedemo_webuitesting.uiview.controls.mainmenu.MainMenu;

public class MainPage<M> extends BaseWebUI<M> {
    private HeaderContainer headerContainer;
    private MainMenu mainMenu;

    public MainPage(M m, WebAction action)
    {
        super(m, action);
        headerContainer = new HeaderContainer(action);
        mainMenu = new MainMenu(action);
    }

    public HeaderContainer getHeaderContainer() {
        return headerContainer;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public MainMenu openMainMenu(){
        webAction.click(headerContainer.Map().getBtnMenu(), "Menu Icon");
        return mainMenu;
    }
}
