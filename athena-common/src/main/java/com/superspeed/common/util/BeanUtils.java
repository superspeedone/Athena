package com.superspeed.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * javabean工具类
 * @ClassName: BeanUtils
 * @Description: Map转实体  实体转Map
 * @author xc.yanww
 * @date 2017-5-26 下午3:35:01
 * @version 1.0
 */
public class BeanUtils {
	private static final String INT = "int";
	private static final String BIGDECIMAL = "java.math.BigDecimal";

	/**
	 * 实体bean转Map
	 * @author xc.yanww
	 * @date 2016-9-29 上午9:24:09
	 * @param o 实体对象
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public static Map<String, Object> beanToMap(Object o) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = null;
		PropertyDescriptor pd = null;
		Method getMethod = null;
		Object fieldValue = null;
		//获取bea类类型名称n
		//String clzName = o.getClass().getSimpleName();
		fields = o.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!"serialVersionUID".equals(field.getName())) {
				//field.setAccessible(true);
				//属性名称
				String fieldName = field.getName();
				//属性值
				//Object fieldValue = field.get(o);
				//获取属性描述器
				pd = new PropertyDescriptor(field.getName(), o.getClass());  
	            getMethod = pd.getReadMethod();//获得get方法  
	            fieldValue = getMethod.invoke(o);//执行get方法返回一个Object  
				map.put(fieldName.toUpperCase(), fieldValue);
			}
		}

		return map;
	}

	/**
	 * Map转实体bean
	 * @author xc.yanww
	 * @date 2016-9-29 上午9:24:53
	 * @param map HashMap对象
	 * @param o 实体对象
	 * @return Object
	 * @throws Exception
	 */
	public static Object mapToBean(Map<String, Object> map, Object o) throws Exception {
		if (!map.isEmpty()) {  
			PropertyDescriptor pd = null;
			Method setMethod = null;
            for (String k : map.keySet()) {  
                Object v = "";  
                if (!k.isEmpty()) {  
                    v = map.get(k);  
                }  
                Field[] fields = null;  
                fields = o.getClass().getDeclaredFields(); 
                //获取bean类类型名称
                //String clzName = o.getClass().getSimpleName(); 
                for (Field field : fields) {  
                    int mod = field.getModifiers();  
                    if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){  
                        continue;  
                    }  
                    if (field.getName().toUpperCase().equals(k) && v != null) { 
                        //field.setAccessible(true);  
                    	pd = new PropertyDescriptor(field.getName(), o.getClass());  
        	            setMethod = pd.getWriteMethod();//获得get方法  
                        if (INT.equals(field.getType().getName())) {
                        	setMethod.invoke(o, Integer.valueOf(String.valueOf(v)));//执行set方法
						} else if (BIGDECIMAL.equals(field.getType().getName())) {
							setMethod.invoke(o, new BigDecimal(String.valueOf(v)).
                        			setScale(2, BigDecimal.ROUND_HALF_UP));
						} else {
							setMethod.invoke(o, v);
						}
                    } 
                }  
            }  
        }  
        return o;  
	}
	
	/**
	 * <p> 通过反射  将Map 转为 Bean 制定浮点型精度 </p>
	 * @author  xc.chenfei01
	 * @date 下午3:42:47  2017-2-26
	 * @param map HashMap对象
	 * @param object 实体类对象
	 * @param scaleLength 浮点数精度
	 * @return Object 接收类型对象
	 * @throws Exception
	 */
	public static Object mapToBean(Map<String, Object> map, Object object,int scaleLength) throws Exception {
		if (map.isEmpty() || object == null) {
			return null;
		}
		PropertyDescriptor pd = null;
		Method setMethod = null;
		// 根据类类型 获取所有成员变量
		Field[] fieldArray = object.getClass().getDeclaredFields();
		 // 获取bean类类型名称 String clzName = o.getClass().getSimpleName();
		if (fieldArray == null || fieldArray.length == 0) {
			return null;// 无成员变量不做转换
		}
		for (String key : map.keySet()) {
			Object value = map.get(key);
			if (value == null) {
				continue;
			}
			for (Field field : fieldArray) {
				int mod = field.getModifiers();
				if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					continue;
				}
				if (field.getName().toUpperCase()
						.equals(key.replace("_", "").toUpperCase())) {
					//field.setAccessible(true);
					pd = new PropertyDescriptor(field.getName(), object.getClass());  
    	            setMethod = pd.getWriteMethod();//获得get方法  
					// 值为String 时按照bean字段转型
					if (field.getType().equals(String.class)) {// String
						if (!(value instanceof String)) {
							value = value + "";
						}
					} else if (field.getType().equals(Integer.class)) {// 整型
						if (!(value instanceof Integer)) {
							value = Integer.valueOf(value + "");
						}
					} else if (field.getType().equals(BigDecimal.class)) {// 大整型
						if (!(value instanceof BigDecimal)) {
							value = new BigDecimal(String.valueOf(value))
									.setScale(scaleLength,BigDecimal.ROUND_HALF_UP);
						}
					} else if (field.getType().equals(Double.class)) {// 浮点型
						if (!(value instanceof Double)) {
							value = new Double(String.valueOf(value));
						}
					} else if (field.getType().equals(Long.class)) {// 长整型
						if (!(value instanceof Long)) {
							value = new Long(String.valueOf(value));
						}
					} else if (field.getType().equals(Short.class)) {// 短整型
						if (!(value instanceof Short)) {
							value = new Short(String.valueOf(value));
						}
					} else if (field.getType().equals(Date.class)) {// 日期型
						if (!(value instanceof Date)) {
							SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							value = sd.parse(value + "");
						}
					}
					setMethod.invoke(object, value);
				}
			}
		}
		return object;
	}
	
}
