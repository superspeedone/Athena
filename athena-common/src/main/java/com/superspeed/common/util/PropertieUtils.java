package com.superspeed.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取参数文件中对应参数值方法
 * @ClassName: PropertieUtils
 * @Description: 获取参数文件中对应参数值方法
 * @author ynChen
 * @date 2017-5-26 下午2:43:17
 * @version 1.0
 */
public class PropertieUtils {
	
	private static Properties  properties;
	
	private static final Logger logger = LoggerFactory.getLogger(PropertieUtils.class);
	
	static {
		InputStream inStream = PropertieUtils.class.getClassLoader().getResourceAsStream("config.properties");
		properties = new Properties();
		try {
			properties.load(inStream);
		} catch (IOException e) {
			properties=null;
			logger.error("config.properties配置文件加载失败,"+e.getMessage());
		}finally{
			if(null !=inStream){
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	/**
	 * 根据key获取app.properties配置文件值
	 * @author ynChen
	 * @Time 2015-12-7 下午3:22:08
	 * @param key
	 * @return String
	 */
	public static String getValue(String key){
		String value = null;
		if(null != key && !"".equals(key)){
			value = properties.getProperty(key);
		}
		return value;
	}

}
