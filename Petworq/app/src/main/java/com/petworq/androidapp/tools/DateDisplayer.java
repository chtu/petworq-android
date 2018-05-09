package com.petworq.androidapp.tools;

/**
 * Created by charlietuttle on 5/8/18.
 */

public class DateDisplayer {

    private int mYear;
    private int mMonth;
    private int mDate;
    private int mHour;
    private int mMinute;

    public static final String[] sMonths = {
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"
    };

    public static final String[] sDays = {
            "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
    };

    public DateDisplayer(String year, String month, String date, String hour, String minute) {
        mYear = Integer.parseInt(year);
        mMonth = Integer.parseInt(month);
        mDate = Integer.parseInt(date);
        mHour = Integer.parseInt(hour);
        mMinute = Integer.parseInt(minute);
    }

    public String getMonth() {
        return sMonths[mMonth];
    }

    public String get12HourTime() {
        String amPm;
        int hour;

        if (mHour >= 0 && mHour < 12) {
            amPm = "am";
            if (mHour == 0)
                hour = 12;
            else
                hour = mHour;
        } else {
            amPm = "pm";
            if (mHour != 12)
                hour = mHour - 12;
            else
                hour = mHour;
        }

        String returnStr = hour + ":" + getMinuteStr(mMinute) + " " + amPm;
        return returnStr;
    }

    private String getMinuteStr(int minute) {
        if (minute < 10)
            return "0" + minute;
        else
            return minute + "";
    }

    public String getDateStr() {
        return  getMonth() + " " + mDate + ", " + mYear;
    }

    public String getFullDateStr() {
        String returnStr = "";
        returnStr += get12HourTime() + ", " + getDateStr();

        return returnStr;
    }
}
