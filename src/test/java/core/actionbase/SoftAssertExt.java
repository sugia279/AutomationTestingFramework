package core.actionbase;

import core.extentreport.TestReportManager;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;
import core.extentreport.ReportLogLevel;

public class SoftAssertExt extends SoftAssert {

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
        TestReportManager.getInstance().setSubStepFail(assertCommand.getMessage() + " <FAILED>. " + suffix, ReportLogLevel.LOG_LVL_4);
        super.onAssertFailure(assertCommand,ex);
    }

}
