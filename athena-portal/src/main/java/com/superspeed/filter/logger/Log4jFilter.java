package com.superspeed.filter.logger;

import com.superspeed.common.util.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * log4j拦截器
 * @ClassName: Log4jFilter
 * @Description: log4j拦截器
 * @author xc.yanww
 * @date 2017-08-01 上午9:26:33
 */
public class Log4jFilter extends HttpServlet implements Filter {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(Log4jFilter.class);

	@Override
	public void init(FilterConfig filterConfig) {
		logger.info("Log4j filter is initialing...");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		HttpServletRequest req = null;

		//请求ID
		Long reqId = new Long(0);
		//客户端ip地址
		String clientIp = "-";
		//回话id
		String sessionId = "-";
		//请求是否结束
		boolean isFist = false;
		//请求开始时间戳
		long t1 = 0;
		//请求结束时间戳
		long t2 = 0;

		try {
			req = (HttpServletRequest) request;
			clientIp = ServletUtils.getIpAddr(req);
			//获取远程客户端ip和请求id
			if (null == req.getAttribute("_log42jfilter_reqid")) {
				reqId = new Long(req.hashCode() + System.currentTimeMillis());
				req.setAttribute("_log42jfilter_reqid", reqId);
				isFist = true;
			} else {
				reqId = (Long) req.getAttribute("_log42jfilter_reqid");
				isFist = false;
			}
			//获取sessionId
			HttpSession session = req.getSession();
			if (null != session) {
				sessionId = session.getId();
			}
			//请求参数存放对象
			StringBuffer paramsBuffer = new StringBuffer();
			Enumeration<String> paramNames = request.getParameterNames();
			//循环遍历所有request域中所有请求参数
			String key;
			String value;
			while (paramNames.hasMoreElements()) {
				// 遍历请求参数
				key = paramNames.nextElement();
				value = request.getParameter(key);
				paramsBuffer.append(key).append("=").append(value);
				if (paramNames.hasMoreElements()) {
					paramsBuffer.append(",");
				}
			}
			if (StringUtils.isBlank(paramsBuffer)) {
				// 如果请求参数为空,返回url路径后面的查询字符串
				paramsBuffer.append(req.getQueryString());
			}
			/**
			 * The MDC is managed on a per thread basis. A child thread automatically 
			 * inherits a copy of the mapped diagnostic context of its parent. 
			 */
			ThreadContext.put("reqId", String.valueOf(reqId));
			ThreadContext.put("clientIp", clientIp);

			logger.info("Log4j2Filter start. SessionId=[" + sessionId + "] Uri=[" + req.getRequestURI()
					+ "] Method=[" + req.getMethod() + "] Qs=[" + paramsBuffer.toString() + "]");
			
			t1 = System.currentTimeMillis();
			//继续执行责任链
			chain.doFilter(request, response);
		} catch (Exception e) {
			logger.info("Log4j2 filter occurs exception", e);
		} finally {
			t2 = System.currentTimeMillis();
			// 请求处理消耗时间
			long during = t2 - t1;
			try {
				logger.info("Log4j2Filter end. During=[" + during + "ms]");
				// 嵌套最外层的filter执行清理动作
				if (isFist) {
					ThreadContext.remove("reqId");
					ThreadContext.remove("clientIp");
					ThreadContext.remove("userName");
				}
			} catch (Exception e) {
				logger.error("Exception occurred when log4j2 filter execute cleaning", e);
			}
		}
	}

}
