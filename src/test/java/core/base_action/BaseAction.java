package core.base_action;

import core.testdata_manager.TestDataManager;
import core.testdata_manager.TestStep;

import java.util.LinkedHashMap;

public class BaseAction {
    protected WebAction webAction;
    protected APIAction apiAction;
    protected LinkedHashMap<String, Object> testVars;
    public BaseAction(WebAction wAction){
        this.webAction = wAction;
        this.apiAction = new APIAction();
    }

    public void setTestVars(LinkedHashMap<String, Object> vars){
        testVars = vars;
    }

    public LinkedHashMap<String, Object> getTestVars(){
        return testVars;
    }
}
