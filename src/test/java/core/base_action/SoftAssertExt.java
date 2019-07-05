package core.base_action;

import core.extent_report.TestReportManager;
import core.extent_report.ReportLogLevel;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

public class SoftAssertExt extends SoftAssert {
    private TakesScreenshot driver;
    public SoftAssertExt(TakesScreenshot dr){
        driver = dr;
    }

    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        System.err.println(assertCommand.getMessage() + " <PASSED> ");
        TestReportManager.getInstance().setSubStepPass(assertCommand.getMessage() + " <PASSED> ", ReportLogLevel.LOG_LVL_4);
        super.onAssertSuccess(assertCommand);
    }

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        String suffix =
                String.format(
                        "Expected [%s] but found [%s]",
                        assertCommand.getExpected().toString(), assertCommand.getActual().toString());
        System.err.println(assertCommand.getMessage() + " <FAILED>. " + suffix);
        String scrFile = driver.getScreenshotAs(OutputType.BASE64);
        TestReportManager.getInstance().setSubStepFail(assertCommand.getMessage() + ".<br>[FAILED]: " + suffix, scrFile, ReportLogLevel.LOG_LVL_4);
        super.onAssertFailure(assertCommand, ex);
    }
}
