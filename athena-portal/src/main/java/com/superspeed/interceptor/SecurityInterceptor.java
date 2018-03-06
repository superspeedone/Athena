package com.superspeed.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SpringMVC拦截器
 * @ClassName: SecurityInterceptor
 * @Description: SpringMVC拦截器
 * @author xc.yanww
 * @date 2017/10/13 16:19
 */
public class SecurityInterceptor implements HandlerInterceptor {

	private static final Logger logger = LogManager.getLogger(SecurityInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj) throws Exception {
		logger.info(this.getClass().getName());
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj, ModelAndView view) throws Exception {

	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception e) throws Exception {

	}

}
