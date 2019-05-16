package core.testdatamanager;

import core.utilities.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

public class TestDataManager {
    protected static String TEST_DATA_FOLDER = "src/test/java/";
    private LinkedHashMap<String, TestSuite> testSuiteMap;
    private LinkedHashMap<String, Object> settingVars;
    private LinkedHashMap<String, Object> vars;

    public TestDataManager(){
        testSuiteMap = new LinkedHashMap<>();
        settingVars = new LinkedHashMap<>();
        vars = new LinkedHashMap<>();
    }

    public static TestDataManager instance;
    public static TestDataManager getInstance(){
        if(instance == null) {
            instance = new TestDataManager();
        }
        return instance;
    }


    public LinkedHashMap<String, TestSuite> getTestSuiteMap() {
        return testSuiteMap;
    }

    public void setTestData(String testDataPath)
    {
        testSuiteMap.put(testDataPath, loadTestDataFromJson(testDataPath));
    }

    public void clearTestSuiteMap()
    {
        getVars().clear();
        for(String keyTS : testSuiteMap.keySet())
        {
            testSuiteMap.get(keyTS).clear();
        }
        testSuiteMap.clear();
    }
    //region load test data

    protected TestSuite loadTestDataFromJson(String testPath) {
        TestSuite tSuite = new TestSuite();
        JSONObject jsonObj = JsonHandler.getDataFile(TEST_DATA_FOLDER + testPath);
        tSuite.set_name((String) jsonObj.get("suiteName"));
        tSuite.set_description((String) jsonObj.get("suiteDescription"));
        tSuite.set_note(jsonObj.get("note"));
        JSONArray testCases =  ((JSONArray) JsonHandler.GetValueJSONObject(jsonObj,"testCases"));
        if(testCases!= null) {

            for (int i = 1; i <= testCases.size(); i++) {
                JSONObject jsonTestCase = (JSONObject) testCases.get(i - 1);
                tSuite.get_testCases().addAll(loadTestCasesFromJson(jsonTestCase));
            }
        }

        return tSuite;
    }

    private ArrayList<TestCase> loadTestCasesFromJson(JSONObject jsonTestCase) {
        ArrayList<TestCase> arr = new ArrayList<>();
        TestCase tcSource = loadTestCaseFromJson(jsonTestCase);
        JSONObject dataTB = JsonHandler.GetJSONObject(jsonTestCase,"dataTable");
        if(dataTB!=null) {
            JsonTableModel jsonTable = new JsonTableModel(dataTB);
            for (int i = 0; i < jsonTable.getRowCount(); i++) {
                TestCase tcRefined = new TestCase();
                tcRefined = applyTestValueFromRowData(tcSource, jsonTable.getRowData(i));
                arr.add(tcRefined);
            }
        }
        else
            arr.add(tcSource);
        return arr;
    }

    private TestCase applyTestValueFromRowData(TestCase tcSource, LinkedHashMap<String, Object> row)    {
        TestCase tcRefined = new TestCase();
        tcRefined.setId((String)StringHandler.replaceValueByMapData(tcSource.getId(),"@dataTable->","@",row));
        tcRefined.setName((String) StringHandler.replaceValueByMapData(tcSource.getName(),"@dataTable->","@",row));
        tcRefined.setDescription((String)StringHandler.replaceValueByMapData(tcSource.getDescription(),"@dataTable->","@",row));
        tcRefined.setObjectives((String)StringHandler.replaceValueByMapData(tcSource.getObjectives(),"@dataTable->","@",row));
        tcRefined.setNote(tcSource.getNote());

        for(TestStep taSource : tcSource.get_testSteps() )
        {
            TestStep taRefined = new TestStep();
            taRefined.setName((String)StringHandler.replaceValueByMapData(taSource.getName(),"@dataTable->","@",row));
            taRefined.setDescription((String)StringHandler.replaceValueByMapData(taSource.getDescription(),"@dataTable->","@",row));
            taRefined.setTestParams(HashMapHandler.replaceValueByMapData(taSource.getTestParams(), "@dataTable->", "@", row));
            tcRefined.get_testSteps().add(taRefined);
        }
        return tcRefined;
    }

    private TestCase loadTestCaseFromJson(JSONObject jsonTestCase)    {
        // Load TestCase from JSONObject
        TestCase tc = new TestCase(jsonTestCase.get("testId"), jsonTestCase.get("testName"), jsonTestCase.get("testDescription"));
        tc.setObjectives(jsonTestCase.get("testObjectives"));
        tc.setNote(jsonTestCase.get("note"));
        tc.setActive(jsonTestCase.get("active"));
        tc.setTag(jsonTestCase.get("tag"));
        JSONArray testSteps = ((JSONArray) jsonTestCase.get("testSteps"));
        for (int i = 0; i < testSteps.size(); i++) {
            JSONObject jsonTestStep = (JSONObject) testSteps.get(i);
            JSONObject params = (JSONObject) jsonTestStep.get("parameters");
            TestStep testStep = new TestStep((String) jsonTestStep.get("name"), (String) jsonTestStep.get("testDescription"), (String) jsonTestStep.get("classExecution"),(String) jsonTestStep.get("method"));
            testStep.getTestParams().putAll(params);
            tc.get_testSteps().add(testStep);
        }
        return tc;
    }

    public void loadSettingVariables(){
        getSettingVars().put("TODAY", DateTimeHandler.currentDayPlus("yyyy-MM-dd_HH-mm-ss", 0));
        getSettingVars().put("TODAY_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", 0));
        getSettingVars().put("TODAY_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", 0));
        getSettingVars().put("TODAY_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", 0));
        getSettingVars().put("NOW_HH:mm", DateTimeHandler.currentDayPlus("HH:mm", 0));
        getSettingVars().put("NOW_HHmm", DateTimeHandler.currentDayPlus("HHmm", 0));
        getSettingVars().put("CURTIME_HHmmss", DateTimeHandler.currentDayPlus("HHmmss", 0));
        getSettingVars().put("NOW+1_HH:mm", DateTimeHandler.currentDayMinutePlus("HH:mm", 1));
        getSettingVars().put("NOW+1_HHmm", DateTimeHandler.currentDayMinutePlus("HHmm", 1));
        getSettingVars().put("YESTERDAY_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", -1));
        getSettingVars().put("TOMORROW_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +1));
        getSettingVars().put("TOMORROW_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", +1));
        getSettingVars().put("NEXTMONTH_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +30));
        getSettingVars().put("NEXTMONTH+2_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +32));
        getSettingVars().put("NEXTMONTH_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", +30));
        getSettingVars().put("hostOpenAPI","http://uxdaprhbilling03:8280/");
        getSettingVars().put("loginPageURL","http://uxdaprhbilling03:8287/#/auth/login");
    }

    public void loadSettingFromFile(String configFile)    {
        Properties props = Config.getProperties(configFile);
        for (Object key:props.keySet()) {
            getSettingVars().put((String)key,props.get(key));
        }
    }

    public LinkedHashMap<String, Object> getVars() {
        return vars;
    }

    public void setVars(LinkedHashMap<String, Object> vars) {
        this.vars = vars;
    }

    public LinkedHashMap<String, Object> getSettingVars() {
        return settingVars;
    }

    public void setSettingVars(LinkedHashMap<String, Object> settingVars) {
        this.settingVars = settingVars;
    }
    //endregion
}
