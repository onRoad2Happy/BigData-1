package com.wyd.BigData.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    /**
     * 获取指定时间当天的凌晨
     * 
     * @param date
     *            给定时间当天的凌晨
     * @return
     */
    public static Date getMorning(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    /**
     * 比较两个日期并返回两个日期相差多少天(d1－d2)
     * @param d1    Date
     * @param d2    Date
     * @return int
     */
    public static int compareDateOnDay(Date d1, Date d2) {
        return Math.abs((int)Math.ceil(Double.valueOf((getMorning(d1).getTime() - getMorning(d2).getTime()))/86400000));
    }
    
}
