package com.delivery;


/**
 * Created by Ticknick on 30/05/2017.
 */

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Finderlo on 2016/8/16.
 */
public class Test {

    private static final String BEFORE_YEAR = "年前";
    private static final String BEFORE_MONTH = "月前";
    private static final String BEFORE_DAY = "天前";
    private static final String BEFORE_HOUR = "小时前";
    private static final String BEFORE_MINUTE = "分钟前";
    private static final String BEFORE_JUST_NOW = "刚刚";


    private static Calendar sCalendar;



    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mMinute;
    private static int mCurrentYear;

    private static int mCurrentMonth;
    private static int mCurrentDay;
    private static int mCurrentHour;
    private static int mCurrentMinute;

    public static String parse4Timeline(String time) {
        //解析创建时间
//    String str = "Mon Aug 01 17:04:17 +0800 2016";

        sCalendar.setTime(parse(time));
        mYear = sCalendar.get(Calendar.YEAR);
        mMonth = sCalendar.get(Calendar.MONTH);
        mDay = sCalendar.get(Calendar.DAY_OF_MONTH);
        mHour = sCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = sCalendar.get(Calendar.MINUTE);


        //解析当前时间,
        refreshTime();
        mCurrentYear = sCalendar.get(Calendar.YEAR);
        mCurrentMonth = sCalendar.get(Calendar.MONTH);
        mCurrentDay = sCalendar.get(Calendar.DAY_OF_MONTH);
        mCurrentHour = sCalendar.get(Calendar.HOUR_OF_DAY);
        mCurrentMinute = sCalendar.get(Calendar.MINUTE);

        //比较两个时间
        if (mYear < mCurrentYear) {
            return mCurrentYear - mYear + BEFORE_YEAR;
        } else if (mMonth < mCurrentMonth) {
            return mCurrentMonth - mMonth + BEFORE_MONTH;
        } else if (mDay < mCurrentDay) {
            return mCurrentDay - mDay + BEFORE_DAY;
        } else if (mHour < mCurrentHour) {
            return mCurrentHour - mHour + BEFORE_HOUR;
        } else if (mMinute < mCurrentMinute) {
            if (mCurrentMinute - mMinute < 5) {
                return BEFORE_JUST_NOW;
            } else
                return mCurrentMinute - mMinute + BEFORE_MINUTE;
        }
        return "16年前";
    }

    public static Date parse(String date) {
        /**The time format like "Mon Aug 01 17:04:17 +0800 2016" */
        return new Date(Date.parse(date));
    }

    public static void refreshTime() {
        sCalendar.setTime(new Date());
    }


    public static void main(String[] args) {
        test();
    }

    //    @Test
    public static void test() {
//        Date date = new Date();
//        date.setYear(2017);
//        date.setMonth(4);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        System.out.println(parse4Timeline(calendar.getTime().toString()));
    }
}
