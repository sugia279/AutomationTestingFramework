package core.test_execution;

import core.base_action.BaseAction;
import core.base_action.SoftAssertExt;
import core.extent_report.TestReportManager;
import core.testdata_manager.TestCase;
import core.base_action.BrowserType;
import core.testdata_manager.TestDataManager;
import core.testdata_manager.TestStep;
import core.testdata_manager.TestVariableManager;
import core.utilities.*;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.*;

public abstract class BaseTest {
    protected static String configFile;
    protected static String prodVer;
    protected String[] testIdList;
    protected String testDataPath;
    protected TestCase curTestCase = null;
    protected TestDataManager testDataManager;
    protected BaseAction baseAction;
    private String userKeywordPackage;
    protected LinkedHashMap<String, Object> actionClasses;
    protected TestVariableManager testVars;

    public BaseTest() {
        baseAction = new BaseAction();
        testDataManager = new TestDataManager();
        actionClasses = new LinkedHashMap<>();
        testVars = new TestVariableManager();
    }

    @BeforeSuite
    @Parameters({"config-file"})
    public void setup(@Optional String cfgFile, ITestContext testContext) {
        if(cfgFile != null)
            configFile = cfgFile;
        TestReportManager.getInstance().initializeReport(testContext.getSuite().getName().toUpperCase());
    }

    @AfterSuite
    public void close() {
        TestReportManager.getInstance().getTestReport().flush();
//        TestReportManager.getInstance().updateRunDurationForTestInLeft();
    }

    @BeforeClass
    public void beforeClass(ITestContext testContext) {
        testVars.getRuntimeVars().putAll(Config.getHashMapProperties(configFile));
        if(baseAction.getWebAction() != null) {
            String browserType = (String)testVars.getRuntimeVars().get("browser");
            String driverTimeOut = (String)testVars.getRuntimeVars().get("driverTimeout");
            if (browserType != null && !browserType.isEmpty()) {
                baseAction.getWebAction().setBrowserType(BrowserType.find(browserType));
                TestReportManager.getInstance().setSystemInfo("Browser", baseAction.getWebAction().getBrowserType().name());
            }
            if (driverTimeOut != null && !driverTimeOut.isEmpty()) {
                baseAction.getWebAction().setTimeoutDefault(Integer.parseInt(driverTimeOut));
            }
            if (baseAction.getWebAction().getBrowser() == null) {
                baseAction.getWebAction().startBrowser();
            }
        }
        if(testVars.getRuntimeVars().get("productVersion") !=null){
            prodVer = (String)testVars.getRuntimeVars().get("productVersion");
        }
        TestReportManager.getInstance().setSystemInfo("Product Version", prodVer);

    }

    @AfterClass
    public void afterClass() {
        afterTestClass();
    }

    @DataProvider
    protected abstract Object[] testDataSet();

    @BeforeMethod
    public void beforeMethod(Object[] params, ITestContext testContext) {
        Object[] testCaseInfo = (Object[]) params[0];
        testDataPath = (String) testCaseInfo[0];
        curTestCase = (TestCase) testCaseInfo[1];
        testVars.loadSystemVariables();
        testDataManager.updateVariable(curTestCase, testVars.getSystemVars());
        // just update the runtime var for testcase info (if yes), updating the runtime vars for the test step must be done inside each test action
        testDataManager.updateTCInfoVariable(curTestCase, testVars.getRuntimeVars());
        baseAction.initSoftAssert();
        String[] suiteNames = {testContext.getSuite().getName().toUpperCase(),
                ((TestRunner) testContext).getTest().getName().toUpperCase().replace(" ", "&thinsp;"),
                testDataManager.getTestSuiteMap().get(testDataPath).get_name().replace(" ", "&thinsp;")};
        TestReportManager.getInstance().setTestInfo(curTestCase, suiteNames);
    }

    @Test(dataProvider = "testDataSet")
    public void runTestCase(Object[] params) {
        String stepInfo = "";
        if (curTestCase.isActive()) {
            for (TestStep step : curTestCase.get_testSteps()) {
                step.setName((String)StringHandler.replaceValueByMapData(step.getName(),"@var->", "@", testVars.getRuntimeVars()));
                stepInfo = step.getName() + "</br><u>Action Class:</u> " + step.getClassExecution() + "</br><u>Action:</u> " + step.getMethod();
                TestReportManager.getInstance().setStepInfo(stepInfo);
                step.setTestParams(HashMapHandler.replaceValueByMapData(step.getTestParams(), "@var->", "@", testVars.getRuntimeVars()));

                if(step.getClassExecution() != null) {
                    try {
                        Object actionClass = actionClasses.get(step.getClassExecution());
                        if (actionClass == null) {
                            Class<?> clazz = null;
                            try {
                                // find keyword in core package first
                                clazz = Class.forName("core.keywords." + step.getClassExecution());
                            } catch (ClassNotFoundException e) {
                                // then find keyword in the defined user package
                                clazz = Class.forName(getUserKeywordPackage() + "." + step.getClassExecution());
                            }

                            Constructor<?> cons = clazz.getConstructor(BaseAction.class);
                            actionClass = cons.newInstance(baseAction);
                            actionClasses.put(step.getClassExecution(), actionClass);
                        }

                        Method setSAAction = actionClass.getClass().getMethod("setSoftAssert", baseAction.getSoftAssert().getClass());
                        setSAAction.invoke(actionClass, baseAction.getSoftAssert());

                        Method setTestVars = actionClass.getClass().getMethod("setTestVars", testVars.getRuntimeVars().getClass());
                        setTestVars.invoke(actionClass, testVars.getRuntimeVars());

                        Class<?>[] methodClasses = null;
                        for(Method m : actionClass.getClass().getMethods()){
                            if(m.getName().equals(step.getMethod()) && m.getParameterCount() == step.getTestParams().size()){
                                methodClasses = new Class<?>[m.getParameterCount()];
                                for(int i =0 ; i< m.getParameterTypes().length; i++){
                                    methodClasses[i] = m.getParameterTypes()[i];
                                }
                            }
                        }

                        Method action = actionClass.getClass().getMethod(step.getMethod(), methodClasses);
                        Object[] values = new Object[methodClasses.length];
//                        for(int i = 0; i < methodClasses.length; i++){
//                            values[i] = .get(i);
//                        }
                        action.invoke(actionClass, step.getTestParams().values().toArray());

                        Method getSAAction = actionClass.getClass().getMethod("getSoftAssert");
                        baseAction.setSoftAssert((SoftAssertExt) getSAAction.invoke(actionClass));
                    } catch (InvocationTargetException e) {
                        if (e.getTargetException() instanceof SkipException) {
                            throw (SkipException) e.getTargetException();
                        } else
                            baseAction.getSoftAssert().assertTrue(false, e.getTargetException().getMessage());
                    } catch (Exception e) {
                        baseAction.getSoftAssert().assertTrue(false, e.toString());
                    }
                }
            }
            baseAction.getSoftAssert().assertAll();
        } else {
            TestReportManager.getInstance().getTestReport().testSkip(curTestCase.getNote());
        }
    }

    @AfterMethod
    public void afterMethod(Object[] params, ITestResult result) {
        afterTestMethod(curTestCase, result);
    }

    protected Object[] fetchDataToDataSet(String... dataPaths) {
        ArrayList<Object[]> arrTestData = new ArrayList<Object[]>();

        for (String path : dataPaths) {
            testDataManager.setTestData(path);
            ArrayList<TestCase> allTestCase = testDataManager.getTestSuiteMap().get(path).get_testCases();
            for (int i = 0; i < allTestCase.size(); i++) {
                if (testIdList != null && !Arrays.stream(testIdList).anyMatch(allTestCase.get(i).getId()::equals)) {
                    continue;
                }
                arrTestData.add(new Object[]{path, allTestCase.get(i)});
            }
        }

        return arrTestData.toArray();
    }

    protected void afterTestMethod(TestCase tc, ITestResult result) {
        Reporter.log(tc.getName());

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                TestReportManager.getInstance().setStepPass("", "", tc.getId() + ": " + tc.getName());
                break;
            case ITestResult.SKIP:
                Reporter.log(result.getThrowable().toString());
                TestReportManager.getInstance().setStepSkip(result.getThrowable().getMessage(), tc.getId() + ": " + tc.getName());
                break;
            case ITestResult.FAILURE:
                String failedString = "";
                if (result.getThrowable().getMessage() != null) {
                    String[] failedMsgs = result.getThrowable().getMessage().split("\n\t");
                    for (int i = 1; i < failedMsgs.length; i++) {
                        failedString = failedString + "<br>" + i + ". " + failedMsgs[i];
                    }
                    if (failedString.equals("")) {
                        failedString = result.getThrowable().getMessage();
                    }
                }
                TestReportManager.getInstance().setStepFail(failedString, tc.getId() + ": " + tc.getName());
                break;
        }

        //keep duration time of test case running
        TestReportManager.getInstance().saveDurationTime("[" + tc.getId() + "] " + tc.getName());
    }

    protected void afterTestClass() {
        testDataManager.clearTestSuiteMap();
        testVars.clear();
        actionClasses.clear();
        if (baseAction.getWebAction() != null && baseAction.getWebAction().getBrowser() != null) {
            baseAction.getWebAction().stopAllBrowsers();
        }
    }

    public String getUserKeywordPackage() {
        return userKeywordPackage;
    }

    public void setUserKeywordPackage(String highLevelActionPackage) {
        this.userKeywordPackage = highLevelActionPackage;
    }
}

