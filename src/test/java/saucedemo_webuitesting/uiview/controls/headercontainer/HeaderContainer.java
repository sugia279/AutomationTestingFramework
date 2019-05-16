package saucedemo_webuitesting.uiview.controls.headercontainer;

import core.actionbase.WebAction;
import org.openqa.selenium.support.ui.ExpectedConditions;
import saucedemo_webuitesting.uiview.controls.UIControl;
import saucedemo_webuitesting.uiview.controls.mainmenu.MainMenu;

public class HeaderContainer extends UIControl<HeaderContainerMap, HeaderContainerValidator> {
    public HeaderContainer(WebAction action){
        super(new HeaderContainerMap(), new HeaderContainerValidator(), action);
    }



    public HeaderContainer openMainMenu(){
        webAction.click(Map().getBtnMenu(),"Menu Icon", false);
        return this;
    }
}
