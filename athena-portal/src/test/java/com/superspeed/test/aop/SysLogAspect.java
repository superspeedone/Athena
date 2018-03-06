package com.superspeed.test.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * 注解实现aop
 */
/*@Aspect
@Component*/
public class SysLogAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
	
	/** 开始时间  */
	private long startTime = 0L;
	/** 结束时间  */
	private long endTime = 0L;

	//配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
	@Pointcut("within(@org.springframework.stereotype.Controller *)")
	public void aspect() {}
	
	/*
	 * 配置前置通知,使用在方法aspect()上注册的切入点
	 * 同时接受JoinPoint切入点对象,可以没有该参数
	 */
	@Before("aspect()")
	public void doBeforeInServiceLayer(JoinPoint joinPoint) {
		//logger.debug("doBeforeInServiceLayer");
		startTime = System.currentTimeMillis();
	}

	//配置后置通知,使用在方法aspect()上注册的切入点
	@After("aspect()")
	public void doAfterInServiceLayer(JoinPoint joinPoint) {
		//logger.debug("doAfterInServiceLayer");
	}
	
	//配置环绕通知,使用在方法aspect()上注册的切入点
	@SuppressWarnings("unchecked")
	@Around("aspect()")
	public Object recordSysLog(ProceedingJoinPoint joinPoint) throws Throwable {

		// 请求的方法名
		String strMethodName = joinPoint.getSignature().getName();
		// 请求的类名
		String strClassName = joinPoint.getTarget().getClass().getName();
		// 请求的参数
		Object[] params = joinPoint.getArgs();
		StringBuffer bfParams = new StringBuffer();
		Enumeration<String> paraNames = null;
		HttpServletRequest request = null;
		request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		//循环遍历所有request域中所有请求参数
		if (params != null && params.length > 0) {
			paraNames = request.getParameterNames();
			String key;
			String value;
			while (paraNames.hasMoreElements()) {
				// 遍历请求参数
				key = paraNames.nextElement();
				value = request.getParameter(key);
				bfParams.append(key).append("=").append(value).append("&");
			}
			if (StringUtils.isBlank(bfParams)) {
				// 如果请求参数为空,返回url路径后面的查询字符串
				bfParams.append(request.getQueryString());
			}
		}
        //打印方法调用日志（类名，方法，参数）
		String strMessage = String.format("[类名]:%s,[方法]:%s,[参数]:%s", strClassName, strMethodName, bfParams.toString());

		// 环绕通知 ProceedingJoinPoint执行proceed方法的作用是让目标方法执行，这也是环绕通知和前置、后置通知方法的一个最大区别。
		Object result = joinPoint.proceed();
		endTime = System.currentTimeMillis();
		logger.debug("{},result={},耗时：{}ms", new Object[] { strMessage, result,  endTime - startTime });
		
		// 判断是否需要日记记录
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		// 插入日志到数据库
//		SysOptLog sysLog = method.getAnnotation(SysOptLog.class);
//		if (sysLog == null || sysLog.isLog()) {
//		}
		return result;
	}
}
