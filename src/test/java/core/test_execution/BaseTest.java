package core.test_execution;

import core.base_action.RestAction;
import core.base_action.WebAction;
import core.extent_report.TestReportManager;
import core.testdata_manager.TestCase;
import core.base_action.BrowserType;
import core.testdata_manager.TestDataManager;
import core.testdata_manager.TestStep;
import core.utilities.Config;
import core.utilities.DateTimeHandler;
import core.utilities.NumberHandler;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class BaseTest {
    protected String[] testIdList;
    protected String testDataPath;
    protected TestCase curTestCase = null;
    protected TestDataManager testDataManager;
    protected RestAction api;
    protected WebAction webAction;
    protected String highLevelActionFolder;
    protected LinkedHashMap<String,Object> actionClasses;
    protected LinkedHashMap<String, Object> systemVars;
    protected LinkedHashMap<String, Object> runtimeVars;

    public BaseTest(){
        webAction = new WebAction();
        api = new RestAction();
        testDataManager = new TestDataManager();
        actionClasses = new LinkedHashMap<>();
        loadSystemVariables();
        runtimeVars = new LinkedHashMap();
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
        //TestReportManager.getInstance().updateRunDurationForTestInLeft();
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
        testDataManager.updateVariable(curTestCase, systemVars);
        webAction.initSoftAssert();
        String[] suiteNames = {testContext.getSuite().getName().toUpperCase() ,
                ((TestRunner) testContext).getTest().getName().replace(" ", "&thinsp;"),
                testDataManager.getTestSuiteMap().get(testDataPath).get_name().replace(" ", "&thinsp;")};
        TestReportManager.getInstance().setTestInfo(curTestCase, suiteNames);
    }

    @Test(dataProvider = "testDataSet")
    public void runTestCase(Object[] params){
        String stepInfo = "";
        for (TestStep step: curTestCase.get_testSteps()) {
            stepInfo = step.getName() + "</br><u>Action Class:</u> " + step.getClassExecution() + "</br><u>Action:</u> " + step.getMethod();
            TestReportManager.getInstance().setStepInfo(stepInfo);

            try {
                Object actionClass = actionClasses.get(step.getClassExecution());
                if(actionClass == null){
                    Class<?> cl = Class.forName(highLevelActionFolder + step.getClassExecution());
                    Constructor<?> cons = cl.getConstructor(WebAction.class);
                    actionClass = cons.newInstance(webAction);
                    actionClasses.put(step.getClassExecution(),actionClass);
                }

                Method setTestVars = actionClass.getClass().getMethod("setTestVars", runtimeVars.getClass());
                setTestVars.invoke(actionClass, runtimeVars);
                Method action = actionClass.getClass().getMethod(step.getMethod(),step.getClass());
                action.invoke(actionClass, step);
            }
            catch(InvocationTargetException e){
                webAction.getSoftAssert().assertTrue(false, e.getTargetException().getMessage());
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
                TestReportManager.getInstance().setStepPass("", "",tc.getId() + ": " + tc.getName());
                break;
            case ITestResult.SKIP:
                Reporter.log(result.getThrowable().toString());
                TestReportManager.getInstance().setStepSkip(tc.getId() + ": " + tc.getName(), result.getThrowable().getMessage(), "ORANGE");
                break;
            case ITestResult.FAILURE:
                String failedString = "";
                if (result.getThrowable().getMessage() != null) {
                    String[] failedMsgs = result.getThrowable().getMessage().split("\n\t");
                    for (int i = 1; i < failedMsgs.length; i++) {
                        failedString = failedString + "<br>" + i + ". " + failedMsgs[i];
                    }
                    if(failedString.equals("")){
                        failedString = result.getThrowable().getMessage();
                    }
                }

                TestReportManager.getInstance().setStepFail(failedString, tc.getId() + ": " + tc.getName());
                break;
        }
        //TestReportManager.getInstance().saveDurationTime("[" + tc.getId() + "] " + tc.getName());

        if(stopBrowser){
            webAction.stopBrowser();
        }
    }

    protected void afterTestClass(boolean stopBrowser){
        testDataManager.clearTestSuiteMap();
        systemVars.clear();
        actionClasses.clear();
        if(stopBrowser && webAction.getBrowser() != null){
            webAction.stopBrowser();
        }
    }

    public void loadSystemVariables(){
        systemVars = new LinkedHashMap<>();
        systemVars.put("CURTIME_HH:mm", DateTimeHandler.currentDayPlus("HH:mm", 0));
        systemVars.put("CURTIME_HHmm", DateTimeHandler.currentDayPlus("HHmm", 0));
        systemVars.put("CURTIME_hhmma", DateTimeHandler.currentDayPlus("hhmma", 0));
        systemVars.put("CURTIME_HHmmss", DateTimeHandler.currentDayPlus("HHmmss", 0));
        systemVars.put("CURTIME-1_HHmm", DateTimeHandler.currentDayMinutePlus("HHmm", -1));
        systemVars.put("CURTIME-1_hhmma", DateTimeHandler.currentDayMinutePlus("hhmma", -1));
        systemVars.put("CURTIME-60_hhmma", DateTimeHandler.currentDayMinutePlus("hhmma", -60));
        systemVars.put("NOW+1_HH:mm", DateTimeHandler.currentDayMinutePlus("HH:mm", 1));
        systemVars.put("CURTIME+1_HHmm", DateTimeHandler.currentDayMinutePlus("HHmm", 1));
        systemVars.put("CURTIME+1_hhmma", DateTimeHandler.currentDayMinutePlus("hhmma", 1));
        systemVars.put("TODAY", DateTimeHandler.currentDayPlus("yyyy-MM-dd_HH-mm-ss", 0));
        systemVars.put("TODAY_dd-MM-yyyy_HH-mm-ss", DateTimeHandler.currentDayPlus("yyyy-MM-dd_HH-mm-ss", 0));
        systemVars.put("TODAY_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", 0));
        systemVars.put("TODAY_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", 0));
        systemVars.put("TODAY_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", 0));
        systemVars.put("TODAY_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", 0));
        systemVars.put("YESTERDAY_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", -1));
        systemVars.put("YESTERDAY_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", -1));
        systemVars.put("YESTERDAY_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", -1));
        systemVars.put("TOMORROW_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +1));
        systemVars.put("TOMORROW_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", +1));
        systemVars.put("TOMORROW_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", +1));
        systemVars.put("TOMORROW_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", +1));
        systemVars.put("NEXTMONTH_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +30));
        systemVars.put("NEXTMONTH_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", +30));
        systemVars.put("NEXTMONTH_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", +30));
        systemVars.put("NEXTMONTH_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", +30));
        systemVars.put("NEXTMONTH+2_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +32));
        systemVars.put("NEXTMONTH+2_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", +32));
        systemVars.put("PREVIOUSMONTH_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", -30));
        systemVars.put("PREVIOUSMONTH_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", -30));
        systemVars.put("PREVIOUSMONTH_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", -30));
        systemVars.put("RANDOM_Integer_7", (new Random()).nextInt(9999999) + 1);
        systemVars.put("RANDOM_IntegerString_7", "" + ((new Random()).nextInt(9999999) + 1));
        systemVars.put("RANDOM_DoubleString_9_2", "" + NumberHandler.getNumberFormat("##0.00",2).format(NumberHandler.getRandomDoubleBetweenRange(0,999999999)));
        systemVars.put("RANDOM_DoubleString_5_6", "" + NumberHandler.getNumberFormat("##0.00",6).format(NumberHandler.getRandomDoubleBetweenRange(0,99999)));
    }
}

