package core.utilities;

import java.text.DecimalFormat;

public class NumberHandler {

    public static DecimalFormat getNumberFormat(int fractionNumber) {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(fractionNumber);
        return df;
    }
    public static DecimalFormat getNumberFormat(String pattern ,int fractionNumber) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(pattern);
        df.setMinimumFractionDigits(fractionNumber);
        return df;
    }
}
