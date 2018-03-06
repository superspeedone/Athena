package com.superspeed.interceptor;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义统一异常拦截
 * @author xc.yanww
 * @ClassName: CustomExceptionResolver
 * @Description: 自定义统一异常拦截
 * @date 2017/10/13 16:14
 */
public class CustomExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Logger logger = LogManager.getLogger(CustomExceptionResolver.class);


    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object
            handler, Exception ex) {

        String viewName = determineViewName(ex, request);
        if (viewName != null) {
            if (!(request.getHeader("accept").indexOf("application/json") > -1 || request.getHeader
                    ("X-Requested-With") != null)) {
                // 如果不是异步请求
                Integer statusCode = determineStatusCode(request, viewName);
                if (statusCode != null) {
                    applyStatusCodeIfPossible(request, response, statusCode);
                }
                return getModelAndView(viewName, ex, request);
            } else {
                ModelAndView mv = new ModelAndView();
                try {
                    // 返回是错误
                    Map<String, Object> jsonMap = new HashMap<String, Object>();
                    jsonMap.put("errorCode", "500");
                    jsonMap.put("errorMsg", ex.getMessage());
                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(jsonMap));
                    writer.close();
                } catch (Exception e) {
                    logger.error("doResolveException", "系统异常！", e);
                }
                return mv;
            }
        } else {
            return null;
        }
    }
}
