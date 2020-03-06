package com.shiro.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DATE: 2019/9/19 12:37
 * USER: create by 申水根
 */
public class StringUtil {
    public static boolean isNull(Object str) {

        boolean bool = false;

        if (str == null || "".equals(str))
            bool = true;

        return bool;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str1 = sdf1.format(date);
        return str1;
    }

    public static String writeAsString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            return "转换json错误，检查数据";
        }
    }

    public static Date getNow() {
        Calendar c1 = Calendar.getInstance();

// 获得年份

        int year = c1.get(Calendar.YEAR);

// 获得月份

        int month = c1.get(Calendar.MONTH) + 1;//（MONTH+1）

// 获得日期

        int date = c1.get(Calendar.DATE);

// 获得小时

        int hour = c1.get(Calendar.HOUR_OF_DAY);

// 获得分钟

        int minute = c1.get(Calendar.MINUTE);

// 获得秒

        int second = c1.get(Calendar.SECOND);

// 获得星期几（注意（这个与Date类是不同的）：1代表星期日、2代表星期1、3代表星期二，以此类推）

        int day = c1.get(Calendar.DAY_OF_WEEK);
        return new Date(year-1900,month-1,date,hour,minute,second);
    }


    public static String dateTimeToString(Date date){
        StringBuilder sb = new StringBuilder();
        Integer year = date.getYear()+1900;
        sb.append(year+"-");
        Integer month = date.getMonth()+1;
        Integer _date = date.getDate();
        Integer hours = date.getHours();
        Integer minutes = date.getMinutes();
        Integer seconds = date.getSeconds();
        System.out.println(year+"-"+month+"-"+_date+" "+hours+":"+minutes+":"+seconds);
        if (!month.equals(0))
        {
            if (month>9)
                sb.append(month+"-");
            else
                sb.append("0"+month+"-");
        }
        if (!_date.equals(0)){
            if (_date>9)
                sb.append(_date);
            else
                sb.append("0"+_date);
        }
        if (!hours.equals(0)) {
            if (hours>9)
                sb.append(" "+hours);
            else
                sb.append(" "+"0"+hours);
        }

        if (!minutes.equals(0)) {
            if (minutes>9)
                sb.append(":"+minutes);
            else
                sb.append(":"+"0"+minutes);
        }

        if (!seconds.equals(0)) {
            if (seconds>9)
                sb.append(":"+seconds);
            else
                sb.append(":"+"0"+seconds);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws  Exception{
//       System.out.println(StringUtil.dateToString(StringUtil.getNow()));
//       System.out.println(StringUtil.dateTimeToString(StringUtil.getNow()));
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM");
        Date date = dateformat.parse("2016-6");
//
//        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        System.out.println(dateformat.format(date));

        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateformat.parse("2016-6-1 01:01:01");
        System.out.println(StringUtil.dateTimeToString(date));
    }
}
