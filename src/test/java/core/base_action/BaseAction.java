package core.base_action;

import java.util.LinkedHashMap;

public class BaseAction {
    protected WebAction webAction;
    protected RestAction apiAction;
    protected LinkedHashMap<String, Object> testVars;
    public BaseAction(WebAction wAction){
        this.webAction = wAction;
        this.apiAction = new RestAction();
    }

    public void setTestVars(LinkedHashMap<String, Object> vars){
        testVars = vars;
    }

    public LinkedHashMap<String, Object> getTestVars(){
        return testVars;
    }
}
