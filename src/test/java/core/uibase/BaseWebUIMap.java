package core.uibase;


import core.actionbase.WebAction;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class BaseWebUIMap {
    protected WebAction webAction;
    public BaseWebUIMap()    {  }
    public void setWebAction(WebAction wAction) {
        webAction = wAction;
    }

    public void initElementMap(){
        PageFactory.initElements(new AjaxElementLocatorFactory(webAction.getBrowser(), webAction.getTimeoutDefault()), this);
    }
}
