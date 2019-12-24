package core.extent_report;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import core.utilities.ArrayListHandler;
import core.utilities.HashMapHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

//import com.drew.metadata.Directory;

public class TestReport {
    private String configFilePath = "", htmlOutputFile ="";
    private HashMap<String,String> envMap = null;
    private ExtentReports extent;
    private ThreadLocal<ExtentTest> testCase = new ThreadLocal();
    private ArrayList<ThreadLocal<ExtentTest>> curSuites = new ArrayList<ThreadLocal<ExtentTest>>();
    private static final Logger logger = LoggerFactory.getLogger(TestReport.class);
    public TestReport()
    {
    }

    public void initReport(String _configFilePath, String _htmlOutputFile, HashMap<String,String> _envMap ) {
        if(extent == null) {
            extent = initExtentReport(_configFilePath, _htmlOutputFile, _envMap);
        }
        else {
            if(!configFilePath.equals(_configFilePath) || !envMap.equals(_envMap))
            {
                extent = initExtentReport(configFilePath, htmlOutputFile, envMap);
            }
        }
    }

    private ExtentReports initExtentReport(String _configFilePath, String _htmlOutputFile, HashMap<String,String> _envMap)    {
        ExtentReports ext = new ExtentReports();
        configFilePath = _configFilePath;
        htmlOutputFile = _htmlOutputFile;
        envMap = _envMap;

        //Create Report directory
        File reportFolder = new File(FilenameUtils.getFullPathNoEndSeparator(htmlOutputFile));
        if (!reportFolder.exists()) {
            reportFolder.mkdir();
        }

        //init html report
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(htmlOutputFile);
        if (!configFilePath.equals("")) {
            ext.setReportUsesManualConfiguration(true);
            htmlReporter.loadXMLConfig(configFilePath);
        }
        ext.attachReporter(htmlReporter);
        for (String key : envMap.keySet()) {
            ext.setSystemInfo(key, envMap.get(key));
        }
        return ext;
    }

    public ExtentReports getExtentReports()
    {
        return extent;
    }

    public void flush() {
        extent.flush();
    }

    public void setAnalysisSUITE()    {
        if (extent != null) {
            extent.setAnalysisStrategy(AnalysisStrategy.SUITE);
        } else {
            throw new NullPointerException("Report has NOT been initialized yet");
        }
    }

    public void createTestSuite(LinkedHashMap<String,String> suitesHierarchy) {
        LinkedHashMap<Integer, Object> indexMap = HashMapHandler.CreateHashMapValueWithKeyIndex(suitesHierarchy.keySet().toArray(), 0);
        String suiteName, suiteDes;
        ThreadLocal<ExtentTest> extTestSuite = new ThreadLocal<>();
        for(int i=0; i< indexMap.size();i++)
        {
            suiteName = (String)indexMap.get(i);
            suiteDes = (String)suitesHierarchy.get(suiteName);
            if(curSuites.size() > i) {
                if (suiteName != curSuites.get(i).get().getModel().getName()) {
                    ArrayListHandler.RemoveAt(curSuites, i, curSuites.size() - 1);
                    break;
                }
            }
            extTestSuite.set(extent.createTest(suiteName, suiteDes));
            curSuites.add(extTestSuite);
        }
    }

    public void addTestcaseToSUITE(String name, String des, String... cats) {
        ExtentTest t = curSuites.get(curSuites.size() - 1).get().createNode(name, des);
        testCase.set(t);
        for(String cat:cats) {
            testCase.get().assignCategory(cat);
        }
    }
    public void addTestSuite(String name, String des, String... cats) {

        ThreadLocal<ExtentTest> extTestSuite = new ThreadLocal<>();
        extTestSuite.set(extent.createTest(name, des));
        for(String cat:cats) {
            extTestSuite.get().assignCategory(cat);
        }
        curSuites.add(extTestSuite);
    }

    public String getLastTestSuiteName()    {
        String name ="";
        name = curSuites.size() > 0?curSuites.get(curSuites.size() - 1).get().getModel().getName():null;
        return name;
    }

    public void addTestcaseToTEST(String name, String des, String... cats) {
        ExtentTest t = extent.createTest(name, des);
        testCase.set(t);
        for(String cat:cats) {
            testCase.get().assignCategory(cat);
        }
    }

    public void testEndLog() {
        logger.info(StringUtils.repeat("     ", 2) + "end testCase.");
    }


    public void testLog(Status status, String msg) {
        testCase.get().log(status,msg);
    }

    public void testLog(String msg) {
        testLog(Status.INFO, msg);
    }

    public void testPass(String msg, String imagePath) {
        ExtentTest test = testCase.get();
        if (!imagePath.equals("")) {
            try {
                test.pass(msg + "<br> <b>Screen:</b> ", MediaEntityBuilder.createScreenCaptureFromBase64String(imagePath).build());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            test.pass(msg);
        }
    }

    public void testSkip(String msg) {
        ExtentTest test = testCase.get();
        test.skip(msg);
    }

    public void testFail(String msg, String imagePath) {
        ExtentTest test = testCase.get();

        if (!imagePath.equals("")) {
            try {
                test.fail(msg + "<br> <b>Error Screen:</b> ", MediaEntityBuilder.createScreenCaptureFromBase64String(imagePath).build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            test.fail(msg);
        }
    }

    public String getTestCaseDurationTime(){
        return testCase.get().getModel().getRunDuration();
    }

    public Date getTestCaseStartTime(){
        return testCase.get().getModel().getStartTime();
    }

    public String getTestSuiteDurationTime(){
        return curSuites.get(curSuites.size() - 1).get().getModel().getRunDuration();
    }

    public Date getTestSuiteStartTime(){
        return curSuites.get(curSuites.size() - 1).get().getModel().getStartTime();
    }
}
