package core.extent_report;

import core.testdata_manager.TestCase;
import core.utilities.DateTimeHandler;
import core.utilities.FileHandler;
import core.utilities.StringHandler;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TestReportManager {
    protected static String REPORT_CONFIG = "src/test/java/core/extent_report/extReportConfig.xml";
    private String reportOutput = System.getProperty("user.dir") + "\\reports\\[TestReport]_[date].html";

//    public static volatile TestReportManager instance;
//    private static Object mutex = new Object();
    public static TestReportManager instance;

    public static TestReportManager getInstance() {
        TestReportManager result = instance;
//        if (result == null) {
//            synchronized (mutex) {
//                result = instance;
//                if (result == null)
//                    instance = result = new TestReportManager();
//            }
//        }
        if (result == null)
            instance = result = new TestReportManager();
        return result;
    }

    private TestReport testReport = null;
    private int actionOrder = 0;

    private LinkedHashMap<String, String> durationTimes = new LinkedHashMap<>();

    public TestReportManager() {
        testReport = new TestReport();
    }

    public void initializeReport(String reportName) {
        String date = DateTimeHandler.currentDateAsString("yyyy.MM.dd.HH.mm.ss");
        reportOutput = getReportOutput().replace("[TestReport]", reportName).replace("[date]", date);
        getTestReport().initReport(REPORT_CONFIG, reportOutput,
                new HashMap<String, String>() {
                    {
                        put("User Name", System.getProperty("user.name"));
                        put("OS version", System.getProperty("os.name"));
                        put("Host Name", System.getenv("COMPUTERNAME"));
                        put("Project", "HiAffinity");
                    }
                });
    }

    public void setSystemInfo(String key, String value) {
        testReport.getExtentReports().setSystemInfo(key, value);
    }

    public void setTestInfo(TestCase tc, String[] suiteNames) {
        String testCaseDescription = "";
        if (!tc.getDescription().equals("")) {
            testCaseDescription = tc.getDescription();
        }
        if (!tc.getObjectives().equals("")) {
            testCaseDescription = testCaseDescription + "<br><b><font color=\"blue\">Objectives</font></b>: " + tc.getObjectives();
        }

        if (!tc.getNote().equals("")) {
            testCaseDescription = testCaseDescription + "<br><b><font color=\"blue\">Note</font></b>: " + tc.getNote();
        }

        if (!tc.getTag().equals("")) {
            testCaseDescription = testCaseDescription + "<br><b><font color=\"blue\">Tag</font></b>: " + tc.getTag();
        }

        String tName = "";
        if (!tc.getId().equals("")) {
            tName = "[" + tc.getId() + "] ";
        }
        tName = tName + tc.getName();
        //<span class=\"badge white-text red\">" + label + "</span><br>"

        for (int i = 0; i < suiteNames.length; i++) {
            if (i == 0) {
                suiteNames[i] = "<b>" + suiteNames[0] + "</b>";
            } else {
                suiteNames[i] = "<b>" + StringHandler.stringMultiply("&emsp;", i) + suiteNames[i] + "</b>";
            }
        }
        testReport.addTestcaseToTEST(tName, testCaseDescription, suiteNames);

        actionOrder = 0;

    }

    public void setStepInfo(String log) {
        setStepInfo(log, false);
    }


    public void setStepInfo(String log, boolean resetOrder) {
        String action = "<b>" + log + "</b>";
        actionOrder = resetOrder ? 1 : actionOrder + 1;
        action = "<b><font color=\"blue\"><u>Step " + actionOrder + ":" + "</u></font> " + log + "</b>";

        testReport.testLog(action);
    }

    public void setSubStepTitle(String log) {
        testReport.testLog("<b><u>" + log + "</u></b>");
    }

    public void setSubStepInfo(String log, String level) {
        testReport.testLog(level + log);
    }

    public void setSubStepPass(String log, String level) {
        testReport.testPass(level + log, "");
    }

    public void setSubStepPass(String log, String image, String level) {
        testReport.testPass(level + log, image);
    }

    public void setSubStepFail(String log, String level) {
        testReport.testFail(level + log, "");
    }

    public void setSubStepFail(String log, String image, String level) {
        testReport.testFail(level + log, image);
    }

    public void setStepPass(String log) {
        testReport.testPass(log, "");
    }

    public void setStepSkip(String log, String label) {
        log = "<span class=\"label white-text orange\" style=\"font: normal 12pt Source Sans Pro\">" + label + "</span><br>" + log;
        testReport.testSkip(log);
    }

    public void setStepFail(String log) {
        testReport.testFail(log, "");
    }

    public void setStepFail(String log, String label) {
        log = "<span class=\"label white-text red\" style=\"font: normal 12pt Source Sans Pro\">" + label + "</span><br>" + log;
        testReport.testFail(log, "");
    }

    public void setStepFail(String log, String imageSource, String label) {
        log = "<span class=\"label white-text red\" style=\"font: normal 12pt Source Sans Pro\">" + label + "</span><br>" + log;
        testReport.testFail(log, imageSource);
    }

    public void setStepPass(String log, String imageSource) {
        testReport.testPass(log, imageSource);
    }

    public void setStepPass(String log, String imageSource, String label) {
        log = "<span class=\"label white-text green\" style=\"font: normal 12pt Source Sans Pro" +
                "\">" + label + "</span><br>" + log;
        testReport.testPass(log, imageSource);
    }

    public void saveDurationTime(String testName) {
        String modifiedKey = "<span class='test-name'>" + testName + "</span>\r\n" +
                //"\t\t\t\t\t\t<span class='test-time'>" + new SimpleDateFormat("MMM d, yyyy hh:mm:ss a").format(testReport.getTestSuiteStartTime()) + "</span>";
                "\t\t\t\t\t\t<span class='test-time'>" + new SimpleDateFormat("MMM d, yyyy hh:mm:ss a").format(testReport.getTestCaseStartTime()) + "</span>";
        //String modifiedValue =  modifiedKey + "\t<span class=\"label time-taken grey lighten-1 white-text\">" + testReport.getTestSuiteDurationTime() + "</span>";
        String modifiedValue = modifiedKey + "\t<span class=\"label time-taken grey lighten-1 white-text\">" + testReport.getTestCaseDurationTime() + "</span>";
        durationTimes.put(modifiedKey, modifiedValue);
    }

    public void updateRunDurationForTestInLeft() {
        FileHandler.modifyFileByHashMap(getReportOutput(), durationTimes);
        durationTimes.clear();
    }

    public void injectScriptToCollapseData(){
        String css =".collapsible{background-color:#777;color:white;cursor:pointer;padding:18px;width:100%;border:none;text-align:left;outline:none;font-size:15px;margin:1px;}.active,.collapsible:hover{background-color:#555;}.content{padding:0 18px;display:none;overflow:hidden;background-color:#f1f1f1;}";
        String js = "var coll=document.getElementsByClassName(\"collapsible\");var i;for(i=0;i<coll.length;i++){coll[i].addEventListener(\"click\",function(){this.classList.toggle(\"active\");var content=this.nextElementSibling;if(content.style.display===\"block\"){content.style.display=\"none\";}else{content.style.display=\"block\";}});}";
        LinkedHashMap<String,String> collapseDataPos = new LinkedHashMap<>();

        FileHandler.modifyFileByHashMap(getReportOutput(),collapseDataPos);
    }

    public TestReport getTestReport() {
        return testReport;
    }

    public String getReportOutput() {
        return reportOutput;
    }
}
