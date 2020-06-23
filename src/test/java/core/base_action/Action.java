package core.base_action;

import core.extent_report.ReportLogLevel;
import core.extent_report.TestReportManager;
import core.testdata_manager.TestStep;
import org.openqa.selenium.TakesScreenshot;

import java.util.LinkedHashMap;

public class Action {
    private WebAction webAction;
    private RestAction restAction;
    protected LinkedHashMap<String, Object> testVars;
    protected SoftAssertExt softAssert;
    public Action(){
        setSoftAssert(new SoftAssertExt());
    }
     public Action(Action bAction){
         webAction = bAction.getWebAction();
         restAction = bAction.getRestAction();
         softAssert = bAction.getSoftAssert();
         testVars = bAction.getTestVars();
     }


    public void setSoftAssert(SoftAssertExt softAssert) {
        this.softAssert = softAssert;
    }

    public void initSoftAssert() {
        if(webAction != null && webAction.getBrowser() !=null) {
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


    public void delayTime(TestStep step) {
        Long delayMillis = (Long) step.getTestParams().get("delayMillis");
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            TestReportManager.getInstance().getTestReport().testFail(ReportLogLevel.LOG_LVL_4 + "InterruptedException: " + e.getMessage(), "");
        }
    }
}
