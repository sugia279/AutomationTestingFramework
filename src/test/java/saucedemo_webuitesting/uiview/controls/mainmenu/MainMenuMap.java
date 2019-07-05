package saucedemo_webuitesting.uiview.controls.mainmenu;

import core.base_ui.BaseWebUIMap;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainMenuMap extends BaseWebUIMap {

    @FindBy(css= "div.bm-cross-button > button")
    private WebElement btnCloseMenu;

    @FindBy(id= "inventory_sidebar_link")
    private WebElement linkAllItems;

    @FindBy(id= "about_sidebar_link")
    private WebElement linkAbout;

    @FindBy(css= "#logout_sidebar_link")
    private WebElement linkLogout;

    @FindBy(id= "reset_sidebar_link")
    private WebElement linkResetAppState;

    public WebElement getBtnCloseMenu() {
        return btnCloseMenu;
    }

    public WebElement getLinkAllItems() {
        return linkAllItems;
    }

    public WebElement getLinkAbout() {
        return linkAbout;
    }

    public WebElement getLinkLogout() {
        return linkLogout;
    }

    public WebElement getLinkResetAppState() {
        return linkResetAppState;
    }
}
