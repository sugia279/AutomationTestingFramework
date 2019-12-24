package core.base_action;

import core.extent_report.ReportLogLevel;
import core.extent_report.TestReportManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

public class SoftAssertExt extends SoftAssert {
    private TakesScreenshot driver;
    private boolean isTakeScreenshot;

    public SoftAssertExt(){

    }

    public SoftAssertExt(TakesScreenshot dr){
        driver = dr;
        isTakeScreenshot = false;
    }

    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        System.err.println(assertCommand.getMessage() + " <PASSED> ");
        if(isTakeScreenshot){
            String scrFile = driver.getScreenshotAs(OutputType.BASE64);
            TestReportManager.getInstance().setSubStepPass(assertCommand.getMessage() + " <PASSED> ", scrFile, ReportLogLevel.LOG_LVL_4);
            isTakeScreenshot = false;
        }
        else {
            TestReportManager.getInstance().setSubStepPass(assertCommand.getMessage() + " <PASSED> ", ReportLogLevel.LOG_LVL_4);
        }
        super.onAssertSuccess(assertCommand);
    }

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        String suffix =
                String.format(
                        "Expected [%s] but found [%s]",
                        assertCommand.getExpected().toString(), assertCommand.getActual().toString());
        System.err.println(assertCommand.getMessage() + " <FAILED>. " + suffix);
        if(driver!=null) {
            String scrFile = driver.getScreenshotAs(OutputType.BASE64);
            TestReportManager.getInstance().setSubStepFail(assertCommand.getMessage() + "<br>[FAILED]: " + suffix, scrFile, ReportLogLevel.LOG_LVL_4);
        }
        else {
            TestReportManager.getInstance().setSubStepFail(assertCommand.getMessage() + "<br>[FAILED]: " + suffix, ReportLogLevel.LOG_LVL_4);
        }
        super.onAssertFailure(assertCommand, ex);
    }

    public SoftAssertExt takeScreenshot() {
        isTakeScreenshot = true;
        return this;
    }
}
