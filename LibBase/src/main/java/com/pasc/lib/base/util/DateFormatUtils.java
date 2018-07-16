package com.pasc.lib.base.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by luomengna871 on 17/3/20.
 */

public class DateFormatUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_AND_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static long getTime(String dateStr, String format) {
        if (!TextUtils.isEmpty(dateStr)) {
            DateFormat df = new SimpleDateFormat(format);
            try {
                Date date = df.parse(dateStr);
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 格式化日期时间
     * 最后返回类似:8月21日 12:13   或  2013年8月21日 12:23
     *
     * @param dateStr 毫秒级整数
     */
    public static String timeFormat(long dateStr) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(DATE_AND_TIME_FORMAT);
        String allTime = sDateFormat.format(new Date(dateStr + 0));
        return timeFormat(allTime);
    }

    /**
     * 格式化日期
     * 最后返回类似:8月21日   或  2013年8月21日
     *
     * @param mills 毫秒级整数
     */
    public static String dateFormat(Long mills) {
        if (mills == null) {
            return "";
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat(DATE_FORMAT);
        String allTime = sDateFormat.format(new Date(mills + 0));
        return allTime;
    }

    /**
     * *
     * 格式化日期
     * 最后返回类似:8月21日   或  2013年8月21日
     *
     * @param mills
     * @param format 时间格式
     * @return
     */
    public static String dateFormat(long mills, String format) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        String allTime = sDateFormat.format(new Date(mills + 0));
        return allTime;
    }

    /**
     * 格式化日期时间
     */
    public static String timeFormat(String timeStr) {
        if (timeStr == null) {
            return "";
        } else {
            String dateStr = timeStr.split(" ")[0];
            String time = timeStr.split(" ")[1];
            time = time.substring(0, 5);
            String monthDay = dateStr.substring(dateStr.indexOf("-") + 1, dateStr.length());
            Calendar calendar = Calendar.getInstance();
            Date todayDate = calendar.getTime();
            Date date = strToDate(dateStr, DATE_FORMAT);
            //            calendar.setTime(date);
            if (date.getYear() == todayDate.getYear()) {
                if (date.getMonth() == todayDate.getMonth()
                        && date.getDate() == todayDate.getDate()) {
                    return time;
                } else if (date.getMonth() == todayDate.getMonth()
                        && date.getDate() + 1 == todayDate.getDate()) {
                    return "昨天" + time;
                } else {
                    return monthDay + " " + time;
                }
            } else {
                return timeStr;
            }
        }
    }

    /**
     * 格式化日期时间
     * 返回日期，不需要时间
     */
    public static String DateFormat(String timeStr) {
        if (timeStr == null) {
            return "";
        } else {
            String dateStr = timeStr.split(" ")[0];
            String monthDay = dateStr.substring(dateStr.indexOf("-") + 1, dateStr.length());
            Calendar calendar = Calendar.getInstance();
            Date todayDate = calendar.getTime();
            Date date = strToDate(dateStr, DATE_FORMAT);
            //            calendar.setTime(date);
            if (date.getYear() == todayDate.getYear()) {
                if (date.getMonth() == todayDate.getMonth()
                        && date.getDate() == todayDate.getDate()) {
                    return "今天";
                } else if (date.getMonth() == todayDate.getMonth()
                        && date.getDate() + 1 == todayDate.getDate()) {
                    return "昨天";
                } else {
                    return monthDay;
                }
            } else {
                return timeStr;
            }
        }
    }

    /**
     * 日期字符串转换为Date
     */
    public static Date strToDate(String dateStr, String format) {
        Date date = null;

        if (!TextUtils.isEmpty(dateStr)) {
            DateFormat df = new SimpleDateFormat(format);
            try {
                date = df.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 获取上个月的当天
     */
    public static Long getTodayOfLastMonth() {
        long str;
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.DATE, Calendar.DATE); //set the date to be 1
        lastDate.add(Calendar.MONTH, -1);//reduce a month to be last month
        //		lastDate.add(Calendar.DATE,-1);//reduce one day to be the first day of last month

        str = lastDate.getTime().getTime();
        return str;
    }

    /**
     * 判断是否为今天
     *
     * @param mills
     * @return
     */
    public static boolean isToday(Long mills) {
        if (mills == null || mills == 0 || mills == -1) {
            return false;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Log.e("asd", fmt.format(new Date(mills)) + "@@@" + fmt.format(new Date()));
        return fmt.format(new Date(mills)).equals(fmt.format(new Date()));
    }

    public static String dateFormat(String timeStamp) {
        long l;
        try {
            l = Long.parseLong(timeStamp);
            return dateFormat(l);
        } catch (NumberFormatException e) {
            return timeStamp;
        }
    }
}
