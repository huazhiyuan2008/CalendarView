package jameson.io.calendarview.util;

import java.util.Calendar;

/**
 * Created by jameson on 1/6/16.
 */
public class CalendarUtil {

    // 指定某年中的某月的第一天是星期几，星期日-星期一分别为0-6
    public static int getWeekdayOfMonth(Calendar c) {
        Calendar cal = (Calendar) c.clone();
        cal.set(Calendar.DATE, 1);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static String getWeekText(int dayPosition) {
        String[] weeks = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sta"};

        if (dayPosition < 0) {
            dayPosition = 0;
        }

        if (dayPosition >= weeks.length) {
            dayPosition = weeks.length - 1;
        }

        return weeks[dayPosition];
    }

    public static Calendar getPreMonthCalendar(Calendar c) {
        return getMonthsByOffset(c, -1);
    }

    public static Calendar getNextMonthCalendar(Calendar c) {
        return getMonthsByOffset(c, 1);
    }

    public static Calendar getMonthsByOffset(Calendar c, int offset) {
        Calendar nextC = (Calendar) c.clone();
        nextC.add(Calendar.MONTH, offset);
        return nextC;
    }
}
