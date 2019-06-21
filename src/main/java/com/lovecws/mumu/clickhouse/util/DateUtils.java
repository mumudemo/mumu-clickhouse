/**
 * Copyright(c) Surfilter Technology Co.,Ltd
 *
 * @Project DNSLogParser
 * @Package com.surfilter.dns.util
 * @version V1.0
 */
package com.lovecws.mumu.clickhouse.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author hapuer
 * Date Handler
 */
public class DateUtils {


    public static final String SPECAL_DATE_FORMAT = "yyyyMMddHHmmss";

    public static final String SPECAL_LONG_DATE_FORMAT = "yyyyMMddHHmmssSSS";

    public static final String SPECAL_TIME_FORMAT = "yyyyMMddHHmm";

    public static final String SPECAL_HOUR_FORMAT = "yyyyMMddHH";

    public static final String SPECAL_DAY_FORMAT = "yyyyMMdd";

    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";

    public static final String DAY_HOUR_FORMAT = "dd-HH时";

    public static final String DAY_FORMAT = "dd";

    public static final String YEAR_MONTH = "yyyy/MM";

    public static final String YEAR_FORMAT = "yyyy";

    public static final String FORMAT_DATE_MM = "yyyy-MM";

    public static final String SPECALCHAR_DATE_FORMAT = "yyyyMMddHHmmssms";

    public static final String Day_MONTH_YEAR__FORMAT = "dd-MMM-yy";

    public static final String Day_MONTH_YEAR__FORMAT_LONG = "dd-MMM-yy hh24:mm";

    public static final String DB_TABLE_SUFFIX_DATE_FORMAT = "yyyyMMdd";
    public static final String FILE_PATH_DATE_FORMAT = "yyyy/MM/dd";
    public static final String ES_INDEX_FORMAT = "yyyy_MM_dd";
    public static final String ES_MONTH_INDEX_FORMAT = "yyyyMM";

    public static final String FORMAT_DATE_DD_EN = "yyyy年MM月dd HH时mm分";
    public static final String FORMAT_LONG_MS = "yyyyMMdd HHmmss";


    public static String formatDateToES(Date date) {
        return formatDate(date, ES_INDEX_FORMAT);
    }

    public static String formatDateTotpES(Date date) {
        return formatDate(date, SPECAL_DAY_FORMAT);
    }

    public static String formatMonthToES(Date date) {
        return formatDate(date, ES_MONTH_INDEX_FORMAT);
    }

    public static String formatDate(Date value, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        String date = formatter.format(value);
        return date;
    }


    public static String getCurrentDayStr() {
        Date currentDate = Calendar.getInstance().getTime();
        return formatDate(currentDate, DB_TABLE_SUFFIX_DATE_FORMAT);
    }

    public static String getDBDayFormatStr(Date date) {
        return formatDate(date, DB_TABLE_SUFFIX_DATE_FORMAT);
    }


    public static String getCurrentDayFilePathStr() {
        Date currentDate = Calendar.getInstance().getTime();
        return formatDate(currentDate, FILE_PATH_DATE_FORMAT);
    }


    public static String getYesterdayDayFilePathStr() {
        Date yesterday = getYesterDay();
        return formatDate(yesterday, FILE_PATH_DATE_FORMAT);
    }


    public static Date getYesterDay() {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = ca.getTime();
        return yesterday;
    }

    public static Date stringToDBFormatDate(String value) {
        DateFormat formatter = new SimpleDateFormat(DB_TABLE_SUFFIX_DATE_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date stringToLongFormatDate(String value) {
        DateFormat formatter = new SimpleDateFormat(LONG_DATE_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //剪除时分秒
    public static Date stripToShortDay(Date date) {

        return stringToShortDate(dateToShortString(date));

    }


    public static Date getCurDate() {
        DateFormat formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
        String curTime = formatter.format(new Date());
        Date date = null;
        try {
            date = formatter.parse(curTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getCurLongDateTime() {
        DateFormat formatter = new SimpleDateFormat(LONG_DATE_FORMAT);
        String curTime = formatter.format(new Date());
        Date date = null;
        try {
            date = formatter.parse(curTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurDateTime() {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
        String curTime = formatter.format(new Date());
        return curTime;
    }

    public static String getStandardDateTime() {
        DateFormat formatter = new SimpleDateFormat(LONG_DATE_FORMAT);
        String curTime = formatter.format(new Date());
        return curTime;
    }

    public static Date stringToYearMonth(String value) {
        DateFormat formatter = new SimpleDateFormat(YEAR_MONTH_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date toShortDate(Date value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static String dateToSpecialcHARString(Date value) {
        DateFormat formatter = new SimpleDateFormat(SPECALCHAR_DATE_FORMAT);
        String shortDate = formatter.format(value);
        return shortDate;
    }

    public static String formatToDayAndHour(Date value) {
        DateFormat formatter = new SimpleDateFormat(DAY_HOUR_FORMAT);
        String date = formatter.format(value);
        return date;
    }

    public static String formatToDay(Date value) {
        DateFormat formatter = new SimpleDateFormat(DAY_FORMAT);
        String date = formatter.format(value);
        return date;
    }

    public static String formatToYearMonth(Date value) {
        DateFormat formatter = new SimpleDateFormat(YEAR_MONTH);
        String date = formatter.format(value);
        return date;
    }

    public static String formatToYear(Date value) {
        DateFormat formatter = new SimpleDateFormat(YEAR_FORMAT);
        String date = formatter.format(value);
        return date;
    }

    public static Date getYearAndMonth(String value) {
        Calendar calendar = Calendar.getInstance();

        DateFormat formatter = new SimpleDateFormat(YEAR_MONTH_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(value);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    public static Date getDeadLineByLimit(Date date, int limit) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, limit);
        return calendar.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * @return 返回当天的开始时间2008-06-17 00:00:00
     */
    public static Date getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateOfLastWeekStart(Date date) {

        Calendar calender = Calendar.getInstance();
        calender.setFirstDayOfWeek(Calendar.MONDAY);
        calender.setTime(date);
        //calender.set(Calendar.DAY_OF_WEEK, -7);
        calender.add(Calendar.DAY_OF_MONTH, -7);
        calender.set(Calendar.DAY_OF_WEEK, calender.getFirstDayOfWeek());
        return calender.getTime();
    }


    public static String getDateWeekStartStr(Date date, String format) {
        Calendar calender = Calendar.getInstance();
        calender.setFirstDayOfWeek(Calendar.MONDAY);
        calender.setTime(date);
        calender.set(Calendar.DAY_OF_WEEK, calender.getFirstDayOfWeek());
        return formatDate(calender.getTime(), format);
    }

    public static Date getDateOfWeekEnd(Date date) {

        Calendar calender = Calendar.getInstance();
        calender.setFirstDayOfWeek(Calendar.MONDAY);
        calender.setTime(date);
        calender.set(Calendar.DAY_OF_WEEK, calender.getFirstDayOfWeek() + 6);
        return calender.getTime();
    }

    public static String getDateWeekEndStr(Date stringToShortDate) {
        return dateToShortString(getDateOfWeekEnd(stringToShortDate));
    }

    public static String getStartOfCurrentDate() {
        SimpleDateFormat H24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return H24.format(getCurrentDay());
    }

    public static String getEndOfCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        SimpleDateFormat H24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return H24.format(calendar.getTime());
    }


    public static String getStartOfDateStr(String checkedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToShortDate(checkedDate));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        SimpleDateFormat H24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return H24.format(calendar.getTime());
    }

    public static String getEndOfDateStr(String checkedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToShortDate(checkedDate));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        SimpleDateFormat H24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return H24.format(calendar.getTime());
    }


    public static Date getStartOfDate(Date checkedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkedDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndOfDate(Date checkedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkedDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }




	/*
	 *由于oracle日期使用hql查询遇到错误反馈Ora-01830，月份无效，该采用critical方式，原因尚不清楚， 待以后验证，
	 *
	 * public static String getDateString4Oracle(){
		DateFormat formatter = new SimpleDateFormat(Day_MONTH_YEAR__FORMAT,Locale.ENGLISH);
		Calendar calendar = Calendar.getInstance();
		return formatter.format(calendar.getTime());
	}

	public static String getDateString4OracleLong(){
		DateFormat formatter = new SimpleDateFormat(Day_MONTH_YEAR__FORMAT_LONG,Locale.ENGLISH);
		Calendar calendar = Calendar.getInstance();
		return formatter.format(calendar.getTime());
	}*/

    /**
     * 放回24小时制的日期
     *
     * @param date
     * @param @param  date
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getDate
     */
    public static String getDate(Date date) {
        String str = "";
        str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + "";
        return str;
    }

    /**
     * 放回24小时制的日期
     *
     * @param date
     * @param date
     * @return String    返回类型
     * @throws
     * @Title: getDate
     */
    public static String getDate(String date) {
        String str = "";
        try {
            str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)) + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @param date 被添加日时间 2008-06-17 00:00:00
     * @param days 添加多少天 如:1
     * @return 2008-06-18 00:00:00
     */
    public static Date incrementDate(Date date, int days) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days == 0 ? 1 : days);
        return calendar.getTime();
    }


    public static Date getCurrentDayBegin() {
        return getCurrentDay();
    }

    public static Date getCurrentDayEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static String getLastDayOfMonth(String checkedMonth) {
        String result = "";
        try {
            Date date = Calendar.getInstance().getTime();
            if (!StringUtils.isEmpty(checkedMonth)) {
                date = new SimpleDateFormat(YEAR_MONTH_FORMAT).parse(checkedMonth);
            }
            Date lastDateOfMonth = lastDayOfMonth(date);
            result = new SimpleDateFormat(LONG_DATE_FORMAT).format(lastDateOfMonth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Date lastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        //calendar.set(Calendar.MONTH, calendar.ge)
        return calendar.getTime();
    }

    //获得某季度的结束日期
    public static String getQuarterEndDate(int paraYear, int paraSeason) {
        switch (paraSeason) {
            case 1:
                return paraYear + "-03-31";
            case 2:
                return paraYear + "-06-30";
            case 3:
                return paraYear + "-09-30";
            default:
                return paraYear + "-12-31";
        }
    }

    public static String getQuarterByDate(String date) {
        return getQuarterByDate(stringToShortDate(date));
    }

    public static String getQuarterByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        if (month >= 1 && month <= 3) {
            return "一季度";
        } else if (month > 3 && month <= 6) {
            return "二季度";
        } else if (month > 6 && month <= 9) {
            return "三季度";
        } else {
            return "四季度";
        }
    }

    //获取当前时间是第几季度
    public static int getSeason(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        switch (month) {
            case 0:
            case 1:
            case 2:
                return 1;
            case 3:
            case 4:
            case 5:
                return 2;
            case 6:
            case 7:
            case 8:
                return 3;
            default:
                return 4;
        }
    }

    public static String getHalfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        StringBuffer dateStr = new StringBuffer();
        dateStr.append(year);
        if (month <= 6) {
            dateStr.append("-06-30");
        } else {
            dateStr.append("-12-31");
        }
        return dateStr.toString();
    }


    //月份
    public static String getBeforeMonth(String date, int num) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date dates = new SimpleDateFormat(YEAR_MONTH_FORMAT).parse(date);
            calendar.setTime(dates);
            calendar.add(Calendar.MONTH, -num);
            //获取一个月最后一天
            calendar.setTime(calendar.getTime());
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(SHORT_DATE_FORMAT).format(calendar.getTime());
    }

    //小时
    public static String getBeforeHour(String date, int num) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date dates = new SimpleDateFormat(LONG_DATE_FORMAT).parse(date);
            calendar.setTime(dates);
            calendar.add(Calendar.HOUR_OF_DAY, -num);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(LONG_DATE_FORMAT).format(calendar.getTime());
    }

    //年份
    public static String getBeforeYear(String date, int num) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date dates = new SimpleDateFormat(YEAR_FORMAT).parse(date);
            calendar.setTime(dates);
            calendar.add(Calendar.YEAR, -num);
            //获取一年最后一个月
            calendar.setTime(calendar.getTime());
            calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
            //获取一个月最后一天
            calendar.setTime(calendar.getTime());
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(SHORT_DATE_FORMAT).format(calendar.getTime());
    }

    //年份
    public static String getYearFirstDay(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date dates = new SimpleDateFormat(YEAR_FORMAT).parse(date);
            calendar.setTime(dates);
            //获取一年第一个一个月
            calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
            //获取一个月第一天
            calendar.setTime(calendar.getTime());
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(SHORT_DATE_FORMAT).format(calendar.getTime());
    }

    public static String getYearLastDay(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date dates = new SimpleDateFormat(YEAR_FORMAT).parse(date);
            calendar.setTime(dates);
            //获取一年第一个一个月
            calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
            //获取一个月第一天
            calendar.setTime(calendar.getTime());
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(SHORT_DATE_FORMAT).format(calendar.getTime());
    }

    public static Date getCurTime() {
        DateFormat formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
        String curTime = formatter.format(new Date());
        try {
            return formatter.parse(curTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getQuarterOrYear(String value, String type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToShortDate(value));
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if ("year".equals(type)) {
            if (month >= 1 && month <= 6) {
                return year + " 上半年";
            } else {
                return year + " 下半年";
            }
        } else {
            if (month >= 1 && month <= 3) {
                return year + " 一季度";
            } else if (month > 3 && month <= 6) {
                return year + " 二季度";
            } else if (month > 6 && month <= 9) {
                return year + " 三季度";
            } else {
                return year + " 四季度";
            }
        }

    }

    public static String getFirstDayByType(String value, String type) {
        return getFirstDayByType(stringToShortDate(value), type);
    }

    //type：日、月年、周、季度、半年、年
    public static String getFirstDayByType(Date value, String type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if ("week".equals(type)) {
            int i = calendar.get(Calendar.DAY_OF_WEEK);
            if (i == 1)
                calendar.add(Calendar.DATE, -6);
            else
                calendar.add(Calendar.DATE, 2 - i);
        } else if ("month".equals(type)) {
            calendar.add(Calendar.DATE, 1 - calendar.get(Calendar.DATE));
        } else if ("quarter".equals(type)) {
            if (month >= 0 && month < 3) {
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DATE, 1);
            } else if (month > 3 && month <= 6) {
                calendar.set(Calendar.MONTH, 3);
                calendar.set(Calendar.DATE, 1);
            } else if (month > 6 && month <= 9) {
                calendar.set(Calendar.MONTH, 6);
                calendar.set(Calendar.DATE, 1);
            } else {
                calendar.set(Calendar.MONTH, 9);
                calendar.set(Calendar.DATE, 1);
            }
        } else if ("halfYear".equals(type)) {
            if (month >= 0 && month < 6) {
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DATE, 1);
            } else {
                calendar.set(Calendar.MONTH, 6);
                calendar.set(Calendar.DATE, 1);
            }
        } else if ("year".equals(type)) {
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, 1);
        }
        return dateToShortString(calendar.getTime());
    }

    public static String getLastDayByType(String value, String type) {
        return getLastDayByType(stringToShortDate(value), type);
    }

    //type：日、月年、周、季度、半年、年
    public static String getLastDayByType(Date value, String type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        int month = calendar.get(Calendar.MONTH);
        if ("week".equals(type)) {
            int i = calendar.get(Calendar.DAY_OF_WEEK);
            if (i > 1)
                calendar.add(Calendar.DATE, 8 - i);
        } else if ("month".equals(type)) {
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        } else if ("quarter".equals(type)) {
            if (month >= 0 && month < 3) {
                calendar.set(Calendar.MONTH, 2);
                calendar.set(Calendar.DATE, 31);
            } else if (month > 3 && month <= 6) {
                calendar.set(Calendar.MONTH, 5);
                calendar.set(Calendar.DATE, 30);
            } else if (month > 6 && month <= 9) {
                calendar.set(Calendar.MONTH, 8);
                calendar.set(Calendar.DATE, 30);
            } else {
                calendar.set(Calendar.MONTH, 11);
                calendar.set(Calendar.DATE, 31);
            }
        } else if ("halfYear".equals(type)) {
            if (month >= 0 && month < 6) {
                calendar.set(Calendar.MONTH, 30);
                calendar.set(Calendar.DATE, 5);
            } else {
                calendar.set(Calendar.MONTH, 11);
                calendar.set(Calendar.DATE, 31);
            }
        } else if ("year".equals(type)) {
            calendar.set(Calendar.MONTH, 11);
            calendar.set(Calendar.DATE, 31);
        }
        return dateToShortString(calendar.getTime());
    }

    public static int getWithDayAsAfter(String date, String type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToShortDate(date));
        if ("day".equals(type)) {
            return 365 - calendar.get(Calendar.DAY_OF_YEAR);
        } else if ("month".equals(type)) {
            return 11 - calendar.get(Calendar.MONTH);
        } else if ("week".equals(type)) {
            return 52 - calendar.get(Calendar.WEEK_OF_YEAR);
        } else if ("quarter".equals(type)) {
            return 4 - getSeason(calendar.getTime());
        }
        return 0;
    }

    public static int getWithDayAsBefore(String date, String type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToShortDate(date));
        if ("day".equals(type)) {
            return calendar.get(Calendar.DAY_OF_YEAR);
        } else if ("month".equals(type)) {
            return calendar.get(Calendar.MONTH);
        } else if ("week".equals(type)) {
            return calendar.get(Calendar.WEEK_OF_YEAR);
        } else if ("quarter".equals(type)) {
            return getSeason(calendar.getTime());
        }
        return 0;
    }

    private static SimpleDateFormat getFmt(final String fmt) {
        return new SimpleDateFormat(fmt);
    }


    /**
     * 按照指定的格式返回日期的字符串格式
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDateString(final Date date, final String format) {
        SimpleDateFormat fmt = getFmt(format);
        return fmt.format(date);
    }

    public static Date addYear(final Date oldDate, final int i)
            throws ParseException {
        Calendar g = Calendar.getInstance();
        g.setTime(oldDate);
        g.add(Calendar.YEAR, i);

        return g.getTime();
    }

    public static Date addMonth(final Date oldDate, final int i)
            throws ParseException {
        Calendar g = Calendar.getInstance();
        g.setTime(oldDate);
        g.add(Calendar.MONTH, i);

        return g.getTime();
    }

    public static Date addDay(final Date oldDate, final int i) {
        Calendar g = Calendar.getInstance();
        g.setTime(oldDate);
        g.add(Calendar.DAY_OF_MONTH, i);

        return g.getTime();
    }


    /**
     * 子系统新增begin
     */
    public static String getStartOfDate(String checkedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToShortDate(checkedDate));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        SimpleDateFormat H24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return H24.format(calendar.getTime());
    }

    public static String getEndOfDate(String checkedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToShortDate(checkedDate));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        SimpleDateFormat H24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return H24.format(calendar.getTime());
    }

    /**
     * 把字段值转化成SQL语句的需要的字符串格式
     *
     * @param fieldValue 被转化的字段值
     * @return SQL语句的需要的字符串格式
     */
    public static String sqlValue(Object fieldValue) {
        if (fieldValue == null)
            return "NULL";

        String className = fieldValue.getClass().getName();

        if (className.equals("java.lang.String"))
            return "'" + fieldValue.toString().replace("'", "''") + "'";
        else if (className.equals("java.util.Date"))
            return "date_format('" + date2Str((Date) fieldValue, "1") + "','%Y-%m-%d HH:mm:ss')";
        else
            return fieldValue.toString();
    }

    /**
     * 格式化日期时间，返回字符串
     *
     * @param date       被格式化的日期时间变量
     * @param formatType 有3种：1：yyyy-MM-dd HH:mm:ss，2：yyyy-MM-dd,3：yyyy-MM-dd HH:mm
     * @return 格式化后的日期时间字符串
     */
    public static String date2Str(Date date, String formatType) {
        String formatString = "yyyy-MM-dd";

        if (formatType.equalsIgnoreCase("1"))
            formatString = "yyyy-MM-dd HH:mm:ss";
        else if (formatType.equalsIgnoreCase("2"))
            formatString = "yyyy-MM-dd";
        else if (formatType.equalsIgnoreCase("3"))
            formatString = "yyyy-MM-dd HH:mm";

        DateFormat format = new SimpleDateFormat(formatString);
        String result = format.format(date);
        return result;
    }

    /**
     * DateParse:(转换时间). <br/>
     *
     * @param o
     * @return
     * @author wangguohong
     * @since JDK 1.6
     */
    public static String toMString(Object o) {
        String s = null;
        if (o instanceof Date) {
            Date d = (Date) o;
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            s = sf.format(d);
        } else {
            s = o.toString();
        }
        return s;
    }

    public static Date dateToDnsName(String value) {
        DateFormat formatter = new SimpleDateFormat(SPECAL_TIME_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date stringToShortDate(String value) {
        DateFormat formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date stringToLongDate(String value) {
        DateFormat formatter = new SimpleDateFormat(SPECAL_DATE_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String dateToFiveMinutesString(Date value) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE, (minute / 5) * 5);
        calendar.set(Calendar.SECOND, 0);
        return dateToSpecialString(calendar.getTime());
    }

    public static String dateToLongString(Date value) {
        DateFormat formatter = new SimpleDateFormat(SPECAL_LONG_DATE_FORMAT);
        String longDate = formatter.format(value);
        return longDate;
    }

    public static String dateToShortString(Date value) {
        DateFormat formatter = new SimpleDateFormat(SPECAL_DAY_FORMAT);
        String shortDate = formatter.format(value);
        return shortDate;
    }

    public static String dateToShortStyleString(Date value) {
        DateFormat formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
        String shortDate = formatter.format(value);
        return shortDate;
    }

    public static String dateToHourTimeString(Date value) {
        DateFormat formatter = new SimpleDateFormat(SPECAL_HOUR_FORMAT);
        String shortDate = formatter.format(value);
        return shortDate;
    }

    public static String dateToDayTimeString(Date value) {
        DateFormat formatter = new SimpleDateFormat(SPECAL_DAY_FORMAT);
        String shortDate = formatter.format(value);
        return shortDate;
    }

    public static String dateToLastHourTimeString(Date value) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        cal.add(Calendar.HOUR_OF_DAY, -1);
        DateFormat formatter = new SimpleDateFormat(SPECAL_HOUR_FORMAT);
        String shortDate = formatter.format(cal.getTime());
        return shortDate;
    }

    public static String dateToLastDayString(Date value) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        DateFormat formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
        String shortDate = formatter.format(cal.getTime());
        return shortDate;
    }

    public static String dateToLastTwoDayString(Date value) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        cal.add(Calendar.DAY_OF_MONTH, -2);
        DateFormat formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
        String shortDate = formatter.format(cal.getTime());
        return shortDate;
    }

    public static String dateToLastThreeDayString(Date value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        cal.add(Calendar.DAY_OF_MONTH, -4);
        DateFormat formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
        String shortDate = formatter.format(cal.getTime());
        return shortDate;
    }

    public static String dateToSpecialString(Date value) {
        DateFormat formatter = new SimpleDateFormat(SPECAL_DATE_FORMAT);
        String shortDate = formatter.format(value);
        return shortDate;
    }

    public static String dataToLocaleLongString(Date value) {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG);
        String longDate = formatter.format(value);
        return longDate;
    }

    public static String dataToLocaleShortString(Date value) {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);
        String longDate = formatter.format(value);
        return longDate;
    }

    public static String formatToYearAndMonth(Date value) {
        DateFormat formatter = new SimpleDateFormat(YEAR_MONTH_FORMAT);
        String date = formatter.format(value);
        return date;
    }

    public static Date firstDayOfYearMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static Date lastDayOfYearMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.getMaximum(Calendar.DATE));
        return calendar.getTime();
    }

    public static Date lastFiveMinutes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -5);
        return calendar.getTime();
    }


    public static Date nextYearAndMonth(String value) {
        Calendar calendar = Calendar.getInstance();

        DateFormat formatter = new SimpleDateFormat(YEAR_MONTH_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(value);
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    public static Date nextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }


    public static Date lastDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();

    }


    public static Date[] calculateWeekSect(Date begin, Date end) {
        Calendar calendar = Calendar.getInstance();
        Date[] weekDate = new Date[2];
        calendar.setTime(begin);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        if (calendar.getTime().after(begin)) {
            calendar.add(Calendar.DATE, -7);
        }
        weekDate[0] = calendar.getTime();

        calendar.setTime(end);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        if (calendar.getTime().before(end)) {
            calendar.add(Calendar.DATE, 7);
        }
        weekDate[1] = calendar.getTime();

        return weekDate;
    }

    public static Date fromLong(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.getTime();
    }


    public static Date getUTCTime(Date date) {
        //Calendar calendar = Calendar.getInstance();
        TimeZone zone = TimeZone.getTimeZone("GMT");
        Calendar calendar = new GregorianCalendar(zone);

        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.HOUR, 8);
        // calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendar.getTime();
    }

    /**
     * @return 返回现在时间的一个整数，这个与mysql数据库time_timestamp(now())相对应
     */
    public static int getCurrentTime() {
        return Long.valueOf(System.currentTimeMillis() / 1000).intValue();
    }


    public static String dateToString(Date dateTime, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(dateTime);
    }

    //获取当前时间是第几季度
    public static int getQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        switch (month) {
            case 0:
            case 1:
            case 2:
                return 1;
            case 3:
            case 4:
            case 5:
                return 2;
            case 6:
            case 7:
            case 8:
                return 3;
            default:
                return 4;
        }
    }

    //获得当前时间是第几周
    public static int getWeekNum(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    //日期
    public static String getBeforeDay(String date, int num) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date dates = new SimpleDateFormat(SHORT_DATE_FORMAT).parse(date);
            calendar.setTime(dates);
            calendar.add(Calendar.DATE, -num);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(SHORT_DATE_FORMAT).format(calendar.getTime());
    }

    public static String getBeforeDay(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -num);
        return new SimpleDateFormat(SHORT_DATE_FORMAT).format(calendar.getTime());
    }

    public static Date getAfterDay(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(date);
            calendar.add(Calendar.DATE, num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    public static String getRunTime(Date effectTime, Date pastTime) {
        String runTime = "";
        Date currentDate = new Date();
        long time = currentDate.getTime() - effectTime.getTime();
        if (pastTime.compareTo(currentDate) < 0) {
            time = pastTime.getTime() - effectTime.getTime();
        }
        time = time / 1000;
        if (time > 0) {
            int day = new Long(time / 86400).intValue();
            int hour = new Long((time - day * 86400) / 3600).intValue();
            int munite = new Long((time - day * 86400 - hour * 3600) / 60).intValue();
            int seconds = new Long(time - day * 86400 - hour * 3600 - munite * 60).intValue();
            runTime = day + "天" + hour + "时" + munite + "分" + seconds + "秒";
        } else {
            runTime = "未运行";
        }
        return runTime;
    }


    //时间转换成年月日格式
    public static String dateToYMDStr(Date date) {
        DateFormat df1 = new SimpleDateFormat(SHORT_DATE_FORMAT);
        String time = df1.format(date);
        return time;
    }


    public static Date stringToDateMHS(Date dateTime) {
        String value = dateToYMDStr(dateTime) + " 00:00:00";
        SimpleDateFormat dateformat1 = new SimpleDateFormat(LONG_DATE_FORMAT);
        try {
            dateTime = dateformat1.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static Date stringToDateMDS(Date dateTime) {
        String value = dateToYMDStr(dateTime) + " 23:59:59";
        SimpleDateFormat dateformat1 = new SimpleDateFormat(LONG_DATE_FORMAT);
        try {
            dateTime = dateformat1.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }


    public static String dateToYMDENStr(Date date) {
        DateFormat df1 = new SimpleDateFormat(FORMAT_DATE_DD_EN);
        String time = df1.format(date);
        return time;
    }

    public static String dateToYMDHMSStr(Date date) {
        DateFormat df1 = new SimpleDateFormat(FORMAT_LONG_MS);
        String time = df1.format(date);
        return time;
    }

    public static Date stringToDate(String date, String format) {
        DateFormat df1 = new SimpleDateFormat(format);
        Date dateTime = new Date();
        try {
            dateTime = df1.parse(date);
        } catch (ParseException e) {
            try {
                dateTime = new SimpleDateFormat(SPECAL_DAY_FORMAT).parse(date);
            } catch (ParseException e1) {
                throw new RuntimeException(e1);
            }
        }
        return dateTime;
    }

    public static Date shortStrToDate(String date) {
        DateFormat df1 = new SimpleDateFormat(SHORT_DATE_FORMAT);
        Date dateTime = new Date();
        try {
            dateTime = df1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static Date longStrToDate(String date) {
        DateFormat df1 = new SimpleDateFormat(FORMAT_LONG_MS);
        Date dateTime = new Date();
        try {
            dateTime = df1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    //当天日期最早时间00:00:00
    public static Date getCurrentStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return (Date) calendar.getTime().clone();
    }

    //当天日期最早时间23:59:59
    public static Date getCurrentEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.HOUR_OF_DAY, 23);
        calendar.set(calendar.MINUTE, 59);
        calendar.set(calendar.SECOND, 59);
        return (Date) calendar.getTime().clone();
    }

    public static Date validateFormatDate(String value) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
        formatter.setLenient(false);
        Date date = null;
        date = formatter.parse(value);
        return date;
    }


    //获取本周星期一的日期
    public static String getMondayDateString(String currentDateStr, String format) {
        if (StringUtils.isBlank(format)) {
            format = SHORT_DATE_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        if (StringUtils.isNotBlank(currentDateStr)) {
            try {
                Date currentDate = sdf.parse(currentDateStr);
                cal.setTime(currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return sdf.format(cal.getTime());
    }

}
