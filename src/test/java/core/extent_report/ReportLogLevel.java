package core.extent_report;

import org.apache.commons.lang3.StringUtils;

public class ReportLogLevel {
    private static final String LVL_STEP = "----";
    public static final String LOG_LVL_1 = StringUtils.repeat(LVL_STEP, 0);
    public static final String LOG_LVL_2 = StringUtils.repeat(LVL_STEP, 1);
    public static final String LOG_LVL_3 = StringUtils.repeat(LVL_STEP, 2);
    public static final String LOG_LVL_4 = StringUtils.repeat(LVL_STEP, 3);
}
