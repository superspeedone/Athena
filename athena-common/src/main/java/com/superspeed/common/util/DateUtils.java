package com.superspeed.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @ClassName: DateUtils
 * @Description: 提供一些日期通用处理的方法 
 * @author xc.yanww
 * @date 2017-5-26 下午3:34:48
 * @version 1.0
 */
public class DateUtils{
	
	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
	
	/** 毫秒 */
	public static final long MS = 1;
	/** 每秒钟的毫秒数 */
	public static final long SECOND_MS = MS * 1000;
	/** 每分钟的毫秒数 */
	public static final long MINUTE_MS = SECOND_MS * 60;
	/** 每小时的毫秒数 */
	public static final long HOUR_MS = MINUTE_MS * 60;
	/** 每天的毫秒数 */
	public static final long DAY_MS = HOUR_MS * 24;
	/** 每月的毫秒数 */
	public static final long MONTH_MS = DAY_MS * 30;
	/** 每年的毫秒数 */
	public static final long YEAR_MS = MONTH_MS * 12;

	/** 标准日期（不含时间）格式化器 */
	private static final String NORM_DATE_FORMAT = new String("yyyy-MM-dd");
	/** 标准日期时间格式化器 */
	private static final String NORM_DATETIME_FORMAT = new String("yyyy-MM-dd HH:mm:ss");
	
	/** Calendar中的天常量 */
	private static final int DAY_OF_MONTH = Calendar.DAY_OF_MONTH;
	/** Calendar中的天常量 */
	private static final int DAY_OF_YEAR = Calendar.YEAR;

	/** 私有构造方法，防止外部实例化 */
	private DateUtils() { }
	
	/**
	 * 获取当前时间戳(10位)
	 * @author xc.yanww
	 * @date 2017/8/29 16:38
	 * @return  时间戳
	 */
	public static String getNowTimeStamp() {
		return getNowTimeStamp(10);
	}
	
	/**
	 * 获取当前时间戳
	 * @author xc.yanww
	 * @date 2017/8/29 16:38
	 * @param length 时间戳长度
	 * @return 时间戳
	 */
	public static String getNowTimeStamp(int length) {
		return length > 0 ? String.valueOf(new Date().getTime()).substring(0, length) : null;
	}

	/**
	 * 当前时间，格式 yyyy-MM-dd HH:mm:ss
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @return 当前时间的标准形式字符串
	 */
	public static String now() {
		return formatDateTime(new Date());
	}
	
	/**
	 * 格式 yyyy-MM-dd HH:mm:ss
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param date 被格式化的日期
	 * @return 格式化后的日期
	 */
	public static String formatDateTime(Date date) {
		return new SimpleDateFormat(NORM_DATETIME_FORMAT).format(date);
	}

	/**
	 * 当前日期，格式 yyyy-MM-dd
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @return 当前日期的标准形式字符串
	 */
	public static String today() {
		return formatDate(new Date());
	}

	/**
	 * 根据特定格式格式化日期
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param date 被格式化的日期
	 * @param format 格式
	 * @return 格式化后的字符串
	 */
	public static String format(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 格式 yyyy-MM-dd
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param date 被格式化的日期
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date) {
		return new SimpleDateFormat(NORM_DATE_FORMAT).format(date);		
	}

	/**
	 * 将特定格式的日期转换为Date对象
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param dateString 特定格式的日期
	 * @param format 格式，例如yyyy-MM-dd
	 * @return 日期对象
	 */
	public static Date parse(String dateString, String format) {
		try {
			return (new SimpleDateFormat(format)).parse(dateString);
		} catch (ParseException e) {
			logger.error("Parse" + dateString + " with format " + format + " error!", e);
		}
		return null;
	}

	/**
	 * 格式yyyy-MM-dd HH:mm:ss
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param dateString 标准形式的时间字符串
	 * @return 日期对象
	 */
	public static Date parseDateTime(String dateString) {
		try {
			return new SimpleDateFormat(NORM_DATETIME_FORMAT).parse(dateString);
		} catch (ParseException e) {
			logger.error("Parse " + dateString + " with format " + new SimpleDateFormat(NORM_DATETIME_FORMAT).toPattern() + " error!", e);
		}
		return null;
	}

	/**
	 * 格式yyyy-MM-dd
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param dateString  标准形式的日期字符串
	 * @return  标准形式的日期字符串
	 */
	public static Date parseDate(String dateString) {
		try {
			return new SimpleDateFormat(NORM_DATE_FORMAT).parse(dateString);
		} catch (ParseException e) {
			logger.error("Parse " + dateString + " with format " + new SimpleDateFormat(NORM_DATE_FORMAT).toPattern() + " error!", e);
		}
		return null;
	}

	/**
	 * 获取指定日期偏移指定时间后的时间
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param date 基准日期
	 * @param calendarField 偏移的粒度大小（小时、天、月等）使用Calendar中的常数
	 * @param offsite 偏移量，正数为向后偏移，负数为向前偏移
	 * @return 偏移后的日期
	 */
	public static Date getOffsiteDate(Date date, int calendarField, int offsite) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(calendarField, offsite);
		return cal.getTime();
	}
	
	/**
	 * 获取当前日期偏移指定时间后的时间
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param calendarField 偏移的粒度大小（小时、天、月等）使用Calendar中的常数
	 * @param offsite 偏移量，正数为向后偏移，负数为向前偏移
	 * @return 偏移后的日期
	 */
	public static Date getOffsiteDate(int calendarField, int offsite) {
		return getOffsiteDate(new Date(), calendarField, offsite);
	}
	
	/**
	 * 获取当前日期偏移指定天后的时间(偏移的粒度大小 :天)
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param offsite 偏移量，正数为向后偏移，负数为向前偏移
	 * @return 偏移后的日期
	 * 
	 */
	public static Date getOffsiteDayNow(int offsite) {
		return getOffsiteDate(new Date(), DAY_OF_MONTH, offsite);
	}
	
	/**
	 * 获取当前日期偏移指定年后的时间(偏移的粒度大小 :年)
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param offsite 偏移量，正数为向后偏移，负数为向前偏移
	 * @return 偏移后的日期
	 */
	public static Date getOffsiteYearNow(int offsite) {
		return getOffsiteDate(new Date(), DAY_OF_YEAR, offsite);
	}
	
	/**
	 * 获取时间差天数
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @return 时间差
	 */
	public static final long getDiffDays(Date startTime, Date endTime) {
		return (startTime.getTime() - endTime.getTime()) / DAY_MS;
	}
	
	/**
	 * 获取时间差秒
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @return 时间差
	 */
	public static final long getDiffSeconds(Date startTime, Date endTime) {
		return (startTime.getTime() - endTime.getTime()) / SECOND_MS;
	}

	/**
	 * 判断两个日期相差的时长<br/>(列：1年前7月25日)
	 * 返回 minuend - subtrahend 的差
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param subtrahend 减数日期
	 * @param minuend 被减数日期
	 * @return 日期差
	 */
	public static String dateDiff(Date subtrahend, Date minuend) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(subtrahend);
		long diff = minuend.getTime() - subtrahend.getTime();
		if(diff <= HOUR_MS)
			return diff / MINUTE_MS + "分钟前";
		else if(diff <= DAY_MS)
			return diff / HOUR_MS + "小时" + (diff % HOUR_MS / MINUTE_MS) + "分钟前";
		else if(diff <= DAY_MS * 2)
			return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"+ calendar.get(Calendar.MINUTE) + "分";
		else if(diff <= DAY_MS * 3)
			return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"+ calendar.get(Calendar.MINUTE) + "分";
		else if(diff <= MONTH_MS)
			return diff / DAY_MS + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点"+ calendar.get(Calendar.MINUTE) + "分";
		else if(diff <= YEAR_MS)
			return diff / MONTH_MS + "个月" + (diff % MONTH_MS) / DAY_MS + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点"+ calendar.get(Calendar.MINUTE) + "分";
		else
			return diff / YEAR_MS + "年前" + (calendar.get(Calendar.MONTH) + 1) + "月"+ calendar.get(Calendar.DATE) + "日";
	}
	
	/**
	 * 距离截止日期还有多长时间
	 * @author xc.yanww
	 * @date 2017/8/29 16:39
	 * @param date
	 * @return String
	 */
	public static String fromDeadline(Date date) {
		long deadline = date.getTime();
		long now = new Date().getTime();
		long remain = deadline - now;
		if (remain <= HOUR_MS)
			return "只剩下" + remain / MINUTE_MS + "分钟";
		else if (remain <= DAY_MS)
			return "只剩下" + remain / HOUR_MS + "小时"
					+ (remain % HOUR_MS / MINUTE_MS) + "分钟";
		else {
			long day = remain / DAY_MS;
			long hour = remain % DAY_MS / HOUR_MS;
			long minute = remain % DAY_MS % HOUR_MS / MINUTE_MS;
			return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
		}
	}
}
