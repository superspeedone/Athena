package com.superspeed.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 校验工具类
 * @ClassName: ValidUtils
 * @Description: 提供一些对象有效性校验的方法
 * @author xc.yanww
 * @date 2017-07-14 上午9:58:21
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ValidUtils {

	/**
	 * 数字字母组合正则表达式
	 */
	public static final String NUM_CHAR_PATTEN = "[a-zA-Z0-9]+";

	private ValidUtils() { }
	
	/**
	 * 判断字符串是否是符合指定格式的时间
	 * @author xc.yanww
	 * @date 2017-08-04 上午9:36:31
	 * @param date 时间字符串
	 * @param format 时间格式
	 * @return 是否符合   true:符合  false:不符合
	 */
	public static boolean isDate(String date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.parse(date);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断字符串是否为空
	 * @author xc.yanww
	 * @date 2017-08-04 上午9:29:57
	 * @param src 源字符串
	 * @return true:为空, false:不为空
	 */
	public static boolean isEmpty(String src) {
		return (null == src || "".equals(src.trim()));
	}

	/**
	 * 判断一组字符串是否有空串
	 * @author xc.yanww
	 * @date 2017-08-04 上午9:37:21
	 * @param src 源字符串
	 * @return true:有, false:没有
	 */
	public static boolean isEmpty(String... src) {
		for (String s : src) {
			if (isEmpty(s)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 判断一个对象是否为空
	 * @author xc.yanww
	 * @date 2017-08-04 上午9:48:13
	 * @param obj 源对象
	 * @return true:为空, false:不为空
	 */
	public static boolean isEmpty(Object obj) {
		return (null == obj);
	}

	/**
	 * 判断List对象是否为空
	 * @author xc.yanww
	 * @date 2017-08-04 上午9:50:00
	 * @param objs List对象
	 * @return true:为空, false:不为空
	 */
	public static boolean isEmpty(List list) {
		if (list != null && list.size() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断集合的有效性
	 * @author xc.yanww
	 * @date 2017-08-04 上午9:52:53
	 * @param col 集合对象
	 * @return true:为空, false:不为空
	 */
	public static boolean isEmpty(Collection col) {
		return (col == null || col.isEmpty());
	}

	/**
	 * 判断一组集合是否有效
	 * @author xc.yanww
	 * @date 2017-08-04 上午9:53:25
	 * @param cols
	 * @return true:为空, false:不为空
	 */
	public static boolean isEmpty(Collection... cols) {
		for (Collection c : cols) {
			if (isEmpty(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断map是否有效
	 * @author xc.yanww
	 * @date 2017-08-04 上午9:56:27
	 * @param map map对象
	 * @return true:为空, false:不为空
	 */
	public static boolean isEmpty(Map map) {
		return (map == null || map.isEmpty());
	}

	/**
	 * 判断一组map是否有空map对象
	 * @author xc.yanww
	 * @date 2017-08-04 上午9:57:07
	 * @param maps 需要判断map
	 * @return true:有, false:没有
	 */
	public static boolean isEmpty(Map... maps) {
		for (Map m : maps) {
			if (isEmpty(m)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断短链接是否是字母数字组合
	 * @author xc.zhengsh
	 * @date 2017-7-28 上午9:30:33
	 * @param url 短链接
	 * @return boolean true:是  false:否
	 */
	public static boolean isNumChar(String url) {
		return url.matches(NUM_CHAR_PATTEN);
	}
}
