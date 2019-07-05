package core.test_execution;

import core.base_action.APIAction;
import core.base_action.WebAction;
import core.extent_report.TestReportManager;
import core.testdata_manager.TestCase;
import core.base_action.BrowserType;
import core.testdata_manager.TestDataManager;
import core.testdata_manager.TestStep;
import core.utilities.Config;
import core.utilities.DateTimeHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;
import org.testng.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public abstract class BaseTest {
    protected String[] testIdList;
    protected String testDataPath;
    protected TestCase curTestCase = null;
    protected TestDataManager testDataManager;
    protected APIAction api;
    protected WebAction webAction;
    protected String highLevelActionFolder;
    protected LinkedHashMap<String, Object> vars;

    public BaseTest(){
        webAction = new WebAction();
        api = new APIAction();
        testDataManager = new TestDataManager();

        loadSystemVariables();
    }

    @BeforeSuite
    @Parameters({"config-file"})
    public void setup(@Optional String configFile) {
        TestReportManager.getInstance().initializeReport();
        if(configFile!= null) {
            String browser = Config.getProperty(configFile, "browser");
            String timeout = Config.getProperty(configFile, "driverTimeout");
            if(browser!= null && !browser.isEmpty()){ webAction.setBrowserType(BrowserType.find(browser));}
            if(timeout!= null && !browser.isEmpty()){ webAction.setTimeoutDefault(Integer.parseInt(timeout));}
        }
    }

    @AfterSuite
    public void close() {
        TestReportManager.getInstance().getTestReport().flush();
        TestReportManager.getInstance().updateRunDurationForTestInLeft();
    }

    @BeforeClass
    public void beforeClass(){
        if(webAction.getBrowser() == null || webAction.getBrowser().toString().contains("(null)"))
            webAction.startBrowser();
    }

    @AfterClass
    public void afterClass(){
        afterTestClass(true);
    }

    @DataProvider
    protected abstract Object[] testDataSet() ;

    @BeforeMethod
    public void beforeMethod(Object[] params, ITestContext testContext){
        Object[] testCaseInfo = (Object[])params[0];
        testDataPath = (String) testCaseInfo[0];
        curTestCase = (TestCase) testCaseInfo[1];
        testDataManager.updateVariable(curTestCase, vars);
        webAction.initSoftAssert();
        String[] suiteNames = {testContext.getSuite().getName().toUpperCase() , testDataManager.getTestSuiteMap().get(testDataPath).get_name()};
        TestReportManager.getInstance().setTestInfo(curTestCase, suiteNames);
    }

    @Test(dataProvider = "testDataSet")
    public void runTestCase(Object[] params){
        String stepInfo = "";
        for (TestStep step: curTestCase.get_testSteps()) {
            stepInfo = step.getName() + "</br><u>Action Class:</u> " + step.getClassExecution() + "</br><u>Action:</u> " + step.getMethod();
            TestReportManager.getInstance().setStepInfo(stepInfo);
            try {
                Class<?> cl = Class.forName(highLevelActionFolder + step.getClassExecution());
                Constructor<?> cons = cl.getConstructor(WebAction.class);
                Object actionClass = cons.newInstance(webAction);
                Method setTestVars = actionClass.getClass().getMethod("setTestVars",vars.getClass());
                setTestVars.invoke(actionClass, vars);
                Method action = actionClass.getClass().getMethod(step.getMethod(),step.getClass());
                action.invoke(actionClass, step);
            }
            catch(Exception e){
                webAction.getSoftAssert().assertTrue(false, e.toString());
            }
        }
        webAction.getSoftAssert().assertAll();
    }

    @AfterMethod
    public void afterMethod(Object[] params, ITestResult result) {
        afterTestMethod(curTestCase, result, false);
    }

    protected Object[] fetchDataToDataSet(String... dataPaths){
        ArrayList<Object[]> arrTestData = new ArrayList<Object[]>();

        for(String path : dataPaths){
            testDataManager.setTestData(path);
            ArrayList<TestCase> allTestCase = testDataManager.getTestSuiteMap().get(path).get_testCases();
            for (int i = 0; i < allTestCase.size(); i++) {
                if (testIdList != null && !Arrays.stream(testIdList).anyMatch(allTestCase.get(i).getId()::equals)) {
                    continue;
                }
                arrTestData.add(new Object[]{ path, allTestCase.get(i)});
            }
        }

        return arrTestData.toArray();
    }

    protected void afterTestMethod(TestCase tc, ITestResult result, boolean stopBrowser){
        Reporter.log(tc.getName());

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                TestReportManager.getInstance().setStepPass(tc.getId() + ": " + tc.getName(), "GREEN");
                break;
            case ITestResult.SKIP:
                Reporter.log(result.getThrowable().toString());
                TestReportManager.getInstance().setStepSkip(tc.getId() + ": " + tc.getName(), result.getThrowable().getMessage(), "ORANGE");
                break;
            case ITestResult.FAILURE:
                String errors = result.getThrowable().getMessage();
                if (errors != null) {
                    errors = errors.replace("\n\t", "<br>");
                }
                String scrFile = ((TakesScreenshot) webAction.getBrowser()).getScreenshotAs(OutputType.BASE64);
                TestReportManager.getInstance().setStepFail(errors, scrFile, tc.getId() + ": " + tc.getName());
                break;
        }
        TestReportManager.getInstance().saveDurationTime("[" + tc.getId() + "] " + tc.getName());

        if(stopBrowser){
            webAction.stopBrowser();
        }
    }

    protected void afterTestClass(boolean stopBrowser){
        testDataManager.clearTestSuiteMap();
        vars.clear();
        if(stopBrowser && webAction.getBrowser() != null){
            webAction.stopBrowser();
        }
    }

    public void loadSystemVariables(){
        vars = new LinkedHashMap<>();
        vars.put("CURTIME_HH:mm", DateTimeHandler.currentDayPlus("HH:mm", 0));
        vars.put("CURTIME_HHmm", DateTimeHandler.currentDayPlus("HHmm", 0));
        vars.put("CURTIME_hhmma", DateTimeHandler.currentDayPlus("hhmma", 0));
        vars.put("CURTIME_HHmmss", DateTimeHandler.currentDayPlus("HHmmss", 0));
        vars.put("CURTIME-1_HHmm", DateTimeHandler.currentDayMinutePlus("HHmm", -1));
        vars.put("CURTIME-1_hhmma", DateTimeHandler.currentDayMinutePlus("hhmma", -1));
        vars.put("CURTIME-60_hhmma", DateTimeHandler.currentDayMinutePlus("hhmma", -60));
        vars.put("NOW+1_HH:mm", DateTimeHandler.currentDayMinutePlus("HH:mm", 1));
        vars.put("CURTIME+1_HHmm", DateTimeHandler.currentDayMinutePlus("HHmm", 1));
        vars.put("CURTIME+1_hhmma", DateTimeHandler.currentDayMinutePlus("hhmma", 1));
        vars.put("TODAY", DateTimeHandler.currentDayPlus("yyyy-MM-dd_HH-mm-ss", 0));
        vars.put("TODAY_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", 0));
        vars.put("TODAY_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", 0));
        vars.put("TODAY_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", 0));
        vars.put("TODAY_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", 0));
        vars.put("YESTERDAY_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", -1));
        vars.put("YESTERDAY_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", -1));
        vars.put("YESTERDAY_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", -1));
        vars.put("TOMORROW_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +1));
        vars.put("TOMORROW_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", +1));
        vars.put("TOMORROW_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", +1));
        vars.put("TOMORROW_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", +1));
        vars.put("NEXTMONTH_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +30));
        vars.put("NEXTMONTH_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", +30));
        vars.put("NEXTMONTH_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", +30));
        vars.put("NEXTMONTH_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", +30));
        vars.put("NEXTMONTH+2_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +32));
        vars.put("NEXTMONTH+2_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", +32));
        vars.put("PREVIOUSMONTH_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", -30));
        vars.put("PREVIOUSMONTH_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", -30));
        vars.put("PREVIOUSMONTH_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", -30));
    }
}

