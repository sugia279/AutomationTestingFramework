package core.extentreport;

import core.testdatamanager.TestCase;
import core.utilities.DateTimeHandler;
import core.utilities.FileHandler;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TestReportManager {
    protected static String REPORT_CONFIG = "src/test/java/core/extentreport/extReportConfig.xml";
    protected static String REPORT_OUTPUT = System.getProperty("user.dir") + "\\reports\\TestReport_date.html";
    public static TestReportManager instance;
    public static TestReportManager getInstance(){
        if(instance == null) {
            instance = new TestReportManager();
        }
        return instance;
    }
    private TestReport testReport = null;
    private int actionOrder = 0;

    private LinkedHashMap<String,String> durationTimes = new LinkedHashMap<>();

    public TestReportManager()
    {
        testReport = new TestReport();
    }

    public void initializeReport(){
        getTestReport().initReport(REPORT_CONFIG,REPORT_OUTPUT = REPORT_OUTPUT.replaceFirst("date", DateTimeHandler.currentDateAsString("yyyy.MM.dd.HH.mm.ss")),
                new HashMap<String, String>() {
                    {
                        put("User Name", System.getProperty("user.name"));
                        put("OS version", System.getProperty("os.name"));
                        put("Java Version", System.getProperty("java.version"));
                        put("Host Name", System.getenv("COMPUTERNAME"));
                        put("Project", "Fossil Testing");
                    }
                });
    }

    public void setTestInfo(TestCase tc, String... suiteNames) {
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
        testReport.addTestcaseToTEST(tName, testCaseDesciption, "<b>" + suiteNames[0] + "</b>"
                , "<b>&emsp;" + suiteNames[1] + "</b>");

        actionOrder = 0;

    }

    public void setStepInfo(String log){
        setStepInfo(log, false);
    }


    public void setStepInfo(String log, boolean resetOrder){
        String action = "<b>" + log + "</b>";
            actionOrder = resetOrder ? 1 : actionOrder + 1;
            action = "<b><font color=\"blue\"><u>Step " + actionOrder + "</u></font> - " + log + "</b>";

        testReport.testLog(action);
    }

    public void setSubStepInfo(String log, String level){
        testReport.testLog(level + log);
    }

    public void setSubStepPass(String log, String level){
        testReport.testPass(level + log,"");
    }

    public void setSubStepFail(String log, String level){
        testReport.testFail(level + log,"","");
    }

    public void setStepPass(String log)    {
        testReport.testPass(log, "");
    }

    public void setStepSkip(String log, String msg)    {
        testReport.testLog("<br>" + log);
        testReport.testSkip("<br>" + msg, "");
    }

    public void setStepFail(String log){
        testReport.testFail(log, "","");
    }

    public void setStepPass(String log, String colorMarkupLabel)    {
        testReport.testPass(log, colorMarkupLabel);
    }

    public void setStepSkip(String log, String msg, String colorMarkupLabel)    {
        testReport.testLog("<br>" + log);
        testReport.testSkip("<br>" + msg, colorMarkupLabel);
    }

    public void setStepFail(String log, String colorMarkupLabel){
        testReport.testFail(log, "",colorMarkupLabel);
    }


    public void saveDurationTime(String testName)
    {
        String modifiedKey =  "<span class='test-name'>" + testName + "</span>\r\n" +
                "\t\t\t\t\t\t<span class='test-time'>" + new SimpleDateFormat("MMM d, yyyy hh:mm:ss a").format(testReport.getTestCaseStartTime()) + "</span>";
        String modifiedValue =  modifiedKey + "\t<span class=\"label time-taken grey lighten-1 white-text\">" + testReport.getTestCaseDurationTime() + "</span>";
        durationTimes.put(modifiedKey, modifiedValue);
    }

    public void updateRunDurationForTestInLeft()    {
        FileHandler.modifyFileByHashMap(REPORT_OUTPUT,durationTimes);
        durationTimes.clear();
    }

    public TestReport getTestReport() {
        return testReport;
    }
}
