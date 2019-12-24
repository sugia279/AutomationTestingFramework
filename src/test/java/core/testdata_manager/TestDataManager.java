package core.testdata_manager;

import core.utilities.HashMapHandler;
import core.utilities.JsonHandler;
import core.utilities.JsonTableModel;
import core.utilities.StringHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TestDataManager {
    protected String testDataFolder;
    protected LinkedHashMap<String, TestSuite> testSuiteMap;

    public String getTestDataFolder() {
        return testDataFolder;
    }

    public void setTestDataPath(String testDataFolder) {
        this.testDataFolder = testDataFolder;
    }

    public TestDataManager(){
        testSuiteMap = new LinkedHashMap<>();
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
        for(String keyTS : testSuiteMap.keySet())
        {
            testSuiteMap.get(keyTS).clear();
        }
        testSuiteMap.clear();
    }
    //region load test data

    protected TestSuite loadTestDataFromJson(String testPath) {
        TestSuite tSuite = new TestSuite();
        JSONObject jsonObj = JsonHandler.getDataFile(getTestDataFolder() + testPath);
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
        tcRefined.setId((String) StringHandler.replaceValueByMapData(tcSource.getId(),"@dataTable->","@",row));
        tcRefined.setName((String) StringHandler.replaceValueByMapData(tcSource.getName(),"@dataTable->","@",row));
        tcRefined.setDescription((String) StringHandler.replaceValueByMapData(tcSource.getDescription(),"@dataTable->","@",row));
        tcRefined.setObjectives((String) StringHandler.replaceValueByMapData(tcSource.getObjectives(),"@dataTable->","@",row));
        tcRefined.setNote((String) StringHandler.replaceValueByMapData(tcSource.getNote(),"@dataTable->","@",row));

        for(TestStep taSource : tcSource.get_testSteps() )
        {
            TestStep taRefined = new TestStep();
            taRefined.setName((String) StringHandler.replaceValueByMapData(taSource.getName(),"@dataTable->","@",row));
            taRefined.setClassExecution((String) StringHandler.replaceValueByMapData(taSource.getClassExecution(), "@dataTable->", "@", row));
            taRefined.setMethod((String) StringHandler.replaceValueByMapData(taSource.getMethod(), "@dataTable->", "@", row));
            taRefined.setDescription((String) StringHandler.replaceValueByMapData(taSource.getDescription(),"@dataTable->","@",row));
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
            TestStep testStep = new TestStep((String) jsonTestStep.get("name"), (String) jsonTestStep.get("testDescription"), (String) jsonTestStep.get("class"),(String) jsonTestStep.get("method"));
            if(params!=null) {
                testStep.getTestParams().putAll(params);
            }
            tc.get_testSteps().add(testStep);
        }

        return tc;
    }

    public void updateVariable(TestCase tc, LinkedHashMap<String, Object> vars){
        updateTCInfoVariable(tc, vars);
        for(TestStep step:tc.get_testSteps()) {
            step.setTestParams(HashMapHandler.replaceValueByMapData(step.getTestParams(), "@var->", "@", vars));
        }
    }

    public void updateTCInfoVariable(TestCase tc, LinkedHashMap<String, Object> vars){
        tc.setName(StringHandler.replaceValueByMapData(tc.getName(), "@var->", "@", vars));
        tc.setDescription(StringHandler.replaceValueByMapData(tc.getDescription(), "@var->", "@", vars));
        tc.setObjectives(StringHandler.replaceValueByMapData(tc.getObjectives(), "@var->", "@", vars));
        tc.setTag(StringHandler.replaceValueByMapData(tc.getTag(), "@var->", "@", vars));
        tc.setNote(StringHandler.replaceValueByMapData(tc.getNote(), "@var->", "@", vars));
    }
}
