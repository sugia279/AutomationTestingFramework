package saucedemo_webuitesting.uiview.controls.headercontainer;

import core.base_action.WebAction;
import saucedemo_webuitesting.uiview.controls.UIControl;

public class HeaderContainer extends UIControl<HeaderContainerMap> {
    public HeaderContainer(WebAction action){
        super(new HeaderContainerMap(), action);
    }



    public HeaderContainer openMainMenu(){
        webAction.click(Map().getBtnMenu(),"Menu Icon");
        return this;
    }
}
