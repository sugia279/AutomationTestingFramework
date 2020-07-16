package core.testdata_manager;

import core.utilities.DateTimeHandler;
import core.utilities.NumberHandler;

import java.util.LinkedHashMap;
import java.util.Random;

public class TestVariableManager {
    private LinkedHashMap<String, Object> systemVars;
    private LinkedHashMap<String, Object> configVars;
    private LinkedHashMap<String, Object> testVars;

    public TestVariableManager(){
        systemVars = new LinkedHashMap<>();
        configVars = new LinkedHashMap<>();
        setTestVars(new LinkedHashMap<>());
    }

    public LinkedHashMap<String, Object> getSystemVars() {
        return systemVars;
    }

    public void setSystemVars(LinkedHashMap<String, Object> systemVars) {
        this.systemVars = systemVars;
    }

    public LinkedHashMap<String, Object> getConfigVars() {
        return configVars;
    }

    public void setConfigVars(LinkedHashMap<String, Object> configVars) {
        this.configVars = configVars;
    }

    public void clear(){
        systemVars.clear();
        configVars.clear();
    }

    public void loadSystemVariables() {
        systemVars.put("CURTIME_HH:mm", DateTimeHandler.currentDayPlus("HH:mm", 0));
        systemVars.put("CURTIME_HHmm", DateTimeHandler.currentDayPlus("HHmm", 0));
        systemVars.put("CURTIME_hhmma", DateTimeHandler.currentDayPlus("hhmma", 0));
        systemVars.put("CURTIME_HHmmss", DateTimeHandler.currentDayPlus("HHmmss", 0));
        systemVars.put("CURTIME-1_HHmm", DateTimeHandler.currentDayMinutePlus("HHmm", -1));
        systemVars.put("CURTIME-1_hhmma", DateTimeHandler.currentDayMinutePlus("hhmma", -1));
        systemVars.put("CURTIME-60_hhmma", DateTimeHandler.currentDayMinutePlus("hhmma", -60));
        systemVars.put("NOW+1_HH:mm", DateTimeHandler.currentDayMinutePlus("HH:mm", 1));
        systemVars.put("CURTIME+1_HHmm", DateTimeHandler.currentDayMinutePlus("HHmm", 1));
        systemVars.put("CURTIME+1_hhmma", DateTimeHandler.currentDayMinutePlus("hhmma", 1));
        systemVars.put("TODAY", DateTimeHandler.currentDayPlus("yyyy-MM-dd_HH-mm-ss", 0));
        systemVars.put("TODAY_dd-MM-yyyy_HH-mm-ss", DateTimeHandler.currentDayPlus("yyyy-MM-dd_HH-mm-ss", 0));
        systemVars.put("TODAY_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", 0));
        systemVars.put("TODAY_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", 0));
        systemVars.put("TODAY_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", 0));
        systemVars.put("TODAY_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", 0));
        systemVars.put("YESTERDAY_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", -1));
        systemVars.put("YESTERDAY_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", -1));
        systemVars.put("YESTERDAY_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", -1));
        systemVars.put("TOMORROW_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +1));
        systemVars.put("TOMORROW_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", +1));
        systemVars.put("TOMORROW_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", +1));
        systemVars.put("TOMORROW_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", +1));
        systemVars.put("NEXTMONTH_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +30));
        systemVars.put("NEXTMONTH_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", +30));
        systemVars.put("NEXTMONTH_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", +30));
        systemVars.put("NEXTMONTH_yyyyMMdd", DateTimeHandler.currentDayPlus("yyyyMMdd", +30));
        systemVars.put("NEXTMONTH+2_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", +32));
        systemVars.put("NEXTMONTH+2_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", +32));
        systemVars.put("NEXTMONTH+2_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", +32));
        systemVars.put("PREVIOUSMONTH_yyyy-MM-dd", DateTimeHandler.currentDayPlus("yyyy-MM-dd", -30));
        systemVars.put("PREVIOUSMONTH_dd-MM-yyyy", DateTimeHandler.currentDayPlus("dd-MM-yyyy", -30));
        systemVars.put("PREVIOUSMONTH_dd/MM/yyyy", DateTimeHandler.currentDayPlus("dd/MM/yyyy", -30));
        systemVars.put("RANDOM_Integer_7", (new Random()).nextInt(9999999) + 1);
        systemVars.put("RANDOM_IntegerString_7", "" + ((new Random()).nextInt(9999999) + 1));
        systemVars.put("RANDOM_DoubleString_9_2", "" + NumberHandler.getNumberFormat("##0.00", 2).format(NumberHandler.getRandomDoubleBetweenRange(0, 999999999)));
        systemVars.put("RANDOM_DoubleString_5_6", "" + NumberHandler.getNumberFormat("##0.00", 6).format(NumberHandler.getRandomDoubleBetweenRange(0, 99999)));
    }

    public LinkedHashMap<String, Object> getTestVars() {
        return testVars;
    }

    public void setTestVars(LinkedHashMap<String, Object> testVars) {
        this.testVars = testVars;
    }
}
