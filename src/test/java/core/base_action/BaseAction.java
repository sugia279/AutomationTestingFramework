package core.base_action;

import org.openqa.selenium.TakesScreenshot;

import java.util.LinkedHashMap;

public class BaseAction {
    private WebAction webAction;
    private RestAction restAction;
    protected LinkedHashMap<String, Object> testVars;
    protected SoftAssertExt softAssert;
    public BaseAction(){
        setWebAction(new WebAction());
        setRestAction(new RestAction());
        setSoftAssert(new SoftAssertExt());
    }
     public BaseAction(BaseAction bAction){
         webAction = bAction.getWebAction();
         restAction = bAction.getRestAction();
         softAssert = bAction.getSoftAssert();
         testVars = bAction.getTestVars();
     }


    public void setSoftAssert(SoftAssertExt softAssert) {
        this.softAssert = softAssert;
    }

    public void initSoftAssert() {
        if(webAction.getBrowser() !=null) {
            setSoftAssert(new SoftAssertExt((TakesScreenshot) webAction.getBrowser()));
        }
        else
            setSoftAssert(new SoftAssertExt());
    }

    public SoftAssertExt getSoftAssert() {
        return softAssert;
    }

    public void setTestVars(LinkedHashMap<String, Object> vars){
        testVars = vars;
    }

    public LinkedHashMap<String, Object> getTestVars(){
        return testVars;
    }

    public WebAction getWebAction() {
        return webAction;
    }

    public void setWebAction(WebAction webAction) {
        this.webAction = webAction;
    }

    public RestAction getRestAction() {
        return restAction;
    }

    public void setRestAction(RestAction restAction) {
        this.restAction = restAction;
    }
}
