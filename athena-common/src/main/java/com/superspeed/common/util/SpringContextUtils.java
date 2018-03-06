package com.superspeed.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring应用上下文获取工具类
 * @ClassName: SpringContextUtils
 * @Description: Spring应用上下文获取工具类
 * @author ynChen
 * @date 2017-5-26 下午3:36:52
 * @version 1.0
 */
public class SpringContextUtils implements ApplicationContextAware{

	/** Spring应用上下文环境  */ 
    private static ApplicationContext applicationContext;  
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		 SpringContextUtils.applicationContext = applicationContext;  
	}

	 /** 
      * 获取对象
      * @author ynChen
      * @time 2017-2-20 上午11:29:50
      * @param name bean名称
      * @return Object
      * @throws BeansException
      */
    public static Object getBean(String name) throws BeansException {  
        return applicationContext.getBean(name);  
    } 
    
    
    /**
     * 获取指定对象
     * @author ynChen
     * @time 2017-2-20 上午11:29:50
     * @param name bean名称
     * @param cls bean类类型
     * @return Object
     * @throws BeansException
     */
	public static Object getBean(String name, Class<?> cls) throws BeansException {  
        return applicationContext.getBean(name,cls);  
    }  
    
}
