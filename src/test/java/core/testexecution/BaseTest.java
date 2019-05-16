package core.testexecution;

import core.actionbase.APIAction;
import core.actionbase.WebAction;
import core.extentreport.TestReportManager;
import core.testdatamanager.TestCase;
import core.actionbase.BrowserType;
import core.testdatamanager.TestDataManager;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseTest {
    protected String[] testIdList;
    protected String testDataPath;
    protected TestCase curTestCase = null;
    protected TestDataManager testDataManager;
    protected APIAction api;
    protected WebAction webAction;

    public BaseTest(){
        webAction = new WebAction();
        api = new APIAction();
        testDataManager = new TestDataManager();
    }

    @BeforeSuite
    @Parameters({"config-file"})
    public void setup(@Optional String configFile) {
        TestReportManager.getInstance().initializeReport();
        testDataManager.loadSettingVariables();
        if(configFile!= null) {
            testDataManager.loadSettingFromFile(configFile);
            if (testDataManager.getSettingVars().get("browser") != null) {
                webAction.setBrowserType(BrowserType.find((String) testDataManager.getSettingVars().get("browser")));
            }
            if (testDataManager.getSettingVars().get("driverTimeout") != null) {
                webAction.setTimeoutDefault(Integer.parseInt((String) testDataManager.getSettingVars().get("driverTimeout")));
            }
        }
    }

    @AfterSuite
    public void close() {
        TestReportManager.getInstance().getTestReport().flush();
        TestReportManager.getInstance().updateRunDurationForTestInLeft();
    }

    @AfterClass
    public void afterClass(){
        afterTestClass(true);
    }

    @BeforeMethod
    public void beforeMethod(Object[] params, ITestContext testContext){
        Object[] testCaseInfo = (Object[])params[0];
        curTestCase = (TestCase) testCaseInfo[0];

        TestReportManager.getInstance().setTestInfo(curTestCase, testContext.getSuite().getName().toUpperCase() , testContext.getName());
        if(webAction.getBrowser() == null || webAction.getBrowser().toString().contains("(null)"))
            webAction.startBrowser();
    }

    @AfterMethod
    public void afterMethod(Object[] params, ITestResult result) {
        afterTestMethod(curTestCase, result, false);
    }

    protected Object[] fetchDataToDataSet(String testPath){
        ArrayList<Object[]> arrTestData = new ArrayList<Object[]>();

        if(!TestDataManager.getInstance().getTestSuiteMap().containsKey(testPath)){
            testDataPath = testPath;
            TestDataManager.getInstance().setTestData(testPath);
        }
        ArrayList<TestCase> allTestCase = TestDataManager.getInstance().getTestSuiteMap().get(testDataPath).get_testCases();
        for (int i = 0; i < allTestCase.size(); i++) {
            if (testIdList != null && !Arrays.stream(testIdList).anyMatch(allTestCase.get(i).getId()::equals)) {
                continue;
            }
            arrTestData.add(new Object[]{allTestCase.get(i)});
        }
        return arrTestData.toArray();
    }

    protected void afterTestMethod(TestCase tc, ITestResult result, boolean stopBrowser){
        Reporter.log(tc.getName());

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                TestReportManager.getInstance().setStepPass(tc.getId() + ": " + tc.getName(),"GREEN");
                break;
            case ITestResult.SKIP:
                Reporter.log(result.getThrowable().toString());
                TestReportManager.getInstance().setStepSkip(tc.getId() + ": " + tc.getName(), result.getThrowable().getMessage(),"ORANGE");
                break;
            case ITestResult.FAILURE:
                TestReportManager.getInstance().getTestReport().testLog(result.getThrowable().getMessage());
                TestReportManager.getInstance().setStepFail(tc.getId() + ": " + tc.getName(),"RED");
                break;
        }
        TestReportManager.getInstance().saveDurationTime("[" + tc.getId() + "] " + tc.getName());

        if(stopBrowser){
            webAction.stopBrowser();
        }
    }

    protected void afterTestClass(boolean stopBrowser){
        testDataManager.clearTestSuiteMap();
        if(stopBrowser && webAction.getBrowser() != null){
            webAction.stopBrowser();
        }
    }
}

