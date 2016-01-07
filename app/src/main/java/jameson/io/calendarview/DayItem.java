package jameson.io.calendarview;

/**
 * Created by jameson on 1/6/16.
 */
public class DayItem {
    private int year;
    private int month;
    private int dayOfMonth;
    private boolean isSelected;
    private boolean isToday;
    // 日期是否在本月内
    private boolean isInCurMonth;

    public DayItem() {
    }

    public DayItem(int day) {
        this.dayOfMonth = day;
    }

    public void setDate(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setIsToday(boolean isToday) {
        this.isToday = isToday;
    }

    public boolean isInCurMonth() {
        return isInCurMonth;
    }

    public void setIsInCurMonth(boolean isInCurMonth) {
        this.isInCurMonth = isInCurMonth;
    }
}
