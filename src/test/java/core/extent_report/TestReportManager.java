package core.extent_report;

import core.testdata_manager.TestCase;
import core.utilities.DateTimeHandler;
import core.utilities.FileHandler;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TestReportManager {
    protected static String REPORT_CONFIG = "src/test/java/core/extent_report/extReportConfig.xml";
    private String reportOutput = System.getProperty("user.dir") + "\\reports\\TestReport_[date].html";

    public static volatile TestReportManager instance;
    private static Object mutex = new Object();
    public static TestReportManager getInstance(){
        TestReportManager result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new TestReportManager();
            }
        }
        return result;
    }
    private TestReport testReport = null;
    private int actionOrder = 0;

    private LinkedHashMap<String,String> durationTimes = new LinkedHashMap<>();

    public TestReportManager()
    {
        testReport = new TestReport();
    }

    public void initializeReport(){
        String date = DateTimeHandler.currentDateAsString("yyyy.MM.dd.HH.mm.ss");
        reportOutput = getReportOutput().replace("[date]", date);
        getTestReport().initReport(REPORT_CONFIG, reportOutput,
                new HashMap<String, String>() {
                    {
                        put("User Name", System.getProperty("user.name"));
                        put("OS version", System.getProperty("os.name"));
                        put("Java Version", System.getProperty("java.version"));
                        put("Host Name", System.getenv("COMPUTERNAME"));
                        put("Project", "HiAffinity");
                    }
                });
    }

    public void setTestInfo(TestCase tc, String[] suiteNames) {
        String testCaseDesciption = "";
        if (!tc.getDescription().equals("")) {
            testCaseDesciption = tc.getDescription();
        }
        if (!tc.getObjectives().equals("")) {
            testCaseDesciption = testCaseDesciption + "<br><b><font color=\"blue\">Objectives</font></b>: " + tc.getObjectives();
        }

        if (!tc.getNote().equals("")) {
            testCaseDesciption = testCaseDesciption + "<br><b><font color=\"blue\">Note</font></b>: " + tc.getNote();
        }

        if (!tc.getTag().equals("")) {
            testCaseDesciption = testCaseDesciption + "<br><b><font color=\"blue\">Tag</font></b>: " + tc.getTag();
        }

        String tName = "";
        if (!tc.getId().equals("")) {
            tName = "[" + tc.getId() + "] ";
        }
        tName = tName + tc.getName();
        //<span class=\"badge white-text red\">" + label + "</span><br>"

        testReport.addTestcaseToTEST(tName, testCaseDesciption, "<b>" + suiteNames[0] + "</b>"
                , "<b>---- " + suiteNames[1] + "</b>", "&emsp;&emsp;" + suiteNames[2]);

        actionOrder = 0;

    }

    public void setStepInfo(String log){
        setStepInfo(log, false);
    }


    public void setStepInfo(String log, boolean resetOrder){
        String action = "<b>" + log + "</b>";
        actionOrder = resetOrder ? 1 : actionOrder + 1;
        action = "<b><font color=\"blue\"><u>Step " + actionOrder + "</u></font> " + log + "</b>";

        testReport.testLog(action);
    }

    public void setSubStepTitle(String log){
        testReport.testLog("<b><u>" + log + "</u></b>");
    }

    public void setSubStepInfo(String log, String level){
        testReport.testLog(level + log);
    }

    public void setSubStepPass(String log, String level){
        testReport.testPass(level + log,"");
    }

    public void setSubStepPass(String log, String image, String level){
        testReport.testPass(level + log, image);
    }

    public void setSubStepFail(String log, String level){
        testReport.testFail(level + log,"");
    }

    public void setSubStepFail(String log, String image, String level){
        testReport.testFail(level + log, image);
    }

    public void setStepPass(String log)    {
        testReport.testPass(log, "");
    }

    public void setStepSkip(String log, String msg)    {
        testReport.testLog("<br>" + log);
        testReport.testSkip("<br>" + msg, "");
    }

    public void setStepFail(String log){
        testReport.testFail(log, "");
    }

    public void setStepSkip(String log, String msg, String colorMarkupLabel)    {
        testReport.testLog("<br>" + log);
        testReport.testSkip("<br>" + msg, colorMarkupLabel);
    }

    public void setStepFail(String log, String label){
        log = "<span class=\"badge white-text red\">" + label + "</span><br>" + log;
        testReport.testFail(log, "");
    }

    public void setStepFail(String log, String imageSource, String label){
        log = "<span class=\"badge white-text red\">" + label + "</span><br>" + log;
        testReport.testFail(log, imageSource);
    }

    public void setStepPass(String log, String imageSource)    {
        testReport.testPass(log, imageSource);
    }

    public void setStepPass(String log, String imageSource, String label)    {
        log = "<span class=\"badge white-text green\">" + label + "</span><br>" + log;
        testReport.testPass(log, imageSource);
    }

    public void saveDurationTime(String testName)
    {
        String modifiedKey =  "<span class='test-name'>" + testName + "</span>\r\n" +
                //"\t\t\t\t\t\t<span class='test-time'>" + new SimpleDateFormat("MMM d, yyyy hh:mm:ss a").format(testReport.getTestSuiteStartTime()) + "</span>";
                "\t\t\t\t\t\t<span class='test-time'>" + new SimpleDateFormat("MMM d, yyyy hh:mm:ss a").format(testReport.getTestCaseStartTime()) + "</span>";
        //String modifiedValue =  modifiedKey + "\t<span class=\"label time-taken grey lighten-1 white-text\">" + testReport.getTestSuiteDurationTime() + "</span>";
        String modifiedValue =  modifiedKey + "\t<span class=\"label time-taken grey lighten-1 white-text\">" + testReport.getTestCaseDurationTime() + "</span>";
        durationTimes.put(modifiedKey, modifiedValue);
    }

    public void updateRunDurationForTestInLeft()    {
        FileHandler.modifyFileByHashMap(getReportOutput(),durationTimes);
        durationTimes.clear();
    }

    public TestReport getTestReport() {
        return testReport;
    }

    public String getReportOutput() {
        return reportOutput;
    }
}
