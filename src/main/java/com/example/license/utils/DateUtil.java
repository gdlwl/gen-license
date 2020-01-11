package com.example.license.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
    public enum DateType{
        /**
         * 年月日时分秒
         */
        YMDHMS("yyyy-MM-dd HH:mm:ss"),
        /**
         * 年月日时分
         */
        YMDHM("yyyy-MM-dd HH:mm"),
        /**
         * 年月日
         */
        YMD("yyyy-MM-dd"),
        /**
         * 年月日中文
         */
        YMD_CN("yyyy年MM月dd日"),
        /**
         * 年月日时分秒中文
         */
        YMDHMS_CN("yyyy年MM月dd日 HH时mm分ss秒");

        private String format;

        DateType(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }
    }

    public DateUtil() {
    }
    private static Map<String, ThreadLocal<DateFormat>> threadLocalMap = new HashMap<>();



    static {
        DateType[] values = DateType.values();
        for (DateType value : values) {
            String format = value.getFormat();
            threadLocalMap.put(format, ThreadLocal.withInitial(() -> new SimpleDateFormat(format)));
        }
    }

    /**
     * 添加自定义日期格式，最好在系统初始化的时候加进去
     * 已存在日期格式，查看{@link DateType}
     * @param format 日期格式，例如yyyy-MM-dd
     */
    public static void putThreadLocalMap(String format) {
        if (threadLocalMap.get(format) == null) {
            threadLocalMap.put(format, ThreadLocal.withInitial(() -> new SimpleDateFormat(format)));
        }
    }

    public static void main(String[] args) {
        String format = "yyyy/MM/dd";
        putThreadLocalMap(format);
        System.out.println(DateUtil.format(new Date(), DateType.YMDHMS));
        System.out.println(DateUtil.format(new Date(), DateType.YMD));
        System.out.println(DateUtil.format(new Date(), DateType.YMD_CN));
        System.out.println(DateUtil.format(new Date(), format));
        String dateStr = "2020-12-03 00:00:00";
        try {
            DateUtil.parse(dateStr,DateType.YMDHMS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //日期转字符串
    public static String format(Date date,DateType dateType) {
        String format = dateType.getFormat();
        return format(date, format);
    }

    public static String format(Date date,String format) {
        ThreadLocal<DateFormat> threadLocal = threadLocalMap.get(format);
        if (threadLocal == null) {
            return null;
        }
        return threadLocal.get().format(date);
    }

    //字符串转日期
    public static Date parse(String str,DateType dateType) throws ParseException {
        ThreadLocal<DateFormat> threadLocal = threadLocalMap.get(dateType.getFormat());
        if (threadLocal == null) {
            return null;
        }
        return threadLocal.get().parse(str);
    }
}