package saucedemo_webuitesting.uiview.controls.headercontainer;

import core.base_ui.BaseWebUIMap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HeaderContainerMap extends BaseWebUIMap {
    private WebElement btnMenu;

    @FindBy(css = ".shopping_cart_link")
    private WebElement linkShoppingCart;

    @FindBy(css = ".shopping_cart_link > span.fa-layers-counter")
    private WebElement spanShoppingCounter;

    public WebElement getBtnMenu() {
        btnMenu = webAction.findElement(By.cssSelector("div.bm-burger-button > button"));
        return btnMenu;
    }

    public WebElement getLinkShoppingCart() {
        return linkShoppingCart;
    }

    public WebElement getSpanShoppingCounter() {
        return spanShoppingCounter;
    }
}
