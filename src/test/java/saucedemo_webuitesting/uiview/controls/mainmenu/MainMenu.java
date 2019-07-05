package saucedemo_webuitesting.uiview.controls.mainmenu;

import core.base_action.WebAction;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import saucedemo_webuitesting.uiview.controls.UIControl;

public class MainMenu extends UIControl<MainMenuMap> {
    public MainMenu(WebAction action){
        super(new MainMenuMap(), action);
    }

    public MainMenu waitForMenuDisplay(){
        webAction.waitUntil(ExpectedConditions.not(
                                    ExpectedConditions.attributeContains(
                                                By.cssSelector(".bm-menu-wrap"),"style","transform: translate3d(-100%, 0px, 0px")));
        return this;
    }

    public MainMenu selectAllItem(){
        webAction.click(Map().getLinkAllItems(),"All Items");
        return this;
    }

    public MainMenu selectResetAppState(){
        webAction.click(Map().getLinkResetAppState(),"Reset App State");
        return this;
    }

    public MainMenu selectLogout(){
        webAction.click(Map().getLinkLogout(),"Log Out");
        return this;
    }
}
