package core.utilities;

import java.util.TimeZone;

public class TimeZonesHandler {
    private static InheritableThreadLocal _thdTZone = new InheritableThreadLocal();


    public static TimeZone getCurrent() {
        final TimeZone l = (TimeZone) _thdTZone.get();
        return l != null ? l : TimeZone.getDefault();
    }

    public static TimeZone setThreadLocal(TimeZone timezone) {
        final TimeZone old = (TimeZone) _thdTZone.get();
        _thdTZone.set(timezone);
        return old;
    }

    public static TimeZone getThreadLocal() {
        return (TimeZone) _thdTZone.get();
    }

    public static TimeZone getTimeZone(int ofsmins) {
        final StringBuffer sb = new StringBuffer(8).append("GMT");
        if (ofsmins >= 0) {
            sb.append('+');
        } else {
            sb.append('-');
            ofsmins = -ofsmins;
        }
        final int hr = ofsmins / 60, min = ofsmins % 60;
        if (min == 0) {
            sb.append(hr);
        } else {
            if (hr < 10)
                sb.append('0');
            sb.append(hr).append(':');
            if (min < 10)
                sb.append('0');
            sb.append(min);
        }
        return TimeZone.getTimeZone(sb.toString());
    }
}
