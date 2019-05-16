package core.extentreport;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    public void flush() {
        extent.flush();
    }

    public void addTestcaseToTEST(String name, String des, String... cats) {
        ExtentTest t = extent.createTest(name, des);
        testCase.set(t);
        for(String cat:cats) {
            testCase.get().assignCategory(cat);
        }
    }

    public void testLog(Status status, String msg) {
        testCase.get().log(status,msg);
    }

    public void testLog(String msg) {
        testLog(Status.INFO, msg);
    }

    public void testPass(String msg, String colorName) {
        ExtentTest test = testCase.get();
        if(colorName!="")
            test.pass(MarkupHelper.createLabel(msg, ExtentColor.valueOf(colorName)));
        else
            test.pass(msg);
    }

    public void testSkip(String msg, String colorName) {
        ExtentTest test = testCase.get();
        if(colorName!="")
            test.skip(MarkupHelper.createLabel(msg, ExtentColor.valueOf(colorName)));
        else
            test.skip(msg);
    }

    public void testFail(String msg, String imagePath, String colorName) {
        ExtentTest test = testCase.get();
        if (!imagePath.equals("")) {
            try {
                test.fail(msg, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if(colorName!="")
                test.fail(MarkupHelper.createLabel(msg, ExtentColor.valueOf(colorName)));
            else
                test.fail(msg);
        }

    }

    public String getTestCaseDurationTime(){
        return testCase.get().getModel().getRunDuration();
    }

    public Date getTestCaseStartTime(){
        return testCase.get().getModel().getStartTime();
    }
}
