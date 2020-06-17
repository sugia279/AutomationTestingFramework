package core.testdata_manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import core.utilities.HashMapHandler;
import core.utilities.JsonHandler;
import core.utilities.JsonTableModel;
import core.utilities.StringHandler;

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
        //JSONObject jsonObj = JsonHandler.getDataFile(getTestDataFolder() + testPath);
        JsonNode jsonObj = JsonHandler.readJsonFromFile(getTestDataFolder() + testPath);
        tSuite.set_name(JsonHandler.getTextValue(jsonObj,"suiteName"));
        tSuite.set_description(JsonHandler.getTextValue(jsonObj,"suiteDescription"));
        tSuite.set_note(JsonHandler.getTextValue(jsonObj,"note"));
        ArrayNode testCases= (ArrayNode)JsonHandler.getJsonNode(jsonObj,"testCases");
        if(testCases!= null) {
            for (int i = 1; i <= testCases.size(); i++) {
                JsonNode jsonTestCase = testCases.get(i - 1);
                tSuite.get_testCases().addAll(loadTestCasesFromJson(jsonTestCase));
            }
        }
        return tSuite;
    }

    private ArrayList<TestCase> loadTestCasesFromJson(JsonNode jsonTestCase) {
        ArrayList<TestCase> arr = new ArrayList<>();
        TestCase tcSource = loadTestCaseFromJson(jsonTestCase);
        JsonNode dataTB = JsonHandler.getJsonNode(jsonTestCase,"dataTable");
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

    private TestCase loadTestCaseFromJson(JsonNode jsonTestCase)    {
        // Load TestCase from JSONObject
        TestCase tc = new TestCase(JsonHandler.getTextValue(jsonTestCase,"testId"), JsonHandler.getTextValue(jsonTestCase,"testName"), JsonHandler.getTextValue(jsonTestCase,"testDescription"));
        tc.setObjectives(JsonHandler.getTextValue(jsonTestCase,"testObjectives"));
        tc.setNote(JsonHandler.getTextValue(jsonTestCase,"note"));
        tc.setActive(JsonHandler.getTextValue(jsonTestCase,"active"));
        tc.setTag(JsonHandler.getTextValue(jsonTestCase,"tag"));
        ArrayNode testSteps = JsonHandler.getArrayNode(jsonTestCase,"testSteps");
        for(JsonNode jsonStep : testSteps) {
            JsonNode params = JsonHandler.getJsonNode(jsonStep,"parameters");
            TestStep testStep = new TestStep(JsonHandler.getTextValue(jsonStep,"name"), JsonHandler.getTextValue(jsonStep,"testDescription"), JsonHandler.getTextValue(jsonStep,"class"),JsonHandler.getTextValue(jsonStep,"method"));
            if(params!=null) {
                testStep.setTestParams(JsonHandler.convertJsonToLinkedHashMap(params));
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
