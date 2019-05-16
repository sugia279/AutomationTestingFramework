package core.utilities;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeHandler {

    public static Date round(Date date, int precision) {
        date.setTime(round(date.getTime(), precision));
        return date;
    }

    public static long round(long time, int precision) {
        return time - (time % precision);
    }

    public static boolean isRounded(Date date, int precision) {
        return (date.getTime() % precision) == 0;
    }

    public static Date now(int precision) {
        return new Date(round(System.currentTimeMillis(), precision));
    }

    public static Date now() {
        return new Date();
    }

    public static Date today() {
        return beginOfDate(new Date(), null);
    }

    public static Date previousDate(Date when) {
        long time = when.getTime() - 24 * 60 * 60 * 1000;
        return new Date(time);
    }

    public static Date beginOfMonth() {
        return beginOfMonth(new Date(), null);
    }

    public static Date beginOfMonth(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();
        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime()); // don't call cal.setTime(Date) which
        // will reset the TimeZone.

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        cal.clear();
        cal.set(year, month, 1);
        return cal.getTime();
    }

    public static Date endOfMonth() {
        return endOfMonth(new Date(), null);
    }

    public static Date endOfMonth(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();
        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.clear();
        cal.set(year, month, monthDays + 1);
        cal.setTimeInMillis(cal.getTimeInMillis() - 1);
        return cal.getTime();
    }

    public static boolean isEndOfMonth(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();
        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day == maxDay;
    }

    public static boolean isBeginOfMonth(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();
        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        final int day = cal.get(Calendar.DAY_OF_MONTH);
        return day == 1;
    }

    public static Date beginOfDate(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();

        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        cal.clear();
        cal.set(year, month, day);

        return cal.getTime();
    }

    public static Date endOfDate(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();

        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        cal.clear();
        cal.set(year, month, day + 1);
        cal.setTimeInMillis(cal.getTimeInMillis() - 1);

        return cal.getTime();
    }

    public static Date beginOfYear() {
        return beginOfYear(new Date(), null);
    }

    public static Date beginOfYear(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();
        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        final int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(year, Calendar.JANUARY, 1);
        return cal.getTime();
    }

    public static Date endOfYear() {
        return endOfYear(new Date(), null);
    }

    public static Date endOfYear(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();
        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        final int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(year + 1, Calendar.JANUARY, 1);
        cal.setTimeInMillis(cal.getTimeInMillis() - 1);
        return cal.getTime();
    }

    public static int yearOfDate(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();
        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        return cal.get(Calendar.YEAR);
    }

    public static int localizedYearOfDate(Date when, Locale locale, TimeZone tz) {
        final int year = yearOfDate(when, tz);
        if (locale.equals(Locale.TAIWAN))
            return year - 1911;
        return year;
    }

    public static int monthOfDate(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();
        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        return cal.get(Calendar.MONTH);
    }

    public static int dayMonthOfDate(Date when, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();
        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Date add(Date when, TimeZone tz, int field, int amount) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();

        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(when.getTime());

        cal.add(field, amount);
        return cal.getTime();
    }

    public static long subtract(Date date2, TimeZone tz, int field, Date date1) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();

        boolean negative = false;
        if (date1.after(date2)) {
            negative = true;
            final Date d = date1;
            date1 = date2;
            date2 = d;
        }

        final Calendar cal1 = Calendar.getInstance(tz);
        cal1.setTimeInMillis(date1.getTime());// don't call cal.setTime(Date) which
        // will reset the TimeZone.

        final Calendar cal2 = Calendar.getInstance(tz);
        cal2.setTimeInMillis(date2.getTime());// don't call cal.setTime(Date) which
        // will reset the TimeZone.

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);

        switch (field) {
            case Calendar.YEAR: {
                return negative ? (year1 - year2) : (year2 - year1);
            }
            case Calendar.MONTH: {
                int month1 = cal1.get(Calendar.MONTH);
                int month2 = cal2.get(Calendar.MONTH);
                int months = 12 * (year2 - year1) + month2 - month1;
                return negative ? -months : months;
            }
            case Calendar.HOUR: {
                long time1 = date1.getTime();
                long time2 = date2.getTime();
                long min1 = (time1 < 0 ? (time1 - (1000 * 60 * 60 - 1)) : time1) / (1000 * 60 * 60);
                long min2 = (time2 < 0 ? (time2 - (1000 * 60 * 60 - 1)) : time2) / (1000 * 60 * 60);
                return negative ? (min1 - min2) : (min2 - min1);
            }
            case Calendar.MINUTE: {
                long time1 = date1.getTime();
                long time2 = date2.getTime();
                long min1 = (time1 < 0 ? (time1 - (1000 * 60 - 1)) : time1) / (1000 * 60);
                long min2 = (time2 < 0 ? (time2 - (1000 * 60 - 1)) : time2) / (1000 * 60);
                return negative ? (min1 - min2) : (min2 - min1);
            }
            case Calendar.SECOND: {
                long time1 = date1.getTime();
                long time2 = date2.getTime();
                long sec1 = (time1 < 0 ? (time1 - (1000 - 1)) : time1) / 1000;
                long sec2 = (time2 < 0 ? (time2 - (1000 - 1)) : time2) / 1000;

                return negative ? (sec1 - sec2) : (sec2 - sec1);
            }
            case Calendar.MILLISECOND: {
                return negative ? (date1.getTime() - date2.getTime()) : (date2.getTime() - date1.getTime());
            }
            case Calendar.DATE:
            default: /* default, like -1 */ {
                int day1 = cal1.get(Calendar.DAY_OF_YEAR);
                int day2 = cal2.get(Calendar.DAY_OF_YEAR);

                int maxDay1 = year1 == year2 ? 0 : cal1.getActualMaximum(Calendar.DAY_OF_YEAR);
                int days = maxDay1 - day1 + day2;

                final Calendar cal = Calendar.getInstance(tz);
                for (int year = year1 + 1; year < year2; year++) {
                    cal.set(Calendar.YEAR, year);
                    days += cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                }
                return negative ? -days : days;
            }
        }

    }

    public static Date merge(Date datePart, Date timePart, TimeZone tz) {
        if (tz == null)
            tz = TimeZonesHandler.getCurrent();

        final Calendar dateCal = Calendar.getInstance(tz);
        dateCal.setTimeInMillis(datePart.getTime());// don't call cal.setTime(Date)
        // which will reset the
        // TimeZone.

        final Calendar timeCal = Calendar.getInstance(tz);
        timeCal.setTimeInMillis(timePart.getTime());// don't call cal.setTime(Date)
        // which will reset the
        // TimeZone.

        final int hour = timeCal.get(Calendar.HOUR);
        final int minute = timeCal.get(Calendar.MINUTE);
        final int second = timeCal.get(Calendar.SECOND);
        final int msillisecond = timeCal.get(Calendar.MILLISECOND);

        dateCal.set(Calendar.HOUR, hour);
        dateCal.set(Calendar.MINUTE, minute);
        dateCal.set(Calendar.SECOND, second);
        dateCal.set(Calendar.MILLISECOND, msillisecond);

        return dateCal.getTime();
    }

    public static String currentDateAsString() {
        return currentDateAsString("yyyyMMddHHmmss");
    }

    public static String currentDateAsString(String pattern) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(pattern);
        return formatter.format(new Date());
    }

    public static String currentDayPlus(String format, int additional) {
        Date res = new DateTime(new Date()).plusDays(additional).toDate();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(res);
    }

    public static String currentDayMinutePlus(String format, int additional) {
        Date res = DateUtils.addMinutes(new Date(), additional);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(res);
    }

    public static String currentDayPlus() {
        return currentDayPlus("yyyy-MM-dd", 0);
    }

    public static String currentDayPlus(int additional) {
        return currentDayPlus("yyyy-MM-dd", additional);
    }

    public static String formatDate(String inputDate, String inputFormat, String outputFormat) {
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
        try {
            return outputFormatter.format(inputFormatter.parse(inputDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatDate(String inputDate) {
        return formatDate(inputDate, "yyyy-MM-dd", "dd/MM/yyyy");
    }

    public static int countDaysBetween2Date(DateTime startDate, DateTime endDate) {
        return  Days.daysBetween(startDate, endDate).getDays();
    }

}
