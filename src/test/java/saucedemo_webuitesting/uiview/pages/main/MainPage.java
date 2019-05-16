package saucedemo_webuitesting.uiview.pages.main;

import core.actionbase.WebAction;
import core.uibase.BaseWebUI;
import saucedemo_webuitesting.uiview.controls.headercontainer.HeaderContainer;
import saucedemo_webuitesting.uiview.controls.mainmenu.MainMenu;

public class MainPage<M,V> extends BaseWebUI<M,V> {
    private HeaderContainer headerContainer;
    private MainMenu mainMenu;

    public MainPage(M m, V v, WebAction action)
    {
        super(m,v, action);
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
